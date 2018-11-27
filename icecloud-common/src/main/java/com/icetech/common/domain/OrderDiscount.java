package com.icetech.common.domain;

import java.io.Serializable;

public class OrderDiscount implements Serializable {
    private Long id;
    private Long parkId;
    private String discountNo;
    private String orderNum;
    private String tradeNo;
    private Integer from;
    private Integer type;
    private String amount;
    private String operAccount;
    private Integer status;
    private String getAmount;
    private String sendTime;
    private String useTime;
    private String updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public String getDiscountNo() {
        return discountNo;
    }

    public void setDiscountNo(String discountNo) {
        this.discountNo = discountNo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOperAccount() {
        return operAccount;
    }

    public void setOperAccount(String operAccount) {
        this.operAccount = operAccount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getGetAmount() {
        return getAmount;
    }

    public void setGetAmount(String getAmount) {
        this.getAmount = getAmount;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "OrderDiscount{" +
                "id=" + id +
                ", parkId=" + parkId +
                ", discountNo='" + discountNo + '\'' +
                ", orderNum='" + orderNum + '\'' +
                ", tradeNo='" + tradeNo + '\'' +
                ", from=" + from +
                ", type=" + type +
                ", amount='" + amount + '\'' +
                ", operAccount='" + operAccount + '\'' +
                ", status=" + status +
                ", getAmount='" + getAmount + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", useTime='" + useTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
