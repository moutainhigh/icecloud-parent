package com.icetech.common.constants;

import java.util.HashMap;
import java.util.Map;


/**
 * @author fangct
 */
public class CodeConstants {
    /**
     * 成功
     */
    public final static String SUCCESS = "200";
    /**
     * 服务器异常
     */
    public final static String ERROR = "500";
    /**
     * 缺失参数或参数格式不正确
     */
    public final static String ERROR_400 = "400";
    /**
     * 签名验证失败
     */
    public final static String ERROR_401= "401";
    /**
     * 请求失败,参数格式正确但业务错误
     */
    public final static String ERROR_402= "402";
    /**
     * 请求方式错误
     */
    public final static String ERROR_403 = "403";
    /**
     * 请求资源不存在
     */
    public final static String ERROR_404 = "404";
    /**
     * 请求重复
     */
    public final static String ERROR_405 = "405";
    /**
     * 下单系统异常
     */
    public final static String ERROR_1100 = "1100";
    /**
     * 无感支付业务失败
     */
    public final static String ERROR_2100 = "2100";
    /**
     * 无感支付扣费失败
     */
    public final static String ERROR_2001 = "2001";
    /**
     * 用户未授权无感支付
     */
    public final static String ERROR_2002 = "2002";
    /**
     * 退款状态不合法
     */
    public final static String ERROR_2003 = "2003";
    /**
     * 车辆有未离场记录
     */
    public final static String ERROR_2004 = "2004";
    /**
     * 退款金额不合法
     */
    public final static String ERROR_2005 = "2005";
    /**
     * 查询费用失败
     */
    public final static String ERROR_3001 = "3001";
    /**
     * 开闸失败
     */
    public final static String ERROR_3002 = "3002";

    public static Map<String, String> map = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put(SUCCESS, "成功");
            put(ERROR, "服务器异常");
            put(ERROR_400, "缺失参数/格式不正确");
            put(ERROR_401, "认证失败");
            put(ERROR_402, "非法参数");
            put(ERROR_403, "请求方式错误");
            put(ERROR_405, "请求重复");
            put(ERROR_404, "请求资源不存在");
            put(ERROR_1100, "下单系统异常");
            put(ERROR_2100, "无感支付业务失败");
            put(ERROR_2001, "无感支付扣费失败");
            put(ERROR_2002 ,"用户未授权无感支付");
            put(ERROR_2003 ,"退款状态不合法");
            put(ERROR_2004 ,"车辆有未离场记录");
            put(ERROR_2005 ,"退款金额不合法");
            put(ERROR_3001 ,"查询费用失败");
            put(ERROR_3002 ,"开闸失败");
        }
    };


    public static String getName(String code) {
        return map.get(code);
    }

    public static String getCode(String name) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (name.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return "";
    }

}
