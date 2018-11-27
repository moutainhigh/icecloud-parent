package com.icetech.datacenter.service.handle;

import com.icetech.common.ToolsUtil;
import com.icetech.datacenter.common.constant.DCTimeOutConstants;
import com.icetech.datacenter.common.redis.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PublicHandle {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 间隔查询时间，50ms
     */
    private static final int INTERVAL = 50;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 获取data信息
     * @param messageId
     * @param timeOut
     * @return
     */
    public String getDataFromRedis(String messageId, Long timeOut){
        Long currentTimeMillis = System.currentTimeMillis();
        Long lastTime = currentTimeMillis + DCTimeOutConstants.QUERYFEE_TIMEOUT;
        int n = 1;
        //查询redis中是否返回
        while (lastTime > currentTimeMillis) {

            if (redisUtils.isValidity()){
                String data = (String) redisUtils.get(messageId);
                if (ToolsUtil.isNotNull(data)){
                    logger.info("第{}次从redis中读取到了msgid：{}响应的信息！", n , messageId);
                    return data;
                }
            }
            try {
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentTimeMillis = System.currentTimeMillis();
            n ++;
        }
        logger.info("时限内未查询到msgid：{}响应的信息！", messageId);
        return null;
    }
}
