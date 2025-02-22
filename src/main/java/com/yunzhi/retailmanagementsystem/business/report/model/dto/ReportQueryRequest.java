package com.yunzhi.retailmanagementsystem.business.report.model.dto;

import com.yunzhi.retailmanagementsystem.common.model.request.PageRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReportQueryRequest extends PageRequestDTO {
    private String reportId;
    private String userId;
    private LocalDate startTime;
    private LocalDate endTime;
}