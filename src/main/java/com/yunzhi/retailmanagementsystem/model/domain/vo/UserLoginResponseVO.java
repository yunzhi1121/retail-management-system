package com.yunzhi.retailmanagementsystem.model.domain.vo;

public record UserLoginResponseVO(
        String token,
        String role,
        long expiresIn
) {}