package com.icetech.datacenter.service.handle;

import com.icetech.api.cloudcenter.service.ParkFeignApi;
import com.icetech.common.DataChangeTools;
import com.icetech.common.DateTools;
import com.icetech.common.SignTools;
import com.icetech.common.UUIDTools;
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
import com.icetech.datacenter.domain.request.DownBaseRequest;
import com.icetech.datacenter.service.mqtt.MqSendMqttServer;
import com.icetech.datacenter.service.mqtt.MqttSendServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Component
public class DownHandle {

    @Autowired
    private ParkFeignApi parkService;
    @Autowired
    private MqSendMqttServer mqSendMqttServer;
    @Autowired
    private MqttSendServer mqttSendServer;
    @Autowired
    private SendinfoRecordDao sendinfoRecordDao;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 签名并下发消息
     * @param parkCode
     * @param serviceName
     * @param t
     * @return
     */
    public <T> String signAndSend(
            String parkCode, String serviceName, T t){
        return signAndSend(parkCode, serviceName, t, null);
    }

    public <T> String signAndSend(
            String parkCode, String serviceName, T t, Long serviceId){
        ObjectResponse<Park> objectResponse = parkService.findByParkCode(parkCode);
        ResponseUtils.notError(objectResponse);
        Park park = objectResponse.getData();
        Long parkId = park.getId();
        String key = park.getKey();
        return signAndSend(parkId, parkCode, key, serviceName, t, serviceId);
    }
    public <T> String signAndSend(
            Long parkId, String serviceName, T t){
        return signAndSend(parkId, serviceName, t, null);
    }
    public <T> String signAndSend(
            Long parkId, String serviceName, T t, Long serviceId){
        ObjectResponse<Park> objectResponse = parkService.findByParkId(parkId);
        ResponseUtils.notError(objectResponse);
        Park park = objectResponse.getData();
        String parkCode = park.getParkCode();
        String key = park.getKey();
        return signAndSend(parkId, parkCode, key, serviceName, t, serviceId);
    }

    /**签名并下发消息
     * @param parkId
     * @param parkCode
     * @param key
     * @param serviceName
     * @param t
     * @param serviceId
     * @param <T>
     * @return
     */
    public <T> String signAndSend(
            Long parkId, String parkCode, String key, String serviceName, T t, Long serviceId){

        DownBaseRequest baseRequest = new DownBaseRequest();
        baseRequest.setParkCode(parkCode);
        baseRequest.setTimestamp(DateTools.unixTimestamp());
        baseRequest.setServiceName(serviceName);
        String messageId = UUIDTools.getUuid();
        baseRequest.setMessageId(messageId);
        baseRequest.setBizContent(t);
        Map<String, Object> map = null;
        try {
            map = SignTools.convertMap(baseRequest);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        String sign = SignTools.getMySign(map, key);
        map.put("sign", sign);

        String json = DataChangeTools.map2JSON(map);
//        if (mqSendMqttServer.sendMessage(parkCode, json)){
        if (mqttSendServer.sendMessage(parkCode, json)){
            //记录
            addRecord(parkId, serviceName, serviceId, messageId, json);
            return messageId;
        }else{
            return null;
        }
    }

    /**
     * 新增下发记录
     * @param parkId
     * @param serviceName
     * @param serviceId
     * @param messageId
     * @param json
     */
    private void addRecord(Long parkId, String serviceName, Long serviceId, String messageId, String json) {
        SendinfoRecord sendinfoRecord = new SendinfoRecord();
        sendinfoRecord.setMessageId(messageId);
        sendinfoRecord.setParkId(parkId);
        sendinfoRecord.setParams(json);
        sendinfoRecord.setServiceId(serviceId);
        sendinfoRecord.setServiceType(DownServiceEnum.getServiceType(serviceName));
        sendinfoRecord.setOperType(SendOperTypeEnum.请求.getOperType());
        redisUtils.set(RedisKeyConstants.MQ_RECORD_PREFIX + messageId, sendinfoRecord, TimeOutConstants.REDIS_TIMEOUT);
        sendinfoRecordDao.insert(sendinfoRecord);
    }
}
