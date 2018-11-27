package com.icetech.datacenter.config.aop;

import com.icetech.common.DataChangeTools;
import com.icetech.common.ToolsUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 外部请求访问日志添加
 *
 */
@Aspect
@Component
public class ResponseLogAop {
    private Logger logger = LoggerFactory.getLogger(ResponseLogAop.class);

    private static final String[] excludeParams = {"base64Str"};

    @Pointcut("execution(public * com.icetech.datacenter.controller.*.*(..))")
    public void reportResponselog() {
    }

    @Pointcut("execution(public * com.icetech.datacenter.service.down.impl.*.*(..))")
    public void downResponselog() {
    }

    @Around(value = "downResponselog()")
    public Object processTx(ProceedingJoinPoint jp) throws Throwable {
        Long startTime = System.currentTimeMillis();
        Object[] args = jp.getArgs();
        Object result = jp.proceed(args);
        //获取请求的参数
        Object baseRequest = args[0];
        log(startTime, null, baseRequest, jp, result.toString(), "平台其他服务");
        return result;
    }
    /**
     * 车场请求
     * @param joinPoint
     * @param ret
     */
    @AfterReturning(returning = "ret", pointcut = "reportResponselog()")
    public void doAfterReturning1(JoinPoint joinPoint, Object ret){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        //获取请求的参数
        Object baseRequest = joinPoint.getArgs()[0];
        String uri = request.getRequestURI().toString();
        if (uri.equals("/")){
            return;
        }
        Long startTime = (Long) request.getAttribute("startTime");
        log(startTime, uri, baseRequest, joinPoint, ret.toString(), "车场");
    }

    private void log(Long startTime, String url, Object baseRequest, JoinPoint joinPoint,
                     String responseStr, String from){
        try {
            // 方法名
            String req_interface = joinPoint.getSignature().getName();

            String params = DataChangeTools.bean2gson(baseRequest);
            if (params != null && excludeParams.length > 0){
                params = ToolsUtil.replaceJsonContent(params, excludeParams);
            }
            long endTime = System.currentTimeMillis();
            long usedTime = endTime - startTime;
            logger.info("处理来自{}的访问，用时 {} 毫秒； 请求的URL:{}, 方法名:{}, 参数:{}, 返回结果:{}", from, usedTime, url, req_interface, params, responseStr);

        } catch (Exception e) {
            // 记录本地异常日志
            logger.error("=======接口调用记录Log异常========");
            logger.error("请求的URL:{}", url);
            logger.error("异常方法:{}异常代码:{}异常信息:{}",
                    joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(),
                    e.getClass().getName(), e.getMessage());
        }
    }

}
