package com.ayush.ecommerce.module.order.repository;

import com.ayush.ecommerce.module.order.entity.Order;
import com.ayush.ecommerce.module.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByUserEmailOrderByCreatedAtDesc(String email);

    @Query("""
       SELECT COALESCE(SUM(o.totalAmount),0)
       FROM Order o
       WHERE o.status <> 'CANCELLED'
       """)
    BigDecimal getTotalRevenue();

    long countByStatus(OrderStatus status);
}
