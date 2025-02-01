package com.yunzhi.retailmanagementsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunzhi.retailmanagementsystem.model.domain.Orders;
import com.yunzhi.retailmanagementsystem.service.OrdersService;
import com.yunzhi.retailmanagementsystem.Mapper.OrdersMapper;
import org.springframework.stereotype.Service;

/**
* @author Chloe
* @description 针对表【orders】的数据库操作Service实现
* @createDate 2025-01-12 17:32:21
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

}




