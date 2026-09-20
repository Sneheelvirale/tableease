package com.tableease.tableease.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tableease.tableease.dto.AuthResponse;
import com.tableease.tableease.dto.LoginRequest;
import com.tableease.tableease.dto.RegisterRequest;
import com.tableease.tableease.dto.UserResponse;
import com.tableease.tableease.model.Role;
import com.tableease.tableease.model.User;
import com.tableease.tableease.repository.UserRepository;
import com.tableease.tableease.util.JwtUtil;

@Service
public class AuthService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	
	public User register(RegisterRequest request) {
		Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
		if(existingUser.isPresent()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
		}
		String hashedPassword = passwordEncoder.encode(request.getPassword());
		User newUser = new User(null, request.getUsername(), hashedPassword, Role.CUSTOMER);
		return userRepository.save(newUser);
	}
	
	public AuthResponse login(LoginRequest request) {
		Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
		if(userOptional.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid username or password");
		}
		User user = userOptional.get();
		if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalind username or password");
		}
		String token = jwtUtil.generateToken(user);
		UserResponse userResponse = new UserResponse(user);
		return new AuthResponse(token, userResponse);
	}
}
