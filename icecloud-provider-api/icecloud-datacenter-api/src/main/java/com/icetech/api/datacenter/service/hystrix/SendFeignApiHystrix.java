package com.icetech.api.datacenter.service.hystrix;

import com.icetech.api.datacenter.model.request.SendRequest;
import com.icetech.api.datacenter.service.SendFeignApi;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.stereotype.Component;

@Component
public class SendFeignApiHystrix implements SendFeignApi {

    @Override
    public ObjectResponse send(SendRequest sendRequest) {
        return null;
    }
}
