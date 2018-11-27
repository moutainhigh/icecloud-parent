package com.icetech.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateTools {
	
	public static final String DF ="yyyy-MM-dd HH:mm:ss";
	public static final String DF_DATE ="MM-dd";
	public static final String DF_TIME ="HH:mm";
	public static final String DF_ ="yyyy-MM-dd";
	public static final String DF_DATE_SECOND ="M/dd";
	public static final String DF_TIME_SECOND ="H:mm";
	public static final String DF_TWOBIT ="yyMMddHHmmss";
	public static final String DF_L ="yyyyMMdd";
	public static final String DF_Hms ="HHmmss";
	public static SimpleDateFormat df_date1 = new SimpleDateFormat(DF_);
	public static final String DF_THREEBIT ="yyMMddHHmmssSSS";
	public static int SPLIT_TYPE_YMD = 1;
	public static int SPLIT_TYPE_OTHER = 2;
	
//	public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	
//	public static SimpleDateFormat df_date = new SimpleDateFormat("MM-dd");
//	
//	public static SimpleDateFormat df_time = new SimpleDateFormat("HH:mm");
//	
//	public static SimpleDateFormat df_ = new SimpleDateFormat("yyyy-MM-dd");
//	
//	public static SimpleDateFormat df_date_second = new SimpleDateFormat("M/dd");
//	
//	public static SimpleDateFormat df_time_second = new SimpleDateFormat("H:mm");
//	
//	public static SimpleDateFormat df_twobit = new SimpleDateFormat("yyMMddHHmmss");
	
	public static Logger logger =LoggerFactory.getLogger(DateTools.class);
	
	/**
	 * 将时间戳装换为字符串类型的日期
	 * @return
	 */
	public static String Timestamp2StringDate(String timestamp, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		long time = new Long(timestamp);
		Date date = new Date(time);
		return sdf.format(date);
	}
	
	/**
	 * 将字符串类型的日期转换为时间戳
	 * @param date
	 * @param format
	 * @return
	 */
	public static String StringDate2Timestamp(String date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date parse = null;
		try {
			parse = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long time = parse.getTime();
		return String.valueOf(time).substring(0, 10);
	}
	
	public static SimpleDateFormat getSimpleDateFormat(String format){
		return new SimpleDateFormat(format);
	}
	
	public static String getFormat(Date date){
		return getSimpleDateFormat(DF).format(date);
	}
	
	public static String getFormat(long date){
		return getSimpleDateFormat(DF).format(date);
	}
	
	public static String getFormat(String format,Date date){
		return getSimpleDateFormat(format).format(date);
	}
	
	public static String getFormat(String format,long date){
		return getSimpleDateFormat(format).format(date);
	}
	public static Date getParse(String date){
		try {
			return getSimpleDateFormat(DF).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date getParse(String format,String date){
		try {
			return getSimpleDateFormat(format).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 计算时间差 
	 * @param date1 
	 * @param date2
	 * @return  相差的分钟数
	 */
	public  static long  time_interval(String date1,String date2){
		long interval=0;
		interval = getParse(date2).getTime()-getParse(date1).getTime();
		long min =interval/60/1000; 
		return min;
	}
	/**
	 *	计算时间差 
	 * @param date1  单位秒
	 * @param date2  单位秒
	 * @return  相差的分钟数
	 */
	public  static long  time_interval(long date1,long date2){
		long interval=0;
		interval = date2-date1;
		long min =interval/60; 
		return min;
	}
	/**
	 * 时间转换 
	 * 将秒转换成日期  XXXX-XX-XX XX:XX:XX
	 */
	public static String secondTostring(int time){
		 return getFormat(time *1000L);
	    //return df.format(time *1000L);
	}
	/**
	 * 时间转换 
	 * 将秒转换成日期  XXXX-XX-XX
	 */
	public static String secondTostringDate3(int time){
		return getFormat(DF_,time *1000L);
        //return df_.format(time *1000L);
	}
	/**
	 * 时间转换 
	 * 将秒转换成日期  XX-XX
	 */
	public static String secondTostringDate(int time){
		 return getFormat(DF_DATE, time *1000L);
        //return df_date.format(time *1000L);
	}
	/**
	 * 时间转换 
	 * 将秒转换成日期  X/XX
	 */
	public static String secondTostringDate2(int time){
		 return getFormat(DF_DATE_SECOND,time *1000L);
        //return df_date_second.format(time *1000L);
	}
	
	/**
	 * 时间转换 
	 * 将秒转换成时间   XX:XX
	 */
	public static String secondTostringTime(int time){
		return getFormat(DF_TIME,time *1000L);
        //return df_time.format(time *1000L);
	}
	/**
	 * 时间转换 
	 * 将秒转换成时间   X:XX
	 */
	public static String secondTostringTime2(int time){
		return getFormat(DF_TIME_SECOND,time *1000L);
        //return df_time_second.format(time *1000L);
	}
	/**
	 * 时间转换   
	 * 将秒转换成XX天XX小时XX分钟
	 * @param time
	 * @return
	 */
	public static String secondTotime(int time){
		String timeStr = null;  
		int days=time/(60*60*24);//换成天
		int hours=(time-(60*60*24*days))/3600;//总秒数-换算成天的秒数=剩余的秒数    剩余的秒数换算为小时
		/*分钟采用进一法，因此增加59秒*/
		int minutes=(time-60*60*24*days-3600*hours + 59)/60;//总秒数-换算成天的秒数-换算成小时的秒数=剩余的秒数    剩余的秒数换算为分
		if (days>0) {
			timeStr =unitFormat(days)+ "天"+unitFormat(hours)+ "小时"+unitFormat(minutes)+ "分钟";
		}else if(hours>0){
			timeStr =unitFormat(hours)+ "小时"+unitFormat(minutes)+ "分钟";
		}else {
			timeStr = unitFormat(minutes)+ "分钟";
		} 
        return timeStr;
	}
	/**
	 * 时间转换   
	 * 将分钟转换成XX天XX小时XX分钟
	 * @param time
	 * @return
	 */
	public static String minuteTotime(long time){
		String timeStr = null; 
        Integer days =(int) (time/(60*24));
        Integer hours = (int) (time/(60)-days*24);
        Integer minutes = (int) (time-hours*60-days*24*60);
        if (days>0) {
			timeStr =unitFormat(days)+ "天"+unitFormat(hours)+ "小时"+unitFormat(minutes)+ "分钟";
		}else if(hours>0){
			timeStr =unitFormat(hours)+ "小时"+unitFormat(minutes)+ "分钟";
		}else {
			timeStr = unitFormat(minutes)+ "分钟";
		}
        return timeStr;
	}
	
	public static String unitFormat(int i) {  
        String retStr = null;  
        if (i >= 0 && i < 10)  
            retStr = "0" + Integer.toString(i);  
        else  
            retStr = "" + i;  
        return retStr;  
    }  
	/**
	 * 将时间增加一定的分钟数
	 */
	public static String addTime(String date,int min){

		return getFormat((getParse(date)).getTime()+60000*min);
	}
	/**
	 * 获取当前时间戳
	 * @return
	 */
	public static String  nowDate(){
		
		return Long.toString(System.currentTimeMillis());
	}
	
	public static String Date(){
		return getFormat(new Date());
		//return df.format(new Date());
	}
	public static long unixTimestamp() {
		long time =System.currentTimeMillis();
		return (long) (time/1000);
	}
	
	/**
	 * 将日期转换成秒  
	 * @return  秒
	 */
	public static int phpCurrentDate(Date date){
		
		//String datestr=df_.format(date)+" 00:00:00";
		String datestr=getFormat(DF_,date)+" 00:00:00";
		long datalong =0;
		datalong=getParse(datestr).getTime();
		return (int) (datalong/1000);
	}
	/**
	 * 将日期加一天
	 * @param date
	 * @return
	 */
	public static Date addDate(Date date){
		 Calendar calendar = new GregorianCalendar();
		 calendar.setTime(date);
		 calendar.add(Calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
		 date=calendar.getTime(); //这个时间就是日期往后加一天的结果 
		 return date;
	}
	/**
	 * 将小时加一小时
	 * @param time
	 * @param arg
	 * @return
	 */
	public static int addHour(int time,int arg){
		return time + (60*60)*arg;
	}
	/**
	 * 将天加一天
	 * @param time
	 * @param arg
	 * @return
	 */
	public static int addDay(int time,int arg){
		return time + (60*60*24)*arg;
	}
	/**
	 * 从当前时间增加天数
	 * @param day 增加天数
	 * @return 单位秒
	 */
	public static int addDayFromNow(int day){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		int time = (int) ((calendar.getTimeInMillis())/1000);//单位秒s
		time = DateTools.addDay(time, day);
		return time;
	}
	/**
	 * 把字符串类型的时间转换成秒
	 * @param time 格式：yyyy-MM-dd HH:mm:ss
	 * @return 单位秒
	 */
	public static long timeStr2seconds(String time){
		if(ToolsUtil.isNotNull(time)){
			time = time.replaceAll("-", "/");
			@SuppressWarnings("deprecation")
			Date start = new Date(time);
			return (start.getTime()/1000);
		}else{
			return 0L;
		}
	}
	/**
	 * 获取当前的年份
	 */
	public static int getCurrentYear(){
		Calendar now = Calendar.getInstance();  
		int year =now.get(Calendar.YEAR);
		return year;
	}
	/**
	 * 获取当前的月份
	 */
	public static int getCurrentMonth(){
		Calendar now = Calendar.getInstance();  
		int month =(now.get(Calendar.MONTH)+1);
		return month;
	}
	/**
	 * 本月末一天
	 * @param num
	 * @return
	 */
	public static String endMonthDate(int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.MONTH, num + 1);
        calendar.add(Calendar.DATE, -1);
        return getFormat(DF_, calendar.getTime());
    }
	/**
	 * 本月第一天
	 * @param num
	 * @return
	 */
	public static String startMonthDate(int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.MONTH, num);
        return getFormat(DF_, calendar.getTime());
    }
	
	/**
	 * 最后周
	 */
	public static String endWeekDate(int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, num * 7);
        int w = calendar.get(Calendar.DAY_OF_WEEK);
        if (w == 1)
            w = 8;
        calendar.add(Calendar.DATE, 8 - w);
        return getFormat(DF_, calendar.getTime());
    }
	/**
	 * 开始周
	 * @param num
	 * @return
	 */
	public static String startWeekDate(int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, num * 7);
        int w = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, 2 - w);
        return getFormat(DF_, calendar.getTime());
    }
	/**
	 * 获取指定日期的下一天
	 * @param date
	 * @return
	 */
	public static String getNextDate(String date) { 
          
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(getParse(DF_, date));
            cal.add(Calendar.DATE, 1);
        } catch (Exception e) { 
            e.printStackTrace();
        }   
        return getFormat(DF_, cal.getTime());
    }
	/**
	 * 获取指定日期的上一天
	 * @param date
	 * @return
	 */
    public static String getUpDate(String date) { 
       
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(getParse(DF_, date));
            cal.add(Calendar.DATE, -1);
        } catch (Exception e) { 
            e.printStackTrace();
        }   
        return getFormat(DF_, cal.getTime());
    }
	/**
	 * 比较时间大小
	 * @param date1 日期1
	 * @param date2 日期2
	 * @return 当日期1大于日期2则返回1，小于返回-1，相等返回0
	 */
	public static int compareDate(String date1,String date2){
		
        try {
            Date dt1 = getParse(date1);
            Date dt2 =getParse(date2);
            if(dt1.getTime() > dt2.getTime()){
            	return 1;
            }else if(dt1.getTime() < dt2.getTime()){
            	return -1;
            }else{
            	return 0;
            }
        } catch (Exception e) { 
            e.printStackTrace();
        }   
        return 0;
	}
	/**
	 * 根据当前时间添加指定天数获取下一日期
	 * @param num 添加天数
	 * @return 日期
	 */
	public static String getNextDate(int num) { 
        
        Calendar cal = Calendar.getInstance();
        try { 
            cal.add(Calendar.DATE, num);
        } catch (Exception e) { 
            e.printStackTrace();
        }   
        return getFormat(DF_,cal.getTime());
    }
	
	/**
	 * 根据指定时间添加指定天数获取下一日期
	 * @param date 时间
	 * @param num 添加天数
	 * @return 日期
	 */
	public static String getNextDate(String date,int num) { 
        Calendar cal = Calendar.getInstance();
        try {
        	cal.setTime(getParse(DF_, date));
            cal.add(Calendar.DATE, num);
        } catch (Exception e) { 
            e.printStackTrace();
        }   
        return getFormat(DF_,cal.getTime());
    }
	
	/**
	 * 获取指定月下最大日期
	 * @param num
	 * @return
	 */
	public static String getMonthOfMaxDate(int num) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.MONTH, num);
		calendar.add(Calendar.DATE, -1);
		return getFormat(DF_,calendar.getTime());
	}
	
	/**
	 * 当前月增加多少个月
	 * 
	 * @param num
	 * @return
	 */
	public static String getStartMonthDate(int num) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1);
		calendar.add(Calendar.MONTH, num);
		return getFormat(DF_,calendar.getTime());
	}
	
	/**
	 * 验证是否为日期
	 * @param str
	 * @return
	 */
	public static boolean isValidDate(String str) {
	      boolean convertSuccess=true;
	      // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
	      try {
	    	  // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
	          getSimpleDateFormat(DF).setLenient(false);
	          str = str.replace("/", "-");
	          getSimpleDateFormat(DF).parse(str);
	      } catch (ParseException e) {
	          // e.printStackTrace();
	    	  // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
	          convertSuccess=false;
	      } 
	      return convertSuccess;
	}
	/**
	 * 取当前天
	 * 
	 * @return
	 */
	public static String getDay() {
		Calendar cal = Calendar.getInstance();
		String date = cal.get(Calendar.DAY_OF_MONTH) + "";
		if (Integer.parseInt(date) < 10)
			date = "0" + cal.get(Calendar.DAY_OF_MONTH);
		return date;
	}

	/**
	 * 取当前星期
	 * 
	 * @return
	 */
	public static String getWeek() {
		Calendar cal = Calendar.getInstance();
		return String.valueOf(cal.get(Calendar.DAY_OF_WEEK) - 1);
	}
	
	/**
	 * 将小时转化为 ：
	 * xx小时xx分钟
	 * xx小时（0分钟）
	 * xx分钟（不到一小时）
	 * @param time
	 * @return
	 */
	public static String getHourtime(String time) {	
		if(StringUtils.isBlank(time)){
			return null;
		}else{
			int index = time.indexOf(".");
			if (index == -1) {
				time = time + "小时";
			} else {
				String begin = time.substring(0, index);
				String last = time.substring(index, time.length());
				int r1 = Integer.parseInt(begin);
				int r2 = (int) ((Double.parseDouble(last)) * 60);
				if (r1 == 0) {
					time = r2 + "分钟";
				} else {
					if (r2 == 0) {
						time = r1 + "小时";
						
					} else {
						time = r1 + "小时" + r2 + "分钟";
					}
				}
			}
			return time;
		}
	}
	/**
	 * 获取当前时间戳的前10位
	 * @return String
	 */
	public static String getCurrentSecond() {
		long time = System.currentTimeMillis();
		String string = Long.toString(time);
		return string.substring(0, 10);
	}
	/**
	 * 时间转换
	 * 将20170808格式转换成时间   2017-08-08
	 * @throws ParseException
	 */
	public static String stringToDate(String time) throws ParseException{
		if(time.contains("\"")){
			time = time.replaceAll("\"", "").trim();
		}
		java.text.DateFormat format2 = new SimpleDateFormat(DF_L);
		Date date=null;
		date = format2.parse(time);
		String new_time=DateTools.getFormat(DF_,date);
		return new_time;
	}
	/**
	 * 去除日期后边的.0
	 */
	public static String formatDateTime(String time){
		return time.replace(".0", "");
	}
	/**
	 * 将时间增加一定的分钟数
	 */
	public static String addMinute(Date date,int min){
		return DF.format(String.valueOf(date.getTime()+60000*min));
	}
	
	/**
	 * 从当前时间增加小时数目
	 * 
	 * @param hour
	 *            增加天数
	 * @return 单位秒
	 */
	public static int addHourFromNow(int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		int time = (int) ((calendar.getTimeInMillis()) / 1000);// 单位秒s
		time = DateTools.addHour(time, hour);
		return time;
	}
	
	/**
	 * 判断传入日期是否为周末
	 * @param arg
	 * @return  true 为周末
	 * 			false 为工作日
	 * @throws ParseException
	 */
	public static boolean findweekend(String arg) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(arg);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
				|| calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			return true;
		} else {
			return false;

		}
	}
	
	/**
	 * 获取日期期间的日期集合
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static List<String> getBetweenDates(String startTime, String endTime){
		List<String> dateList = new ArrayList<String>();
		try {
			Date d1 = DateTools.df_date1.parse(startTime);//定义开始日期
			Date d2 = DateTools.df_date1.parse(endTime);//定义结束日期
			Calendar dd = Calendar.getInstance();//定义日期实例
			dd.setTime(d1);//设置日期起始时间
			while(dd.getTime().before(d2) || dd.getTime().equals(d2)){//判断是否到结束日期(包含结束日期)
				String dateStr =  DateTools.df_date1.format(dd.getTime());
				dateList.add(dateStr);
				dd.add(Calendar.DAY_OF_MONTH, 1);//进行当前日期月份加1
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateList;
	}
	/**
	 * 开始日期与结束日期相差多久（[*天]*时*分）
	 *
	 * @param startTime
	 *            开始日期
	 * @param endTime
	 *            结束日期
	 * @param format
	 * @param splitType
	 *            分隔类型 1："*天*时*分"，2："时:分:秒"
	 * @return
	 * @throws ParseException
	 */
	public static String dateDiff(String startTime, String endTime, String format, int splitType)
			throws ParseException {
		if (ToolsUtil.isNull(startTime) || ToolsUtil.isNull(endTime))
			return "";
		// 按照传入的格式生成一个simpledateformate对象
		SimpleDateFormat sd = new SimpleDateFormat(format);
		// 获得两个时间的毫秒时间差异
		return dateDiff(sd.parse(endTime).getTime(), sd.parse(startTime).getTime(), splitType);
	}
	/**
	 * 开始日期与结束日期相差多久（[*天]*时*分）
	 *
	 * @param startTime
	 *            开始日期，毫秒
	 * @param endTime
	 *            结束日期，毫秒
	 * @param splitType
	 *            分隔类型 1："*天*时*分"，2："时:分:秒"
	 * @return
	 * @throws ParseException
	 */
	public static String dateDiff(long startTime, long endTime, int splitType)
			throws ParseException {
		if (ToolsUtil.isNull(startTime) || ToolsUtil.isNull(endTime))
			return "";
		// 获得两个时间的毫秒时间差异
		long diff = endTime - startTime;
		return dateDiff(diff, splitType);
	}

	/**
	 * 开始日期与结束日期相差多久（[*天]*时*分）
	 *
	 * @param diff
	 *            间隔毫秒数，毫秒
	 * @param splitType
	 *            分隔类型 1："*天*时*分"，2："时:分:秒"
	 * @return
	 * @throws ParseException
	 */
	public static String dateDiff(long diff, int splitType){
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long nh = 1000 * 60 * 60;// 一小时的毫秒数
		long nm = 1000 * 60;// 一分钟的毫秒数
		long ns = 1000;// 一秒钟的毫秒数long diff;

		long day = diff / nd;// 计算差多少天
		long hour = diff % nd / nh;// 计算差多少小时
		long min = diff % nd % nh / nm;// 计算差多少分钟
		long sec = diff % nd % nh % nm / ns;// 计算差多少秒//输出结果
		String result = "";
		if (splitType == SPLIT_TYPE_YMD) {
			if (day != 0) {
				result = day + "天" + hour + "时" + min + "分";
			} else if (hour != 0) {
				result = hour + "时" + min + "分";
			} else {
				result = hour + "时" + min + "分";
			}
		} else if (splitType == SPLIT_TYPE_OTHER) {
			String hourStr = "" + hour;
			String minStr = "" + min;
			String secStr = "" + sec;
			if (hour < 10)
				hourStr = "0" + hourStr;
			if (min < 10)
				minStr = "0" + minStr;
			if (sec < 10)
				secStr = "0" + secStr;
			if (day != 0) {
				hour = hour + day * 24;
				if (hour < 10)
					hourStr = "0" + hour;
				result = hour + ":" + minStr + ":" + secStr;
			} else if (hour != 0) {
				result = hourStr + ":" + minStr + ":" + secStr;
			} else {
				result = hourStr + ":" + minStr + ":" + secStr;
			}
		}
		return result;
	}

}
