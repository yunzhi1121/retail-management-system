package com.yunzhi.retailmanagementsystem.business.user.service;

import com.yunzhi.retailmanagementsystem.business.user.model.dto.UserCreateDTO;
import com.yunzhi.retailmanagementsystem.business.user.model.vo.UserVO;
import com.yunzhi.retailmanagementsystem.common.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.business.user.model.po.Users;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Chloe
* @description 针对表【users】的数据库操作Service
* @createDate 2025-01-12 16:35:23
*/
public interface UsersService extends IService<Users> {
    UserVO createUser(UserCreateDTO createDTO) throws BusinessException;

    Users login(String username, String password) throws BusinessException;

    void updateUsername(String userId, String newUsername) throws BusinessException;

    void updatePassword(String userId, String oldPassword, String newPassword) throws BusinessException;

    void assignRole(String userId, String role) throws BusinessException;

    Users getByUserId(String userId) throws BusinessException;

    Users getByUsername(String username) throws BusinessException;
}