package com.icetech.common.domain.request;

import com.icetech.common.annotation.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * Description : 请求参数基础类
 * @author fangct
 * @param <T>
 */
@Data
public class BaseRequest<T> implements Serializable {
    @NotNull
    public String serviceName;
    @NotNull
    public String sign;
    @NotNull
    public Long timestamp;
    @NotNull
    public T bizContent;

}
