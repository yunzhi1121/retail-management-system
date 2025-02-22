// CustomerReportRequestDTO.java
package com.yunzhi.retailmanagementsystem.business.report.model.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

@Data
public class CustomerReportRequestDTO {
    @NotNull(message = "开始日期不能为空")
    private LocalDate startDate;
    
    @NotNull(message = "结束日期不能为空")
    private LocalDate endDate;

}