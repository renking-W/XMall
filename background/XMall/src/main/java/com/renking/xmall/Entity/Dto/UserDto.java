
package com.renking.xmall.Entity.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    //token
    private String token;
    //用户名
    private String userName;
    //手机号
    private String phone;
    //密码
    private String password;
    //验证码
    private String code;
}