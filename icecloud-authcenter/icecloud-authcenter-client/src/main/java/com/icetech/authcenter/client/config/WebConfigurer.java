package com.icetech.authcenter.client.config;

import com.icetech.authcenter.client.interceptor.ApiAuthInterceptor;
import com.icetech.authcenter.client.interceptor.MvcAuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Auther: lisc
 * @Date: 2018/10/24 19:03
 * @Description: 配置springmvc拦截器
 */
@Configuration
@Primary
public class WebConfigurer implements WebMvcConfigurer {
    @Autowired
    private ApiAuthInterceptor apiAuthInterceptor;
    @Autowired
    private MvcAuthInterceptor mvcAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiAuthInterceptor).addPathPatterns("/api/**");
        registry.addInterceptor(mvcAuthInterceptor).addPathPatterns("/**");
    }


}
