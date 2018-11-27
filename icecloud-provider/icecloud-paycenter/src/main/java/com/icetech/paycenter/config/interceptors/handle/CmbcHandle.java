package com.icetech.paycenter.config.interceptors.handle;

import com.icetech.common.HttpRequestJsonTools;
import com.icetech.paycenter.common.config.CmbcConfig;
import com.icetech.paycenter.domain.ParkCmbc;
import com.icetech.paycenter.mapper.normalpay.ParkCmbcDao;
import com.icetech.paycenter.service.handler.CmbcSignEncryptDncryptSignChkHandler;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 民生的请求处理
 * @author fangct
 */
@Component
public class CmbcHandle {
    private static final Logger logger = LoggerFactory.getLogger(CmbcHandle.class);
    @Autowired
    private ParkCmbcDao parkCmbcDao;

    public boolean preHandle(HttpServletRequest request) throws Exception {

        String json = HttpRequestJsonTools.getRequestJsonString(request);
        //获取异步通知的请求报文
        if (StringUtils.isBlank(json)){
            return false;
        }
        String uri = request.getRequestURI();
        String[] uriArr = uri.split("/");
        String parkCode = uriArr[uriArr.length - 1];
        String context = new JSONObject(json).get("context").toString();
        if (StringUtils.isBlank(context)){
            return false;
        }
        ParkCmbc parkCmbc = parkCmbcDao.selectByParkCode(parkCode);
        if (parkCmbc == null){
            return false;
        }
        String dncrypt = CmbcSignEncryptDncryptSignChkHandler.dncrypt(context, parkCmbc.getMerchantPrivateKeyPath(), parkCmbc.getPrivateKey());
        if (dncrypt == null){
            return false;
        }
        JSONObject jsonObject = new JSONObject(dncrypt);
        String body = jsonObject.get("body").toString();
        request.setAttribute("context", body);
        request.setAttribute("parkCode", parkCode);
        return true;
    }

}
