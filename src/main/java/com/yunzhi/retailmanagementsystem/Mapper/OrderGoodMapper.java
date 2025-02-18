package com.yunzhi.retailmanagementsystem.Mapper;

import com.yunzhi.retailmanagementsystem.model.domain.po.OrderGood;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
* @author Chloe
* @description 针对表【order_good】的数据库操作Mapper
* @createDate 2025-01-12 17:32:25
* @Entity generator.domain.OrderGood
*/
public interface OrderGoodMapper extends BaseMapper<OrderGood> {
    /**
     * 批量插入订单商品关联记录
     * @param list 订单商品列表
     * @return 插入记录数
     */
    int insertBatch(@Param("list") List<OrderGood> list);

    List<OrderGood> selectByOrderId(String orderId);
}




