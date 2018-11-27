package com.icetech.cloudcenter.domain.order.query;

public class OrderDiscountQuery {

    /**
     * 条件参数
     */
    private String[] discountNos;
    private Long parkId;
    private String orderNum;
    private Integer oldStatus;

    /**
     * 修改参数
     */
    private String tradeNo;
    private Integer newStatus;

    public String[] getDiscountNos() {
        return discountNos;
    }

    public void setDiscountNos(String[] discountNos) {
        this.discountNos = discountNos;
    }

    public Long getParkId() {
        return parkId;
    }

    public void setParkId(Long parkId) {
        this.parkId = parkId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Integer getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(Integer newStatus) {
        this.newStatus = newStatus;
    }

    public Integer getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(Integer oldStatus) {
        this.oldStatus = oldStatus;
    }
}
