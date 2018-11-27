package com.icetech.api.cloudcenter.model.request;

import com.icetech.common.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class SearchCarRequest implements Serializable {

    @NotNull
    private String parkCode;
    @NotNull
    private String plateNum;

}
