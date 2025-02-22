package com.yunzhi.retailmanagementsystem.business.user.controller;

import com.yunzhi.retailmanagementsystem.business.user.model.dto.*;
import com.yunzhi.retailmanagementsystem.business.user.model.po.Users;
import com.yunzhi.retailmanagementsystem.business.user.model.vo.UserLoginResponseVO;
import com.yunzhi.retailmanagementsystem.business.user.model.vo.UserVO;
import com.yunzhi.retailmanagementsystem.business.user.service.UsersService;
import com.yunzhi.retailmanagementsystem.common.constant.security.Permission;
import com.yunzhi.retailmanagementsystem.common.model.response.BaseResponse;
import com.yunzhi.retailmanagementsystem.common.model.response.ResultUtils;
import com.yunzhi.retailmanagementsystem.common.security.authentication.JwtTokenProvider;
import com.yunzhi.retailmanagementsystem.common.security.authorization.RequiresPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@Validated
public class UsersController {

    @Autowired
    private UsersService usersService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static final long EXPIRATION_TIME = 3600000; // 1小时，毫秒

    //━━━━━━━━━━━━━━ 端点实现 ━━━━━━━━━━━━━━
    @PostMapping("/add")
    @RequiresPermission(Permission.USERS_CREATE)
    public BaseResponse<UserVO> createUser(@RequestBody @Valid UserCreateDTO createDTO) {
        return ResultUtils.success(usersService.createUser(createDTO), "用户创建成功");
    }

    @PostMapping("/login")
    @RequiresPermission(Permission.USERS_READ)
    public BaseResponse<UserLoginResponseVO> login(@RequestBody @Valid UsersLoginDTO dto) {
        Users user = usersService.login(dto.username(), dto.password());
        String token = jwtTokenProvider.generateToken(user.getUserId(), user.getRole());
        return ResultUtils.success(new UserLoginResponseVO(
                token,
                user.getRole(),
                EXPIRATION_TIME / 1000
        ), "登录成功");
    }

    // 修改用户名
    @PutMapping("/{userId}/username")
    @RequiresPermission(Permission.USERS_UPDATE)
    public BaseResponse<Void> updateUsername(
            @PathVariable String userId,
            @RequestBody @Valid UsersUpdateUsernameDTO dto
    ) {
        usersService.updateUsername(userId, dto.getNewUsername());
        return ResultUtils.success("用户名修改成功");
    }

    // 修改密码
    @PutMapping("/{userId}/password")
    @RequiresPermission(Permission.USERS_UPDATE)
    public BaseResponse<Void> updatePassword(
            @PathVariable String userId,
            @RequestBody @Valid UsersUpdatePasswordDTO dto
    ) {
        usersService.updatePassword(userId,dto.getOldPassword(), dto.getNewPassword());
        return ResultUtils.success("密码修改成功");
    }


    @PutMapping("/{userId}/role")
    @RequiresPermission(Permission.USERS_UPDATE)
    public BaseResponse<Void> assignRole(
            @PathVariable String userId,
            @RequestBody @Valid UsersRoleAssignDTO dto
    ) {
        usersService.assignRole(userId, dto.role());
        return ResultUtils.success("角色分配成功");
    }


}