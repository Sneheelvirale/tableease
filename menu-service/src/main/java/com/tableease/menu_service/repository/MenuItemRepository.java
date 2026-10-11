package com.tableease.menu_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tableease.menu_service.model.MenuItem;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
    boolean existsByName(String name);
}