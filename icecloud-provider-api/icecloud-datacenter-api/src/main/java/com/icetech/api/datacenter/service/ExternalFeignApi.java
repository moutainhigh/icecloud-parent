package com.icetech.api.datacenter.service;

import com.icetech.api.datacenter.model.request.PrepayReportRequest;
import com.icetech.api.datacenter.service.hystrix.ExternalFeignApiHystrix;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Description : 外部接口类
 *  @author fangct
 */
@FeignClient(value = "icecloud-datacenter", fallback = ExternalFeignApiHystrix.class)
public interface ExternalFeignApi {

    /**
     * 上报预缴费
     * @param prepayReportRequest
     * @return
     */
    @PostMapping(value = "/api/datacenter/prepayReport")
    ObjectResponse prepayReport(@RequestBody PrepayReportRequest prepayReportRequest);
}
