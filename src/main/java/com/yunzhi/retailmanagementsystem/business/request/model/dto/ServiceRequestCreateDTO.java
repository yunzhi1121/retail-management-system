package com.yunzhi.retailmanagementsystem.business.request.model.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 创建服务请求的DTO
 */
@Data
public class ServiceRequestCreateDTO {
    @NotBlank(message = "客户ID不能为空")
    private String customerId;

    @NotBlank(message = "请求描述不能为空")
    private String description;
}
