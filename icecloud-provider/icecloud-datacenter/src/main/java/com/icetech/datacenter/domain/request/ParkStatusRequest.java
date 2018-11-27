package com.icetech.datacenter.domain.request;

import com.icetech.common.annotation.NotNull;

public class ParkStatusRequest {

    @NotNull
    private Long localTime;
    private String channelId;
    private Integer emptyNumber;

    public Long getLocalTime() {
        return localTime;
    }

    public void setLocalTime(Long localTime) {
        this.localTime = localTime;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getEmptyNumber() {
        return emptyNumber;
    }

    public void setEmptyNumber(Integer emptyNumber) {
        this.emptyNumber = emptyNumber;
    }
}
