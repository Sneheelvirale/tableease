package com.tableease.menu_service.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.tableease.menu_service.model.MenuItem;
import com.tableease.menu_service.repository.MenuItemRepository;

@RestController
@RequestMapping("/api")
public class MenuItemController {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @GetMapping("/menu")
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    @PostMapping("/menu")
    public MenuItem createMenuItem(@Valid @RequestBody MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    @PutMapping("/menu/{id}")
    public MenuItem updateMenuItem(@PathVariable Integer id, @Valid @RequestBody MenuItem menuItem) {
        menuItem.setId(id);
        return menuItemRepository.save(menuItem);
    }

    @DeleteMapping("/menu/{id}")
    public void deleteMenuItem(@PathVariable Integer id) {
        menuItemRepository.deleteById(id);
    }
}