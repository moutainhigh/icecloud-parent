package com.icetech.paycenter.domain.autopay.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 *场内状态查询
 */
@Setter
@Getter
@ToString
public class FindCarStatusRequest implements Serializable {

    private String parkCode;
    private String orderNum;
    private String plateNum;

}
