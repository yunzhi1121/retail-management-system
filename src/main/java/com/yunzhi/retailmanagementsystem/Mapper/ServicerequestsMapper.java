package com.yunzhi.retailmanagementsystem.Mapper;

import com.yunzhi.retailmanagementsystem.model.domain.po.Servicerequests;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
/**
* @author Chloe
* @description 针对表【servicerequests】的数据库操作Mapper
* @createDate 2025-01-12 17:32:12
* @Entity generator.domain.Servicerequests
*/
public interface ServicerequestsMapper extends BaseMapper<Servicerequests> {
    // 根据客户ID查询服务请求列表
    List<Servicerequests> selectByCustomerId(String customerId);

    // 根据服务请求状态查询服务请求列表
    List<Servicerequests> selectByStatus(String status);

    // 根据服务请求ID查询服务请求
    Servicerequests selectByRequestId(String requestId);
}




