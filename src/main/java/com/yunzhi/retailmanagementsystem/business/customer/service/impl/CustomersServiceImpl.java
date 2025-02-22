package com.yunzhi.retailmanagementsystem.business.customer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunzhi.retailmanagementsystem.business.customer.mapper.CustomersMapper;
import com.yunzhi.retailmanagementsystem.business.customer.model.dto.CustomerCreateDTO;
import com.yunzhi.retailmanagementsystem.business.customer.model.dto.CustomerUpdateDTO;
import com.yunzhi.retailmanagementsystem.business.customer.model.vo.CustomerVO;
import com.yunzhi.retailmanagementsystem.common.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.common.model.response.ErrorCode;
import com.yunzhi.retailmanagementsystem.business.customer.model.po.Customers;
import com.yunzhi.retailmanagementsystem.business.customer.service.CustomersService;
import com.yunzhi.retailmanagementsystem.common.utils.coverter.VOConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author Chloe
 * @description 针对表【customers】的数据库操作Service实现
 * @createDate 2025-01-12 17:32:33
 */
@Service
@Slf4j
public class CustomersServiceImpl extends ServiceImpl<CustomersMapper, Customers> implements CustomersService {
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$");
    private static final Pattern PHONE_REGEX = Pattern.compile("^1[3-9]\\d{9}$");

    @Override
    public CustomerVO createCustomer(CustomerCreateDTO customerCreateDTO) throws BusinessException {
        // 参数校验
        validateNotEmpty(customerCreateDTO.getName(), "客户姓名");
        validateNotEmpty(customerCreateDTO.getEmail(), "邮箱");
        validateFormat(customerCreateDTO.getEmail(), EMAIL_REGEX, ErrorCode.PARAM_FORMAT_ERROR);
        if (customerCreateDTO.getPhone() != null) validateFormat(customerCreateDTO.getPhone(), PHONE_REGEX, ErrorCode.PARAM_FORMAT_ERROR);

        // 检查邮箱唯一性
        if (lambdaQuery().eq(Customers::getEmail, customerCreateDTO.getEmail()).one() != null) {
            throw new BusinessException(ErrorCode.DUPLICATE_DATA, "邮箱已存在");
        }

        // 创建客户
        Customers customer = new Customers();
        customer.setCustomerId(UUID.randomUUID().toString());
        customer.setName(customerCreateDTO.getName());
        customer.setEmail(customerCreateDTO.getEmail());
        customer.setPhone(customerCreateDTO.getPhone());

        try {
            save(customer);
        } catch (Exception e) {
            log.error("Error saving customer: ", e);
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "客户创建失败");
        }

        return VOConverter.convertToVO(customer);
    }

    @Override
    public CustomerVO updateCustomer(String customerID, CustomerUpdateDTO customerUpdateDTO) {
        // 参数校验
        validateNotEmpty(customerID, "客户ID");
        if (customerUpdateDTO.getEmail() != null) validateFormat(customerUpdateDTO.getEmail(), EMAIL_REGEX, ErrorCode.PARAM_FORMAT_ERROR);
        if (customerUpdateDTO.getPhone() != null) validateFormat(customerUpdateDTO.getPhone(), PHONE_REGEX, ErrorCode.PARAM_FORMAT_ERROR);

        // 获取现有客户
        Customers customer = lambdaQuery()
                .eq(Customers::getCustomerId, customerID)
                .oneOpt()
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND));

        // 邮箱冲突检查
        if (customerUpdateDTO.getEmail() != null && !customerUpdateDTO.getEmail().equals(customer.getEmail())) {
            if (lambdaQuery().eq(Customers::getEmail, customerUpdateDTO.getEmail()).exists()) {
                throw new BusinessException(ErrorCode.DUPLICATE_DATA);
            }
        }
        // 更新字段
        try {
            lambdaUpdate()
                    .eq(Customers::getCustomerId, customerID)
                    .set(customerUpdateDTO.getName() != null && !customerUpdateDTO.getName().isEmpty(), Customers::getName, customerUpdateDTO.getName())
                    .set(customerUpdateDTO.getEmail() != null && !customerUpdateDTO.getEmail().isEmpty(), Customers::getEmail, customerUpdateDTO.getEmail())
                    .set(customerUpdateDTO.getPhone() != null && !customerUpdateDTO.getPhone().isEmpty(), Customers::getPhone, customerUpdateDTO.getPhone())
                    .update();
        } catch (Exception e) {
            log.warn("客户信息更新失败：{}", customerID);
            throw new BusinessException(ErrorCode.DB_OPERATION_FAILED, "客户信息更新失败");
        }
        return VOConverter.convertToVO(customer);
    }

    @Override
    public Customers getCustomerByIdOrEmail(String customerID, String email) {
        // 参数校验
        if (customerID == null && email == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "至少提供一个查询参数");
        }

        return lambdaQuery()
                .eq(customerID != null, Customers::getCustomerId, customerID)
                .eq(email != null, Customers::getEmail, email)
                .oneOpt()
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND));
    }

    //━━━━━━━━━━━━━━ 工具方法 ━━━━━━━━━━━━━━
    private void validateNotEmpty(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, field + "不能为空");
        }
    }

    private void validateFormat(String value, Pattern pattern, ErrorCode errorCode) {
        try {
            pattern.matcher(value).matches();
        } catch (Exception e) {
            log.error("Error : ", e);
            throw new BusinessException(errorCode);
        }
    }
}