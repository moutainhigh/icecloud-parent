package com.icetech.datacenter.domain.request;

import com.icetech.common.annotation.NotNull;

public class DeviceStatusRequest {
    @NotNull
    private String deviceNo;
    @NotNull
    private Integer deviceStatus;
    private String failureCause;

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public Integer getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(Integer deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getFailureCause() {
        return failureCause;
    }

    public void setFailureCause(String failureCause) {
        this.failureCause = failureCause;
    }
}
