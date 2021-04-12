package com.sofi.coupons.beans.dto;

import java.sql.Date;

import com.sofi.coupons.entities.CouponEntity;
import com.sofi.coupons.enums.Category;

//A coupon DTO containing limited information (for requests of coupons' lists)
public class CouponInfoDto {

	private long id;
	private Category category;
	private String title;
	private Date endDate;
	private float price;
	private String image;
	private long companyId;


	public CouponInfoDto(CouponEntity couponEntity) {
		super();
		this.id = couponEntity.getId();
		this.category = couponEntity.getCategory();
		this.title = couponEntity.getTitle();
		this.endDate = couponEntity.getEndDate();
		this.price = couponEntity.getPrice();
		this.image = couponEntity.getImage();
		this.companyId = couponEntity.getCompanyEntity().getId();
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	@Override
	public String toString() {
		return "ID: " + id + ", Category: " + category + ", Title: " + title + ", End Date: " + endDate + ", Price: " + price + ", Image: " + image + ", company ID: " + companyId;
	}
}