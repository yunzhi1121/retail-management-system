package com.yunzhi.retailmanagementsystem.business.order.controller;

import com.yunzhi.retailmanagementsystem.business.customer.model.po.Customers;
import com.yunzhi.retailmanagementsystem.business.customer.service.CustomersService;
import com.yunzhi.retailmanagementsystem.business.good.model.po.Goods;
import com.yunzhi.retailmanagementsystem.business.order.model.dto.OrderCreateDTO;
import com.yunzhi.retailmanagementsystem.business.order.model.dto.OrderUpdateDTO;
import com.yunzhi.retailmanagementsystem.business.order.model.po.OrderGood;
import com.yunzhi.retailmanagementsystem.business.order.model.po.Orders;
import com.yunzhi.retailmanagementsystem.business.order.model.vo.OrderDetailVO;
import com.yunzhi.retailmanagementsystem.business.order.model.vo.OrderItemVO;
import com.yunzhi.retailmanagementsystem.business.order.model.vo.OrderVO;
import com.yunzhi.retailmanagementsystem.business.order.service.OrdersService;
import com.yunzhi.retailmanagementsystem.common.constant.security.Permission;
import com.yunzhi.retailmanagementsystem.common.model.response.BaseResponse;
import com.yunzhi.retailmanagementsystem.common.model.response.ResultUtils;
import com.yunzhi.retailmanagementsystem.common.security.authorization.RequiresPermission;
import com.yunzhi.retailmanagementsystem.common.utils.coverter.VOConverter;
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
    @PostMapping("/add")
    @RequiresPermission(Permission.ORDERS_CREATE)
    public ResponseEntity<BaseResponse<OrderVO>> createOrder(@RequestBody @Valid OrderCreateDTO dto) throws Exception {
        return ResponseEntity.ok(ResultUtils.success(ordersService.createOrder(dto), "订单创建成功"));
    }

    @Operation(summary = "更新订单")
    @PutMapping("/{orderId}")
    @RequiresPermission(Permission.ORDERS_UPDATE)
    public ResponseEntity<BaseResponse<Void>> updateOrder(@PathVariable String orderId, @RequestBody @Valid OrderUpdateDTO dto) throws Exception {
        ordersService.updateOrder(orderId, dto);
        return ResponseEntity.ok(ResultUtils.success("订单修改成功"));
    }

    @Operation(summary = "获取订单详情")
    @GetMapping("/{orderId}")
    @RequiresPermission(Permission.ORDERS_READ)
    public ResponseEntity<BaseResponse<OrderDetailVO>> getOrderDetail(@PathVariable @NotBlank String orderId) {
        return ResponseEntity.ok(ResultUtils.success(ordersService.getOrderDetail(orderId), "获取订单详情成功"));
    }

    @Operation(summary = "取消订单")
    @DeleteMapping("/{orderId}")
    @RequiresPermission(Permission.ORDERS_UPDATE)
    public ResponseEntity<BaseResponse<Void>> cancelOrder(@PathVariable @NotBlank String orderId) {
        ordersService.cancelOrder(orderId);
        return ResponseEntity.ok(ResultUtils.success("订单已取消"));
    }

    @Operation(summary = "获取客户订单列表")
    @GetMapping("/customer/{customerId}")
    @RequiresPermission(Permission.ORDERS_READ)
    public ResponseEntity<BaseResponse<List<OrderVO>>> getCustomerOrders(@PathVariable @NotBlank String customerId) {
        return ResponseEntity.ok(ResultUtils.success(ordersService.getCustomerOrders(customerId), "客户订单列表获取成功"));
    }
}