package com.tableease.tableease.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	public List<Booking> getAllBooking(){
		return bookingService.getAllBooking();
	}
	
	@PostMapping("/bookings")
	public Booking createBooking(@Valid @RequestBody BookingRequest bookingRequest) {
		return bookingService.createBooking(bookingRequest);
	}
	
	
	@DeleteMapping("/bookings/{id}")
	public void deleteBooking(@PathVariable Integer id) {
		bookingService.deleteBooking(id);
	}
}

