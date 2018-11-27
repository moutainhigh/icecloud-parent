package com.icetech.api.datacenter.service;

import com.icetech.api.datacenter.model.request.RemoteSwitchRequest;
import com.icetech.api.datacenter.service.hystrix.RemoteSwitchFeignApiHystrix;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Description : 远程开关闸接口
 *  @author fangct
 */
@FeignClient(value = "icecloud-datacenter",fallback = RemoteSwitchFeignApiHystrix.class)
public interface RemoteSwitchFeignApi {

    @PostMapping(value = "/api/datacenter/remoteSwitch")
    ObjectResponse remoteSwitch(@RequestBody RemoteSwitchRequest remoteSwitchRequest);
}
