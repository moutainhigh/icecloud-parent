package com.icetech.paycenter.service.impl;


import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.icetech.common.ResultTools;
import com.icetech.paycenter.common.client.PayCenterClient;
import com.icetech.paycenter.common.config.WxConfig;
import com.icetech.paycenter.common.enumeration.PayCenterTradeStatus;
import com.icetech.paycenter.common.tool.WxPayUtil;
import com.icetech.paycenter.domain.AccountRecord;
import com.icetech.paycenter.domain.ParkWeixin;
import com.icetech.paycenter.domain.ThirdInfo;
import com.icetech.paycenter.domain.request.Notification4PayRequest;
import com.icetech.paycenter.domain.request.PayCenterBaseRequest;
import com.icetech.paycenter.mapper.AccountRecordDao;
import com.icetech.paycenter.mapper.ParkWeixinDao;
import com.icetech.paycenter.mapper.ThirdInfoDao;
import com.icetech.paycenter.service.INotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 异步通知实现类
 * @author wangzw
 */
@Service
public class Notification4WxPayServiceImpl implements INotificationService {
    private static final Logger logger = LoggerFactory.getLogger(Notification4WxPayServiceImpl.class);
    private static final String SERVICE_NAME = "paynotify";
    @Autowired
    private AccountRecordDao accountRecordDao;
    @Autowired
    private PayCenterClient payCenterClient;
    @Autowired
    private WxConfig wxConfig;
    @Autowired
    private ParkWeixinDao parkWeixinDao;
    @Autowired
    private ThirdInfoDao thirdInfoDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String doNotification(String context,String parkCode) throws Exception {
        logger.info("【支微信异步通知】>>>> 通知参数：{}，车厂编号：{}",context,parkCode);
        try {
            //验证数据签名是否正确
            WxPayService wxPayService = new WxPayServiceImpl();
            ParkWeixin parkWeixin = parkWeixinDao.selectByParkCode(parkCode);
            WxPayConfig wxPayConfig = WxPayUtil.getWxPayConfig(wxConfig, parkWeixin);
            wxPayService.setConfig(wxPayConfig);
            WxPayOrderNotifyResult notifyResult = wxPayService.parseOrderNotifyResult(context);
            if (Objects.nonNull(notifyResult)){
                AccountRecord accountRecord = accountRecordDao.selectByOutTradeNo(notifyResult.getOutTradeNo());
                if (Objects.nonNull(accountRecord)){
                    //将状态更新为成功
                    accountRecord.setStatus(PayCenterTradeStatus.SUCCESS.getCode());
                    accountRecord.setCenterSeqId(notifyResult.getTransactionId());
                    accountRecord.setOpenId(notifyResult.getOpenid());
                    logger.info("【微信异步通知】>>>> 交易流水状态更改,[车场号:{}][流水号:{}][状态:{}]",parkCode,accountRecord.getTradeNo(),accountRecord.getStatus());
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
                    String response = payCenterClient.sendPark(baseRequest,thirdInfo,parkWeixin.getNotifyUrl());
                    logger.info("【微信异步通知】>>>> H5返回参数:{}",response);
                    boolean success = ResultTools.isSuccess(response);
                    return success? WxPayNotifyResponse.success("OK") :WxPayNotifyResponse.fail("fail");
                }
            }
        }catch (WxPayException e){
            logger.info("【微信支付异步通知异常】>>>> msg:{}",e.getReturnMsg());
            return WxPayNotifyResponse.fail(e.getReturnMsg());
        }
        return WxPayNotifyResponse.fail("系统异常");
    }
}
