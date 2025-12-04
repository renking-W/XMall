package com.renking.xmall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.renking.xmall.Mapper")
public class XMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(XMallApplication.class, args);
    }

}