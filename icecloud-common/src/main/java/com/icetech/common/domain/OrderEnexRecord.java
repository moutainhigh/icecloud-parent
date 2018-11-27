package com.icetech.common.domain;

import java.io.Serializable;

/**
 * Description : 订单进出场表实体
 * @author fangct
 */
public class OrderEnexRecord implements Serializable {
    private Long id;
    private Integer recordType;
    private String orderNum;
    private Long parkId;
    private String plateNum;
    private Long enterTime;
    private Long exitTime;
    private Integer type;
    private Integer carType;
    private String enterNo;
    private String exitNo;
    private String image;
    private String operAccount;
    private String channelId;
    private String carDesc;
    private String carBrand;
    private String carColor;
    private String localOrderNum;
    private String createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCarType() {
        return carType;
    }

    public void setCarType(Integer carType) {
        this.carType = carType;
    }

    public String getEnterNo() {
        return enterNo;
    }

    public void setEnterNo(String enterNo) {
        this.enterNo = enterNo;
    }

    public String getExitNo() {
        return exitNo;
    }

    public void setExitNo(String exitNo) {
        this.exitNo = exitNo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOperAccount() {
        return operAccount;
    }

    public void setOperAccount(String operAccount) {
        this.operAccount = operAccount;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getCarDesc() {
        return carDesc;
    }

    public void setCarDesc(String carDesc) {
        this.carDesc = carDesc;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
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
        return "OrderEnexRecord{" +
                "id=" + id +
                ", recordType=" + recordType +
                ", orderNum='" + orderNum + '\'' +
                ", parkId=" + parkId +
                ", plateNum='" + plateNum + '\'' +
                ", enterTime=" + enterTime +
                ", exitTime=" + exitTime +
                ", type=" + type +
                ", carType=" + carType +
                ", enterNo='" + enterNo + '\'' +
                ", exitNo='" + exitNo + '\'' +
                ", image='" + image + '\'' +
                ", operAccount='" + operAccount + '\'' +
                ", channelId='" + channelId + '\'' +
                ", carDesc='" + carDesc + '\'' +
                ", carBrand='" + carBrand + '\'' +
                ", carColor='" + carColor + '\'' +
                ", localOrderNum='" + localOrderNum + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
