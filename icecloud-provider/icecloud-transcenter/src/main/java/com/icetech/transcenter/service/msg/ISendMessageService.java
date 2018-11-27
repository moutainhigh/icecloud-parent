package com.icetech.transcenter.service.msg;

import com.icetech.transcenter.domain.ApiBaseRequest;

public interface ISendMessageService {
    /**
     * 短信发送接口
     * @param apiBaseRequest
     * @return
     * @throws Exception
     */
    String sendMessage(ApiBaseRequest apiBaseRequest) throws Exception;
}
