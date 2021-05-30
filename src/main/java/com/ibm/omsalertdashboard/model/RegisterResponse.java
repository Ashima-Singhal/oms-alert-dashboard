package com.ibm.omsalertdashboard.model;

public class RegisterResponse {

	String username;
	String role;
	String message;
	public RegisterResponse(String username, String role, String message) {
		super();
		this.username = username;
		this.role = role;
		this.message = message;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
