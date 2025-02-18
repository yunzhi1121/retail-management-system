package com.yunzhi.retailmanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunzhi.retailmanagementsystem.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.model.domain.po.Users;
import com.yunzhi.retailmanagementsystem.response.ErrorCode;
import com.yunzhi.retailmanagementsystem.service.UsersService;
import com.yunzhi.retailmanagementsystem.Mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

/**
* @author Chloe
* @description 针对表【users】的数据库操作Service实现
* @createDate 2025-01-12 16:35:23
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{
    private static final String SALT = "your_salt_value"; // 自定义盐值

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public void registerUser(Users user) {
        // 检查用户名唯一性
        if (lambdaQuery().eq(Users::getUsername, user.getUsername()).one() != null) {
            throw new BusinessException(ErrorCode.USERNAME_EXISTS);
        }

        // 密码加密
        String encryptedPwd = DigestUtils.md5DigestAsHex((SALT + user.getPassword()).getBytes());
        user.setPassword(encryptedPwd);
        // 设置初始状态
        user.setApproved(false);
        user.setUserId(UUID.randomUUID().toString());
        user.setRole("普通用户");

        if (!save(user)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "用户注册失败");
        }
    }

    @Override
    public Users login(String username, String password) {
        Users user = lambdaQuery().eq(Users::getUsername, username).one();
        if (user == null) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        String inputEncrypted = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        if (!inputEncrypted.equals(user.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        if (!user.isApproved()) {
            throw new BusinessException(ErrorCode.USER_NOT_APPROVED);
        }

        return user;
    }

    @Override
    public void approveUser(String userId) {
        Users user = getExistUser(userId);
        user.setApproved(true);
        if (!updateById(user)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "审批状态更新失败");
        }
    }

    @Override
    public void assignRole(String userId, String role) {
        Users user = getExistUser(userId);
        user.setRole(role);
        if (!updateById(user)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "角色分配失败");
        }
    }

    @Override
    public Users getByUserId(String userId) {
        return getExistUser(userId);
    }

    @Override
    public Users getByUsername(String username) {
        Users user = lambdaQuery().eq(Users::getUsername, username).one();
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    private Users getExistUser(String userId) {
        Users user = getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    @Override
    public List<Users> getUnapprovedUsers() {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("approved", false);
        return usersMapper.selectList(queryWrapper);
    }
}




