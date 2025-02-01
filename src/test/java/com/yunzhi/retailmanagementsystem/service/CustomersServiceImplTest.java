package com.yunzhi.retailmanagementsystem.service;

import com.yunzhi.retailmanagementsystem.model.domain.Customers;
import com.yunzhi.retailmanagementsystem.service.CustomersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CustomersServiceImplTest {

    @Autowired
    private CustomersService customersService;

    @Test
    void createCustomerAndReturnId_Success() {
        String name = "John Doe";
        String email = UUID.randomUUID() + "@example.com";
        String phone = "12345678901";

        String customerId = customersService.createCustomerAndReturnId(name, email, phone);
        assertNotNull(customerId);
    }

    @Test
    void createCustomerAndReturnId_EmailExists() {
        String name = "John Doe";
        String email = UUID.randomUUID() + "@example.com";
        String phone = "12345678901";

        // 先创建一个客户
        String firstCustomerId = customersService.createCustomerAndReturnId(name, email, phone);
        assertNotNull(firstCustomerId);

        // 尝试使用相同的邮箱再次创建
        String secondCustomerId = customersService.createCustomerAndReturnId(name, email, phone);
        assertNull(secondCustomerId);
    }

    @Test
    void updateCustomerInfo_Success() {
        String name = "John Doe";
        String email = UUID.randomUUID() + "@example.com";
        String phone = "12345678901";

        // 创建一个客户
        String customerId = customersService.createCustomerAndReturnId(name, email, phone);
        assertNotNull(customerId);

        String newName = "Jane Doe";
        String newEmail = UUID.randomUUID() + "@example.com";
        String newPhone = "09876543210";

        boolean result = customersService.updateCustomerInfo(customerId, newName, newEmail, newPhone);
        assertTrue(result);

        Customers updatedCustomer = customersService.getCustomerByIdOrEmail(customerId, null);
        assertEquals(newName, updatedCustomer.getName());
        assertEquals(newEmail, updatedCustomer.getEmail());
        assertEquals(newPhone, updatedCustomer.getPhone());
    }

    @Test
    void updateCustomerInfo_CustomerNotFound() {
        String nonExistentCustomerId = UUID.randomUUID().toString();
        String name = "Jane Doe";
        String email = UUID.randomUUID() + "@example.com";
        String phone = "09876543210";

        boolean result = customersService.updateCustomerInfo(nonExistentCustomerId, name, email, phone);
        assertFalse(result);
    }

    @Test
    void getCustomerByIdOrEmail_ById() {
        String name = "John Doe";
        String email = UUID.randomUUID() + "@example.com";
        String phone = "12345678901";

        // 创建一个客户
        String customerId = customersService.createCustomerAndReturnId(name, email, phone);
        assertNotNull(customerId);

        Customers customer = customersService.getCustomerByIdOrEmail(customerId, null);
        assertNotNull(customer);
        assertEquals(customerId, customer.getCustomerID());
    }

    @Test
    void getCustomerByIdOrEmail_ByEmail() {
        String name = "John Doe";
        String email = UUID.randomUUID() + "@example.com";
        String phone = "12345678901";

        // 创建一个客户
        String customerId = customersService.createCustomerAndReturnId(name, email, phone);
        assertNotNull(customerId);

        Customers customer = customersService.getCustomerByIdOrEmail(null, email);
        assertNotNull(customer);
        assertEquals(email, customer.getEmail());
    }

    @Test
    void getCustomerByIdOrEmail_NeitherIdNorEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            customersService.getCustomerByIdOrEmail(null, null);
        });
    }
}