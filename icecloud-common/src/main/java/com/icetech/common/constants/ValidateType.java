package com.icetech.common.constants;
/**
 * 
* @Description: 验证参数的类型
* @author fangct
* @date 2017年12月12日 下午4:51:10
 */
public enum ValidateType {

	必传("allMust"),
	任一必传("anyone");
	
	private final String type;

	private ValidateType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
