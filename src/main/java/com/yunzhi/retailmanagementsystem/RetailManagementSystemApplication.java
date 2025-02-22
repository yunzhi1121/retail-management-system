package com.yunzhi.retailmanagementsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({
        "com.yunzhi.retailmanagementsystem.business.**.mapper"
})
public class RetailManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RetailManagementSystemApplication.class, args);
    }

}
