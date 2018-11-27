package com.icetech.api.datacenter.service;

import com.icetech.api.datacenter.model.request.SendRequest;
import com.icetech.api.datacenter.service.hystrix.SendFeignApiHystrix;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 下发接口
 * @author fangct
 */
@FeignClient(value = "icecloud-datacenter",fallback = SendFeignApiHystrix.class)
public interface SendFeignApi{

    @PostMapping(value = "/api/datacenter/send")
    ObjectResponse send(@RequestBody SendRequest sendRequest) ;

}
