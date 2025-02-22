package com.yunzhi.retailmanagementsystem.common.config.security;

import com.yunzhi.retailmanagementsystem.common.security.authorization.PermissionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebRequestPermissionInterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private PermissionInterceptor permissionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(permissionInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns("/users/login", "/users/register"); // 排除登录和注册
    }
}