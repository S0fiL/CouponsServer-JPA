package com.sofi.coupons.beans.dto;

import java.sql.Timestamp;

import com.sofi.coupons.entities.CouponEntity;
import com.sofi.coupons.entities.PurchaseEntity;

//A purchase DTO containing all purchase information plus some of the coupon information (for a single purchase request)
public class PurchaseInfoDto {

	private long id;
	private int amount;
	private Timestamp timestamp;
	private long userId;
	private long couponId;
	private String couponTitle;
	private float price;
	private String companyName;


	public PurchaseInfoDto(PurchaseEntity purchaseEntity, CouponEntity couponEntity) {

		this.id = purchaseEntity.getId();
		this.amount = purchaseEntity.getAmount();
		this.timestamp = purchaseEntity.getTimestamp();
		this.userId = purchaseEntity.getUserEntity().getId();
		this.couponId = couponEntity.getId();
		this.couponTitle = couponEntity.getTitle();
		this.price = couponEntity.getPrice();
		this.companyName = couponEntity.getCompanyEntity().getName();
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getCouponId() {
		return couponId;
	}

	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}

	public String getCouponTitle() {
		return couponTitle;
	}

	public void setCouponTitle(String couponTitle) {
		this.couponTitle = couponTitle;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Override
	public String toString() {
		return "ID: " + id + ", Amount: " + amount + ", Timestamp: " + timestamp + ", User ID: " + userId + ", Coupon ID: " + couponId
				+ ", Coupon Title: " + couponTitle + ", Price: " + price + ", Company Name: " + companyName;
	}
}