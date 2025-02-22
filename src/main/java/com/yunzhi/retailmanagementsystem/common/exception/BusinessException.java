package com.yunzhi.retailmanagementsystem.common.exception;
import com.yunzhi.retailmanagementsystem.common.model.response.ErrorCode;

/**
 * 自定义异常类
 *
 */
public class BusinessException extends RuntimeException {

    /**
     * 异常码
     */
    private final int code;

    /**
     * 描述
     */
    private final String description;

    /**
     * 构造方法
     *
     * @param message    异常消息
     * @param code       异常码
     * @param description 描述
     */
    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    /**
     * 构造方法
     *
     * @param errorCode 错误码枚举
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    /**
     * 构造方法
     *
     * @param errorCode    错误码枚举
     * @param description 描述
     */
    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode, String description, Exception e) {
        super(errorCode.getMessage(), e);
        this.code = errorCode.getCode();
        this.description = description;
    }

    /**
     * 获取异常码
     *
     * @return 异常码
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取描述
     *
     * @return 描述
     */
    public String getDescription() {
        return description;
    }
}
