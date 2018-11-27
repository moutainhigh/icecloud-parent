package com.icetech.paycenter.service;

import com.icetech.common.*;
import com.icetech.common.constants.CodeConstants;
import com.icetech.paycenter.common.config.UnionpayConfig;
import com.icetech.paycenter.common.tool.UnionpaySignTools;
import com.icetech.paycenter.domain.autopay.ParkUnionpay;
import com.icetech.paycenter.domain.autopay.response.UnionpayResponse;

import com.icetech.paycenter.mapper.autopay.ParkUnionpayDao;
import lombok.Getter;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 银联无感支付基础类
 *
 * @author fangct
 */
@Service
public class BaseUnionpayService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UnionpayConfig unionpayConfig;

    @Autowired
    private ParkUnionpayDao parkUnionpayDao;

    /**
     * 发送http post请求
     *
     * @param nameValuePairs 请求对象
     * @param parkCode       停车场编号
     * @param isNeedParkInfo 是否需要传入车场名和编号
     * @return
     */
    protected String sendHttp(Map<String, Object> nameValuePairs, String parkCode, Boolean isNeedParkInfo) {

        ParkUnionpay parkUnionpay = new ParkUnionpay();
        parkUnionpay.setParkCode(parkCode);
        parkUnionpay = parkUnionpayDao.selectById(parkUnionpay);
        if (parkUnionpay == null) {
            logger.info("<银联无感支付> 根据车场id未找到注册信息，parkCode：{}", parkCode);
            return ResultTools.setResponse(CodeConstants.ERROR_404, CodeConstants.getName(CodeConstants.ERROR_404));
        }

        String sign = "";
        try {
            nameValuePairs.put("MerCode", parkUnionpay.getMerCode());
            if (isNeedParkInfo != null && isNeedParkInfo) {
                nameValuePairs.put("ParkCode", parkUnionpay.getOutParkcode());
                nameValuePairs.put("ParkName", parkUnionpay.getParkName());
            }
            sign = UnionpaySignTools.sign(nameValuePairs, parkUnionpay.getSecretKey());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("<银联无感支付> 拼接或加密时出错，异常：{}", e);
            return ResultTools.setResponse(CodeConstants.ERROR, CodeConstants.getName(CodeConstants.ERROR));
        }
        nameValuePairs.put("Sign", sign);

        String result = executeRequest(unionpayConfig.getUrl(), nameValuePairs);
        logger.info("<银联无感支付> 请求参数：{}，返回结果：{}", nameValuePairs, result);
        return result;
    }

    /**
     * 通用的返回结果处理
     *
     * @param unionpayResponse
     * @return
     */
    protected String commonDealResult(UnionpayResponse unionpayResponse) {
        //补充错误描述
        String extendMsg = "," + unionpayResponse.getResultMsg();
        if (ErrCode.成功.getCode().equals(unionpayResponse.getResultCode())) {
            return ResultTools.setResponse(CodeConstants.SUCCESS, CodeConstants.getName(CodeConstants.SUCCESS));
        } else if (ErrCode.重复请求错误.getCode().equals(unionpayResponse.getResultCode())) {
            return ResultTools.setResponse(CodeConstants.ERROR_405, CodeConstants.getName(CodeConstants.ERROR_405));
        } else if (ErrCode.用户不存在.getCode().equals(unionpayResponse.getResultCode())) {
            return ResultTools.setResponse(CodeConstants.ERROR_2002, CodeConstants.getName(CodeConstants.ERROR_2002));
        } else if (ErrCode.车辆有未离场记录.getCode().equals(unionpayResponse.getResultCode())) {
            return ResultTools.setResponse(CodeConstants.ERROR_2004, CodeConstants.getName(CodeConstants.ERROR_2004));
        } else {
            return ResultTools.setResponse(CodeConstants.ERROR_2100, CodeConstants.getName(CodeConstants.ERROR_2100) + extendMsg);
        }
    }

    /**
     * HttpClient的post请求
     *
     * @param sendurl        请求地址
     * @param nameValuePairs 参数的键值对
     * @return
     */
    private String executeRequest(String sendurl, Map<String, Object> nameValuePairs) {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //POST的URL
        HttpPost httppost = new HttpPost(sendurl);

        //遍历NameValuePairs集合，用于存储欲传送的参数
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        List<String> keys = new ArrayList<String>(nameValuePairs.keySet());
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = nameValuePairs.get(key);
            formparams.add(new BasicNameValuePair(key, String.valueOf(value)));
        }

        String result = "";
        try {
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, "UTF-8");
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public enum ErrCode {
        成功("00"),
        验签失败("01"),
        交易代码错误("02"),
        报文格式错误("03"),
        接入方错误("04"),
        用户已冻结("05"),
        车主用户已冻结("06"),
        用户不存在("07"),
        重复请求错误("08"),
        请求处理中("09"),
        退款号不存在("11"),
        扣款状态不合法("12"),
        退款超过付款金额("13"),
        绑定重复("14"),
        用户不存在不受理扣费请求("15"),
        用户未签约银行卡("17"),
        用户未添加银行卡("28"),
        未找到交易记录("29"),
        余额不足("30"),
        订单已存在且已成功("53"),
        订单已存在且已关闭("54"),
        订单已存在且已退款("55"),
        用户在黑名单("56"),
        车辆有未离场记录("60"),
        其他异常错误("99");

        @Getter
        private String code;

        private ErrCode(String code) {
            this.code = code;
        }
    }
}