package com.yunzhi.retailmanagementsystem.business.report.model.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SalesReportVO {
    private String goodId;
    private String goodName;
    private Integer quantitySold;
    private BigDecimal totalSales;

    public SalesReportVO(String goodId, String goodName, Integer quantitySold, BigDecimal totalSales) {
        this.goodId = goodId;
        this.goodName = goodName;
        this.quantitySold = quantitySold;
        this.totalSales = totalSales;
    }
}