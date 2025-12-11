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
    //用户信息 key
    public static final String USER_INFO_KEY = "user:info";
    //用户信息过期时间
    public static final long USER_INFO_EXPIRE_TIME = 60 * 60 * 8L;
    //商品列表 key
    public static final String PRODUCT_List_KEY = "product:list";
    //商品列表过期时间
    public static final long PRODUCT_INFO_EXPIRE_TIME = 60 * 60 * 2L;
    //商品信息 key
    public static final String PRODUCT_INFO_KEY = "product:info";
    //空值过期时间
    public static final long NULL_EXPIRE_TIME = 60*5L;
    //订单信息 key
    public static final String ORDER_INFO_KEY = "order:info";
    //购物车信息 key
    public static final String CART_INFO_KEY = "cart:info";
    //购物车过期时间
    public static final long CART_INFO_EXPIRE_TIME = 60 * 60 * 2L;
}
