package com.renking.xmall.Common.exception;


public class ServiceException extends RuntimeException {
    private Integer code;

    public ServiceException(String message,Integer code) {
        super(message);
        this.code = code;
    }

    public ServiceException(String message) {
        super(message);
        this.code = 500;
    }

    public Integer getCode() {
        return code;
    }
}
