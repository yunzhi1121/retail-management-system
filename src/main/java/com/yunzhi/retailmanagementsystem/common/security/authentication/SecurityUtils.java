package com.yunzhi.retailmanagementsystem.common.security.authentication;

import com.yunzhi.retailmanagementsystem.common.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.common.model.response.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

// SecurityUtils.java
@Component
public class SecurityUtils {
    /**
     * 从安全上下文中获取当前用户ID
     * @return 用户ID字符串
     * @throws BusinessException 如果未认证或信息不完整
     */
    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "用户未认证");
        }

        // 直接获取主体中的用户ID
        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            return (String) principal;
        }
        throw new BusinessException(ErrorCode.INVALID_CREDENTIAL, "用户ID解析失败");
    }
}