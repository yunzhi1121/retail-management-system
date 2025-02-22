package com.yunzhi.retailmanagementsystem.business.track.controller;

import com.yunzhi.retailmanagementsystem.business.track.model.dto.TrackingCreateDTO;
import com.yunzhi.retailmanagementsystem.common.constant.security.Permission;
import com.yunzhi.retailmanagementsystem.common.model.response.BaseResponse;
import com.yunzhi.retailmanagementsystem.common.model.response.ResultUtils;
import com.yunzhi.retailmanagementsystem.business.track.service.TrackingService;
import com.yunzhi.retailmanagementsystem.business.track.model.vo.TrackingVO;
import com.yunzhi.retailmanagementsystem.common.security.authorization.RequiresPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tracking")
public class TrackingController {

    @Autowired
    private TrackingService trackingService;

    @PostMapping("/add")
    @RequiresPermission(Permission.TRACKING_CREATE)
    public BaseResponse<String> createTracking(@RequestBody TrackingCreateDTO createDTO) {
        String trackingId = trackingService.createTracking(createDTO);
        return ResultUtils.success(trackingId,"物流信息创建成功");
    }

    @GetMapping("/order/{orderId}")
    @RequiresPermission(Permission.TRACKING_READ)
    public BaseResponse<List<TrackingVO>> getByOrderId(@PathVariable String orderId) {
        List<TrackingVO> trackingList = trackingService.getTrackingByOrderId(orderId);
        return ResultUtils.success(trackingList, "订单物流信息查询成功");
    }
}