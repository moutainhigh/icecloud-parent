package com.icetech.api.cloudcenter.service;

import com.icetech.api.cloudcenter.model.request.QueryOrderFeeRequest;
import com.icetech.api.cloudcenter.model.response.QueryOrderFeeResponse;
import com.icetech.common.domain.response.ObjectResponse;

/**
 * Description : 订单查询费用接口
 * @author fangct
 */
public interface QueryOrderFeeFeginApi {

    /**
     * 查询订单费用
     * @param queryOrderFeeRequest
     * @return
     */
    ObjectResponse<QueryOrderFeeResponse> queryOrderFee(QueryOrderFeeRequest queryOrderFeeRequest);

}
