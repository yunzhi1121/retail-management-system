package com.yunzhi.retailmanagementsystem.common.security.authorization;

import com.yunzhi.retailmanagementsystem.common.constant.security.Permission;
import com.yunzhi.retailmanagementsystem.common.constant.security.Role;
import com.yunzhi.retailmanagementsystem.common.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.common.model.response.ErrorCode;
import com.yunzhi.retailmanagementsystem.common.security.authentication.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class PermissionInterceptor implements HandlerInterceptor {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod handlerMethod) {
            RequiresPermission requiresPermission = handlerMethod.getMethodAnnotation(RequiresPermission.class);
            if (requiresPermission != null) {
                // 获取当前用户的角色
                String userRole = getCurrentUserRole(request); // 需要实现从上下文（如JWT Token）中获取用户角色
                Role role = Role.valueOf(userRole);

                // 获取注解中的权限标识
                Permission requiredPermission = requiresPermission.value();

                // 检查权限
                permissionService.checkPermission(role, requiredPermission);
            }
        }
        return true;
    }

    private String getCurrentUserRole(HttpServletRequest request) {
        // 从HTTP请求中解析令牌
        String token = resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            try {
                // 根据令牌获取认证信息
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                String roleWithPrefix = auth.getAuthorities().iterator().next().getAuthority();
                // 移除 ROLE_ 前缀
                String role = roleWithPrefix.replaceFirst("ROLE_", "").trim();
                return role;
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.TOKEN_INVALID, "认证信息解析失败");
            }
        }
        return null; // 如果没有提供令牌或令牌无效，则返回null
    }

    /**
     * 从HTTP请求中解析令牌
     *
     * @param req HTTP请求对象
     * @return 解析出的令牌字符串，如果没有提供则返回null
     */
    private String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}