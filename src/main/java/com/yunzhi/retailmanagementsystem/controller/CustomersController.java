package com.yunzhi.retailmanagementsystem.controller;

import com.yunzhi.retailmanagementsystem.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.model.domain.dto.CustomerCreateDTO;
import com.yunzhi.retailmanagementsystem.model.domain.dto.CustomerUpdateDTO;
import com.yunzhi.retailmanagementsystem.model.domain.vo.CustomerVO;
import com.yunzhi.retailmanagementsystem.response.BaseResponse;
import com.yunzhi.retailmanagementsystem.response.ErrorCode;
import com.yunzhi.retailmanagementsystem.response.ResultUtils;
import com.yunzhi.retailmanagementsystem.service.CustomersService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.yunzhi.retailmanagementsystem.utils.VOConverter.convertToCustomerVO;

/**
 * 客户管理控制器
 */
@Validated
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomersController {
    private final CustomersService customersService;

    //━━━━━━━━━━━━━━ 端点实现 ━━━━━━━━━━━━━━
    @PostMapping("/add")
    public BaseResponse<String> createCustomer(@RequestBody @Valid CustomerCreateDTO dto) {
        return ResultUtils.success(
                customersService.createCustomer(dto.name(), dto.email(), dto.phone())
        );
    }


    @PutMapping("/{customerId}")
    public BaseResponse<Void> updateCustomer(
            @PathVariable String customerId,
            @RequestBody @Valid CustomerUpdateDTO dto
    ) {
        customersService.updateCustomer(customerId, dto.name(), dto.email(), dto.phone());
        return ResultUtils.success("客户添加成功");
    }


    @GetMapping
    public BaseResponse<CustomerVO> getCustomer(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String email
    ) {
        if (customerId == null && email == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "至少需要提供一个查询参数");
        }
        return ResultUtils.success(convertToCustomerVO(customersService.getCustomerByIdOrEmail(customerId, email)), null);
    }

}
