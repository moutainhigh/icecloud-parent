package com.icetech.paycenter.common.enumeration;

import lombok.Getter;

/**
 * 民生银行统一下单返回结果
 */
public enum CmbcResultCode {
    SUCCESS("S","成功"),
    ERROR("E","失败"),
    WAIT("R","待处理"),
    CANCEL("C","已撤销/已关闭"),
    TRANSFER("T","订单转入退款"),
    ;
    private @Getter String code;
    private @Getter String desc;
    CmbcResultCode(String code,String desc){
        this.code = code;
        this.desc = desc;
    }
}
