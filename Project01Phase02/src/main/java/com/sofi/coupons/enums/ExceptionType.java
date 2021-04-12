package com.sofi.coupons.enums;

public enum ExceptionType {

	RESTRICTED_ACCESS(631, "You are unouthorized to access this data", true, true),
	GENERAL_ERROR(601, "Genral Error", true, false),
	INVALID_ID(602, "This ID is invalid", false, false),
	INVALID_UPDATE(603, "The data you are trying to update can't be changed", false, false),
	NAME_TAKEN(604, "This Company name is already taken", false, false),
	EMAIL_TAKEN(605, "This e-mail is already taken", false, false),
	INVALID_PHONE(606,"You've entered an invalid phone number", false, false),
	INVALID_ADDRESS(607, "Invalid address, make sure it doesn't contain more than 45 characters", false, false),
	INVALID_COMPANY_NAME(608, "Invalid company name, make sure it contains between 2 and 45 characters", false, false),
	INVALID_EMAIL(609, "You've entered an invalid e-mail", false, false),
	USER_NAME_TAKEN(610, "This user name is already taken", false, false),
	INVALID_PASSWORD_LENGTH(611, "Your password has to contain between 8 and 18 characters", false, false),
	INVALID_PASSWORD(612, "Your password has to contain at least one number, one letter and no special characters", false, false),
	USER_TYPE_MISSMATCH(613, "This user type can't have a company ID", false, false),
	INVALID_FIRST_NAME(614, "You've entered an invalid first name", false, false),
	INVALID_LAST_NAME(615, "You've entered an invalid last name", false, false),
	LOGIN_FAILED(616, "The username or the password is incorrect, please try again", false, false),
	TITLE_ALREADY_EXISTS(617, "A coupon with this title already exists in your company, please choose a different title", false, false),
	INVALID_TITLE(618, "This title is invalid, make sure it's not empty and doesn't contain more than 45 characters", false, false),
	INVALID_DESCRIPTION(619, "The description is too long, make sure it doesn't contain more than 150 characters", false, false),
	INVALID_AMOUNT(620, "Can't add or update coupons with amount less than 1", false, false),
	INVALID_PRICE(621, "The price has to be a positive number", false, false),
	NOT_ENOUGH_IN_STOCK(622, "There is not enough coupons in stock", false, false),
	INVALID_DATE(623, "The dates you've entered are invalid", false, false),
	INVALID_DATE_UPDATE(624, "Can't shorten the coupon validity period", false, false),
	INVALID_LINK(625, "This image link is too long, make sure it doesn't contain more than 250 characters", false, false),
	COUPON_EXPIRED(626,"This coupon has expired",false, false),
	INVALID_PURCHASE(627,"You have to purchase at least 1 coupon", false, false),
	INVALID_CATEGORY(628,"Category has to be specified", false, false),
	INVALID_USER_TYPE(629,"User type has to be specified", false, false),
	INCORRECT_PASSWORD(630,"The password is incorrect", false, false);


	private int exceptionNumber;
	private String message;
	boolean isPrintStackTrace;
	boolean isUrgent;


	private ExceptionType(int exceptionNumber, String message, boolean isPrintStackTrace, boolean isUrgent) {
		this.exceptionNumber = exceptionNumber;
		this.message = message;
		this.isPrintStackTrace = isPrintStackTrace;
		this.isUrgent = isUrgent;
	}


	public int getExceptionNumber() {
		return exceptionNumber;
	}

	public String getMessage() {
		return message;
	}

	public boolean isPrintStackTrace() {
		return isPrintStackTrace;
	}

	public boolean isUrgent() {
		return isUrgent;
	}
}