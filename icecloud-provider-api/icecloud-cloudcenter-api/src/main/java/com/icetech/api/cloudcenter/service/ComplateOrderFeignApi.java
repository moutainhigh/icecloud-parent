package com.icetech.api.cloudcenter.service;

import com.icetech.api.cloudcenter.model.request.ComplateOrderRequest;
import com.icetech.api.cloudcenter.service.hystrix.ComplateOrderFeignApiHystrix;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Description : 完整订单记录服务接口
 * @author fangct
 */
@FeignClient(value = "icecloud-cloudcenter",fallback = ComplateOrderFeignApiHystrix.class)
public interface ComplateOrderFeignApi {

    /**
     * 完整订单记录上报业务
     * @param complateOrderRequest
     * @return
     */
    @PostMapping(value = "/api/cloudcenter/complateOrder")
    ObjectResponse complateOrder(ComplateOrderRequest complateOrderRequest);

}
