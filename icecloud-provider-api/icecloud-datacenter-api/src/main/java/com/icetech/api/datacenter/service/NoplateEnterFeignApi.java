package com.icetech.api.datacenter.service;

import com.icetech.api.datacenter.model.request.NoplateEnterRequest;
import com.icetech.api.datacenter.service.hystrix.ExternalFeignApiHystrix;
import com.icetech.api.datacenter.service.hystrix.NoplateEnterFeignApiHystrix;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Description : 无牌车入场接口
 *  @author fangct
 */
@FeignClient(value = "icecloud-datacenter",fallback = NoplateEnterFeignApiHystrix.class)
public interface NoplateEnterFeignApi {

    @PostMapping(value = "/api/datacenter/noplateEnter")
    ObjectResponse noplateEnter(@RequestBody NoplateEnterRequest noplateEnterRequest);
}
