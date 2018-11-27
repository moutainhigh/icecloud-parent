package com.icetech.datacenter.domain.request;

import com.icetech.common.annotation.NotNull;
import com.icetech.common.domain.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Description : 下发业务请求参数基础类
 * @author fangct
 * @param <T>
 */
@Getter
@Setter
@ToString
public class DownBaseRequest<T> extends BaseRequest {

    @NotNull
    private String parkCode;
    @NotNull
    private String messageId;

}
