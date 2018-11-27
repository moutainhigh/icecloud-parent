package com.icetech.transcenter.common.enumeration;

/**
 * 缴费状态
 * @author wangzw
 */
public enum AppPayStatus {
    NO_PAY(1,"无需支付"),
    WAIT_PAY(2,"待支付"),
    AREADY_PAY(3,"已支付"),
    TIME_OUT_PAY(4,"缴费超时"),
    ;
    private Integer code;
    private String desc;
    AppPayStatus(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }
}
