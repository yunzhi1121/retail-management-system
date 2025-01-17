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
    // 用户注册
    boolean registerUser(Users user);

    // 用户登录
    Users login(String username, String password);

    boolean approveUser(String userId);

    // 分配用户角色
    boolean assignRole(String userId, String role);

    // 检查用户权限
    boolean checkPermission(String userId, String action);

    Users getByUserId(String userId);

    Users getByUsername(String username);

    List<Users> getUnapprovedUsers();
}
