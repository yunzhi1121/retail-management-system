package com.yunzhi.retailmanagementsystem.common.constant.business;

import lombok.Getter;

public enum ServiceRequestStatus {
    PENDING("待处理"),
    PROCESSING("处理中"),
    RESOLVED("已解决"),
    CLOSED("已关闭");

    @Getter
    private final String description;

    ServiceRequestStatus(String description) {
        this.description = description;
    }

}
