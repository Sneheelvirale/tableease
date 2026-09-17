package com.tableease.tableease.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BookingRequest {
	
	@NotNull
	private Integer tableId;
	private LocalDateTime bookingTime;
	@NotBlank
	private String customerName;
	@Positive
	private int guestCount;
	
	public BookingRequest() {
		super();
	}
	public BookingRequest(Integer tableId, LocalDateTime bookingTime, @NotBlank String customerName,
			@Positive int guestCount) {
		super();
		this.tableId = tableId;
		this.bookingTime = bookingTime;
		this.customerName = customerName;
		this.guestCount = guestCount;
	}
	
	public Integer getTableId() {
		return tableId;
	}
	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}
	public LocalDateTime getBookingTime() {
		return bookingTime;
	}
	public void setBookingTime(LocalDateTime bookingTime) {
		this.bookingTime = bookingTime;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public int getGuestCount() {
		return guestCount;
	}
	public void setGuestCount(int guestCount) {
		this.guestCount = guestCount;
	}
	
	
}
