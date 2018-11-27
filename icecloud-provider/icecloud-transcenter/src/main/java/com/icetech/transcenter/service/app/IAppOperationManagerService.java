package com.icetech.transcenter.service.app;


import com.icetech.transcenter.domain.request.*;

import java.net.UnknownHostException;

/**
 * 运维app管理平台接口
 * @author wangzw
 */
public interface IAppOperationManagerService {
    /**
     * 车辆信息查询
     * @param request
     * @return
     */
    String searchCar(AppSearchCarRequest request);

    /**
     * 拉去费用
     * @param request
     * @return
     */
    String queryFee(AppPullFeeRequest request);

    /**
     * 下单获取二维码
     * @param request
     * @return
     */
    String doUnifiedOrder(AppUnifiedOrderRequest request) throws UnknownHostException;

    /**
     * 获取查询结果
     * @param request
     * @return
     */
    String queryPayResult(AppPayResultRequest request);

    /**
     * 预交费通知
     * @param request
     * @return
     */
    String notifyPrepay(AppPrePaymentNotifyRequest request);
}
