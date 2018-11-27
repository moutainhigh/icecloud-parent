package com.icetech.transcenter.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 第三方信息
 */
@Getter
@Setter
public class ThirdInfo {
    /**
     * 主键自增长id
     */
    private Integer id;

    /**
     * 第三方接入id
     */
    private String pid;

    /**
     * 第三方名称
     */
    private String name;

    /**
     * 密钥
     */
    private String secretKey;


    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}