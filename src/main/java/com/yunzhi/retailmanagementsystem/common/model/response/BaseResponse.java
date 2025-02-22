package com.yunzhi.retailmanagementsystem.common.model.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * 该类用于封装API响应结果，提供了一种统一的返回格式
 * 它包含状态码、数据、消息和描述等信息，以便调用者能够根据这些信息进行相应的处理
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int code;
    private T data;
    private String message;
    private String description; // 简要错误描述

    /**
     * 全参数构造方法
     *
     * @param code        状态码
     * @param data        数据
     * @param message     消息
     * @param description 描述
     */
    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message != null ? message : "";
        this.description = description != null ? description : "";
    }

    /**
     * 部分参数构造方法，不包含描述
     *
     * @param code    状态码
     * @param data    数据
     * @param message 消息
     */
    public BaseResponse(int code, T data, String message) {
        this(code, data, message, "");
    }

    /**
     * 部分参数构造方法，只包含状态码和数据
     *
     * @param code 状态码
     * @param data 数据
     */
    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    /**
     * 根据错误码枚举对象构造
     *
     * @param errorCode 错误码枚举对象，包含状态码、消息和描述等信息
     */
    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }
}
