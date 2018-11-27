package com.icetech.paycenter.service.handler;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取支付宝的用户信息
 * @author wangzw
 */
public class AliCodeHandler {
    protected static Logger logger = LoggerFactory.getLogger(AliCodeHandler.class);
    private static final String SERVER_URL = "https://openapi.alipay.com/gateway.do";
    private static final String FORMAT = "json";
    private static final String CHARSET = "UTF-8";
    private static final String SIGN_TYPE = "RSA2";
    private static final String AUTHORIZATION_CODE = "authorization_code";

    public static String getAccAndUserid(String alicode, String appId,String privateKey,String publicKey){
        AlipaySystemOauthTokenResponse oauthTokenResponse;
        AlipayClient alipayClient = new DefaultAlipayClient(SERVER_URL, appId, privateKey, FORMAT, CHARSET, publicKey, SIGN_TYPE);
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setCode(alicode);
        request.setGrantType(AUTHORIZATION_CODE);
        try {
            oauthTokenResponse = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            //处理异常
            e.printStackTrace();
            logger.error("<支付宝授权信息> 获取授权信息失败");
            return null;
        }
        return oauthTokenResponse.getAlipayUserId();
    }
}
