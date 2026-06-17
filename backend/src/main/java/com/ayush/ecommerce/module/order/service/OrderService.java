package com.ayush.ecommerce.module.order.service;

import com.ayush.ecommerce.module.order.dto.CreateOrderRequest;
import com.ayush.ecommerce.module.order.dto.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(
            String userEmail,
            CreateOrderRequest request
    );
}
