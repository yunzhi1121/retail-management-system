package com.yunzhi.retailmanagementsystem.response;

import java.util.List;

/**
 * 返回工具类
 */
public class ResultUtils {
    /**
     * 创建一个成功的响应对象，不包含数据
     *
     * @param description 成功响应的描述信息
     * @return 返回一个包含成功状态和描述信息的BaseResponse对象
     */
    public static <T> BaseResponse<T> success(String description) {
        return new BaseResponse<>(200, null, "ok", description);
    }

    /**
     * 创建一个成功的响应对象，包含数据
     *
     * @param data 成功响应携带的数据
     * @param description 成功响应的描述信息
     * @return 返回一个包含成功状态、数据和描述信息的BaseResponse对象
     */
    public static <T> BaseResponse<T> success(T data, String description) {
        return new BaseResponse<>(200, data, "ok", description);
    }



    /**
     * 创建一个错误的响应对象，包含错误代码和默认错误信息
     *
     * @param errorCode 错误代码枚举对象，包含错误代码和默认错误信息
     * @return 返回一个包含错误状态和默认错误信息的BaseResponse对象
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }


    /**
     * 创建一个错误的响应对象，包含错误代码、默认错误信息和附加描述信息
     *
     * @param errorCode 错误代码枚举对象，包含错误代码和默认错误信息
     * @param description 附加的错误描述信息
     * @return 返回一个包含错误状态、默认错误信息和附加描述信息的BaseResponse对象
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String description) {
        return new BaseResponse<>(errorCode.getCode(), null, errorCode.getMessage(), description);
    }

    /**
     * 创建一个自定义错误的响应对象，包含自定义错误代码、错误信息和附加描述信息
     *
     * @param code 自定义错误代码
     * @param message 自定义错误信息
     * @param description 附加的错误描述信息
     * @return 返回一个包含自定义错误状态、错误信息和附加描述信息的BaseResponse对象
     */
    public static <T> BaseResponse<T> error(int code, String message, String description) {
        return new BaseResponse<>(code, null, message, description);
    }

}
