package com.yunzhi.retailmanagementsystem.business.order.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class OrderItemDTO {

        @NotBlank(message = "商品ID不能为空")
        @Schema(description = "商品ID", example = "GOOD_001")
        private String goodId;

        @Min(value = 1, message = "商品数量必须大于0")
        @Schema(description = "购买数量", example = "2")
        private Integer quantity;
}
