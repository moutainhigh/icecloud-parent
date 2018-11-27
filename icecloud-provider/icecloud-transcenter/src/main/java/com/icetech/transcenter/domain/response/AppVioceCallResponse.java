package com.icetech.transcenter.domain.response;

import com.icetech.transcenter.domain.BaseDomain;
import lombok.Data;

/**
 * app响应
 *
 * @author wangzw
 * @create 2018-10-14 11:55
 **/
@Data
public class AppVioceCallResponse extends BaseDomain {
    /**
     * 消息id
     */
    private String messageId;
    /**
     * 应用id
     */
    private String pid;
    /**
     * servicename
     */
    private String serviceName;
    /**
     * code
     */
    private String code;
    /**
     * msg
     */
    private String msg;
}
