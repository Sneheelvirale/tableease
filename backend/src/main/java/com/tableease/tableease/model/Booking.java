package com.tableease.tableease.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
public class Booking {
	
	@ManyToOne
	private DiningTable table;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private LocalDateTime bookingTime;
	@NotBlank
	private String customerName;
	@Positive
	private int guestCount;
	
	
	public Booking() {
		super();
	}
	public Booking(DiningTable table, Integer id, LocalDateTime bookingTime, @NotBlank String customerName,
			@Positive int guestCount) {
		super();
		this.table = table;
		this.id = id;
		this.bookingTime = bookingTime;
		this.customerName = customerName;
		this.guestCount = guestCount;
	}
	public DiningTable getTable() {
		return table;
	}
	public void setTable(DiningTable table) {
		this.table = table;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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

