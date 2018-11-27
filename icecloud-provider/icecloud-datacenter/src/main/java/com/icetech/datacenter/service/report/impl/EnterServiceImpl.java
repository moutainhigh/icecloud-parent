package com.icetech.datacenter.service.report.impl;

import com.icetech.api.cloudcenter.model.request.EnterRequest;
import com.icetech.api.cloudcenter.service.OrderEnterFeignApi;
import com.icetech.common.DataChangeTools;
import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.datacenter.domain.request.DataCenterBaseRequest;
import com.icetech.datacenter.service.AbstractService;
import com.icetech.datacenter.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnterServiceImpl extends AbstractService implements ReportService {

    @Autowired
    private OrderEnterFeignApi orderEnterService;

    @Override
    public String report(DataCenterBaseRequest dataCenterBaseRequest, Long parkId) {
        EnterRequest enterRequest = DataChangeTools.convert2bean(dataCenterBaseRequest.getBizContent(), EnterRequest.class);

        //参数校验
        verifyParams(enterRequest);
        enterRequest.setParkId(parkId);

        /**
         * 开始处理业务
         */
        ObjectResponse objectResponse = orderEnterService.enter(enterRequest, dataCenterBaseRequest.getParkCode());
        if (objectResponse == null){
            return ResultTools.setResponse(CodeConstants.ERROR, CodeConstants.getName(CodeConstants.ERROR));
        }else{
            return DataChangeTools.bean2gson(objectResponse);
        }
    }

}
