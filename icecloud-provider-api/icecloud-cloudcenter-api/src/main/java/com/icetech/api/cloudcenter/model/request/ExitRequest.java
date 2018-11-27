package com.icetech.api.cloudcenter.model.request;

import com.icetech.common.annotation.NotNull;

public class ExitRequest extends ExitCommonRequest {
    @NotNull
    private String channelId;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
    @Override
    public String toString() {
        return "ExitRequest{" +
                "parkId=" + this.getParkId() +
                ", plateNum='" + this.getPlateNum() + '\'' +
                ", channelId='" + channelId + '\'' +
                ", exitName='" + this.getExitName() + '\'' +
                ", exitTime=" + this.getExitTime() +
                ", exitImage='" + this.getExitImage() + '\'' +
                ", totalAmount='" + this.getTotalAmount() + '\'' +
                ", paidAmount='" + this.getPaidAmount() + '\'' +
                ", discountAmount='" + this.getDiscountAmount() + '\'' +
                ", userAccount='" + this.getUserAccount() + '\'' +
                ", paidInfo=" + this.getPaidInfo() +
                '}';
    }
}
