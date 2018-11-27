package com.icetech.datacenter.common.enumeration;

import lombok.Getter;

/**
 * 下发的操作类型
 *
 */
public enum SendOperTypeEnum {

	请求(1),
	响应(2),
	;

	@Getter
	private final Integer operType;

	SendOperTypeEnum(Integer operType) {
		this.operType = operType;
	}

}
