package com.icetech.transcenter.domain;

import com.icetech.common.domain.request.BaseRequest;
import lombok.Data;

@Data
public class ApiBaseRequest<T> extends BaseRequest<T> {
    private String pid;
}
