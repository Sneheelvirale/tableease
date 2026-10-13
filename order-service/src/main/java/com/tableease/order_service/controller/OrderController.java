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
        // Feign Client call to menu-service
        Map<String, Object> menuItem = menuClient.getMenuItemById(order.getMenuId());
        
        if (menuItem != null && menuItem.get("price") != null) {
            Double price = Double.valueOf(menuItem.get("price").toString());
            order.setTotalPrice(price * order.getQuantity());
        } else {
            order.setTotalPrice(0.0);
        }
        
        order.setStatus("CREATED");
        return orderRepository.save(order);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}