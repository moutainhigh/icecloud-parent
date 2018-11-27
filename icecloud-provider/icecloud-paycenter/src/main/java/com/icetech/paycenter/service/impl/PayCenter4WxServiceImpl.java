package com.icetech.paycenter.service.impl;

import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderCloseResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.github.binarywang.wxpay.util.SignUtils;
import com.icetech.common.Base64Tools;
import com.icetech.common.JsonTools;
import com.icetech.common.ResultTools;
import com.icetech.common.UUIDTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.paycenter.common.config.WxConfig;
import com.icetech.paycenter.common.enumeration.PayCenterInterfaceEnum;
import com.icetech.paycenter.common.enumeration.PayCenterTradeStatus;
import com.icetech.paycenter.common.enumeration.SelectTradeType;
import com.icetech.paycenter.common.tool.WxPayUtil;
import com.icetech.paycenter.domain.AccountRecord;
import com.icetech.paycenter.domain.ParkWeixin;
import com.icetech.paycenter.domain.RequestLogWithBLOBs;
import com.icetech.paycenter.domain.normalpay.request.TransactionDetailsDownloadRequest;
import com.icetech.paycenter.domain.normalpay.request.UnifiedOrderRequest;
import com.icetech.paycenter.domain.normalpay.response.PayResultResponse;
import com.icetech.paycenter.domain.normalpay.response.UnifiedOrderResponse;
import com.icetech.paycenter.domain.request.CloseOrderRequest;
import com.icetech.paycenter.domain.request.PayResultRequest;
import com.icetech.paycenter.domain.request.RefundRequest;
import com.icetech.paycenter.mapper.AccountRecordDao;
import com.icetech.paycenter.mapper.ParkWeixinDao;
import com.icetech.paycenter.mapper.RequestLogDao;
import com.icetech.paycenter.service.IWxPayCenterService;
import com.icetech.paycenter.service.handler.WeiXinCodeHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.icetech.paycenter.common.enumeration.ResponseTradeStatus.getStatus4WxStatus;
import static com.icetech.paycenter.common.enumeration.SelectTradeType.getWxTradeType;

/**
 * 微信支付中心对接服务类
 * @author wangzw
 */
@Service
public class PayCenter4WxServiceImpl implements IWxPayCenterService {
    private static final Logger logger = LoggerFactory.getLogger(PayCenter4WxServiceImpl.class);
    @Autowired
    private ParkWeixinDao parkWeixinDao;
    @Autowired
    private WxConfig wxConfig;
    @Autowired
    private RequestLogDao requestLogDao;
    @Autowired
    private AccountRecordDao accountRecordDao;

