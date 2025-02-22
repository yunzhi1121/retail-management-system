package com.yunzhi.retailmanagementsystem.business.request.service;

import com.yunzhi.retailmanagementsystem.business.request.model.dto.ServiceRequestCreateDTO;
import com.yunzhi.retailmanagementsystem.business.request.model.dto.ServiceRequestProcessDTO;
import com.yunzhi.retailmanagementsystem.business.request.model.po.ServiceRequests;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunzhi.retailmanagementsystem.business.request.model.vo.ServiceRequestVO;

/**
* @author Chloe
* @description 针对表【servicerequests】的数据库操作Service
* @createDate 2025-01-12 17:32:12
*/
public interface ServiceRequestsService extends IService<ServiceRequests> {
    // 创建请求
    String createRequest(ServiceRequestCreateDTO createDTO);

    // 查询请求详情
    ServiceRequestVO getRequestDetails(String requestId);

    // 处理请求（更新状态）
    void processRequest(String requestId, ServiceRequestProcessDTO processDTO);

}
