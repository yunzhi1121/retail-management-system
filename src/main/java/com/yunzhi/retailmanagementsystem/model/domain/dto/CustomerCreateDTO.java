package com.yunzhi.retailmanagementsystem.model.domain.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public record CustomerCreateDTO(
        @NotBlank(message = "客户姓名不能为空")
        String name,
        @NotBlank(message = "邮箱不能为空")
        @Email
        String email,
        @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
        String phone
) {}