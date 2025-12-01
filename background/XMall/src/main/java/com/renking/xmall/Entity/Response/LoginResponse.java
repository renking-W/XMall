
package com.renking.xmall.Entity.Response;

import lombok.Data;

@Data
public class LoginResponse {
    private boolean success;
    private String message;
    private String token; // 可选：如果需要返回认证token

    public static LoginResponse success(String message, String token) {
        LoginResponse response = new LoginResponse();
        response.success = true;
        response.message = message;
        response.token = token;
        return response;
    }

    public static LoginResponse failure(String message) {
        LoginResponse response = new LoginResponse();
        response.success = false;
        response.message = message;
        return response;
    }
}