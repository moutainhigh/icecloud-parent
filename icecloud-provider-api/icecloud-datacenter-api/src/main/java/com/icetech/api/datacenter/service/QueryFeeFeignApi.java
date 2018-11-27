package com.icetech.api.datacenter.service;

import com.icetech.api.datacenter.model.request.QueryFeeRequest;
import com.icetech.api.datacenter.service.hystrix.QueryFeeFeignApiHystrix;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Description : 费用查询业务接口类
 *  @author fangct
 */
@FeignClient(value = "icecloud-datacenter",fallback = QueryFeeFeignApiHystrix.class)
public interface QueryFeeFeignApi {

    @PostMapping(value = "/api/datacenter/queryFee")
    ObjectResponse queryFee(@RequestBody QueryFeeRequest queryFeeRequest);
}
