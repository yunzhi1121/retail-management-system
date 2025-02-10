package com.yunzhi.retailmanagementsystem.service;

import com.yunzhi.retailmanagementsystem.model.domain.Users;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Chloe
* @description 针对表【users】的数据库操作Service
* @createDate 2025-01-12 16:35:23
*/
public interface UsersService extends IService<Users> {
    /**
     * 注册新用户
     *
     * @param user 待注册的用户对象
     * @return 如果用户成功注册，返回true；否则返回false
     */
    boolean registerUser(Users user);

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 如果登录成功，返回用户对象；否则返回null
     */
    Users login(String username, String password);

    /**
     * 审批用户
     *
     * @param userId 待审批的用户ID
     * @return 如果用户成功被审批，返回true；否则返回false
     */
    boolean approveUser(String userId);

    /**
     * 分配角色给用户
     *
     * @param userId 用户ID
     * @param role 角色名称
     * @return 如果角色成功分配给用户，返回true；否则返回false
     */
    boolean assignRole(String userId, String role);

    /**
     * 检查用户是否有指定权限
     *
     * @param userId 用户ID
     * @param action 权限操作名称
     * @return 如果用户有指定权限，返回true；否则返回false
     */
    boolean checkPermission(String userId, String action);

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId 用户ID
     * @return 如果找到用户，返回用户对象；否则返回null
     */
    Users getByUserId(String userId);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 如果找到用户，返回用户对象；否则返回null
     */
    Users getByUsername(String username);

    /**
     * 获取所有未审批的用户列表
     *
     * @return 未审批的用户列表
     */
    List<Users> getUnapprovedUsers();
}
