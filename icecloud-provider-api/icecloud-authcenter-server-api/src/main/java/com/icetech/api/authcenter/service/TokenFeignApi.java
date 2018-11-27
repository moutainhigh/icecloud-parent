package com.icetech.api.authcenter.service;

import com.icetech.api.authcenter.model.request.AuthConfig;
import com.icetech.api.authcenter.service.hystrix.TokenFeignApiHystrix;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: lisc
 * @Date: 2018/10/25 09:38
 * @Description: 服务鉴权
 */
@FeignClient(value = "icecloud-authcenter-server", fallback = TokenFeignApiHystrix.class)
public interface TokenFeignApi {
    /**
     * 获得token
     * @param authConfig
     * @return
     */
    @PostMapping("/api/token/auth")
    ObjectResponse<String> auth(@RequestBody AuthConfig authConfig);

    /**
     * 刷新token
     * @param token
     * @return
     */
    @PostMapping("/api/token/refresh")
    ObjectResponse<String> refreshToken(@RequestBody String token);

    /**
     * token认证
     * @param token
     * @return
     */
    @PostMapping("/api/token/validate")
    ObjectResponse<Boolean> validateToken(@RequestBody String token);


}
