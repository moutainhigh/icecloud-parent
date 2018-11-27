package com.icetech.datacenter.domain;

public class Heartbeat {
    private Long id;
    private Long parkId;
    private Long localTime;
    private Long serverTime;
    private String channelId;

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

    public Long getLocalTime() {
        return localTime;
    }

    public void setLocalTime(Long localTime) {
        this.localTime = localTime;
    }

    public Long getServerTime() {
        return serverTime;
    }

    public void setServerTime(Long serverTime) {
        this.serverTime = serverTime;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
