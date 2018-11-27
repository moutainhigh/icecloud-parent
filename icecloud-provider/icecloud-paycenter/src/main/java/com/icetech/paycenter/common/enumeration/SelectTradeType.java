package com.icetech.paycenter.common.enumeration;


import lombok.Getter;

public enum SelectTradeType {

    /** 民生银行支付类型 **/
    CMBC_API_WXQRCODE	    ("CMBC-API_WXQRCODE","微信正扫"),
    CMBC_API_WXSCAN	        ("CMBC-API_WXSCAN","微信反扫"),
    CMBC_H5_WXJSAPI	        ("CMBC-H5_WXJSAPI","微信公众号跳转"),
    CMBC_API_ZFBQRCODE	    ("CMBC-API_ZFBQRCODE","支付宝正扫"),
    CMBC_API_ZFBSCAN	    ("CMBC-API_ZFBSCAN","支付宝反扫"),
    CMBC_H5_ZFBJSAPI	    ("CMBC-H5_ZFBJSAPI","支付宝服务号"),
    CMBC_API_QQQRCODE	    ("CMBC-API_QQQRCODE","QQ正扫"),
    CMBC_API_QQSCAN	        ("CMBC-API_QQSCAN","QQ 反扫 "),
    CMBC_API_BDQRCODE 	    ("CMBC-API_BDQRCODE","百度正扫"),
    CMBC_API_BDSCAN 	    ("CMBC-API_BDSCAN","百度反扫"),
    CMBC_API_UNIONQRCODE	("CMBC-API_UNIONQRCODE","银联合码"),
    CMBC_API_UNIONSCAN	    ("CMBC-API_API_UNIONSCAN","银联合码"),
    CMBC_API_CMBCSCAN	    ("CMBC-API_CMBCSCAN","民生反扫"),

    /** 微信支付类型 **/
    WX_APP	                ("WX-APP","APP跳转微信支付"),
    WX_JSAPI	            ("WX-JSAPI","微信公众号支付"),
    WX_NATIVE	            ("WX-NATIVE","原生扫码支付"),
    WX_MWEB	                ("WX-MWEB","H5跳转支付"),

    /** 微支付宝付类型 **/
    ALI_MOBILE              ("ALI-MOBILE","支付宝移动支付"),
    ALI_PC                  ("ALI-PC","支付宝PC支付"),
    ALI_WAP                 ("ALI-WAP","支付宝WAP支付"),
    ALI_QR                  ("ALI-QR","支付宝当面付之扫码支付"),


