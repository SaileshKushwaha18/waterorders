package com.example.rubiconwater.service;

import com.example.rubiconwater.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    public Order createOrder(Order orders);

    public Order updateOrder(Order orders, Long id);

    public List<Order> allOrder();

    public Optional<Order> findOrder(Long id);

    public void deleteOrder(Long id);

    public List<Order> findEligibleOrders();

    public void startProcess();
}
