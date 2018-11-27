package com.icetech.authcenter.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Auther: lisc
 * @Date: 2018/10/25 11:50
 * @Description: 启动
 */
@SpringBootApplication
@EnableFeignClients({"com.icetech"})
@EnableEurekaClient
@EnableHystrix
@EnableHystrixDashboard
public class AuthCenterServerApplication {
    private static final Logger logger = LoggerFactory.getLogger(AuthCenterServerApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(AuthCenterServerApplication.class, args);
        logger.info("------启动完成------");
    }
}
