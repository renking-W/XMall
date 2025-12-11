package com.renking.xmall.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
public class User extends BaseEntity implements Serializable {
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
    //金额
    private BigDecimal money;
}
