package com.icetech.api.transcenter.service.hystrix;

import com.icetech.api.taskcenter.service.SendInfoFeignApi;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.stereotype.Component;

@Component
public class SendInfoFeignApiHystrix implements SendInfoFeignApi {

    @Override
    public ObjectResponse notifySuccess(Integer serviceType, Integer serviceId) {
        return null;
    }
}
