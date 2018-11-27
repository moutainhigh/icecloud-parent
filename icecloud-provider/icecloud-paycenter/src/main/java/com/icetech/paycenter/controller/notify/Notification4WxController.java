package com.icetech.paycenter.controller.notify;

import com.icetech.paycenter.service.INotificationService;
import com.icetech.paycenter.service.PayCenterServiceFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 异步通知
 * @author wangzw
 */
@RestController
@RequestMapping("/wx")
public class Notification4WxController {
    private static final String WX_Pay_NOTIFY_BEAN="notification4WxPayServiceImpl";
    private static final String WX_REFUND_NOTIFY_BEAN="notification4WxRefundServiceImpl";
    @Autowired
    private PayCenterServiceFactory payCenterServiceFactory;

    @PostMapping("notify/pay/{parkCode}")
    @ResponseBody
    public String wxPayNotification(HttpServletRequest request, @PathVariable("parkCode") String parkCode) throws Exception{
        String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        INotificationService notifyServiceImpl = payCenterServiceFactory.getNotiFyServiceImpl(WX_Pay_NOTIFY_BEAN);
        String result = notifyServiceImpl.doNotification(xmlResult,parkCode);
        return result;
    }

    @PostMapping("notify/refund/{parkCode}")
    @ResponseBody
    public String wxRefundNotification(HttpServletRequest request, @PathVariable("parkCode") String parkCode) throws Exception{
        String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        INotificationService notifyServiceImpl = payCenterServiceFactory.getNotiFyServiceImpl(WX_REFUND_NOTIFY_BEAN);
        String result = notifyServiceImpl.doNotification(xmlResult,parkCode);
        return result;
    }
}
