package com.icetech.paycenter.controller.notify;

import com.icetech.common.JsonTools;
import com.icetech.paycenter.service.INotificationService;
import com.icetech.paycenter.service.PayCenterServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 异步通知
 * @author wangzw
 */
@RestController
@RequestMapping("/ali")
public class Notification4AliController {
    private static final String ALI_Pay_NOTIFY_BEAN="notification4AliPayServiceImpl";
    @Autowired
    private PayCenterServiceFactory payCenterServiceFactory;

    @PostMapping("notify/pay/{parkCode}")
    @ResponseBody
    public String aLiPayNotification(HttpServletRequest request, @PathVariable("parkCode") String parkCode) throws Exception{

        INotificationService notifyServiceImpl = payCenterServiceFactory.getNotiFyServiceImpl(ALI_Pay_NOTIFY_BEAN);
        String result = notifyServiceImpl.doNotification(doAliPayRes(request),parkCode);
        return result;
    }

    public String doAliPayRes(HttpServletRequest request) throws ServletException, IOException {
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        return JsonTools.toString(params);
    }
}
