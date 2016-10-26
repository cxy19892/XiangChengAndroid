package com.yzm.sleep.background;

public class SleepInfo {

	//本地存储用户信息，sharedpreferences文件名和key
	public static final String FILENAME_USER = "user";
	public static final String KEY_USERID = "userid";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_USERAGE = "age";
	public static final String KEY_USEGENDER = "gender";
	public static final String KEY_OCCUPATION = "occupation";
	public static final String KEY_PROFILE = "profile";
	public static final String KEY_LOCATION_X = "user_location_x";
	public static final String KEY_LOCATION_Y = "user_location_y";
	
	//本地存储睡眠信息，sharedpreferences文件名和key
	public static final String SLEEP_SETTIME = "sleep_settime";
	
	public static final String ENDTIME = "endtime";
	
	public static final String STARTTIME = "starttime";
	
	public static final String AIDL_STARTTIME= "aidlstarttime";
	
	public static final String AIDL_ENDTIME= "aidlendtime";
	
	public static final String SLEEP_LENTH= "sleeplenthtime";
	
	/**
	 * 美艳觉入睡时间
	 */
	public static final int HELTH_SLEEP = 22 * 60;
	
	/**
	 * 美艳觉起床时间
	 */
	public static final int HELTH_UP = 2 * 60;
	
	public static long SET_ENDTIME = 390;
	
	public static long SET_STARTTIME = 22*60;
	
	public static long BEFORE_SLEEP = 1*60;
	
	public static long AFTER_SLEEP = 3*60;
	
	public static long NOTIFICATION_SPACE = 30*60*1000;		//毫秒
	
	public static long NOTIFICATION_INTERUPT = 18*60*1000;		//毫秒
	
	public static String NOTIFICATION_TIME = "notificationtime";
	public static String REMIND_SLEEP_INTERVAL_TIME = "remind_sleep_interval_time";
	
	public static final float ALERATE_DATA = 3;				//用于鉴定睡眠起伏加速度
	
	public static final int INSERT_COUNTS = 14;				//插入空白日期最大天数
	
	public static final long FAULT_JUDGETIME = 1*60*1000;	//容错机制条件限制
	
	public static final long CIRCULATE_TIME = 5*60*1000;	//永久进程循环时间间隔
	
	public static final long ADDATA_TIME = 1*60*1000;	//插入数据时间间隔
	
	public static final long ACCINSERT_TIME = 5*1000;	//插入数据时间间隔
	
	public static final long SYNCHRO_TIME = 6*60*1000;	//同步时间
	
	public static final String SET_AGEIN = "setagain";		//再次设置入睡时间
	
	public static final String OPEN_SET = "isopen";
	
	public static final String REMIND_TYPE = "remindtype";
	public static final String REMIND_TYPE_DAY = "remindtype_day";
	public static final String REMIND_TYPE_NIGHT = "remindtype_night";
	public static final String REMIND_BEFORE_SLEEP = "remindtype_before_sleep";
	
	public static final String REMIND_MSG = "remindmsg";
	
	public static final String REMIND_TIME = "remindtime";
	public static final String REMIND_TIME_DAY = "remindtime_day";
	public static final String REMIND_TIME_NIGHT = "remindtime_night";
	
	public static final String UMENG_COUNT_ID = "useful_counts";
	
	public static final String LOCATION_X = "104.070158";
	
	public static final String LOCATION_Y = "30.548638";
}
