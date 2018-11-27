package com.icetech.api.cloudcenter.model.request;

import com.icetech.common.annotation.NotNull;

public class ComplateOrderRequest extends ExitCommonRequest {
    @NotNull
    private String localOrderNum;
    @NotNull
    private String inChannelId;
    @NotNull
    private String outChannelId;
    @NotNull
    private String entraceName;
    @NotNull
    private Long enterTime;
    @NotNull
    private Long type;
    @NotNull
    private Long carType;
    @NotNull
    private String enterImage;
    private String carDesc;
    private String carBrand;
    private String carColor;

    public String getLocalOrderNum() {
        return localOrderNum;
    }

    public void setLocalOrderNum(String localOrderNum) {
        this.localOrderNum = localOrderNum;
    }

    public String getInChannelId() {
        return inChannelId;
    }

    public void setInChannelId(String inChannelId) {
        this.inChannelId = inChannelId;
    }

    public String getOutChannelId() {
        return outChannelId;
    }

    public void setOutChannelId(String outChannelId) {
        this.outChannelId = outChannelId;
    }

    public String getEntraceName() {
        return entraceName;
    }

    public void setEntraceName(String entraceName) {
        this.entraceName = entraceName;
    }

    public Long getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(Long enterTime) {
        this.enterTime = enterTime;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getCarType() {
        return carType;
    }

    public void setCarType(Long carType) {
        this.carType = carType;
    }

    public String getEnterImage() {
        return enterImage;
    }

    public void setEnterImage(String enterImage) {
        this.enterImage = enterImage;
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

    @Override
    public String toString() {
        return "ComplateOrderRequest{" +
                "parkId=" + this.getParkId() +
                ", localOrderNum='" + localOrderNum + '\'' +
                ", plateNum='" + this.getPlateNum() + '\'' +
                ", inChannelId='" + inChannelId + '\'' +
                ", outChannelId='" + outChannelId + '\'' +
                ", exitName='" + this.getExitName() + '\'' +
                ", entraceName='" + entraceName + '\'' +
                ", enterTime=" + enterTime +
                ", exitTime=" + this.getExitTime() +
                ", type=" + type +
                ", carType=" + carType +
                ", enterImage='" + enterImage + '\'' +
                ", exitImage='" + this.getExitImage() + '\'' +
                ", carDesc='" + carDesc + '\'' +
                ", carBrand='" + carBrand + '\'' +
                ", carColor='" + carColor + '\'' +
                ", totalAmount='" + this.getTotalAmount() + '\'' +
                ", paidAmount='" + this.getPaidAmount() + '\'' +
                ", discountAmount='" + this.getDiscountAmount() + '\'' +
                ", userAccount='" + this.getUserAccount() + '\'' +
                ", paidInfo=" + this.getPaidInfo() +
                '}';
    }
}
