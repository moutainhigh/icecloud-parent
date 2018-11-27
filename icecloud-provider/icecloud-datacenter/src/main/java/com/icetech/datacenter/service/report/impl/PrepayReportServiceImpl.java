package com.icetech.datacenter.service.report.impl;

import com.icetech.api.datacenter.model.request.PrepayReportRequest;
import com.icetech.api.datacenter.service.ExternalFeignApi;
import com.icetech.common.DataChangeTools;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.datacenter.domain.request.DataCenterBaseRequest;
import com.icetech.datacenter.service.AbstractService;
import com.icetech.datacenter.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrepayReportServiceImpl extends AbstractService implements ReportService {

    @Autowired
    private ExternalFeignApi externalService;

    @Override
    public String report(DataCenterBaseRequest dataCenterBaseRequest, Long parkId) {
        PrepayReportRequest prepayReportRequest = DataChangeTools.convert2bean(dataCenterBaseRequest.getBizContent(), PrepayReportRequest.class);
        //参数校验
        verifyParams(prepayReportRequest);

        prepayReportRequest.setParkId(parkId);
        ObjectResponse objectResponse = externalService.prepayReport(prepayReportRequest);
        return DataChangeTools.bean2gson(objectResponse);

    }

}
