package com.icetech.api.datacenter.service.hystrix;

import com.icetech.api.datacenter.model.request.NoplateEnterRequest;
import com.icetech.api.datacenter.model.request.PrepayReportRequest;
import com.icetech.api.datacenter.service.NoplateEnterFeignApi;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.stereotype.Component;

@Component
public class NoplateEnterFeignApiHystrix implements NoplateEnterFeignApi {

    @Override
    public ObjectResponse noplateEnter(NoplateEnterRequest noplateEnterRequest) {
        return null;
    }
}
