package com.sofi.coupons.logic;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sofi.coupons.beans.data.CacheLoginData;
import com.sofi.coupons.beans.dto.CouponDto;
import com.sofi.coupons.beans.dto.CouponInfoDto;
import com.sofi.coupons.dao.ICouponsDao;
import com.sofi.coupons.entities.CompanyEntity;
import com.sofi.coupons.entities.CouponEntity;
import com.sofi.coupons.enums.ExceptionType;
import com.sofi.coupons.exceptions.ApplicationException;
//import com.sofi.coupons.enums.Category;

@Controller
public class CouponsController {

	@Autowired
	private ICouponsDao couponsDao;
	@Autowired
	private CompaniesController companiesController;

	///////////////////////////////////////////////
	//Public methods that call validation methods//
	///////////////////////////////////////////////

	public long createCoupon(CouponDto coupon) throws ApplicationException {
		createCouponValidation(coupon);
		CompanyEntity companyEntity = companiesController.getCompanyEntity(coupon.getCompanyId());
		CouponEntity couponEntity = new CouponEntity(coupon, companyEntity);
		try {
			couponsDao.save(couponEntity);
		} catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to create coupon: " + coupon.toString());
		}
		return couponEntity.getId();
	}


	public CouponDto getCoupon(long id) throws ApplicationException {
		CouponDto coupon;
		try {
			coupon = couponsDao.getCoupon(id);
		} catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to get coupon by ID: " + id);
		}
		if(coupon == null) {
			throw new ApplicationException(ExceptionType.INVALID_ID, "Invalid coupon ID: " + id);
		}
		return coupon;
	}


	public void updateCoupon(CouponDto coupon, HttpServletRequest request) throws ApplicationException {
		CacheLoginData cacheLoginData = (CacheLoginData) request.getAttribute("cacheLoginData");
		coupon.setCompanyId(cacheLoginData.getCompanyId());
		updateCouponValidation(coupon, cacheLoginData);
		CompanyEntity companyEntity = companiesController.getCompanyEntity(coupon.getCompanyId());
		CouponEntity couponEntity = new CouponEntity(coupon, companyEntity);
		try {
			couponsDao.save(couponEntity);
		} catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to update coupon: " + coupon.toString());
		}
	}


	public void deleteCoupon (long id, HttpServletRequest request) throws ApplicationException {
		CacheLoginData cacheLoginData = (CacheLoginData) request.getAttribute("cacheLoginData");
		//Coupons can be deleted only by users from the same company
		long couponsCompanyId = getCouponsCompanyId(id);
		if(couponsCompanyId != cacheLoginData.getCompanyId()) {
			throw new ApplicationException(ExceptionType.RESTRICTED_ACCESS,  "An attempt to delete data that does not belong to the user: User ID - " + cacheLoginData.getUserId() + ", Coupon ID - " + id);
		}
		try {
			couponsDao.deleteById(id);
		} catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to delete coupon: " + id);
		}
	}


	public List<CouponInfoDto> getAllCoupons() throws ApplicationException{
		try {
			return couponsDao.getAllCoupons();
		} catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to get all coupons");
		}
	}


	public List<CouponInfoDto> getCouponsByCompany(long companyId) throws ApplicationException{
		try {
			return couponsDao.getCouponsByCompany(companyId);
		} catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to get coupons by company: " + companyId);
		}
	}

	
