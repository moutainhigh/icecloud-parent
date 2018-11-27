package com.icetech.paycenter.rpc;

import com.icetech.api.paycenter.model.request.PayCenterBaseRequestDto;
import com.icetech.api.paycenter.service.AuthorizationFeignApi;
import com.icetech.common.SignTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.validator.Validator;
import com.icetech.paycenter.domain.ParkAlipay;
import com.icetech.paycenter.domain.ParkWeixin;
import com.icetech.paycenter.domain.ThirdInfo;
import com.icetech.paycenter.domain.request.PayCenterBaseRequest;
import com.icetech.paycenter.mapper.ParkAlipayDao;
import com.icetech.paycenter.mapper.ParkWeixinDao;
import com.icetech.paycenter.mapper.ThirdInfoDao;
import com.icetech.paycenter.service.handler.AliCodeHandler;
import com.icetech.paycenter.service.handler.WeiXinCodeHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class AuthorizationFeignClient implements AuthorizationFeignApi {
    @Autowired
    private ParkWeixinDao parkWeixinDao;
    @Autowired
    private ParkAlipayDao parkAlipayDao;
    @Autowired
    private ThirdInfoDao thirdInfoDao;

    @Override
    public ObjectResponse<String> getAuthLink4Wx(String parkCode) {
        //获取微信配置
        ParkWeixin parkWeixin = parkWeixinDao.selectByParkCode(parkCode);
        if (Objects.isNull(parkWeixin)){
            return new ObjectResponse(CodeConstants.ERROR_404,CodeConstants.getName(CodeConstants.ERROR_404));
        }
        //注: ##### 替换为回调地址;
        String authorUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
                "appid="+ parkWeixin.getAppId()+
                "&redirect_uri=#####" +
                "&response_type=code" +
                "&scope=snsapi_base" +
                "&state=STATE" +
                "&connect_redirect=1#wechat_redirect";
        return new ObjectResponse(CodeConstants.SUCCESS,CodeConstants.getName(CodeConstants.SUCCESS),authorUrl);
    }


    @Override
    public ObjectResponse<String> getAuthLink4Ali(String parkCode) {
        //获取微信配置
        ParkAlipay parkAlipay = parkAlipayDao.selectByParkCode(parkCode);
        if (Objects.isNull(parkAlipay)){
            return new ObjectResponse(CodeConstants.ERROR_404,CodeConstants.getName(CodeConstants.ERROR_404));
        }
        //
        //注: ##### 替换为回调地址;
        String authorUrl = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?" +
                "app_id="+parkAlipay.getAppId()+"" +
                "&scope=auth_base" +
                "&redirect_uri=ENCODED_URL";
        return new ObjectResponse(CodeConstants.SUCCESS,CodeConstants.getName(CodeConstants.SUCCESS),authorUrl);
    }

    @Override
    public ObjectResponse<String> getWxOpenId(String wxCode, String parkCode) {
        //获取微信配置
        ParkWeixin parkWeixin = parkWeixinDao.selectByParkCode(parkCode);
        if (Objects.isNull(parkWeixin)){
            return new ObjectResponse(CodeConstants.ERROR_404,CodeConstants.getName(CodeConstants.ERROR_404));
        }
        String openId = WeiXinCodeHandler.getWeiXinUserInfo(wxCode, parkWeixin.getAppId(), parkWeixin.getAppSecret());
        if (StringUtils.isEmpty(openId)){
            return new ObjectResponse(CodeConstants.ERROR,CodeConstants.getName(CodeConstants.ERROR));
        }
        return new ObjectResponse(CodeConstants.SUCCESS,CodeConstants.getName(CodeConstants.SUCCESS),openId);
    }

    @Override
    public ObjectResponse<String> getAliUserId(String wxCode, String parkCode) {
        //获取微信配置
        ParkAlipay parkAlipay = parkAlipayDao.selectByParkCode(parkCode);
        if (Objects.isNull(parkAlipay)){
            return new ObjectResponse(CodeConstants.ERROR_404,CodeConstants.getName(CodeConstants.ERROR_404));
        }
        String userId = AliCodeHandler.getAccAndUserid(wxCode, parkAlipay.getAppId(), parkAlipay.getPrivateKey(),parkAlipay.getAliPublicKey());
        if (StringUtils.isEmpty(userId)){
            return new ObjectResponse(CodeConstants.ERROR,CodeConstants.getName(CodeConstants.ERROR));
        }
        return new ObjectResponse(CodeConstants.SUCCESS,CodeConstants.getName(CodeConstants.SUCCESS),userId);
    }

    @Override
    public ObjectResponse<Boolean> verifySign(PayCenterBaseRequestDto baseRequestDto) {
        try {
            PayCenterBaseRequest baseRequest = new PayCenterBaseRequest();
            BeanUtils.copyProperties(baseRequestDto, baseRequest);
            if (Validator.validate(baseRequestDto)){
                String pid = baseRequest.getPid();
                ThirdInfo thirdInfo = new ThirdInfo();
                thirdInfo.setPid(pid);
                thirdInfo = thirdInfoDao.selectById(thirdInfo);
                String secretKey = thirdInfo.getSecretKey();
                return  new ObjectResponse(CodeConstants.SUCCESS,CodeConstants.getName(CodeConstants.SUCCESS),SignTools.verifyMD5Sign(baseRequest, secretKey));
            }else{
                return new ObjectResponse(CodeConstants.ERROR_400,CodeConstants.getName(CodeConstants.ERROR_400));
            }
        }catch (Exception e){
            return new ObjectResponse(CodeConstants.ERROR,CodeConstants.getName(CodeConstants.ERROR));
        }
    }
}
