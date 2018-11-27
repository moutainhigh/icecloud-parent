package com.icetech.api.cloudcenter.model.request;

import com.icetech.common.annotation.NotNull;

import java.io.Serializable;

public class EnterRequest  implements Serializable {
    private Long parkId;
    @NotNull
    private String localOrderNum;
    @NotNull
    private String plateNum;
    @NotNull
    private String channelId;
    @NotNull
    private String entranceName;
    @NotNull
    private Long enterTime;
    @NotNull
    private Integer type;
    @NotNull
    private Integer carType;
    @NotNull
    private String enterImage;
    private String carDesc;
    private String carBrand;
    private String carColor;

    public Long getParkId() {
        return parkId;
    }

    public void setParkId(Long parkId) {
        this.parkId = parkId;
    }

    public String getLocalOrderNum() {
        return localOrderNum;
    }

    public void setLocalOrderNum(String localOrderNum) {
        this.localOrderNum = localOrderNum;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getEntranceName() {
        return entranceName;
    }

    public void setEntranceName(String entranceName) {
        this.entranceName = entranceName;
    }

    public Long getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(Long enterTime) {
        this.enterTime = enterTime;
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
        return "EnterRequest{" +
                "parkId=" + parkId +
                ", localOrderNum='" + localOrderNum + '\'' +
                ", plateNum='" + plateNum + '\'' +
                ", channelId='" + channelId + '\'' +
                ", entranceName='" + entranceName + '\'' +
                ", enterTime=" + enterTime +
                ", type=" + type +
                ", carType=" + carType +
                ", enterImage='" + enterImage + '\'' +
                ", carDesc='" + carDesc + '\'' +
                ", carBrand='" + carBrand + '\'' +
                ", carColor='" + carColor + '\'' +
                '}';
    }
}
