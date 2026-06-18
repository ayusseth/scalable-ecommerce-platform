package com.ayush.ecommerce.module.order.service;

import com.ayush.ecommerce.module.order.dto.CreateOrderRequest;
import com.ayush.ecommerce.module.order.dto.OrderResponse;
import com.ayush.ecommerce.module.order.dto.UpdateOrderStatusRequest;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(
            String userEmail,
            CreateOrderRequest request
    );

    List<OrderResponse> getMyOrders(String userEmail);

    OrderResponse getOrderDetails(String userEmail, String orderNumber);

    OrderResponse updateOrderStatus(String orderNumber, UpdateOrderStatusRequest request);
}
