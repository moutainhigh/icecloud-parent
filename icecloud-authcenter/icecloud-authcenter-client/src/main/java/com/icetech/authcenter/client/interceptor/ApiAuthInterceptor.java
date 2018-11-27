package com.icetech.authcenter.client.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.icetech.authcenter.client.annotation.IgnoreToken;
import com.icetech.authcenter.client.constants.ConfigConstants;
import com.icetech.api.authcenter.service.TokenFeignApi;
import com.icetech.authcenter.client.utils.ResponseMsgUtils;
import com.icetech.common.JsonTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.util.ResponseUtils;
import com.icetech.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Auther: lisc
 * @Date: 2018/10/24 18:51
 * @Description: api请求拦截器 对api请求进行鉴权
 */
@Component
public class ApiAuthInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    @Lazy
    private TokenFeignApi tokenFeignApi;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 配置该注解，说明不进行服务拦截
        IgnoreToken annotation = handlerMethod.getBeanType().getAnnotation(IgnoreToken.class);
        if (annotation == null) {
            annotation = handlerMethod.getMethodAnnotation(IgnoreToken.class);
        }
        if(annotation!=null) {
            return super.preHandle(request, response, handler);
        }

        try {
            String token = request.getHeader(ConfigConstants.AUTH_HEADER);
            if (StringUtils.isEmpty(token)){
                ResponseMsgUtils.error(response);
                return false;
            }
            //验证token
            ObjectResponse<Boolean> objectResponse = tokenFeignApi.validateToken(token);
            if (objectResponse!=null&&"200".equals(objectResponse.getCode())&&objectResponse.getData()){
                return super.preHandle(request, response, handler);
            }else{
                ResponseMsgUtils.error(response);
                return false;
            }
        } catch (Exception e) {
            ResponseMsgUtils.error(response);
            return false;
        }
    }
}
