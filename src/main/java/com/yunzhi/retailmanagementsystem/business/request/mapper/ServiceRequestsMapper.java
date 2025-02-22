package com.yunzhi.retailmanagementsystem.business.request.mapper;

import com.yunzhi.retailmanagementsystem.business.request.model.po.ServiceRequests;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
/**
* @author Chloe
* @description 针对表【servicerequests】的数据库操作Mapper
* @createDate 2025-01-12 17:32:12
* @Entity generator.domain.Servicerequests
*/
public interface ServiceRequestsMapper extends BaseMapper<ServiceRequests> {
    // 根据客户ID查询服务请求列表
    List<ServiceRequests> selectByCustomerId(String customerId);

    // 根据服务请求状态查询服务请求列表
    List<ServiceRequests> selectByStatus(String status);

    // 根据服务请求ID查询服务请求
    ServiceRequests selectByRequestId(String requestId);
}




