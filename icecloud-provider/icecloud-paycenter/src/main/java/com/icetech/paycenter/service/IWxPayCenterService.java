package com.icetech.paycenter.service;

import com.icetech.paycenter.domain.request.CloseOrderRequest;

/**
 * 微信支付接口
 */
public interface IWxPayCenterService extends IPayCenterService{

    /**
     * 订单关闭
     * @param closeOrderRequest
     * @return
     */
    String doCloseOrder(CloseOrderRequest closeOrderRequest);

}
