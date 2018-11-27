package com.icetech.paycenter.controller.notify;

import com.icetech.paycenter.service.INotificationService;
import com.icetech.paycenter.service.PayCenterServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 异步通知
 * @author wangzw
 */
@RestController
@RequestMapping("/cmbc")
public class Notification4CmbcController {
    private static final String CMBC_BENA="notification4CmbcServiceImpl";
    @Autowired
    private PayCenterServiceFactory payCenterServiceFactory;
    @PostMapping("notify/pay/{parkCode}")
    public String cmbcNotification(HttpServletRequest request) throws Exception{
        String context = (String) request.getAttribute("context");
        String parkCode = (String) request.getAttribute("parkCode");
        INotificationService notiFyServiceImpl = payCenterServiceFactory.getNotiFyServiceImpl(CMBC_BENA);
        String result = notiFyServiceImpl.doNotification(context,parkCode);
        return result;
    }
}
