package com.yunzhi.retailmanagementsystem.business.good.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class GoodsCreateDTO {
        @NotBlank(message = "商品名称不能为空")
        private String name;

        private String description;

        @Positive(message = "商品数量必须大于0")
        private Integer quantity;

        @Positive(message = "商品价格必须大于0")
        private BigDecimal price;
}