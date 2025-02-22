package com.yunzhi.retailmanagementsystem.business.report.model.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReportVO {
    private String reportId;
    private String userId;
    private String parameters;   // 原始参数字符串
    private LocalDateTime generatedDate;
    
    // 示例：添加解析后的参数（非数据库字段，前端展示用）
    private LocalDate startDate;
    private LocalDate endDate;
    private String reportType;
}