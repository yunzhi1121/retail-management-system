package com.yunzhi.retailmanagementsystem.business.track.model.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class TrackingCreateDTO {
    @NotBlank(message = "订单ID不能为空")
    private String orderId;

    @NotBlank(message = "物流位置不能为空")
    private String location;

    @NotBlank(message = "物流位置不能为空")
    private LocalDateTime timestamp;
}