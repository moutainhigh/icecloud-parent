package com.icetech.datacenter.domain.request;

import com.icetech.common.annotation.NotNull;

public class DeviceInfoRequest {
    @NotNull
    private String deviceNo;
    @NotNull(condition = "operType ==1")
    private Integer deviceType;
    @NotNull(condition = "operType ==1")
    private String channelId;
    @NotNull(condition = "operType ==1")
    private String gateName;
    @NotNull(condition = "operType ==1")
    private Integer gateType;
    @NotNull(condition = "operType ==1")
    private String deviceIp;
    private String devicePort;
    @NotNull
    private Integer operType;
    @NotNull
    private Integer operTime;

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getGateName() {
        return gateName;
    }

    public void setGateName(String gateName) {
        this.gateName = gateName;
    }

    public Integer getGateType() {
        return gateType;
    }

    public void setGateType(Integer gateType) {
        this.gateType = gateType;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getDevicePort() {
        return devicePort;
    }

    public void setDevicePort(String devicePort) {
        this.devicePort = devicePort;
    }

    public Integer getOperType() {
        return operType;
    }

    public void setOperType(Integer operType) {
        this.operType = operType;
    }

    public Integer getOperTime() {
        return operTime;
    }

    public void setOperTime(Integer operTime) {
        this.operTime = operTime;
    }
}
