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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "创建订单", description = "根据传入的订单创建信息创建一个新的订单")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "订单创建成功",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "请求参数错误",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "服务器内部错误",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/add")
    @RequiresPermission(Permission.ORDERS_CREATE)
    public ResponseEntity<BaseResponse<OrderVO>> createOrder(@RequestBody @Valid @Parameter(description = "订单创建信息", required = true) OrderCreateDTO dto) throws Exception {
        return ResponseEntity.ok(ResultUtils.success(ordersService.createOrder(dto), "订单创建成功"));
    }

    @Operation(summary = "更新订单", description = "根据订单 ID 和传入的更新信息更新订单")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "订单修改成功",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "请求参数错误",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "未找到指定订单",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "服务器内部错误",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class)))
    })
    @PutMapping("/{orderId}")
    @RequiresPermission(Permission.ORDERS_UPDATE)
    public ResponseEntity<BaseResponse<Void>> updateOrder(@PathVariable @Parameter(description = "订单 ID", required = true) String orderId, @RequestBody @Valid @Parameter(description = "订单更新信息", required = true) OrderUpdateDTO dto) throws Exception {
        ordersService.updateOrder(orderId, dto);
        return ResponseEntity.ok(ResultUtils.success("订单修改成功"));
    }

    @Operation(summary = "获取订单详情", description = "根据订单 ID 获取订单的详细信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "获取订单详情成功",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "未找到指定订单",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "服务器内部错误",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/{orderId}")
    @RequiresPermission(Permission.ORDERS_READ)
    public ResponseEntity<BaseResponse<OrderDetailVO>> getOrderDetail(@PathVariable @NotBlank @Parameter(description = "订单 ID", required = true) String orderId) {
        return ResponseEntity.ok(ResultUtils.success(ordersService.getOrderDetail(orderId), "获取订单详情成功"));
    }

    @Operation(summary = "取消订单", description = "根据订单 ID 取消指定的订单")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "订单已取消",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "未找到指定订单",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "服务器内部错误",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class)))
    })
    @DeleteMapping("/{orderId}")
    @RequiresPermission(Permission.ORDERS_UPDATE)
    public ResponseEntity<BaseResponse<Void>> cancelOrder(@PathVariable @NotBlank @Parameter(description = "订单 ID", required = true) String orderId) {
        ordersService.cancelOrder(orderId);
        return ResponseEntity.ok(ResultUtils.success("订单已取消"));
    }

    @Operation(summary = "获取客户订单列表", description = "根据客户 ID 获取该客户的所有订单列表")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "客户订单列表获取成功",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "未找到指定客户的订单",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "服务器内部错误",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/customer/{customerId}")
    @RequiresPermission(Permission.ORDERS_READ)
    public ResponseEntity<BaseResponse<List<OrderVO>>> getCustomerOrders(@PathVariable @NotBlank @Parameter(description = "客户 ID", required = true) String customerId) {
        return ResponseEntity.ok(ResultUtils.success(ordersService.getCustomerOrders(customerId), "客户订单列表获取成功"));
    }
}