    ;
    private @Getter String code;
    private @Getter String desc;
    SelectTradeType(String code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public static String getWxTradeType(String selectTradeType){
        if (!selectTradeType.startsWith("WX-")){
            return null;
        }
        if (selectTradeType.equals(SelectTradeType.WX_APP.getCode())){
            return WxConstant.WX_APP;
        }
        if (selectTradeType.equals(SelectTradeType.WX_JSAPI.getCode())){
            return WxConstant.WX_JSAPI;
        }
        if (selectTradeType.equals(SelectTradeType.WX_NATIVE.getCode())){
            return WxConstant.WX_NATIVE;
        }
        if (selectTradeType.equals(SelectTradeType.WX_MWEB.getCode())){
            return WxConstant.WX_MWEB;
        }
        return null;
    }

    public static String getCmbcTradeType(String selectTradeType){
        if (!selectTradeType.startsWith("CMBC-")){
            return selectTradeType;
        }
        if (selectTradeType.equals(SelectTradeType.CMBC_API_WXQRCODE.getCode())){
            return CmbcConstant.CMBC_API_WXQRCODE;
        }
        if (selectTradeType.equals(SelectTradeType.CMBC_API_WXSCAN.getCode())){
            return CmbcConstant.CMBC_API_WXSCAN;
        }
        if (selectTradeType.equals(SelectTradeType.CMBC_H5_WXJSAPI.getCode())){
            return CmbcConstant.CMBC_H5_WXJSAPI;
        }
        if (selectTradeType.equals(SelectTradeType.CMBC_API_ZFBQRCODE.getCode())){
            return CmbcConstant.CMBC_API_ZFBQRCODE;
        }
        if (selectTradeType.equals(SelectTradeType.CMBC_API_ZFBSCAN.getCode())){
            return CmbcConstant.CMBC_API_ZFBSCAN;
        }if (selectTradeType.equals(SelectTradeType.CMBC_H5_ZFBJSAPI.getCode())){
            return CmbcConstant.CMBC_H5_ZFBJSAPI;
        }
        if (selectTradeType.equals(SelectTradeType.CMBC_API_QQQRCODE.getCode())){
            return CmbcConstant.CMBC_API_QQQRCODE;
        }
        if (selectTradeType.equals(SelectTradeType.CMBC_API_QQSCAN.getCode())){
            return CmbcConstant.CMBC_API_QQSCAN;
        }
        if (selectTradeType.equals(SelectTradeType.CMBC_API_BDQRCODE.getCode())){
            return CmbcConstant.CMBC_API_BDQRCODE;
        }
        if (selectTradeType.equals(SelectTradeType.CMBC_API_BDSCAN.getCode())){
            return CmbcConstant.CMBC_API_BDSCAN;
        }
        if (selectTradeType.equals(SelectTradeType.CMBC_API_UNIONQRCODE.getCode())){
            return CmbcConstant.CMBC_API_UNIONQRCODE;
        }
        if (selectTradeType.equals(SelectTradeType.CMBC_API_UNIONSCAN.getCode())){
            return CmbcConstant.CMBC_API_UNIONSCAN;
        }
        if (selectTradeType.equals(SelectTradeType.CMBC_API_CMBCSCAN.getCode())){
            return CmbcConstant.CMBC_API_CMBCSCAN;
        }

        return null;
    }

    /**
     * 目前支持的微信支付类型
     */
    public static class WxConstant {
        public final static String WX_APP = "APP";									    // APP支付
        public final static String WX_JSAPI = "JSAPI";								    // 公众号支付或小程序支付
        public final static String WX_NATIVE = "NATIVE";							    // 原生扫码支付
        public final static String WX_MWEB = "MWEB";								    // H5支付
    }

    /**
     * 目前支持的支付宝支付类型
     */
    public static class AliConstant {
        public final static String ALI_MOBILE = "ALI-MOBILE";		                    // 支付宝移动支付
        public final static String ALI_PC = "ALI-PC";	    		                    // 支付宝PC支付
        public final static String ALI_WAP = "ALI-WAP";	    	                        // 支付宝WAP支付
        public final static String ALI_QR = "ALI-QR";	    	                        // 支付宝当面付之扫码支付
    }

    /**
     * 目前民生支付支持的类型
     */
    public static class CmbcConstant {
        public final static String CMBC_API_WXQRCODE = "API_WXQRCODE";
        public final static String CMBC_API_WXSCAN = "API_WXSCAN";
        public final static String CMBC_H5_WXJSAPI = "H5_WXJSAPI";
        public final static String CMBC_API_ZFBQRCODE = "API_ZFBQRCODE";
        public final static String CMBC_API_ZFBSCAN = "API_ZFBSCAN";
        public final static String CMBC_H5_ZFBJSAPI = "H5_ZFBJSAPI";
        public final static String CMBC_API_QQQRCODE = "API_QQQRCODE";
        public final static String CMBC_API_QQSCAN = "API_QQSCAN";
        public final static String CMBC_API_BDQRCODE = "API_BDQRCODE";
        public final static String CMBC_API_BDSCAN = "API_BDSCAN";
        public final static String CMBC_API_UNIONQRCODE = "API_UNIONQRCODE";
        public final static String CMBC_API_UNIONSCAN = "API_UNIONSCAN";
        public final static String CMBC_API_CMBCSCAN = "API_CMBCSCAN";
    }
}
