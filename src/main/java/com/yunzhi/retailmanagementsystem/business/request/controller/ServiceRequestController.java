package com.yunzhi.retailmanagementsystem.business.request.controller;

import com.yunzhi.retailmanagementsystem.business.request.model.dto.ServiceRequestCreateDTO;
import com.yunzhi.retailmanagementsystem.business.request.model.dto.ServiceRequestProcessDTO;
import com.yunzhi.retailmanagementsystem.business.request.model.vo.ServiceRequestVO;
import com.yunzhi.retailmanagementsystem.business.request.service.ServiceRequestsService;
import com.yunzhi.retailmanagementsystem.common.constant.security.Permission;
import com.yunzhi.retailmanagementsystem.common.model.response.BaseResponse;
import com.yunzhi.retailmanagementsystem.common.model.response.ResultUtils;
import com.yunzhi.retailmanagementsystem.common.security.authorization.RequiresPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service-requests")
public class ServiceRequestController {

    @Autowired
    private ServiceRequestsService serviceRequestService;

    @PostMapping("/add")
    @RequiresPermission(Permission.SERVICE_REQUESTS_CREATE)
    public BaseResponse<String> createRequest(@RequestBody ServiceRequestCreateDTO createDTO) {
        String requestId = serviceRequestService.createRequest(createDTO);
        return ResultUtils.success(requestId, "请求创建成功");
    }

    @GetMapping("/{requestId}")
    @RequiresPermission(Permission.SERVICE_REQUESTS_READ)
    public BaseResponse<ServiceRequestVO> getRequestDetails(@PathVariable String requestId) {
        ServiceRequestVO vo = serviceRequestService.getRequestDetails(requestId);
        return ResultUtils.success(vo, "请求详情查询成功");
    }

    @PostMapping("/{requestId}/process")
    @RequiresPermission(Permission.SERVICE_REQUESTS_UPDATE)
    public BaseResponse<Void> processRequest(
            @PathVariable String requestId,
            @RequestBody ServiceRequestProcessDTO processDTO
    ) {
        serviceRequestService.processRequest(requestId, processDTO);
        return ResultUtils.success("请求处理成功（请求状态已更新）");
    }
}
