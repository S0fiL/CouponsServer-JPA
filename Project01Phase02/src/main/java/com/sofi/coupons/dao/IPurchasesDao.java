package com.sofi.coupons.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sofi.coupons.beans.dto.PurchaseDto;
import com.sofi.coupons.beans.dto.PurchaseInfoDto;
import com.sofi.coupons.entities.PurchaseEntity;

public interface IPurchasesDao extends CrudRepository<PurchaseEntity, Long> {

	public boolean existsById (long id);

	@Query("select new com.sofi.coupons.beans.dto.PurchaseInfoDto(p, p.coupon) from PurchaseEntity p where p.id = ?1")
	public PurchaseInfoDto getPurchase(long id);

	@Query("select new com.sofi.coupons.beans.dto.PurchaseDto(p) from PurchaseEntity p")
	public List<PurchaseDto> getAllPurchases();

	@Query("select new com.sofi.coupons.beans.dto.PurchaseDto(p) from PurchaseEntity p where p.coupon.id in (select c.id from CouponEntity c where c.company.id = ?1)")
	public List<PurchaseDto> getPurchasesByCompany(long companyId);

	@Query("select new com.sofi.coupons.beans.dto.PurchaseInfoDto(p, p.coupon) from PurchaseEntity p where p.user.id = ?1")
	public List<PurchaseInfoDto> getPurchasesByUser(long userId);
}
