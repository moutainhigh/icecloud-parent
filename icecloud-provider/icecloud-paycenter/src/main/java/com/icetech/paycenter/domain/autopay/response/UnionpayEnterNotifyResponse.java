package com.icetech.paycenter.domain.autopay.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 银联免密支付-入场通知响应参数
 * @author fangct
 */
@Setter
@Getter
public class UnionpayEnterNotifyResponse extends UnionpayResponse {

    /**
     *用户ID
     */
    private String CustId;

}
