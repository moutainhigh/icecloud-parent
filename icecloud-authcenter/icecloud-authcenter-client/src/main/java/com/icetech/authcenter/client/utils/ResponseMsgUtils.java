package com.icetech.authcenter.client.utils;

import com.icetech.common.JsonTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.util.ResponseUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Auther: lisc
 * @Date: 2018/11/6 11:17
 * @Description: 通过httpservletresponse返回feign能识别的json错误消息
 */
public class ResponseMsgUtils {
    //成功
    public static void ok(HttpServletResponse response){
        PrintWriter writer = getPrintWriter(response);
        writer.append(JsonTools.toString(ResponseUtils.returnSuccessResponse()));
    }

    //失败
    public static void error(HttpServletResponse response){
        PrintWriter writer = getPrintWriter(response);
        writer.append(JsonTools.toString(ResponseUtils.returnErrorResponse(CodeConstants.ERROR_401,CodeConstants.getName(CodeConstants.ERROR_401))));
    }


    private static PrintWriter getPrintWriter(HttpServletResponse response) {
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer;
    }

}
