package com.yunzhi.retailmanagementsystem.common.model.response;

/**
 * 统一错误码枚举
 * 编码规则：HTTP状态码前缀 + 模块编号 + 具体错误码（共5位数字）
 *
 * 分类结构：
 * 40000-40999 客户端请求错误
 * 40100-40199 认证/权限错误
 * 40400-40499 数据不存在
 * 40900-40999 资源冲突
 * 50000-59999 服务端错误
 * 50000-50099 通用服务错误
 * 50100-50199 报表服务错误
 */
public enum ErrorCode {
    // region 基础错误 (400xx)
    PARAM_ERROR(40000, "请求参数错误", "请检查请求参数格式"),
    PARAM_MISSING(40001, "必要参数缺失", "请检查必填参数"),
    PARAM_FORMAT_ERROR(40002, "参数格式错误", "请检查参数格式是否符合要求"),
    DATA_VALIDATION_FAILED(40003, "数据校验失败", "请检查数据规则"),

    // region 认证授权 (401xx)
    UNAUTHORIZED(40100, "未授权访问", "请先登录系统"),
    INVALID_CREDENTIAL(40101, "认证凭证无效", "用户名/密码错误或令牌失效"),
    ACCESS_DENIED(40103, "访问权限不足", "当前角色无权进行此操作"),
    TOKEN_EXPIRED(40104, "访问令牌过期", "请重新登录获取新令牌"),
    TOKEN_INVALID(40105, "令牌格式无效", "请使用有效的令牌格式"),

    // region 数据不存在 (404xx)
    USER_NOT_FOUND(40401, "用户不存在", "请检查用户ID或账号"),
    CUSTOMER_NOT_FOUND(40402, "客户不存在", "请检查客户ID或联系方式"),
    GOODS_NOT_FOUND(40403, "商品不存在", "请检查商品ID或条码"),
    ORDER_NOT_FOUND(40404, "订单不存在", "请检查订单ID"),
    REPORT_NOT_FOUND(40405, "报表不存在", "请检查报表ID"),
    SERVICE_REQUEST_NOT_FOUND(40406, "服务请求不存在", "请检查请求ID是否正确"),
    TRACKING_NOT_FOUND(40407, "物流记录不存在", "请检查订单ID"),

    // region 业务限制 (409xx)
    DUPLICATE_DATA(40900, "数据重复", "请检查唯一性约束字段"),
    ORDER_STATUS_CONFLICT(40901, "订单状态冲突", "当前状态不允许此操作"),
    STOCK_INSUFFICIENT(40902, "库存不足", "当前库存无法满足需求"),
    CONCURRENT_CONFLICT(40903, "并发操作冲突", "请刷新数据后重试"),
    SERVICE_REQUEST_COMPLETED(40904, "请求已完成", "当前请求已处理完成，无法再次处理"),

    // region 服务端错误 (500xx)
    INTERNAL_ERROR(50000, "系统内部错误", "请联系系统管理员"),
    DB_OPERATION_FAILED(50001, "数据库操作失败", "请检查数据库连接或SQL"),
    FILE_PROCESS_ERROR(50002, "文件处理失败", "请检查文件格式或权限"),
    THIRD_PARTY_ERROR(50003, "第三方服务异常", "服务暂时不可用，请稍后重试"),

    // region 报表服务错误 (501xx)
    REPORT_GENERATE_FAILED(50101, "报表生成失败", "数据处理过程中发生错误"),
    REPORT_SERIALIZATION_ERROR(50102, "报表序列化失败", "请检查数据格式"),
    REPORT_SAVE_FAILED(50103, "报表存储失败", "文件系统或数据库存储异常"),
    REPORT_DATA_EMPTY(50104, "报表数据为空", "没有符合条件的数据"),

    // 成功状态
    SUCCESS(20000, "操作成功", "");

    /**
     * 错误码（5位数字）
     */
    private final int code;

    /**
     * 简明错误信息（用户可见）
     */
    private final String message;

    /**
     * 详细错误描述（开发排查用）
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    // region getter
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
    // endregion
}