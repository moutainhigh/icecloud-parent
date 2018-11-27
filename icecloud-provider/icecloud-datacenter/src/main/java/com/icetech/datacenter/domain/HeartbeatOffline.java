package com.icetech.datacenter.domain;

public class HeartbeatOffline {
    private Long id;
    private Long parkId;
    private Long lastConnectionTime;
    private Long reconnectTime;
    private Long offTime;
    private String createTime;

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

    public Long getLastConnectionTime() {
        return lastConnectionTime;
    }

    public void setLastConnectionTime(Long lastConnectionTime) {
        this.lastConnectionTime = lastConnectionTime;
    }

    public Long getReconnectTime() {
        return reconnectTime;
    }

    public void setReconnectTime(Long reconnectTime) {
        this.reconnectTime = reconnectTime;
    }

    public Long getOffTime() {
        return offTime;
    }

    public void setOffTime(Long offTime) {
        this.offTime = offTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
