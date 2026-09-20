package com.tableease.tableease.dto;

import com.tableease.tableease.model.Role;
import com.tableease.tableease.model.User;

public class UserResponse {
	private Long id;
	private String username;
	private Role role;
	
	public UserResponse() {
	    super();
	}
	
	public UserResponse(Long id, String username, Role role) {
		super();
		this.id = id;
		this.username = username;
		this.role = role;
	}
	
	public UserResponse(User user) {
	    this.id = user.getId();
	    this.username = user.getUsername();
	    this.role = user.getRole();
	}
	
	public Long getId() {
	    return id;
	}
	public void setId(Long id) {
	    this.id = id;
	}
	public String getUsername() {
	    return username;
	}
	public void setUsername(String username) {
	    this.username = username;
	}
	public Role getRole() {
	    return role;
	}
	public void setRole(Role role) {
	    this.role = role;
	}
	
}
