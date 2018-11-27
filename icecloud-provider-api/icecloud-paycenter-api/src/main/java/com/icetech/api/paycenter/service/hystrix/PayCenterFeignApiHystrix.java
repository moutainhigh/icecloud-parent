package com.icetech.api.paycenter.service.hystrix;

import com.icetech.api.paycenter.model.request.*;
import com.icetech.api.paycenter.model.response.PayResultResponseDto;
import com.icetech.api.paycenter.model.response.UnifiedOrderResponseDto;
import com.icetech.api.paycenter.service.PayCenterFeignApi;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.stereotype.Component;

@Component
public class PayCenterFeignApiHystrix implements PayCenterFeignApi {
    @Override
    public ObjectResponse<UnifiedOrderResponseDto> doUnifiedOrder(UnifiedOrderRequestDto unifiedOrderRequest) {
        return null;
    }

    @Override
    public ObjectResponse autoPay(ExitpayRequestDto exitpayRequest) {
        return null;
    }

    @Override
    public ObjectResponse<PayResultResponseDto> getPayResult(PayResultRequestDto request, String tradeType) {
        return null;
    }

    @Override
    public ObjectResponse<String> autoPayEnterNotify(EnterNotifyRequestDto request) {
        return null;
    }

    @Override
    public ObjectResponse<String> autoPayExitNotify(ExitNotifyRequestDto request) {
        return null;
    }
}
