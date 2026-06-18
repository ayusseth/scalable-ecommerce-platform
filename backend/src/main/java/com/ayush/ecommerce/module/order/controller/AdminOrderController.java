package com.ayush.ecommerce.module.order.controller;


import com.ayush.ecommerce.module.order.dto.OrderResponse;
import com.ayush.ecommerce.module.order.dto.UpdateOrderStatusRequest;
import com.ayush.ecommerce.module.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {
    private final OrderService orderService;

    @PutMapping("/{orderNumber}/status")
    public OrderResponse updateOrderStatus(@PathVariable String orderNumber,
                                           @Valid @RequestBody
                                           UpdateOrderStatusRequest request){
        return orderService.updateOrderStatus(orderNumber,request);
    }
}
