package com.sofi.coupons.beans.dto;

import java.sql.Date;

import com.sofi.coupons.entities.CouponEntity;
import com.sofi.coupons.enums.Category;

//A coupon DTO containing all the information plus company name(for creation/update or for a single coupon request)
public class CouponDto {

	private long id;
	private Category category;
	private String title;
	private String description;
	private Date startDate;
	private Date endDate;
	private int amount;
	private float price;
	private String image;
	private long companyId;
	private String companyName;


	public CouponDto() {
		super();
	}

	public CouponDto(Category category, String title, String description, Date startDate, Date endDate,
			int amount, float price, String image, long companyId) {
		super();
		this.category = category;
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.price = price;
		this.image = image;
		this.companyId = companyId;
	}

	public CouponDto(long id, Category category, String title, String description, Date startDate,
			Date endDate, int amount, float price, String image,  long companyId) {
		this(category, title, description, startDate, endDate, amount, price, image, companyId);
		this.id = id;
	}

	public CouponDto(CouponEntity couponEntity) {
		this(couponEntity.getId(), couponEntity.getCategory(), couponEntity.getTitle(), couponEntity.getDescription(), couponEntity.getStartDate(), couponEntity.getEndDate(),
				couponEntity.getAmount(), couponEntity.getPrice(), couponEntity.getImage(), couponEntity.getCompanyEntity().getId());
		this.companyName = couponEntity.getCompanyEntity().getName();
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
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


	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Override
	public String toString() {
		return "ID: " + id + ", Category: " + category + ", Title: " + title + ", Description: " + description + ", Start Date: " + startDate
				+ ", End Date: " + endDate + ", Amount: " + amount + ", Price: " + price + ", Image: " + image + ", Company ID: " + companyId
				+ ", Company Name: " + companyName;
	}
}