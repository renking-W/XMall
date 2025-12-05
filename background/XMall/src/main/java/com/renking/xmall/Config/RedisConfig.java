package com.renking.xmall.Config;

import lombok.Data;

@Data
public class RedisConfig {

    // 用户验证码 key
    public static final String USER_CODE_KEY = "user:code";
    // 用户token key
    public static final String USER_TOKEN_KEY = "user:token";
    //用户token过期时间
    public static final long USER_TOKEN_EXPIRE_TIME = 60 * 60 * 8L;
    //用户验证码过期时间
    public static final long USER_CODE_EXPIRE_TIME = 300L;
}
