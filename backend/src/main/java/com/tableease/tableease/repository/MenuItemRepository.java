package com.tableease.tableease.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.tableease.tableease.model.MenuItem;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
	boolean existsByName(String name);
}
