package com.ayush.ecommerce.module.order.controller;

import com.ayush.ecommerce.module.order.dto.CreateOrderRequest;
import com.ayush.ecommerce.module.order.dto.OrderResponse;
import com.ayush.ecommerce.module.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(
            Authentication authentication,
            @Valid @RequestBody CreateOrderRequest request
    ) {

        String email = authentication.getName();

        return orderService.createOrder(
                email,
                request
        );
    }

    @GetMapping("/my-orders")
    public List<OrderResponse> getMyOrders(Authentication authentication){
        return orderService.getMyOrders(authentication.getName());
    }

    @GetMapping("/{orderNumber}")
    public OrderResponse getOrderDetails(Authentication authentication, @PathVariable String orderNumber){
        return orderService.getOrderDetails(authentication.getName(), orderNumber);
    }
}