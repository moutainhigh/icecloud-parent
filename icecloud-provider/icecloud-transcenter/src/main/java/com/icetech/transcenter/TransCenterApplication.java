package com.icetech.transcenter;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author wangzw
 */
@SpringBootApplication
@MapperScan("com.icetech.transcenter.dao")
@EnableFeignClients({"com.icetech"})
@EnableEurekaClient
@EnableTransactionManagement
@EnableHystrix
@EnableHystrixDashboard
public class TransCenterApplication {

    private static final Logger logger = LoggerFactory.getLogger(TransCenterApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(TransCenterApplication.class, args);
        logger.info("------启动完成------");
    }
}
