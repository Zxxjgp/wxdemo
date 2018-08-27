package com.wx.ioc.wxdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WxdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxdemoApplication.class, args);
    }
}