    @Override
    public String doUnifiedOrder(UnifiedOrderRequest orderRequest) {
        try {
            //获取微信的配置
            ParkWeixin parkWeixin = parkWeixinDao.selectByParkCode(orderRequest.getParkCode());
            if (Objects.isNull(parkWeixin)){
                return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
            }
            WxPayConfig wxPayConfig = WxPayUtil.getWxPayConfig(wxConfig,parkWeixin,getWxTradeType(orderRequest.getSelectTradeType()),wxConfig.getBaseUnifiedOrderNotifyUrl()+"/"+orderRequest.getParkCode());
            WxPayService wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(wxPayConfig);
            WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = createUnifiedOrderRequest(orderRequest, wxPayConfig, parkWeixin);
            RequestLogWithBLOBs requestLog = createRequestLog(JsonTools.toString(wxPayUnifiedOrderRequest), PayCenterInterfaceEnum.UNIFIED_ORDER.getCode());
            WxPayUnifiedOrderResult wxPayUnifiedOrderResult = wxPayService.unifiedOrder(wxPayUnifiedOrderRequest);
            requestLog.setReturnResult(JsonTools.toString(wxPayUnifiedOrderResult));
            requestLogDao.insert(requestLog);
            logger.info("【微信支付统一下单】 >>>> 下单成功");
            Map<String, Object> map = new HashMap<>();
            map.put("payOrderId", orderRequest.getTradeNo());
            switch (getWxTradeType(orderRequest.getSelectTradeType())) {
                case SelectTradeType.WxConstant.WX_NATIVE : {
                    // 二维码支付链接
                    map.put("payUrl", Base64Tools.encode2String(wxPayUnifiedOrderResult.getCodeURL()));
                    break;
                }
                case SelectTradeType.WxConstant.WX_APP : {
                    Map<String, String> payInfo = new HashMap<>();
                    String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
                    String nonceStr = String.valueOf(System.currentTimeMillis());
                    // APP支付绑定的是微信开放平台上的账号，APPID为开放平台上绑定APP后发放的参数
                    String appId = wxPayConfig.getAppId();
                    Map<String, String> configMap = new HashMap<>();
                    // 此map用于参与调起sdk支付的二次签名,格式全小写，timestamp只能是10位,格式固定，切勿修改
                    String partnerId = wxPayConfig.getMchId();
                    configMap.put("prepayid", wxPayUnifiedOrderResult.getPrepayId());
                    configMap.put("partnerid", partnerId);
                    String packageValue = "Sign=WXPay";
                    configMap.put("package", packageValue);
                    configMap.put("timestamp", timestamp);
                    configMap.put("noncestr", nonceStr);
                    configMap.put("appid", appId);
                    // 此map用于客户端与微信服务器交互
                    payInfo.put("sign", SignUtils.createSign(configMap, null,wxPayConfig.getMchKey(), null));
                    payInfo.put("prepayId", wxPayUnifiedOrderResult.getPrepayId());
                    payInfo.put("partnerId", partnerId);
                    payInfo.put("appId", appId);
                    payInfo.put("packageValue", packageValue);
                    payInfo.put("timeStamp", timestamp);
                    payInfo.put("nonceStr", nonceStr);
                    map.put("payParams", payInfo);
                    break;
                }
                case SelectTradeType.WxConstant.WX_JSAPI : {
                    Map<String, String> payInfo = new HashMap<>();
                    String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
                    String nonceStr = String.valueOf(System.currentTimeMillis());
                    payInfo.put("appId", wxPayUnifiedOrderResult.getAppid());
                    // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                    payInfo.put("timeStamp", timestamp);
                    payInfo.put("nonceStr", nonceStr);
                    payInfo.put("package", "prepay_id=" + wxPayUnifiedOrderResult.getPrepayId());
                    payInfo.put("signType", WxPayConstants.SignType.MD5);
                    payInfo.put("paySign", SignUtils.createSign(payInfo,null, wxPayConfig.getMchKey(), null));
                    map.put("payParams", payInfo);
                    break;
                }
                case SelectTradeType.WxConstant.WX_MWEB : {
                    // h5支付链接地址
                    map.put("payUrl", wxPayUnifiedOrderResult.getMwebUrl());
                    break;
                }
            }
            UnifiedOrderResponse unifiedOrderResponse = new UnifiedOrderResponse();
            unifiedOrderResponse.setMapPayInfo(map);
            unifiedOrderResponse.setPayInfo(JsonTools.toString(map));
            return ResultTools.setResponse(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS),unifiedOrderResponse);
        }catch (WxPayException e){
            return ResultTools.setResponse(CodeConstants.ERROR_1100, StringUtils.isBlank(e.getCustomErrorMsg())?e.getReturnMsg():e.getCustomErrorMsg());
        }catch (Exception e){
            return ResultTools.setResponse(CodeConstants.ERROR, CodeConstants.getName(CodeConstants.ERROR));
        }
    }

    @Override
    public String doPayResult(PayResultRequest payResultRequest) {
        try {
            //获取微信的配置
            ParkWeixin parkWeixin = parkWeixinDao.selectByParkCode(payResultRequest.getParkCode());
            if (Objects.isNull(parkWeixin)){
                return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
            }
            WxPayConfig wxPayConfig = WxPayUtil.getWxPayConfig(wxConfig,parkWeixin);
            WxPayService wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(wxPayConfig);
            RequestLogWithBLOBs requestLog = createRequestLog(payResultRequest.getTradeNo(), PayCenterInterfaceEnum.QUERY_ORDER.getCode());
            WxPayOrderQueryResult wxPayOrderQueryResult = wxPayService.queryOrder(null, payResultRequest.getParkCode()+payResultRequest.getTradeNo());
            requestLog.setReturnResult(JsonTools.toString(wxPayOrderQueryResult));
            requestLogDao.insert(requestLog);
            PayResultResponse payResultResponse = new PayResultResponse();
            payResultResponse.setTradeStatus(getStatus4WxStatus(wxPayOrderQueryResult.getTradeState()));
            payResultResponse.setPrice(wxPayOrderQueryResult.getTotalFee()==null?null:wxPayOrderQueryResult.getTotalFee().toString());
            payResultResponse.setTradeType("WX-"+wxPayOrderQueryResult.getTradeType());
            return ResultTools.setResponse(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS),payResultResponse);
        }catch (WxPayException e){
            return ResultTools.setResponse(CodeConstants.ERROR_1100, "微信订单查询异常");
        }catch (Exception e){
            return ResultTools.setResponse(CodeConstants.ERROR, CodeConstants.getName(CodeConstants.ERROR));
        }
    }

    @Override
    public String doRefund(RefundRequest refundRequest) {
        try {
            //获取微信的配置
            ParkWeixin parkWeixin = parkWeixinDao.selectByParkCode(refundRequest.getParkCode());
            if (Objects.isNull(parkWeixin)){
                return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
            }
            WxPayConfig wxPayConfig = WxPayUtil.getWxPayConfig(wxConfig, parkWeixin,"",wxConfig.getBaseRefundNotifyUrl());
            WxPayService wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(wxPayConfig);
            //查询交易记录
            AccountRecord accountRecord = accountRecordDao.selectByParkCodeAndTradeNo(refundRequest.getParkCode(), refundRequest.getTradeNo());
            if (Objects.isNull(accountRecord) || !accountRecord.getStatus().equals(PayCenterTradeStatus.SUCCESS.getCode())){
                return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
            }
            WxPayRefundRequest wxPayRefundRequest = createWxPayRefundRequest(accountRecord, refundRequest);
            RequestLogWithBLOBs requestLog = createRequestLog(JsonTools.toString(wxPayRefundRequest), PayCenterInterfaceEnum.REFUND.getCode());
            WxPayRefundResult refund = wxPayService.refund(wxPayRefundRequest);
            requestLog.setReturnResult(JsonTools.toString(refund));
            requestLogDao.insert(requestLog);
            return ResultTools.setResponse(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS));
        }catch (WxPayException e){
            return ResultTools.setResponse(CodeConstants.ERROR_1100, e.getReturnMsg());
        }catch (Exception e){
            return ResultTools.setResponse(CodeConstants.ERROR, CodeConstants.getName(CodeConstants.ERROR));
        }
    }

    @Override
    public String downloadTransactionDetails(TransactionDetailsDownloadRequest downloadRequest) {

        return null;
    }


    @Override
    public String doCloseOrder(CloseOrderRequest closeOrderRequest) {
        try {
            //获取微信的配置
            ParkWeixin parkWeixin = parkWeixinDao.selectByParkCode(closeOrderRequest.getParkCode());
            if (Objects.isNull(parkWeixin)){
                return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
            }
            WxPayConfig wxPayConfig = WxPayUtil.getWxPayConfig(wxConfig, parkWeixin);
            WxPayService wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(wxPayConfig);
            //查询交易记录(交易不存在则不允许关闭)
            AccountRecord accountRecord = accountRecordDao.selectByParkCodeAndTradeNo(closeOrderRequest.getParkCode(), closeOrderRequest.getTradeNo());
            if (Objects.isNull(accountRecord)){
                return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
            }
            RequestLogWithBLOBs requestLog = createRequestLog(accountRecord.getOutTradeNo(), PayCenterInterfaceEnum.CLOSE_ORDER.getCode());
            WxPayOrderCloseResult wxPayOrderCloseResult = wxPayService.closeOrder(accountRecord.getOutTradeNo());
            requestLog.setReturnResult(JsonTools.toString(wxPayOrderCloseResult));
            requestLogDao.insert(requestLog);
            return ResultTools.setResponse(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS),wxPayOrderCloseResult.getResultMsg());
        }catch (WxPayException e){
            return ResultTools.setResponse(CodeConstants.ERROR_1100, e.getReturnMsg());
        }catch (Exception e){
            return ResultTools.setResponse(CodeConstants.ERROR, CodeConstants.getName(CodeConstants.ERROR));
        }
    }
    /**
     * 构建微信统一下单请求数据
     * @param unifiedOrderRequest
     * @param wxPayConfig
     * @return
     */
    private WxPayUnifiedOrderRequest createUnifiedOrderRequest(UnifiedOrderRequest unifiedOrderRequest, WxPayConfig wxPayConfig, ParkWeixin parkWeixin) {
        String tradeType = wxPayConfig.getTradeType();
        // 支付金额,单位分
        Integer totalFee = Integer.valueOf(unifiedOrderRequest.getPrice());
        String deviceInfo = unifiedOrderRequest.getDeviceInfo();
        String body = unifiedOrderRequest.getProductInfo();
        String detail = null;
        String attach = null;
        String outTradeNo = unifiedOrderRequest.getParkCode()+unifiedOrderRequest.getTradeNo();
        String feeType = "CNY";
        String spBillCreateIP = unifiedOrderRequest.getSpbillCreateIp();
        String timeStart = null;
        String timeExpire = null;
        String goodsTag = null;
        String notifyUrl = wxPayConfig.getNotifyUrl()+"/"+unifiedOrderRequest.getParkCode();
        String productId = null;
        if(tradeType.equals(SelectTradeType.WxConstant.WX_NATIVE)) productId = UUIDTools.generateShortUuid();
        String limitPay = null;
        String openId = null;
        if(tradeType.equals(SelectTradeType.WxConstant.WX_JSAPI)) {
            if (StringUtils.isEmpty(unifiedOrderRequest.getWxCode())){
                openId = unifiedOrderRequest.getOpenId();
            }else {
                openId =  WeiXinCodeHandler.getWeiXinUserInfo(unifiedOrderRequest.getWxCode(), parkWeixin.getAppId(),parkWeixin.getAppSecret());
            }
        }
        String sceneInfo = null;
        if(tradeType.equals(SelectTradeType.WxConstant.WX_MWEB)) sceneInfo = unifiedOrderRequest.getSceneInfo();
        // 微信统一下单请求对象
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        request.setDeviceInfo(deviceInfo);
        request.setBody(body);
        request.setDetail(detail);
        request.setAttach(attach);
        request.setOutTradeNo(outTradeNo);
        request.setFeeType(feeType);
        request.setTotalFee(totalFee);
        request.setSpbillCreateIp(spBillCreateIP);
        request.setTimeStart(timeStart);
        request.setTimeExpire(timeExpire);
        request.setGoodsTag(goodsTag);
        request.setNotifyUrl(notifyUrl);
        request.setTradeType(tradeType);
        request.setProductId(productId);
        request.setLimitPay(limitPay);
        request.setOpenid(openId);
        request.setSceneInfo(sceneInfo);
        return request;
    }

    /**
     * 构建微信退款请求数据
     * @param accountRecord
     * @param refundOrder
     * @return
     */
    private WxPayRefundRequest createWxPayRefundRequest(AccountRecord accountRecord, RefundRequest refundOrder) {
        // 微信退款请求对象
        WxPayRefundRequest request = new WxPayRefundRequest();
        request.setTransactionId(accountRecord.getCenterSeqId());
        request.setOutTradeNo(refundOrder.getParkCode()+refundOrder.getTradeNo());
        request.setOutRefundNo(refundOrder.getRefundTradeNo());
        request.setRefundDesc(refundOrder.getOrderNote());
        request.setRefundFee(Integer.valueOf(refundOrder.getPrice()));
        request.setRefundFeeType("CNY");
        request.setTotalFee(accountRecord.getIncome());
        return request;
    }
    private RequestLogWithBLOBs createRequestLog(String context, String intefaceName) {
        RequestLogWithBLOBs requestLog = new RequestLogWithBLOBs();
        requestLog.setCreateTime(new Date());
        requestLog.setReqInterface(intefaceName);
        requestLog.setReqParams(context);
        return requestLog;
    }
}
