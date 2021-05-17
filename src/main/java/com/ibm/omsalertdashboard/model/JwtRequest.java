package com.ibm.omsalertdashboard.model;

public class JwtRequest {

	private String username;
	private String password;
	//private String role;
	
	
	public JwtRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
		//this.role = role;
	}
	public String getUserName() { 
		return username;
	}
	public void setUserName(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
//	public String getRole() {
//		return role;
//	}
//	public void setRole(String role) {
//		this.role = role;
//	}
	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + "]";
	}
	
	
}