package com.sofi.coupons.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sofi.coupons.beans.LoginInput;
import com.sofi.coupons.beans.PasswordChange;
import com.sofi.coupons.beans.UserLoginData;
import com.sofi.coupons.beans.data.CacheLoginData;
import com.sofi.coupons.beans.dto.UserDto;
import com.sofi.coupons.beans.dto.UserInfoDto;
import com.sofi.coupons.exceptions.ApplicationException;
import com.sofi.coupons.logic.UsersController;

@RestController
@RequestMapping("/users")
public class UsersApi {

	@Autowired
	private UsersController usersController;

	@PostMapping
	public long createUser(@RequestBody UserDto user, HttpServletRequest request) throws ApplicationException {
		long userId = usersController.createUser(user, request);
		return userId;
	}

	@GetMapping("/myAccount")
	public UserInfoDto getUser(HttpServletRequest request) throws ApplicationException {
		CacheLoginData cacheLoginData = (CacheLoginData) request.getAttribute("cacheLoginData");
		return usersController.getUser(cacheLoginData.getUserId());
	}

	@GetMapping("/{userId}")
	public UserInfoDto getUserForAdmin(@PathVariable("userId") long id) throws ApplicationException {
		return usersController.getUser(id);
	}

	@PutMapping
	public void updateUser(@RequestBody UserDto user, HttpServletRequest request) throws ApplicationException {
		CacheLoginData cacheLoginData = (CacheLoginData) request.getAttribute("cacheLoginData");
		user.setId(cacheLoginData.getUserId());
		user.setUserType(cacheLoginData.getUserType());
		user.setCompanyId(cacheLoginData.getCompanyId());
		usersController.updateUser(user);
	}

	@DeleteMapping
	public void deleteUser(HttpServletRequest request) throws ApplicationException {
		CacheLoginData cacheLoginData = (CacheLoginData) request.getAttribute("cacheLoginData");
		usersController.deleteUser(cacheLoginData.getUserId());
	}

	@DeleteMapping("/{userId}")
	public void deleteUserForAdmin(@PathVariable("userId") long id) throws ApplicationException {
		usersController.deleteUser(id);
	}

	@GetMapping
	public List<UserInfoDto> getAllUsers() throws ApplicationException {
		return usersController.getAllUsers();
	}

	@PutMapping("/password")
	public void changePassword(@RequestBody PasswordChange passwords, HttpServletRequest request) throws ApplicationException {
		CacheLoginData cacheLoginData = (CacheLoginData) request.getAttribute("cacheLoginData");
		usersController.changePassword(passwords, cacheLoginData.getUserId());
	}
	
	@PostMapping("/login")
	public UserLoginData login(@RequestBody LoginInput loginInput) throws ApplicationException {
		return usersController.login(loginInput);
	}
	
	@DeleteMapping("/logOut")
	public void logOut(HttpServletRequest request) {
		CacheLoginData cacheLoginData = (CacheLoginData) request.getAttribute("cacheLoginData");
		String token = request.getHeader("authorization");
		usersController.logOut(cacheLoginData.getUserId(), token);
	}
}