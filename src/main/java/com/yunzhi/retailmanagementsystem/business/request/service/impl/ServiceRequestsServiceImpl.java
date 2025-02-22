package com.yunzhi.retailmanagementsystem.business.request.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunzhi.retailmanagementsystem.common.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.business.customer.mapper.CustomersMapper;
import com.yunzhi.retailmanagementsystem.business.request.model.dto.ServiceRequestCreateDTO;
import com.yunzhi.retailmanagementsystem.business.request.model.dto.ServiceRequestProcessDTO;
import com.yunzhi.retailmanagementsystem.business.customer.model.po.Customers;
import com.yunzhi.retailmanagementsystem.business.request.model.po.ServiceRequests;
import com.yunzhi.retailmanagementsystem.business.request.model.vo.ServiceRequestVO;
import com.yunzhi.retailmanagementsystem.common.constant.business.ServiceRequestStatus;
import com.yunzhi.retailmanagementsystem.common.model.response.ErrorCode;
import com.yunzhi.retailmanagementsystem.business.request.service.ServiceRequestsService;
import com.yunzhi.retailmanagementsystem.business.request.mapper.ServiceRequestsMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
* @author Chloe
* @description 针对表【servicerequests】的数据库操作Service实现
* @createDate 2025-01-12 17:32:12
*/
@Service
public class ServiceRequestsServiceImpl extends ServiceImpl<ServiceRequestsMapper, ServiceRequests>
    implements ServiceRequestsService {

    @Autowired
    private CustomersMapper customersMapper;

    @Override
    public String createRequest(ServiceRequestCreateDTO createDTO) {
        // 校验客户是否存在
        Customers customer = customersMapper.selectById(createDTO.getCustomerId());
        if (customer == null) {
            throw new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND);
        }

        // 创建请求实体
        ServiceRequests entity = new ServiceRequests();
        BeanUtils.copyProperties(createDTO, entity);
        entity.setRequestId(UUID.randomUUID().toString());
        entity.setStatus(ServiceRequestStatus.PENDING.getDescription()); // 默认状态：待处理

        // 保存到数据库
        this.save(entity);
        return entity.getRequestId();
    }

    @Override
    public ServiceRequestVO getRequestDetails(String requestId) {
        ServiceRequests request = this.getById(requestId);
        if (request == null) {
            throw new BusinessException(ErrorCode.SERVICE_REQUEST_NOT_FOUND);
        }

        ServiceRequestVO vo = new ServiceRequestVO(request.getRequestId(), request.getCustomerId(), request.getDescription(), request.getStatus());
        BeanUtils.copyProperties(request, vo);
        return vo;
    }

    @Override
    public void processRequest(String requestId, ServiceRequestProcessDTO processDTO) {
        ServiceRequests entity = this.getById(requestId);
        if (entity == null) {
            throw new BusinessException(ErrorCode.SERVICE_REQUEST_NOT_FOUND);
        }
        if(entity.getStatus()=="COMPLETED"){
            throw new BusinessException(ErrorCode.SERVICE_REQUEST_COMPLETED);
        }
        // 更新状态和负责人
        entity.setStatus(processDTO.getTargetStatus());
        this.updateById(entity);
    }
}




