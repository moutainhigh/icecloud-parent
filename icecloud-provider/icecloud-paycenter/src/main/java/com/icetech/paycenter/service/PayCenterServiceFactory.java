package com.icetech.paycenter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付中心策略工厂
 * @author wangzw
 */
@Component
public class PayCenterServiceFactory {
    @Autowired
    private Map<String, IPayCenterService> payCenterServiceMap = new HashMap<>();
    @Autowired
    private Map<String,INotificationService> notificationServiceMap = new HashMap<>();

    /**
     * 获取相应的策略类型
     * @param type 不同的策略的 bean名称
     * @return
     */
    public IPayCenterService getPayServiceImpl(String type){
        return this.payCenterServiceMap.get(type);
    }
    /**
     * 获取相应的策略类型
     * @param type 不同的策略的 bean名称
     * @return
     */
    public INotificationService getNotiFyServiceImpl(String type){
        return this.notificationServiceMap.get(type);
    }
}
