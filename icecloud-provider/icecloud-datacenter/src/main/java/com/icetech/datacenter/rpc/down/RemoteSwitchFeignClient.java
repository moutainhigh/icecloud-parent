package com.icetech.datacenter.rpc.down;

import com.icetech.api.datacenter.model.request.RemoteSwitchRequest;
import com.icetech.api.datacenter.service.RemoteSwitchFeignApi;
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
 * 远程控制开关闸接口
 *
 * @author fangct
 */
@Service
public class RemoteSwitchFeignClient implements RemoteSwitchFeignApi {

    private static final String SUCCESS = "SUCCESS";

    @Autowired
    private DownHandle downHandle;
    @Autowired
    private PublicHandle publicHandle;

    @Override
    public ObjectResponse remoteSwitch(RemoteSwitchRequest remoteSwitchRequest) {
        String parkCode = remoteSwitchRequest.getParkCode();
        remoteSwitchRequest.setParkCode(null);
        String messageId = downHandle.signAndSend(parkCode, DownServiceEnum.远程开关闸.getServiceName(),
                remoteSwitchRequest);
        AssertTools.notNull(messageId, CodeConstants.ERROR, "下发消息失败");
        String data = publicHandle.getDataFromRedis(messageId, DCTimeOutConstants.REMOTE_SWITCH_TIMEOUT);
        if (ToolsUtil.isNotNull(data) && SUCCESS.equals(data)){
            return ResponseUtils.returnSuccessResponse();
        }else{
            return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_3002);
        }
    }
}
