package com.icetech.paycenter.service.impl;

import com.icetech.common.DataChangeTools;
import com.icetech.common.DateTools;
import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.paycenter.domain.autopay.request.EnterNotifyRequest;
import com.icetech.paycenter.domain.autopay.request.ExitNotifyRequest;
import com.icetech.paycenter.domain.autopay.request.ExitpayRequest;
import com.icetech.paycenter.domain.autopay.response.UnionpayEnterNotifyResponse;
import com.icetech.paycenter.domain.autopay.response.UnionpayResponse;
import com.icetech.paycenter.domain.request.RefundRequest;
import com.icetech.paycenter.service.BaseUnionpayService;
import com.icetech.paycenter.service.IAutopayService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 银联无感支付
 *
 * @author fangct
 */
@Service
public class PayCenter4UnionServiceImpl extends BaseUnionpayService implements IAutopayService {

    @Override
    public String enterNotify(EnterNotifyRequest request) {

        String parkCode = request.getParkCode();
        Map<String, Object> nameValuePairs = new HashMap<>();
        nameValuePairs.put("RefNo", request.getOrderNum());
        nameValuePairs.put("CarNo", request.getPlateNum());
        nameValuePairs.put("InTime", request.getEnterTime());
        nameValuePairs.put("TransId", "40101");
        nameValuePairs.put("Version", "V1.0");

        String responseResult = sendHttp(nameValuePairs,parkCode, true);
        UnionpayEnterNotifyResponse unionpayEnterNotifyResponse = DataChangeTools.gson2bean(responseResult, UnionpayEnterNotifyResponse.class);

        if (unionpayEnterNotifyResponse != null) {
            return commonDealResult(unionpayEnterNotifyResponse);
        }else{
            return ResultTools.setResponse(CodeConstants.ERROR, CodeConstants.getName(CodeConstants.ERROR));
        }
    }

    @Override
    public String exitNotify(ExitNotifyRequest request) {

        String parkCode = request.getParkCode();
        Map<String, Object> nameValuePairs = new HashMap<>();
        nameValuePairs.put("RefNo", request.getOrderNum());
        nameValuePairs.put("CarNo", request.getPlateNum());
        nameValuePairs.put("ExitTime", request.getExitTime());
        nameValuePairs.put("TransId", "40201");
        nameValuePairs.put("Version", "V1.0");

        String responseResult = sendHttp(nameValuePairs,parkCode, true);
        UnionpayResponse unionpayResponse = DataChangeTools.gson2bean(responseResult, UnionpayResponse.class);

        if (unionpayResponse != null) {
            return commonDealResult(unionpayResponse);
        }else{
            return ResultTools.setResponse(CodeConstants.ERROR, CodeConstants.getName(CodeConstants.ERROR));
        }
    }

    @Override
    public String exitpay(ExitpayRequest request, String outTradeNo) {

        String parkCode = request.getParkCode();
        Map<String, Object> nameValuePairs = new HashMap<>();
        nameValuePairs.put("CarNo", request.getPlateNum());
        nameValuePairs.put("OutTradeNo", outTradeNo);
        nameValuePairs.put("MerDate", DateTools.getFormat(DateTools.DF_L, new Date()));
        nameValuePairs.put("InTime", request.getEnterTime());
        nameValuePairs.put("OutTime", request.getExitTime());
        nameValuePairs.put("Amount", request.getTotalPrice());
        nameValuePairs.put("PayAmount", request.getUnpayPrice());
//        nameValuePairs.put("NoticeUrl", request.getNotifyUrl());
        nameValuePairs.put("TransId", "40001");
        nameValuePairs.put("Version", "V1.0");

        String responseResult = sendHttp(nameValuePairs,parkCode, true);
        UnionpayResponse unionpayResponse = DataChangeTools.gson2bean(responseResult, UnionpayResponse.class);

        if (unionpayResponse != null) {
            return commonDealResult(unionpayResponse);
        }else{
            return ResultTools.setResponse(CodeConstants.ERROR, CodeConstants.getName(CodeConstants.ERROR));
        }
    }

    @Override
    public String refund(RefundRequest request, String outTradeNo, String merDate) {

        String parkCode = request.getParkCode();
        Map<String, Object> nameValuePairs = new HashMap<>();
        nameValuePairs.put("MerDate", merDate);
        nameValuePairs.put("OutTradeNo", outTradeNo);
        nameValuePairs.put("Remark", request.getOrderNote());
        nameValuePairs.put("TransId", "40301");
        nameValuePairs.put("Version", "V1.0");

        String responseResult = sendHttp(nameValuePairs,parkCode, true);
        UnionpayResponse unionpayResponse = DataChangeTools.gson2bean(responseResult, UnionpayResponse.class);

        if (unionpayResponse != null) {
            return commonDealResult(unionpayResponse);
        }else{
            return ResultTools.setResponse(CodeConstants.ERROR, CodeConstants.getName(CodeConstants.ERROR));
        }
    }

    @Override
    public String querySignedStatus(String plateNum, String parkCoe) {

        Map<String, Object> nameValuePairs = new HashMap<>();
        nameValuePairs.put("CarNo", plateNum);
        nameValuePairs.put("CarBusi", "01");
        nameValuePairs.put("TransId", "40401");
        nameValuePairs.put("Version", "V1.0");

        String responseResult = sendHttp(nameValuePairs,parkCoe, false);
        UnionpayResponse unionpayResponse = DataChangeTools.gson2bean(responseResult, UnionpayResponse.class);

        if (unionpayResponse != null) {
            return commonDealResult(unionpayResponse);
        }else{
            return ResultTools.setResponse(CodeConstants.ERROR, CodeConstants.getName(CodeConstants.ERROR));
        }
    }
}
