package com.yunzhi.retailmanagementsystem.model.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public record OrderCreateDTO(
        @NotBlank(message = "客户ID不能为空")
        @Schema(description = "客户ID", example = "CUST_001")
        String customerId,

        @Valid
        @NotEmpty(message = "订单商品不能为空")
        @Schema(description = "商品条目列表")
        List<OrderItemDTO> items,

        @NotBlank(message = "支付方式不能为空")
        @Schema(description = "支付方式", example = "ALIPAY")
        String paymentMethod,

        @NotBlank(message = "配送方式不能为空")
        @Schema(description = "配送方式", example = "EXPRESS")
        String deliveryMethod,

        @Schema(description = "订单备注", example = "请尽快发货")
        String remarks
) {}