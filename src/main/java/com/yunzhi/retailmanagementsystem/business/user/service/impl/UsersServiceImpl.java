package com.yunzhi.retailmanagementsystem.business.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunzhi.retailmanagementsystem.business.user.mapper.UsersMapper;
import com.yunzhi.retailmanagementsystem.business.user.model.dto.UserCreateDTO;
import com.yunzhi.retailmanagementsystem.business.user.model.po.Users;
import com.yunzhi.retailmanagementsystem.business.user.model.vo.UserVO;
import com.yunzhi.retailmanagementsystem.business.user.service.UsersService;
import com.yunzhi.retailmanagementsystem.common.constant.security.Role;
import com.yunzhi.retailmanagementsystem.common.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.common.model.response.ErrorCode;
import com.yunzhi.retailmanagementsystem.common.utils.coverter.VOConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

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
    @Transactional
    public UserVO createUser(UserCreateDTO createDTO) {
        // 校验用户名唯一性
        if (lambdaQuery().eq(Users::getUsername, createDTO.getUsername()).one() != null) {
            throw new BusinessException(ErrorCode.DUPLICATE_DATA, "用户名已存在");
        }
        if(createDTO.getRole() == null || createDTO.getRole().isEmpty() ||createDTO.getUsername() == null || createDTO.getUsername().isEmpty()){
            throw new BusinessException(ErrorCode.PARAM_ERROR, "角色不能为空");
        }

        // 角色合法性校验
        boolean isValid = false;
        for (Role role : Role.values()) {
            if (role.toString().equals(createDTO.getRole())) {
                isValid = true;
                break;
            }
        }
        if (!isValid) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "非法的角色类型");
        }

        // 创建用户实体
        Users user = new Users();
        BeanUtils.copyProperties(createDTO, user);

        // 设置系统生成属性
        user.setUserId(UUID.randomUUID().toString());
        user.setPassword(initDefaultPassword()); // 初始化默认密码

        try {
            save(user);
        } catch (Exception e) {
            log.error("Error saving user: ", e);
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "用户创建失败");
        }

        return VOConverter.convertToVO(user);
    }

    // 初始化默认密码（可配置化）
    private String initDefaultPassword() {
        String defaultPassword = "Ds123456"; // 建议从配置中心读取
        return DigestUtils.md5DigestAsHex((SALT + defaultPassword).getBytes());
    }


    @Override
    public Users login(String username, String password) {
        Users user = lambdaQuery().eq(Users::getUsername, username).one();
        if (user == null) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIAL, "用户不存在");
        }

        String inputEncrypted = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        if (!inputEncrypted.equals(user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIAL, "密码错误");
        }

        return user;
    }

    @Override
    public void updateUsername(String userId, String newUsername) throws BusinessException {
        Users user = getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "用户不存在");
        }
        if(newUsername == null || newUsername.isEmpty()){
            throw new BusinessException(ErrorCode.PARAM_ERROR, "新用户名不能为空");
        }
        user.setUsername(newUsername);
        updateById(user);
    }

    @Override
    public void updatePassword(String userId, String oldPassword, String newPassword) throws BusinessException {
        Users user = getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "用户不存在");
        }
        String inputEncrypted = DigestUtils.md5DigestAsHex((SALT + oldPassword).getBytes());
        if (!user.getPassword().equals(inputEncrypted)) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIAL, "旧密码不正确");
        }
        if(newPassword == null || newPassword.isEmpty()){
            throw new BusinessException(ErrorCode.PARAM_ERROR, "新密码不能为空");
        }
        user.setPassword(newPassword);
        updateById(user);
    }

    @Override
    public void assignRole(String userId, String role) {
        Users user = getExistUser(userId);
        if (role == null || role.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "角色不能为空");
        }
        // 角色合法性校验（根据实际角色配置调整）
        boolean isValid = false;
        for (Role roles : Role.values()) {
            if (roles.toString().equals(role)) {
                isValid = true;
                break;
            }
        }
        if (!isValid) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "非法的角色类型");
        }
        user.setRole(role);
        try {
            updateById(user);
        } catch (Exception e) {
            log.error("Error saving user: ", e);
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "用户创建失败");

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
}




