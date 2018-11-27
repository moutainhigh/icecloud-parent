package com.icetech.paycenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.icetech.common.*;
import com.icetech.common.constants.CodeConstants;
import com.icetech.paycenter.common.config.CmbcConfig;
import com.icetech.paycenter.common.enumeration.CmbcResultCode;
import com.icetech.paycenter.common.enumeration.PayCenterInterfaceEnum;
import com.icetech.paycenter.common.enumeration.SelectTradeType;
import com.icetech.paycenter.domain.ParkCmbc;
import com.icetech.paycenter.domain.RequestLogWithBLOBs;
import com.icetech.paycenter.domain.normalpay.request.*;
import com.icetech.paycenter.domain.normalpay.response.CmbcCommonResponse;
import com.icetech.paycenter.domain.normalpay.response.CmbcPayResultReponse;
import com.icetech.paycenter.domain.normalpay.response.PayResultResponse;
import com.icetech.paycenter.domain.normalpay.response.UnifiedOrderResponse;
import com.icetech.paycenter.domain.request.PayResultRequest;
import com.icetech.paycenter.domain.request.RefundRequest;
import com.icetech.paycenter.mapper.RequestLogDao;
import com.icetech.paycenter.mapper.normalpay.ParkCmbcDao;
import com.icetech.paycenter.service.IPayCenterService;
import com.icetech.paycenter.service.handler.AliCodeHandler;
import com.icetech.paycenter.service.handler.CmbcSignEncryptDncryptSignChkHandler;
import com.icetech.paycenter.service.handler.WeiXinCodeHandler;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

import static com.icetech.paycenter.common.enumeration.SelectTradeType.getCmbcTradeType;


/**
 * 民生银行支付策略
 * @author wangzw
 */
