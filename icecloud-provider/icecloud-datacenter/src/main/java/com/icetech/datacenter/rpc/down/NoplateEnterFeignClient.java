package com.icetech.datacenter.rpc.down;

import com.icetech.api.datacenter.model.request.NoplateEnterRequest;
import com.icetech.api.datacenter.service.NoplateEnterFeignApi;
import com.icetech.common.AssertTools;
import com.icetech.common.ToolsUtil;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.util.ResponseUtils;
import com.icetech.datacenter.common.constant.DCTimeOutConstants;
import com.icetech.datacenter.common.enumeration.DownServiceEnum;
import com.icetech.datacenter.service.handle.DownHandle;
import com.icetech.datacenter.service.handle.PublicHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 缴费通知下发接口
 *
 * @author fangct
 */
@Service
public class NoplateEnterFeignClient implements NoplateEnterFeignApi {

    private static final String SUCCESS = "SUCCESS";

    @Autowired
    private DownHandle downHandle;
    @Autowired
    private PublicHandle publicHandle;

    @Override
    public ObjectResponse noplateEnter(NoplateEnterRequest noplateEnterRequest) {
        String parkCode = noplateEnterRequest.getParkCode();
        noplateEnterRequest.setParkCode(null);
        String messageId = downHandle.signAndSend(parkCode, DownServiceEnum.无牌车入场.getServiceName(),
                noplateEnterRequest);
        AssertTools.notNull(messageId, CodeConstants.ERROR, "下发消息失败");
        String data = publicHandle.getDataFromRedis(messageId, DCTimeOutConstants.NOPLATE_TIMEOUT);
        if (ToolsUtil.isNotNull(data) && SUCCESS.equals(data)){
            return ResponseUtils.returnSuccessResponse();
        }else{
            return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_3002);
        }
    }
}
