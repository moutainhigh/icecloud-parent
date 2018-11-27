package com.icetech.authcenter.server.rpc;

import com.icetech.api.authcenter.model.request.AuthConfig;
import com.icetech.api.authcenter.service.TokenFeignApi;
import com.icetech.authcenter.server.common.tool.AuthClientSign;
import com.icetech.authcenter.server.config.ClientConfig;
import com.icetech.authcenter.server.domain.AuthClient;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.util.ResponseUtils;
import com.icetech.common.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: lisc
 * @Date: 2018/10/25 13:50
 * @Description: token生成 刷新 验证 实现
 */
@Slf4j
@RestController
public class TokenFeignClient implements TokenFeignApi {
    @Autowired
    private ClientConfig clientConfig;
    @Autowired
    private AuthClientSign authClientSign;
    /**
     * 获得token
     *
     * @param authConfig
     * @return
     */
    @Override
    public ObjectResponse<String> auth(@RequestBody AuthConfig authConfig) {
        //校验
        try {
            if (!Validator.validate(authConfig)){
                return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_400);
            }
        } catch (Exception e) {
            return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_400);
        }
        AuthClient authClient = clientConfig.getByName(authConfig.getName()).orElse(null);
        if (authClient==null){
            return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_401,"服务不存在");
        }
        if (!authConfig.getSecret().equals(authClient.getSecret())){
            return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_401,"secret不正确");
        }
        //生成签名
        String token = authClientSign.sign(authConfig.getName());
        log.info("<服务认证>生成签名,服务名称={},token=[]",authConfig.getName(), token);
        return ResponseUtils.returnSuccessResponse(token);
    }



    /**
     * 刷新token
     *
     * @param token
     * @return
     */
    @Override
    public ObjectResponse<String> refreshToken(@RequestBody String token) {
        try {
            //验证签名
            String name = authClientSign.parseToken(token);
            if (name!=null){
                //生成签名
                token = authClientSign.sign(name);
                log.info("<服务认证>刷新签名,服务名称={},token=[]",name, token);
                return ResponseUtils.returnSuccessResponse(token);
            }
        } catch (Exception e) {
        }
        return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_401);
    }



    /**
     * token认证
     *
     * @param token
     * @return
     */
    @Override
    public ObjectResponse<Boolean> validateToken(@RequestBody String token) {
        try {
            //验证签名
            String name = authClientSign.parseToken(token);
            if (name!=null){
                log.info("<服务认证>验证签名,服务名称={},结果=[]",name,"true");
                return ResponseUtils.returnSuccessResponse(true);
            }
        } catch (Exception e) {
        }
        return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_401);
    }
}
