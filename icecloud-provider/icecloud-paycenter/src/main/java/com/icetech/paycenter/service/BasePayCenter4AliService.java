package com.icetech.paycenter.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.google.common.collect.Maps;
import com.icetech.common.Base64Tools;
import com.icetech.common.JsonTools;
import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;

import com.icetech.paycenter.common.config.AliConfig;
import com.icetech.paycenter.common.enumeration.PayCenterInterfaceEnum;
import com.icetech.paycenter.domain.ParkAlipay;
import com.icetech.paycenter.domain.RequestLogWithBLOBs;
import com.icetech.paycenter.domain.normalpay.request.UnifiedOrderRequest;
import com.icetech.paycenter.domain.normalpay.response.UnifiedOrderResponse;
import com.icetech.paycenter.mapper.RequestLogDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * 支付宝不同下单实现
 * @author wangzw
 */
@Service
public class BasePayCenter4AliService{
    private static final Logger logger = LoggerFactory.getLogger(BasePayCenter4AliService.class);

    @Autowired
    private AliConfig aliConfig;
    @Autowired
    private RequestLogDao requestLogDao;

    /**
     * 支付宝WAP支付下单 2.0
     * @param orderRequest
     * @param parkAlipay
     * @return
     */
    public String doAliPayWapReq(UnifiedOrderRequest orderRequest, ParkAlipay parkAlipay) {
        AlipayClient client = new DefaultAlipayClient(aliConfig.getIsSandbox()?aliConfig.DEV_URL:aliConfig.URL, parkAlipay.getAppId(), parkAlipay.getPrivateKey(), aliConfig.FORMAT, aliConfig.CHARSET, parkAlipay.getAliPublicKey(), aliConfig.SIGNTYPE);
        AlipayTradeWapPayRequest aliRequest = new AlipayTradeWapPayRequest();

        //封装请求信息
        AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
        model.setOutTradeNo(orderRequest.getParkCode()+orderRequest.getTradeNo());
        model.setSubject(orderRequest.getProductInfo());
        model.setTotalAmount(Double.valueOf(orderRequest.getPrice())/100+"");
        model.setProductCode("QUICK_WAP_PAY");
        model.setBody(orderRequest.getProductInfo());
        model.setQuitUrl(parkAlipay.getQuitUrl());
        aliRequest.setBizModel(model);
        aliRequest.setNotifyUrl(aliConfig.getBaseNotifyUrl()+"/"+orderRequest.getParkCode());
        aliRequest.setReturnUrl(parkAlipay.getReturnUrl());
        AlipayTradeWapPayResponse response;
        try {
            RequestLogWithBLOBs requestLog = createRequestLog(JsonTools.toString(aliRequest), PayCenterInterfaceEnum.UNIFIED_ORDER.getCode());
            response = client.pageExecute(aliRequest);
            requestLog.setReturnResult(JsonTools.toString(response));
            requestLogDao.insert(requestLog);
        } catch (AlipayApiException e) {
            logger.error("【支付宝WAP下单异常】>>>> 信息:",e.getErrMsg());
            return ResultTools.setResponse(CodeConstants.ERROR_1100, e.getErrMsg());
        }
        if (response.getCode()==null||response.getCode().equals("10000")){
            Map map = Maps.newHashMap();
            map.put("payUrl",response.getBody());
            UnifiedOrderResponse unifiedOrderResponse = new UnifiedOrderResponse();
            unifiedOrderResponse.setPayInfo(response.getBody());
            unifiedOrderResponse.setMapPayInfo(map);
            return ResultTools.setResponse(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS),unifiedOrderResponse);
        }
        return ResultTools.setResponse(CodeConstants.ERROR_1100, response.getMsg());

    }

    /**
     * 支付宝PC支付下单
     * @param orderRequest
     * @param parkAlipay
     * @return
     */
    public String doAliPayPcReq(UnifiedOrderRequest orderRequest, ParkAlipay parkAlipay) {

        AlipayClient client = new DefaultAlipayClient(aliConfig.getIsSandbox()?aliConfig.DEV_URL:aliConfig.URL, parkAlipay.getAppId(), parkAlipay.getPrivateKey(), aliConfig.FORMAT, aliConfig.CHARSET, parkAlipay.getAliPublicKey(), aliConfig.SIGNTYPE);
        AlipayTradePagePayRequest aliRequest = new AlipayTradePagePayRequest();

        //封装请求信息
        AlipayTradePagePayModel model=new AlipayTradePagePayModel();
        model.setOutTradeNo(orderRequest.getParkCode()+orderRequest.getTradeNo());
        model.setSubject(orderRequest.getProductInfo());
        model.setTotalAmount(Double.valueOf(orderRequest.getPrice())/100+"");
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        model.setBody(orderRequest.getProductInfo());
        model.setQrPayMode("2");
        model.setQrcodeWidth(Long.parseLong("200"));
        aliRequest.setBizModel(model);
        aliRequest.setNotifyUrl(aliConfig.getBaseNotifyUrl()+"/"+orderRequest.getParkCode());
        aliRequest.setReturnUrl(parkAlipay.getReturnUrl());
        AlipayTradePagePayResponse response;
        try {
            RequestLogWithBLOBs requestLog = createRequestLog(JsonTools.toString(aliRequest), PayCenterInterfaceEnum.UNIFIED_ORDER.getCode());
            response = client.pageExecute(aliRequest);
            requestLog.setReturnResult(JsonTools.toString(response));
            requestLogDao.insert(requestLog);
        } catch (AlipayApiException e) {
            logger.error("【支付宝PC下单异常】>>>> 信息:",e.getErrMsg());
            return ResultTools.setResponse(CodeConstants.ERROR_1100, e.getErrMsg());
        }
        if (response.getCode()==null|| response.getCode().equals("10000")){
            Map map = Maps.newHashMap();
            //返回的为表单提交信息
            map.put("payUrl",response.getBody());
            UnifiedOrderResponse unifiedOrderResponse = new UnifiedOrderResponse();
            unifiedOrderResponse.setPayInfo(response.getBody());
            unifiedOrderResponse.setMapPayInfo(map);
            return ResultTools.setResponse(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS),unifiedOrderResponse);
        }
        return ResultTools.setResponse(CodeConstants.ERROR_1100, response.getMsg());
    }

    /**
     * 支付宝APP支付下单
     * @param orderRequest
     * @param parkAlipay
     * @return
     */
    public String doAliPayAppReq(UnifiedOrderRequest orderRequest, ParkAlipay parkAlipay) {
        AlipayClient client = new DefaultAlipayClient(aliConfig.getIsSandbox()?aliConfig.DEV_URL:aliConfig.URL, parkAlipay.getAppId(), parkAlipay.getPrivateKey(), aliConfig.FORMAT, aliConfig.CHARSET, parkAlipay.getAliPublicKey(), aliConfig.SIGNTYPE);

        AlipayTradeAppPayRequest aliRequest = new AlipayTradeAppPayRequest();
        // 封装请求支付信息
        AlipayTradeAppPayModel model=new AlipayTradeAppPayModel();
        model.setOutTradeNo(orderRequest.getParkCode()+orderRequest.getTradeNo());
        model.setSubject(orderRequest.getProductInfo());
        model.setTotalAmount(Double.valueOf(orderRequest.getPrice())/100+"");
        model.setBody(orderRequest.getProductInfo());
        model.setProductCode("QUICK_MSECURITY_PAY");
        aliRequest.setBizModel(model);
        aliRequest.setNotifyUrl(aliConfig.getBaseNotifyUrl()+"/"+orderRequest.getParkCode());
        aliRequest.setReturnUrl(parkAlipay.getReturnUrl());
        AlipayTradeAppPayResponse response;
        try {
            RequestLogWithBLOBs requestLog = createRequestLog(JsonTools.toString(aliRequest), PayCenterInterfaceEnum.UNIFIED_ORDER.getCode());
            response = client.sdkExecute(aliRequest);
            requestLog.setReturnResult(JsonTools.toString(response));
            requestLogDao.insert(requestLog);
        } catch (AlipayApiException e) {
            logger.error("【支付宝APP下单异常】>>>> 信息:",e.getErrMsg());
            return ResultTools.setResponse(CodeConstants.ERROR_1100, e.getErrMsg());
        }
        if (response.getCode()==null||response.getCode().equals("10000")){
            Map map = Maps.newHashMap();
            map.put("payParams", response.getBody());
            UnifiedOrderResponse unifiedOrderResponse = new UnifiedOrderResponse();
            unifiedOrderResponse.setPayInfo(response.getBody());
            unifiedOrderResponse.setMapPayInfo(map);
            return ResultTools.setResponse(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS),unifiedOrderResponse);
        }
        return ResultTools.setResponse(CodeConstants.ERROR_1100, response.getMsg());
    }

    /**
     * 支付宝当面付之扫码支付下单
     * @param orderRequest
     * @param parkAlipay
     * @return
     */
    public String doAliPayQrReq(UnifiedOrderRequest orderRequest, ParkAlipay parkAlipay) {
        AlipayClient client = new DefaultAlipayClient(aliConfig.getIsSandbox()?aliConfig.DEV_URL:aliConfig.URL, parkAlipay.getAppId(), parkAlipay.getPrivateKey(), aliConfig.FORMAT, aliConfig.CHARSET, parkAlipay.getAliPublicKey(), aliConfig.SIGNTYPE);
        AlipayTradePrecreateRequest aliRequest = new AlipayTradePrecreateRequest();
        // 封装请求支付信息
        AlipayTradePrecreateModel model=new AlipayTradePrecreateModel();
        model.setOutTradeNo(orderRequest.getParkCode()+orderRequest.getTradeNo());
        model.setSubject(orderRequest.getProductInfo());
        model.setTotalAmount(Double.valueOf(orderRequest.getPrice())/100+"");
        model.setBody(orderRequest.getProductInfo());
        aliRequest.setBizModel(model);
        // 设置异步通知地址
        aliRequest.setNotifyUrl(aliConfig.getBaseNotifyUrl()+"/"+orderRequest.getParkCode());
        // 设置同步地址
        aliRequest.setReturnUrl(parkAlipay.getReturnUrl());
        AlipayTradePrecreateResponse response;
        try {
            RequestLogWithBLOBs requestLog = createRequestLog(JsonTools.toString(aliRequest), PayCenterInterfaceEnum.UNIFIED_ORDER.getCode());
            response = client.execute(aliRequest);
            requestLog.setReturnResult(JsonTools.toString(response));
            requestLogDao.insert(requestLog);
        } catch (AlipayApiException e) {
            logger.error("【支付宝扫码付下单异常】>>>> 信息:",e.getErrMsg());
            return ResultTools.setResponse(CodeConstants.ERROR_1100, e.getErrMsg());
        }
        Map map = Maps.newHashMap();
        if (response.getCode()==null||response.getCode().equals("10000")){
            map.put("payUrl", Base64Tools.encode2String(response.getQrCode()));
            UnifiedOrderResponse unifiedOrderResponse = new UnifiedOrderResponse();
            unifiedOrderResponse.setPayInfo(Base64Tools.encode2String(response.getQrCode()));
            unifiedOrderResponse.setMapPayInfo(map);
            return ResultTools.setResponse(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS),unifiedOrderResponse);
        }
        return ResultTools.setResponse(CodeConstants.ERROR_1100, response.getMsg());
    }

    private RequestLogWithBLOBs createRequestLog(String context, String intefaceName) {
        RequestLogWithBLOBs requestLog = new RequestLogWithBLOBs();
        requestLog.setCreateTime(new Date());
        requestLog.setReqInterface(intefaceName);
        requestLog.setReqParams(context);
        return requestLog;
    }
}
