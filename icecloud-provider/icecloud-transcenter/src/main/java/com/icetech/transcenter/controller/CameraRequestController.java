package com.icetech.transcenter.controller;

import com.icetech.common.JsonTools;
import com.icetech.transcenter.common.redis.RedisUtils;
import com.icetech.transcenter.service.mq.CameraRequestService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

/**
 * 相机端请求controller
 */
@Controller
@RequestMapping("/app")
public class CameraRequestController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CameraRequestService cameraRequestService;
    @Autowired
    private RedisUtils redisUtils;
    @RequestMapping(value = "/voicecall",method = { RequestMethod.POST})
    public void notifyApp(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String type = request.getParameter("type");
        Map<String, String[]> parameterMap = request.getParameterMap();
        logger.info("【相机端请求】>>>>所有map参数:{}", JsonTools.toString(parameterMap));
        if ("HeartBeat".equals(type)){
            return;
        }
        String uid = request.getParameter("uid");
        if (StringUtils.isNotBlank(uid)){
            send(response, uid);
        }
    }


    private void send(HttpServletResponse response, String uid) throws Exception {
        synchronized (uid) {
            Object objuid = redisUtils.get("sn_uid" + uid);
            if (Objects.isNull(objuid)) {
                redisUtils.set("sn_uid" + uid, uid, 5L);
                Boolean result = cameraRequestService.sendMsg4App(uid);
                response.getWriter().write(result ? "SUCCESS" : "ERROR");
            } else {
                logger.info("【5秒内不允许重复请求......】");
            }
        }
    }
}
