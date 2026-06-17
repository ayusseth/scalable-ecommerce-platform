package com.ayush.ecommerce.module.order.service;

import com.ayush.ecommerce.module.auth.repository.UserRepository;
import com.ayush.ecommerce.module.order.dto.CreateOrderRequest;
import com.ayush.ecommerce.module.order.dto.OrderResponse;
import com.ayush.ecommerce.module.order.repository.OrderItemRepository;
import com.ayush.ecommerce.module.order.repository.OrderRepository;
import com.ayush.ecommerce.module.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    @Override
    public OrderResponse createOrder(
            String userEmail,
            CreateOrderRequest request) {
        return null;
    }
}
