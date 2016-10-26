package com.yzm.sleep.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.yzm.sleep.bean.DateBean;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat") 
public class CalenderUtil {
	
    /** 
     * 根据年 月 获取对应的月份 天数 
     * */  
    public static int getDaysByYearMonth(int year, int month) {  
          
        Calendar a = Calendar.getInstance();  
        a.set(Calendar.YEAR, year);  
        a.set(Calendar.MONTH, month - 1);  
        a.set(Calendar.DATE, 1);  
        a.roll(Calendar.DATE, -1);  
        int maxDate = a.get(Calendar.DATE);  
        return maxDate;  
    } 
    
    /** 
     * 根据年 月 获取对应的月份 天数 
     * */  
    public static int getDaysByYearMonth(String year1, String month1) { 
    	try{
    		int year=Integer.parseInt(year1);
    		int month=Integer.parseInt(month1);
    		return getDaysByYearMonth(year,month);
    	}catch(Exception e){
    		return 30;
    	}
    } 
    
    /**
     *  获取前面几天的日期集合
     * @param dayNum  前多少天
     * @return  日期集合
     */
    public static List<String> getLastSevenDay(int dayNum) {
    	List<String> list = new ArrayList<String>();
    	try {
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		for(int i = 0;i < dayNum;i++){
    			Calendar cal = Calendar.getInstance();
        		cal.setTime(new Date());
        		cal.add(Calendar.DATE, -(dayNum - i));
        		list.add(df.format(cal.getTime()));
    		}
		} catch (Exception e) {}
        return list;
    }
	
    public static String[] getSevenDay(int dayNum) {
    	String[] list = new String[dayNum];
    	try {
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		for(int i = 0;i < dayNum;i++){
    			Calendar cal = Calendar.getInstance();
        		cal.setTime(new Date());
        		cal.add(Calendar.DATE, -(dayNum - i));
        		list[i] = (df.format(cal.getTime()));
    		}
		} catch (Exception e) {}
        return list;
    }
	
	/**
	 * 获取指定日期来指定天数日期
	 * @param date 指定日期
	 * @param dayNum 指定天数
	 * @return
	 */
    public static String[] getSevenDay(String date, int dayNum) {
    	String[] list = new String[dayNum];
    	try {
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		for(int i = 0; i < dayNum; i++){
    			Calendar cal = Calendar.getInstance();
        		cal.setTime(new Date(df.parse(date).getTime()));
        		cal.add(Calendar.DATE, -(dayNum - i - 1));
        		list[i] = (df.format(cal.getTime()));
    		}
		} catch (Exception e) {}
        return list;
    }
    
    /**
     * 获指定日期 前或者后 指定天数日期集合 
     * @param dayData 指定日期 格式 yyyy-MM-dd
     * @param dayNum 获取多少天 
     * @param isBefore 是否是获取前面日期
     */
    public static List<DateBean> getDateList(String dayData,int dayNum,boolean isBefore) {
    	List<DateBean> list = new ArrayList<DateBean>();
    	try {
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		for(int i = 0;i < dayNum;i++){
    			Calendar cal = Calendar.getInstance();
        		cal.setTime(new Date(df.parse(dayData).getTime()));
        		if(isBefore)
        			cal.add(Calendar.DATE, -(dayNum - i));
        		else
        			cal.add(Calendar.DATE, i+1);
        		DateBean dateBean=new DateBean();
        		dateBean.setDate(df.format(cal.getTime()));
        		dateBean.setState("4");
        		list.add(dateBean);
    		}
		} catch (Exception e) {
		}
    	return list;
    }

	/**
	 * 判断当前日期是星期几
	 * @param day 格式yyyy-MM-dd
	 * @return 1 周一,2 周二,3 周三,4 周四,5 周五,6 周六,7周末
	 */
	public static int getWeekDay(String day){
		Calendar calendar=Calendar.getInstance();
		try {
			calendar.setTime(new Date(new SimpleDateFormat("yyyy-MM-dd").parse(day).getTime()));
		} catch (Exception e) {
			calendar.setTime(new Date());
		}
		int weekDay=calendar.get(Calendar.DAY_OF_WEEK)-1;
		return weekDay == 0 ? 7 : weekDay;
	}
	
	/**
	 * 获取指定日期所在月的 日期集合
	 * @param date 指定日期
	 * @return 日期集合
	 */
	public static List<DateBean> getCurrentMonthDayList(Date date){
		 List<DateBean> mList=new ArrayList<DateBean>();
		 Calendar currentMonth = Calendar.getInstance();
		 currentMonth.setTime(date);
		 currentMonth.set(Calendar.DAY_OF_MONTH, 1);
		 int monthDayNum=currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
		 for(int i =0; i< monthDayNum; i++){
			Calendar ca= Calendar.getInstance();
			ca.setTime(currentMonth.getTime());
			ca.add(Calendar.DATE,i);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			DateBean bean=new DateBean();
			bean.setDate(sdf.format(ca.getTime()));
			bean.setState("4");
			mList.add(bean);
		 }
		 return mList;
	}
	
