package com.icetech.paycenter.service.factory;

import com.icetech.common.DataChangeTools;
import com.icetech.paycenter.common.enumeration.UnionpayTransEnum;
import com.icetech.paycenter.domain.autopay.request.UnionpayBaseNotifyRequest;
import com.icetech.paycenter.domain.autopay.request.UnionpayFindCarStatusRequest;
import com.icetech.paycenter.domain.autopay.request.UnionpaySignedNotifyRequest;

import java.util.Map;

/**
 * 银联无感支付通知类request工厂类
 * @author fangct
 */
public class UnionpayNotifyRequestFactory {

    public static UnionpayBaseNotifyRequest createRequest(String transId, Map<String, Object> paramMap){

        String json = DataChangeTools.bean2gson(paramMap);

        UnionpayBaseNotifyRequest unionpayBaseNotifyRequest = null;
        if (transId.equals(UnionpayTransEnum.签约状态推送.getRequestTransId())){
            unionpayBaseNotifyRequest = DataChangeTools.gson2bean(json, UnionpaySignedNotifyRequest.class);
        }else if (transId.equals(UnionpayTransEnum.车辆场内状态查询.getRequestTransId())){
            unionpayBaseNotifyRequest = DataChangeTools.gson2bean(json, UnionpayFindCarStatusRequest.class);
        }
        return unionpayBaseNotifyRequest;
    }
}
