package com.yunzhi.retailmanagementsystem.service;

import com.yunzhi.retailmanagementsystem.model.domain.Users;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UsersServiceTest {

    @Resource
    private UsersService usersService;
    private Users testUser;

    @BeforeEach
    public void setUp() {
        // 创建一个测试用户对象
        testUser = new Users();
        testUser.setUserID("123");
        testUser.setUsername("testUser");
        testUser.setPassword("password");
        testUser.setRole("user");
        //调用了 MyBatis-Plus 的 Mapper 接口的 insert 方法
        usersService.save(testUser);
    }

    @Test
    public void testGetByUserId() {
        // 调用 UserService 的 getByUserId 方法
        Users user = usersService.getByUserId("123");

        assertNotNull(user);
        assertEquals("123", user.getUserID());
        assertEquals("testUser", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("user", user.getRole());
    }

    @Test
    public void testGetByUsername() {
        // 调用 UserService 的 getByUsername 方法
        Users user = usersService.getByUsername("testUser");

        assertNotNull(user);
        assertEquals("123", user.getUserID());
        assertEquals("testUser", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("user", user.getRole());
    }

    @AfterEach
    public void tearDown() {
        //调用了 MyBatis-Plus 的 Mapper 接口的 deleteById 方法
        usersService.removeById("123");
    }

}