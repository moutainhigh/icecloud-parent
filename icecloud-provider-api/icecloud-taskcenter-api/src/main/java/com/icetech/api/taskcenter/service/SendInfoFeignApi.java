package com.icetech.api.taskcenter.service;


import com.icetech.api.transcenter.service.hystrix.SendInfoFeignApiHystrix;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author wangzw
 */
@FeignClient(value = "icecloud-taskcenter", fallback = SendInfoFeignApiHystrix.class)
public interface SendInfoFeignApi {
    /**
     * 更新下发信息状态
     * @return
     */
    @PostMapping("/api/taskcenter/notifySuccess")
    ObjectResponse notifySuccess(Integer serviceType, Integer serviceId);
}
