package com.icetech.datacenter.service.factory;

import com.icetech.api.datacenter.service.SendFeignApi;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.exception.ResponseBodyException;
import com.icetech.datacenter.common.enumeration.DownServiceEnum;
import com.icetech.datacenter.service.Spring;

/**
 * 获取下发业务实现类
 * @author fangct
 */
public class SendServiceFactory {

    private static final String SERVICE_SUFFIX = "ServiceImpl";

    public static SendFeignApi createSendService(Integer serviceType){
        String serviceName = DownServiceEnum.getServiceName(serviceType);
        SendFeignApi sendService = getServiceBean(serviceName);
        return sendService;
    }

    /**
     * 获取service实现类
     * @param prefix
     * @return
     */
    private static SendFeignApi getServiceBean(String prefix){
        String serviceName = prefix + SERVICE_SUFFIX;
        try {
            return Spring.getBean(serviceName);
        }catch(RuntimeException e){
            throw new ResponseBodyException(CodeConstants.ERROR_404, CodeConstants.getName(CodeConstants.ERROR_404));
        }
    }
}
