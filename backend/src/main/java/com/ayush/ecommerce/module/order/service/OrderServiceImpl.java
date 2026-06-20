package com.ayush.ecommerce.module.order.service;

import com.ayush.ecommerce.exception.*;
import com.ayush.ecommerce.module.auth.entity.User;
import com.ayush.ecommerce.module.auth.repository.UserRepository;
import com.ayush.ecommerce.module.notification.service.EmailService;
import com.ayush.ecommerce.module.order.dto.*;
import com.ayush.ecommerce.module.order.entity.Order;
import com.ayush.ecommerce.module.order.entity.OrderItem;
import com.ayush.ecommerce.module.order.entity.OrderStatus;
import com.ayush.ecommerce.module.order.repository.OrderItemRepository;
import com.ayush.ecommerce.module.order.repository.OrderRepository;
import com.ayush.ecommerce.module.product.entity.Product;
import com.ayush.ecommerce.module.product.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;
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
    private final EmailService emailService;

    @Override
    @Transactional
    @CacheEvict(
            value = "dashboard",
            allEntries = true
    )
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
            if(product.getStockQuantity() < itemRequest.getQuantity()){
                throw new InsufficientStockException(
                        "Insufficient stock for " + product.getName()
                );
            }
            product.setStockQuantity(
                    product.getStockQuantity()
                            - itemRequest.getQuantity()
            );
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
        productRepository.saveAll(products);

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
        emailService.sendEmail(
                user.getEmail(),
                "Order Created Successfully",
                """
                Dear Customer,
        
                Your order has been placed successfully.
        
                Order Number: %s
        
                Total Amount: ₹%s
        
                Current Status: %s
        
                Thank you for shopping with us.
                """
                        .formatted(
                                savedOrder.getOrderNumber(),
                                savedOrder.getTotalAmount(),
                                savedOrder.getStatus()
                        )
        );


        return OrderResponse.builder()
                .orderId(savedOrder.getId())
                .orderNumber(savedOrder.getOrderNumber())
                .status(savedOrder.getStatus())
                .totalAmount(savedOrder.getTotalAmount())
                .items(itemResponses)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getMyOrders(String userEmail) {
        return orderRepository
                .findByUserEmailOrderByCreatedAtDesc(userEmail)
                .stream()
                .map(order ->
                        OrderResponse.builder()
                                .orderId(order.getId())
                                .orderNumber(order.getOrderNumber())
                                .status(order.getStatus())
                                .totalAmount(order.getTotalAmount())
                                .items(List.of())
                                .build()
                )
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderDetails(String userEmail, String orderNumber) {

        Order order = orderRepository
                .findByOrderNumber(orderNumber)
                .orElseThrow(()-> new OrderNotFoundException("Order not found"));
        if(!order.getUser()
                .getEmail()
                .equals(userEmail)){
            throw new OrderNotFoundException("Order not found");
        }
        List<OrderItemResponse> itemResponses = orderItemRepository
                .findByOrderOrderNumber(orderNumber)
                .stream()
                .map(item-> OrderItemResponse.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .subtotal(item.getSubtotal())
                        .build()
                ).toList();

        return OrderResponse.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .items(itemResponses)
                .build();

    }

    @Override
    @Transactional
    @CacheEvict(
            value = "dashboard",
            allEntries = true
    )
    public OrderResponse updateOrderStatus(String orderNumber, UpdateOrderStatusRequest request) {

        Order order = orderRepository
                .findByOrderNumber(orderNumber)
                .orElseThrow(()-> new OrderNotFoundException("Order not fond"));

        if(!isValidTransition(order.getStatus(), request.getStatus())){
            throw new InvalidOrderStatusTransitionException(
                    "Invalid status transition from "
                    +order.getStatus()
                    +" to "
                    +request.getStatus());
        }
        order.setStatus(request.getStatus());
        order.setUpdatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        return OrderResponse.builder()
                .orderId(savedOrder.getId())
                .orderNumber(savedOrder.getOrderNumber())
                .status(savedOrder.getStatus())
                .totalAmount(savedOrder.getTotalAmount())
                .items(List.of())
                .build();

    }

    @Override
    @Transactional
    @CacheEvict(
            value = "dashboard",
            allEntries = true
    )
    public OrderResponse cancelOrder(String userEmail, String orderNumber, CancelOrderRequest request) {
        Order order = orderRepository
                .findByOrderNumber(orderNumber)
                .orElseThrow(()-> new OrderNotFoundException("Order not found"));

        if(!order.getUser().getEmail().equals(userEmail)){
            throw new OrderNotFoundException("Order not found");
        }
        if(order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.PAID){
            throw new OrderCancellationException("Order cannot be cancelled in status "+ order.getStatus());
        }

        List<OrderItem> orderItems = orderItemRepository.findByOrderOrderNumber(orderNumber);
        for(OrderItem item :orderItems){
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity()+ item.getQuantity());
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());

        productRepository.saveAll(orderItems.stream()
                .map(OrderItem::getProduct)
                .toList());

        Order savedOrder = orderRepository.save(order);
        List<OrderItemResponse> itemResponses = orderItems.stream()
                .map(item-> OrderItemResponse.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .subtotal(item.getSubtotal())
                        .build()
                ).toList();

        return OrderResponse.builder()
                .orderId(savedOrder.getId())
                .orderNumber(savedOrder.getOrderNumber())
                .status(savedOrder.getStatus())
                .totalAmount(savedOrder.getTotalAmount())
                .items(itemResponses)
                .build();

    }

    private boolean isValidTransition(OrderStatus currentStatus, OrderStatus newStatus){
        return switch (currentStatus){
            case PENDING -> newStatus == OrderStatus.PAID
                    || newStatus == OrderStatus.CANCELLED;

            case PAID -> newStatus == OrderStatus.PROCESSING
                    || newStatus == OrderStatus.CANCELLED;

            case PROCESSING -> newStatus == OrderStatus.SHIPPED;

            case SHIPPED -> newStatus == OrderStatus.DELIVERED;

            case DELIVERED, CANCELLED -> false;
        };
    }
}
