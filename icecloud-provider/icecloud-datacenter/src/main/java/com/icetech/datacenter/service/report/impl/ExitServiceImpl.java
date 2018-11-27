package com.icetech.datacenter.service.report.impl;

import com.icetech.api.cloudcenter.model.request.ExitRequest;
import com.icetech.api.cloudcenter.service.OrderExitFeignApi;
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
public class ExitServiceImpl extends AbstractExitService implements ReportService {

    @Autowired
    private OrderExitFeignApi orderExitService;

    @Override
    public String report(DataCenterBaseRequest dataCenterBaseRequest, Long parkId) {
        ExitRequest exitRequest = DataChangeTools.convert2bean(dataCenterBaseRequest.getBizContent(), ExitRequest.class);

        /**
         * 参数校验
         */
        if (verifyParams(exitRequest)){

            exitRequest.setParkId(parkId);
            /**
             * 开始处理业务
             */
            try {
                ObjectResponse objectResponse = orderExitService.exit(exitRequest,dataCenterBaseRequest.getParkCode());
                if (objectResponse == null) {
                    return ResultTools.setResponse(CodeConstants.ERROR, CodeConstants.getName(CodeConstants.ERROR));
                } else {
                    return DataChangeTools.bean2gson(objectResponse);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResultTools.setResponse(CodeConstants.ERROR, CodeConstants.getName(CodeConstants.ERROR));
            }

        }else {
            return ResultTools.setResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
        }

    }

}
