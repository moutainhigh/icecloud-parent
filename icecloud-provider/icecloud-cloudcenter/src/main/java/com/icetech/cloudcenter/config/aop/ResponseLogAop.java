package com.icetech.cloudcenter.config.aop;

import com.icetech.common.DataChangeTools;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 外部请求访问日志添加
 *
 */
@Aspect
@Component
public class ResponseLogAop {
    private Logger logger = LoggerFactory.getLogger(ResponseLogAop.class);

    @Pointcut("execution(public * com.icetech.cloudcenter.rpc.*.*(..))")
    public void dubboResponselog() {
    }

    @Around(value = "dubboResponselog()")
    public Object processTx(ProceedingJoinPoint jp) throws Throwable {
        Long startTime = System.currentTimeMillis();
        Object[] args = jp.getArgs();
        Object result = jp.proceed(args);
        log(startTime, args, jp, result.toString());
        return result;
    }

    private void log(Long startTime, Object baseRequest, JoinPoint joinPoint,
                     String responseStr){
        try {
            // 方法名
            String req_interface = joinPoint.getSignature().getName();

            long endTime = System.currentTimeMillis();
            long usedTime = endTime - startTime;
            logger.info("方法名:{}，用时 {} 毫秒，参数:{}, 返回结果:{}", req_interface, usedTime,
                    DataChangeTools.bean2gson(baseRequest), DataChangeTools.bean2gson(responseStr));

        } catch (Exception e) {
            // 记录本地异常日志
            logger.error("=======接口调用记录Log异常========");
            logger.error("异常方法:{}异常代码:{}异常信息:{}",
                    joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(),
                    e.getClass().getName(), e.getMessage());
        }
    }

}
