package com.icetech.paycenter.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 第三方与车场关联表
 */
@Getter
@Setter
public class ThirdPark {
    /**
     * 主键自增长id
     */
    private Integer id;

    /**
     * 第三方接入id
     */
    private String pid;

    /**
     * 车场编号
     */
    private String parkCode;

}