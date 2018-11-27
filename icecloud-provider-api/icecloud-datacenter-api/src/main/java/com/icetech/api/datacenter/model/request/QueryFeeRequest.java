package com.icetech.api.datacenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 缴费查询
 * @author fangct
 */
@Data
public class QueryFeeRequest implements Serializable {
    /**
     * 车场ID
     */
    private Long parkId;
    /**
     * 车场编号
     */
    private String parkCode;
    /**
     * 车场key
     */
    private String key;
    /**
     * 订单号
     */
    private String orderNum;
    /**
     * 车牌号
     */
    private String plateNum;
    /**
     * 通道ID
     */
    private String channelId;
}