	/**
	 * 获取昨天的日期
	 * @return
	 */
	public static String getYesterdayDate(){
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			String date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
			return date;
		} catch (Exception e) {
		}
		return "";
	}
	
	/**
	 * 获取指定日期所在月的 日期集合
	 * @param date 指定日期
	 * @return 日期集合
	 */
	public static List<DateBean> getCurrentMonthDayList(String dayDate){
		 List<DateBean> mList=new ArrayList<DateBean>();
		 try {
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			 Calendar currentMonth = Calendar.getInstance();
			 currentMonth.setTime(new Date(sdf.parse(dayDate).getTime()));
			 currentMonth.set(Calendar.DAY_OF_MONTH, 1);
			 int monthDayNum=currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
			 for(int i =0; i< monthDayNum; i++){
				Calendar ca= Calendar.getInstance();
				ca.setTime(currentMonth.getTime());
				ca.add(Calendar.DATE,i);
				
				DateBean bean=new DateBean();
				bean.setDate(sdf.format(ca.getTime()));
				bean.setState("4");
				mList.add(bean);
			 }
			} catch (Exception e) {
			}
		 return mList;
	}
	
	/**
	 * 获取指定日期所在月的一号日期
	 * @param date 指定日期
	 * @return 一号日期 格式 yyyy-MM-dd
	 */
	public static String getCurrentMonthFirstDay(Date date){
		try {
			 Calendar currentMonth = Calendar.getInstance();
			 currentMonth.setTime(date);
			 currentMonth.set(Calendar.DAY_OF_MONTH, 1);
			 return new SimpleDateFormat("yyyy-MM-dd").format(currentMonth.getTime());
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 获取指定日期所在月的一号日期
	 * @param date 指定日期 yyyy-MM-dd
	 * @return 一号日期 格式 yyyy-MM-dd
	 */
	public static String getCurrentMonthFirstDay(String dayDate){
		try {
			 Calendar currentMonth = Calendar.getInstance();
			 currentMonth.setTime(new Date(new SimpleDateFormat("yyyy-MM-dd").parse(dayDate).getTime()));
			 currentMonth.set(Calendar.DAY_OF_MONTH, 1);
			 return new SimpleDateFormat("yyyy-MM-dd").format(currentMonth.getTime());
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 获取指定日期所在月的最后一天日期
	 * @param date 指定日期
	 * @return 最后一天日期 格式 yyyy-MM-dd
	 */
	public static String getCurrentMonthLastDay(Date date){
		try {
			 Calendar currentMonth = Calendar.getInstance();
			 currentMonth.setTime(date);
			 currentMonth.set(Calendar.DAY_OF_MONTH, currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
			 return new SimpleDateFormat("yyyy-MM-dd").format(currentMonth.getTime());
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 获取指定日期所在月的最后一天日期
	 * @param date 指定日期
	 * @return 最后一天日期 格式 yyyy-MM-dd
	 */
	public static String getCurrentMonthLastDay(String dayDate){
		try {
			 Calendar currentMonth = Calendar.getInstance();
			 currentMonth.setTime(new Date(new SimpleDateFormat("yyyy-MM-dd").parse(dayDate).getTime()));
			 currentMonth.set(Calendar.DAY_OF_MONTH, currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
			 return new SimpleDateFormat("yyyy-MM-dd").format(currentMonth.getTime());
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 根据日期获取显示的字符
	 * @param dayDate 格式  yyyy-MM-dd 
	 * @return
	 */
	public static String getStrByDate(String dayDate){
		return getStrByDate(dayDate, "yyyy-MM-dd");
	}
	
	/**
	 * 根据日期获取显示的字符
	 * @param dayDate
	 * @param fromat
	 * @return
	 */
	public static String getStrByDate(String dayDate, String fromat){
		try {
			long day = new SimpleDateFormat(fromat).parse(dayDate).getTime();
			Calendar calendar=Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			
			String currentYear=new SimpleDateFormat("yyyy").format(calendar.getTime());
			String selectYear=new SimpleDateFormat("yyyy").format(day);
			if(currentYear.equals(selectYear)){
//				String currentDay=new SimpleDateFormat("MMdd").format(calendar.getTime());
//				String selectDay=new SimpleDateFormat("MMdd").format(day);
//				if(currentDay.equals(selectDay)){
//					return "昨晚";
//				}else
					return new SimpleDateFormat("MM月dd日").format(day);
			}else
				return new SimpleDateFormat("yyyy年MM月dd日").format(day);
		} catch (Exception e) {
			return "昨晚";
		}
	}
}
