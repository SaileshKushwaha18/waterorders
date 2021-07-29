package com.example.rubiconwater.service;

import com.example.rubiconwater.model.Order;
import com.example.rubiconwater.model.OrderStatus;
import com.example.rubiconwater.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public Order createOrder(Order order) {
        logger.info("Calling CreateOrder service method");

        List<Order> ordersDB = (List<Order>) orderRepository.findByFarmIdAndEndTimeIsAfter(order.getFarmId(), order.getStartTime());

        boolean isOverlapping = false;

        for (Order order1 : ordersDB) {
            if ((order1.getOrderStatus().equals(OrderStatus.REQUESTED) ||
                    order1.getOrderStatus().equals(OrderStatus.INPROGRESS))) {
                isOverlapping = true;
                break;
            }
        }

        if (isOverlapping) {
            logger.info("Overlapping Order found for the duration, StartTime={0}, EndTime={1} , FirmId = {2}", order.getStartTime(), order.getEndTime(), order.getFarmId());
            throw new RuntimeException("Overlapping Order found for the farm=" + order.getFarmId());
        } else {
            if (order.getDuration() > 0) {
                if (order.getStartTime() != null) {
                    LocalDateTime localDateTime = order.getStartTime();
                    LocalDateTime endDateTime = localDateTime.plusHours(order.getDuration());
                    order.setEndTime(endDateTime);
                }
            }

            if (order.getOrderStatus() != null) {
                order.setOrderStatus(order.getOrderStatus());
            } else {
                order.setOrderStatus(OrderStatus.REQUESTED);
            }

            order.setCreatedTime(LocalDateTime.now());
        }
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Order order, Long id) {
        logger.info("Calling UpdateOrder service method");
        Optional<Order> orderDB = orderRepository.findById(id);

        if (orderDB != null && !orderDB.isPresent()) {
            logger.info("Order with id = {0} not found", id);
        } else {
            if (order.getDuration() > 0) {
                orderDB.get().setDuration(order.getDuration());

                if (order.getStartTime() != null) {
                    LocalDateTime localDateTime = order.getStartTime();
                    localDateTime.plusHours(order.getDuration());
                    orderDB.get().setEndTime(localDateTime);
                }
            }

            if (order.getOrderStatus() != null) {
                orderDB.get().setOrderStatus(order.getOrderStatus());
            }

            orderDB.get().setUpdatedTime(LocalDateTime.now());
        }
        return orderRepository.save(orderDB.get());
    }

    @Override
    public List<Order> allOrder() {
        logger.info("Calling allOrder service method");
        return (List<Order>) orderRepository.findAll();
    }

    @Override
    public Optional<Order> findOrder(Long id) {
        logger.info("Calling findOrder by Id service method");
        return orderRepository.findById(id);
    }

    @Override
    public void deleteOrder(Long id) {
        logger.info("Calling deleteOrder service method");
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> findEligibleOrders(){
        List<OrderStatus> orderStatuses = new ArrayList<>();
        orderStatuses.add(OrderStatus.CANCELLED);
        orderStatuses.add(OrderStatus.DELIVERED);
        return (List<Order>) orderRepository.findAllByOrderStatusIsNotIn(orderStatuses);
    }

    @Override
    public void startProcess(){
        while(true){
            List<Order> ordersDB = findEligibleOrders();
            Iterator iterator = ordersDB.listIterator();
            while(iterator.hasNext()){
                Order order = (Order) iterator.next();
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//                LocalDateTime currentDateTime = LocalDateTime.parse(LocalDateTime.now().toString(), formatter);
                LocalDateTime currentDateTime = LocalDateTime.now();
                if(currentDateTime.isAfter(order.getStartTime()) && currentDateTime.isBefore(order.getEndTime())){
                    System.out.println("Order is InProgress : OrderStatus="+OrderStatus.INPROGRESS);
                    order.setOrderStatus(OrderStatus.INPROGRESS);
                    System.out.println(order.toString());
                } else if(currentDateTime.isBefore(order.getStartTime())){
                    System.out.println("Order is Requested : OrderStatus="+OrderStatus.REQUESTED);
                    System.out.println(order.toString());
                } else if(currentDateTime.isAfter(order.getEndTime())) {
                    System.out.println("Order is Delivered : OrderStatus="+OrderStatus.DELIVERED);
                    order.setOrderStatus(OrderStatus.DELIVERED);
                    System.out.println(order.toString());
                }
                updateOrder(order,order.getId());
            }
        }
    }
}
