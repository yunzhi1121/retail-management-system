package com.yunzhi.retailmanagementsystem.business.order.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "订单视图对象")
public class OrderVO {
        @Schema(description = "订单号", example = "ORDER_2025001")
        private String orderId;

        @Schema(description = "客户ID", example = "CUST_001")
        private String customerId;

        @Schema(description = "订单总额", example = "5999.00")
        private BigDecimal totalAmount;

        @Schema(description = "订单状态", example = "PENDING")
        private String status;

        @Schema(description = "创建时间", example = "2025-02-18T10:30:00")
        private Date orderDate;
}