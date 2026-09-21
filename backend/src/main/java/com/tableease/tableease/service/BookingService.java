package com.tableease.tableease.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tableease.tableease.dto.BookingRequest;
import com.tableease.tableease.model.Booking;
import com.tableease.tableease.model.DiningTable;
import com.tableease.tableease.repository.BookingRepository;
import com.tableease.tableease.repository.DiningTableRepository;

@Service
public class BookingService {
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private DiningTableRepository diningTableRepository;
	
	public Booking createBooking(BookingRequest request) {
	    Optional<DiningTable> tableOptional = diningTableRepository.findById(request.getTableId());
	    if(tableOptional.isEmpty()) {
	    	throw new ResponseStatusException(HttpStatus.NOT_FOUND,"tABLE NOT FOUND WITH ID:" +request.getTableId());
	    }else {
	    	DiningTable table = tableOptional.get();
	    	Booking booking = new Booking(table, null ,request.getBookingTime(),request.getCustomerName(),request.getGuestCount());
	    	return bookingRepository.save(booking);
	    }
	}
	
	public List<Booking> getAllBooking() {
		return bookingRepository.findAll();
	}
	
	public List<Booking> getBookingsByCustomerName(String customerName){
		return bookingRepository.findByCustomerName(customerName);
	}
	
	
	
	public boolean cancelUserBooking(Integer bookingId, String customerName) {
	    Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
	    
	    if (optionalBooking.isPresent()) {
	        Booking booking = optionalBooking.get();
	        // Safe equals check to prevent NullPointerException
	        if (customerName != null && customerName.equalsIgnoreCase(booking.getCustomerName())) {
	            bookingRepository.deleteById(bookingId);
	            return true;
	        }
	    }
	    return false;
	}

	
}
