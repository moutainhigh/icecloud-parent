package com.icetech.paycenter.service.factory;

import com.icetech.paycenter.common.enumeration.UnionpayTransEnum;
import com.icetech.paycenter.domain.autopay.response.UnionpayResponse;

/**
 * 银联无感支付通知类响应工厂类
 * @author fangct
 */
public class UnionpayNotifyResponseFactory {

    public static UnionpayResponse createResponse(String transId){

        UnionpayResponse unionpayResponse = new UnionpayResponse();

        if (transId.equals(UnionpayTransEnum.签约状态推送.getRequestTransId())){
            unionpayResponse.setTransId(UnionpayTransEnum.签约状态推送.getResponseTransId());
        }else if (transId.equals(UnionpayTransEnum.车辆场内状态查询.getRequestTransId())){
            unionpayResponse.setTransId(UnionpayTransEnum.车辆场内状态查询.getResponseTransId());
        }
        return unionpayResponse;
    }

}
