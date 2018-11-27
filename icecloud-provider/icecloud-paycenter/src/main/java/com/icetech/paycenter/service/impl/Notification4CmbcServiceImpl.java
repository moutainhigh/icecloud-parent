package com.icetech.paycenter.service.impl;


import com.icetech.common.JsonTools;
import com.icetech.common.ResultTools;

import com.icetech.paycenter.common.client.PayCenterClient;
import com.icetech.paycenter.common.enumeration.CmbcResultCode;
import com.icetech.paycenter.common.enumeration.PayCenterTradeStatus;
import com.icetech.paycenter.domain.AccountRecord;
import com.icetech.paycenter.domain.ParkCmbc;
import com.icetech.paycenter.domain.ThirdInfo;
import com.icetech.paycenter.domain.request.Notification4PayRequest;
import com.icetech.paycenter.domain.request.PayCenterBaseRequest;
import com.icetech.paycenter.domain.response.NotificationResponse;

import com.icetech.paycenter.mapper.AccountRecordDao;
import com.icetech.paycenter.mapper.ThirdInfoDao;
import com.icetech.paycenter.mapper.normalpay.ParkCmbcDao;
import com.icetech.paycenter.service.INotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 * 异步通知实现类
 * @author wangzw
 */
@Service
public class Notification4CmbcServiceImpl implements INotificationService {
    private static final Logger logger = LoggerFactory.getLogger(Notification4CmbcServiceImpl.class);
    private static final String SUCCESS = "SUCCESS";
    private static final String ERROR = "ERROR";
    private static final String SERVICE_NAME = "paynotify";
    @Autowired
    private AccountRecordDao accountRecordDao;
    @Autowired
    private PayCenterClient payCenterClient;
    @Autowired
    private ParkCmbcDao parkCmbcDao;
    @Autowired
    private ThirdInfoDao thirdInfoDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String doNotification(String context,String parkCode) throws Exception {
        logger.info("【民生银行异步通知】>>>> 通知参数：{}，车厂编号：{}",context,parkCode);
        NotificationResponse notificationResponse = JsonTools.toBean(context, NotificationResponse.class);
        //获取订单信息
        if(Objects.nonNull(notificationResponse)){
            String orderNo = notificationResponse.getOrderNo();
            AccountRecord accountRecord = accountRecordDao.selectByOutTradeNo(orderNo);
            if (Objects.nonNull(accountRecord)){
                accountRecord.setUpdateTime(new Date());
                accountRecord.setCenterSeqId(notificationResponse.getCenterSeqId());
                if (notificationResponse.getTradeStatus().equals(CmbcResultCode.SUCCESS.getCode())){
                    accountRecord.setStatus(PayCenterTradeStatus.SUCCESS.getCode());
                }
                if (notificationResponse.getTradeStatus().equals(CmbcResultCode.ERROR.getCode())){
                    accountRecord.setStatus(PayCenterTradeStatus.ERROR.getCode());
                }
                if (notificationResponse.getTradeStatus().equals(CmbcResultCode.CANCEL.getCode())){
                    accountRecord.setStatus(PayCenterTradeStatus.CANCEL.getCode());
                }
                logger.info("【民生银行异步通知】>>>> 交易流水状态更改,[车场号:{}][流水号:{}][状态:{}]",parkCode,accountRecord.getTradeNo(),accountRecord.getStatus());
                accountRecordDao.update(accountRecord);
                //获取民生银行的配置信息
                ParkCmbc parkCmbc = parkCmbcDao.selectByParkCode(parkCode);
                ThirdInfo thirdInfo = thirdInfoDao.selectByParkCode(parkCode);
                if (Objects.isNull(parkCmbc)|| Objects.isNull(thirdInfo)) return ERROR;
                PayCenterBaseRequest baseRequest = new PayCenterBaseRequest();
                baseRequest.setServiceName(SERVICE_NAME);
                Notification4PayRequest notification4payRequest = new Notification4PayRequest();
                notification4payRequest.setPrice(accountRecord.getIncome().toString());
                notification4payRequest.setTradeStatus(notificationResponse.getTradeStatus());
                notification4payRequest.setTradeNo(accountRecord.getTradeNo());
                notification4payRequest.setExtraInfo(accountRecord.getCenterInfo());
                baseRequest.setBizContent(notification4payRequest);
                String response = payCenterClient.sendPark(baseRequest,thirdInfo,parkCmbc.getNotifyUrl());
                logger.info("民生银行异步通知】>>>> 第三方响应信息:{}",response);
                boolean success = ResultTools.isSuccess(response);
                return success?SUCCESS:ERROR;
            }
        }
        return ERROR;
    }
}
