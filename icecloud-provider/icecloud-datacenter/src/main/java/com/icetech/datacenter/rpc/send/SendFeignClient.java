package com.icetech.datacenter.rpc.send;

import com.icetech.api.datacenter.model.request.SendRequest;
import com.icetech.api.datacenter.service.SendFeignApi;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.exception.ResponseBodyException;
import com.icetech.common.validator.Validator;
import com.icetech.datacenter.service.factory.SendServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 统一下发实现
 * @author fangct
 */
@Service
public class SendFeignClient implements SendFeignApi {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public ObjectResponse send(SendRequest sendRequest) {

        try {
            //检验参数
            if (Validator.validate(sendRequest)){
                SendFeignApi sendService = SendServiceFactory.createSendService(sendRequest.getServiceType());
                return sendService.send(sendRequest);
            }else{
                return new ObjectResponse(CodeConstants.ERROR_400, CodeConstants.getName(CodeConstants.ERROR_400));
            }
        }catch (ResponseBodyException e){
            return new ObjectResponse(e.getErrCode(), e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("<下发业务> 出现异常，参数：{}, error：{}", sendRequest, e.getMessage());
            return new ObjectResponse(CodeConstants.ERROR, CodeConstants.getName(CodeConstants.ERROR));
        }
    }
}
