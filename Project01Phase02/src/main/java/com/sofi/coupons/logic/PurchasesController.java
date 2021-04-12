package com.sofi.coupons.logic;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sofi.coupons.beans.data.CacheLoginData;
import com.sofi.coupons.beans.dto.CouponDto;
import com.sofi.coupons.beans.dto.PurchaseDto;
import com.sofi.coupons.beans.dto.PurchaseInfoDto;
import com.sofi.coupons.dao.IPurchasesDao;
import com.sofi.coupons.entities.CouponEntity;
import com.sofi.coupons.entities.PurchaseEntity;
import com.sofi.coupons.entities.UserEntity;
import com.sofi.coupons.enums.ExceptionType;
import com.sofi.coupons.enums.UserType;
import com.sofi.coupons.exceptions.ApplicationException;

@Controller
public class PurchasesController {

	@Autowired
	private IPurchasesDao purchasesDao;
	@Autowired
	private CouponsController couponsController;
	@Autowired
	private UsersController usersController;

	///////////////////////////////////////////////
	//Public methods that call validation methods//
	///////////////////////////////////////////////

	public long createPurchase(PurchaseDto purchase) throws ApplicationException {
		purchaseCreationValidation(purchase);
		UserEntity userEntity = usersController.getUserEntity(purchase.getUserId());
		CouponEntity couponEntity = couponsController.getCouponEntity(purchase.getCouponId());
		PurchaseEntity purchaseEntity = new PurchaseEntity(purchase, userEntity, couponEntity);
		try {
			purchasesDao.save(purchaseEntity);
		} catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to create purchase: " + purchase);
		}
		return purchaseEntity.getId();		
	}


	public PurchaseInfoDto getPurchase(long id, HttpServletRequest request) throws ApplicationException {
		CacheLoginData cacheLoginData = (CacheLoginData) request.getAttribute("cacheLoginData");
		PurchaseInfoDto purchase;
		try {
			purchase = purchasesDao.getPurchase(id);
		} catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to get purchase by ID: " + id);
		}
		if(purchase == null) {
			throw new ApplicationException(ExceptionType.INVALID_ID, "Invalid purchase ID: " + id);
		}
		//Only the user who made the purchase, employee of this coupon's company and admins can request the purchase details
		Long couponsCompanyId = couponsController.getCouponsCompanyId(purchase.getCouponId());
		if(cacheLoginData.getUserType() != UserType.ADMIN && cacheLoginData.getUserId() != purchase.getUserId() && cacheLoginData.getCompanyId() != couponsCompanyId) {
			throw new ApplicationException(ExceptionType.RESTRICTED_ACCESS, "An attempt to access data that does not belong to the user: User ID - " + cacheLoginData.getUserId() + ", Purchase - " + purchase);
		}
		return purchase;
	}


	public void deletePurchase(long id) throws ApplicationException {
		boolean purchaseExists;
		try {
			purchaseExists = purchasesDao.existsById(id);
		} catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to delete purchase: " + id);
		}
		if(!purchaseExists) {
			throw new ApplicationException(ExceptionType.INVALID_ID, "Invalid purchase ID: " + id);
		}
		purchasesDao.deleteById(id);
	}


	public List<PurchaseDto> getAllPurchases() throws ApplicationException {
		try {
			return purchasesDao.getAllPurchases();
		} catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to get all purchases");
		}
	}


	public List<PurchaseDto> getPurchasesByCompany(long companyId) throws ApplicationException {
		try {
			return purchasesDao.getPurchasesByCompany(companyId);
		} catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to get purchases by company: " + companyId);
		}
	}


	public List<PurchaseInfoDto> getPurchasesByUser(long userId) throws ApplicationException {
		try {
			return purchasesDao.getPurchasesByUser(userId);
		} catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to get purchases by user: " + userId);
		}
	}

	//////////////////////////////
	//Private validation methods//
	//////////////////////////////

	private void purchaseCreationValidation(PurchaseDto purchase) throws ApplicationException {
		
		//Setting the timestamp to the time when the purchase is made
		Timestamp timestamp = new Timestamp(Calendar.getInstance().getTimeInMillis());
		purchase.setTimestamp(timestamp);

		//Amount has to be a positive number
		if(purchase.getAmount() <= 0) {
			throw new ApplicationException(ExceptionType.INVALID_PURCHASE, "Invalid amount: " + purchase.getAmount());
		}

		CouponDto coupon = couponsController.getCoupon(purchase.getCouponId());

		//Check if the coupon expired
		if(timestamp.after(coupon.getEndDate())) {
			throw new ApplicationException(ExceptionType.COUPON_EXPIRED, "Coupon has expired: coupon - " + coupon.getId());
		}

		//Can't purchase coupons that are sold out
		if(coupon.getAmount() < purchase.getAmount()) {
			throw new ApplicationException(ExceptionType.NOT_ENOUGH_IN_STOCK, "Requested amount is bigger than coupons amount: requested amount - " + purchase.getAmount() + ", coupons amount - " + coupon.getAmount());
		}
		//Updating the coupons amount
		int newAmount = coupon.getAmount() - purchase.getAmount();
		couponsController.updateCouponAmount(newAmount, coupon.getId());
	}
}