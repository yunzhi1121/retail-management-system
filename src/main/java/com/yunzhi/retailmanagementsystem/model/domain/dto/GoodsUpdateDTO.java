package com.yunzhi.retailmanagementsystem.model.domain.dto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public record GoodsUpdateDTO(
        @Size(min = 1, max = 100, message = "商品名称长度必须在 1-100 之间")
        String name,

        String description,

        @Positive(message = "商品数量必须大于0")
        Integer quantity,

        @Positive(message = "商品价格必须大于0")
        BigDecimal price
) {}