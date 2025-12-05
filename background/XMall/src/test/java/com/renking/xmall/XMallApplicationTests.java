package com.renking.xmall;

import com.renking.xmall.Config.RedisConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class XMallApplicationTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Test
    void contextLoads() {
        String phone = "18888888888";
        String token = "token";
        stringRedisTemplate.opsForValue().set(RedisConfig.USER_TOKEN_KEY+":"+phone, token, 60*60*8L, TimeUnit.SECONDS);
    }

}
