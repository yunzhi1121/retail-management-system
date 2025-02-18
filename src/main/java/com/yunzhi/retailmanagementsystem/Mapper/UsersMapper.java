package com.yunzhi.retailmanagementsystem.Mapper;

import com.yunzhi.retailmanagementsystem.model.domain.po.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Chloe
* @description 针对表【users】的数据库操作Mapper
* @createDate 2025-01-12 16:35:23
* @Entity generator.domain.Users
*/
public interface UsersMapper extends BaseMapper<Users> {
    // 根据用户名查询用户
    //Users selectByUsername(String username);

    // 根据用户ID查询用户
    //Users selectByUserId(String userId);

    // 根据用户角色查询用户列表
    //List<Users> selectByRole(String role);
}




