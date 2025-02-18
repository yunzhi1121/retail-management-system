package com.yunzhi.retailmanagementsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunzhi.retailmanagementsystem.Mapper.CustomersMapper;
import com.yunzhi.retailmanagementsystem.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.response.ErrorCode;
import com.yunzhi.retailmanagementsystem.model.domain.po.Customers;
import com.yunzhi.retailmanagementsystem.service.CustomersService;
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
    public String createCustomer(String name, String email, String phone) {
        // 参数校验
        validateNotEmpty(name, "客户姓名");
        validateNotEmpty(email, "邮箱");
        validateFormat(email, EMAIL_REGEX, ErrorCode.EMAIL_FORMAT_ERROR);
        if (phone != null) validateFormat(phone, PHONE_REGEX, ErrorCode.PHONE_FORMAT_ERROR);

        // 检查邮箱唯一性
        if (lambdaQuery().eq(Customers::getEmail, email).one() != null) {
            throw new BusinessException(ErrorCode.EMAIL_EXISTS);
        }

        // 创建客户
        Customers customer = new Customers();
        customer.setCustomerId(UUID.randomUUID().toString());
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);

        if (!save(customer)) {
            log.error("客户创建失败：{}", customer);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "客户创建失败");
        }
        return customer.getCustomerId();
    }

    @Override
    public void updateCustomer(String customerID, String name, String email, String phone) {
        // 参数校验
        validateNotEmpty(customerID, "客户ID");
        if (email != null) validateFormat(email, EMAIL_REGEX, ErrorCode.EMAIL_FORMAT_ERROR);
        if (phone != null) validateFormat(phone, PHONE_REGEX, ErrorCode.PHONE_FORMAT_ERROR);

        // 获取现有客户
        Customers customer = lambdaQuery()
                .eq(Customers::getCustomerId, customerID)
                .oneOpt()
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND));

        // 邮箱冲突检查
        if (email != null && !email.equals(customer.getEmail())) {
            if (lambdaQuery().eq(Customers::getEmail, email).exists()) {
                throw new BusinessException(ErrorCode.EMAIL_EXISTS);
            }
        }

        // 更新字段
        boolean updated = lambdaUpdate()
                .eq(Customers::getCustomerId, customerID)
                .set(name != null && !name.isEmpty(), Customers::getName, name)
                .set(email != null && !email.isEmpty(), Customers::getEmail, email)
                .set(phone != null && !phone.isEmpty(), Customers::getPhone, phone)
                .update();

        if (!updated) {
            log.warn("客户信息更新失败：{}", customerID);
            throw new BusinessException(ErrorCode.UPDATE_FAILED);
        }
    }

    @Override
    public Customers getCustomerByIdOrEmail(String customerID, String email) {
        // 参数校验
        if (customerID == null && email == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "至少提供一个查询参数");
        }

        return lambdaQuery()
                .eq(customerID != null, Customers::getCustomerId, customerID)
                .eq(email != null, Customers::getEmail, email)
                .oneOpt()
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND));
    }

    // 通用校验方法
    private void validateNotEmpty(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, field + "不能为空");
        }
    }

    private void validateFormat(String value, Pattern pattern, ErrorCode errorCode) {
        if (!pattern.matcher(value).matches()) {
            throw new BusinessException(errorCode);
        }
    }
}