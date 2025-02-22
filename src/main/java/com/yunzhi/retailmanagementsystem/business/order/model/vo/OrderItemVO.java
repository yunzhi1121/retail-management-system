package com.yunzhi.retailmanagementsystem.business.order.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "订单项视图对象")
public class OrderItemVO {
        @Schema(description = "商品ID")
        private String goodId;

        @Schema(description = "商品名称", example = "智能手机")
        private String goodName;

        @Schema(description = "单价", example = "2999.00")
        private BigDecimal price;

        @Schema(description = "购买数量", example = "2")
        private Integer quantity;
}