package com.yunzhi.retailmanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunzhi.retailmanagementsystem.Mapper.CustomersMapper;
import com.yunzhi.retailmanagementsystem.Mapper.GoodsMapper;
import com.yunzhi.retailmanagementsystem.Mapper.OrderGoodMapper;
import com.yunzhi.retailmanagementsystem.Mapper.OrdersMapper;
import com.yunzhi.retailmanagementsystem.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.model.domain.po.Customers;
import com.yunzhi.retailmanagementsystem.model.domain.po.Goods;
import com.yunzhi.retailmanagementsystem.model.domain.po.OrderGood;
import com.yunzhi.retailmanagementsystem.model.domain.po.Orders;
import com.yunzhi.retailmanagementsystem.model.domain.enums.OrderStatus;
import com.yunzhi.retailmanagementsystem.response.ErrorCode;
import com.yunzhi.retailmanagementsystem.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Chloe
 * @description 针对表【orders】的数据库操作Service实现
 * @createDate 2025-01-12 17:32:21
 */
@Service
@RequiredArgsConstructor
@Slf4j
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
    @Transactional(rollbackFor = Exception.class)
    public Orders createOrder(String customerId, Map<String, Integer> itemQuantities,
                              String paymentMethod, String deliveryMethod, String remarks) {
        // 1. 校验客户存在性
        Customers customer = customersMapper.selectById(customerId);
        if (customer == null) {
            throw new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND);
        }

        // 2. 校验商品库存
        List<String> goodIds = new ArrayList<>(itemQuantities.keySet());
        List<Goods> goodsList = goodsMapper.selectBatchIds(goodIds);
        validateGoods(goodsList, itemQuantities, goodIds);

        // 3. 创建订单记录
        Orders order = buildOrder(customerId, paymentMethod, deliveryMethod, remarks, goodsList, itemQuantities);
        ordersMapper.insert(order);

        // 4. 保存订单商品关联
        saveOrderGoods(order.getOrderId(), goodsList, itemQuantities);

        // 5. 扣减库存
        updateGoodsStock(goodsList, itemQuantities, false);

        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrder(String orderId, Map<String, Integer> newItemQuantities,
                            String newPaymentMethod, String newDeliveryMethod, String newRemarks) {
        // 1. 验证订单状态
        Orders order = getValidOrder(orderId);
        if (!OrderStatus.PENDING.getCode().equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.ORDER_STATUS_ERROR, "只允许修改待发货订单");
        }

        // 2. 释放原库存
        List<OrderGood> oldOrderGoods = orderGoodMapper.selectByOrderId(orderId);
        revertOldStock(oldOrderGoods);

        // 3. 删除原商品关联
        orderGoodMapper.deleteById(orderId);

        // 4. 校验新商品库存
        List<String> newGoodIds = new ArrayList<>(newItemQuantities.keySet());
        List<Goods> newGoodsList = goodsMapper.selectBatchIds(newGoodIds);
        validateGoods(newGoodsList, newItemQuantities, newGoodIds);

        // 5. 更新订单信息
        updateOrderInfo(order, newPaymentMethod, newDeliveryMethod, newRemarks, newGoodsList, newItemQuantities);

        // 6. 保存新商品关联
        saveOrderGoods(orderId, newGoodsList, newItemQuantities);

        // 7. 扣减新库存
        updateGoodsStock(newGoodsList, newItemQuantities, false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(String orderId) {
        // 1. 验证订单状态
        Orders order = getValidOrder(orderId);
        if (!OrderStatus.PENDING.getCode().equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.ORDER_STATUS_ERROR, "只允许取消待发货订单");
        }

        // 2. 释放库存
        List<OrderGood> orderGoods = orderGoodMapper.selectByOrderId(orderId);
        revertOldStock(orderGoods);

        // 3. 更新订单状态
        order.setStatus(OrderStatus.CANCELLED.getCode());
        ordersMapper.updateById(order);
    }

    @Override
    public Orders getOrderDetail(String orderId) {
        Orders order = ordersMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        return order;
    }

    @Override
    public List<Orders> getCustomerOrders(String customerId) {
        Customers customer = customersMapper.selectById(customerId);
        if (customer == null) {
            throw new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        return ordersMapper.selectList(new LambdaQueryWrapper<Orders>()
                .eq(Orders::getCustomerId, customerId)
                .orderByDesc(Orders::getOrderDate));
    }
    @Override
    public List<OrderGood> getOrderGoodsByOrderId(String orderId) {
        if (orderId == null || orderId.isEmpty()) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        return Optional.ofNullable(orderGoodMapper.selectList(
                new LambdaQueryWrapper<OrderGood>()
                        .eq(OrderGood::getOrderId, orderId) // 使用实体类字段引用
        )).orElse(Collections.emptyList()); // 避免返回 null
    }

    @Override
    public List<Goods> batchGetGoodsByIds(Collection<String> goodIds) {
        if (goodIds == null || goodIds.isEmpty()) {
            throw new IllegalArgumentException("商品ID集合不能为空");
        }

        // 限制最大查询数量防止内存溢出
        int maxBatchSize = 1000;
        if (goodIds.size() > maxBatchSize) {
            throw new BusinessException(ErrorCode.QUERY_LIMIT_EXCEEDED);
        }

        return goodsMapper.selectBatchIds(goodIds);
    }

    // 辅助方法---------------------------------------------------------------

    private Orders getValidOrder(String orderId) {
        Orders order = ordersMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        return order;
    }

    private void validateGoods(List<Goods> goodsList, Map<String, Integer> itemQuantities, List<String> goodIds) {
        // 校验商品是否存在
        if (goodsList.size() != goodIds.size()) {
            Set<String> existIds = goodsList.stream()
                    .map(Goods::getGoodId)
                    .collect(Collectors.toSet());
            String missingIds = goodIds.stream()
                    .filter(id -> !existIds.contains(id))
                    .collect(Collectors.joining(","));
            throw new BusinessException(ErrorCode.GOODS_NOT_FOUND, "缺失商品ID: " + missingIds);
        }

        // 校验库存
        goodsList.forEach(good -> {
            int required = itemQuantities.get(good.getGoodId());
            if (good.getQuantity() < required) {
                throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK,
                        String.format("商品[%s]库存不足，剩余%d件", good.getName(), good.getQuantity()));
            }
        });
    }

    private Orders buildOrder(String customerId, String paymentMethod, String deliveryMethod,
                              String remarks, List<Goods> goodsList, Map<String, Integer> itemQuantities) {
        return new Orders()
                .setOrderId(UUID.randomUUID().toString())
                .setCustomerId(customerId)
                .setTotalAmount(calculateTotalAmount(goodsList, itemQuantities))
                .setPaymentMethod(paymentMethod)
                .setDeliveryMethod(deliveryMethod)
                .setRemarks(remarks)
                .setStatus(OrderStatus.PENDING.getCode())
                .setOrderDate(new Date());
    }

    private BigDecimal calculateTotalAmount(List<Goods> goodsList, Map<String, Integer> itemQuantities) {
        return goodsList.stream()
                .map(good -> good.getPrice()
                        .multiply(BigDecimal.valueOf(itemQuantities.get(good.getGoodId()))))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void saveOrderGoods(String orderId, List<Goods> goodsList, Map<String, Integer> itemQuantities) {
        List<OrderGood> orderGoods = goodsList.stream()
                .map(good -> new OrderGood()
                        .setOrderId(orderId)
                        .setGoodId(good.getGoodId())
                        .setQuantity(itemQuantities.get(good.getGoodId())))
                .collect(Collectors.toList());
        orderGoodMapper.insertBatch(orderGoods);
    }

    private void updateGoodsStock(List<Goods> goodsList, Map<String, Integer> itemQuantities, boolean isRevert) {
        goodsList.forEach(good -> {
            int quantityChange = itemQuantities.get(good.getGoodId());
            int newQuantity = isRevert ?
                    good.getQuantity() + quantityChange :
                    good.getQuantity() - quantityChange;

            good.setQuantity(newQuantity);
            if (goodsMapper.updateById(good) == 0) {
                throw new BusinessException(ErrorCode.CONCURRENT_UPDATE_ERROR, "商品库存更新冲突");
            }
        });
    }

    private void revertOldStock(List<OrderGood> oldOrderGoods) {
        List<String> oldGoodIds = oldOrderGoods.stream()
                .map(OrderGood::getGoodId)
                .collect(Collectors.toList());

        List<Goods> oldGoods = goodsMapper.selectBatchIds(oldGoodIds);
        Map<String, Integer> revertQuantities = oldOrderGoods.stream()
                .collect(Collectors.toMap(OrderGood::getGoodId, OrderGood::getQuantity));

        updateGoodsStock(oldGoods, revertQuantities, true);
    }

    private void updateOrderInfo(Orders order, String paymentMethod, String deliveryMethod,
                                 String remarks, List<Goods> newGoods, Map<String, Integer> newQuantities) {
        order.setPaymentMethod(paymentMethod)
                .setDeliveryMethod(deliveryMethod)
                .setRemarks(remarks)
                .setTotalAmount(calculateTotalAmount(newGoods, newQuantities));

        if (ordersMapper.updateById(order) == 0) {
            throw new BusinessException(ErrorCode.CONCURRENT_UPDATE_ERROR, "订单更新冲突");
        }
    }
}