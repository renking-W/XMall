package com.renking.xmall.Entity;

import lombok.Data;

@Data
public class User {
    // id
    private Long id;
    //用户名
    private String userName;
    //手机号
    private String phone;
    //密码
    private String password;
}
