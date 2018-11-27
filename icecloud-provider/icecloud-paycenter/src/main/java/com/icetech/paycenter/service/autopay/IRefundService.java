package com.icetech.paycenter.service.autopay;

import com.icetech.paycenter.domain.request.RefundRequest;

public interface IRefundService {

    /**
     * 退款
     * @param refundRequest
     * @return
     * @throws Exception
     */
    String refund(RefundRequest refundRequest) throws Exception;
}
