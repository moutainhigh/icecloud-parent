package com.icetech.paycenter.domain;

import java.util.Date;

/**
 * 
 */
public class RequestLog {
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

    /**
     * 主健自增长
     * @return id 主健自增长
     */
    public Integer getId() {
        return id;
    }

    /**
     * 主健自增长
     * @param id 主健自增长
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 请求内容
     * @return req_interface 请求内容
     */
    public String getReqInterface() {
        return reqInterface;
    }

    /**
     * 请求内容
     * @param reqInterface 请求内容
     */
    public void setReqInterface(String reqInterface) {
        this.reqInterface = reqInterface == null ? null : reqInterface.trim();
    }

    /**
     * 创建时间
     * @return create_time 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 更新时间
     * @return update_time 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 更新时间
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}