package com.sofi.coupons.logic;

import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sofi.coupons.beans.LoginInput;
import com.sofi.coupons.beans.PasswordChange;
import com.sofi.coupons.beans.UserLoginData;
import com.sofi.coupons.beans.data.CacheLoginData;
import com.sofi.coupons.beans.dto.UserDto;
import com.sofi.coupons.beans.dto.UserInfoDto;
import com.sofi.coupons.dao.IUsersDao;
import com.sofi.coupons.entities.CompanyEntity;
import com.sofi.coupons.entities.UserEntity;
import com.sofi.coupons.enums.ExceptionType;
import com.sofi.coupons.enums.UserType;
import com.sofi.coupons.exceptions.ApplicationException;

@Controller
public class UsersController {

	private final String SALT = "*01100011C01100001A01101011K01100101E=404*";
	@Autowired
	private IUsersDao usersDao;
	@Autowired
	private CacheController cacheController;
	@Autowired
	private CompaniesController companiesController;

	///////////////////////////////////////////////
	//Public methods that call validation methods//
	///////////////////////////////////////////////

	public long createUser (UserDto user, HttpServletRequest request) throws ApplicationException {
		createUserValidation(user, request);
		//Encrypting users password so no one could get it from the data base
		user.setPassword(String.valueOf(user.getPassword().hashCode())+SALT);
		CompanyEntity companyEntity = companiesController.getCompanyEntity(user.getCompanyId());
		UserEntity userEntity = new UserEntity(user, companyEntity);
		try {
			usersDao.save(userEntity);
		}catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to create user: " + user);
		}
		return userEntity.getId();
	}

	public UserInfoDto getUser (long id) throws ApplicationException {
		UserInfoDto user;
		try {
			user = usersDao.getUser(id); 
		}catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to get user by ID: " + id);
		}
		if(user == null) {
			throw new ApplicationException(ExceptionType.INVALID_ID, "Invalid user ID: " + id);
		}
		return user;
	}

	public void updateUser (UserDto user) throws ApplicationException {
		updateUserValidation(user);
		try {
			usersDao.updateUser(user.getUserName(), user.getFirstName(), user.getLastName(), user.getId());
		}catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to update user: " + user);
		}
	}

	public void deleteUser (long id) throws ApplicationException {
		boolean userExists;
		try {
			userExists = usersDao.existsById(id);
		}catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to check if user exists: " + id);
		}
		if(!userExists) {
			throw new ApplicationException(ExceptionType.INVALID_ID, "Invalid user ID: " + id);
		}
		cacheController.deleteAllUserData(id);
		try {
			usersDao.deleteById(id);
		}catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to delete user: " + id);
		}
	}

	public List<UserInfoDto> getAllUsers() throws ApplicationException {
		try {
			return usersDao.getAllUsers();
		}catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to get all user");
		}
	}
	
	public void changePassword(PasswordChange passwords, long id) throws ApplicationException {
		passwordValidation(passwords.getNewPassword());
		passwords.setOldPassword(String.valueOf(passwords.getOldPassword().hashCode())+SALT);
		passwords.setNewPassword(String.valueOf(passwords.getNewPassword().hashCode())+SALT);
		boolean isPasswordCorrect;
		try {
			isPasswordCorrect = usersDao.existsByIdAndPassword(id, passwords.getOldPassword());
		}catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to check if user exists: " + id);
		}
		if(!isPasswordCorrect) {
			throw new ApplicationException(ExceptionType.INCORRECT_PASSWORD, "passwords don't match");
		}
		try {
			usersDao.changePassword(passwords.getNewPassword(), id);
		}catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to change password, user ID:" + id);
		}
	}

	public UserLoginData login (LoginInput loginInput) throws ApplicationException {
		//Encrypting users password so it will match the password in the data base
		loginInput.setPassword(String.valueOf(loginInput.getPassword().hashCode())+SALT);
		CacheLoginData cacheLoginData;
		try {
			cacheLoginData = usersDao.login(loginInput.getUserName(),loginInput.getPassword());
		}catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to login");
		}
		//If there is no match found in the data base the input was incorrect
		if(cacheLoginData == null) {
			throw new ApplicationException(ExceptionType.LOGIN_FAILED, "Invalid username or password");
		}
		//Creating an unique token for the user after a successful login
		String token = generateToken(loginInput.getUserName());
		//Saving all the necessary user details in the cache
		cacheController.putData(token, cacheLoginData);
		cacheController.putTokenToIdLink(cacheLoginData.getUserId(), token);
		//Returning the user the token and type for future comparison
		UserLoginData userLoginData = new UserLoginData(cacheLoginData.getUserType(), token);
		return userLoginData;
	}
	
	public void logOut(long id, String token) {
		cacheController.deleteData(id, token);
	}

	//////////////////////////////
	//Private validation methods//
	//////////////////////////////

	private void createUserValidation(UserDto user, HttpServletRequest request) throws ApplicationException {

		//Check if all the data is valid
		userInfoValidation(user);
		passwordValidation(user.getPassword());

		CacheLoginData cacheLoginData = (CacheLoginData) request.getAttribute("cacheLoginData");

		//Only an admin that is already logged in can create none customer users
		if(user.getUserType() != UserType.CUSTOMER && cacheLoginData == null) {
			throw new ApplicationException(ExceptionType.RESTRICTED_ACCESS, "An attempt to create a user other than customer by an unouthorized user: " + user);
		}
		//User type can't be null
		if(user.getUserType() == null) {
			throw new ApplicationException(ExceptionType.INVALID_USER_TYPE, "Invalid user type" + null);
		}
		//User that doesn't work in a company can't have a company ID
		if(user.getUserType() != UserType.COMPANY && user.getCompanyId() != null) {
			throw new ApplicationException(ExceptionType.USER_TYPE_MISSMATCH, "User type and company ID missmatch: user type - " + user.getUserType() + ", company ID - " + user.getCompanyId());
		}
		//If user works in a company he needs to have a valid company ID
		if(user.getUserType() == UserType.COMPANY && user.getCompanyId() == null) {
			throw new ApplicationException(ExceptionType.INVALID_ID, "Invalid company ID: " + user.getCompanyId());
		}
		//Can't create two users with the same user name
		Boolean usernameExists;
		try {
			usernameExists = usersDao.existsByUserName(user.getUserName());
		}catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to check userName: " + user.getUserName());
		}
		if(usernameExists) {
			throw new ApplicationException(ExceptionType.USER_NAME_TAKEN, "User name already taken: " + user.getUserName());
		}
	}


	private void updateUserValidation (UserDto user) throws ApplicationException {

		//Check if all the data is valid
		userInfoValidation(user);

		//Get user's original info 
		UserInfoDto oldUser = getUser(user.getId());

		//If updating user name, need to check if the new user name isn't already taken
		if(!user.getUserName().equals(oldUser.getUserName())) {
			boolean userNameExists;
			try {
				userNameExists = usersDao.existsByUserName(user.getUserName());
			}catch (Exception e){
				throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to check userName: " + user.getUserName());
			}
			if(userNameExists) {
				throw new ApplicationException(ExceptionType.USER_NAME_TAKEN, "New user name is already taken: " + user.getUserName());
			}
		}
	}


	private void userInfoValidation (UserDto user) throws ApplicationException {

		//Assuming user name has to be an e-mail, it has to contain between 6 and 45 characters, the characters . and @
		if(isOutOfRange(user.getUserName(), 6, 45) || !user.getUserName().contains("@") 
				|| !user.getUserName().contains(".")) {
			throw new ApplicationException(ExceptionType.INVALID_EMAIL, "Invalid user name: " + user.getUserName());
		}
		//Last name and first name have to contain between 2 and 45 characters
		if(isOutOfRange(user.getFirstName(), 2, 45)) {
			throw new ApplicationException(ExceptionType.INVALID_FIRST_NAME, "Invalid first name: " + user.getFirstName());  
		}
		if(isOutOfRange(user.getLastName(), 2, 45)) {
			throw new ApplicationException(ExceptionType.INVALID_LAST_NAME, "Invalid last name: " + user.getLastName());
		}
		//Creating a pattern of symbols that last name and first name can't contain and matching it 
		Pattern Name = Pattern.compile("[^A-Za-z '-]");
		Matcher matchFn = Name.matcher(user.getFirstName());
		Matcher matchLn = Name.matcher(user.getLastName());

		//Last name and first name can't contain the symbols in the pattern above
		if(matchFn.find()) {
			throw new ApplicationException(ExceptionType.INVALID_FIRST_NAME, "Invalid first name: " + user.getFirstName());  
		}
		if(matchLn.find()) {
			throw new ApplicationException(ExceptionType.INVALID_LAST_NAME, "Invalid last name: " + user.getLastName());
		}
	}
	
	
	private void passwordValidation(String password) throws ApplicationException {
		//Password has to contain between 8 and 18 characters
		if(isOutOfRange(password, 8, 18)) { 
			throw new ApplicationException(ExceptionType.INVALID_PASSWORD_LENGTH, "Invalid password length");
		}
		//Creating a pattern of characters that the password can't contain and two that it has to contain and matching them
		Pattern symbols = Pattern.compile("[^A-Za-z0-9]");
		Pattern numbers = Pattern.compile("[0-9]");
		Pattern letter = Pattern.compile("[A-Za-z]");
		Matcher matchS = symbols.matcher(password);
		Matcher matchN = numbers.matcher(password);
		Matcher matchL = letter.matcher(password);
		//Password has to contain one letter, one number and no symbols
		if(matchS.find() || !matchN.find() || !matchL.find()) {
			throw new ApplicationException(ExceptionType.INVALID_PASSWORD, "Invalid password");
		}
	}

	private String generateToken(String userName) {
		String instance = Calendar.getInstance().toString();
		int token = (userName + instance).hashCode();
		return String.valueOf(token+SALT);
	}

	private static boolean isOutOfRange(String text, int minChar, int maxChar) {

		if(text == null || text.length() < minChar || text.length() > maxChar) {
			return true;
		}
		return false;
	}

	////////////////////////////////////////
	//Public methods for other controllers//
	////////////////////////////////////////

	public UserEntity getUserEntity(long id) throws ApplicationException {
		try {
			return usersDao.findById(id).get();
		}catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to get user entity: " + id);
		}
	}
}