package com.icetech.paycenter.service.handler;

import com.icetech.common.HttpTools;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 微信授权获取用户信息
 * @author wangzw
 */
public class WeiXinCodeHandler {
    protected static Logger logger = LoggerFactory.getLogger(AliCodeHandler.class);

    public static String getWeiXinUserInfo(String weixinCode, String appId,String appSecret){
        //获取 openId
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appId+"&secret="+appSecret+"&code=" + weixinCode +"&grant_type=authorization_code";
        String response = HttpTools.get(url);
        JSONObject jsonObject;
        String openId;
        try {
            jsonObject = new JSONObject(response);
            openId = jsonObject.get("openid").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return openId;
    }
}
