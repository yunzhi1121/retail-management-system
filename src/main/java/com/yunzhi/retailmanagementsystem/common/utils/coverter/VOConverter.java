package com.yunzhi.retailmanagementsystem.common.utils.coverter;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.yunzhi.retailmanagementsystem.business.customer.model.po.Customers;
import com.yunzhi.retailmanagementsystem.business.customer.model.vo.CustomerVO;
import com.yunzhi.retailmanagementsystem.business.good.model.po.Goods;
import com.yunzhi.retailmanagementsystem.business.good.model.vo.GoodsVO;
import com.yunzhi.retailmanagementsystem.business.order.model.dto.OrderItemDTO;
import com.yunzhi.retailmanagementsystem.business.order.model.po.OrderGood;
import com.yunzhi.retailmanagementsystem.business.order.model.po.Orders;
import com.yunzhi.retailmanagementsystem.business.order.model.vo.OrderItemVO;
import com.yunzhi.retailmanagementsystem.business.order.model.vo.OrderVO;
import com.yunzhi.retailmanagementsystem.business.report.model.po.Reports;
import com.yunzhi.retailmanagementsystem.business.report.model.vo.ReportVO;
import com.yunzhi.retailmanagementsystem.business.user.model.po.Users;
import com.yunzhi.retailmanagementsystem.business.user.model.vo.UserVO;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VOConverter {
    // 通用属性拷贝方法（处理基础字段拷贝）
    private static <T> void copyCommonProperties(Object source, T target) {
        if (source == null || target == null) return;
        BeanUtils.copyProperties(source, target);
    }

    // 用户相关转换
    public static UserVO convertToVO(Users user) {
        if (user == null) return null;
        UserVO vo = new UserVO();
        copyCommonProperties(user, vo);
        return vo;
    }

    // 客户转换
    public static CustomerVO convertToVO(Customers customer) {
        if (customer == null) return null;
        CustomerVO vo = new CustomerVO();
        copyCommonProperties(customer, vo);
        // 可添加特殊处理逻辑
        return vo;
    }

    // 货物转换
    public static GoodsVO convertToVO(Goods goods) {
        if (goods == null) return null;
        GoodsVO vo = new GoodsVO();
        copyCommonProperties(goods, vo);
        // 可添加特殊处理逻辑
        return vo;
    }

    // 订单转换
    public static OrderVO convertToVO(Orders order) {
        if (order == null) return null;
        OrderVO vo = new OrderVO();
        copyCommonProperties(order, vo);
        return vo;
    }

    // 商品项转换（带批量查询优化）
    public static List<OrderItemVO> convertToItemVOs(
            List<OrderGood> orderGoods,
            Function<Set<String>, Map<String, Goods>> goodsLoader) {

        if (CollectionUtils.isEmpty(orderGoods)) return Collections.emptyList();

        Set<String> goodIds = orderGoods.stream()
                .map(OrderGood::getGoodId)
                .collect(Collectors.toSet());

        Map<String, Goods> goodsMap = goodsLoader.apply(goodIds);

        return orderGoods.stream()
                .map(og -> convertToItemVO(og, goodsMap.get(og.getGoodId())))
                .collect(Collectors.toList());
    }

    private static OrderItemVO convertToItemVO(OrderGood orderGood, Goods good) {
        OrderItemVO vo = new OrderItemVO();
        copyCommonProperties(orderGood, vo);
        if (good != null) {
            vo.setGoodName(good.getName());
            vo.setPrice(good.getPrice());
        }
        return vo;
    }

    // 报表转换
    public static ReportVO convertToVO(Reports report) {
        if (report == null) return null;
        ReportVO vo = new ReportVO();
        copyCommonProperties(report, vo);
        // 添加报表特定解析逻辑
        return vo;
    }

    // 通用集合转换
    public static <T, R> List<R> convertList(List<T> sourceList, Function<T, R> converter) {
        return sourceList.stream()
                .map(converter)
                .collect(Collectors.toList());
    }


}