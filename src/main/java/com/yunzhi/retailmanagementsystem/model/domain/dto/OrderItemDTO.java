package com.yunzhi.retailmanagementsystem.model.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public record OrderItemDTO(
        @NotBlank(message = "商品ID不能为空")
        @Schema(description = "商品ID", example = "GOOD_001")
        String goodId,

        @Min(value = 1, message = "商品数量必须大于0")
        @Schema(description = "购买数量", example = "2")
        Integer quantity
) {}