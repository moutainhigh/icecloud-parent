package com.icetech.transcenter.common.enumeration;

/**
 * 车型
 * @author wangzw
 */
public enum AppCarType {
    BIG_CAR(1,"小车"),
    SMALL_CAR(2,"大车"),
    ;
    private Integer code;
    private String desc;
    AppCarType(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }
}
