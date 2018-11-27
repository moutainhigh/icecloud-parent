package com.icetech.api.datacenter.service;

import com.icetech.api.datacenter.model.request.NoplateExitRequest;
import com.icetech.api.datacenter.service.hystrix.NoplateExitFeignApiHystrix;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Description : 无牌车离场接口
 *  @author fangct
 */
@FeignClient(value = "icecloud-datacenter",fallback = NoplateExitFeignApiHystrix.class)
public interface NoplateExitFeignApi {

    @PostMapping(value = "/api/datacenter/noplateEnter")
    ObjectResponse noplateExit(@RequestBody NoplateExitRequest noplateExitRequest);
}
