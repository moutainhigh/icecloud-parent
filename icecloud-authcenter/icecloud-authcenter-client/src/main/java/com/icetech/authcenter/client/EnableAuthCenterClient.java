package com.icetech.authcenter.client;

import com.icetech.authcenter.client.configuration.AutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.lang.annotation.*;

/**
 * @Auther: lisc
 * @Date: 2018/10/24 17:37
 * @Description: 快速启动鉴权客户端
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(AutoConfiguration.class)
@Documented
@Inherited
@EnableScheduling
public @interface EnableAuthCenterClient{
}
