package com.icetech.transcenter.common.enumeration;

/**
 * 车辆类型
 * @author wangzw
 */
public enum AppParkCarType {
    TEMP_CAR(1,"临时车"),
    CARD_CAR(2,"月卡车"),
    ;
    private Integer code;
    private String desc;
    AppParkCarType(Integer code,String desc){
        this.code = code;
        this.desc = desc;
    }
}
