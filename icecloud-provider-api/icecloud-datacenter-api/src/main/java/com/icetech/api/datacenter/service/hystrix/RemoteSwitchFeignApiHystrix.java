package com.icetech.api.datacenter.service.hystrix;

import com.icetech.api.datacenter.model.request.RemoteSwitchRequest;
import com.icetech.api.datacenter.service.RemoteSwitchFeignApi;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.stereotype.Component;

@Component
public class RemoteSwitchFeignApiHystrix implements RemoteSwitchFeignApi {


    @Override
    public ObjectResponse remoteSwitch(RemoteSwitchRequest remoteSwitchRequest) {
        return null;
    }
}
