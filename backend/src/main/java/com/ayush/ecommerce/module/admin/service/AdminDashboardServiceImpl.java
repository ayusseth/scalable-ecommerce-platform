package com.ayush.ecommerce.module.admin.service;

import com.ayush.ecommerce.module.admin.dto.DashboardStatsResponse;
import com.ayush.ecommerce.module.order.repository.OrderRepository;
import com.ayush.ecommerce.module.product.repository.ProductRepository;
import com.ayush.ecommerce.module.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl
        implements AdminDashboardService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    public DashboardStatsResponse getDashboardStats() {

        BigDecimal revenue =
                orderRepository.getTotalRevenue();

        if(revenue == null){
            revenue = BigDecimal.ZERO;
        }

        return DashboardStatsResponse.builder()
                .totalUsers(
                        userRepository.count()
                )
                .totalProducts(
                        productRepository.countByActiveTrue()
                )
                .totalOrders(
                        orderRepository.count()
                )
                .totalRevenue(
                        revenue
                )
                .build();
    }
}