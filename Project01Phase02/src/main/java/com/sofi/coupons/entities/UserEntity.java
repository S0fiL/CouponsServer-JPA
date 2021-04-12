package com.sofi.coupons.entities;

import java.io.Serializable;
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

import com.sofi.coupons.beans.dto.UserDto;
import com.sofi.coupons.enums.UserType;

@Entity
@Table(name = "users")
public class UserEntity implements Serializable{

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	@Column(name = "user_name", unique = true, nullable = false)
	private String userName;

	@Column(name = "password", unique = false, nullable = false)
	private String password;

	@Column(name = "user_type", unique = false, nullable = false)
	private UserType userType;

	@Column(name = "first_name", unique = false, nullable = false)
	private String firstName;

	@Column(name = "last_name", unique = false, nullable = false)
	private String lastName;

	@ManyToOne
	private CompanyEntity company;

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<PurchaseEntity> purchases;


	public UserEntity() {
		super();
	}

	public UserEntity(UserDto userDto, CompanyEntity company) {
		super();
		this.id = userDto.getId();
		this.userName = userDto.getUserName();
		this.password = userDto.getPassword();
		this.userType = userDto.getUserType();
		this.firstName = userDto.getFirstName();
		this.lastName = userDto.getLastName();
		this.company = company;
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

	public CompanyEntity getCompanyEntity() {
		return company;
	}

	public void setCompanyEntity(CompanyEntity company) {
		this.company = company;
	}

	public List<PurchaseEntity> getPurchases() {
		return purchases;
	}

	public void setPurchases(List<PurchaseEntity> purchases) {
		this.purchases = purchases;
	}

	public Long getCompanyId() {
		if(this.company == null) {
			return null;
		}
		return this.company.getId();
	}

	@Override
	public String toString() {
		return "ID: " + id + ", User Name: " + userName + ", Password: " + password + ", User Type: " + userType
				+ ", First Name: " + firstName + ", Last Name: " + lastName + ", Company: " + company
				+ ", Purchases: " + purchases;
	}
}