package com.icetech.api.datacenter.service.hystrix;

import com.icetech.api.datacenter.model.request.PrepayReportRequest;
import com.icetech.api.datacenter.service.ExternalFeignApi;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.stereotype.Component;

@Component
public class ExternalFeignApiHystrix implements ExternalFeignApi {


    @Override
    public ObjectResponse prepayReport(PrepayReportRequest prepayReportRequest) {
        return null;
    }
}
