package com.icetech.paycenter.common.tool;

import com.icetech.common.EncrpytTools;
import com.icetech.common.ToolsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 银联无感支付的签名工具
 * @author fangct
 */
public class UnionpaySignTools {

    private static Logger logger = LoggerFactory.getLogger(UnionpaySignTools.class);

    /**
     * 验签
     * @param paraMap 请求参数
     * @param key 密钥
     * @return
     */
    public static boolean verifySign(Map<String, Object> paraMap, String key){
        String sign = (String) paraMap.get("Sign");
        paraMap.remove("Sign");
        String mySign = sign(paraMap, key);
        if (mySign.equals(sign)){
            return true;
        }else{
            logger.info("<银联无感支付通知签名校验> SHA256验签不通过，sign：{}，mySign：{}", sign, mySign);
            return false;
        }
    }

    /**
     * 签名
     * @param nameValuePairs 参与签名的参数
     */
    public static String sign(Map<String, Object> nameValuePairs, String key){
        nameValuePairs.put("Key", key);
        String linkString = createLinkString(nameValuePairs);
        String sign = EncrpytTools.getSHA256Str(linkString);
        nameValuePairs.remove("Key");
        return sign;
    }

    /**
     * 创建“key1=value1&key2=value2”形式的加密串，
     * 注意：key小写
     *
     * @param map
     * @return
     */
    private static String createLinkString(Map<String, Object> map) {

        List<String> keys = new ArrayList<String>(map.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String tempKey = keys.get(i).toLowerCase();
            Object value = map.get(key);
            if (ToolsUtil.isNull(value)) {
                continue;
            } else {
                if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                    prestr = prestr + tempKey + "=" + value;
                } else {
                    prestr = prestr + tempKey + "=" + value + "&";
                }
            }
        }
        if (prestr.endsWith("&")) {// 拼接时，不包括最后一个&字符
            prestr = prestr.substring(0, prestr.length() - 1);
        }
        return prestr;
    }
}
