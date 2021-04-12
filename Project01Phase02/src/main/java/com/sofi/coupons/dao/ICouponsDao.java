package com.sofi.coupons.dao;

import java.sql.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sofi.coupons.beans.dto.CouponDto;
import com.sofi.coupons.beans.dto.CouponInfoDto;
import com.sofi.coupons.entities.CouponEntity;
//import com.sofi.coupons.enums.Category;

public interface ICouponsDao extends CrudRepository<CouponEntity, Long>{

	public boolean existsByTitleAndCompanyId(String title, Long companyId);

	@Query("select new com.sofi.coupons.beans.dto.CouponDto(c) from CouponEntity c where c.id = ?1")
	public CouponDto getCoupon(long id);

	@Query("select new com.sofi.coupons.beans.dto.CouponInfoDto(c) from CouponEntity c")
	public List<CouponInfoDto> getAllCoupons();


	@Query("select new com.sofi.coupons.beans.dto.CouponInfoDto(c) from CouponEntity c where c.company.id = ?1")
	public List<CouponInfoDto> getCouponsByCompany(Long companyId);

//	@Query("select new com.sofi.coupons.beans.dto.CouponInfoDto(c) from CouponEntity c where c.category = ?1")
//	public List<CouponInfoDto> getCouponsByCategory(Category category);
	
//	@Query("select new com.sofi.coupons.beans.dto.CouponInfoDto(c) from CouponEntity c where c.price <= ?1 and c.id in (select p.coupon.id from PurchaseEntity p where p.user.id = ?2)")
//	public List<CouponInfoDto> getPurchasedCouponsByMaxPrice(float maxPrice, long userId);

	@Query("select c.company.id from CouponEntity c where c.id = ?1")
	public Long getCouponsCompanyId(long id);

	@Transactional
	@Modifying
	@Query("update CouponEntity c set c.amount = ?1 where c.id = ?2")
	public void updateCouponAmount(int couponAmount, long couponId);
	
	@Transactional
	@Modifying
	public void deleteAllByEndDateBefore(Date expirationDate);
}