package com.icetech.paycenter.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.icetech.common.JsonTools;
import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.paycenter.common.config.AliConfig;
import com.icetech.paycenter.common.enumeration.PayCenterInterfaceEnum;
import com.icetech.paycenter.common.enumeration.ResponseTradeStatus;
import com.icetech.paycenter.common.enumeration.SelectTradeType;
import com.icetech.paycenter.domain.ParkAlipay;
import com.icetech.paycenter.domain.RequestLogWithBLOBs;
import com.icetech.paycenter.domain.normalpay.request.TransactionDetailsDownloadRequest;
import com.icetech.paycenter.domain.normalpay.request.UnifiedOrderRequest;
import com.icetech.paycenter.domain.normalpay.response.PayResultResponse;
import com.icetech.paycenter.domain.request.PayResultRequest;
import com.icetech.paycenter.domain.request.RefundRequest;
import com.icetech.paycenter.mapper.ParkAlipayDao;
import com.icetech.paycenter.mapper.RequestLogDao;
import com.icetech.paycenter.service.BasePayCenter4AliService;
import com.icetech.paycenter.service.IAliPayCenterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

import static com.icetech.paycenter.common.enumeration.ResponseTradeStatus.getStatus4AliStatus;


/**
 * 支付宝支付中心对接服务类
 * @author wangzw
 */
@Service
public class PayCenter4AliServiceImpl implements IAliPayCenterService {
    private static final Logger logger = LoggerFactory.getLogger(PayCenter4AliServiceImpl.class);
    @Autowired
    private ParkAlipayDao parkAlipayDao;
    @Autowired
    private BasePayCenter4AliService payCenter4AliService;
    @Autowired
    private AliConfig aliConfig;
    @Autowired
    private RequestLogDao requestLogDao;
    @Override
    public String doUnifiedOrder(UnifiedOrderRequest orderRequest) {
        //获取支付宝配置
        ParkAlipay parkAlipay = parkAlipayDao.selectByParkCode(orderRequest.getParkCode());
        if (Objects.isNull(parkAlipay)){
            return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));

        }
        String result;
            switch (orderRequest.getSelectTradeType()){
            case SelectTradeType.AliConstant.ALI_MOBILE:{
                result = payCenter4AliService.doAliPayAppReq(orderRequest, parkAlipay);
                break;
            }
            case SelectTradeType.AliConstant.ALI_PC:{
                result = payCenter4AliService.doAliPayPcReq(orderRequest, parkAlipay);
                break;
            }
            case SelectTradeType.AliConstant.ALI_WAP:{
                result = payCenter4AliService.doAliPayWapReq(orderRequest, parkAlipay);
                break;
            }
            case SelectTradeType.AliConstant.ALI_QR:{
                result = payCenter4AliService.doAliPayQrReq(orderRequest, parkAlipay);
                break;
            }
            default:
                result = ResultTools.setResponse(CodeConstants.ERROR,CodeConstants.getName(CodeConstants.ERROR));
        }

        return result;
    }

    @Override
    public String doPayResult(PayResultRequest payResultRequest) {
        //获取支付宝配置
        ParkAlipay parkAlipay = parkAlipayDao.selectByParkCode(payResultRequest.getParkCode());
        if (Objects.isNull(parkAlipay)){
            return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));

        }
        AlipayClient client = new DefaultAlipayClient(aliConfig.getIsSandbox()?aliConfig.DEV_URL:aliConfig.URL, parkAlipay.getAppId(), parkAlipay.getPrivateKey(), aliConfig.FORMAT, aliConfig.CHARSET, parkAlipay.getAliPublicKey(), aliConfig.SIGNTYPE);
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(payResultRequest.getParkCode()+payResultRequest.getTradeNo());
        request.setBizModel(model);
        AlipayTradeQueryResponse queryResponse;
        try {
            RequestLogWithBLOBs requestLog = createRequestLog(JsonTools.toString(request), PayCenterInterfaceEnum.QUERY_ORDER.getCode());
            queryResponse = client.execute(request);
            requestLog.setReturnResult(JsonTools.toString(queryResponse));
            requestLogDao.insert(requestLog);
        } catch (AlipayApiException e) {
            logger.error("【支付宝查询异常】>>>> 信息:",e.getErrMsg());
            return ResultTools.setResponse(CodeConstants.ERROR_1100, "支付宝查询订单异常");
        }
        PayResultResponse payResultResponse = new PayResultResponse();
        payResultResponse.setPrice(queryResponse.getPayAmount());
        if (queryResponse.getCode().equals("10000")){
            payResultResponse.setTradeStatus(getStatus4AliStatus(queryResponse.getTradeStatus()));
            return ResultTools.setResponse(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS),payResultResponse);
        }
        payResultResponse.setTradeStatus(ResponseTradeStatus.未支付待支付.getCode());
        return ResultTools.setResponse(CodeConstants.ERROR_1100, queryResponse.getMsg());
    }

    @Override
    public String doRefund(RefundRequest refundRequest) {
        //获取支付宝配置
        ParkAlipay parkAlipay = parkAlipayDao.selectByParkCode(refundRequest.getParkCode());
        if (Objects.isNull(parkAlipay)){
            return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));

        }
        AlipayClient client = new DefaultAlipayClient(aliConfig.getIsSandbox()?aliConfig.DEV_URL:aliConfig.URL, parkAlipay.getAppId(), parkAlipay.getPrivateKey(), aliConfig.FORMAT, aliConfig.CHARSET, parkAlipay.getAliPublicKey(), aliConfig.SIGNTYPE);
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(refundRequest.getParkCode()+refundRequest.getTradeNo());
        model.setRefundAmount(Double.valueOf(refundRequest.getPrice())/100+"");
        model.setRefundReason(refundRequest.getOrderNote());
        request.setBizModel(model);
        AlipayTradeRefundResponse refundResponse;
        try {
            RequestLogWithBLOBs requestLog = createRequestLog(JsonTools.toString(request), PayCenterInterfaceEnum.REFUND.getCode());
            refundResponse = client.execute(request);
            requestLog.setReturnResult(JsonTools.toString(refundResponse));
            requestLogDao.insert(requestLog);
        } catch (AlipayApiException e) {
            logger.error("【支付宝退款异常】>>>> 信息:",e.getErrMsg());
            return ResultTools.setResponse(CodeConstants.ERROR_1100, e.getErrMsg());
        }
        if (refundResponse.getCode() == null||refundResponse.getCode().equals("10000")){
            return ResultTools.setResponse(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS));
        }
        return ResultTools.setResponse(CodeConstants.ERROR_1100, refundResponse.getMsg());
    }

    @Override
    public String downloadTransactionDetails(TransactionDetailsDownloadRequest downloadRequest) {
        return null;
    }

    private RequestLogWithBLOBs createRequestLog(String context, String intefaceName) {
        RequestLogWithBLOBs requestLog = new RequestLogWithBLOBs();
        requestLog.setCreateTime(new Date());
        requestLog.setReqInterface(intefaceName);
        requestLog.setReqParams(context);
        return requestLog;
    }
}
