package com.ayush.ecommerce.module.admin.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class DashboardStatsResponse implements java.io.Serializable{

    private Long totalUsers;

    private Long totalProducts;

    private Long totalOrders;

    private BigDecimal totalRevenue;

    private Long pendingOrders;

    private Long paidOrders;

    private Long processingOrders;

    private Long shippedOrders;

    private Long deliveredOrders;

    private Long cancelledOrders;

    private Long lowStockProducts;
}