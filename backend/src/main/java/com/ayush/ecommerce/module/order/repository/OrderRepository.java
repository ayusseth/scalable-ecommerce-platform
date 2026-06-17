package com.ayush.ecommerce.module.order.repository;

import com.ayush.ecommerce.module.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNumber(String orderNumber);
}
