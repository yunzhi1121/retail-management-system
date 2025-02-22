package com.yunzhi.retailmanagementsystem.business.request.model.vo;

import com.yunzhi.retailmanagementsystem.common.constant.business.ServiceRequestStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 服务请求的视图对象
 */
@Data
public class ServiceRequestVO {
    private String requestId;
    private String customerId;
    private String description;
    private String status;

    public ServiceRequestVO(String requestId, String customerId, String description, String serviceRequestStatus) {
        this.requestId = requestId;
        this.customerId = customerId;
        this.description = description;
        this.status = serviceRequestStatus;
    }
//    private LocalDateTime createTime;
//    private String assignee; // 负责人

}