package com.yunzhi.retailmanagementsystem.model.domain.vo;

public record UserVO(
        String userId,
        String username,
        String role,
        boolean approved
) {}