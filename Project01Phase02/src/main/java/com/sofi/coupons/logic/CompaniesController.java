package com.sofi.coupons.logic;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sofi.coupons.beans.dto.CompanyDto;
import com.sofi.coupons.dao.ICompaniesDao;
import com.sofi.coupons.entities.CompanyEntity;
import com.sofi.coupons.enums.ExceptionType;
import com.sofi.coupons.exceptions.ApplicationException;

@Controller
public class CompaniesController {

	@Autowired
	private  ICompaniesDao companiesDao;

	///////////////////////////////////////////////
	//Public methods that call validation methods//
	///////////////////////////////////////////////

	public long createCompany(CompanyDto company) throws ApplicationException {
		createCompanyValidation(company);
		CompanyEntity companyEntity = new CompanyEntity(company);
		try {
			companiesDao.save(companyEntity);
		}catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to create company: " + company.toString());
		}
		System.out.println(companyEntity.getId());
		return companyEntity.getId();
	}


	public CompanyDto getCompany (long id) throws ApplicationException {
		CompanyDto company;
		try {
			company = companiesDao.getCompany(id);
		}catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to get company by ID :" + id);
		}
		if(company == null) {
			throw new ApplicationException(ExceptionType.INVALID_ID, "Invalid company ID: " + id);
		}
		return company;
	}


	public void updateCompany (CompanyDto company) throws ApplicationException {
		updateCompanyValidation(company);
		CompanyEntity companyEntity = new CompanyEntity(company);
		try {
			companiesDao.save(companyEntity);
		}catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to update company :" + company.toString());
		}
	}


	public void deleteCompany (long id) throws ApplicationException {
		boolean companyExists;
		try {
			companyExists = companiesDao.existsById(id);
		}catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to check if cmpany exists: " + id);
		}
		if(!companyExists) {
			throw new ApplicationException(ExceptionType.INVALID_ID, "Invalid company ID: " + id);
		}
		try {
			companiesDao.deleteById(id);
		}catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to delete company: " + id);
		}
	}


	public List<CompanyDto> getAllCompanies() throws ApplicationException{
		try {
			return companiesDao.getAllCompanies();
		}catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to get all companies");
		}
	}

	//////////////////////////////
	//Private validation methods//
	//////////////////////////////	

	private void createCompanyValidation(CompanyDto company) throws ApplicationException {

		//Check if all the data is valid
		companyInfoValidation(company);

		//Can't create two companies with the same name
		boolean nameExists;
		try {
			nameExists = companiesDao.existsByName(company.getName());
		} catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to check company name: " + company.getName());
		}
		if(nameExists) {
			throw new ApplicationException(ExceptionType.NAME_TAKEN, "Company name already exists: " + company.getName());
		}
		//Can't create two companies with the same e-mail
		boolean emailExists;
		try {
			emailExists = companiesDao.existsByEmail(company.getEmail());
		} catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to check company email: " + company.getEmail());
		}
		if(emailExists) {
			throw new ApplicationException(ExceptionType.EMAIL_TAKEN, "Company e-mail already exists: " + company.getEmail());
		}
	}


	private void updateCompanyValidation(CompanyDto company) throws ApplicationException {

		//Check if all the data is valid
		companyInfoValidation(company);

		//Get company's original info 
		CompanyDto oldCompany = getCompany(company.getId());

		//If updating company name, need to check if the new name isn't already taken
		if(!company.getName().equals(oldCompany.getName())) { 
			boolean nameExists;
			try {
				nameExists = companiesDao.existsByName(company.getName());
			} catch (Exception e){
				throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to check company name: " + company.getName());
			}
			if(nameExists) {
				throw new ApplicationException(ExceptionType.NAME_TAKEN, "New company name is already taken: " + company.getName());
			}
		}
		//If updating company e-mail, need to check if the new email is'nt already taken
		if(!company.getEmail().equals(oldCompany.getEmail())) {
			boolean emailExists;
			try {
				emailExists = companiesDao.existsByEmail(company.getEmail());
			} catch (Exception e){
				throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to check company email: " + company.getEmail());
			}
			if(emailExists) {
				throw new ApplicationException(ExceptionType.EMAIL_TAKEN, "New company e-mail already exists: " + company.getEmail());
			}
		}
	}


	private void companyInfoValidation(CompanyDto company) throws ApplicationException {

		//Company name has to contain between 2 and 45 characters
		if(isOutOfRange(company.getName(), 2, 45)) { 
			throw new ApplicationException(ExceptionType.INVALID_COMPANY_NAME, "Invalid name: " + company.getName());
		}
		//Company e-mail has to contain between 6 and 45 characters and symbols . and @
		if(isOutOfRange(company.getEmail(), 6, 45) || !company.getEmail().contains("@") || !company.getEmail().contains(".")) {
			throw new ApplicationException(ExceptionType.INVALID_EMAIL, "Invalid e-mail: " + company.getEmail());
		}
		//Company phone number has to contain between 3 and 14 characters
		if(isOutOfRange(company.getPhone(), 3, 14)) { 
			throw new ApplicationException(ExceptionType.INVALID_PHONE, "Invalid phone number: " + company.getPhone());
		}
		//Creating a pattern of all the symbols that a phone number can't contain and matching it
		Pattern p = Pattern.compile("[^0-9*()-]");
		Matcher m = p.matcher(company.getPhone());

		//Company phone number can't contain any of the symbols in the pattern above
		if(m.find()) { 
			throw new ApplicationException(ExceptionType.INVALID_PHONE, "Invalid phone number: " + company.getPhone());
		}
		//Company address can't be longer than 45 characters (Can be null)
		if(company.getAddress() != null && company.getAddress().length() > 45) {
			throw new ApplicationException(ExceptionType.INVALID_ADDRESS, "Address is too long: " + company.getAddress());
		}
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

	public CompanyEntity getCompanyEntity(Long id) throws ApplicationException {
		if(id == null) {
			return null;
		}
		Optional<CompanyEntity> companies = null;
		try {
		companies = companiesDao.findById(id);
		}catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to get company entity: " + id);
		}
		try {
			return companies.get();
		}catch (Exception e){
			throw new ApplicationException(e, ExceptionType.INVALID_ID, "Invalid company ID: " + id);
		}
	}
}



