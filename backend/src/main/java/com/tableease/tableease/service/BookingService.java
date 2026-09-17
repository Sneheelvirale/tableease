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
	
	public void deleteBooking(Integer id) {
		if(!bookingRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Booking not found with id:"+id);
		}else {
			bookingRepository.deleteById(id);
		}
	}
}
