package com.ayush.ecommerce.module.order.service;

import com.ayush.ecommerce.exception.ProductNotFoundException;
import com.ayush.ecommerce.exception.UserNotFoundException;
import com.ayush.ecommerce.module.auth.entity.User;
import com.ayush.ecommerce.module.auth.repository.UserRepository;
import com.ayush.ecommerce.module.order.dto.CreateOrderRequest;
import com.ayush.ecommerce.module.order.dto.OrderItemResponse;
import com.ayush.ecommerce.module.order.dto.OrderResponse;
import com.ayush.ecommerce.module.order.entity.Order;
import com.ayush.ecommerce.module.order.entity.OrderItem;
import com.ayush.ecommerce.module.order.entity.OrderStatus;
import com.ayush.ecommerce.module.order.repository.OrderItemRepository;
import com.ayush.ecommerce.module.order.repository.OrderRepository;
import com.ayush.ecommerce.module.product.entity.Product;
import com.ayush.ecommerce.module.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        User user = userRepository
                .findByEmail(userEmail)
                .orElseThrow(()-> new UserNotFoundException("User not found"));
        List<Long> productIds = request.getItems()
                .stream()
                .map(item->item.getProductId())
                .toList();

        List<Product> products = productRepository
                .findByIdInAndActiveTrue(productIds);
        if(products.size()!=productIds.size()){
            throw new ProductNotFoundException("One or more products not found");
        }

        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(
                        Product::getId,
                        Function.identity()
                ));

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItemResponse> itemResponses = new ArrayList<>();
        List<OrderItem> orderItems = new ArrayList<>();

        for(var itemRequest: request.getItems()){
            Product product = productMap.get(itemRequest.getProductId());
            BigDecimal subtotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            totalAmount = totalAmount.add(subtotal);

            itemResponses.add(OrderItemResponse.builder()
                            .productId(product.getId())
                            .productName(product.getName())
                            .quantity(itemRequest.getQuantity())
                            .unitPrice(product.getPrice())
                            .subtotal(subtotal)
                    .build());
            orderItems.add(
                    OrderItem.builder()
                            .product(product)
                            .quantity(itemRequest.getQuantity())
                            .unitPrice(product.getPrice())
                            .subtotal(subtotal)
                            .build()
            );
        }

        String orderNumber = "ORD-"+ UUID.randomUUID()
                .toString()
                .substring(0,8)
                .toUpperCase();

        Order order = Order.builder()
                .orderNumber(orderNumber)
                .user(user)
                .status(OrderStatus.PENDING)
                .totalAmount(totalAmount)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);

        for(OrderItem orderItem:orderItems){
            orderItem.setOrder(savedOrder);
            orderItemRepository.save(orderItem);
        }


        return OrderResponse.builder()
                .orderId(savedOrder.getId())
                .orderNumber(savedOrder.getOrderNumber())
                .status(savedOrder.getStatus())
                .totalAmount(savedOrder.getTotalAmount())
                .items(itemResponses)
                .build();
    }
}
