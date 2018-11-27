package com.icetech.common.domain;

import java.io.Serializable;

/**
 * Description : 订单交易信息表实体
 * @author fangct
 */
public class OrderPay implements Serializable {
    private Long id;
    private Long parkId;
    private String orderNum;
    private String tradeNo;
    private Integer payStatus;
    private Long payTime;
    private String totalPrice;
    private String paidPrice;
    private String discountPrice;
    private Integer payChannel;
    private String payTerminal;
    private Integer payWay;
    private String channelId;
    private Long lastPayTime;
    private String userAccount;
    private String remark;
    private String createTime;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

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

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaidPrice() {
        return paidPrice;
    }

    public void setPaidPrice(String paidPrice) {
        this.paidPrice = paidPrice;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(Integer payChannel) {
        this.payChannel = payChannel;
    }

    public String getPayTerminal() {
        return payTerminal;
    }

    public void setPayTerminal(String payTerminal) {
        this.payTerminal = payTerminal;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public Long getLastPayTime() {
        return lastPayTime;
    }

    public void setLastPayTime(Long lastPayTime) {
        this.lastPayTime = lastPayTime;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "OrderPay{" +
                "id=" + id +
                ", parkId=" + parkId +
                ", orderNum='" + orderNum + '\'' +
                ", tradeNo='" + tradeNo + '\'' +
                ", payStatus=" + payStatus +
                ", payTime=" + payTime +
                ", totalPrice='" + totalPrice + '\'' +
                ", paidPrice='" + paidPrice + '\'' +
                ", discountPrice='" + discountPrice + '\'' +
                ", payChannel=" + payChannel +
                ", payTerminal='" + payTerminal + '\'' +
                ", payWay=" + payWay +
                ", lastPayTime=" + lastPayTime +
                ", userAccount='" + userAccount + '\'' +
                ", remark='" + remark + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
