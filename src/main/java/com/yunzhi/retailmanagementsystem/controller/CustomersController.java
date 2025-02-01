package com.yunzhi.retailmanagementsystem.controller;

import com.yunzhi.retailmanagementsystem.model.domain.Customers;
import com.yunzhi.retailmanagementsystem.service.CustomersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 客户管理控制器
 */
@RestController
@RequestMapping("/customers")
public class CustomersController {

    @Autowired
    private CustomersService customersService;

    /**
     * 添加客户
     * 方法: POST
     * URL: /customers
     * 请求体: {"name": "string", "email": "string", "phone": "string"}
     * 成功响应: 201 Created
     * 失败响应: 400 Bad Request / 500 Internal Server Error
     */
    @PostMapping("/addcustomers")
    public ResponseEntity<String> createCustomer(@RequestBody Customers customer) {
        try {
            // 调用修改后的 createCustomerAndReturnId 方法
            String customerId = customersService.createCustomerAndReturnId(customer.getName(), customer.getEmail(), customer.getPhone());
            if (customerId != null) {
                // 成功创建客户，返回 201 状态码和包含客户 ID 的消息
                return new ResponseEntity<>("Customer created successfully with ID: " + customerId, HttpStatus.CREATED);
            } else {
                // 邮箱已存在，返回 400 状态码和相应消息
                return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalArgumentException e) {
            // 输入参数不合法，返回 400 状态码和异常消息
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // 发生内部服务器错误，返回 500 状态码和相应消息
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 更新客户信息
     * 方法: PUT
     * URL: /customers/{customerID}
     * 请求体: {"name": "string", "email": "string", "phone": "string"}
     * 成功响应: 200 OK
     * 失败响应: 400 Bad Request / 404 Not Found / 500 Internal Server Error
     */
    @PutMapping("/{customerID}")
    public ResponseEntity<String> updateCustomerInfo(@PathVariable String customerID, @RequestBody Customers customer) {
        try {
            boolean result = customersService.updateCustomerInfo(customerID, customer.getName(), customer.getEmail(), customer.getPhone());
            if (result) {
                return new ResponseEntity<>("Customer information updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据客户 ID 或邮箱获取客户信息
     * 方法: GET
     * URL: /customers?customerID=xxx&email=xxx
     * 成功响应: 200 OK
     * 失败响应: 400 Bad Request / 404 Not Found / 500 Internal Server Error
     */
    @GetMapping
    public ResponseEntity<Customers> getCustomerByIdOrEmail(@RequestParam(required = false) String customerID, @RequestParam(required = false) String email) {
        try {
            Customers customer = customersService.getCustomerByIdOrEmail(customerID, email);
            if (customer != null) {
                return new ResponseEntity<>(customer, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}