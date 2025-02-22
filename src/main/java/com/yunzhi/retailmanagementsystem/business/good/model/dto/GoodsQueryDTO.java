package com.yunzhi.retailmanagementsystem.business.good.model.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Data
public class GoodsQueryDTO {
    private String name;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    @Min(value = 1, message = "页码必须大于等于1")
    private int page = 1;
    @Min(value = 1, message = "每页数量必须大于等于1")
    private int pageSize = 10;
}