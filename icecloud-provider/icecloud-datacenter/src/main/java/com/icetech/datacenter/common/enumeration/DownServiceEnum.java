package com.icetech.datacenter.common.enumeration;

import lombok.Getter;

/**
 * 下发业务对应的类型
 *
 */
public enum DownServiceEnum {

	预缴费("notifyPrepay",1),
	缴费查询("queryFee",2),
    无牌车入场("noplateEnter",3),
    无牌车离场("noplateExit",4),
    远程开关闸("remoteSwitch",5),
    月卡("issuedCard",6);


	@Getter
	private final String serviceName;
	@Getter
	private final Integer serviceType;

	private DownServiceEnum(String serviceName, Integer serviceType) {
		this.serviceName = serviceName;
		this.serviceType = serviceType;
	}

	public static String getServiceName(Integer serviceType) {
		for (DownServiceEnum p : DownServiceEnum.values()) {
			if (serviceType.equals(p.getServiceType())) {
				return p.serviceName;
			}
		}
		return null;
	}

	public static Integer getServiceType(String serviceName) {
		for (DownServiceEnum p : DownServiceEnum.values()) {
			if (serviceName.equals(p.getServiceName())) {
				return p.serviceType;
			}
		}
		return null;
	}


}
