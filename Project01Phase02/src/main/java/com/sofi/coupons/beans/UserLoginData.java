package com.sofi.coupons.beans;

import com.sofi.coupons.enums.UserType;

public class UserLoginData {

	private UserType userType;
	private String token;


	public UserLoginData(UserType userType, String token) {
		super();
		this.userType = userType;
		this.token = token;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "User type: " + userType + ", Token: " + token;
	}
}