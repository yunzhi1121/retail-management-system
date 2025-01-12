package com.yunzhi.retailmanagementsystem.service;

import com.yunzhi.retailmanagementsystem.model.domain.Users;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UsersServiceTest {

    @Resource
    private UsersService usersService;

    @Test
    public void testAddUser() {
        Users users = new Users();
        users.setUserID("123");
        users.setUsername("yunzhi");
        users.setPassword("123456");
        users.setRole("admin");


        boolean result = usersService.save(users);
        System.out.println(users.getId());
        Assertions.assertTrue(result);
    }

}