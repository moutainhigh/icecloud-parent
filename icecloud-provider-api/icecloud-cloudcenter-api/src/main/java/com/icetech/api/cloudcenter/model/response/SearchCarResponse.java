package com.icetech.api.cloudcenter.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class SearchCarResponse implements Serializable {

    private String plateNum;
    private String orderNum;
    private String enterTime;
    private Integer carType;
    private Integer payStatus;
    private String enterImage;

}
