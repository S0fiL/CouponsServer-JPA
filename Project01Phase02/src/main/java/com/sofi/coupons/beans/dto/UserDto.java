package com.sofi.coupons.beans.dto;

import com.sofi.coupons.enums.UserType;

//A user DTO containing all the information (for creation/update)
public class UserDto {

	private long id;
	private String userName;
	private String password;
	private UserType userType;
	private String firstName;
	private String lastName;
	private Long companyId;


	public UserDto() {
		super();
	}

	public UserDto(String userName, String password, String firstName, String lastName) {
		super();
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public UserDto(String userName, String password, Long companyId, UserType userType, String firstName, String lastName) {
		this(userName, password, firstName, lastName);
		this.userType = userType;
		this.companyId = companyId;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
		return "ID: " + id + ", User Name: " + userName + ", Company ID: " + companyId
				+ ", User Type: " + userType + ", First Name: " + firstName + ", Last Name: " + lastName;
	}
}