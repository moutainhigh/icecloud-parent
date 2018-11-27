package com.icetech.transcenter.controller;

import com.icetech.common.SignTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.exception.ResponseBodyException;
import com.icetech.common.validator.Validator;
import com.icetech.transcenter.domain.ApiBaseRequest;
import com.icetech.transcenter.domain.ThirdInfo;
import com.icetech.transcenter.mapper.ThirdInfoDao;
import com.icetech.transcenter.service.msg.ISendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息发送
 * @author wangzw
 */
@RestController
@RequestMapping("/msg")
public class SendMessageController {

    @Autowired
    private ISendMessageService sendMessageService;
    @Autowired
    private ThirdInfoDao thirdInfoDao;

    /**
     * 接受短信下发请求
     * @param apiBaseRequest
     * @return
     * @throws Exception
     */
    @PostMapping("sms")
    public String sendSmsMsg(@RequestBody ApiBaseRequest apiBaseRequest) throws Exception {
        //验证签名
        verifySign(apiBaseRequest);
        String response = sendMessageService.sendMessage(apiBaseRequest);
        return response;
    }

    /**
     * 验证签名
     * @param baseRequest
     */
    private void verifySign(ApiBaseRequest baseRequest) throws NoSuchFieldException, IllegalAccessException {
        if (Validator.validate(baseRequest)){
            String pid = baseRequest.getPid();
            ThirdInfo thirdInfo = new ThirdInfo();
            thirdInfo.setPid(pid);
            thirdInfo = thirdInfoDao.selectById(thirdInfo);
            String secretKey = thirdInfo.getSecretKey();
            boolean verifyResult = SignTools.verifyMD5Sign(baseRequest, secretKey);
            if (!verifyResult) {
                throw new ResponseBodyException(CodeConstants.ERROR_401, "验签未通过");
            }
        }else{
            throw new ResponseBodyException(CodeConstants.ERROR_400, "必填参数为空");
        }
    }
}
