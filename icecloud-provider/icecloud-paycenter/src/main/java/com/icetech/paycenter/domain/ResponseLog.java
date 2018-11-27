package com.icetech.paycenter.domain;

import lombok.Data;

import java.util.Date;

/**
 * 
 */
@Data
public class ResponseLog {
    /**
     * 主健自增长
     */
    private Integer id;

    /**
     * 请求内容
     */
    private String reqInterface;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}