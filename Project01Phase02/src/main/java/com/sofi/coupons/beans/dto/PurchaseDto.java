package com.sofi.coupons.beans.dto;

import java.sql.Timestamp;

import com.sofi.coupons.entities.PurchaseEntity;

//A purchase DTO containing all the information (for creation or requesting a list by admin or company)
public class PurchaseDto {

	private long id;
	private int amount;
	private Timestamp timestamp;
	private long userId;
	private long couponId;


	public PurchaseDto() {
		super();
	}

	public PurchaseDto(int amount, long couponId) {
		super();
		this.amount = amount;
		this.couponId = couponId;
	}

	public PurchaseDto(PurchaseEntity purchase) {
		this(purchase.getAmount(), purchase.getCouponEntity().getId());
		this.id = purchase.getId();
		this.timestamp = purchase.getTimestamp();
		this.userId = purchase.getUserEntity().getId();
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

	@Override
	public String toString() {
		return "ID: " + id  + ", Amount: " + amount + ", Timestamp: " + timestamp + "User ID: " + userId + "Coupon ID: " + couponId;
	}
}