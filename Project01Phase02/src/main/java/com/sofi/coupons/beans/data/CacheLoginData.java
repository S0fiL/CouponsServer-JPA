package com.sofi.coupons.beans.data;

import com.sofi.coupons.enums.UserType;

public class CacheLoginData {

	private long userId;
	private UserType userType;
	private Long companyId;


	public CacheLoginData() {
		super();
	}

	public CacheLoginData(long userId, UserType userType, Long companyId) {
		super();
		this.userId = userId;
		this.userType = userType;
		this.companyId = companyId;
	}


	public UserType getUserType() {
		return userType;
	}
	public Long getCompanyId() {
		return companyId;
	}

	public long getUserId() {
		return userId;
	}

	@Override
	public String toString() {
		return "Usert type: " + userType + ", Company ID: " + companyId + ", UserID: " + userId;
	}
}