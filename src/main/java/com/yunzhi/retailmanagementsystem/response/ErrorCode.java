package com.yunzhi.retailmanagementsystem.response;

/**
 * 错误码

 */
public enum ErrorCode {

    SUCCESS(20000, "ok", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "请求数据为空", ""),

    // user相关
    USERNAME_EXISTS(40002, "用户名已存在", "请更换用户名"),
    LOGIN_FAILED(40102, "登录失败", "用户名或密码错误"),
    USER_NOT_APPROVED(40301, "用户未审批", ""),
    USER_NOT_FOUND(40401, "用户不存在", ""),

    // 权限相关
    NOT_LOGIN(40100, "未登录", ""),
    NO_AUTH(40101, "无权限", ""),

    // customer相关错误码
    CUSTOMER_NOT_FOUND(40401, "客户不存在", "请检查客户ID或邮箱"),
    EMAIL_EXISTS(40901, "邮箱已存在", "请更换邮箱地址"),
    EMAIL_FORMAT_ERROR(40002, "邮箱格式错误", "请输入正确邮箱格式"),
    PHONE_FORMAT_ERROR(40003, "手机号格式错误", "请输入11位有效手机号"),
    UPDATE_FAILED(50002, "更新失败", "请检查更新条件"),

    // goods相关
    GOODS_NOT_FOUND(40400, "商品不存在", ""),
    GOODS_EXISTS(40001, "商品已存在", ""),

    // inventory相关
    INSUFFICIENT_STOCK(40010, "库存不足", ""),

    // token相关
    TOKEN_ERROR(40101, "Token验证失败", "请检查Token有效性"),
    TOKEN_EXPIRED(40102, "Token已过期", "请重新登录获取Token"),
    TOKEN_GENERATE_ERROR(50010, "Token生成失败", "系统密钥配置异常"),

    // 订单相关
    ORDER_NOT_FOUND(40401, "订单不存在", "请检查订单ID"),
    ORDER_STATUS_ERROR(40901, "订单状态异常", "当前状态不允许此操作"),

    // 并发控制
    CONCURRENT_UPDATE_ERROR(40903, "数据更新冲突", "请刷新后重试"),

    // 查询限制错误
    QUERY_LIMIT_EXCEEDED(40010, "查询超出限制", "单次查询数量不能超过 {0} 条"),

    // 系统错误
    SYSTEM_ERROR(50000, "系统内部异常", "");

    private final int code;

    /**
     * 状态码信息
     */
    private final String message;

    /**
     * 状态码描述（详情）
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


    public String getDescription() {
        return description;
    }
}
