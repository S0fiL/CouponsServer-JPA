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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sofi.coupons.beans.data.CacheLoginData;
import com.sofi.coupons.beans.dto.CouponDto;
import com.sofi.coupons.beans.dto.CouponInfoDto;
import com.sofi.coupons.exceptions.ApplicationException;
import com.sofi.coupons.logic.CouponsController;
//import com.sofi.coupons.enums.Category;

@RestController
@RequestMapping("/coupons")
public class CouponsApi {

	@Autowired
	private CouponsController couponsController;

	@PostMapping
	public long createCoupon(@RequestBody CouponDto coupon, HttpServletRequest request) throws ApplicationException {
		CacheLoginData cacheLoginData = (CacheLoginData) request.getAttribute("cacheLoginData");
		coupon.setCompanyId(cacheLoginData.getCompanyId());
		long couponId = couponsController.createCoupon(coupon);
		return couponId;
	}

	@GetMapping("/{couponId}")
	public CouponDto getCoupon(@PathVariable("couponId") long id) throws ApplicationException {
		return couponsController.getCoupon(id);
	}

	@PutMapping
	public void updateCoupon(@RequestBody CouponDto coupon, HttpServletRequest request) throws ApplicationException {
		couponsController.updateCoupon(coupon, request);
	}

	@DeleteMapping("/{couponId}")
	public void deleteCoupon(@PathVariable("couponId") long id, HttpServletRequest request) throws ApplicationException {
		couponsController.deleteCoupon(id, request);
	}

	@GetMapping
	public List<CouponInfoDto> getAllCoupons() throws ApplicationException {
		return couponsController.getAllCoupons();
	}

	@GetMapping("/byCompany")
	public List<CouponInfoDto> getCouponsByCompany(HttpServletRequest request) throws ApplicationException {
		CacheLoginData cacheLoginData = (CacheLoginData) request.getAttribute("cacheLoginData");
		return couponsController.getCouponsByCompany(cacheLoginData.getCompanyId());
	}

	@GetMapping("/byCompanyForAdmin")
	public List<CouponInfoDto> getCouponsByCompany(@RequestParam long companyId) throws ApplicationException {
		return couponsController.getCouponsByCompany(companyId);
	}
	
//	@GetMapping("/byCategory")
//	public List<CouponInfoDto> getCouponsByCategory(@RequestParam("category") Category category) throws ApplicationException {
//		return couponsController.getCouponsByCategory(category);
//	}

//	@GetMapping("/byMaxPrice")
//	public List<CouponInfoDto> getPurchasedCouponsByMaxPrice(@RequestParam("maxPrice") float maxPrice, HttpServletRequest request) throws ApplicationException {
//		CacheLoginData cacheLoginData = (CacheLoginData) request.getAttribute("cacheLoginData");
//		return couponsController.getPurchasedCouponsByMaxPrice(cacheLoginData.getUserId(), maxPrice);
//	}
}