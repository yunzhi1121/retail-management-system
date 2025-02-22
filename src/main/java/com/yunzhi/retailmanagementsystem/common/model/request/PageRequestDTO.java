package com.yunzhi.retailmanagementsystem.common.model.request;

import lombok.Data;
import javax.validation.constraints.Min;

@Data
public class PageRequestDTO {
    @Min(value = 1, message = "页码不能小于1")
    private int currentPage = 1; // 默认第一页

    @Min(value = 1, message = "每页数量不能小于1")
    private int pageSize = 10;   // 默认每页10条
}