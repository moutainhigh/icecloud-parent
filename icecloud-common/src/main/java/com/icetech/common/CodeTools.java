package com.icetech.common;

import java.util.Date;
import java.util.Random;

public class CodeTools {

	public  static  String randomCoupons(){
		return getCharAndNumr(8);
	}
	/**
	 * java生成随机数字和字母组合
	 * @param length [生成随机数的长度]
	 * @return
	 */
	public static String getCharAndNumr(int length) {
		String val = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			// 输出字母还是数字
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; 
			// 字符串
			if ("char".equalsIgnoreCase(charOrNum)) {
				int choice = 65; // 取得大写字母
				val += (char) (choice + random.nextInt(26));
			} else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}

    /**
     * 生成订单号
     * @return
     */
	public static String GenerateOrderNum() {
		Date date = new Date();
		String subStr = DateTools.getFormat(DateTools.DF_THREEBIT, date);
		int rand =(int)((Math.random()*9+1)*10000);
		return subStr + rand;
	}

	/**
	 * 生成交易流水号
	 * @return
	 */
	public static String GenerateTradeNo() {
		String prefix = "T";
		Date date = new Date();
		String subStr = DateTools.getFormat(DateTools.DF_THREEBIT, date);
		int rand =(int)((Math.random()*9+1)*10000);
		return prefix + subStr + rand;
	}

	/**
	 * 生成优惠编号：13位，当前秒+3位随机数
	 * @return
	 */
	public static String GenerateDiscountNo() {
        String prefix = "Y";
		StringBuffer sb = new StringBuffer();
		long seconds = DateTools.unixTimestamp();
		sb.append(seconds);
		int rand =(int)((Math.random()*9+1)*100);
		sb.append(rand);
		return prefix + sb.toString();
	}
}
