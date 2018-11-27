package com.icetech.common.domain.response;


import lombok.Data;

import java.io.Serializable;

@Data
public class Response implements Serializable {
	
	private String code;
	private String msg;

}