@Service
public class PayCenter4CmbcServiceImpl implements IPayCenterService {
    private static final Logger logger = LoggerFactory.getLogger(PayCenter4CmbcServiceImpl.class);
    @Autowired
    private ParkCmbcDao parkCmbcDao;
    @Autowired
    private RequestLogDao requestLogDao;
    @Autowired
    private CmbcConfig cmbcConfig;
    @Autowired
    private Environment environment;
    @Override
    public String doUnifiedOrder(UnifiedOrderRequest unifiedOrderRequest){
        try {
            String YMD = DateTools.getFormat("yyyyMMdd",new Date());
            String ALL = DateTools.getFormat("yyyyMMddHHmmssSSS",new Date());
            //获取配置信息
            ParkCmbc parkCmbc = parkCmbcDao.selectByParkCode(unifiedOrderRequest.getParkCode());
            if (Objects.isNull(parkCmbc)){
                return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
            }
            //创建民生银行下单请求数据
            CmbcUnifiedOrderRequest cmbcUnifiedOrderRequest = getCmbcUnifiedOrderRequest(YMD, ALL, unifiedOrderRequest, parkCmbc);
            String context = JSON.toJSONString(cmbcUnifiedOrderRequest);
            RequestLogWithBLOBs requestLog = createRequestLog(context, PayCenterInterfaceEnum.UNIFIED_ORDER.getCode());
            //发起请求
            String response = doPost(context, parkCmbc,cmbcConfig.getIsTestEnv()?CmbcConfig.UNIFIED_ORDER_URL_DEV:CmbcConfig.UNIFIED_ORDER_URL);
            logger.info("【民生银行接口返回:】>>>> {}",response);
            requestLog.setReturnResult(response);
            requestLogDao.insert(requestLog);
            Check check = new Check(parkCmbc, response).invoke();
            CmbcCommonResponse cmbcCommonResponse = check.getCmbcCommonResponse();
            String dncryptContext = check.getDncryptContext();
            if (!check.isOk){
                return ResultTools.setResponse(CodeConstants.ERROR_1100, cmbcCommonResponse.getGateReturnMessage());
            }
            if (!CmbcResultCode.SUCCESS.getCode().equals(cmbcCommonResponse.getGateReturnType())){
                return ResultTools.setResponse(CodeConstants.ERROR_1100, cmbcCommonResponse.getGateReturnMessage());
            }
            JSONObject jsonObject = new JSONObject(dncryptContext);
            String result = jsonObject.get("body").toString();
            JSONObject jsonResult = new JSONObject(result);
            String payInfo = jsonResult.get("payInfo").toString();
            String tradeStatus = jsonResult.get("tradeStatus").toString();
            UnifiedOrderResponse unifiedOrderResponse = new UnifiedOrderResponse();
            unifiedOrderResponse.setPayInfo(payInfo);
            unifiedOrderResponse.setTradeStatus(tradeStatus);
            unifiedOrderResponse.setMapPayInfo(getPayInfo(payInfo,unifiedOrderRequest));
            return ResultTools.setResponse(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS),unifiedOrderResponse);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("【调用民生银行下单接口异常】>>>> {}",e);
            return ResultTools.setResponse(CodeConstants.ERROR, CodeConstants.getName(CodeConstants.ERROR));
        }
    }

    /**
     * 解析民生银行的支付要素
     * @param payInfoStr
     * @param unifiedOrderRequest
     * @return
     */
    private Map getPayInfo(String payInfoStr, UnifiedOrderRequest unifiedOrderRequest) {
        logger.info("【民生银行支付要素payInfo】>>>> {}",payInfoStr);
        Map<String, Object> map = new HashMap<>();
        map.put("payOrderId", unifiedOrderRequest.getTradeNo());
        //正扫返回二维码
        if (unifiedOrderRequest.getSelectTradeType().endsWith("RCODE")){
            map.put("payUrl",payInfoStr);
        }
        //微信公众号支付返回支付要素
        if (unifiedOrderRequest.getSelectTradeType().equals(SelectTradeType.CMBC_H5_WXJSAPI.getCode())){
            String[] split = payInfoStr.split("\\|");
            if (split.length>0){
                List<String> params = Arrays.asList(split);
                Map payInfo = Maps.newHashMap();
                params.forEach(param->{
                    String[] strings = param.split("=");
                    payInfo.put(strings[0],strings[1]);
                });
                map.put("payParams", payInfo);
            }
        }
        return map;
    }

    @Override
    public String doPayResult(PayResultRequest payResultRequest) {
        try {
            //获取配置信息
            ParkCmbc parkCmbc = parkCmbcDao.selectByParkCode(payResultRequest.getParkCode());
            if (Objects.isNull(parkCmbc)){
                return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
            }
            //创建民生银行查询请求
            CmbcPayResultRequest cmbcPayResultRequest = getCmbcPayResultRequest(payResultRequest, parkCmbc);
            //生成请求json串
            String context = JSON.toJSONString(cmbcPayResultRequest);
            RequestLogWithBLOBs requestLog = createRequestLog(context,PayCenterInterfaceEnum.QUERY_ORDER.getCode());
            String response = doPost(context, parkCmbc,cmbcConfig.getIsTestEnv()?CmbcConfig.PAY_RESULT_URL_DEV:CmbcConfig.PAY_RESULT_URL);
            requestLog.setReturnResult(response);
            requestLogDao.insert(requestLog);
            Check check = new Check(parkCmbc, response).invoke();
            CmbcCommonResponse cmbcCommonResponse = check.getCmbcCommonResponse();
            String dncryptContext = check.getDncryptContext();
            if (!check.getIsOk()){
                return ResultTools.setResponse(CodeConstants.ERROR_1100, CodeConstants.getName(CodeConstants.ERROR_401));
            }
            if (!CmbcResultCode.SUCCESS.getCode().equals(cmbcCommonResponse.getGateReturnType())){
                return ResultTools.setResponse(CodeConstants.ERROR_1100, cmbcCommonResponse.getGateReturnMessage());
            }
            //对成功结果进行解析
            JSONObject jsonObject = new JSONObject(dncryptContext);
            String body = jsonObject.get("body").toString();
            CmbcPayResultReponse cmbcPayResultReponse = JsonTools.toBean(body, CmbcPayResultReponse.class);
            PayResultResponse payResultResponse = new PayResultResponse();
            payResultResponse.setPrice(cmbcPayResultReponse.getAmount());
            payResultResponse.setTradeStatus(cmbcPayResultReponse.getTradeStatus());
            payResultResponse.setTradeType("CMBC-"+cmbcPayResultReponse.getTradeType());
            return ResultTools.setResponse(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS),payResultResponse);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("【调用民生银行查询接口异常】>>>>,{}",e);
            return ResultTools.setResponse(CodeConstants.ERROR, CodeConstants.getName(CodeConstants.ERROR));
        }
    }


    @Override
    public String doRefund(RefundRequest refundRequest) {
        try {
            //获取配置信息
            ParkCmbc parkCmbc = parkCmbcDao.selectByParkCode(refundRequest.getParkCode());
            if (Objects.isNull(parkCmbc)){
                return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
            }
            //创建民生银行退款请求
            CmbcRefundRequest cmbcRefundRequest = getCmbcRefundRequest(refundRequest, parkCmbc);
            //生成请求json串
            String context = JSON.toJSONString(cmbcRefundRequest);
            RequestLogWithBLOBs requestLog = createRequestLog(context,PayCenterInterfaceEnum.REFUND.getCode());
            String response = doPost(context, parkCmbc,cmbcConfig.getIsTestEnv()?CmbcConfig.REFUND_URL_DEV:CmbcConfig.REFUND_URL);
            Check check = new Check(parkCmbc, response).invoke();
            requestLog.setReturnResult(response);
            requestLogDao.insert(requestLog);
            CmbcCommonResponse cmbcCommonResponse = check.getCmbcCommonResponse();
            if (!check.getIsOk()){
                return ResultTools.setResponse(CodeConstants.ERROR_1100, cmbcCommonResponse.getGateReturnMessage());
            }
            if (!CmbcResultCode.SUCCESS.getCode().equals(cmbcCommonResponse.getGateReturnType())){
                return ResultTools.setResponse(CodeConstants.ERROR_1100, cmbcCommonResponse.getGateReturnMessage());
            }
            return ResultTools.setResponse(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("【调用民生银行退款接口异常】>>>>,{}",e);
            return ResultTools.setResponse(CodeConstants.ERROR, CodeConstants.getName(CodeConstants.ERROR));
        }
    }

    @Override
    public String downloadTransactionDetails(TransactionDetailsDownloadRequest downloadRequest) {
        try {
            //获取配置信息
            ParkCmbc parkCmbc = parkCmbcDao.selectByParkCode(downloadRequest.getParkCode());
            if (Objects.isNull(parkCmbc)){
                return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
            }
            //第一次请求
            String context = com.alibaba.fastjson.JSONObject.toJSONString(downloadRequest);
            String response = doPost(context, parkCmbc, cmbcConfig.getIsTestEnv()?CmbcConfig.DOWNLOAD_URL_DEV:CmbcConfig.DOWNLOAD_URL);
            Check check = new Check(parkCmbc, response).invoke();
            CmbcCommonResponse cmbcCommonResponse = check.getCmbcCommonResponse();
            if (!check.getIsOk()){
                return ResultTools.setResponse(CodeConstants.ERROR_401, CodeConstants.getName(CodeConstants.ERROR_401));
            }
            String businessContext = cmbcCommonResponse.getBusinessContext();
            JSONObject jsonObject = new JSONObject(businessContext);
            //获取所有文件的总块数
            String segmentCount = jsonObject.get("segmentCount").toString();
            Integer integer = Integer.valueOf(segmentCount);
            String fileName = downloadRequest.getPlatformId();
            FileWriter fileWriter = new FileWriter(new File(cmbcConfig.getFilePath()+File.separator+fileName));
            for (int i = 1;i<=integer;i++){
                //循环去调用文件下载
                downloadRequest.setSegmentIndex(i+"");
                context = com.alibaba.fastjson.JSONObject.toJSONString(downloadRequest);
                response = doPost(context, parkCmbc, cmbcConfig.getIsTestEnv()?CmbcConfig.DOWNLOAD_URL_DEV:CmbcConfig.DOWNLOAD_URL);
                check = new Check(parkCmbc, response).invoke();
                cmbcCommonResponse = check.getCmbcCommonResponse();
                if (!check.getIsOk()){
                    return ResultTools.setResponse(CodeConstants.ERROR_401, CodeConstants.getName(CodeConstants.ERROR_401));
                }
                businessContext = cmbcCommonResponse.getBusinessContext();
                jsonObject = new JSONObject(businessContext);
                //获取文件的内容
                segmentCount = jsonObject.get("segmentContent").toString();
                //解密
                Base64Tools.decode2String(segmentCount);
                fileWriter.write(segmentCount);
                fileWriter.flush();
            }
            //关闭流
            fileWriter.close();
            return ResultTools.setResponse(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("【调用民生银行下载明细单接口异常】>>>>,{}",e);
            return ResultTools.setResponse(CodeConstants.ERROR, CodeConstants.getName(CodeConstants.ERROR));
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------
    //----------------------------------------------   私有方法   -------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------

    private CmbcRefundRequest getCmbcRefundRequest(RefundRequest refundRequest, ParkCmbc parkCmbc) {
        CmbcRefundRequest cmbcRefundRequest = new CmbcRefundRequest();
        cmbcRefundRequest.setPlatformId(parkCmbc.getPlatformId());
        cmbcRefundRequest.setMerchantNo(parkCmbc.getMerchantNo());
        cmbcRefundRequest.setMerchantSeq(refundRequest.getParkCode()+refundRequest.getTradeNo());
        cmbcRefundRequest.setOrderAmount(refundRequest.getPrice());
        cmbcRefundRequest.setOrderNote(refundRequest.getOrderNote());
        cmbcRefundRequest.setReserve("");
        return cmbcRefundRequest;
    }

    private CmbcPayResultRequest getCmbcPayResultRequest(PayResultRequest payResultRequest, ParkCmbc parkCmbc) {
        CmbcPayResultRequest cmbcPayResultRequest = new CmbcPayResultRequest();
        cmbcPayResultRequest.setMerchantNo(parkCmbc.getMerchantNo());
        cmbcPayResultRequest.setMerchantSeq(payResultRequest.getParkCode()+payResultRequest.getTradeNo());
        cmbcPayResultRequest.setPlatformId(parkCmbc.getPlatformId());
        //此时 1 为查询;
        cmbcPayResultRequest.setTradeType("1");
        cmbcPayResultRequest.setReserve("");
        cmbcPayResultRequest.setOrgvoucherNo("");
        return cmbcPayResultRequest;
    }

    private CmbcUnifiedOrderRequest getCmbcUnifiedOrderRequest(String YMD, String ALL, UnifiedOrderRequest unifiedOrderRequest, ParkCmbc parkCmbc) {
        CmbcUnifiedOrderRequest cmbcUnifiedOrderRequest = new CmbcUnifiedOrderRequest();
        cmbcUnifiedOrderRequest.setPlatformId(parkCmbc.getPlatformId());
        cmbcUnifiedOrderRequest.setMerchantNo(parkCmbc.getMerchantNo());
        cmbcUnifiedOrderRequest.setMchSeqNo(unifiedOrderRequest.getTradeNo());
        cmbcUnifiedOrderRequest.setMerchantSeq(parkCmbc.getParkCode()+unifiedOrderRequest.getTradeNo());
        cmbcUnifiedOrderRequest.setSelectTradeType(getCmbcTradeType(unifiedOrderRequest.getSelectTradeType()));
        cmbcUnifiedOrderRequest.setAmount(unifiedOrderRequest.getPrice());
        cmbcUnifiedOrderRequest.setOrderInfo(unifiedOrderRequest.getProductInfo());
        //反扫(需传递付款码)
        if (unifiedOrderRequest.getSelectTradeType().endsWith("SCAN") && StringUtils.isNotBlank(unifiedOrderRequest.getPayCode())){
            //反扫时提供的支付码的BASE64 编码
            String base64Code = Base64Tools.encode2String(unifiedOrderRequest.getPayCode());
            cmbcUnifiedOrderRequest.setRemark(base64Code);
        }
        //支付宝服务号
        if (unifiedOrderRequest.getSelectTradeType().equals(SelectTradeType.CMBC_H5_ZFBJSAPI.getCode())
                || unifiedOrderRequest.getSelectTradeType().equals(SelectTradeType.CmbcConstant.CMBC_H5_ZFBJSAPI)) {
            String accAndUserid;
            if (StringUtils.isNotBlank(unifiedOrderRequest.getAliCode())) {
                accAndUserid= AliCodeHandler.getAccAndUserid(unifiedOrderRequest.getAliCode(), parkCmbc.getAliAppId(), parkCmbc.getAliPrivateKey(), parkCmbc.getAliPublicKey());
            }else {
                accAndUserid = unifiedOrderRequest.getUserId();
            }
            //获取支付宝用户标识
            cmbcUnifiedOrderRequest.setUserId(accAndUserid);
        }
        //微信公众号
        if (unifiedOrderRequest.getSelectTradeType().equals(SelectTradeType.CMBC_H5_WXJSAPI.getCode())
                ||unifiedOrderRequest.getSelectTradeType().equals(SelectTradeType.CmbcConstant.CMBC_H5_WXJSAPI)){
            String openId;
            if (StringUtils.isNotBlank(unifiedOrderRequest.getWxCode())){
                openId =  WeiXinCodeHandler.getWeiXinUserInfo(unifiedOrderRequest.getWxCode(), parkCmbc.getWxAppId(),parkCmbc.getWxAppSecret());
            }else {
                openId = unifiedOrderRequest.getOpenId();
            }
            //获取微信的 appid 和 openid
            cmbcUnifiedOrderRequest.setSubAppId(parkCmbc.getWxAppId());
            cmbcUnifiedOrderRequest.setSubOpenId(openId);
        }
       /* //微信小程序和微信H5支付
        if (unifiedOrderRequest.getSelectTradeType().equals(SelectTradeType.wx.getCode())
                || unifiedOrderRequest.getSelectTradeType().equals(SelectTradeType.API_WXAPP.getCode())
                && StringUtils.isNotBlank(unifiedOrderRequest.getWxCode())){
            //设置 Ip
            cmbcUnifiedOrderRequest.setSpbillCreateIp(unifiedOrderRequest.getSpbillCreateIp());
        }*/
        //支付中心的异步通知 url
        cmbcUnifiedOrderRequest.setNotifyUrl(cmbcConfig.getBaseNotifyUrl()+"/"+unifiedOrderRequest.getParkCode());
        cmbcUnifiedOrderRequest.setTransDate(YMD);
        cmbcUnifiedOrderRequest.setTransTime(ALL);
        cmbcUnifiedOrderRequest.setDeviceInfo(unifiedOrderRequest.getDeviceInfo());
        cmbcUnifiedOrderRequest.setInExtData("请求扩展大字段");
        return cmbcUnifiedOrderRequest;
    }

    private CmbcCommonRequest getCmbcCommonRequest(String encryptContext) {
        CmbcCommonRequest cmbcCommonRequest = new CmbcCommonRequest();
        cmbcCommonRequest.setBusinessContext(encryptContext);
        cmbcCommonRequest.setVersion("");
        cmbcCommonRequest.setMerchantNo("");
        cmbcCommonRequest.setMerchantSeq("");
        cmbcCommonRequest.setTransDate("");
        cmbcCommonRequest.setTransTime("");
        cmbcCommonRequest.setTransCode("");
        cmbcCommonRequest.setSecurityType("");
        cmbcCommonRequest.setReserve1("");
        cmbcCommonRequest.setReserve2("");
        cmbcCommonRequest.setReserve3("");
        cmbcCommonRequest.setReserve4("");
        cmbcCommonRequest.setReserve5("");
        cmbcCommonRequest.setReserveJson("");
        cmbcCommonRequest.setSource("");
        cmbcCommonRequest.setSessionId("");
        return cmbcCommonRequest;
    }

    private RequestLogWithBLOBs createRequestLog(String context, String intefaceName) {
        RequestLogWithBLOBs requestLog = new RequestLogWithBLOBs();
        requestLog.setCreateTime(new Date());
        requestLog.setReqInterface(intefaceName);
        requestLog.setReqParams(JsonTools.toString(context));
        return requestLog;
    }

    private String doPost(String context, ParkCmbc parkCmbc, String url){
        //获取签名
        String sign = CmbcSignEncryptDncryptSignChkHandler.getSign(context, parkCmbc.getMerchantPrivateKeyPath(), parkCmbc.getPrivateKey());
        //拼接签名和Json串
        String signContext = CmbcSignEncryptDncryptSignChkHandler.sign(sign, context);
        //加密
        String encryptContext = CmbcSignEncryptDncryptSignChkHandler.encrypt(signContext, parkCmbc.getCmbcPublicKeyPath());
        //生成请求报文
        CmbcCommonRequest cmbcCommonRequest = getCmbcCommonRequest(encryptContext);
        //发送请求
        String response = HttpTools.postJson(url, JSON.toJSONString(cmbcCommonRequest));
        return response;
    }

    @Data
    private class Check {
        private ParkCmbc parkCmbc;
        private String response;
        private CmbcCommonResponse cmbcCommonResponse;
        private String dncryptContext;
        private Boolean isOk;

        public Check(ParkCmbc parkCmbc, String response) {
            this.parkCmbc = parkCmbc;
            this.response = response;
        }

        public Check invoke() {
            cmbcCommonResponse = JsonTools.toBean(response, CmbcCommonResponse.class);
            if (cmbcCommonResponse.getGateReturnType().equals(CmbcResultCode.ERROR.getCode())){
                isOk = Boolean.FALSE;
                return this;
            }
            //获取返回加密数据
            String businessContext = cmbcCommonResponse.getBusinessContext();
            //进行解密
            dncryptContext = CmbcSignEncryptDncryptSignChkHandler.dncrypt(businessContext, parkCmbc.getMerchantPrivateKeyPath(), parkCmbc.getPrivateKey());
            //将解密后的数据重新赋值
            cmbcCommonResponse.setBusinessContext(dncryptContext);
            //验证签名
            isOk = CmbcSignEncryptDncryptSignChkHandler.signCheck(dncryptContext, parkCmbc.getCmbcPublicKeyPath());
            return this;
        }
    }
}
