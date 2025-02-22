package com.yunzhi.retailmanagementsystem.common.constant.business;

import lombok.Getter;

public enum OrderStatus {
    PENDING("待发货"),
    SHIPPED("已发货"),
    CANCELLED("已取消"),
    COMPLETED("已完成");

    @Getter
    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}
