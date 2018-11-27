package com.icetech.paycenter.config.aop;

import com.icetech.common.JsonTools;
import com.icetech.paycenter.domain.ResponseLogWithBLOBs;
import com.icetech.paycenter.domain.request.PayCenterBaseRequest;
import com.icetech.paycenter.mapper.ResponseLogDao;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 外部请求访问日志添加
 *
 */
@Aspect
@Component
public class ResponseLogAop {

    @Autowired
    private ResponseLogDao responseLogDao;

    @Pointcut("execution(public * com.icetech.paycenter.controller..*.park*(..))")
    public void parkResponselog() {
    }
    @Pointcut("execution(public * com.icetech.paycenter.controller..*.*Notification(..))")
    public void notifyResponseLog() {
    }


    /**
     * 民生银行异步通知请求记录
     * @param joinPoint
     * @param ret
     */
    @AfterReturning(returning = "ret", pointcut = "notifyResponseLog()")
    public void doAfterReturning1(JoinPoint joinPoint, Object ret){
        //获取请求的参数
        HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[0];
        ResponseLogWithBLOBs responseLog = new ResponseLogWithBLOBs();
        responseLog.setReqInterface("paynotify");
        responseLog.setReqParams((String) request.getAttribute("context"));
        //获取返回的参数
        String response = ret.toString();
        responseLog.setReturnResult(response);
        responseLogDao.insert(responseLog);
    }

    /**
     * 车场请求记录
     * @param joinPoint
     * @param ret
     */
    @AfterReturning(returning = "ret", pointcut = "parkResponselog()")
    public void doAfterReturning(JoinPoint joinPoint, Object ret){
        //获取请求的参数
        Object request = joinPoint.getArgs()[0];
        ResponseLogWithBLOBs responseLog = new ResponseLogWithBLOBs();
        String reqInterface = "";
        if (request instanceof PayCenterBaseRequest){
            reqInterface = ((PayCenterBaseRequest) request).getServiceName();
        }else{
            reqInterface = joinPoint.getSignature().getName();
        }
        responseLog.setReqInterface(reqInterface);
        responseLog.setReqParams(request == null ? "" : JsonTools.toString(request));

        //获取返回的参数
        String response = ret.toString();
        responseLog.setReturnResult(response);
        responseLogDao.insert(responseLog);
    }
}
