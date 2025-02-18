package com.yunzhi.retailmanagementsystem.exception;

import com.yunzhi.retailmanagementsystem.response.BaseResponse;
import com.yunzhi.retailmanagementsystem.response.ErrorCode;
import com.yunzhi.retailmanagementsystem.response.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * 该类用于全局捕获和处理异常，以统一异常处理逻辑
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 业务异常处理器
     *
     * 该方法用于捕获业务异常（BusinessException）并返回相应的错误响应
     *
     * @param e 业务异常实例，包含错误代码、消息和描述
     * @return 返回一个包含错误信息的BaseResponse对象
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    /**
     * 参数校验异常处理器
     * 该方法用于捕获参数校验异常（MethodArgumentNotValidException）并返回相应的错误响应
     *
     * @param ex 参数校验异常实例
     * @return 返回一个包含错误信息的BaseResponse对象
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<List<String>> handleValidationException(MethodArgumentNotValidException ex) {
        // 提取所有错误信息
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        // 将错误信息日志打印出来
        log.error("参数校验异常: {}", errors);

        // 将所有错误信息返回到 data 中，并将错误描述汇总到 description 中
        return new BaseResponse<>(
                ErrorCode.PARAMS_ERROR.getCode(),
                errors, // 错误列表作为 data
                "参数校验失败",
                String.join("; ", errors) // 将错误描述拼接成字符串返回
        );
    }


    /**
     * 运行时异常处理器
     *
     * 该方法用于捕获运行时异常（RuntimeException）并返回系统错误响应
     *
     * @param e 运行时异常实例
     * @return 返回一个包含系统错误信息的BaseResponse对象
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage());
    }
}
