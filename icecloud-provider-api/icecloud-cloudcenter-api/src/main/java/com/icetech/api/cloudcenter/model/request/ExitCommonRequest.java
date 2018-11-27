package com.icetech.api.cloudcenter.model.request;

import com.icetech.common.annotation.NotNull;

import java.io.Serializable;
import java.util.List;

public class ExitCommonRequest implements Serializable{
    @NotNull
    private Long parkId;
    @NotNull
    private String orderNum;
    @NotNull
    private String plateNum;
    @NotNull
    private String exitName;
    @NotNull
    private Long exitTime;
    @NotNull
    private String exitImage;
    @NotNull
    private String totalAmount;
    @NotNull
    private String paidAmount;
    @NotNull
    private String discountAmount;
    private String userAccount;
    private List<PaidInfo> paidInfo;

    public Long getParkId() {
        return parkId;
    }

    public void setParkId(Long parkId) {
        this.parkId = parkId;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getExitName() {
        return exitName;
    }

    public void setExitName(String exitName) {
        this.exitName = exitName;
    }

    public Long getExitTime() {
        return exitTime;
    }

    public void setExitTime(Long exitTime) {
        this.exitTime = exitTime;
    }

    public String getExitImage() {
        return exitImage;
    }

    public void setExitImage(String exitImage) {
        this.exitImage = exitImage;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public List<PaidInfo> getPaidInfo() {
        return paidInfo;
    }

    public void setPaidInfo(List<PaidInfo> paidInfo) {
        this.paidInfo = paidInfo;
    }

    public class PaidInfo implements Serializable {
        @NotNull
        private String tradeNo;
        @NotNull
        private String totalPrice;
        @NotNull
        private String paidPrice;
        @NotNull
        private String discountPrice;
        @NotNull
        private Integer payWay;
        @NotNull
        private Integer payChannel;
        private String payTerminal;
        @NotNull
        private Long payTime;
        private List<DiscountInfo> discountInfo;

        public List<DiscountInfo> getDiscountInfo() {
            return discountInfo;
        }

        public void setDiscountInfo(List<DiscountInfo> discountInfo) {
            this.discountInfo = discountInfo;
        }

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
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

        public Integer getPayWay() {
            return payWay;
        }

        public void setPayWay(Integer payWay) {
            this.payWay = payWay;
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

        public Long getPayTime() {
            return payTime;
        }

        public void setPayTime(Long payTime) {
            this.payTime = payTime;
        }

        @Override
        public String toString() {
            return "PaidInfo{" +
                    "tradeNo='" + tradeNo + '\'' +
                    ", totalPrice='" + totalPrice + '\'' +
                    ", paidPrice='" + paidPrice + '\'' +
                    ", discountPrice='" + discountPrice + '\'' +
                    ", payWay=" + payWay +
                    ", payChannel=" + payChannel +
                    ", payTerminal='" + payTerminal + '\'' +
                    ", payTime=" + payTime +
                    ", discountInfo=" + discountInfo +
                    '}';
        }
    }

    public class DiscountInfo implements Serializable {
        @NotNull
        private String discountNo;
        @NotNull
        private Integer discountType;
        @NotNull
        private String discountNumber;
        @NotNull
        private Integer discountTime;

        public String getDiscountNo() {
            return discountNo;
        }

        public void setDiscountNo(String discountNo) {
            this.discountNo = discountNo;
        }

        public Integer getDiscountType() {
            return discountType;
        }

        public void setDiscountType(Integer discountType) {
            this.discountType = discountType;
        }

        public String getDiscountNumber() {
            return discountNumber;
        }

        public void setDiscountNumber(String discountNumber) {
            this.discountNumber = discountNumber;
        }

        public Integer getDiscountTime() {
            return discountTime;
        }

        public void setDiscountTime(Integer discountTime) {
            this.discountTime = discountTime;
        }

        @Override
        public String toString() {
            return "DiscountInfo{" +
                    "discountNo='" + discountNo + '\'' +
                    ", discountType=" + discountType +
                    ", discountNumber='" + discountNumber + '\'' +
                    ", discountTime=" + discountTime +
                    '}';
        }
    }

}
