package com.tableease.order_service.controller;

import com.tableease.order_service.client.MenuClient;
import com.tableease.order_service.entity.Order;
import com.tableease.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuClient menuClient;

    @PostMapping
    public Order placeOrder(@RequestBody Order order) {
        // Menu Service se item details fetch karna using OpenFeign
        Object menuItem = menuClient.getMenuItemById(order.getMenuId());
        
        // Yahan aap price calculate karke order save kar sakte hain
        order.setStatus("CREATED");
        order.setTotalPrice(299.0); // Dummy or parsed from menuItem
        
        return orderRepository.save(order);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}