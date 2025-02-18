package com.yunzhi.retailmanagementsystem.controller;

import com.yunzhi.retailmanagementsystem.model.domain.po.Customers;
import com.yunzhi.retailmanagementsystem.model.domain.po.Goods;
import com.yunzhi.retailmanagementsystem.model.domain.po.OrderGood;
import com.yunzhi.retailmanagementsystem.model.domain.po.Orders;
import com.yunzhi.retailmanagementsystem.model.domain.dto.OrderCreateDTO;
import com.yunzhi.retailmanagementsystem.model.domain.dto.OrderUpdateDTO;
import com.yunzhi.retailmanagementsystem.model.domain.vo.OrderDetailVO;
import com.yunzhi.retailmanagementsystem.model.domain.vo.OrderItemVO;
import com.yunzhi.retailmanagementsystem.model.domain.vo.OrderVO;
import com.yunzhi.retailmanagementsystem.response.BaseResponse;
import com.yunzhi.retailmanagementsystem.response.ResultUtils;
import com.yunzhi.retailmanagementsystem.service.CustomersService;
import com.yunzhi.retailmanagementsystem.service.OrdersService;
import com.yunzhi.retailmanagementsystem.utils.VOConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "订单管理", description = "订单创建、查询、更新等操作")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private CustomersService customersService;

    //━━━━━━━━━━━━━━ 端点实现 ━━━━━━━━━━━━━━
    @Operation(summary = "创建订单")
    @PostMapping
    public ResponseEntity<BaseResponse<OrderVO>> createOrder(
            @RequestBody @Valid OrderCreateDTO dto
    ) {
        // DTO 转服务层参数
        Map<String, Integer> itemMap = VOConverter.convertItemsToMap(dto.items());

        // 调用服务层
        Orders order = ordersService.createOrder(
                dto.customerId(),
                itemMap,
                dto.paymentMethod(),
                dto.deliveryMethod(),
                dto.remarks()
        );

        // 领域模型转 VO
        return ResponseEntity.ok(ResultUtils.success(VOConverter.convertToOrderVO(order), null));
    }

    @Operation(summary = "获取订单详情")
    @GetMapping("/{orderId}")
    public ResponseEntity<BaseResponse<OrderDetailVO>> getOrderDetail(
            @PathVariable @NotBlank String orderId
    ) {
        Orders order = ordersService.getOrderDetail(orderId);
        Customers customer = customersService.getCustomerByIdOrEmail(order.getCustomerId(), null);

        // 获取订单商品关联记录
        List<OrderGood> orderGoods = ordersService.getOrderGoodsByOrderId(orderId);

        // 转换商品条目为 VO
        List<OrderItemVO> items = VOConverter.convertToItemVOs(
                orderGoods,
                goodIds -> ordersService.batchGetGoodsByIds(goodIds)
                        .stream()
                        .collect(Collectors.toMap(Goods::getGoodId, Function.identity()))
        );

        OrderDetailVO vo = new OrderDetailVO(
                order.getOrderId(),
                customer.getName(),
                items,
                order.getTotalAmount(),
                order.getPaymentMethod(),
                order.getDeliveryMethod(),
                order.getStatus(),
                order.getOrderDate()
        );

        return ResponseEntity.ok(ResultUtils.success(vo, "订单创建成功"));
    }

    @Operation(summary = "更新订单")
    @PutMapping("/{orderId}")
    public ResponseEntity<BaseResponse<Void>> updateOrder(
            @PathVariable String orderId,
            @RequestBody @Valid OrderUpdateDTO dto
    ) {
        Map<String, Integer> itemMap = VOConverter.convertItemsToMap(dto.items());

        ordersService.updateOrder(
                orderId,
                itemMap,
                dto.paymentMethod(),
                dto.deliveryMethod(),
                dto.remarks()
        );

        return ResponseEntity.ok(ResultUtils.success("订单修改成功"));
    }

    @Operation(summary = "取消订单")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<BaseResponse<Void>> cancelOrder(
            @PathVariable @NotBlank String orderId
    ) {
        ordersService.cancelOrder(orderId);
        return ResponseEntity.ok(ResultUtils.success("订单已取消"));
    }

    @Operation(summary = "获取客户订单列表")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<BaseResponse<List<OrderVO>>> getCustomerOrders(
            @PathVariable @NotBlank String customerId
    ) {
        List<Orders> orders = ordersService.getCustomerOrders(customerId);
        List<OrderVO> vos = orders.stream()
                .map(VOConverter::convertToOrderVO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ResultUtils.success(vos, "客户订单列表获取成功"));
    }
}