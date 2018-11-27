package com.icetech.cloudcenter.domain.order.query;

public class OrderPayQuery {

    private String orderNum;
    private Integer oldPayStatus;
    private Integer newPayStatus;

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getOldPayStatus() {
        return oldPayStatus;
    }

    public void setOldPayStatus(Integer oldPayStatus) {
        this.oldPayStatus = oldPayStatus;
    }

    public Integer getNewPayStatus() {
        return newPayStatus;
    }

    public void setNewPayStatus(Integer newPayStatus) {
        this.newPayStatus = newPayStatus;
    }
}
