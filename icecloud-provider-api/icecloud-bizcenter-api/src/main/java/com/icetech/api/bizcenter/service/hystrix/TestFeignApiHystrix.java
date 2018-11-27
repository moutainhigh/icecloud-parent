package com.icetech.api.bizcenter.service.hystrix;

import com.icetech.api.bizcenter.service.TestFeignApi;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Auther: lisc
 * @Date: 2018/11/1 17:19
 * @Description:
 */
@Component
public class TestFeignApiHystrix implements TestFeignApi {
    @Override
    public ObjectResponse apiTest() {
        return null;
    }

    @Override
    public ObjectResponse otherTest() {
        return null;
    }

    @Override
    public ObjectResponse otherTest2() {
        return null;
    }

    @Override
    public ObjectResponse otherTest3() {
        return null;
    }
}
