package com.yunzhi.retailmanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunzhi.retailmanagementsystem.model.domain.Users;
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
    public boolean registerUser(Users user) {
        // 检查用户名是否已存在
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        Users existingUser = usersMapper.selectOne(queryWrapper);
        if (existingUser!= null) {
            return false;
        }

        // 对密码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + user.getPassword()).getBytes());
        user.setPassword(encryptPassword);

        // 设置用户初始审批状态为未审批（false）
        user.setApproved(false);

        // 设置role初始值为普通用户角色
        user.setRole("普通用户");

        // 手动生成userID
        user.setUserID(UUID.randomUUID().toString());

        // 插入新用户
        return usersMapper.insert(user) > 0;
    }

    @Override
    public Users login(String username, String password) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Users user = usersMapper.selectOne(queryWrapper);
        if (user!= null) {
            // 对输入密码进行加密后与数据库中存储的加密密码比对
            String inputEncryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
            // 检查用户是否已审批
            if (inputEncryptPassword.equals(user.getPassword())) {
                return user;
            }
        }
        return null;
    }


    @Override
    public boolean assignRole(String userId, String role) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userID", userId);
        Users user = usersMapper.selectOne(queryWrapper);
        if (user!= null) {
            user.setRole(role);
            return usersMapper.updateById(user) > 0;
        }
        return false;
    }

    @Override
    public boolean approveUser(String userId) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userID", userId);
        Users user = usersMapper.selectOne(queryWrapper);
        if (user!= null) {
            user.setApproved(true);
            return usersMapper.updateById(user) > 0;
        }
        return false;
    }


    @Override
    public boolean checkPermission(String userId, String action) {
        return false;
    }

    @Override
    public Users getByUserId(String userId) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userID", userId);
        return usersMapper.selectOne(queryWrapper);
    }

    @Override
    public Users getByUsername(String username) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return usersMapper.selectOne(queryWrapper);
    }

    @Override
    public List<Users> getUnapprovedUsers() {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("approved", false);
        return usersMapper.selectList(queryWrapper);
    }
}




