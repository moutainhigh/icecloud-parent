package com.icetech.paycenter.controller;


import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.paycenter.common.config.WxConfig;
import com.icetech.paycenter.domain.normalpay.response.UnifiedOrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取授权码
 * @author wangzw
 */
@RestController
public class ThirdTestController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    WxConfig wxConfig;
    @RequestMapping("/thirdUrl")
    public String test(){
        logger.info("<请求车场> 调用中...");
        Map<String, String> map = new HashMap<>();
        map.put("status", "-1");
        return ResultTools.setResponse(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS), map);
    }
    @RequestMapping("/ali/pc")
    public String pay(HttpServletResponse response) throws IOException {
        return wxConfig.getBaseRefundNotifyUrl();
    }
}
