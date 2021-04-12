package com.sofi.coupons.beans;

public class ExceptionBean {

	private int num;
	private String name;
	private String message;


	public ExceptionBean(int num, String name, String message) {
		super();
		this.num = num;
		this.name = name;
		this.message = message;
	}


	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getType() {
		return name;
	}

	public void setType(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Num: " + num + ", Name: " + name + ", Message: " + message;
	}
}