package com.icetech.paycenter.service.factory;

import com.icetech.paycenter.common.enumeration.UnionpayTransEnum;
import com.icetech.paycenter.service.Spring;
import com.icetech.paycenter.service.autopay.UnionpayNotifyService;

/**
 * 银联无感支付通知类service工厂类
 * @author fangct
 */
public class UnionpayNotifyServiceFactory {

    public static UnionpayNotifyService createBean(String transId){

        String beanName = "";

        if (transId.equals(UnionpayTransEnum.签约状态推送.getRequestTransId())){
            beanName = UnionpayTransEnum.签约状态推送.getBeanName();
        }else if (transId.equals(UnionpayTransEnum.车辆场内状态查询.getRequestTransId())){
            beanName = UnionpayTransEnum.车辆场内状态查询.getBeanName();
        }
        return Spring.getBean(beanName);
    }

}
