package com.yunzhi.retailmanagementsystem.business.order.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class OrderDetailVO {

    @Schema(description = "订单号")
    private String orderId;

    @Schema(description = "客户姓名", example = "张三")
    private String customerName;

    @Schema(description = "商品明细")
    private List<OrderItemVO> items;

    @Schema(description = "订单总额")
    private BigDecimal totalAmount;

    @Schema(description = "支付方式")
    private String paymentMethod;

    @Schema(description = "配送方式")
    private String deliveryMethod;

    @Schema(description = "订单状态")
    private String status;

    @Schema(description = "创建时间")
    private LocalDateTime orderDate;

    public OrderDetailVO(String orderId, String name, List<OrderItemVO> items, BigDecimal totalAmount, String paymentMethod, String deliveryMethod, String status, LocalDateTime orderDate) {
        this.orderId = orderId;
        this.customerName = name;
        this.items = items;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.deliveryMethod = deliveryMethod;
        this.status = status;
        this.orderDate = orderDate;
    }
}
