package com.yunzhi.retailmanagementsystem.service;

import com.yunzhi.retailmanagementsystem.model.domain.po.Goods;
import com.yunzhi.retailmanagementsystem.model.domain.po.OrderGood;
import com.yunzhi.retailmanagementsystem.model.domain.po.Orders;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Chloe
 * @description 针对表【orders】的数据库操作Service
 * @createDate 2025-01-12 17:32:21
 */
public interface OrdersService {
    /**
     * 创建订单（参数使用基础类型/领域模型）
     * @param customerId 客户ID
     * @param itemQuantities 商品ID与数量的映射
     * @param paymentMethod 支付方式
     * @param deliveryMethod 配送方式
     * @param remarks 备注
     * @return 订单实体对象
     */
    Orders createOrder(
            String customerId,
            Map<String, Integer> itemQuantities,
            String paymentMethod,
            String deliveryMethod,
            String remarks
    );

    /**
     * 更新订单
     * @param orderId 订单ID
     * @param newItemQuantities 新的商品数量映射
     * @param newPaymentMethod 新支付方式
     * @param newDeliveryMethod 新配送方式
     * @param newRemarks 新备注
     */
    void updateOrder(
            String orderId,
            Map<String, Integer> newItemQuantities,
            String newPaymentMethod,
            String newDeliveryMethod,
            String newRemarks
    );

    /**
     * 取消订单
     * @param orderId 订单ID
     */
    void cancelOrder(String orderId);

    /**
     * 获取订单详情
     * @param orderId 订单ID
     * @return 订单实体对象
     */
    Orders getOrderDetail(String orderId);

    /**
     * 获取客户订单列表
     * @param customerId 客户ID
     * @return 订单实体列表
     */
    List<Orders> getCustomerOrders(String customerId);
    List<OrderGood> getOrderGoodsByOrderId(String orderID);
    /**
     * 批量获取商品信息
     * @param goodIds 商品ID集合
     * @return 商品列表
     */
    List<Goods> batchGetGoodsByIds(Collection<String> goodIds);
}