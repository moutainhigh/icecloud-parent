package com.icetech.api.datacenter.service.hystrix;

import com.icetech.api.datacenter.model.request.QueryFeeRequest;
import com.icetech.api.datacenter.service.QueryFeeFeignApi;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.stereotype.Component;

@Component
public class QueryFeeFeignApiHystrix implements QueryFeeFeignApi {


    @Override
    public ObjectResponse queryFee(QueryFeeRequest queryFeeRequest) {
        return null;
    }
}
