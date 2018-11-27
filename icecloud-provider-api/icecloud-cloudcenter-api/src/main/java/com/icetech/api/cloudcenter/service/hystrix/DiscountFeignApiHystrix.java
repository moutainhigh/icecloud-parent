package com.icetech.api.cloudcenter.service.hystrix;

import com.icetech.api.cloudcenter.service.DiscountFeignApi;
import com.icetech.common.domain.OrderDiscount;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.domain.response.PageQuery;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DiscountFeignApiHystrix implements DiscountFeignApi {

    @Override
    public ObjectResponse<List<OrderDiscount>> findList(PageQuery<OrderDiscount> pageQuery) {
        return null;
    }
}
