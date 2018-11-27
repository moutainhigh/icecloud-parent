package com.icetech.paycenter.domain.request;


import com.icetech.common.annotation.NotNull;
import com.icetech.common.domain.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * Description : 请求参数基础类
 * @author fangct
 * @param <T>
 */
@Setter
@Getter
public class PayCenterBaseRequest<T> extends BaseRequest<T> {
    @NotNull
    private String pid;

    @Override
    public String toString() {
        return "PayCenterBaseRequest{" +
                "pid='" + pid + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", sign='" + sign + '\'' +
                ", timestamp=" + timestamp +
                ", bizContent=" + bizContent +
                '}';
    }
}
