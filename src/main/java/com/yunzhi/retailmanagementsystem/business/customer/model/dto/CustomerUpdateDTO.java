package com.yunzhi.retailmanagementsystem.business.customer.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CustomerUpdateDTO {

        private String name;

        private String email;

        private String phone;
}