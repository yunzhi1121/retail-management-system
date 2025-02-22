package com.yunzhi.retailmanagementsystem.business.track.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunzhi.retailmanagementsystem.common.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.business.order.mapper.OrdersMapper;
import com.yunzhi.retailmanagementsystem.business.track.model.dto.TrackingCreateDTO;
import com.yunzhi.retailmanagementsystem.business.order.model.po.Orders;
import com.yunzhi.retailmanagementsystem.business.track.model.po.Tracking;
import com.yunzhi.retailmanagementsystem.business.track.model.vo.TrackingVO;
import com.yunzhi.retailmanagementsystem.common.model.response.ErrorCode;
import com.yunzhi.retailmanagementsystem.business.order.service.OrdersService;
import com.yunzhi.retailmanagementsystem.business.track.service.TrackingService;
import com.yunzhi.retailmanagementsystem.business.track.mapper.TrackingMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
* @author Chloe
* @description 针对表【tracking】的数据库操作Service实现
* @createDate 2025-01-12 17:32:06
*/
@Service
public class TrackingServiceImpl extends ServiceImpl<TrackingMapper, Tracking>
    implements TrackingService{
    @Autowired
    private OrdersService orderService;
    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    public String createTracking(TrackingCreateDTO createDTO) {
        // 校验订单是否存在
        Orders order = ordersMapper.selectById(createDTO.getOrderId());
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        if (order.getStatus().equals("已取消")) {
            throw new BusinessException(ErrorCode.ORDER_STATUS_CONFLICT, "订单已取消，无法添加物流记录");
        }
        //验证参数是否为空
        if (createDTO.getLocation() == null || createDTO.getLocation().isEmpty() ) {
            throw new BusinessException(ErrorCode.PARAM_MISSING, "物流位置不能为空");
        }
        if(createDTO.getTimestamp() == null|| createDTO.getTimestamp().equals("")){
            throw new BusinessException(ErrorCode.PARAM_MISSING, "物流时间不能为空");
        }

        // 创建物流记录
        Tracking entity = new Tracking();
        BeanUtils.copyProperties(createDTO, entity);
        entity.setTrackingId(UUID.randomUUID().toString());

        //TODO 订单状态从待发货改为已发货

        this.save(entity);
        return entity.getTrackingId();
    }

    @Override
    public List<TrackingVO> getTrackingByOrderId(String orderId) {
        QueryWrapper<Tracking> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("OrderID", orderId)
                .orderByAsc("timestamp"); // 按时间排序

        List<Tracking> trackingList = this.list(queryWrapper);
        if(trackingList.isEmpty()){
            throw new BusinessException(ErrorCode.TRACKING_NOT_FOUND);
        }
        return trackingList.stream()
                .map(entity -> {
                    TrackingVO vo = new TrackingVO();
                    BeanUtils.copyProperties(entity, vo);
                    return vo;
                })
                .collect(Collectors.toList());
    }
}




