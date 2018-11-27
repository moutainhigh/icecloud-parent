package com.icetech.common.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class Park implements Serializable {
    private Long id;
    private String parkName;
    private String parkCode;
    private String key;
}
