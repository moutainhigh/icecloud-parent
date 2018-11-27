package com.icetech.datacenter.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkDevice {
    private Long id;
    private Long parkId;
    private String deviceNo;
    private Integer type;
    private Integer channelId;
    private Integer modelType;
    private String ip;
    private String port;
    private Integer callMethods;
    private Integer status;
    private Integer delFlag;
    private String createTime;
    private String updateTime;
    private String endUpdatetime;
    private String adder;

}
