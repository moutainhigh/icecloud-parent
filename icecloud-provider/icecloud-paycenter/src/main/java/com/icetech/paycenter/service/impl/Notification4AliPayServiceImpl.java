package com.icetech.paycenter.service.impl;


import com.alipay.api.internal.util.AlipaySignature;
import com.icetech.common.JsonTools;
import com.icetech.common.ResultTools;
import com.icetech.paycenter.common.client.PayCenterClient;
import com.icetech.paycenter.common.config.AliConfig;
import com.icetech.paycenter.common.enumeration.PayCenterTradeStatus;
import com.icetech.paycenter.domain.AccountRecord;
import com.icetech.paycenter.domain.ParkAlipay;
import com.icetech.paycenter.domain.ThirdInfo;
import com.icetech.paycenter.domain.request.Notification4PayRequest;
import com.icetech.paycenter.domain.request.PayCenterBaseRequest;
import com.icetech.paycenter.mapper.AccountRecordDao;
import com.icetech.paycenter.mapper.ParkAlipayDao;
import com.icetech.paycenter.mapper.ThirdInfoDao;
import com.icetech.paycenter.service.INotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

/**
 * 异步通知实现类
 * @author wangzw
 */
@Service
public class Notification4AliPayServiceImpl implements INotificationService {
    private static final Logger logger = LoggerFactory.getLogger(Notification4AliPayServiceImpl.class);
    private static final String SERVICE_NAME = "paynotify";
    private static final String SUCCESS = "SUCCESS";
    private static final String ERROR = "ERROR";
    @Autowired
    private AccountRecordDao accountRecordDao;
    @Autowired
    private PayCenterClient payCenterClient;
    @Autowired
    private AliConfig aliConfig;
    @Autowired
    private ParkAlipayDao parkAlipayDao;
    @Autowired
    private ThirdInfoDao thirdInfoDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String doNotification(String context,String parkCode) throws Exception {
        logger.info("【支付宝异步通知】>>>> 通知参数：{}，车厂编号：{}",context,parkCode);
        ParkAlipay parkAlipay = parkAlipayDao.selectByParkCode(parkCode);
        Map<String,String> params = JsonTools.toBean(context, Map.class);
        boolean signResult = AlipaySignature.rsaCheckV1(params, parkAlipay.getAliPublicKey(), aliConfig.CHARSET, aliConfig.SIGNTYPE);
        if (!signResult){
            logger.info("【支付宝异步通知签名验证失败】");
            return ERROR;
        }
        String outTradeNo = params.get("out_trade_no");
        String notifyType = params.get("notify_type");
        if (Objects.nonNull(outTradeNo) && notifyType.equals("trade_status_sync")){
            AccountRecord accountRecord = accountRecordDao.selectByOutTradeNo(outTradeNo);
            if (Objects.nonNull(accountRecord)){
                //将状态更新为成功
                accountRecord.setStatus(PayCenterTradeStatus.SUCCESS.getCode());
                accountRecord.setCenterSeqId(params.get("trade_no"));
                accountRecord.setOpenId(params.get("buyer_id"));
                logger.info("【支付宝异步通知】>>>> 交易流水状态更改,[车场号:{}][流水号:{}][状态:{}]",parkCode,accountRecord.getTradeNo(),accountRecord.getStatus());
                accountRecordDao.update(accountRecord);
                ThirdInfo thirdInfo = thirdInfoDao.selectByParkCode(parkCode);
                //向第三方推送异步通知
                PayCenterBaseRequest baseRequest = new PayCenterBaseRequest();
                baseRequest.setServiceName(SERVICE_NAME);
                Notification4PayRequest notification4payRequest = new Notification4PayRequest();
                notification4payRequest.setPrice(accountRecord.getIncome().toString());
                notification4payRequest.setTradeStatus(PayCenterTradeStatus.SUCCESS.getCode());
                notification4payRequest.setTradeNo(accountRecord.getTradeNo());
                notification4payRequest.setExtraInfo(accountRecord.getCenterInfo());
                baseRequest.setBizContent(notification4payRequest);
                String response = payCenterClient.sendPark(baseRequest,thirdInfo,parkAlipay.getNotifyUrl());
                logger.info("【支付宝异步通知】>>>> H5返回参数:{}",response);
                boolean success = ResultTools.isSuccess(response);
                return success? SUCCESS :ERROR;
            }
        }
        return ERROR;

    }
}
