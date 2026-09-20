package com.tableease.tableease.dto;

public class AuthResponse {
	
	private String token;
	private UserResponse user;
	
	public AuthResponse() {
		super();
	}
	
	public AuthResponse(String token, UserResponse user) {
		super();
		this.token = token;
		this.user = user;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public UserResponse getUser() {
		return user;
	}
	public void setUser(UserResponse user) {
		this.user = user;
	}
}
