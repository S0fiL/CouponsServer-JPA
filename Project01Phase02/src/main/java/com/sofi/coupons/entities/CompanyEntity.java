package com.sofi.coupons.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sofi.coupons.beans.dto.CompanyDto;

@Entity
@Table(name = "companies")
public class CompanyEntity implements Serializable{

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	@Column(name = "name", unique = true, nullable = false)
	private String name;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "phone", unique = false, nullable = false)
	private String phone;

	@Column(name = "address", unique = false, nullable = true)
	private String address;

	@OneToMany(mappedBy = "company", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<UserEntity> users;

	@OneToMany(mappedBy = "company", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<CouponEntity> coupons;


	public CompanyEntity() {
		super();
	}

	public CompanyEntity(CompanyDto companyDto) {
		super();
		this.id = companyDto.getId();
		this.name = companyDto.getName();
		this.email = companyDto.getEmail();
		this.phone = companyDto.getPhone();
		this.address = companyDto.getAddress();
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(List<UserEntity> users) {
		this.users = users;
	}

	@JsonIgnore
	public List<CouponEntity> getCoupons() {
		return coupons;
	}

	@JsonIgnore
	public void setCoupons(List<CouponEntity> coupons) {
		this.coupons = coupons;
	}

	@Override
	public String toString() {
		return "ID: " + id + ", Name: " + name + ", Email: " + email + ", Phone: " + phone + ", Address: "
				+ address + ", Users: " + users + ", Coupons: " + coupons;
	}
}