package com.yunzhi.retailmanagementsystem.service;

import com.yunzhi.retailmanagementsystem.model.domain.Goods;
import com.yunzhi.retailmanagementsystem.model.domain.OrderGood;
import com.yunzhi.retailmanagementsystem.model.domain.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author Chloe
* @description 针对表【orders】的数据库操作Service
* @createDate 2025-01-12 17:32:21
*/
public interface OrdersService extends IService<Orders> {



    /**
     * 创建一个新的订单
     *
     * @param customerID 客户标识，用于关联订单到特定客户
     * @param goodQuantities 商品及其数量的映射，键为商品标识，值为商品数量
     * @param paymentMethod 支付方式，描述订单的支付手段
     * @param deliveryMethod 配送方式，描述订单的配送方法
     * @param remarks 备注信息，用于记录订单的额外信息或特殊要求
     * @return 返回新创建的订单对象
     */
    Orders createOrder(String customerID, Map<String, Integer> goodQuantities, String paymentMethod, String deliveryMethod, String remarks) ;

    /**
     * 更新现有订单的信息
     *
     * @param orderID 订单标识，用于指定需要更新的订单
     * @param newGoodQuantities 新的商品及其数量的映射，用于更新订单中的商品信息
     * @param newPaymentMethod 新的支付方式，用于更新订单的支付手段
     * @param newDeliveryMethod 新的配送方式，用于更新订单的配送方法
     * @param newRemarks 新的备注信息，用于更新订单的额外信息或特殊要求
     * @return 返回一个布尔值，表示订单更新是否成功
     */
    boolean updateOrder(String orderID, Map<String, Integer> newGoodQuantities, String newPaymentMethod, String newDeliveryMethod, String newRemarks);

    /**
     * 取消指定的订单
     *
     * @param orderID 订单ID，用于识别和定位订单
     * @return 取消成功返回true，否则返回false
     */
    boolean cancelOrder(String orderID);

    /**
     * 获取指定订单的状态
     *
     * @param orderID 订单ID，用于识别和定位订单
     * @return 返回订单的状态信息
     */
    String getOrderStatus(String orderID);

    /**
     * 根据客户ID获取该客户的所有订单
     *
     * @param customerID 客户ID，用于筛选订单
     * @return 返回属于该客户的所有订单列表
     */
    List<Orders> getOrdersByCustomer(String customerID);

    /**
     * 根据订单ID获取订单信息
     *
     * @param orderID 订单的唯一标识符
     * @return 返回订单对象，如果找不到则返回null
     */
    Orders getOrderById(String orderID);

    /**
     * 根据订单ID获取订单商品列表
     *
     * @param orderID 订单的唯一标识符
     * @return 对应订单ID的订单商品列表，如果找不到则返回空列表
     */
    List<OrderGood> getOrderGoodsByOrderId(String orderID);
}
