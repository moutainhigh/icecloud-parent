package com.icetech.authcenter.client.task;

import com.icetech.api.authcenter.model.request.AuthConfig;
import com.icetech.api.authcenter.service.TokenFeignApi;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Auther: lisc
 * @Date: 2018/10/24 18:34
 * @Description: 定时任务自动去鉴权中心获得token或者更新token
 */
@Slf4j
@Component
public class TokenScheduledTask {
    public final static long ONE_MINUTE = 50 * 60 * 1000;

    @Autowired
    private AuthConfig authConfig;
    @Autowired
    private TokenFeignApi tokenFeignApi;


    @Scheduled(fixedDelay = ONE_MINUTE)
    public void reloadApiToken() {
        String token =  System.getProperty("auth.token");
        if (StringUtils.isBlank(token)){
            while (StringUtils.isBlank(token)) {
                try {
                    Thread.sleep(1000);
                    ObjectResponse<String> response = tokenFeignApi.auth(authConfig);
                    ResponseUtils.notError(response);
                    token = response.getData();
                } catch (Exception e) {
                    log.error("<服务鉴权>获取token失败", e);
                }
            }
        }else {
            while (true) {
                try {
                    Thread.sleep(1000);
                    ObjectResponse<String> response = tokenFeignApi.refreshToken(token);
                    ResponseUtils.notError(response);
                    token = response.getData();
                    break;
                } catch (InterruptedException e) {
                    log.error("<服务鉴权>刷新token失败", e);
                    //防止超时等token失效情况重复刷新进行递归重试
                    System.setProperty("auth.token","");
                    reloadApiToken();
                    break;
                }
            }
        }
        System.setProperty("auth.token", token);
    }

}
