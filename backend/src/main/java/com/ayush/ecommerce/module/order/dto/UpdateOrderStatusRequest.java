package com.ayush.ecommerce.module.order.dto;

import com.ayush.ecommerce.module.order.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {

    @NotNull
    private OrderStatus status;
}
