package com.icetech.transcenter.service.mq;

import com.icetech.common.DateTools;
import com.icetech.common.JsonTools;
import com.icetech.common.SignTools;
import com.icetech.common.UUIDTools;
import com.icetech.transcenter.config.DefaultThirdConfig;
import com.icetech.transcenter.domain.AppBaseRequest;
import com.icetech.transcenter.domain.request.AppVioceCallRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author
 * @create 2018-10-14 13:58
 **/
@Service
public class CameraRequestService {
    private static String serviceName = "voiceCall";
    @Autowired
    private DefaultThirdConfig defaultThirdConfig;

    @Autowired
    private MqSendMqttServer mqSendMqttServer;
    public Boolean sendMsg4App(String uid) throws Exception {
        //创建请求体
        String body = createBody(uid);
        return mqSendMqttServer.sendMessage(defaultThirdConfig.getParkCode(), body);
    }

    private String createBody(String uid) throws Exception {
        AppBaseRequest<AppVioceCallRequest> request = new AppBaseRequest<>();
        AppVioceCallRequest appVioceCallRequest = new AppVioceCallRequest();
        appVioceCallRequest.setParkCode(defaultThirdConfig.getParkCode());
        appVioceCallRequest.setSnNo(uid);
        request.setBizContent(appVioceCallRequest);
        request.setPid(defaultThirdConfig.getPid());
        request.setServiceName(serviceName);
        request.setTimestamp(DateTools.unixTimestamp());
        request.setMessageId(UUIDTools.getUuid());
        //设置签名
        Map<String, Object> map = SignTools.convertMap(request);
        String sign = SignTools.getMySign(map,defaultThirdConfig.getKey());
        request.setSign(sign);
        return JsonTools.toString(request);
    }
}
