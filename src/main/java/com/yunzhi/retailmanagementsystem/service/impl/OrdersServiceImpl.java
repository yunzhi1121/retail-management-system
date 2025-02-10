package com.yunzhi.retailmanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunzhi.retailmanagementsystem.Mapper.CustomersMapper;
import com.yunzhi.retailmanagementsystem.Mapper.GoodsMapper;
import com.yunzhi.retailmanagementsystem.Mapper.OrderGoodMapper;
import com.yunzhi.retailmanagementsystem.Mapper.OrdersMapper;
import com.yunzhi.retailmanagementsystem.model.domain.Customers;
import com.yunzhi.retailmanagementsystem.model.domain.Goods;
import com.yunzhi.retailmanagementsystem.model.domain.OrderGood;
import com.yunzhi.retailmanagementsystem.model.domain.Orders;
import com.yunzhi.retailmanagementsystem.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Chloe
 * @description 针对表【orders】的数据库操作Service实现
 * @createDate 2025-01-12 17:32:21
 */

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private OrderGoodMapper orderGoodMapper;
    @Autowired
    private CustomersMapper customersMapper;

    @Override
    @Transactional
    public Orders createOrder(String customerID, Map<String, Integer> goodQuantities, String paymentMethod, String deliveryMethod, String remarks) {
        // 0. 验证用户 ID 是否存在
        Customers customer = customersMapper.selectOne(Wrappers.<Customers>lambdaQuery().eq(Customers::getCustomerID, customerID));
        if (customer == null) {
            throw new RuntimeException("用户 ID 不存在");
        }
        List<Goods> goods = new ArrayList<>();
        // 1. 检查商品库存是否足够
        for (Map.Entry<String, Integer> entry : goodQuantities.entrySet()) {
            String goodId = entry.getKey();
            Integer quantity = entry.getValue();
            Goods dbGood = goodsMapper.selectById(goodId);
            if (dbGood == null) {
                throw new RuntimeException("商品 ID 不存在");
            }
            if (dbGood.getQuantity() < quantity) {
                throw new RuntimeException("商品库存不足");
            }
            Goods good = new Goods();
            good.setGoodID(goodId);
            good.setQuantity(quantity);
            good.setPrice(dbGood.getPrice());
            goods.add(good);
        }
        // 2. 计算订单金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Goods good : goods) {
            totalAmount = totalAmount.add(good.getPrice().multiply(BigDecimal.valueOf(good.getQuantity())));
        }
        // 3. 保存订单，并生成订单 ID
        String orderID = UUID.randomUUID().toString();
        Orders order = new Orders();
        order.setOrderID(orderID);
        order.setCustomerID(customerID);
        order.setOrderDate(new Date());
        order.setStatus("待发货");
        order.setTotalAmount(totalAmount);
        order.setPaymentMethod(paymentMethod);
        order.setDeliveryMethod(deliveryMethod);
        order.setRemarks(remarks);
        ordersMapper.insert(order);
        // 4. 插入订单商品关联记录
        for (Goods good : goods) {
            OrderGood orderGood = new OrderGood();
            orderGood.setOrderID(orderID);
            orderGood.setGoodID(good.getGoodID());
            orderGood.setQuantity(good.getQuantity());
            orderGoodMapper.insert(orderGood);
            // 5. 更新商品库存
            Goods dbGood = goodsMapper.selectById(good.getGoodID());
            dbGood.setQuantity(dbGood.getQuantity() - good.getQuantity());
            goodsMapper.updateById(dbGood);
        }
        return order;
    }
    @Override
    @Transactional
    public boolean updateOrder(String orderID, Map<String, Integer> newGoodQuantities, String newPaymentMethod, String newDeliveryMethod, String newRemarks) {
        // 1. 查询订单状态，确保订单未发货
        Orders order = ordersMapper.selectById(orderID);
        if (!"待发货".equals(order.getStatus())) {
            throw new RuntimeException("订单已发货，无法修改订单");
        }
        // 2. 释放原订单商品库存
        QueryWrapper<OrderGood> wrapper = new QueryWrapper<>();
        wrapper.eq("orderID", orderID);
        List<OrderGood> oldOrderGoods = orderGoodMapper.selectList(wrapper);
        for (OrderGood oldOrderGood : oldOrderGoods) {
            Goods good = goodsMapper.selectById(oldOrderGood.getGoodID());
            good.setQuantity(good.getQuantity() + oldOrderGood.getQuantity());
            goodsMapper.updateById(good);
        }
        // 删除原订单商品关联记录
        orderGoodMapper.delete(wrapper);
        List<Goods> newGoods = new ArrayList<>();
        // 3. 检查新商品库存是否足够
        for (Map.Entry<String, Integer> entry : newGoodQuantities.entrySet()) {
            String goodId = entry.getKey();
            Integer quantity = entry.getValue();
            Goods dbGood = goodsMapper.selectById(goodId);
            if (dbGood == null) {
                throw new RuntimeException("商品 ID 不存在");
            }
            if (dbGood.getQuantity() < quantity) {
                throw new RuntimeException("商品库存不足");
            }
            Goods good = new Goods();
            good.setGoodID(goodId);
            good.setQuantity(quantity);
            good.setPrice(dbGood.getPrice());
            newGoods.add(good);
        }
        // 4. 计算新订单金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Goods good : newGoods) {
            totalAmount = totalAmount.add(good.getPrice().multiply(BigDecimal.valueOf(good.getQuantity())));
        }
        // 5. 更新订单信息
        order.setTotalAmount(totalAmount);
        order.setPaymentMethod(newPaymentMethod);
        order.setDeliveryMethod(newDeliveryMethod);
        order.setRemarks(newRemarks);
        ordersMapper.updateById(order);
        // 6. 插入新的订单商品关联记录
        for (Goods good : newGoods) {
            OrderGood orderGood = new OrderGood();
            orderGood.setOrderID(orderID);
            orderGood.setGoodID(good.getGoodID());
            orderGood.setQuantity(good.getQuantity());
            orderGoodMapper.insert(orderGood);
            // 7. 更新商品库存
            Goods dbGood = goodsMapper.selectById(good.getGoodID());
            dbGood.setQuantity(dbGood.getQuantity() - good.getQuantity());
            goodsMapper.updateById(dbGood);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean cancelOrder(String orderID) {
        // 1. 查询订单状态，确保订单未发货
        Orders order = ordersMapper.selectById(orderID);
        if (!"待发货".equals(order.getStatus())) {
            throw new RuntimeException("订单已发货，无法取消订单");
        }

        // 2. 释放订单商品库存
        QueryWrapper<OrderGood> wrapper = new QueryWrapper<>();
        wrapper.eq("orderID", orderID);
        List<OrderGood> orderGoods = orderGoodMapper.selectList(wrapper);
        for (OrderGood orderGood : orderGoods) {
            Goods good = goodsMapper.selectById(orderGood.getGoodID());
            good.setQuantity(good.getQuantity() + orderGood.getQuantity());
            goodsMapper.updateById(good);
        }

        // 3. 更新订单状态为已取消
        order.setStatus("已取消");
        ordersMapper.updateById(order);

        return true;
    }

    @Override
    public String getOrderStatus(String orderID) {
        Orders order = ordersMapper.selectById(orderID);
        if (order != null) {
            return order.getStatus();
        }
        return null;
    }

    @Override
    public List<Orders> getOrdersByCustomer(String customerID) {
        Customers cutomer = customersMapper.selectById(customerID);
        if (cutomer == null) {
            throw new RuntimeException("客户不存在");
        }
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.eq("customerID", customerID);
        return ordersMapper.selectList(wrapper);
    }

    @Override
    public Orders getOrderById(String orderID) {
        return ordersMapper.selectById(orderID);
    }

    @Override
    public List<OrderGood> getOrderGoodsByOrderId(String orderID) {
        QueryWrapper<OrderGood> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("orderID", orderID);
        return orderGoodMapper.selectList(queryWrapper);
    }
}