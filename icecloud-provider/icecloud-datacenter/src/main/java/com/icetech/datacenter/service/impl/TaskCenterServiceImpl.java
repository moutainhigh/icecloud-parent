package com.icetech.datacenter.service.impl;

import com.icetech.api.taskcenter.service.SendInfoFeignApi;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.response.ObjectResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 通知定时任务结果
 * @author fangct
 */
@Service
public class TaskCenterServiceImpl{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SendInfoFeignApi sendInfoService;

    /***
     * 通知定时任务
     * @param serviceType
     * @param serviceId
     * @return
     */
    public String notify(Integer serviceType, Integer serviceId){
        try {
            ObjectResponse objectResponse = sendInfoService.notifySuccess(serviceType, serviceId);
            if (objectResponse != null && objectResponse.getCode().equals(CodeConstants.SUCCESS)){
                return CodeConstants.SUCCESS;
            }else{
                logger.info("<通知定时任务> 返回未成功，serviceType:{}, serviceId:{}", serviceType, serviceId);
                return CodeConstants.ERROR;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("<通知定时任务> 异常，serviceType：{}，serviceId：{}，errorMsg：{}", serviceType, serviceId, e.getMessage());
            return CodeConstants.ERROR;
        }

    }
}
