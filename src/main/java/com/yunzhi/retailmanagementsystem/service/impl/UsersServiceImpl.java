package com.yunzhi.retailmanagementsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunzhi.retailmanagementsystem.model.domain.Users;
import com.yunzhi.retailmanagementsystem.service.UsersService;
import com.yunzhi.retailmanagementsystem.Mapper.UsersMapper;
import org.springframework.stereotype.Service;

/**
* @author Chloe
* @description 针对表【users】的数据库操作Service实现
* @createDate 2025-01-12 16:35:23
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

}




