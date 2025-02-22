package com.yunzhi.retailmanagementsystem.business.track.model.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TrackingVO {
    private String trackingId;
    private String orderId;
    private String location;
    private LocalDateTime timestamp;
}