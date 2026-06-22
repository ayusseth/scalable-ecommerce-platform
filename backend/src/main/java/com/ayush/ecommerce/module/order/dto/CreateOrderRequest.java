package com.ayush.ecommerce.module.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    @Valid
    @NotEmpty
    private List<CreateOrderItemRequest> items;

    @NotNull
    private Long addressId;
}
