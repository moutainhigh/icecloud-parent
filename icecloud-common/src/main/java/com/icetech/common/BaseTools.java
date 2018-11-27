package com.icetech.common;

public class BaseTools {
	/**
	 * 判断不是空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNotNull(Object obj) {
		if (obj == null || obj.toString().equals("")
				|| obj.toString().toLowerCase().equals("null"))
			return false;
		return true;
	}

	/**
	 * 判断是空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj) {
		if (obj == null || obj.toString().trim().length() == 0
				|| obj.toString().toLowerCase().equals("null")){
			return true;
		}
		return false;
	}

}
