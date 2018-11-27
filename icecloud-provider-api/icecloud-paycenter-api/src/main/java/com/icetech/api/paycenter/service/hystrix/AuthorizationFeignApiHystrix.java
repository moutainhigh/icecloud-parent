package com.icetech.api.paycenter.service.hystrix;

import com.icetech.api.paycenter.model.request.PayCenterBaseRequestDto;
import com.icetech.api.paycenter.service.AuthorizationFeignApi;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationFeignApiHystrix implements AuthorizationFeignApi {
    @Override
    public ObjectResponse<String> getAuthLink4Wx(String parkCode) {
        return null;
    }

    @Override
    public ObjectResponse<String> getAuthLink4Ali(String parkCode) {
        return null;
    }

    @Override
    public ObjectResponse<String> getWxOpenId(String wxCode, String parkCode) {
        return null;
    }

    @Override
    public ObjectResponse<String> getAliUserId(String aliCode, String parkCode) {
        return null;
    }

    @Override
    public ObjectResponse<Boolean> verifySign(PayCenterBaseRequestDto baseRequest) {
        return null;
    }
}
