package com.icetech.datacenter.service.mqtt;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.*;
import com.icetech.api.cloudcenter.service.OrderFeignApi;
import com.icetech.common.*;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.OrderInfo;
import com.icetech.common.domain.response.ObjectResponse;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by alvin on 17-7-24.
 * This is simple example for mq java client recv mqtt msg
 */
@Component
public class VisualRecvMqttDemo implements MessageListener {

    private static String pubTopic = "topic-cloud-test";
    private static String accessKey = "LTAIGsriNzjuQJo0";
    private static String secretKey = "E8EDk6jvFkXN0SV5NBLhHYvdAFTOQ8";
    private static int qos = 0;
    private static boolean cleanSession=true;
    private static String producerId="PID-cloud-test";

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        long start = System.currentTimeMillis();

        try {
            System.out.println("<mqttDemo> 接收：" +
                    new String(message.getBody(), "utf-8"));
            String body = new String(message.getBody());
            JSONObject jsonObject = JSONObject.parseObject(body);
            String serviceName = (String) jsonObject.get("serviceName");
            String messageId = (String) jsonObject.get("messageId");
            JSONObject bizContent = (JSONObject) jsonObject.get("bizContent");
            Map<String, Object> paramsMap = new HashMap<String, Object>();
            paramsMap.put("code", "200");
            paramsMap.put("msg", "成功");
            String parkCode = (String) jsonObject.get("parkCode");
            paramsMap.put("parkCode", parkCode);
            paramsMap.put("serviceName", serviceName);
            paramsMap.put("messageId", messageId);
            if (serviceName.equals("queryFee")){
                buildParaByPlateNum(paramsMap, bizContent);
            }else if(serviceName.equals("noplateEnter")){
                noplateEnter(paramsMap, bizContent);
            }else if(serviceName.equals("noplateExit")){
                noplateExit(paramsMap, bizContent);
            }
            String resBody = JSONObject.toJSONString(paramsMap);
            System.out.println("<mqttDemo> 响应：" + resBody);
            send(resBody, parkCode);
            System.out.println("<mqttDemo> 发送mqtt消息用时"+(System.currentTimeMillis() - start) +"ms" );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Action.CommitMessage;
//                return Action.ReconsumeLater;
    }

    public static final String URL_TEST = "http://127.0.0.1:8001/report";
    private void noplateEnter(Map<String, Object> paramsMap, JSONObject bizContent) {
        System.out.println("------进入到无牌车入场------");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("parkCode", (String) paramsMap.get("parkCode"));
        map.put("serviceName", "enter");
        map.put("sign", "");
        map.put("timestamp", DateTools.unixTimestamp());

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("localOrderNum", UUIDTools.getUuid());
        paramMap.put("plateNum", (String) bizContent.get("plateNum"));
        paramMap.put("channelId", (String) bizContent.get("channelId"));
        paramMap.put("entranceName", "入口1");
        paramMap.put("enterTime", bizContent.get("enterTime"));
        paramMap.put("type", RandomUtils.nextInt(1,4));
        paramMap.put("carType", RandomUtils.nextInt(1,3));
        paramMap.put("enterImage", "fdja.jpg");
//        paramMap.put("carDesc", "");
        paramMap.put("carBrand", "大众");
        paramMap.put("carColor", "黑色");

        map.put("bizContent", paramMap);
        System.out.println("------Post请求入场接口------");
        String res = HttpTools.postJson(URL_TEST, DataChangeTools.bean2gson(map));
        System.out.println("返回："+ res);
        ObjectResponse objectResponse = ResultTools.getObjectResponse(res, Map.class);
        Map data = (Map)objectResponse.getData();

        System.out.println("===无牌车入场："+(String) bizContent.get("plateNum")+"的订单号：" + data.get("orderNum"));
    }
    @Autowired
    private OrderFeignApi orderService;
    private void noplateExit(Map<String, Object> paramsMap, JSONObject bizContent) {
        System.out.println("------进入到无牌车离场------");
        String parkCode = (String) paramsMap.get("parkCode");
        String plateNum = (String) bizContent.get("plateNum");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("parkCode", parkCode);
        map.put("serviceName", "exit");
        map.put("sign", "");
        map.put("timestamp", DateTools.unixTimestamp());

        Map<String, Object> paramMap = new HashMap<String, Object>();
        ObjectResponse<OrderInfo> inPark = orderService.findInPark(plateNum, parkCode);
        if (!inPark.getCode().equals(CodeConstants.SUCCESS))
            return;
        paramMap.put("orderNum", inPark.getData().getOrderNum());
        paramMap.put("plateNum", plateNum);
        paramMap.put("channelId", (String) bizContent.get("channelId"));
        paramMap.put("exitName", "出口1");
        paramMap.put("exitTime", bizContent.get("exitTime"));
        paramMap.put("exitImage", "京A20324.jpg");
        paramMap.put("totalAmount", "0.01");
        paramMap.put("paidAmount", "0.01");
        paramMap.put("discountAmount", "0.00");
        paramMap.put("userAccount", "fangct");

        Map<String, Object> paidInfo = new HashMap<String, Object>();
        paidInfo.put("tradeNo", CodeTools.GenerateTradeNo());
        paidInfo.put("totalPrice", "0.01");
        paidInfo.put("paidPrice", "0.01");
        paidInfo.put("discountPrice", "0.00");
        paidInfo.put("payWay", 1);
        paidInfo.put("payChannel", 1);
        paidInfo.put("payTerminal", "测试");
        paidInfo.put("payTime", DateTools.unixTimestamp());

        ArrayList list = new ArrayList();
        list.add(paidInfo);
        paramMap.put("paidInfo", list);

        map.put("bizContent", paramMap);

        System.out.println("===无牌车离场准备请求...");
        String res = HttpTools.postJson(URL_TEST, DataChangeTools.bean2gson(map));
        System.out.println("===无牌车离场："+res);
    }

