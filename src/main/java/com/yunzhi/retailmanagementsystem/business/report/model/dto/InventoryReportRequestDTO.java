// InventoryReportRequestDTO.java
package com.yunzhi.retailmanagementsystem.business.report.model.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class InventoryReportRequestDTO {
    private Integer threshold = 50; // 库存预警阈值，默认50
}