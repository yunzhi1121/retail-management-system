package com.yunzhi.retailmanagementsystem.business.good.model.dto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class GoodsUpdateDTO {
        @Size(min = 1, max = 100, message = "商品名称长度必须在 1-100 之间")
        private String name;

        private String description;

        @Positive(message = "商品数量必须大于0")
        private Integer quantity;

        @Positive(message = "商品价格必须大于0")
        private BigDecimal price;
}