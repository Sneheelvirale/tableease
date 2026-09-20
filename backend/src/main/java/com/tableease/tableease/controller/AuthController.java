package com.tableease.tableease.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tableease.tableease.dto.AuthResponse;
import com.tableease.tableease.dto.LoginRequest;
import com.tableease.tableease.dto.RegisterRequest;
import com.tableease.tableease.dto.UserResponse;
import com.tableease.tableease.model.User;
import com.tableease.tableease.service.AuthService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping("/register")
	public UserResponse register(@Valid @RequestBody RegisterRequest request) {
		User user = authService.register(request);
		return new UserResponse(user);
	}
	
	@PostMapping("/login")
	public AuthResponse login(@Valid @RequestBody LoginRequest request) {
		return authService.login(request);
	}
}

