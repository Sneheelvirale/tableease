package com.tableease.tableease.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.tableease.tableease.model.User;

import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {
	private final SecretKey secretKey =  Jwts.SIG.HS256.key().build();
	private final long expirationMs = 1000*60*60;
	
	public String generateToken(User user) {
		return Jwts.builder()
			.subject(user.getUsername())
			.claim("role", user.getRole().name())
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis()+ expirationMs))
			.signWith(secretKey)
			.compact();
	}
	
	public String extractUsername(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.getSubject();
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
	
	public String extractRole(String token) {
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.get("role", String.class);
	}
}