//	public List<CouponInfoDto> getCouponsByCategory(Category category) throws ApplicationException{
//		try {
//			return couponsDao.getCouponsByCategory(category);
//		} catch (Exception e){
//			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to get coupons by category: " + category);
//		}
//	}

	
//	public List<CouponInfoDto> getPurchasedCouponsByMaxPrice(long userId, float maxPrice) throws ApplicationException{
//		//Price has to be positive
//		if(maxPrice<=0) {
//			throw new ApplicationException(ExceptionType.INVALID_PRICE, "Invalid price: " + maxPrice);
//		}
//		try {
//			return couponsDao.getPurchasedCouponsByMaxPrice(maxPrice, userId);
//		} catch (Exception e){
//			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to get coupons by max price: Price - " + maxPrice + ", user ID - " + userId);
//		}
//	}

	//////////////////////////////
	//Private validation methods//
	//////////////////////////////

	private void createCouponValidation(CouponDto coupon) throws ApplicationException {

		//Check if all the data is valid
		couponInfoValidation(coupon);	

		//Start date can't be before the coupon creation date
		if(coupon.getStartDate().before(new java.sql.Date(Calendar.getInstance().getTimeInMillis()))) {
			throw new ApplicationException(ExceptionType.INVALID_DATE, "Invalid start date: " + coupon.getStartDate());
		}
		//Can't create two coupons with the same title for the same company
		boolean titleExists;
		try {
			titleExists = couponsDao.existsByTitleAndCompanyId(coupon.getTitle(), coupon.getCompanyId());
		} catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to check coupon title: " + coupon.getTitle());
		}
		if(titleExists) {
			throw new ApplicationException(ExceptionType.TITLE_ALREADY_EXISTS, "Title already exists: " + coupon.getTitle());
		}
	}


	private void updateCouponValidation(CouponDto coupon, CacheLoginData cacheLoginData) throws ApplicationException {

		//Check if all the data is valid
		couponInfoValidation(coupon);

		//Get coupon's original info 
		CouponDto oldCoupon = getCoupon(coupon.getId());

		//Coupons can be updated only by users from the same company
		if(oldCoupon.getCompanyId() != coupon.getCompanyId()) {
			throw new ApplicationException(ExceptionType.RESTRICTED_ACCESS, "An attempt to update data that does not belong to the user: User ID - " + cacheLoginData.getUserId() + ", Coupon - " + coupon.toString());
		}
		//Can't decrease coupons amount
		if(oldCoupon.getAmount() > coupon.getAmount()) {
			throw new ApplicationException(ExceptionType.INVALID_UPDATE, "Invalid coupon update: " + coupon.toString());
		}
		//Can't shorten coupon validity period
		oldCoupon.getStartDate().setDate(oldCoupon.getStartDate().getDate()+1);
		if(coupon.getStartDate().after(oldCoupon.getStartDate()) || coupon.getEndDate().before(oldCoupon.getEndDate())) {
			throw new ApplicationException(ExceptionType.INVALID_DATE_UPDATE, "Invalid date updates: " + coupon.toString());
		}
		//Start date can't be updated to a date before the update date 
		if(!coupon.getStartDate().equals(oldCoupon.getStartDate()) && coupon.getStartDate().before(Calendar.getInstance().getTime())) {
			throw new ApplicationException(ExceptionType.INVALID_DATE, "Invalid date: " + coupon.getStartDate());
		}
		//If updating coupon title, need to check if the new title isn't already taken
		if(!coupon.getTitle().equals(oldCoupon.getTitle())) {
			boolean titleExists;
			try {
				titleExists = couponsDao.existsByTitleAndCompanyId(coupon.getTitle(), coupon.getCompanyId());
			} catch (Exception e){
				throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to check coupon title: " + coupon.getTitle());
			}
			if(titleExists) {
				throw new ApplicationException(ExceptionType.TITLE_ALREADY_EXISTS, "New coupon title is already taken: " + coupon.getTitle());
			}
		}
	}


	private void couponInfoValidation(CouponDto coupon) throws ApplicationException {

		//Title has to contain between 1 and 45 characters
		if(coupon.getTitle() == null || coupon.getTitle().isEmpty() || coupon.getTitle().length()>1000) {
			throw new ApplicationException(ExceptionType.INVALID_TITLE, "Invalid title: " + coupon.getTitle());
		}	
		//Description has to be shorter than 150 characters (Can be null)
		if(coupon.getDescription() != null && coupon.getDescription().length()>1000) {
			throw new ApplicationException(ExceptionType.INVALID_DESCRIPTION, "Description is too long: " + coupon.getDescription());
		}
		//Coupon expiration date can't be before the start date
		if(coupon.getEndDate().before(coupon.getStartDate())) {
			throw new ApplicationException(ExceptionType.INVALID_DATE, "Invalid dates: " + coupon.getStartDate() + ", " + coupon.getEndDate());
		}
		//Dates can't be null
		if(coupon.getEndDate() == null || coupon.getStartDate() == null) {
			throw new ApplicationException(ExceptionType.INVALID_DATE, "Invalid dates: " + coupon.getStartDate() + ", " + coupon.getEndDate());
		}
		//Category can't be null
		if(coupon.getCategory() == null) {
			throw new ApplicationException(ExceptionType.INVALID_CATEGORY, "Invalid category: " + coupon.getCategory());
		}
		//Amount has to be a positive number, also can't update a coupon that sold out without adding more to buy
		if(coupon.getAmount() <= 0) {
			throw new ApplicationException(ExceptionType.INVALID_AMOUNT, "Invalid amount: " + coupon.getAmount());
		}
		//Price has to be positive number 
		if(coupon.getPrice() <= 0) {
			throw new ApplicationException(ExceptionType.INVALID_PRICE, "Invalid price: " + coupon.getPrice());
		}
		//Image URL link can't be longer than 256 characters (Can be null)
		if(coupon.getImage() != null && coupon.getImage().length() > 225) {
			throw new ApplicationException(ExceptionType.INVALID_LINK, "Image URL is too long: " + coupon.getImage());
		}
	}

	////////////////////////////////////////
	//Public methods for other controllers//
	////////////////////////////////////////	

	public CouponEntity getCouponEntity(long id) throws ApplicationException {
		try {
			return couponsDao.findById(id).get();
		} catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to get coupon entity: " + id);
		}
	}


	public Long getCouponsCompanyId(long couponId) throws ApplicationException {
		Long companyId;
		try {
			companyId = couponsDao.getCouponsCompanyId(couponId);
		} catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to get company ID by coupon ID: " + couponId);
		}
		if(companyId == null) {
			throw new ApplicationException(ExceptionType.INVALID_ID, "Invalid coupon ID: " + couponId);
		}
		return companyId;
	}


	public void updateCouponAmount(int couponAmount, long couponId) throws ApplicationException {
		try {
			couponsDao.updateCouponAmount(couponAmount, couponId);
		} catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to update coupon amount after purchase: coupon ID - " + couponId + ", Amount - " + couponAmount);
		}
	}
}