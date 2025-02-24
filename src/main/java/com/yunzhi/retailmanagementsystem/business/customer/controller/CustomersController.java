package com.yunzhi.retailmanagementsystem.business.customer.controller;

import com.yunzhi.retailmanagementsystem.business.customer.model.dto.CustomerCreateDTO;
import com.yunzhi.retailmanagementsystem.business.customer.model.dto.CustomerUpdateDTO;
import com.yunzhi.retailmanagementsystem.business.customer.model.vo.CustomerVO;
import com.yunzhi.retailmanagementsystem.business.customer.service.CustomersService;
import com.yunzhi.retailmanagementsystem.common.constant.security.Permission;
import com.yunzhi.retailmanagementsystem.common.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.common.model.response.BaseResponse;
import com.yunzhi.retailmanagementsystem.common.model.response.ErrorCode;
import com.yunzhi.retailmanagementsystem.common.model.response.ResultUtils;
import com.yunzhi.retailmanagementsystem.common.security.authorization.RequiresPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.yunzhi.retailmanagementsystem.common.utils.coverter.VOConverter.convertToVO;

/**
 * 客户管理控制器
 */
@Validated
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Tag(name = "客户管理", description = "提供客户的创建、更新和查询功能")
public class CustomersController {
    @Autowired
    private CustomersService customersService;

    //━━━━━━━━━━━━━━ 端点实现 ━━━━━━━━━━━━━━
    @PostMapping("/add")
    @RequiresPermission(Permission.CUSTOMERS_CREATE)
    @Operation(summary = "创建客户", description = "根据传入的客户信息创建一个新的客户记录")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "客户添加成功",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "请求参数错误",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "服务器内部错误",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class)))
    })
    public BaseResponse<CustomerVO> createCustomer(@RequestBody @Valid CustomerCreateDTO dto) {
        return ResultUtils.success(customersService.createCustomer(dto), "客户添加成功");
    }

    @PutMapping("/{customerId}")
    @RequiresPermission(Permission.CUSTOMERS_UPDATE)
    @Operation(summary = "更新客户信息", description = "根据客户 ID 和传入的更新信息更新客户记录")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "客户信息更新成功",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "请求参数错误",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "未找到指定客户",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "服务器内部错误",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class)))
    })
    public BaseResponse<CustomerVO> updateCustomer(
            @Parameter(description = "客户 ID", required = true) @PathVariable String customerId,
            @RequestBody @Valid CustomerUpdateDTO dto
    ) {
        return ResultUtils.success(customersService.updateCustomer(customerId, dto), "客户信息更新成功");
    }

    @GetMapping
    @RequiresPermission(Permission.CUSTOMERS_READ)
    @Operation(summary = "查询客户信息", description = "根据客户 ID 或邮箱查询客户信息，至少需要提供一个查询参数")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "客户信息查询成功",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "请求参数错误，至少需要提供一个查询参数",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "未找到指定客户",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "服务器内部错误",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class)))
    })
    public BaseResponse<CustomerVO> getCustomer(
            @Parameter(description = "客户 ID", required = false) @RequestParam(required = false) String customerId,
            @Parameter(description = "客户邮箱", required = false) @RequestParam(required = false) String email
    ) {
        if (customerId == null && email == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "至少需要提供一个查询参数");
        }
        return ResultUtils.success(convertToVO(customersService.getCustomerByIdOrEmail(customerId, email)), "客户信息查询成功");
    }
}