package com.icetech.paycenter.common.enumeration;

import lombok.Getter;

/**
 * 接口响应的交易状态
 */
public enum ResponseTradeStatus {
    订单交易成功("S"),
    下单失败("E"),
    未支付待支付("R"),
    已撤销已关闭("C"),
    订单转入退款("T"),
    ;
    private @Getter String code;
    ResponseTradeStatus(String code){
        this.code = code;
    }

    public static String getStatus4WxStatus(String wxStatus){
        if (wxStatus.equals(WxTradeStatus.SUCCESS)){
            return ResponseTradeStatus.订单交易成功.getCode();
        }
        if (wxStatus.equals(WxTradeStatus.PAYERROR)){
            return ResponseTradeStatus.下单失败.getCode();
        }
        if (wxStatus.equals(WxTradeStatus.NOTPAY)||wxStatus.equals(WxTradeStatus.USERPAYING)){
            return ResponseTradeStatus.未支付待支付.getCode();
        }
        if (wxStatus.equals(WxTradeStatus.CLOSED)||wxStatus.equals(WxTradeStatus.REVOKED)){
            return ResponseTradeStatus.已撤销已关闭.getCode();
        }
        if (wxStatus.equals(WxTradeStatus.REFUND)){
            return ResponseTradeStatus.订单转入退款.getCode();
        }
        return null;
    }

    public static String getStatus4AliStatus(String aLiStatus){
        if (aLiStatus.equals(AliTradeStatus.TRADE_SUCCESS)){
            return ResponseTradeStatus.订单交易成功.getCode();
        }
        if (aLiStatus.equals(AliTradeStatus.TRADE_CLOSED)){
            return ResponseTradeStatus.下单失败.getCode();
        }
        if (aLiStatus.equals(AliTradeStatus.WAIT_BUYER_PAY)){
            return ResponseTradeStatus.未支付待支付.getCode();
        }
        if (aLiStatus.equals(AliTradeStatus.TRADE_FINISHED)){
            return ResponseTradeStatus.已撤销已关闭.getCode();
        }
        if (aLiStatus.equals(AliTradeStatus.TRADE_CLOSED)){
            return ResponseTradeStatus.订单转入退款.getCode();
        }
        return null;
    }

    /**
     * 微信返回的订单状态类型
     */
    public static class WxTradeStatus {
        public final static String SUCCESS = "SUCCESS";					//支付成功
        public final static String REFUND = "REFUND";                   //转入退款
        public final static String NOTPAY = "NOTPAY";                   //未支付
        public final static String CLOSED = "CLOSED";                   //已关闭
        public final static String REVOKED = "REVOKED";                 //已撤销
        public final static String USERPAYING = "USERPAYING";           //用户支付中
        public final static String PAYERROR = "PAYERROR";               //支付失败(其他原因，如银行返回失败)
    }

    /**
     * 支付宝返回订单状态
     */
    public static class AliTradeStatus{
        public final static String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";					//交易创建，等待买家付款
        public final static String TRADE_CLOSED = "TRADE_CLOSED";                       //未付款交易超时关闭，或支付完成后全额退款
        public final static String TRADE_SUCCESS = "TRADE_SUCCESS";                     //交易支付成功
        public final static String TRADE_FINISHED = "TRADE_FINISHED";                   //交易结束，不可退款
    }
}
