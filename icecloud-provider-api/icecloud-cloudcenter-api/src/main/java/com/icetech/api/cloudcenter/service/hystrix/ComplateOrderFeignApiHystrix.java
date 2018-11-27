package com.icetech.api.cloudcenter.service.hystrix;

import com.icetech.api.cloudcenter.model.request.ComplateOrderRequest;
import com.icetech.api.cloudcenter.service.ComplateOrderFeignApi;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.stereotype.Component;

/**
 * Description : 完整订单记录服务接口
 * @author fangct
 */
@Component
public class ComplateOrderFeignApiHystrix implements ComplateOrderFeignApi {


    @Override
    public ObjectResponse complateOrder(ComplateOrderRequest complateOrderRequest) {
        return null;
    }
}
