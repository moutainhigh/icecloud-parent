package com.icetech.paycenter.common.enumeration;

import lombok.Getter;

/**
 * 支付中心的交易状态
 */
public enum PayCenterTradeStatus {
    SUCCESS("S","交易成功"),
    ERROR("E","交易失败"),
    WAIT("R","交易中"),
    CANCEL("C","交易关闭或撤回"),
    TRANSFER("T","退款"),
    ;
    private @Getter String code;
    private @Getter String desc;
    PayCenterTradeStatus(String code,String desc){
        this.code = code;
        this.desc = desc;
    }
}
