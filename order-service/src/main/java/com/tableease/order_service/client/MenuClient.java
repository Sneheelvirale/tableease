package com.tableease.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "menu-service")
public interface MenuClient {

    @GetMapping("/api/menu")
    List<Object> getAllMenuItems();

    @GetMapping("/api/menu/{id}")
    Object getMenuItemById(@PathVariable("id") Long id);
}