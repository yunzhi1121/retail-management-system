package com.yunzhi.retailmanagementsystem.service;

import com.yunzhi.retailmanagementsystem.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.model.domain.po.Users;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Chloe
* @description 针对表【users】的数据库操作Service
* @createDate 2025-01-12 16:35:23
*/
public interface UsersService extends IService<Users> {
    void registerUser(Users user) throws BusinessException;

    Users login(String username, String password) throws BusinessException;

    void approveUser(String userId) throws BusinessException;

    void assignRole(String userId, String role) throws BusinessException;

    Users getByUserId(String userId) throws BusinessException;

    Users getByUsername(String username) throws BusinessException;

    List<Users> getUnapprovedUsers();
}