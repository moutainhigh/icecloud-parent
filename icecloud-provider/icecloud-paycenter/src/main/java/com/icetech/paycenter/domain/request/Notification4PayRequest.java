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
public class Notification4PayRequest implements Serializable {
    /**
     * 交易流水号
     */
    private String tradeNo;
    /**
     * 价格
     */
    private String price;
    /**
     * 状态
     */
    private String tradeStatus;
    /**
     * 额外数据包
     */
    private String extraInfo;
}
