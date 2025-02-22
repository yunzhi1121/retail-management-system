package com.yunzhi.retailmanagementsystem.business.track.service;

import com.yunzhi.retailmanagementsystem.business.track.model.dto.TrackingCreateDTO;
import com.yunzhi.retailmanagementsystem.business.track.model.po.Tracking;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunzhi.retailmanagementsystem.business.track.model.vo.TrackingVO;

import java.util.List;

/**
* @author Chloe
* @description 针对表【tracking】的数据库操作Service
* @createDate 2025-01-12 17:32:06
*/
public interface TrackingService extends IService<Tracking> {
    // 创建物流记录
    String createTracking(TrackingCreateDTO createDTO);

    // 查询订单的所有物流记录
    List<TrackingVO> getTrackingByOrderId(String orderId);
}
