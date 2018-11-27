package com.icetech.common.domain;

import java.io.Serializable;

/**
 * Description : 订单修改记录表实体
 * @author fangct
 */
public class OrderModifyRecord implements Serializable {
    private Long id;
    private Integer correctType;
    private String orderNum;
    private Long parkId;
    private String beforeModify;
    private String afterModify;
    private String beforeCardesc;
    private String afterCardesc;
    private String modifyTime;
    private String operAccount;
    private String createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCorrectType() {
        return correctType;
    }

    public void setCorrectType(Integer correctType) {
        this.correctType = correctType;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Long getParkId() {
        return parkId;
    }

    public void setParkId(Long parkId) {
        this.parkId = parkId;
    }

    public String getBeforeModify() {
        return beforeModify;
    }

    public void setBeforeModify(String beforeModify) {
        this.beforeModify = beforeModify;
    }

    public String getAfterModify() {
        return afterModify;
    }

    public void setAfterModify(String afterModify) {
        this.afterModify = afterModify;
    }

    public String getBeforeCardesc() {
        return beforeCardesc;
    }

    public void setBeforeCardesc(String beforeCardesc) {
        this.beforeCardesc = beforeCardesc;
    }

    public String getAfterCardesc() {
        return afterCardesc;
    }

    public void setAfterCardesc(String afterCardesc) {
        this.afterCardesc = afterCardesc;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getOperAccount() {
        return operAccount;
    }

    public void setOperAccount(String operAccount) {
        this.operAccount = operAccount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "OrderModifyRecord{" +
                "id=" + id +
                ", correctType=" + correctType +
                ", orderNum='" + orderNum + '\'' +
                ", parkId=" + parkId +
                ", beforeModify='" + beforeModify + '\'' +
                ", afterModify='" + afterModify + '\'' +
                ", beforeCardesc='" + beforeCardesc + '\'' +
                ", afterCardesc='" + afterCardesc + '\'' +
                ", modifyTime='" + modifyTime + '\'' +
                ", operAccount='" + operAccount + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
