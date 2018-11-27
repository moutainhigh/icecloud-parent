package com.icetech.api.authcenter.service.hystrix;

import com.icetech.api.authcenter.service.TokenFeignApi;
import com.icetech.api.authcenter.model.request.AuthConfig;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.util.ResponseUtils;
import org.springframework.stereotype.Component;

/**
 * @Auther: lisc
 * @Date: 2018/10/25 10:13
 * @Description: 服务鉴权错误处理
 */
@Component
public class TokenFeignApiHystrix implements TokenFeignApi {

    /**
     * 获得token
     *
     * @param authConfig
     * @return
     */
    @Override
    public ObjectResponse<String> auth(AuthConfig authConfig) {
        return ResponseUtils.returnErrorResponse(CodeConstants.ERROR);
    }

    /**
     * 刷新token
     *
     * @param token
     * @return
     */
    @Override
    public ObjectResponse<String> refreshToken(String token) {
        return ResponseUtils.returnErrorResponse(CodeConstants.ERROR);
    }

    /**
     * token认证
     *
     * @param token
     * @return
     */
    @Override
    public ObjectResponse<Boolean> validateToken(String token) {
        return ResponseUtils.returnErrorResponse(CodeConstants.ERROR);
    }
}
