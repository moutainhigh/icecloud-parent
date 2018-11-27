package com.icetech.transcenter.config.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 日志切面
 * @author wangzw
 *
 */

@Aspect
@Component
public class ServiceLog4AppAop {

    private Logger logger = LoggerFactory.getLogger(ServiceLog4AppAop.class);

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("execution(public * com.icetech.api.service.app.*.*(..))")
    public void serviceLog() {
    }

    @Before("serviceLog()")
    public void doBefore(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());
    }

    @AfterReturning(returning = "ret", pointcut = "serviceLog()")
    public void doAfterReturning(JoinPoint joinPoint, Object ret){
        logger.info("请求方法名称 : {}, 参数 : {}, 响应内容 : {}, 接口耗时 : {}", joinPoint.getSignature().getDeclaringTypeName() + "."
                + joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()), ret, (System.currentTimeMillis() - startTime.get()) + "ms");
    }
}