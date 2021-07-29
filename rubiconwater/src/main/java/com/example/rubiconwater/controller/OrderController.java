package com.example.rubiconwater.controller;

import com.example.rubiconwater.model.Order;
import com.example.rubiconwater.model.OrderStatus;
import com.example.rubiconwater.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrder() {
        logger.info("Get All Orders.");
        return ResponseEntity.ok(orderService.allOrder());
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Optional<Order>> getOrder(@PathVariable Long id) {
        logger.info("Get Orders by Id.");
        Optional<Order> order1 = orderService.findOrder(id);
        if(order1 != null && order1.isPresent()) {
            return ResponseEntity.ok().body(order1);
        } else {
            return (ResponseEntity<Optional<Order>>) ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        logger.info("Delete Orders by Id.");
        orderService.deleteOrder(id);
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> postOrders(@RequestBody Order order) {
        logger.info("Order has been placed but not yet delivered.");
        order.setOrderStatus(OrderStatus.REQUESTED);
        Order order1 = orderService.createOrder(order);

        logger.info("Order has been placed but not yet delivered.");
        System.out.println(order1.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(order1);
    }

    @PutMapping("/orders/{id}/cancelled")
    public ResponseEntity<Order> cancelOrders(@RequestBody Order order, @PathVariable Long id) {
        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setUpdatedTime(LocalDateTime.now());
        Order order1 = orderService.updateOrder(order, id);

        logger.info("Order was cancelled before delivery.");
        System.out.println(order1.toString());
        return ResponseEntity.status(HttpStatus.OK).body(order1);
    }

    @PutMapping("/orders/{id}/delivered")
    public ResponseEntity<Order> deliverOrder(@RequestBody Order order, @PathVariable Long id) {
        order.setOrderStatus(OrderStatus.DELIVERED);
        order.setUpdatedTime(LocalDateTime.now());
        Order order1 = orderService.updateOrder(order, id);

        logger.info("Order has been delivered.");
        System.out.println(order1.toString());
        return ResponseEntity.status(HttpStatus.OK).body(order1);
    }

    @PutMapping("/orders/{id}/inProgress")
    public ResponseEntity<Order> inProgressOrder(@RequestBody Order order, @PathVariable Long id) {
        order.setOrderStatus(OrderStatus.INPROGRESS);
        order.setUpdatedTime(LocalDateTime.now());
        Order order1 = orderService.updateOrder(order, id);

        logger.info("Order is being delivered right now.");
        System.out.println(order1.toString());
        return ResponseEntity.status(HttpStatus.OK).body(order1);
    }

    @PostMapping("/startProcess")
    public void startProcess() {
        orderService.startProcess();
    }
}
