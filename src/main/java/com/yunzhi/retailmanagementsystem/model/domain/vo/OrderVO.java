package com.yunzhi.retailmanagementsystem.model.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Date;

public record OrderVO(
        @Schema(description = "订单号", example = "ORDER_2025001")
        String orderId,

        @Schema(description = "客户ID", example = "CUST_001")
        String customerId,

        @Schema(description = "订单总额", example = "5999.00")
        BigDecimal totalAmount,

        @Schema(description = "订单状态", example = "PENDING")
        String status,

        @Schema(description = "创建时间", example = "2025-02-18T10:30:00")
        Date orderDate
) {}