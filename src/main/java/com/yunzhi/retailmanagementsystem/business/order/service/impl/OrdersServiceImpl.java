package com.yunzhi.retailmanagementsystem.business.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunzhi.retailmanagementsystem.business.customer.mapper.CustomersMapper;
import com.yunzhi.retailmanagementsystem.business.good.mapper.GoodsMapper;
import com.yunzhi.retailmanagementsystem.business.order.mapper.OrderGoodMapper;
import com.yunzhi.retailmanagementsystem.business.order.mapper.OrdersMapper;
import com.yunzhi.retailmanagementsystem.business.order.model.dto.OrderCreateDTO;
import com.yunzhi.retailmanagementsystem.business.order.model.dto.OrderItemDTO;
import com.yunzhi.retailmanagementsystem.business.order.model.dto.OrderUpdateDTO;
import com.yunzhi.retailmanagementsystem.business.order.model.vo.OrderDetailVO;
import com.yunzhi.retailmanagementsystem.business.order.model.vo.OrderItemVO;
import com.yunzhi.retailmanagementsystem.business.order.model.vo.OrderVO;
import com.yunzhi.retailmanagementsystem.common.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.business.customer.model.po.Customers;
import com.yunzhi.retailmanagementsystem.business.good.model.po.Goods;
import com.yunzhi.retailmanagementsystem.business.order.model.po.OrderGood;
import com.yunzhi.retailmanagementsystem.business.order.model.po.Orders;
import com.yunzhi.retailmanagementsystem.common.constant.business.OrderStatus;
import com.yunzhi.retailmanagementsystem.common.model.response.ErrorCode;
import com.yunzhi.retailmanagementsystem.business.order.service.OrdersService;
import com.yunzhi.retailmanagementsystem.common.utils.coverter.VOConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yunzhi.retailmanagementsystem.common.utils.coverter.VOConverter.convertToVO;

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
    public OrderVO createOrder(OrderCreateDTO dto) {
        // 1. 校验客户存在性
        Customers customer = customersMapper.selectById(dto.getCustomerId());
        if (customer == null) {
            throw new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND);
        }

        // 2. 校验商品库存
        Map<String, Integer> itemQuantities = convertItemsToMap(dto.getItems());
        List<Goods> goodsList = validateGoodsStock(itemQuantities);

        // 3. 创建订单记录
        Orders order = buildOrder(dto, goodsList, itemQuantities);
        ordersMapper.insert(order);

        // 4. 保存订单商品关联
        saveOrderGoods(order.getOrderId(), goodsList, itemQuantities);

        // 5. 扣减库存
        updateGoodsStock(goodsList, itemQuantities, false);

        return convertToVO(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrder(String orderId, OrderUpdateDTO dto) {
        Orders order = getValidOrder(orderId);
        if (!order.getStatus().equals("PENDING")) {
            throw new BusinessException(ErrorCode.ORDER_STATUS_CONFLICT, "只能修改待发货订单");
        }

        // 释放原库存
        List<OrderGood> oldOrderGoods = orderGoodMapper.selectByOrderId(orderId);
        revertOldStock(oldOrderGoods);

        // 删除原有订单商品
        orderGoodMapper.deleteById(orderId);

        // 校验新商品库存
        Map<String, Integer> newItemQuantities = convertItemsToMap(dto.getItems());
        List<Goods> newGoodsList = validateGoodsStock(newItemQuantities);

        // 更新订单信息
        updateOrderInfo(order, dto, newGoodsList, newItemQuantities);

        // 保存新商品关联
        saveOrderGoods(orderId, newGoodsList, newItemQuantities);

        // 扣减新库存
        updateGoodsStock(newGoodsList, newItemQuantities, false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(String orderId) {
        // 1. 验证订单状态
        Orders order = getValidOrder(orderId);
        if (!order.getStatus().equals("PENDING")) {
            throw new BusinessException(ErrorCode.ORDER_STATUS_CONFLICT, "只允许取消待发货订单");
        }

        // 2. 释放库存
        List<OrderGood> orderGoods = orderGoodMapper.selectByOrderId(orderId);
        revertOldStock(orderGoods);

        // 3. 更新订单状态
        order.setStatus(OrderStatus.CANCELLED.toString());
        ordersMapper.updateById(order);
    }

    @Override
    public OrderDetailVO getOrderDetail(String orderId) {
        Orders order = getValidOrder(orderId);
        Customers customer = customersMapper.selectById(order.getCustomerId());

        List<OrderGood> orderGoods = orderGoodMapper.selectByOrderId(orderId);
        // 批量查询商品信息，避免多次查询数据库
        List<String> goodId = orderGoods.stream()
                .map(OrderGood::getGoodId)
                .collect(Collectors.toList());
        Map<String, Goods> goodsMap = goodsMapper.selectBatchIds(goodId).stream()
                .collect(Collectors.toMap(Goods::getGoodId, Function.identity()));

        // 构建订单商品项列表
        List<OrderItemVO> items = orderGoods.stream()
                .map(og -> {
                    Goods good = goodsMap.get(og.getGoodId());
                    if (good != null) {
                        return new OrderItemVO(og.getGoodId(), good.getName(), good.getPrice(), og.getQuantity());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new OrderDetailVO(order.getOrderId(), customer.getName(), items,
                order.getTotalAmount(), order.getPaymentMethod(), order.getDeliveryMethod(),
                order.getStatus(), order.getOrderDate());
    }

    @Override
    public List<OrderVO> getCustomerOrders(String customerId) {
        Customers customer = customersMapper.selectById(customerId);
        if (customer == null) {
            throw new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        return ordersMapper.selectList(new LambdaQueryWrapper<Orders>()
                        .eq(Orders::getCustomerId, customerId)
                        .orderByDesc(Orders::getOrderDate))
                .stream().map(VOConverter::convertToVO).collect(Collectors.toList());
    }

    // 辅助方法---------------------------------------------------------------

    private Orders getValidOrder(String orderId) {
        try {
            Orders order = ordersMapper.selectByOrderId(orderId);
            if (order == null) {
                throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
            }
            return order;
        } catch (Exception e) {
            log.error("Error : ", e);
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "订单查询失败");
        }
    }

    private List<Goods> validateGoodsStock(Map<String, Integer> itemQuantities) {
        List<Goods> goodsList = goodsMapper.selectBatchIds(new ArrayList<>(itemQuantities.keySet()));
        for (Goods good : goodsList) {
            int required = itemQuantities.get(good.getGoodId());
            if (good.getQuantity() < required) {
                throw new BusinessException(ErrorCode.STOCK_INSUFFICIENT,
                        String.format("商品[%s]库存不足，剩余%d件", good.getName(), good.getQuantity()));
            }
        }
        return goodsList;
    }

    private Orders buildOrder(OrderCreateDTO dto, List<Goods> goodsList, Map<String, Integer> itemQuantities) {
        return new Orders(UUID.randomUUID().toString(), dto.getCustomerId(), LocalDateTime.now(), OrderStatus.PENDING.toString(),
                calculateTotalAmount(goodsList, itemQuantities), dto.getPaymentMethod(),
                dto.getDeliveryMethod(), dto.getRemarks());
    }
    private BigDecimal calculateTotalAmount(List<Goods> goodsList, Map<String, Integer> itemQuantities) {
        return goodsList.stream()
                .map(good -> good.getPrice()
                        .multiply(BigDecimal.valueOf(itemQuantities.get(good.getGoodId()))))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void saveOrderGoods(String orderId, List<Goods> goodsList, Map<String, Integer> itemQuantities) {
        try {
            List<OrderGood> orderGoods = goodsList.stream()
                    .map(good -> new OrderGood()
                            .setOrderId(orderId)
                            .setGoodId(good.getGoodId())
                            .setQuantity(itemQuantities.get(good.getGoodId())))
                    .collect(Collectors.toList());
            orderGoodMapper.insertBatch(orderGoods);
        } catch (Exception e) {
            log.error("Error : ", e);
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "订单商品关联创建失败");
        }



    }

    private void updateGoodsStock(List<Goods> goodsList, Map<String, Integer> itemQuantities, boolean isRevert) {
        goodsList.forEach(good -> {
            int quantityChange = itemQuantities.get(good.getGoodId());
            int newQuantity = isRevert ?
                    good.getQuantity() + quantityChange :
                    good.getQuantity() - quantityChange;
            good.setQuantity(newQuantity);
            if (goodsMapper.updateById(good) == 0) {
                throw new BusinessException(ErrorCode.CONCURRENT_CONFLICT, "商品库存更新冲突");
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

    private void updateOrderInfo(Orders order, OrderUpdateDTO dto, List<Goods> newGoods, Map<String, Integer> newQuantities) {
        order.setPaymentMethod(dto.getPaymentMethod())
                .setDeliveryMethod(dto.getDeliveryMethod())
                .setRemarks(dto.getRemarks())
                .setTotalAmount(calculateTotalAmount(newGoods, newQuantities));
        try {
            ordersMapper.updateById(order);
        } catch (Exception e) {
            log.error("Error : ", e);
            throw new BusinessException(ErrorCode.CONCURRENT_CONFLICT, "订单更新冲突");

        }
    }
    /**
     * 将 OrderItemDTO 列表转换为 Map<goodId, quantity>
     */
    public Map<String, Integer> convertItemsToMap(List<OrderItemDTO> items) {
        Map<String, Integer> collect = items.stream()
                .collect(Collectors.toMap(
                        OrderItemDTO::getGoodId,
                        OrderItemDTO::getQuantity,
                        (existing, replacement) -> existing + replacement // 处理重复键，将数量相加
                ));
        return collect;
    }
}