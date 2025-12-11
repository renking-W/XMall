
package com.renking.xmall.Entity.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    // id
    private Integer id;
    //用户名
    private String userName;
    //手机号
    private String phone;
    //密码
    private String password;
    //头像
    private String avatar;
    //性别
    private Integer gender;
    //token
    private String token;
    //旧密码
    private String oldPassword;
    //验证码
    private String code;
    // 金额
    private BigDecimal money;
}