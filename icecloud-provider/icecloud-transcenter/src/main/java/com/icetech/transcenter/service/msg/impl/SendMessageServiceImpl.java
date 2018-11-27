package com.icetech.transcenter.service.msg.impl;

import com.google.common.collect.Lists;
import com.icetech.common.DataChangeTools;
import com.icetech.common.DateTools;
import com.icetech.common.HttpTools;
import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.util.StringUtils;
import com.icetech.transcenter.common.enumeration.SmsResponseCode;
import com.icetech.transcenter.config.msg.MsgConfig;
import com.icetech.transcenter.domain.ApiBaseRequest;
import com.icetech.transcenter.domain.request.SmsRequest;
import com.icetech.transcenter.domain.response.SmsResponse;
import com.icetech.transcenter.domain.response.SmsResponseInfo;
import com.icetech.transcenter.service.msg.ISendMessageService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.*;

/**
 * 发送短信
 * @author wangzw
 */
@Service
public class SendMessageServiceImpl implements ISendMessageService {
    private static final Logger logger = LoggerFactory.getLogger(SendMessageServiceImpl.class);
    @Autowired
    private MsgConfig msgConfig;
    @Override
    public String sendMessage(ApiBaseRequest baseRequest) throws Exception {
        //校验参数
        SmsRequest smsRequest = DataChangeTools.convert2bean(baseRequest.getBizContent(), SmsRequest.class);
        Calendar calendar=Calendar.getInstance();
        Date currentTime = new Date();
        calendar.setTime(currentTime);
        calendar.add(Calendar.SECOND,smsRequest.getTimes());
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("OperID", msgConfig.getOperId());
        params.put("OperPass",msgConfig.getOperPass());
        params.put("SendTime",DateTools.getFormat("yyyyMMddHHmmss", currentTime));
        params.put("ValidTime", DateTools.getFormat("yyyyMMddHHmmss",calendar.getTime()));
        params.put("AppendID",msgConfig.getAppendNumber());
        params.put("DesMobile",smsRequest.getDesMobile());
        params.put("Content",URLEncoder.encode(smsRequest.getContent(),"GBK"));
        String response = HttpTools.get(msgConfig.getUrl() + "?" + StringUtils.getUrlParamsByMap(params));
        SmsResponse smsResponse = buildResponseWithXml(response);
        if (smsResponse.getCode().equals(SmsResponseCode.ERROR_00.getCode())
                ||smsResponse.getCode().equals(SmsResponseCode.ERROR_01.getCode())
                ||smsResponse.getCode().equals(SmsResponseCode.ERROR_03.getCode())){
            return ResultTools.setResponse(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS),smsResponse.getMessage());
        }else if (!smsResponse.getCode().equals(SmsResponseCode.ERROR_500.getCode())){
            return ResultTools.setResponse(CodeConstants.ERROR_402, SmsResponseCode.getDescByCode(smsResponse.getCode()));
        }
        return ResultTools.setResponse(CodeConstants.ERROR, CodeConstants.getName(CodeConstants.ERROR));
    }


    /**
     * 解析xml封装对象
     * @param xml
     * @return
     * @throws DocumentException
     */
    private SmsResponse buildResponseWithXml(String xml) throws DocumentException {
        SmsResponse smsResponse = new SmsResponse();
        Document doc = DocumentHelper.parseText(xml);
        Element rootElt = doc.getRootElement();
        //获取code
        Iterator code = rootElt.elementIterator("code");
        Element codeEle = (Element) code.next();
        smsResponse.setCode(codeEle.getText());
        //获取message
        Iterator messages = rootElt.elementIterator("message");
        List<SmsResponseInfo> messageInfo = Lists.newArrayList();
        while (messages.hasNext()) {
            SmsResponseInfo smsResponseInfo = new SmsResponseInfo();
            Element recordEle = (Element) messages.next();
            String desmobile = recordEle.elementTextTrim("desmobile");
            smsResponseInfo.setDesmobile(desmobile);
            String msgid = recordEle.elementTextTrim("msgid");
            smsResponseInfo.setMsgid(msgid);
            messageInfo.add(smsResponseInfo);
        }
        smsResponse.setMessage(messageInfo);
        return smsResponse;
    }
}
