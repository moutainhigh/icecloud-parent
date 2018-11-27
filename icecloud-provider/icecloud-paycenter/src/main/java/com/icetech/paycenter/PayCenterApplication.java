package com.icetech.paycenter;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.icetech.paycenter.mapper")
@EnableTransactionManagement(proxyTargetClass = true)
@EnableFeignClients({"com.icetech"})
@EnableEurekaClient
@EnableHystrix
@EnableHystrixDashboard
public class PayCenterApplication {
	private static final Logger logger = LoggerFactory.getLogger(PayCenterApplication.class);
	public static void main(String[] args) {
        SpringApplication.run(PayCenterApplication.class, args);
		logger.info("------启动完成------");
	}
}
