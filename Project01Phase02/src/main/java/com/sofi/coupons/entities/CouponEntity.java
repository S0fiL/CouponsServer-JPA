package com.sofi.coupons.entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sofi.coupons.beans.dto.CouponDto;
import com.sofi.coupons.enums.Category;

@Entity
@Table(name = "coupons")
public class CouponEntity implements Serializable{

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private long id;
	
	@Column(name = "category", unique = false, nullable = false)
	private Category category;
	
	@Column(name = "title", unique = false, nullable = false)
	private String title;
	
	@Column(name = "description", unique = false, nullable = true, length = 1000)
	private String description;
	
	@Column(name = "start_date", unique = false, nullable = false)
	private Date startDate;
	
	@Column(name = "end_date", unique = false, nullable = false)
	private Date endDate;
	
	@Column(name = "amount", unique = false, nullable = false)
	private int amount;
	
	@Column(name = "price", unique = false, nullable = false)
	private float price;
	
	@Column(name = "image", unique = false, nullable = true)
	private String image;
	
	@OneToMany(mappedBy = "coupon", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<PurchaseEntity> purchases;
	
	@ManyToOne
	private CompanyEntity company;

	public CouponEntity() {
		super();
	}

	public CouponEntity(CouponDto couponDto, CompanyEntity company) {
		super();
		this.id = couponDto.getId();
		this.category = couponDto.getCategory();
		this.title = couponDto.getTitle();
		this.description = couponDto.getDescription();
		this.startDate = couponDto.getStartDate();
		this.endDate = couponDto.getEndDate();
		this.amount = couponDto.getAmount();
		this.price = couponDto.getPrice();
		this.image = couponDto.getImage();
		this.company = company;
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

	public List<PurchaseEntity> getPurchases() {
		return purchases;
	}

	public void setPurchases(List<PurchaseEntity> purchases) {
		this.purchases = purchases;
	}

	public CompanyEntity getCompanyEntity() {
		return company;
	}

	public void setCompanyEntity(CompanyEntity company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "ID: " + id + ", Category: " + category + ", Title: " + title + ", Description: " + description
				+ ", Start Date: " + startDate + ", End Date: " + endDate + ", Amount: " + amount + ", Price: " + price
				+ ", Image: " + image + ", Purchases: " + purchases + ", Company: " + company;
	}
}