package com.icetech.cloudcenter;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.icetech.cloudcenter.dao")
@EnableTransactionManagement(proxyTargetClass = true)
@EnableFeignClients({"com.icetech"})
@EnableEurekaClient
@EnableHystrix
@EnableHystrixDashboard
public class CloudCenterApplication {
	private static final Logger logger = LoggerFactory.getLogger(CloudCenterApplication.class);
	public static void main(String[] args) {
        SpringApplication.run(CloudCenterApplication.class, args);
		logger.info("------启动完成------");
	}
}
