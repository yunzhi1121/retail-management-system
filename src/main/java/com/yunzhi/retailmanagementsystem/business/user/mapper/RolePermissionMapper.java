package com.yunzhi.retailmanagementsystem.business.user.mapper;

import com.yunzhi.retailmanagementsystem.business.user.model.po.RolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Chloe
* @description 针对表【role_permission】的数据库操作Mapper
* @createDate 2025-02-21 10:50:48
* @Entity com.yunzhi.retailmanagementsystem.business.user.model.po.RolePermission
*/
public interface RolePermissionMapper extends BaseMapper<RolePermission> {
    /**
     * 根据角色名称查询权限标识列表
     * @param roleId 角色名称（枚举值）
     * @return 权限标识列表
     */
    @Select("SELECT PermissionID FROM role_permission WHERE RoleID = #{roleId}")
    List<String> findPermissionsByRole(String roleId);

    /**
     * 根据权限代码查询角色列表
     * @param permissionId 权限代码
     * @return 角色名称列表
     */
//    @Select("SELECT RoleID FROM role_permission WHERE PermissionID = #{permissionId}")
    List<String> findRolesByPermission(String permissionId);

    /**
     * 删除角色的所有权限
     * @param role 角色名称（枚举值）
     */
    void deleteByRole(String role);

    /**
     * 为角色分配权限
     * @param role 角色名称（枚举值）
     * @param permission 权限标识（枚举值）
     */
    void insertRolePermission(String role, String permission);

}




