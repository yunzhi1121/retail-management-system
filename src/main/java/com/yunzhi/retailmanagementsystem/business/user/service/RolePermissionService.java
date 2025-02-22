package com.yunzhi.retailmanagementsystem.business.user.service;

import com.yunzhi.retailmanagementsystem.business.user.model.po.RolePermission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunzhi.retailmanagementsystem.common.constant.security.Role;

import java.util.List;

/**
* @author Chloe
* @description 针对表【role_permission】的数据库操作Service
* @createDate 2025-02-21 10:50:49
*/
public interface RolePermissionService extends IService<RolePermission> {
    void assignPermissions(Role role, List<String> permissionCodes);
}
