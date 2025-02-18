package com.yunzhi.retailmanagementsystem.model.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public record GoodsCreateDTO(
        @NotBlank(message = "商品名称不能为空")
        String name,

        String description,

        @Positive(message = "商品数量必须大于0")
        Integer quantity,

        @Positive(message = "商品价格必须大于0")
        BigDecimal price
) {}