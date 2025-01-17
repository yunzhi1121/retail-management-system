package com.yunzhi.retailmanagementsystem.service;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yunzhi.retailmanagementsystem.Mapper.UsersMapper;
import com.yunzhi.retailmanagementsystem.model.domain.Users;
import com.yunzhi.retailmanagementsystem.service.UsersService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@SpringBootTest
public class UsersRegisterTest {

    @Autowired
    private UsersService userService;

    @MockBean
    private UsersMapper usersMapper;

    @Test
    public void testRegisterUserSuccess() {
        // 模拟一个用户对象
        Users newUser = new Users();
        newUser.setUsername("newUser");
        newUser.setPassword("password");

        // 模拟 selectOne 方法返回 null，表示用户名不存在
        when(usersMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);
        // 模拟 insert 方法返回 1，表示插入成功
        when(usersMapper.insert(any(Users.class))).thenReturn(1);

        // 调用 registerUser 方法进行注册
        boolean result = userService.registerUser(newUser);

        // 验证结果为 true，表示注册成功
        assertTrue(result);
    }

    @Test
    public void testRegisterUserFailure() {
        // 模拟一个用户对象
        Users existingUser = new Users();
        existingUser.setUsername("existingUser");
        existingUser.setPassword("password");

        // 模拟 selectOne 方法返回一个用户对象，表示用户名已存在
        when(usersMapper.selectOne(any(QueryWrapper.class))).thenReturn(new Users());

        // 调用 registerUser 方法进行注册
        boolean result = userService.registerUser(existingUser);

        // 验证结果为 false，表示注册失败
        assertFalse(result);
    }
}