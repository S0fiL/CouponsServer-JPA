package com.sofi.coupons.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sofi.coupons.beans.dto.PurchaseDto;

@Entity
@Table(name = "purchases")
public class PurchaseEntity implements Serializable{

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	@Column(name = "amount", unique = false, nullable = false)
	private int amount;

	@Column(name = "timestamp", unique = false, nullable = false)
	private Timestamp timestamp;

	@ManyToOne
	private UserEntity user;

	@ManyToOne
	private CouponEntity coupon;


	public PurchaseEntity() {
		super();
	}

	public PurchaseEntity(PurchaseDto purchaseDto, UserEntity user, CouponEntity coupon) {
		super();
		this.amount = purchaseDto.getAmount();
		this.timestamp = purchaseDto.getTimestamp();
		this.user = user;
		this.coupon = coupon;
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

	public UserEntity getUserEntity() {
		return user;
	}

	public void setUserEntity(UserEntity user) {
		this.user = user;
	}

	public CouponEntity getCouponEntity() {
		return coupon;
	}

	public void setCouponEntity(CouponEntity coupon) {
		this.coupon = coupon;
	}

	@Override
	public String toString() {
		return "ID: " + id + ", Amount: " + amount + ", Timestamp: " + timestamp + ", User: "
				+ user + ", Coupon: " + coupon;
	}
}