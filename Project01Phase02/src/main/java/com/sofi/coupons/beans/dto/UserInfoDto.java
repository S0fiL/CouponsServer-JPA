package com.sofi.coupons.beans.dto;

import com.sofi.coupons.entities.UserEntity;
import com.sofi.coupons.enums.UserType;

//A user DTO containing no password (for a requests of users' list/single user)
public class UserInfoDto {

	private long id;
	private String userName;
	private UserType userType;
	private String firstName;
	private String lastName;
	private Long companyId;


	public UserInfoDto(UserEntity userEntity) {
		super();
		this.id = userEntity.getId();
		this.userName = userEntity.getUserName();
		this.userType = userEntity.getUserType();
		this.firstName = userEntity.getFirstName();
		this.lastName = userEntity.getLastName();
		this.companyId = userEntity.getCompanyId();
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	@Override
	public String toString() {
		return "ID: " + id + ", User Name: " + userName + ", User Type: " + userType + ", First Name: " + firstName
				+ ", Last Name: " + lastName + ", Company ID: " + companyId;
	}
}