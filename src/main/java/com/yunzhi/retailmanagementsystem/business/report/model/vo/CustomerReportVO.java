// CustomerReportVO.java
package com.yunzhi.retailmanagementsystem.business.report.model.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CustomerReportVO {
    private String customerId;
    private String customerName;
    private String email;
    private Integer purchaseCount;     // 购买次数
    private BigDecimal totalSpending;  // 总消费金额
    private LocalDateTime lastPurchaseDate; // 最后购买日期

    public CustomerReportVO(String customerId, String customerName, String email, 
                           Integer purchaseCount, BigDecimal totalSpending, 
                           LocalDateTime lastPurchaseDate) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.email = email;
        this.purchaseCount = purchaseCount;
        this.totalSpending = totalSpending;
        this.lastPurchaseDate = lastPurchaseDate;
    }
}