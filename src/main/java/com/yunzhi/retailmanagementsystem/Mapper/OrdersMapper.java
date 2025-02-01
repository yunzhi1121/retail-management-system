package com.yunzhi.retailmanagementsystem.Mapper;

import com.yunzhi.retailmanagementsystem.model.domain.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
/**
* @author Chloe
* @description 针对表【orders】的数据库操作Mapper
* @createDate 2025-01-12 17:32:21
* @Entity generator.domain.Orders
*/
public interface OrdersMapper extends BaseMapper<Orders> {
    // 根据客户ID查询订单列表
    List<Orders> selectByCustomerId(String customerId);

    // 根据订单状态查询订单列表
    List<Orders> selectByStatus(String status);

    // 根据订单ID查询订单
    Orders selectByOrderId(String orderId);
}




