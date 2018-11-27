package com.icetech.api.cloudcenter.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class QueryOrderFeeRequest implements Serializable {
    /**
     * 参数定义1
     */
    //车场编号
    private String parkCode;
    //车牌号
    private String plateNum;
    //通道ID
    private String channelId;

    /**
     * 参数定义2
     */
    //订单编号
    private String orderNum;

}
