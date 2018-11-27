package com.icetech.common;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fangct 时间：2015年10月28日 功能：设计货币的工具 备注：
 */

public class MoneyTool {
	/**
	 * 分转换为元.
	 * 
	 * @param fen
	 *            分
	 * @return 元
	 */
	public static String fromFenToYuan(final String fen) {
		if (fen.equals("0")) {
			return "0";
		}
		String yuan = "";
		final int MULTIPLIER = 100;
		Pattern pattern = Pattern.compile("^[1-9][0-9]*{1}");
		Matcher matcher = pattern.matcher(fen);
		if (matcher.matches()) {
			yuan = new BigDecimal(fen).divide(new BigDecimal(MULTIPLIER)).setScale(2).toString();
		} else {
			System.out.println("参数格式不正确!");
		}
		return yuan;
	}
	
	/**
	 * 元转换为分.
	 * 
	 * @param yuan
	 *            元
	 * @return 分
	 */
	public static String fromYuanToFen(final String yuan) {
		if (null ==yuan || "0".equals(yuan) || "".equals(yuan)) {
			return "0";
		}
		String fen = "";
		Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]{1,2})?$");
		Matcher matcher = pattern.matcher(yuan);
		if (matcher.matches()) {
			try {
				NumberFormat format = NumberFormat.getInstance();
				Number number = format.parse(yuan);
				double temp = number.doubleValue() * 100.0;
				// 默认情况下GroupingUsed属性为true 不设置为false时,输出结果为2,012
				format.setGroupingUsed(false);
				// 设置返回数的小数部分所允许的最大位数
				format.setMaximumFractionDigits(0);
				fen = format.format(temp);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} 
		return fen;
	}

}
