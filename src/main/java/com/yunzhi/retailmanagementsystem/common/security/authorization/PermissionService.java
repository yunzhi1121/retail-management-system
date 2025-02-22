package com.yunzhi.retailmanagementsystem.common.security.authorization;

import com.yunzhi.retailmanagementsystem.common.constant.security.Permission;
import com.yunzhi.retailmanagementsystem.common.constant.security.Role;
import com.yunzhi.retailmanagementsystem.common.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.common.model.response.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {
    @Autowired
    private RolePermissions rolePermissions;

    /**
     * 检查权限（推荐使用枚举版本）
     */
    public void checkPermission(Role role, Permission permission) {
        if (!rolePermissions.hasPermission(role, permission)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
    }

    /**
     * 带资源所有者的权限检查
     */
    public void checkPermissionWithOwner(Role role, Permission permission, String resourceOwnerId, String currentUserId) {
        // 如果是资源所有者则允许访问
        if (resourceOwnerId.equals(currentUserId)) {
            return;
        }
        checkPermission(role, permission);
    }
}