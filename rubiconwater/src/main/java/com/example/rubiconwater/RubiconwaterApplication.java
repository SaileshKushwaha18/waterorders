package com.example.rubiconwater;

import com.example.rubiconwater.model.Order;
import com.example.rubiconwater.model.OrderStatus;
import com.example.rubiconwater.repository.OrderRepository;
import com.example.rubiconwater.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.format.datetime.standard.DateTimeFormatterFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

@SpringBootApplication
public class RubiconwaterApplication {

    @Autowired
    private OrderService orderService;

    public static void main(String[] args) {
        SpringApplication.run(RubiconwaterApplication.class, args);
    }

}
