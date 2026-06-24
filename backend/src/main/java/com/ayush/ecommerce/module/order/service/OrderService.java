package com.ayush.ecommerce.module.order.service;

import com.ayush.ecommerce.module.order.dto.CancelOrderRequest;
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

    List<OrderResponse> getAllOrders();

    OrderResponse getOrderDetails(String userEmail, String orderNumber);

    OrderResponse updateOrderStatus(String orderNumber, UpdateOrderStatusRequest request);

    OrderResponse cancelOrder(String userEmail, String orderNumber, CancelOrderRequest request);

    List<OrderResponse> searchOrders(
            String keyword
    );



}
