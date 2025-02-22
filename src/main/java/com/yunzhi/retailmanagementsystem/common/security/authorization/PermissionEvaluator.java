package com.yunzhi.retailmanagementsystem.common.security.authorization;

import com.yunzhi.retailmanagementsystem.common.constant.security.Permission;
import com.yunzhi.retailmanagementsystem.common.constant.security.Role;
import com.yunzhi.retailmanagementsystem.common.exception.BusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Optional;

public class PermissionEvaluator implements org.springframework.security.access.PermissionEvaluator {

    private final RolePermissions rolePermissions;

    public PermissionEvaluator(RolePermissions rolePermissions) {
        this.rolePermissions = rolePermissions;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object target, Object permission) {
        // 1. 获取用户角色
        Role userRole = extractUserRole(authentication);

        // 2. 转换权限标识
        Permission requiredPermission = convertToPermission(permission);

        // 3. 执行权限校验
        try {
            return rolePermissions.hasPermission(userRole, requiredPermission);
        } catch (BusinessException e) {
            // 处理角色不存在等业务异常
            return false;
        }
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId,
                                 String targetType,
                                 Object permission) {
        // 暂时不实现基于对象的权限验证
        return false;
    }

    private Role extractUserRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(this::convertToRole)
                .orElseThrow(() -> new SecurityException("用户未分配角色"));
    }

    private Role convertToRole(String roleName) {
        try {
            return Role.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            throw new SecurityException("无效的用户角色: " + roleName);
        }
    }

    private Permission convertToPermission(Object permission) {
        if (!(permission instanceof String permissionCode)) {
            throw new SecurityException("权限标识类型不合法");
        }

        return Optional.ofNullable(Permission.parse(permissionCode))
                .orElseThrow(() -> new SecurityException("无效的权限标识: " + permissionCode));
    }
}