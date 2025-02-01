package com.yunzhi.retailmanagementsystem.Mapper;

import com.yunzhi.retailmanagementsystem.model.domain.OrderGood;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
/**
* @author Chloe
* @description 针对表【order_good】的数据库操作Mapper
* @createDate 2025-01-12 17:32:25
* @Entity generator.domain.OrderGood
*/
public interface OrderGoodMapper extends BaseMapper<OrderGood> {
    // 根据订单ID查询订单商品列表
    List<OrderGood> selectByOrderId(String orderId);

    // 根据商品ID查询包含该商品的订单商品列表
    List<OrderGood> selectByGoodId(String goodId);
}




