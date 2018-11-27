package com.icetech.paycenter.service;


import com.icetech.paycenter.domain.autopay.request.EnterNotifyRequest;
import com.icetech.paycenter.domain.autopay.request.ExitNotifyRequest;
import com.icetech.paycenter.domain.autopay.request.ExitpayRequest;
import com.icetech.paycenter.domain.request.RefundRequest;

/**
 * 支付中心免密支付接口
 * @author fangct
 */
public interface IAutopayService {

    /**
     * 入场通知
     * @param request 请求参数
     * @return
     */
    String enterNotify(EnterNotifyRequest request);

    /**
     * 离场通知
     * @param request 请求参数
     * @return
     */
    String exitNotify(ExitNotifyRequest request);

    /**
     * 离场扣费
     * @param request 请求参数
     * @return
     */
    String exitpay(ExitpayRequest request, String outTradeNo);

    /**
     * 退款
     * @param request 请求参数
     * @param outTradeNo 请求扣款时的交易单号
     * @param merDate 请求扣款时的交易日期
     * @return
     */
    String refund(RefundRequest request, String outTradeNo, String merDate);

    /**
     * 查询签约状态
     * @param plateNum 车牌号码
     * @param parkCoe 车场编号
     * @return
     */
    String querySignedStatus(String plateNum, String parkCoe);
}
