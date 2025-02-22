package com.yunzhi.retailmanagementsystem.business.order.service;

import com.yunzhi.retailmanagementsystem.business.good.model.po.Goods;
import com.yunzhi.retailmanagementsystem.business.order.model.dto.OrderCreateDTO;
import com.yunzhi.retailmanagementsystem.business.order.model.dto.OrderItemDTO;
import com.yunzhi.retailmanagementsystem.business.order.model.dto.OrderUpdateDTO;
import com.yunzhi.retailmanagementsystem.business.order.model.po.OrderGood;
import com.yunzhi.retailmanagementsystem.business.order.model.po.Orders;
import com.yunzhi.retailmanagementsystem.business.order.model.vo.OrderDetailVO;
import com.yunzhi.retailmanagementsystem.business.order.model.vo.OrderVO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Chloe
 * @description 针对表【orders】的数据库操作Service
 * @createDate 2025-01-12 17:32:21
 */
public interface OrdersService {

    OrderVO createOrder(OrderCreateDTO dto) throws Exception;
    void updateOrder(String orderId, OrderUpdateDTO dto) throws Exception;
    void cancelOrder(String orderId);
    OrderDetailVO getOrderDetail(String orderId);
    List<OrderVO> getCustomerOrders(String customerId);
    Map<String, Integer> convertItemsToMap(List<OrderItemDTO> items);
}