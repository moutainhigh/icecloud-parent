package com.icetech.paycenter.common.enumeration;

import lombok.Getter;

public enum UnionpayTransEnum {

    签约状态推送("40701","40702", "autopaySignedNotifyServiceImpl"),

    车辆场内状态查询("40801","40802", "autopayFindCarStatusServiceImpl");

    @Getter
    private String requestTransId;
    @Getter
    private String responseTransId;
    @Getter
    private String beanName;

    private UnionpayTransEnum(String requestTransId, String responseTransId, String beanName) {
        this.requestTransId = requestTransId;
        this.responseTransId = responseTransId;
        this.beanName = beanName;
    }
}
