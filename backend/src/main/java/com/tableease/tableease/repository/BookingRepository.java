package com.tableease.tableease.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tableease.tableease.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Integer>{

}
