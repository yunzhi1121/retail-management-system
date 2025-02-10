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
     *
     * @param name  客户姓名
     * @param email 客户邮箱，要求唯一
     * @param phone 客户电话号码
     * @return 新创建客户的ID
     */
    String createCustomerAndReturnId(String name, String email, String phone);

    /**
     * 修改客户信息（不可修改 customerID）
     *
     * @param customerID 客户ID，用于标识客户，不可修改
     * @param name       客户姓名
     * @param email      客户邮箱
     * @param phone      客户电话号码
     * @return 修改成功返回 true，否则返回 false
     */
    boolean updateCustomerInfo(String customerID, String name, String email, String phone);

    /**
     * 通过 customerID、email 或 phone 查询单个客户
     *
     * @param customerID 客户ID
     * @param email      客户邮箱
     * @return 返回查询到的客户对象，如果未找到则返回 null
     */
    Customers getCustomerByIdOrEmail(String customerID, String email);


}
