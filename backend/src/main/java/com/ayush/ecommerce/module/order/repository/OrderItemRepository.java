package com.ayush.ecommerce.module.order.repository;

import com.ayush.ecommerce.module.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
