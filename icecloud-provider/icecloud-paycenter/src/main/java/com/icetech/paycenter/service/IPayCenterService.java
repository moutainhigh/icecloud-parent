package com.icetech.paycenter.service;


import com.icetech.paycenter.domain.normalpay.request.TransactionDetailsDownloadRequest;
import com.icetech.paycenter.domain.normalpay.request.UnifiedOrderRequest;
import com.icetech.paycenter.domain.request.PayResultRequest;
import com.icetech.paycenter.domain.request.RefundRequest;

/**
 * 支付中心统一接口
 * @author wangzw
 */
public interface IPayCenterService {

    /**
     * 统一下单接口
     * @param orderRequest
     * @return
     */
    String doUnifiedOrder(UnifiedOrderRequest orderRequest);

    /**
     * 支付结果查询
     * @param payResultRequest
     * @return
     */
    String doPayResult(PayResultRequest payResultRequest);

    /**
     * 发起退款
     * @param refundRequest
     * @return
     */
    String doRefund(RefundRequest refundRequest);

    /**
     * 下载交易明细
     * @param downloadRequest
     * @return
     */
    String downloadTransactionDetails(TransactionDetailsDownloadRequest downloadRequest);
}
