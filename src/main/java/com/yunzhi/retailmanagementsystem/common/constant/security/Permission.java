package com.yunzhi.retailmanagementsystem.common.constant.security;

public enum Permission {
    // 客户相关权限
    CUSTOMERS_CREATE("CUSTOMERS:CREATE", "创建客户"),
    CUSTOMERS_UPDATE("CUSTOMERS:UPDATE", "更新客户"),
    CUSTOMERS_READ("CUSTOMERS:READ", "查询客户"),

    // 商品相关权限
    GOODS_CREATE("GOODS:CREATE", "添加商品"),
    GOODS_UPDATE("GOODS:UPDATE", "更新商品"),
    GOODS_READ("GOODS:READ", "查询商品"),

    // 库存相关权限
    INVENTORY_CREATE("INVENTORY:CREATE", "添加库存"),
    INVENTORY_UPDATE("INVENTORY:UPDATE", "减少库存"),
    INVENTORY_READ("INVENTORY:READ", "查询库存状态"),

    // 订单相关权限
    ORDERS_CREATE("ORDERS:CREATE", "创建订单"),
    ORDERS_UPDATE("ORDERS:UPDATE", "更新订单"),
    ORDERS_READ("ORDERS:READ", "查询订单"),
    ORDERS_DELETE("ORDERS:DELETE", "取消订单"),

    // 报表相关权限
    REPORTS_SALES("REPORTS:SALES", "生成销售报表"),
    REPORTS_INVENTORY("REPORTS:INVENTORY", "生成库存报表"),
    REPORTS_CUSTOMERS("REPORTS:CUSTOMERS", "生成客户报表"),
    REPORTS_READ("REPORTS:READ", "查询报表"),

    // 服务请求相关权限
    SERVICE_REQUESTS_CREATE("SERVICE_REQUESTS:CREATE", "创建服务请求"),
    SERVICE_REQUESTS_READ("SERVICE_REQUESTS:READ", "查询服务请求"),
    SERVICE_REQUESTS_UPDATE("SERVICE_REQUESTS:UPDATE", "处理服务请求"),

    // 物流相关权限
    TRACKING_CREATE("TRACKING:CREATE", "创建物流信息"),
    TRACKING_READ("TRACKING:READ", "查询物流信息"),

    // 用户相关权限
    USERS_CREATE("USERS:CREATE", "注册用户"),
    USERS_LOGIN("USERS:LOGIN", "用户登录"),
    USERS_READ("USERS:READ", "查询用户"),
    USERS_UPDATE("USERS:UPDATE", "更新用户信息");

    private final String code;
    private final String description;

    Permission(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
    /**
     * 通过权限代码查找对应的权限枚举值
     * @param code 权限代码
     * @return 对应的权限枚举值，如果未找到则返回 null
     */
    public static Permission parse(String code) {
        for (Permission value : values()) {
            if (value.code.equalsIgnoreCase(code)) {
                return value;
            }
        }
        return null;
    }
}