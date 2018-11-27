package com.icetech.api.bizcenter.service;

import com.icetech.api.bizcenter.service.hystrix.TestFeignApiHystrix;
import com.icetech.common.domain.response.ObjectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * @Auther: lisc
 * @Date: 2018/11/1 17:17
 * @Description:
 */
@FeignClient(value = "icecloud-bizcenter" ,fallback = TestFeignApiHystrix.class)
public interface TestFeignApi {

    @GetMapping("/api/test/token")
    ObjectResponse apiTest();

    @GetMapping("/api/test")
    ObjectResponse otherTest();

    @GetMapping("/other/test/token")
    ObjectResponse otherTest2();

    @GetMapping("/other/test")
    ObjectResponse otherTest3();
}
