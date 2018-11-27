package com.icetech.paycenter.service.autopay;

import com.icetech.paycenter.domain.autopay.request.UnionpayBaseNotifyRequest;

/**
 * 银联无感支付通知接口类
 * @author fangct
 */
public interface UnionpayNotifyService {

    /**
     * 处理通知
     * @param unionpayBaseNotifyRequest 银联通知基础参数类
     * @return
     */
    String dealNotify(UnionpayBaseNotifyRequest unionpayBaseNotifyRequest) throws Exception;
}
