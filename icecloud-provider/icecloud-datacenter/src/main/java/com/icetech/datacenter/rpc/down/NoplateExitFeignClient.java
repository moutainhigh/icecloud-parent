package com.icetech.datacenter.rpc.down;

import com.icetech.api.datacenter.model.request.NoplateExitRequest;
import com.icetech.api.datacenter.service.NoplateExitFeignApi;
import com.icetech.common.AssertTools;
import com.icetech.common.ToolsUtil;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.util.ResponseUtils;
import com.icetech.datacenter.common.constant.DCTimeOutConstants;
import com.icetech.datacenter.common.enumeration.DownServiceEnum;
import com.icetech.datacenter.service.handle.DownHandle;
import com.icetech.datacenter.service.handle.PublicHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 缴费通知下发接口
 *
 * @author fangct
 */
@Service
public class NoplateExitFeignClient implements NoplateExitFeignApi {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String SUCCESS = "SUCCESS";

    @Autowired
    private DownHandle downHandle;
    @Autowired
    private PublicHandle publicHandle;

    @Override
    public ObjectResponse noplateExit(NoplateExitRequest noplateExitRequest) {
        String parkCode = noplateExitRequest.getParkCode();
        noplateExitRequest.setParkCode(null);
        String messageId = downHandle.signAndSend(parkCode, DownServiceEnum.无牌车离场.getServiceName(),
                noplateExitRequest);
        AssertTools.notNull(messageId, CodeConstants.ERROR, "下发消息失败");
        String data = publicHandle.getDataFromRedis(messageId, DCTimeOutConstants.NOPLATE_TIMEOUT);
        if (ToolsUtil.isNotNull(data) && SUCCESS.equals(data)){
            return ResponseUtils.returnSuccessResponse();
        }else{
            return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_3002);
        }
    }


}
