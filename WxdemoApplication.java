package com.wx.ioc.wxdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
@MapperScan("com.wx.ioc.wxdemo.dao.*")
public class WxdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxdemoApplication.class, args);
    }
}
