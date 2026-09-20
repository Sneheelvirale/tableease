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

import com.tableease.tableease.model.MenuItem;
import com.tableease.tableease.service.MenuItemService;

import jakarta.validation.Valid;

@RestController
public class MenuItemController {
	
	@Autowired
	private MenuItemService menuItemService;
	
	@GetMapping("/menu")
	public List<MenuItem> getAllMenuItems() {
		return menuItemService.getAllMenuItems();
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/menu")
	public MenuItem createMenuItem(@Valid @RequestBody MenuItem menuItem){
		return menuItemService.createMenuItem(menuItem);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/menu/{id}")
	public MenuItem updateMenuItem(@PathVariable Integer id ,@Valid @RequestBody MenuItem menuItem) {
		return menuItemService.updateMenuItem(id, menuItem);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/menu/{id}")
	public void deleteMenuItem(@PathVariable Integer id) {
		menuItemService.deleteMenuItem(id);
	}
}
