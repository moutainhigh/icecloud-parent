package com.icetech.bizcenter.controller;

import com.icetech.api.bizcenter.service.TestFeignApi;
import com.icetech.authcenter.client.annotation.IgnoreToken;
import com.icetech.authcenter.client.annotation.RequireToken;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.util.ResponseUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @Auther: lisc
 * @Date: 2018/11/1 15:59
 * @Description:
 */
@RestController
public class TestFeignApiController implements TestFeignApi {

    @Override
    public ObjectResponse apiTest(){
        HashMap<String, String> map = new HashMap<>();
        map.put("你好","世界");
        return ResponseUtils.returnSuccessResponse();
    }

    @IgnoreToken
    @Override
    public ObjectResponse otherTest(){
        HashMap<String, String> map = new HashMap<>();
        map.put("你好","世界");
        return  ResponseUtils.returnSuccessResponse();
    }

    @RequireToken
    @Override
    public ObjectResponse otherTest2() {
        HashMap<String, String> map = new HashMap<>();
        map.put("你好","世界");
        return  ResponseUtils.returnSuccessResponse();
    }

    @Override
    public ObjectResponse otherTest3() {
        HashMap<String, String> map = new HashMap<>();
        map.put("你好","世界");
        return  ResponseUtils.returnSuccessResponse();
    }
}
