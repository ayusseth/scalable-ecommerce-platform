package com.ayush.ecommerce.module.order.dto;

import com.ayush.ecommerce.module.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private Long orderId;
    private String orderNumber;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private List<OrderItemResponse> items;
}
