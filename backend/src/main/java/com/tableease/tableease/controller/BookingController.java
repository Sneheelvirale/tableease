package com.tableease.tableease.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tableease.tableease.dto.BookingRequest;
import com.tableease.tableease.model.Booking;
import com.tableease.tableease.service.BookingService;

import jakarta.validation.Valid;

@RestController
public class BookingController {
	@Autowired
	private BookingService bookingService;
	
	@GetMapping("/bookings")
	public List<Booking> getAllBooking(Authentication authentication){
		String currentUsername = authentication.getName();
		return bookingService.getBookingsByCustomerName(currentUsername);
	}
	
	@PostMapping("/bookings")
	public Booking createBooking(@Valid @RequestBody BookingRequest bookingRequest, Authentication authentication) {
		bookingRequest.setCustomerName(authentication.getName());
		return bookingService.createBooking(bookingRequest);
	}
	
	
	@DeleteMapping("/bookings/{id}")
	public ResponseEntity<?> cancelBooking(@PathVariable Integer id, Authentication authentication) {
	    if (authentication == null || !authentication.isAuthenticated()) {
	        return ResponseEntity.status(401).body("User not authenticated.");
	    }
	    
	    String currentUsername = authentication.getName();
	    boolean isCancelled = bookingService.cancelUserBooking(id, currentUsername);
	    
	    if (isCancelled) {
	        return ResponseEntity.ok("Booking cancelled successfully.");
	    } else {
	        return ResponseEntity.status(403).body("Unauthorized or booking not found.");
	    }
	}
}

