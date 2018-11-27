package com.icetech.paycenter.config.interceptors;

import com.icetech.common.HttpTools;
import com.icetech.paycenter.config.interceptors.handle.CmbcHandle;
import com.icetech.paycenter.service.Spring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 通用拦截器
 * @author fangct
 */
public class CommonInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        httpServletRequest.setAttribute("startTime", System.currentTimeMillis());
        httpServletResponse.setContentType("text/html; charset=UTF-8");
        //获取ip地址
        String uri = httpServletRequest.getRequestURI();
        //民生请求进入
        if (uri.contains("/cmbc/")) {
            CmbcHandle cmbcHandle = Spring.getBean("cmbcHandle");
            boolean isSuccess = cmbcHandle.preHandle(httpServletRequest);
            if (!isSuccess){
                PrintWriter out = httpServletResponse.getWriter();
                out.append("ERROR");
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        Long startTime = (Long) httpServletRequest.getAttribute("startTime");
        Long endTime = System.currentTimeMillis();
        String ip = null;
        //获取ip地址
        try{
            ip = HttpTools.getIpAddr(httpServletRequest);
        }catch (Exception e){
            ip = "";
        }
        String uri = httpServletRequest.getRequestURI();
        logger.info("<通用拦截器> IP来源：{}，请求地址：{}，响应时间：{}ms", ip, uri, endTime - startTime);
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
//        logger.info("--------------处理请求完成视图渲染后的处理操作---------------");
    }
}
