package com.icetech.transcenter.domain;

import lombok.Data;

/**
 * ${DESCRIPTION}
 *
 * @author
 * @create 2018-10-14 13:51
 **/
@Data
public class AppBaseRequest<T> extends ApiBaseRequest<T> {
    /**
     * 消息id
     */
    private String messageId;
}
