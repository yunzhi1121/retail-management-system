package com.yunzhi.retailmanagementsystem.business.customer.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerVO {
    private String customerId;
    private String name;
    private String email;
    private String phone;
}