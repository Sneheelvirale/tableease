package com.tableease.tableease;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.tableease.tableease.model.MenuItem;
import com.tableease.tableease.repository.MenuItemRepository;

@SpringBootApplication
public class TableeaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(TableeaseApplication.class, args);
	}
	
	
}
