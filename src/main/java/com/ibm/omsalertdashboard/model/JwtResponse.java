package com.ibm.omsalertdashboard.model;

public class JwtResponse {

	String token;
	String username;
	String role;
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public JwtResponse() {
		
	}

	public JwtResponse(String token,String username,String role) {
		this.token = token;
		this.username = username;
		this.role = role;
	}
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
