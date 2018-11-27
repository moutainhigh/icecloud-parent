package com.icetech.datacenter.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 *MQ下发记录表
 * @author fangct
 */
@Setter
@Getter
@ToString
public class SendinfoRecord implements Serializable {
    private Long id;

    private String messageId;

    private Long parkId;

    private Integer serviceType;

    private Long serviceId;

    private String params;

    private Integer operType;

    private String createTime;

}