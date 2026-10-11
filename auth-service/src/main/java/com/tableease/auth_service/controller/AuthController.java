package com.tableease.auth_service.controller;

import com.tableease.auth_service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> request) {

        return Map.of("message", "User registered successfully!");
    }
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        if ("admin".equals(username) && "password123".equals(password)) {
            String token = jwtUtil.generateToken(username);
            return Map.of("token", token);
        } else {
            throw new RuntimeException("Invalid Credentials");
        }
    }
}