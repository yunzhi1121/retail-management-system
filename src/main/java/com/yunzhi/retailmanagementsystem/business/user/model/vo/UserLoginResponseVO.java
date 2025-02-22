package com.yunzhi.retailmanagementsystem.business.user.model.vo;

public record UserLoginResponseVO(
        String token,
        String role,
        long expiresIn
) {}