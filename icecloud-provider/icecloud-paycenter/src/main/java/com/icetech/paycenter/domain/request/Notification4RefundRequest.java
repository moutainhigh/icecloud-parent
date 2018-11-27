package com.icetech.paycenter.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 异步通知请求
 *
 * @author wangzw
 * @create 2018-09-03 9:28
 **/
@Data
public class Notification4RefundRequest extends Notification4PayRequest implements Serializable {
    private String refundTradeNo;
}
