package com.yunzhi.retailmanagementsystem.model.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record OrderItemVO(
        @Schema(description = "商品ID")
        String goodId,

        @Schema(description = "商品名称", example = "智能手机")
        String goodName,

        @Schema(description = "单价", example = "2999.00")
        BigDecimal price,

        @Schema(description = "购买数量", example = "2")
        Integer quantity
) {}