package com.yunzhi.retailmanagementsystem.service;

import com.yunzhi.retailmanagementsystem.model.domain.po.Customers;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Chloe
* @description 针对表【customers】的数据库操作Service
* @createDate 2025-01-12 17:32:33
*/
public interface CustomersService extends IService<Customers> {
    String createCustomer(String name, String email, String phone);
    void updateCustomer(String customerID, String name, String email, String phone);
    Customers getCustomerByIdOrEmail(String customerID, String email);
}