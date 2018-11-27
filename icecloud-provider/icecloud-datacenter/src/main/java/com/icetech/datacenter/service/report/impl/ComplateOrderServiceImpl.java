package com.icetech.datacenter.service.report.impl;

import com.icetech.api.cloudcenter.model.request.ComplateOrderRequest;
import com.icetech.api.cloudcenter.service.ComplateOrderFeignApi;
import com.icetech.common.DataChangeTools;
import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.datacenter.domain.request.DataCenterBaseRequest;
import com.icetech.datacenter.service.report.AbstractExitService;
import com.icetech.datacenter.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplateOrderServiceImpl extends AbstractExitService implements ReportService {

    @Autowired
    private ComplateOrderFeignApi complateOrderService;

    @Override
    public String report(DataCenterBaseRequest dataCenterBaseRequest, Long parkId) {
        ComplateOrderRequest complateOrderRequest = DataChangeTools.convert2bean(dataCenterBaseRequest.getBizContent(), ComplateOrderRequest.class);
        complateOrderRequest.setParkId(parkId);
        /**
         * 参数校验
         */
        if (verifyParams(complateOrderRequest)){
            ObjectResponse objectResponse = complateOrderService.complateOrder(complateOrderRequest);
            if (objectResponse == null) {
                return ResultTools.setResponse(CodeConstants.ERROR, CodeConstants.getName(CodeConstants.ERROR));
            } else {
                return DataChangeTools.bean2gson(objectResponse);
            }
        }else{
            return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
        }

    }

}
