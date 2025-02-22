package com.yunzhi.retailmanagementsystem.business.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunzhi.retailmanagementsystem.business.user.model.po.RolePermission;
import com.yunzhi.retailmanagementsystem.business.user.service.RolePermissionService;
import com.yunzhi.retailmanagementsystem.business.user.mapper.RolePermissionMapper;
import com.yunzhi.retailmanagementsystem.common.constant.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author Chloe
* @description 针对表【role_permission】的数据库操作Service实现
* @createDate 2025-02-21 10:50:49
*/
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission>
    implements RolePermissionService{

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    /**
     * 为角色分配权限
     * @param role 角色名称（枚举值）
     * @param permissionCodes 权限标识列表（枚举值）
     */
    @Transactional
    public void assignPermissions(Role role, List<String> permissionCodes) {
        // 清空角色的旧权限
        rolePermissionMapper.deleteByRole(role.name());

        // 为角色分配新权限
        for (String code : permissionCodes) {
            rolePermissionMapper.insertRolePermission(role.name(), code);
        }
    }

}




