package com.icetech.datacenter.service.mqtt;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.icetech.api.cloudcenter.service.ParkFeignApi;
import com.icetech.common.DataChangeTools;
import com.icetech.common.ToolsUtil;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.constants.RedisKeyConstants;
import com.icetech.common.constants.TimeOutConstants;
import com.icetech.common.domain.Park;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.util.ResponseUtils;
import com.icetech.datacenter.common.enumeration.DownServiceEnum;
import com.icetech.datacenter.common.enumeration.SendOperTypeEnum;
import com.icetech.datacenter.common.redis.RedisUtils;
import com.icetech.datacenter.dao.SendinfoRecordDao;
import com.icetech.datacenter.domain.SendinfoRecord;
import com.icetech.datacenter.service.impl.TaskCenterServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * MQTT消费者监听
 * @author fangct
 */
@Component
public class MqRecvMqttListener implements MessageListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private TaskCenterServiceImpl taskCenterService;
    @Autowired
    private ParkFeignApi parkService;
    @Autowired
    private SendinfoRecordDao sendinfoRecordDao;

    /**
     * 同步返回的服务
     */
    private static final String excludes = DownServiceEnum.缴费查询.getServiceName() + "," +
            DownServiceEnum.无牌车入场.getServiceName() + "," +
            DownServiceEnum.无牌车离场.getServiceName() +"," +
            DownServiceEnum.远程开关闸.getServiceName() ;

    //成功标识
    private static final String SUCCESS = "SUCCESS";

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        try {
            // 解析mq内容
            if (null != message && message.getBody().length > 0) {
                String body = new String(message.getBody(), "utf-8");
                logger.info("<MQ监听> Receive msgbody:{}", body);
                Map<String, String> paramsMap = DataChangeTools.json2MapString(body);

                String messageId = paramsMap.get("messageId");
                String parkCode = paramsMap.get("parkCode");
                String serviceName = paramsMap.get("serviceName");
                String code = paramsMap.get("code");
                String msg = paramsMap.get("msg");
                String data = paramsMap.get("data");

                int flag = 0;//1：普通回写（需要通知TimeTask）2：不需要通知TimeTask
                //验证入参有效性
                if (ToolsUtil.isNull(messageId) || ToolsUtil.isNull(serviceName) ||ToolsUtil.isNull(code)) {
                    logger.info("<MQ监听> 本地响应参数不合法, msgid:{}", messageId);
                    // 获取业务类型 缴费和开关闸 写入redis
                    if (excludes.contains(serviceName) && messageId != null) {
                        // 写入redis空值
                        redisUtils.set(messageId, data, TimeOutConstants.REDIS_TIMEOUT);
                    }
                }else{
                    // 获取业务类型 缴费和开关闸 写入redis
                    if (excludes.contains(serviceName)) {
//                        flag = 2;
                        if(code.equals(CodeConstants.SUCCESS)){//如果本地处理成功
                            // 写入redis
                            String value = "";
                            if (ToolsUtil.isNotNull(data)){
                                value = data;
                            }else{
                                value = SUCCESS;
                            }
                            redisUtils.set(messageId, value, TimeOutConstants.REDIS_TIMEOUT);
                            logger.info("<MQ监听> 写入redis成功，key：{}，value：{}", messageId, value);
                        } else {// 其他下发修改数据库状态
                            // 写入redis, 置空，为了可以尽快根据msgid获取到结果
                            redisUtils.set(messageId, null, TimeOutConstants.REDIS_TIMEOUT);
                            logger.info("<MQ监听> 本地处理失败, msgid:{}, 返回提示信息msg:{}", messageId, msg);
                        }
                    }else{
                        flag = 1;
                    }

                    ObjectResponse<Park> objectResponse = parkService.findByParkCode(parkCode);
                    ResponseUtils.notError(objectResponse);
                    Park park = objectResponse.getData();
                    Long parkId = park.getId();

                    Integer serviceType = DownServiceEnum.getServiceType(serviceName);
                    SendinfoRecord sendinfoRecord = buildRecord(body, messageId, parkId, serviceType);
                    sendinfoRecordDao.insert(sendinfoRecord);
                    /*
					 ****回写本地的返回结果，或通知TaskCenter
					 */
                    Integer serviceId = sendinfoRecord.getServiceId() == null ? null : sendinfoRecord.getServiceId().intValue();
                    if(flag == 1 && serviceId != null){
                        taskCenterService.notify(serviceType, serviceId);
                    }
                }
            }
            return Action.CommitMessage;
        } catch (Exception e) {
            // 消费失败
            try {
                logger.error("<MQ监听> body:{}, 消费者处理返回结果异常, {}", new String(message.getBody(), "utf-8"), e.getMessage());
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            return Action.ReconsumeLater;
        }
    }

    /**
     * 构建响应记录
     * @param body
     * @param messageId
     * @param parkId
     * @param serviceType
     * @return
     */
    private SendinfoRecord buildRecord(String body, String messageId, Long parkId, Integer serviceType) {
        SendinfoRecord sendinfoRecord = (SendinfoRecord) redisUtils.get(RedisKeyConstants.MQ_RECORD_PREFIX + messageId);
        if (sendinfoRecord == null){
            sendinfoRecord = sendinfoRecordDao.selectOneByMsgId(messageId, SendOperTypeEnum.请求.getOperType());
            if (sendinfoRecord == null){
                sendinfoRecord = new SendinfoRecord();
                sendinfoRecord.setParams(body);
                sendinfoRecord.setServiceType(serviceType);
                sendinfoRecord.setMessageId(messageId);
                sendinfoRecord.setParkId(parkId);
            }
        }
        sendinfoRecord.setParams(body);
        sendinfoRecord.setOperType(SendOperTypeEnum.响应.getOperType());
        return sendinfoRecord;
    }
}
