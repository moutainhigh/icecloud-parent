package com.icetech.cloudcenter.domain.park;

import java.io.Serializable;
import java.util.Date;

/**
 *  停车场系统配置表
 *
 *  Created by wangzw on '2018-10-29 09:55:14'.
 */
public class ParkConfig implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
     * id (主健自增)
     */
    private int id;

    /**
     * 车场id
     */
    private int parkId;

    /**
     * 是否同步数据（0不同步1同步；默认0）
     */
    private int isSync;

    /**
     * 计费精度 0：分钟，1：秒 默认0
     */
    private int billPrecision;

    /**
     * 军警车是否免费停车 0：免费 1：不免费  默认0
     */
    private int isfreeSpecialcar;

    /**
     * 是否支持AB车管理 0：不支持 1：支持 默认0
     */
    private int issupAbmanage;

    /**
     * AB车场内切换**时间不收费，单位分钟
     */
    private int switchTm;

    /**
     * 免费时间内是否自动抬杆 0：不自动  1：自动 默认为1
     */
    private int isreleaseFreetm;

    /**
     * 是否上报车辆图片 0：不上报 1：上报 默认为1
     */
    private int isupimage;

    /**
     * 临时车是否允许进场 0：允许 1：不允许 默认为0
     */
    private int issupTempcar;

    /**
     * 是否支持电子支付 0：不支持 1：支持 默认为1
     */
    private int isEpayment;

    /**
     * 是否支持无感支付 0：不支持 1：支持 默认为1
     */
    private int isNosenpayment;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 缴费后离场预留免费时长(单位分钟)
     */
    private int isfreeAfterpay;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParkId() {
        return parkId;
    }

    public void setParkId(int parkId) {
        this.parkId = parkId;
    }

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }

    public int getBillPrecision() {
        return billPrecision;
    }

    public void setBillPrecision(int billPrecision) {
        this.billPrecision = billPrecision;
    }

    public int getIsfreeSpecialcar() {
        return isfreeSpecialcar;
    }

    public void setIsfreeSpecialcar(int isfreeSpecialcar) {
        this.isfreeSpecialcar = isfreeSpecialcar;
    }

    public int getIssupAbmanage() {
        return issupAbmanage;
    }

    public void setIssupAbmanage(int issupAbmanage) {
        this.issupAbmanage = issupAbmanage;
    }

    public int getSwitchTm() {
        return switchTm;
    }

    public void setSwitchTm(int switchTm) {
        this.switchTm = switchTm;
    }

    public int getIsreleaseFreetm() {
        return isreleaseFreetm;
    }

    public void setIsreleaseFreetm(int isreleaseFreetm) {
        this.isreleaseFreetm = isreleaseFreetm;
    }

    public int getIsupimage() {
        return isupimage;
    }

    public void setIsupimage(int isupimage) {
        this.isupimage = isupimage;
    }

    public int getIssupTempcar() {
        return issupTempcar;
    }

    public void setIssupTempcar(int issupTempcar) {
        this.issupTempcar = issupTempcar;
    }

    public int getIsEpayment() {
        return isEpayment;
    }

    public void setIsEpayment(int isEpayment) {
        this.isEpayment = isEpayment;
    }

    public int getIsNosenpayment() {
        return isNosenpayment;
    }

    public void setIsNosenpayment(int isNosenpayment) {
        this.isNosenpayment = isNosenpayment;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public int getIsfreeAfterpay() {
        return isfreeAfterpay;
    }

    public void setIsfreeAfterpay(int isfreeAfterpay) {
        this.isfreeAfterpay = isfreeAfterpay;
    }

}