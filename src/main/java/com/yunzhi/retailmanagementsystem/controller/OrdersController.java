package com.yunzhi.retailmanagementsystem.controller;

import com.yunzhi.retailmanagementsystem.model.domain.Goods;
import com.yunzhi.retailmanagementsystem.model.domain.OrderGood;
import com.yunzhi.retailmanagementsystem.model.domain.Orders;
import com.yunzhi.retailmanagementsystem.service.GoodsService;
import com.yunzhi.retailmanagementsystem.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;
    @Autowired
    private GoodsService goodsService;

    // 1 创建订单
    @PostMapping
    public ResponseEntity<Object> createOrder(@RequestBody Map<String, Object> requestBody) {
        try {
            String customerID = (String) requestBody.get("customerID");
            List<Map<String, Object>> items = (List<Map<String, Object>>) requestBody.get("items");
            String paymentMethod = (String) requestBody.get("paymentMethod");
            String deliveryMethod = (String) requestBody.get("deliveryMethod");
            String remarks = (String) requestBody.get("remarks");

            Map<String, Integer> goodQuantities = new HashMap<>();
            for (Map<String, Object> item : items) {
                String goodID = (String) item.get("goodID");
                int quantity = (int) item.get("quantity");
                goodQuantities.put(goodID, quantity);
            }

            Orders order = ordersService.createOrder(customerID, goodQuantities, paymentMethod, deliveryMethod, remarks);

            Map<String, Object> response = new HashMap<>();
            response.put("orderID", order.getOrderID());
            response.put("message", "Order created successfully.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errorCode", "400");
            errorResponse.put("errorMessage", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errorCode", "400");
            errorResponse.put("errorMessage", "Invalid input data.");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // 更新订单信息
    @PutMapping("/{orderID}/update/")
    public ResponseEntity<Object> updateOrder(@PathVariable String orderID, @RequestBody Map<String, Object> requestBody) {
        try {
            // 从请求体中获取新的商品列表
            List<Map<String, Object>> items = (List<Map<String, Object>>) requestBody.get("items");
            Map<String, Integer> newGoodQuantities = new HashMap<>();
            if (items != null) {
                for (Map<String, Object> item : items) {
                    String goodID = (String) item.get("goodID");
                    int quantity = ((Number) item.get("quantity")).intValue();
                    newGoodQuantities.put(goodID, quantity);
                }
            }

            // 从请求体中获取新的支付方式
            String newPaymentMethod = (String) requestBody.get("paymentMethod");
            String deliveryMethod = (String) requestBody.get("deliveryMethod");
            String remarks = (String) requestBody.get("remarks");

            // 调用服务层的 updateOrder 方法更新订单信息
            boolean success = ordersService.updateOrder(orderID, newGoodQuantities, newPaymentMethod, deliveryMethod, remarks);

            if (success) {
                Map<String, Object> response = new HashMap<>();
                response.put("orderID", orderID);
                response.put("message", "Order information updated successfully.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("errorCode", "400");
                errorResponse.put("errorMessage", "Failed to update order information. Order may be in an invalid status or insufficient stock.");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errorCode", "400");
            errorResponse.put("errorMessage", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errorCode", "500");
            errorResponse.put("errorMessage", "An unexpected error occurred.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 取消订单
    @DeleteMapping("/{orderID}/cancel")
    public ResponseEntity<Object> cancelOrder(@PathVariable String orderID) {
        try {
            boolean success = ordersService.cancelOrder(orderID);
            if (success) {
                Map<String, Object> response = new HashMap<>();
                response.put("orderID", orderID);
                response.put("message", "Order canceled successfully.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("errorCode", "400");
                errorResponse.put("errorMessage", "Failed to cancel order.");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errorCode", "400");
            errorResponse.put("errorMessage", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errorCode", "500");
            errorResponse.put("errorMessage", "An unexpected error occurred.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 查询订单
    @GetMapping("/{orderID}")
    public ResponseEntity<Object> getOrder(@PathVariable String orderID) {
        try {
            Orders order = ordersService.getOrderById(orderID);
            if (order == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("errorCode", "404");
                errorResponse.put("errorMessage", "Order not found.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("orderID", order.getOrderID());
            response.put("customerID", order.getCustomerID());

            List<Map<String, Object>> items = new ArrayList<>();
            List<OrderGood> orderGoods = ordersService.getOrderGoodsByOrderId(orderID);
            for (OrderGood orderGood : orderGoods) {
                Goods good = goodsService.getGoodsById(orderGood.getGoodID());
                Map<String, Object> item = new HashMap<>();
                item.put("goodID", good.getGoodID());
                item.put("name", good.getName());
                item.put("quantity", orderGood.getQuantity());
                item.put("price", good.getPrice());
                items.add(item);
            }
            response.put("items", items);
            response.put("totalAmount", order.getTotalAmount());
            response.put("paymentMethod", order.getPaymentMethod());
            response.put("deliveryMethod", order.getDeliveryMethod());
            response.put("status", order.getStatus());
            response.put("createdAt", order.getOrderDate());
            response.put("updatedAt", new Date());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errorCode", "500");
            errorResponse.put("errorMessage", "An unexpected error occurred.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 查询用户所有订单
    @GetMapping("/customer/{customerID}")
    public ResponseEntity<Object> getOrdersByCustomer(@PathVariable String customerID) {
        try {
            List<Orders> orders = ordersService.getOrdersByCustomer(customerID);
            List<Map<String, Object>> responseList = new ArrayList<>();
            for (Orders order : orders) {
                Map<String, Object> response = new HashMap<>();
                response.put("orderID", order.getOrderID());
                response.put("totalAmount", order.getTotalAmount());
                response.put("status", order.getStatus());
                response.put("createdAt", order.getOrderDate());
                responseList.add(response);
            }
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errorCode", "400");
            errorResponse.put("errorMessage", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errorCode", "500");
            errorResponse.put("errorMessage", "An unexpected error occurred.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}