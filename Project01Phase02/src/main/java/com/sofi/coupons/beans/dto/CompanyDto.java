package com.sofi.coupons.beans.dto;

import com.sofi.coupons.entities.CompanyEntity;

public class CompanyDto {

	private long id;
	private String name;
	private String email;
	private String phone;
	private String address;


	public CompanyDto() {
		super();
	}

	public CompanyDto(String name, String email, String phone, String address) {
		super();
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.address = address;
	}

	public CompanyDto(long id, String name, String email, String phone, String address) {
		this(name, email, phone, address);
		this.id = id;
	}

	public CompanyDto(CompanyEntity companyEntity) {
		this(companyEntity.getName(), companyEntity.getEmail(), companyEntity.getPhone(), companyEntity.getAddress());
		this.id = companyEntity.getId();
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

	@Override
	public String toString() {
		return "ID: " + id + ", Name: " + name + ", E-mail: " + email + ", Phone: " + phone + ", Address: " + address;
	}
}