package com.icetech.api.cloudcenter.service.hystrix;

import com.icetech.api.cloudcenter.model.request.QueryOrderFeeRequest;
import com.icetech.api.cloudcenter.model.response.QueryOrderFeeResponse;
import com.icetech.api.cloudcenter.service.QueryOrderFeeFeginApi;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.stereotype.Component;

/**
 * Description : 订单查询费用接口
 * @author fangct
 */
@Component
public class QueryOrderFeeFeginApiHystrix implements QueryOrderFeeFeginApi {


    @Override
    public ObjectResponse<QueryOrderFeeResponse> queryOrderFee(QueryOrderFeeRequest queryOrderFeeRequest) {
        return null;
    }
}
