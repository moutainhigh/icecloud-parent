package com.icetech.paycenter.service;

import com.icetech.paycenter.domain.request.PayResultRequest;

/**
 * 查询支付状态
 * @author fangct
 */
public interface IQueryOrderService {

    /**
     * 查询支付结果
     * @param payResultRequest
     * @return
     */
    String queryOrder(PayResultRequest payResultRequest);
}
