package com.yunzhi.retailmanagementsystem.model.domain.enums;

import lombok.Getter;

public enum OrderStatus {
    PENDING("待处理"),
    SHIPPED("已发货"),
    CANCELLED("已取消");

    @Getter
    private final String code;

    OrderStatus(String code) {
        this.code = code;
    }
}
