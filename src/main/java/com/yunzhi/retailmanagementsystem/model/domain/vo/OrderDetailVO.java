package com.yunzhi.retailmanagementsystem.model.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record OrderDetailVO(
        @Schema(description = "订单号")
        String orderId,

        @Schema(description = "客户姓名", example = "张三")
        String customerName,

        @Schema(description = "商品明细")
        List<OrderItemVO> items,

        @Schema(description = "订单总额")
        BigDecimal totalAmount,

        @Schema(description = "支付方式")
        String paymentMethod,

        @Schema(description = "配送方式")
        String deliveryMethod,

        @Schema(description = "订单状态")
        String status,

        @Schema(description = "创建时间")
        Date orderDate
) {}