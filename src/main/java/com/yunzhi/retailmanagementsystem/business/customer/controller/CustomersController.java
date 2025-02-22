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
public class CustomersController {
    @Autowired
    private CustomersService customersService;

    //━━━━━━━━━━━━━━ 端点实现 ━━━━━━━━━━━━━━
    @PostMapping("/add")
    @RequiresPermission(Permission.CUSTOMERS_CREATE)
    public BaseResponse<CustomerVO> createCustomer(@RequestBody @Valid CustomerCreateDTO dto) {
        return ResultUtils.success(customersService.createCustomer(dto), "客户添加成功" );
    }


    @PutMapping("/{customerId}")
    @RequiresPermission(Permission.CUSTOMERS_UPDATE)
    public BaseResponse<CustomerVO> updateCustomer(
            @PathVariable String customerId,
            @RequestBody @Valid CustomerUpdateDTO dto
    ) {
        customersService.updateCustomer(customerId, dto);
        return ResultUtils.success(customersService.updateCustomer(customerId, dto), "客户信息更新成功");
    }


    @GetMapping
    @RequiresPermission(Permission.CUSTOMERS_READ)
    public BaseResponse<CustomerVO> getCustomer(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String email
    ) {
        if (customerId == null && email == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "至少需要提供一个查询参数");
        }
        return ResultUtils.success(convertToVO(customersService.getCustomerByIdOrEmail(customerId, email)), "客户信息查询成功");
    }

}
