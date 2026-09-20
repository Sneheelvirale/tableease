package com.tableease.tableease.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tableease.tableease.model.DiningTable;
import com.tableease.tableease.service.DiningTableService;

import jakarta.validation.Valid;

@RestController
public class DiningTableController {
	@Autowired
	private DiningTableService diningTableService;
	
	@GetMapping("/tables")
	public List<DiningTable> getAllTables(){
		return diningTableService.getAllTables();
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/tables")
	public DiningTable createDiningTable(@Valid  @RequestBody DiningTable diningTable) {
		return diningTableService.createTable(diningTable);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/tables/{id}")
	public DiningTable updateDiningTable(@PathVariable Integer id,@Valid @RequestBody DiningTable diningTable){
		return diningTableService.updateTable(id, diningTable);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/tables/{id}")
	public void deleteDiningTable(@PathVariable Integer id) {
		diningTableService.deleteTable(id);
	}	
}
	
