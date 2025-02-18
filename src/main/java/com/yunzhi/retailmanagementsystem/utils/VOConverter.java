package com.yunzhi.retailmanagementsystem.utils;

import com.yunzhi.retailmanagementsystem.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.model.domain.dto.OrderItemDTO;
import com.yunzhi.retailmanagementsystem.model.domain.po.*;
import com.yunzhi.retailmanagementsystem.model.domain.vo.*;
import com.yunzhi.retailmanagementsystem.response.ErrorCode;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VOConverter {
    /**
     * 将 Users 对象转换为 UserVO
     */
//    public static UserVO convertToUserVO(Users user) {
//        return new UserVO(
//                user.getUserId(),
//                user.getUsername(),
//                user.getRole(),
//                user.isApproved()
//        );
//    }

    /**
     * 将 Users 对象转换为 UserUnapprovedVO
     */
    public static UserUnapprovedVO convertToUserUnapprovedVO(Users user) {
        return new UserUnapprovedVO(
                user.getUserId(),
                user.getUsername()
        );
    }
    public static CustomerVO convertToCustomerVO(Customers customer) {
        if (customer == null) {
            return null;
        }
        return new CustomerVO(
                customer.getCustomerId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone()
        );
    }
    public static OrderVO convertToOrderVO(Orders order) {
        if (order == null) {
            return null;
        }
        return new OrderVO(
                order.getOrderId(),
                order.getCustomerId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getOrderDate()
        );
    }

    /**
     * 将 OrderItemDTO 列表转换为 Map<goodId, quantity>
     */
    public static Map<String, Integer> convertItemsToMap(List<OrderItemDTO> items) {
        return items.stream()
                .collect(Collectors.toMap(
                        OrderItemDTO::goodId,
                        OrderItemDTO::quantity
                ));
    }

    /**
     * 将 OrderGood 列表转换为 OrderItemVO 列表
     */
    public static List<OrderItemVO> convertToItemVOs(
            List<OrderGood> orderGoods,
            Function<Set<String>, Map<String, Goods>> batchGetGoodsByIds
    ) {
        // 提取所有商品ID
        Set<String> goodIds = orderGoods.stream()
                .map(OrderGood::getGoodId)
                .collect(Collectors.toSet());

        // 批量查询商品信息
        Map<String, Goods> goodsMap = batchGetGoodsByIds.apply(goodIds);

        // 构建VO列表
        return orderGoods.stream()
                .map(og -> {
                    Goods good = goodsMap.get(og.getGoodId());
                    if (good == null) {
                        throw new BusinessException(ErrorCode.GOODS_NOT_FOUND,
                                "商品不存在: " + og.getGoodId());
                    }

                    return new OrderItemVO(
                            og.getGoodId(),
                            good.getName(),    // 从Goods获取
                            good.getPrice(),   // 从Goods获取
                            og.getQuantity()
                    );
                })
                .collect(Collectors.toList());
    }

}