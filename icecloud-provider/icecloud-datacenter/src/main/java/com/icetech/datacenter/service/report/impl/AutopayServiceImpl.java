package com.icetech.datacenter.service.report.impl;

import com.icetech.api.cloudcenter.service.OrderFeignApi;
import com.icetech.api.cloudcenter.service.OrderPayFeignApi;
import com.icetech.api.paycenter.model.request.ExitpayRequestDto;
import com.icetech.api.paycenter.service.PayCenterFeignApi;
import com.icetech.common.*;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.constants.PayStatusConstants;
import com.icetech.common.domain.OrderInfo;
import com.icetech.common.domain.OrderPay;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.util.ResponseUtils;
import com.icetech.datacenter.domain.request.AutopayRequest;
import com.icetech.datacenter.domain.request.DataCenterBaseRequest;
import com.icetech.datacenter.service.AbstractService;
import com.icetech.datacenter.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AutopayServiceImpl extends AbstractService implements ReportService {

    @Autowired
    private PayCenterFeignApi payCenterService;

    @Autowired
    private OrderFeignApi orderService;

    @Autowired
    private OrderPayFeignApi orderPayService;

    @Override
    public String report(DataCenterBaseRequest dataCenterBaseRequest, Long parkId) {

        AutopayRequest autopayRequest = DataChangeTools.convert2bean(dataCenterBaseRequest.getBizContent(), AutopayRequest.class);

        //参数校验
        verifyParams(autopayRequest);

        /**
         * 开始处理业务
         */
        ExitpayRequestDto exitpayRequest = getExitpayRequest(dataCenterBaseRequest, autopayRequest);
        logger.info("准备请求扣费，参数：{}", exitpayRequest);
        ObjectResponse objectResponse1 = payCenterService.autoPay(exitpayRequest);
        String tradeNo = CodeTools.GenerateTradeNo();
        savePayRecord(autopayRequest,parkId, tradeNo);
        if (objectResponse1 != null && objectResponse1.getCode().equals(CodeConstants.SUCCESS)){
            Map<String, Object> retMap = new HashMap<String, Object>();

            retMap.put("tradeNo", tradeNo);
            retMap.put("payWay", 4);
            return ResultTools.setResponse(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS),retMap);

        }
        return DataChangeTools.bean2gson(objectResponse1);
    }

    /**
     * 保存交易记录
     * @param autopayRequest
     * @param tradeNo
     */
    private void savePayRecord(AutopayRequest autopayRequest, Long parkId, String tradeNo) {

        OrderPay orderPay = new OrderPay();
        orderPay.setOrderNum(autopayRequest.getOrderNum());
        orderPay.setPayStatus(PayStatusConstants.PAID);
        orderPay.setParkId(parkId);
        orderPay.setDiscountPrice((String.valueOf(ToolsUtil.parseDouble(autopayRequest.getTotalPrice()) - ToolsUtil.parseDouble(autopayRequest.getPaidPrice()))));
        orderPay.setPaidPrice(autopayRequest.getPaidPrice());
        //无感支付
        orderPay.setPayChannel(7);
        orderPay.setPayTerminal("银联无感支付");
        orderPay.setPayTime(autopayRequest.getPayTime());
        //银联
        orderPay.setPayWay(4);
        orderPay.setTotalPrice(autopayRequest.getTotalPrice());
        orderPay.setTradeNo(tradeNo);
        orderPayService.addOrderPay(orderPay);
    }

    private ExitpayRequestDto getExitpayRequest(DataCenterBaseRequest dataCenterBaseRequest, AutopayRequest autopayRequest) {
        ExitpayRequestDto exitpayRequest = new ExitpayRequestDto();
        exitpayRequest.setParkCode(dataCenterBaseRequest.getParkCode());

        String tradeNo = CodeTools.GenerateTradeNo();
        exitpayRequest.setTradeNo(tradeNo);

        ObjectResponse<OrderInfo> objectResponse = orderService.findByOrderNum(autopayRequest.getOrderNum());
        ResponseUtils.notError(objectResponse);
        OrderInfo orderInfo = objectResponse.getData();
        exitpayRequest.setEnterTime(DateTools.secondTostring(orderInfo.getEnterTime().intValue()));
        exitpayRequest.setUnpayPrice(MoneyTool.fromYuanToFen(autopayRequest.getUnpayPrice()));
        exitpayRequest.setTotalPrice(MoneyTool.fromYuanToFen(autopayRequest.getTotalPrice()));
        exitpayRequest.setExitTime(DateTools.secondTostring(autopayRequest.getPayTime().intValue()));
        exitpayRequest.setPlateNum(autopayRequest.getPlateNum());
        return exitpayRequest;
    }

}
