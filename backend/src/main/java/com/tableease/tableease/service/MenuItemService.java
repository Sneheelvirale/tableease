package com.tableease.tableease.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tableease.tableease.model.MenuItem;
import com.tableease.tableease.repository.MenuItemRepository;

@Service
public class MenuItemService {
	
	@Autowired
	private MenuItemRepository  menuItemRepository;
	
	public List<MenuItem> getAllMenuItems(){
		return menuItemRepository.findAll();
	}
	
	public MenuItem createMenuItem(MenuItem menuItem) {
		return menuItemRepository.save(menuItem);
	}
	
	public MenuItem updateMenuItem(Integer id, MenuItem menuItem) {
	    Optional<MenuItem> existingItem = menuItemRepository.findById(id);
	    if(existingItem.isPresent()) {
	    	MenuItem itemToUpdate = existingItem.get();
	    	itemToUpdate.setName(menuItem.getName());
	    	itemToUpdate.setPrice(menuItem.getPrice());
	    	return menuItemRepository.save(itemToUpdate);
	    }else {
	    	throw new ResponseStatusException(HttpStatus.NOT_FOUND,  "Menu item not found with id: " + id);
	    }
	}
	
	public void deleteMenuItem(Integer id) {
		if(!menuItemRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Menu item not found with id: "+id);
		}else {
			menuItemRepository.deleteById(id);
		}
		
	}
}
