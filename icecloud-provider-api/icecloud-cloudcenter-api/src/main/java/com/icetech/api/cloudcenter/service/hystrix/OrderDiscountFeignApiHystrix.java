package com.icetech.api.cloudcenter.service.hystrix;


import com.icetech.api.cloudcenter.model.request.DiscountRequest;
import com.icetech.api.cloudcenter.service.OrderDiscountFeignApi;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderDiscountFeignApiHystrix implements OrderDiscountFeignApi {


    @Override
    public ObjectResponse modifyDiscount(DiscountRequest discountRequest) {
        return null;
    }

    @Override
    public ObjectResponse findDiscountNos(String tradeNo, Long parkId) {
        return null;
    }
}
