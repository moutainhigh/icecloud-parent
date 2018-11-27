package com.icetech.common.domain;

import java.io.Serializable;

/**
 * Description : 订单表实体
 * @author fangct
 */
public class OrderInfo implements Serializable {
    private Long id;
    private String orderNum;
    private Long parkId;
    private String plateNum;
    private Integer type;
    private Long enterTime;
    private Long exitTime;
    private Integer carType;
    private String totalPrice;
    private String paidPrice;
    private String discountPrice;
    private Integer serviceStatus;
    private String operAccount;
    private Long prepayTime;
    private String prepayAmount;
    private String carDesc;
    private String localOrderNum;
    private String createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(Long enterTime) {
        this.enterTime = enterTime;
    }

    public Long getExitTime() {
        return exitTime;
    }

    public void setExitTime(Long exitTime) {
        this.exitTime = exitTime;
    }

    public Integer getCarType() {
        return carType;
    }

    public void setCarType(Integer carType) {
        this.carType = carType;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaidPrice() {
        return paidPrice;
    }

    public void setPaidPrice(String paidPrice) {
        this.paidPrice = paidPrice;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(Integer serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getOperAccount() {
        return operAccount;
    }

    public void setOperAccount(String operAccount) {
        this.operAccount = operAccount;
    }

    public Long getPrepayTime() {
        return prepayTime;
    }

    public void setPrepayTime(Long prepayTime) {
        this.prepayTime = prepayTime;
    }

    public String getPrepayAmount() {
        return prepayAmount;
    }

    public void setPrepayAmount(String prepayAmount) {
        this.prepayAmount = prepayAmount;
    }

    public String getCarDesc() {
        return carDesc;
    }

    public void setCarDesc(String carDesc) {
        this.carDesc = carDesc;
    }

    public String getLocalOrderNum() {
        return localOrderNum;
    }

    public void setLocalOrderNum(String localOrderNum) {
        this.localOrderNum = localOrderNum;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "id=" + id +
                ", orderNum='" + orderNum + '\'' +
                ", parkId=" + parkId +
                ", plateNum='" + plateNum + '\'' +
                ", type=" + type +
                ", enterTime=" + enterTime +
                ", exitTime=" + exitTime +
                ", carType=" + carType +
                ", totalPrice='" + totalPrice + '\'' +
                ", paidPrice='" + paidPrice + '\'' +
                ", discountPrice='" + discountPrice + '\'' +
                ", serviceStatus=" + serviceStatus +
                ", operAccount='" + operAccount + '\'' +
                ", prepayTime=" + prepayTime +
                ", prepayAmount='" + prepayAmount + '\'' +
                ", carDesc='" + carDesc + '\'' +
                ", localOrderNum='" + localOrderNum + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
