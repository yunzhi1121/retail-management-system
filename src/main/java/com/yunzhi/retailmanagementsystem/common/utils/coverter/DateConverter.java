package com.yunzhi.retailmanagementsystem.common.utils.coverter;

import com.yunzhi.retailmanagementsystem.business.order.model.po.Orders;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateConverter {
    
    /**
     * 将 java.util.Date 转换为 java.time.LocalDate（默认时区）
     * @param date 需要转换的 Date 对象，允许为 null
     * @return 转换后的 LocalDate 对象（可能为 null）
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    /**
     * 安全获取订单日期的 LocalDate（过滤 null 值）
     * @param order 订单对象
     * @return LocalDate 或 null
     */
//    public static LocalDate getOrderLocalDate(Orders order) {
//        return order.getOrderDate() != null ?
//               toLocalDate(order.getOrderDate()) :
//               null;
//    }
    /**
     * 将 java.time.LocalDate 转换为 java.util.Date（默认时区午夜时间）
     * @param localDate 需要转换的 LocalDate 对象，允许为 null
     * @return 转换后的 Date 对象（可能为 null）
     * @apiNote 转换时会使用系统默认时区，将 LocalDate 转为当天的 00:00:00 时间点
     */
    public static Date toDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}