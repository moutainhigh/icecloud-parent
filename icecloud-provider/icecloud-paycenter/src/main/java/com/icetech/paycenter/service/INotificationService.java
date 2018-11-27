package com.icetech.paycenter.service;

public interface INotificationService {

    /**
     * 接受第三方的异步通知,发送付款通知
     * @param context
     * @return
     */
    String doNotification(String context, String parkCode) throws Exception;
}
