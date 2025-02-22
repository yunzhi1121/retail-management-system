package com.yunzhi.retailmanagementsystem.business.customer.service;

import com.yunzhi.retailmanagementsystem.business.customer.model.dto.CustomerCreateDTO;
import com.yunzhi.retailmanagementsystem.business.customer.model.dto.CustomerUpdateDTO;
import com.yunzhi.retailmanagementsystem.business.customer.model.po.Customers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunzhi.retailmanagementsystem.business.customer.model.vo.CustomerVO;
import com.yunzhi.retailmanagementsystem.common.exception.BusinessException;

/**
* @author Chloe
* @description 针对表【customers】的数据库操作Service
* @createDate 2025-01-12 17:32:33
*/
public interface CustomersService extends IService<Customers> {
    CustomerVO createCustomer(CustomerCreateDTO customerCreateDTO) throws BusinessException;
    CustomerVO updateCustomer(String customerID, CustomerUpdateDTO customerUpdateDTO);
    Customers getCustomerByIdOrEmail(String customerID, String email);
}