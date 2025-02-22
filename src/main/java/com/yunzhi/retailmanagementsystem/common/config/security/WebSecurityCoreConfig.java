package com.yunzhi.retailmanagementsystem.common.config.security;

import com.yunzhi.retailmanagementsystem.common.security.authentication.JwtAuthenticationFilter;
import com.yunzhi.retailmanagementsystem.common.security.authorization.PermissionEvaluator;
import com.yunzhi.retailmanagementsystem.common.security.authorization.RolePermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 启用方法级权限控制
public class WebSecurityCoreConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private RolePermissions rolePermissions;



    @Bean
    public PermissionEvaluator permissionEvaluator(RolePermissions rolePermissions) {
        return new PermissionEvaluator(rolePermissions);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // 禁用CSRF（因使用JWT无状态）
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 无状态会话
                .and()
            .authorizeRequests()
                .antMatchers("/users/login", "/users/register").permitAll() // 公开接口
                .anyRequest().authenticated() // 其他接口需认证
                .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // 添加JWT过滤器
    }
}