package com.yunzhi.retailmanagementsystem.business.customer.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
@Data
public class CustomerCreateDTO {

        @NotBlank(message = "客户姓名不能为空")
        private String name;

        @NotBlank(message = "邮箱不能为空")
        @Email
        private String email;

        @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
        private String phone;
}