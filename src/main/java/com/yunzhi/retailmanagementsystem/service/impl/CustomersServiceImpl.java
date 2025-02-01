package com.yunzhi.retailmanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunzhi.retailmanagementsystem.model.domain.Customers;
import com.yunzhi.retailmanagementsystem.service.CustomersService;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author Chloe
 * @description 针对表【customers】的数据库操作Service实现
 * @createDate 2025-01-12 17:32:33
 */
@Service
public class CustomersServiceImpl extends ServiceImpl<com.yunzhi.retailmanagementsystem.Mapper.CustomersMapper, Customers> implements CustomersService {

    // 邮箱格式正则表达式
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    // 电话号码格式正则表达式（假设为11位数字）
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{11}$");

    @Override
    public String createCustomerAndReturnId(String name, String email, String phone) {
        // 输入参数校验
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer email cannot be null or empty.");
        }
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        if (phone != null &&!isValidPhone(phone)) {
            throw new IllegalArgumentException("Invalid phone number format.");
        }

        // 检查 email 是否已存在
        LambdaQueryWrapper<Customers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customers::getEmail, email);
        Customers existingCustomer = this.getOne(queryWrapper);

        if (existingCustomer != null) {
            return null; // Email 已存在，返回 null 表示创建失败
        }

        // 创建客户对象
        Customers newCustomer = new Customers();
        String customerId = UUID.randomUUID().toString(); // 生成唯一 ID
        newCustomer.setCustomerID(customerId);
        newCustomer.setName(name);
        newCustomer.setEmail(email);
        newCustomer.setPhone(phone);

        try {
            boolean result = this.save(newCustomer);
            return result ? customerId : null; // 插入成功返回客户 ID，失败返回 null
        } catch (Exception e) {
            // 记录异常日志，实际项目中可以使用日志框架（如 Log4j、SLF4J 等）
            System.err.println("Error creating customer: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean updateCustomerInfo(String customerID, String name, String email, String phone) {
        // 输入参数校验
        if (customerID == null || customerID.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty.");
        }
        if (name != null && name.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be empty.");
        }
        if (email != null &&!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        if (phone != null &&!isValidPhone(phone)) {
            throw new IllegalArgumentException("Invalid phone number format.");
        }

        try {
            // 查询是否存在该客户
            Customers customer = this.getById(customerID);
            if (customer == null) {
                return false; // 客户不存在
            }

            // 检查新邮箱是否已被其他客户使用
            if (email != null &&!email.equals(customer.getEmail())) {
                LambdaQueryWrapper<Customers> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Customers::getEmail, email)
                        .ne(Customers::getCustomerID, customerID);
                Customers existingCustomerWithEmail = this.getOne(queryWrapper);
                if (existingCustomerWithEmail != null) {
                    throw new IllegalArgumentException("Email is already in use by another customer.");
                }
            }

            // 更新信息
            if (name != null) {
                customer.setName(name);
            }
            if (email != null) {
                customer.setEmail(email);
            }
            if (phone != null) {
                customer.setPhone(phone);
            }

            return this.updateById(customer);
        } catch (Exception e) {
            // 记录异常日志，实际项目中可以使用日志框架（如 Log4j、SLF4J 等）
            System.err.println("Error updating customer information: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Customers getCustomerByIdOrEmail(String customerID, String email) {
        // 输入参数校验
        if (customerID == null && email == null) {
            throw new IllegalArgumentException("At least one parameter (customerID or email) must be provided.");
        }

        LambdaQueryWrapper<Customers> queryWrapper = new LambdaQueryWrapper<>();
        if (customerID != null) {
            queryWrapper.eq(Customers::getCustomerID, customerID);
        } else {
            queryWrapper.eq(Customers::getEmail, email);
        }

        try {
            return this.getOne(queryWrapper, false);
        } catch (Exception e) {
            // 记录异常日志，实际项目中可以使用日志框架（如 Log4j、SLF4J 等）
            System.err.println("Error getting customer by ID or email: " + e.getMessage());
            return null;
        }
    }

    // 校验邮箱格式
    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    // 校验电话号码格式
    private boolean isValidPhone(String phone) {
        return PHONE_PATTERN.matcher(phone).matches();
    }
}