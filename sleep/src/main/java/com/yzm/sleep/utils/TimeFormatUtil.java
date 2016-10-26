package com.yzm.sleep.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class TimeFormatUtil {

	/**
	 * 将long型的时间转换成对应格式
	 * 
	 * @param time
	 * @param pattern
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatTime(long time, String pattern) {
		try {
			Date date = new Date(time);
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 分钟转时间格式
	 * 
	 * @param time
	 * @return
	 */
	public static String minToTime(int time) {
		try {
			int hour = time / 60;
			int min = time % 60;
			String newTime = (hour < 10 ? ("0" + hour) : String.valueOf(hour))
					+ ":" + (min < 10 ? ("0" + min) : String.valueOf(min));
			return newTime;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static String formatTime1(long time, String pattern) {
		try {
			Date date = new Date(time * 1000);
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 分钟转换成 *h*m的格式
	 * 
	 * @param min
	 * @return
	 */
	public static String minToHour1(int min) {
		return min / 60 > 0 ? String.valueOf(min / 60) + "h"
				+ String.valueOf(min % 60) + "m" : min + "m";
	}

	/**
	 * 分钟转换成 *小时*分的格式
	 * 
	 * @param min
	 * @return
	 */
	public static String minToHour2(int min) {
		return min / 60 > 0 ? String.valueOf(min / 60) + "小时"
				+ String.valueOf(min % 60) + "分" : min + "分";
	}

	/**
	 *  时间格式转为秒的单位 "HH:mm" --> 2418782
	 * @param strTime
	 * @return
	 */
	public static int timeToMiss(String strTime) {
		try {
			String hour = strTime.split(":")[0];
			String min = strTime.split(":")[1];
			return Integer.parseInt(hour) * 3600 + Integer.parseInt(min) * 60;
		} catch (Exception e) {
		}
		return 0;
	}

	
	
	
	/**
	 * 获取本周所有日期
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String[] getWeekAllDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
			calendar.add(Calendar.DATE, -1);
		}
		String[] days = new String[7];
		for (int i = 0; i < 7; i++) {
			days[i] = sdf.format(calendar.getTime());
			calendar.add(Calendar.DATE, 1);
		}
		return days;
	}
 
	/**
	 * 获取本周所有日期
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String[] getWeekAllDateForPillow() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
			calendar.add(Calendar.DATE, -1);
		}
		String[] days = new String[7];
		for (int i = 0; i < 7; i++) {
			days[i] = sdf.format(calendar.getTime());
			calendar.add(Calendar.DATE, 1);
		}
		return days;
	}

	/**
	 * 获取上周日期数组
	 * 
	 * @param parren
	 *            日期格式
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String[] getWeekAllOnDate(String parren) {
		SimpleDateFormat sf = new SimpleDateFormat(parren);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.WEEK_OF_MONTH, -1);
		String[] days = new String[7];
		for (int i = 0; i < 7; i++) {
			cal.add(Calendar.DATE, -1 * cal.get(Calendar.DAY_OF_WEEK) + 2 + i);
			days[i] = sf.format(cal.getTime());
		}
		return days;
	}

	/**
	 * 获取上周星期天的日期
	 * 
	 * @param parren
	 *            日期格式
	 * @return
	 */
	public static String getWeekAllOnSunday(String parren) {
		String[] days = getWeekAllOnDate(parren);
		return days[6];
	}

	/**
	 * 获取今天是否是周一
	 * 
	 * @return
	 */
	public static boolean getWeekAllOnMonday() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (week == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取本周周一的日期
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static long getMondayDate() {
		Calendar c = Calendar.getInstance();
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week + 1);
		return c.getTime().getTime();
	}

	/**
	 * 获取昨天的日期
	 * 
	 * @param date
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getYesterDay(String date, String pattern) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(date));
			cal.add(Calendar.DAY_OF_MONTH, -1);
			return sdf.format(cal.getTime());
		} catch (Exception e) {
			// TODO: handle exception
		}

		return "";
	}
	
	public static String getTomaDay(String pattern) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DAY_OF_MONTH, +1);
			return sdf.format(cal.getTime());
		} catch (Exception e) {
			// TODO: handle exception
		}

		return "";
	}

	/**
	 * 获取昨天的日期
	 * 
	 * @param date
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getYesterDay(String pattern) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DAY_OF_MONTH, -1);
			return sdf.format(cal.getTime());
		} catch (Exception e) {
		}
		return "";
	}
	
	/**
	 * 获取 index 天前的日期
	 * @param pattern
	 * @param index
	 * @return
	 */
	public static String getOtherDay(String pattern, int index){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DAY_OF_MONTH, -index);
			return sdf.format(cal.getTime());
		} catch (Exception e) {
		}
		return "";
	}
	
	/**
	 * 返回index天前的weekday
	 * @param index
	 * @return
	 */
	public static String getWeekDate(int index){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int week = cal.get(Calendar.DAY_OF_WEEK) - 1-index;
		String weekstr = "";
		switch (week%7) {
		case 1:
			weekstr = "周一";
			break;
		case 2:
			weekstr = "周二";
			break;
		case 3:
			weekstr = "周三";
			break;
		case 4:
			weekstr = "周四";
			break;
		case 5:
			weekstr = "周五";
			break;
		case 6:
			weekstr = "周六";
			break;
		case 0:
			weekstr = "周日";
			break;
		case -1:
			weekstr = "周六";
			break;
		case -2:
			weekstr = "周五";
			break;
		case -3:
			weekstr = "周四";
			break;
		case -4:
			weekstr = "周三";
			break;
		case -5:
			weekstr = "周二";
			break;
		case -6:
			weekstr = "周一";
			break;
		default:
			break;
		}
		return weekstr;
	}
	
	/**
	 * 获取当前的年份
	 * @return
	 */
	public static String getTodayYearTime(){
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy");

		Date curDate = new Date(System.currentTimeMillis());//获取当前时间

		return formatter.format(curDate);
	}
	
	//获取今天的日期
	public static String getTodayTime(){
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");

		Date curDate = new Date(System.currentTimeMillis());//获取当前时间

		return formatter.format(curDate);
	}
	
	//获取今天的日期
	public static String getTodayTimeyyyyMMdd(){
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMdd");

		Date curDate = new Date(System.currentTimeMillis());//获取当前时间

		return formatter.format(curDate);
	}
	
	//获取昨天的日期
	public static String getYesterdayTimeyyyyMMdd(){
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMdd");

		Date curDate = new Date(System.currentTimeMillis()-24*60*60000);//获取当前时间

		return formatter.format(curDate);
	}
		
	/**
	 * 判断传入的时间是否在今天之前
	 * @param time yyyyMMdd
	 * @return
	 * @throws ParseException 
	 */
	public static boolean isGetTimebeforeNow(String time) throws ParseException{
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");  
        Date getData =  df.parse(time);
        Date curDate = new Date(System.currentTimeMillis());
		if(curDate.after(getData)){
			return true;
		}else
			return false;
	}
	
	//获取昨天的日期
	public static String getYesterdayTime(){
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");

		Date curDate = new Date(System.currentTimeMillis()-24*60*60000);//获取当前时间

		return formatter.format(curDate);
	}
	
	public static String getDatetimeBygetTime(long time){
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");

		Date curDate = new Date(time);//获取当前时间

		return formatter.format(curDate);
	}
	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getNowTime(){
		SimpleDateFormat formatter = new SimpleDateFormat ("HH:mm");

		Date curDate = new Date(System.currentTimeMillis());//获取当前时间

		return formatter.format(curDate);
	}
	
	/**
	 * 获取当前一分钟后的时间
	 * @return
	 */
	public static String getNowTimeAfter1Min(){
		SimpleDateFormat formatter = new SimpleDateFormat ("HH:mm");

		Date curDate = new Date(System.currentTimeMillis()+60*1000);//获取当前时间

		return formatter.format(curDate);
	}
	/**
	 * 比较time1与time2的先后如果time1在time2之后返回true, 如果time1在time2之前返回false
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean compareTime(String time1, String time2){
		try {
			Calendar calendar1 = Calendar.getInstance();
			calendar1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time1.split(":")[0]));
			calendar1.set(Calendar.MINUTE, Integer.parseInt(time1.split(":")[1]));
			
			Calendar calendar2 = Calendar.getInstance();
			calendar2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time1.split(":")[0]));
			calendar2.set(Calendar.MINUTE, Integer.parseInt(time1.split(":")[1]));
			
			if (calendar1.getTimeInMillis()// 如果闹钟设置时间还没过期
					- calendar2.getTimeInMillis() > 0) {
				return true;
			} else {// 闹钟时间过期
				return false;
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 判断字符串是否是由数字组成
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){ 
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();    
	 } 
	
	//获取传入时间的时间点
	public static String getTime(long time){
		SimpleDateFormat formatter = new SimpleDateFormat ("HH:mm");

		Date curDate = new Date(time);

		return formatter.format(curDate);
	}
	
	public static String getTimeOfYearMonth(long time){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		String currentYear=new SimpleDateFormat("yyyy").format(calendar.getTime());
		String selectYear=new SimpleDateFormat("yyyy").format(time);
		if(currentYear.equals(selectYear)){
			return new SimpleDateFormat("MM月dd日 HH:mm").format(time);
		}else
			return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(time);
	}
	
	public static String getTimeForYM(long time){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		String currentYear=new SimpleDateFormat("yyyy").format(calendar.getTime());
		String selectYear=new SimpleDateFormat("yyyy").format(time*1000);
		if(currentYear.equals(selectYear)){
			return formatTime1(time, "MM月dd日");
		}else
			return formatTime1(time, "yyyy-MM-dd");
	}
	/**
	 * 获取时间 包含时分
	 * @param time
	 * @return
	 */
	public static String getTimeForYMHm(long time){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		String currentYear=new SimpleDateFormat("yyyy").format(calendar.getTime());
		String selectYear=new SimpleDateFormat("yyyy").format(time*1000);
		if(currentYear.equals(selectYear)){
			return formatTime1(time, "MM月dd日  HH:mm");
		}else
			return formatTime1(time, "yyyy-MM-dd HH:mm");
	}
	
	/**
	 * 日期时间转时间戳
	 * @param time yyyyMMdd HH:mm
	 * @return ms
	 */
	public static String getDataFromDaytime(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyyMMdd HH:mm",
                        Locale.CHINA);
        Date date;
        String times = null;
        try {
                date = sdr.parse(time);
                long l = date.getTime();
                String stf = String.valueOf(l);
                times = stf;//.substring(0, 10);
                LogUtil.d("chen", times);
        } catch (ParseException e) {
                e.printStackTrace();
        }
        return times;
	}
	
	//1446620715656
	/**
	 * 获取传入的时间距离当前时间的长度
	 * @param time
	 * @return
	 */
	public static String getTimeBeforeCurrentTime(String time){
		if(!isNumeric(time)){//如果传入的字符串不是数字
			return "";
		}
		String theGapTimeString= "";
		long getTime = 0;
		long currentTime = System.currentTimeMillis();
		if(time.length() == 13){//单位是毫秒
			getTime = Long.parseLong(time);
		}else if(time.length() == 10){//单位是秒
			getTime = Long.parseLong(time) * 1000;
		}else{//传入的不是时间格式
			return "";
		}
		long gapcompareTime = currentTime - getTime;
		if(getDatetimeBygetTime(getTime).equals(getTodayTime())){
			if(gapcompareTime<60000){
				theGapTimeString = "刚刚";
			}else if(gapcompareTime>60000 && gapcompareTime <= 60*60000){
				theGapTimeString = gapcompareTime/60000+"分钟前";
			}else if(gapcompareTime > 60*60000 && gapcompareTime <= 24*60*60000){
				theGapTimeString = gapcompareTime/(60*60000)+"小时前";
			}
		}else if(getDatetimeBygetTime(getTime).equals(getYesterdayTime())){
			theGapTimeString = "昨天"+getTime(getTime);
		}else{
			theGapTimeString = getTimeOfYearMonth(getTime);
		}

		return theGapTimeString;

	}
	
	/**
	 * 超过4点才算今天。
	 * @return   如果超过4点返回的Calender 日期是今天的日期，0~4点之间返回的Calendar日期是昨天的日期
	 */
	@SuppressLint("SimpleDateFormat") 
	public static String isToday(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		Calendar calender = Calendar.getInstance();
		calender.setTime(new Date());
		calender.add(Calendar.HOUR_OF_DAY, -4);
		calender.add(Calendar.DAY_OF_MONTH, -1);
		return sdf.format(calender.getTime());
	}
	
	/**
	 * 超过4点才算今天。
	 * @return   如果超过4点返回的Calender 日期是今天的日期，0~4点之间返回的Calendar日期是昨天的日期
	 */
	@SuppressLint("SimpleDateFormat") 
	public static String isToday(String fo){
		SimpleDateFormat sdf = new SimpleDateFormat(fo);
		Calendar calender = Calendar.getInstance();
		calender.setTime(new Date());
		calender.add(Calendar.HOUR_OF_DAY, -4);
		calender.add(Calendar.DAY_OF_MONTH, -1);
		return sdf.format(calender.getTime());
	}
	
	public static String getToday(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		Calendar calender = Calendar.getInstance();
		calender.setTime(new Date());
		return sdf.format(calender.getTime());
	}
	
	public static String getTodayMD(){
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
		Calendar calender = Calendar.getInstance();
		calender.setTime(new Date());
		return sdf.format(calender.getTime());
	}
	
	public static boolean isBetweenTime(String startT, String endT){
		try {
			if(compareTime2(startT, endT)){
				if((isTimebeforeNow(startT) && isTimebeAfterNow("24:00")) || (isTimebeforeNow("0:00") && isTimebeAfterNow(endT))){//start time 在第一天， end time在第二天
					return true;
				}else{
					return false;
				}
			}else{
				if(isTimebeforeNow(startT) && isTimebeAfterNow(endT)){
					return true;
				}else{
					return false;
				}
			}
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return false;
	}
	/**
	 * time1在time2之后返回true 反之false
	 * @param time1
	 * @param time2
	 * @return
	 * @throws ParseException
	 */
	public static boolean compareTime2(String time1, String time2) throws ParseException{
		
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");  
        Date Date1 =  df.parse(time1);
        Date Date2 = df.parse(time2);
		if(Date1.after(Date2)){
			return true;
		}else
			return false;
	}
	
	@SuppressLint("SimpleDateFormat") 
	public static boolean isTimebeforeNow(String time) throws ParseException{
		
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");  
        Date getData =  df.parse(time);
        Date curDate = df.parse(getNowTime());
		if(curDate.after(getData)){
			return true;
		}else
			return false;
	}
	
	@SuppressLint("SimpleDateFormat") 
	public static boolean isTimebeAfterNow(String time) throws ParseException{
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");  
        Date getData =  df.parse(time);
        Date curDate = df.parse(getNowTime());
		if(curDate.before(getData)){
			return true;
		}else
			return false;
	}
	
	public static long getTimeLong(String time){
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");  
        Date getData = null;
		try {
			getData = df.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        if(getData != null){
        	return getData.getTime();
        }else{
        	return 0l;
        }
	}
	
	public static String ExchangeTimeformat(String getdate, String Format1, String Format2){
		DateFormat fmt =new SimpleDateFormat(Format1);
		DateFormat fmt2 =new SimpleDateFormat(Format2);
		try {
			Date date = fmt.parse(getdate);
			return fmt2.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 如果传入的    date1 > date2 返回true; else  false
	 * @param date1
	 * @param date2
	 * @param Format 时间格式
	 * @return
	 */
	public static boolean isDateBiger(String date1, String date2, String Format){
		DateFormat df = new SimpleDateFormat(Format);
		long diff = 0l;
		try{
		    Date d1 = df.parse(date1);
		    Date d2 = df.parse(date2);
		    diff = d1.getTime() - d2.getTime();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		if(diff>0){
			return true;
		}
		return false;
	}
}
