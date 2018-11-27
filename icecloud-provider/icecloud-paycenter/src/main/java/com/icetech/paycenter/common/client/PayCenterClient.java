package com.icetech.paycenter.common.client;

import com.icetech.common.DateTools;
import com.icetech.common.HttpTools;
import com.icetech.common.SignTools;
import com.icetech.paycenter.domain.ThirdInfo;
import com.icetech.paycenter.domain.request.PayCenterBaseRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 支付中心的客户端请求
 * @author fangct
 */
@Component
public class PayCenterClient {
    private Logger logger = LoggerFactory.getLogger(PayCenterClient.class);
    /**
     * parkpay请求
     * @param baseRequest
     * @param thirdInfo 支付前台的配置参数
     * @return
     */
    public String sendPark(PayCenterBaseRequest baseRequest, ThirdInfo thirdInfo, String url) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        baseRequest.setTimestamp(DateTools.unixTimestamp());
        baseRequest.setPid(thirdInfo.getPid());
        Map<String, Object> map = SignTools.convertMap(baseRequest);
        String sign = SignTools.getMySign(map, thirdInfo.getSecretKey());
        map.put("sign", sign);
        String params = SignTools.map2SortJSON(map);
        String result = HttpTools.postJson(url, params);
        logger.info("<请求支付前台> 请求参数：{}，返回结果：{}", params, result);
        return result;
    }
}
