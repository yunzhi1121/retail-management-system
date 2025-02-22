// InventoryReportVO.java
package com.yunzhi.retailmanagementsystem.business.report.model.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class InventoryReportVO {
    private String goodId;
    private String goodName;
    private Integer currentStock;    // 当前库存
    private Integer minStockLevel;   // 最低库存阈值
    private String status;          // 库存状态：NORMAL/WARNING

    public InventoryReportVO(String goodId, String goodName, 
                            Integer currentStock, Integer minStockLevel) {
        this.goodId = goodId;
        this.goodName = goodName;
        this.currentStock = currentStock;
        this.minStockLevel = minStockLevel;
        this.status = currentStock < minStockLevel ? "WARNING" : "NORMAL";
    }
}