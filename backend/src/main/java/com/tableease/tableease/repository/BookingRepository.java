package com.tableease.tableease.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tableease.tableease.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Integer>{
	List<Booking> findByCustomerName(String customerName);
}
