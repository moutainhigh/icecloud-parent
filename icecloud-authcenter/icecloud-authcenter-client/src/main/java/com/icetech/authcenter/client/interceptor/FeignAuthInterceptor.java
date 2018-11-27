package com.icetech.authcenter.client.interceptor;

import com.icetech.authcenter.client.constants.ConfigConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @Auther: lisc
 * @Date: 2018/10/24 18:41
 * @Description: feign拦截器 发送请求前自动设置请求头
 */
public class FeignAuthInterceptor  implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header(ConfigConstants.AUTH_HEADER, System.getProperty("auth.token"));
    }
}
