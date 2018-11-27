package com.icetech.authcenter.client.config;

import com.icetech.authcenter.client.interceptor.FeignAuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: lisc
 * @Date: 2018/10/24 18:43
 * @Description: 配置feign过滤器
 */
@Configuration
public class FeignConfiguration {
    /**
     * 创建Feign请求拦截器，在发送请求前设置认证的token,各个微服务将token设置到环境变量中来达到通用
     * @return
     */
    @Bean
    public FeignAuthInterceptor feignAuthInterceptor() {
        return new FeignAuthInterceptor();
    }
}
