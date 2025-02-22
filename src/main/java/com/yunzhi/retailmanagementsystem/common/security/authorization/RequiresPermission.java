package com.yunzhi.retailmanagementsystem.common.security.authorization;

import com.yunzhi.retailmanagementsystem.common.constant.security.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 用于方法
@Retention(RetentionPolicy.RUNTIME) // 运行时保留
public @interface RequiresPermission {
    Permission value(); // 接受枚举类型
}