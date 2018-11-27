package com.icetech.paycenter.common.enumeration;

import lombok.Getter;

/**
 * pay center 接口明细
 */
public enum  PayCenterInterfaceEnum {

    UNIFIED_ORDER("payment.unifiedOrder","下单接口"),
    QUERY_ORDER("common.queryOrder","订单查询"),
    REFUND("common.refund","退款"),
    DOWNLOAD("payment.download","交易明细下载"),
    CLOSE_ORDER("close.order","关闭订单"),
    ;
    private @Getter String code;
    private @Getter String desc;
    PayCenterInterfaceEnum(String code,String desc){
        this.code = code;
        this.desc = desc;
    }

}
