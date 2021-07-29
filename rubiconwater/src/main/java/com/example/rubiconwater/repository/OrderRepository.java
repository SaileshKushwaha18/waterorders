package com.example.rubiconwater.repository;

import com.example.rubiconwater.model.Order;
import com.example.rubiconwater.model.OrderStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findByFarmIdAndEndTimeIsAfter(Long farmId, LocalDateTime startTime);

    List<Order> findAllByOrderStatusIsNotIn(List<OrderStatus> orderStatuses);
}
