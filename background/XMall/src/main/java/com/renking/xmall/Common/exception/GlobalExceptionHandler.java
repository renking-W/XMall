package com.renking.xmall.Common.exception;

import com.renking.xmall.Entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice  // ✅ 添加这个注解
public class GlobalExceptionHandler {
    /**
     * 1. 处理自定义的业务异常
     * 只要代码里 throw new ServiceException("密码错误")，就会进到这里
     */
    @ExceptionHandler(ServiceException.class)
    public Result<?> handleServiceException(ServiceException e) {
        // 记录一个简单的警告日志即可，因为这是预期的业务错误
        log.warn("业务异常: {}", e.getMessage());
        return Result.failure(e.getCode(), e.getMessage());
    }

    /**
     * 2. 处理其他所有未知的异常 (兜底方案)
     * 比如空指针 (NullPointerException)、数组越界、除以0 等
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        // 这里要记录详细的错误堆栈，方便后端排查 BUG
        log.error("系统内部异常: ", e);
        return Result.failure(500, "系统开小差了，请联系管理员");
    }
}
