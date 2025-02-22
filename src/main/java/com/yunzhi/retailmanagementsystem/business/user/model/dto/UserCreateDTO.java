package com.yunzhi.retailmanagementsystem.business.user.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

// UserCreateDTO.java
@Data
public class UserCreateDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @NotBlank(message = "角色不能为空")
    private String role;
}