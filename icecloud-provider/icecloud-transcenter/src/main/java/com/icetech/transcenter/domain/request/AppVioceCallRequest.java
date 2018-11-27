package com.icetech.transcenter.domain.request;

import com.icetech.transcenter.domain.BaseDomain;
import lombok.Data;

/**
 * 平台请求
 *
 * @author wangzw
 * @create 2018-10-14 11:55
 **/
@Data
public class AppVioceCallRequest extends BaseDomain {

    /**
     * 车厂编号
     */
    private String parkCode;

    /**
     * 相机编号
     */
    private String snNo;
}
