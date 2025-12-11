package com.renking.xmall.Config;

public class StatusCode {

    //用户不存在
    public static final int USER_NOT_EXIST = 20501;
    //token无效
    public static final int TOKEN_INVALID = 20502;
    //请求成功
    public static final Integer SUCCESS = 20000;
    //验证码无效
    public static final Integer CODE_UNVALID = 20503;
    //数据解析失败
    public static final Integer DATA_PARSE_ERROR = 50001;
    //用户密码错误
    public static final Integer USER_PASSWORD_ERROR = 20504;
    //查询失败
    public static final Integer QUERY_FAILED = 50002;
    //缓存写入失败
    public static final Integer CACHE_WRITE_FAILED = 50003;
    //商品不存在
    public static final Integer PRODUCT_NOT_EXIST = 20505;
    //商品修改失败
    public static final Integer PRODUCT_EDIT_EROR = 20506;
    //商品添加失败
    public static final Integer PRODUCT_ADD_ERROR = 20507;
    //用户充值失败
    public static final Integer USER_RECHARGE_ERROR = 20508;
    //用户购买失败
    public static final Integer ORDER_BUY_ERROR = 20509;
    //用户余额不足
    public static final Integer USER_MONEY_NOT_ENOUGH = 20510;
    //商品库存不足
    public static final Integer PRODUCT_STOCK_NOT_ENOUGH = 20511;
    //MQ消费者异常
    public static final Integer MQ_CONSUMER_ERROR = 50003;
    //用户扣减余额失败
    public static final Integer USER_REDUCE_MONEY_ERROR = 20512;
    //数据查询失败
    public static final Integer DATA_QUERY_ERROR = 50004;
}
