package com.yunzhi.retailmanagementsystem.business.customer.mapper;

import com.yunzhi.retailmanagementsystem.business.customer.model.po.Customers;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author Chloe
* @description 针对表【customers】的数据库操作Mapper
* @createDate 2025-01-12 17:32:33
* @Entity generator.domain.Customers
*/
public interface CustomersMapper extends BaseMapper<Customers> {
    // 根据客户姓名模糊查询客户列表
    List<Customers> selectByNameLike(String name);

    // 根据客户ID查询客户
    Customers selectByCustomerId(String customerId);

    // 根据客户邮箱查询客户
    Customers selectByEmail(String email);

    int updateCustomerInfo(String customerID, String name, String email, String phone);
}




