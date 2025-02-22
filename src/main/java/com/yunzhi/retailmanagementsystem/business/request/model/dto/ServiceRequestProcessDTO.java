package com.yunzhi.retailmanagementsystem.business.request.model.dto;

import com.yunzhi.retailmanagementsystem.common.constant.business.ServiceRequestStatus;
import lombok.Data;

/**
 * 处理请求的DTO
 */
@Data
public class ServiceRequestProcessDTO {
    private String targetStatus; // 目标状态
}