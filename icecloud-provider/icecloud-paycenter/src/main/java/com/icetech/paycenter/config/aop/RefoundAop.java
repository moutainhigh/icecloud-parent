package com.icetech.paycenter.config.aop;


import com.icetech.common.DataChangeTools;
import com.icetech.common.ResultTools;
import com.icetech.paycenter.common.enumeration.PayCenterTradeStatus;
import com.icetech.paycenter.domain.AccountRecord;
import com.icetech.paycenter.domain.request.PayCenterBaseRequest;
import com.icetech.paycenter.domain.request.RefundRequest;

import com.icetech.paycenter.mapper.AccountRecordDao;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * 退款的aop
 * @author wangzw
 */
@Aspect
@Component
public class RefoundAop {
    private Logger logger = LoggerFactory.getLogger(RefoundAop.class);
    @Autowired
    private AccountRecordDao accountRecordDao;

    @Pointcut("execution(public * com.icetech.paycenter.service.normalpay.impl.RefundServiceImpl.execute(..))")
    public void serviceLog() {
    }

    @AfterReturning(returning = "ret", pointcut = "serviceLog()")
    public void doAfterReturning(JoinPoint joinPoint, Object ret){
        //获取返回的参数
        String response = ret.toString();
        boolean success = ResultTools.isSuccess(response);
        if (!success)return;
        //获取请求参数
        PayCenterBaseRequest baseRequest = (PayCenterBaseRequest) joinPoint.getArgs()[0];
        RefundRequest refundRequest = DataChangeTools.convert2bean(baseRequest.getBizContent(), RefundRequest.class);
        //更新流水记录为退款中
        AccountRecord accountRecord = accountRecordDao.selectByParkCodeAndTradeNo(refundRequest.getParkCode(), refundRequest.getTradeNo());
        if (Objects.isNull(accountRecord)) return;
        accountRecord.setStatus(PayCenterTradeStatus.TRANSFER.getCode());
        accountRecord.setUpdateTime(new Date());
        logger.info("<退款调用> 交易流水状态更改,[车场号:{}][流水号:{}][状态:{}]",accountRecord.getParkCode(),accountRecord.getTradeNo(),accountRecord.getStatus());
        accountRecordDao.update(accountRecord);
    }
}
