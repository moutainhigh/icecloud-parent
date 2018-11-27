package com.icetech.bizcenter;

import com.icetech.authcenter.client.EnableAuthCenterClient;
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
@EnableTransactionManagement(proxyTargetClass = true)
@EnableEurekaClient
@EnableHystrix
@EnableHystrixDashboard
@EnableAuthCenterClient
@EnableFeignClients(value = {"com.icetech.api"})
public class BizCenterApplication {
	private static final Logger logger = LoggerFactory.getLogger(BizCenterApplication.class);
	public static void main(String[] args) {
        SpringApplication.run(BizCenterApplication.class, args);
		logger.info("------启动完成------");
	}
}
