package com.tableease.tableease.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tableease.tableease.model.DiningTable;
import com.tableease.tableease.repository.DiningTableRepository;

@Service
public class DiningTableService {
	@Autowired
	private DiningTableRepository diningTableRepository;
	
	public List<DiningTable> getAllTables(){
		return diningTableRepository.findAll();
	}
	
	public DiningTable createTable(DiningTable diningTable) {
	    return diningTableRepository.save(diningTable);
	}
	
	public DiningTable updateTable(Integer id, DiningTable diningTable) {
		Optional<DiningTable> existingTable = diningTableRepository.findById(id);
		if(existingTable.isPresent()) {
			DiningTable tableToUpdate = existingTable.get();
			tableToUpdate.setStatus(diningTable.getStatus());
			tableToUpdate.setTableNumber(diningTable.getTableNumber());
			tableToUpdate.setCapacity(diningTable.getCapacity());
	    	return diningTableRepository.save(tableToUpdate);
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,  "Table not found with id: " + id);
		}
	}
	
	public void deleteTable(Integer id) {
		if(!diningTableRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Table not found with id: "+id);
		}else {
			diningTableRepository.deleteById(id);
		}
	}
}
