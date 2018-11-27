package com.icetech.transcenter.common.enumeration;

/**
 * 缴费渠道
 * @author wangzw
 */
public enum AppPayChannel {
    LOCAL_PAY(1,"本地出口支付"),
    PAAS_PAY(2,"平台支付，手机APP支付默认选择方式"),
    AUTO_PAY(3,"自助缴费机"),
    CENTER_PAY(4,"中央收费站"),
    THIRD_PAY(5,"第三方"),
    ;
    private Integer code;
    private String desc;
    AppPayChannel(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }
}
