package com.icetech.transcenter.common.enumeration;

/**
 * 缴费方式
 * @author wangzw
 */
public enum AppPayWay {
    MONEY_PAY(1,"现金"),
    WX_PAY(2,"微信"),
    ZFB_PAY(3,"支付宝"),
    ;
    private Integer code;
    private String desc;
    AppPayWay(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }
}
