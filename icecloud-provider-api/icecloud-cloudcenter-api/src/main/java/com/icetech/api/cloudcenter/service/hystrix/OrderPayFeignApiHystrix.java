package com.icetech.api.cloudcenter.service.hystrix;

import com.icetech.api.cloudcenter.service.OrderPayFeignApi;
import com.icetech.common.domain.OrderPay;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.domain.response.PageQuery;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderPayFeignApiHystrix implements OrderPayFeignApi {


    @Override
    public ObjectResponse addOrderPay(OrderPay orderPay) {
        return null;
    }

    @Override
    public ObjectResponse<OrderPay> findOne(OrderPay orderPay) {
        return null;
    }

    @Override
    public ObjectResponse modifyOrderPay(OrderPay orderPay) {
        return null;
    }

    @Override
    public ObjectResponse<List<OrderPay>> findList(PageQuery<OrderPay> payPageQuery) {
        return null;
    }
}
