package com.renking.xmall.Entity;

import com.renking.xmall.Config.StatusCode;
import lombok.Data;

@Data
public class Result<T> {
    private T data;
    private String message;
    private Integer code;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setData(data);
        result.setCode(StatusCode.SUCCESS);
        result.setMessage("操作成功");
        return result;
    }

    public static <T> Result<T> failure(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
