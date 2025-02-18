package com.yunzhi.retailmanagementsystem.Mapper;

import com.yunzhi.retailmanagementsystem.model.domain.po.Tracking;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
/**
* @author Chloe
* @description 针对表【tracking】的数据库操作Mapper
* @createDate 2025-01-12 17:32:06
* @Entity generator.domain.Tracking
*/
public interface TrackingMapper extends BaseMapper<Tracking> {
    // 根据订单ID查询物流追踪信息
    List<Tracking> selectByOrderId(String orderId);

    // 根据物流追踪ID查询物流追踪信息
    Tracking selectByTrackingId(String trackingId);
}




