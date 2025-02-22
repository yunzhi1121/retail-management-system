package com.yunzhi.retailmanagementsystem.common.constant.security;

import lombok.Data;


public enum Role {
    ADMIN("超级管理员"),
    SALES_ADMIN("销售管理员"),
    INVENTORY_ADMIN("仓库管理员"),
    GENERAL_ADMIN("普通管理员"),
    TRACK_ADMIN("物流管理员"),
    CUSTOMER_ADMIN("客户管理员");


    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}