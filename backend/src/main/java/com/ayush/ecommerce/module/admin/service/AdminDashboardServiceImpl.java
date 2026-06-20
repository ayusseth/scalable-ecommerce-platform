package com.ayush.ecommerce.module.admin.service;

import com.ayush.ecommerce.module.admin.dto.DashboardStatsResponse;
import com.ayush.ecommerce.module.order.entity.OrderStatus;
import com.ayush.ecommerce.module.order.repository.OrderRepository;
import com.ayush.ecommerce.module.product.repository.ProductRepository;
import com.ayush.ecommerce.module.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(
            value = "dashboard",
            key = "'stats'"
    )
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

                .pendingOrders(
                        orderRepository.countByStatus(
                                OrderStatus.PENDING
                        )
                )

                .paidOrders(
                        orderRepository.countByStatus(
                                OrderStatus.PAID
                        )
                )

                .processingOrders(
                        orderRepository.countByStatus(
                                OrderStatus.PROCESSING
                        )
                )

                .shippedOrders(
                        orderRepository.countByStatus(
                                OrderStatus.SHIPPED
                        )
                )

                .deliveredOrders(
                        orderRepository.countByStatus(
                                OrderStatus.DELIVERED
                        )
                )

                .cancelledOrders(
                        orderRepository.countByStatus(
                                OrderStatus.CANCELLED
                        )
                )

                .lowStockProducts(
                        productRepository
                                .countByStockQuantityLessThanAndActiveTrue(
                                        10
                                )
                )

                .build();
    }
}