    public void buildParaByPlateNum(Map<String, Object> paramsMap, JSONObject bizContent){
        String plateNum = (String) bizContent.get("plateNum");
        System.out.println("车牌号：" + plateNum);
        if (ToolsUtil.isNull(plateNum)){
            ObjectResponse<OrderInfo> objectResponse = orderService.findInPark("京A12345", (String) paramsMap.get("parkCode"));
            if (!objectResponse.getCode().equals(CodeConstants.SUCCESS))
                return;
            plateNum = objectResponse.getData().getPlateNum();
            bizContent.put("orderNum", objectResponse.getData().getOrderNum());
            bizContent.put("plateNum", "京A12345");
        }
        String[] case1 = {"京A25388", "京A567771"};
        String[] case2 = {"京A76907", "京A623162"};
        String[] case3 = {"京A22340", "京A677560"};
        String[] case4 = {"京A66463", "京A822760"};
        if (Arrays.asList(case1).contains(bizContent.get("plateNum"))){
            paramsMap.put("data", "{" +
                    "'orderNum':'" + bizContent.get("orderNum") + "'," +
                    "'plateNum':'" + plateNum+ "'," +
                    "'totalAmount':'" + 10 + "'," +
                    "'paidAmount':'" + 0 + "'," +
                    "'discountAmount':'" + 0 + "'," +
                    "'discountPrice':'" + 9.99 + "'," +
                    "'unpayPrice':'" + 0.01 + "'," +
                    "'parkTime':" + 1000 + "," +
//                                "'payTime':" + DateTools.unixTimestamp() + "," +
                    "'queryTime':" + DateTools.unixTimestamp() +
                    "}");
        }else if (Arrays.asList(case2).contains(plateNum)){
            paramsMap.put("data", "{" +
                    "'orderNum':'" + bizContent.get("orderNum") + "'," +
                    "'plateNum':'" + plateNum+ "'," +
                    "'totalAmount':'" + 10 + "'," +
                    "'paidAmount':'" + 10 + "'," +
                    "'discountAmount':'" + 0 + "'," +
                    "'discountPrice':'" + 0 + "'," +
                    "'unpayPrice':'" + 0.00 + "'," +
                    "'parkTime':" + 1000 + "," +
                    "'payTime':" + (DateTools.unixTimestamp() - 100) + "," +
                    "'queryTime':" + DateTools.unixTimestamp() +
                    "}");
        }else if (Arrays.asList(case3).contains(plateNum)){
            paramsMap.put("data", "{" +
                    "'orderNum':'" + bizContent.get("orderNum") + "'," +
                    "'plateNum':'" + plateNum+ "'," +
                    "'totalAmount':'" + 20 + "'," +
                    "'paidAmount':'" + 0.01 + "'," +
                    "'discountAmount':'" + 9.99 + "'," +
                    "'discountPrice':'" + 9.99 + "'," +
                    "'unpayPrice':'" + 0.01 + "'," +
                    "'parkTime':" + 2000 + "," +
                    "'payTime':" + (DateTools.unixTimestamp() - 2000) + "," +
                    "'queryTime':" + DateTools.unixTimestamp() +
                    "}");
        }else if (Arrays.asList(case4).contains(plateNum)){
            paramsMap.put("data", "{" +
                    "'orderNum':'" + bizContent.get("orderNum") + "'," +
                    "'plateNum':'" + plateNum+ "'," +
                    "'totalAmount':'" + 0 + "'," +
                    "'paidAmount':'" + 0 + "'," +
                    "'discountAmount':'" + 0 + "'," +
                    "'discountPrice':'" + 0 + "'," +
                    "'unpayPrice':'" + 0 + "'," +
                    "'parkTime':" + 1000 + "," +
//                                "'payTime':" + DateTools.unixTimestamp() + "," +
                    "'queryTime':" + DateTools.unixTimestamp() +
                    "}");
        }else{
            buildPara(paramsMap, bizContent);
        }

    }
    public static void buildPara(Map<String, Object> paramsMap, JSONObject bizContent){
        String plateNum = (String) bizContent.get("plateNum");
        int n = RandomUtils.nextInt(1,5);
        System.out.println("随机数：" + n);
        switch (n){
            case 1:
                paramsMap.put("data", "{" +
                        "'orderNum':'" + bizContent.get("orderNum") + "'," +
                        "'plateNum':'" + plateNum+ "'," +
                        "'totalAmount':'" + 10 + "'," +
                        "'paidAmount':'" + 0 + "'," +
                        "'discountAmount':'" + 0 + "'," +
                        "'discountPrice':'" + 9.99 + "'," +
                        "'unpayPrice':'" + 0.01 + "'," +
                        "'parkTime':" + 1000 + "," +
//                                "'payTime':" + DateTools.unixTimestamp() + "," +
                        "'queryTime':" + DateTools.unixTimestamp() +
                        "}");
                break;
            case 2:
                paramsMap.put("data", "{" +
                        "'orderNum':'" + bizContent.get("orderNum") + "'," +
                        "'plateNum':'" + plateNum+ "'," +
                        "'totalAmount':'" + 10 + "'," +
                        "'paidAmount':'" + 10 + "'," +
                        "'discountAmount':'" + 0 + "'," +
                        "'discountPrice':'" + 0 + "'," +
                        "'unpayPrice':'" + 0.00 + "'," +
                        "'parkTime':" + 1000 + "," +
                        "'payTime':" + (DateTools.unixTimestamp() - 100) + "," +
                        "'queryTime':" + DateTools.unixTimestamp() +
                        "}");
                break;
            case 3:
                paramsMap.put("data", "{" +
                        "'orderNum':'" + bizContent.get("orderNum") + "'," +
                        "'plateNum':'" + plateNum+ "'," +
                        "'totalAmount':'" + 20 + "'," +
                        "'paidAmount':'" + 0.01 + "'," +
                        "'discountAmount':'" + 9.99 + "'," +
                        "'discountPrice':'" + 9.99 + "'," +
                        "'unpayPrice':'" + 0.01 + "'," +
                        "'parkTime':" + 2000 + "," +
                                "'payTime':" + (DateTools.unixTimestamp() - 2000) + "," +
                        "'queryTime':" + DateTools.unixTimestamp() +
                        "}");
                break;
            case 4:
                paramsMap.put("data", "{" +
                        "'orderNum':'" + bizContent.get("orderNum") + "'," +
                        "'plateNum':'" + plateNum+ "'," +
                        "'totalAmount':'" + 0 + "'," +
                        "'paidAmount':'" + 0 + "'," +
                        "'discountAmount':'" + 0 + "'," +
                        "'discountPrice':'" + 0 + "'," +
                        "'unpayPrice':'" + 0 + "'," +
                        "'parkTime':" + 1000 + "," +
//                                "'payTime':" + DateTools.unixTimestamp() + "," +
                        "'queryTime':" + DateTools.unixTimestamp() +
                        "}");
                break;
        }

    }

    private static String prefix = "DEMO_";
    public static void send(String resBody, String parkCode) throws Exception {
        Producer producer = null;
        if (ProducerMap.get(prefix + parkCode) != null){
            producer = ProducerMap.get(prefix + parkCode);
            if (producer.isClosed()){
                producer.start();
            }
        }else {
            Properties initProperties = new Properties();
            initProperties.put(PropertyKeyConst.ProducerId, producerId);
            initProperties.put(PropertyKeyConst.AccessKey, accessKey);
            initProperties.put(PropertyKeyConst.SecretKey, secretKey);
            producer = ONSFactory.createProducer(initProperties);
            producer.start();
            ProducerMap.put(prefix + parkCode, producer);
        }

        final Message msg = new Message(
                pubTopic,//the topic is mqtt parent topic
                "MQ2MQTT",//MQ Tag,must set MQ2MQTT
                resBody.getBytes());//mqtt msg body
        msg.putUserProperties("qoslevel", String.valueOf(qos));
        msg.putUserProperties("cleansessionflag", String.valueOf(cleanSession));
        SendResult result = producer.send(msg);
        System.out.println("发送成功：" + result);

    }

}
