package com.yunzhi.retailmanagementsystem.service;

import com.yunzhi.retailmanagementsystem.model.domain.Customers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunzhi.retailmanagementsystem.model.domain.Orders;
import com.yunzhi.retailmanagementsystem.model.domain.Servicerequests;

import java.util.List;

/**
* @author Chloe
* @description 针对表【customers】的数据库操作Service
* @createDate 2025-01-12 17:32:33
*/
public interface CustomersService extends IService<Customers> {
    /**
     * 创建新客户，确保 email 唯一
     */
    String createCustomerAndReturnId(String name, String email, String phone);

    /**
     * 修改客户信息（不可修改 customerID）
     */
    boolean updateCustomerInfo(String customerID, String name, String email, String phone);

    /**
     * 通过 customerID、email 或 phone 查询单个客户
     */
    Customers getCustomerByIdOrEmail(String customerID, String email);


}
