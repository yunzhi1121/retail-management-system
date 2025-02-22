package com.yunzhi.retailmanagementsystem.common.security.authorization;

import com.yunzhi.retailmanagementsystem.business.user.mapper.RolePermissionMapper;
import com.yunzhi.retailmanagementsystem.common.constant.security.Permission;
import com.yunzhi.retailmanagementsystem.common.constant.security.Role;
import com.yunzhi.retailmanagementsystem.common.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.common.model.response.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RolePermissions {

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    /**
     * 获取指定角色的权限列表
     * @param role 角色名称（枚举值）
     * @return 权限标识列表
     */
    public List<String> getPermissionsForRole(Role role) {
        List<String> permissions = rolePermissionMapper.findPermissionsByRole(role.name());
        if (permissions.isEmpty()) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "角色不存在: " + role.name());
        }
        return permissions;
    }

    /**
     * 检查角色是否拥有指定权限
     * @param role 角色名称（枚举值）
     * @param permission 权限标识（枚举值）
     * @return 是否拥有权限
     */
    public boolean hasPermission(Role role, Permission permission) {
//        List<String> permissions = getPermissionsForRole(role);
//
//        // 管理员拥有所有权限
//        if (permissions.contains("*")) {
//            return true;
//        }
//
//        return permissions.contains(permission.getCode());
        //管理员拥有所有权限
        if (role == Role.ADMIN) {
            return true;
        }
        List<String> roles = rolePermissionMapper.findRolesByPermission(permission.toString());
        return roles.contains(role.name());
    }
}