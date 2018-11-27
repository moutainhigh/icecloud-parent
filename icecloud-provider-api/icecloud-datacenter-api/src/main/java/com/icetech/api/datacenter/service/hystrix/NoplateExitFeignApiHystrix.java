package com.icetech.api.datacenter.service.hystrix;

import com.icetech.api.datacenter.model.request.NoplateExitRequest;
import com.icetech.api.datacenter.service.NoplateExitFeignApi;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.stereotype.Component;

@Component
public class NoplateExitFeignApiHystrix implements NoplateExitFeignApi {

    @Override
    public ObjectResponse noplateExit(NoplateExitRequest noplateExitRequest) {
        return null;
    }
}
