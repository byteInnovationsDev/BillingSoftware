package com.byteInnovations.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table	(name = "BI_MA_USER") 
public class User {
	
	@Id
	@Column(name = "USER_ID")
	private int userId;
	@Column(name = "USER_NAME")
	private String UserName;
	@Column(name = "USER_TYPE")
	private String userType;	
	@Column(name = "USER_PASSWORD")
	private String userPassword;
	@Column(name = "USER_FULL_NAME")
	private String userFullName;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getUserFullName() {
		return userFullName;
	}
	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}
	public User(int userId, String userName, String userType, String password) {
		super();
		this.userId = userId;
		UserName = userName;
		this.userType = userType;
		this.userPassword = password;
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", UserName=" + UserName + ", userType=" + userType + ", password=" + userPassword
				+ "]";
	}
	
	
	

}
