package com.icetech.authcenter.client.configuration;

import com.icetech.api.authcenter.model.request.AuthConfig;
import com.icetech.api.authcenter.service.TokenFeignApi;
import com.icetech.authcenter.client.config.FeignConfiguration;
import com.icetech.authcenter.client.config.WebConfigurer;
import com.icetech.authcenter.client.interceptor.ApiAuthInterceptor;
import com.icetech.authcenter.client.interceptor.FeignAuthInterceptor;
import com.icetech.authcenter.client.interceptor.MvcAuthInterceptor;
import com.icetech.authcenter.client.task.TokenScheduledTask;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @Auther: lisc
 * @Date: 2018/10/24 17:46
 * @Description: 自动扫描相关包
 */
@ComponentScan({"com.icetech.authcenter"})
@EnableFeignClients("com.icetech.api.authcenter.service")
@EnableConfigurationProperties({AuthConfig.class})
public class AutoConfiguration {


}
