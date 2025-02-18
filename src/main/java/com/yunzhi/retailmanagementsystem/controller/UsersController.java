package com.yunzhi.retailmanagementsystem.controller;

import com.yunzhi.retailmanagementsystem.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.model.domain.po.Users;
import com.yunzhi.retailmanagementsystem.model.domain.dto.LoginDTO;
import com.yunzhi.retailmanagementsystem.model.domain.dto.RoleAssignDTO;
import com.yunzhi.retailmanagementsystem.model.domain.vo.UserLoginResponseVO;
import com.yunzhi.retailmanagementsystem.model.domain.vo.UserRegisterResponseVO;
import com.yunzhi.retailmanagementsystem.model.domain.vo.UserUnapprovedVO;
import com.yunzhi.retailmanagementsystem.response.BaseResponse;
import com.yunzhi.retailmanagementsystem.response.ErrorCode;
import com.yunzhi.retailmanagementsystem.response.ResultUtils;
import com.yunzhi.retailmanagementsystem.service.UsersService;
import com.yunzhi.retailmanagementsystem.utils.RSAKeyUtil;
import com.yunzhi.retailmanagementsystem.utils.VOConverter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.PrivateKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Validated
public class UsersController {

    @Autowired
    private UsersService usersService;
    private static final long EXPIRATION_TIME = 3600000; // 1小时，毫秒

    //━━━━━━━━━━━━━━ 端点实现 ━━━━━━━━━━━━━━
    @PostMapping("/register")
    public BaseResponse<UserRegisterResponseVO> registerUser(@RequestBody @Valid Users user) {
        if (user.getUsername() == null || user.getUsername().isEmpty() || user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        usersService.registerUser(user);
        Users registered = usersService.getByUsername(user.getUsername());
        return ResultUtils.success(new UserRegisterResponseVO(
                registered.getUserId(),
                "注册成功，等待审批"
        ), null);
    }

    @PostMapping("/login")
    public BaseResponse<UserLoginResponseVO> login(@RequestBody @Valid LoginDTO dto) {
        Users user = usersService.login(dto.username(), dto.password());
        String token = generateToken(user);
        return ResultUtils.success(new UserLoginResponseVO(
                token,
                user.getRole(),
                EXPIRATION_TIME / 1000
        ), "登录成功");
    }

    @GetMapping("/unapproved")
    public BaseResponse<List<UserUnapprovedVO>> getUnapprovedUsers() {
        List<Users> unapprovedUsers = usersService.getUnapprovedUsers();
        List<UserUnapprovedVO> vos = unapprovedUsers.stream()
                .map(VOConverter::convertToUserUnapprovedVO)
                .collect(Collectors.toList());
        return ResultUtils.success(vos, null);
    }

    @PutMapping("/{userId}/approve")
    public BaseResponse<Void> approveUser(@PathVariable String userId) {
        usersService.approveUser(userId);
        return ResultUtils.success(null);
    }

    @PutMapping("/{userId}/role")
    public BaseResponse<Void> assignRole(
            @PathVariable String userId,
            @RequestBody @Valid RoleAssignDTO dto
    ) {
        usersService.assignRole(userId, dto.role());
        return ResultUtils.success(null);
    }

    //━━━━━━━━━━━━━━ 工具方法 ━━━━━━━━━━━━━━
    private String generateToken(Users user) {
        try {
            PrivateKey privateKey = RSAKeyUtil.getPrivateKeyFromEnv("PRIVATE_KEY");
            return Jwts.builder()
                    .setSubject(user.getUserId())
                    .claim("role", user.getRole()) // 更规范的claims设置方式
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(privateKey, SignatureAlgorithm.RS256)
                    .compact();
        } catch (io.jsonwebtoken.security.SecurityException ex) {
            throw new BusinessException(ErrorCode.TOKEN_GENERATE_ERROR);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }
}