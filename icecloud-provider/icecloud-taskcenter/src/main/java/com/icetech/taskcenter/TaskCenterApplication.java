package com.icetech.taskcenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.icetech.paycenter.dao")
@EnableTransactionManagement(proxyTargetClass = true)
@EnableFeignClients({"com.icetech"})
@EnableEurekaClient
@EnableHystrix
@EnableHystrixDashboard
public class TaskCenterApplication {
    public static void main(final String[] args) {
        SpringApplication.run(TaskCenterApplication.class, args);
    }
}
