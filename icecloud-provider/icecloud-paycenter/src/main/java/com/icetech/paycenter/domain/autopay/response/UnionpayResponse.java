package com.icetech.paycenter.domain.autopay.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 银联免密支付公共响应参数
 * @author fangct
 */
@Setter
@Getter
public class UnionpayResponse implements Serializable {

    /**
     * 业务代码
     */
    private String TransId;
    /**
     *交易结果
     */
    private String ResultCode;
    /**
     *失败结果描述
     */
    private String ResultMsg;
    /**
     *备注
     */
    private String Remark;

}
