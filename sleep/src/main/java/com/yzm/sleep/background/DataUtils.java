package com.yzm.sleep.background;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.yzm.sleep.model.DaySleepMsg;
import com.yzm.sleep.model.JudgeObject;
import com.yzm.sleep.utils.FaultState;
import com.yzm.sleep.utils.PreManager;

public class DataUtils {
	
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static String getDate() {
		Calendar c = Calendar.getInstance();
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = getMonth(c.get(Calendar.MONTH) + 1);
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		StringBuffer sb = new StringBuffer();
		sb.append(year).append("-").append(dealData(c.get(Calendar.MONTH) + 1)).append("-").append(dealData(c.get(Calendar.DAY_OF_MONTH)));
		
		return sb.toString();
	}
	
	/**
	 * 获取前一天的日期
	 * @param date 格式为"yyyy-MM-dd"
	 * @return
	 * @throws ParseException
	 */
	public static String getBeforeDate(String date) throws ParseException {
		Long time = sdf.parse(date).getTime() - 24*60*60*1000;
		return sdf.format(new Date(time));
	}
	
	/**
	 * 获取下一天的日期
	 * @param date 格式为"yyyy-MM-dd"
	 * @return
	 * @throws ParseException
	 */
	public static String getNextDate(String date) throws ParseException {
		Long time = sdf.parse(date).getTime() + 24*60*60*1000;
		return sdf.format(new Date(time));
	}
	
	public static String getMonth(int i) {
		if(i < 10) {
			return "0"+i;
		}else {
			return i+"";	
		}
	}
	
	/**
	 * 获取当前当日分钟数，比如是凌晨1点则是60分钟
	 * @return
	 */
	public static long getMinute(){
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		long minuteOfDay = hour*60 + minute;
		return minuteOfDay;
	}
	
	/**
	 * 获取当日凌晨的毫秒数
	 * @return
	 * @throws ParseException
	 */
	public static long getSSS() throws ParseException {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		String date = year+"-"+month+"-"+day;
		long ss = sdf.parse(date).getTime();
		return ss;
	}
	
	/**
	 * 获取指定日期凌晨的毫秒数
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static long getSS(Date date) throws ParseException {
		String str = sdf.format(date);
		return sdf.parse(str).getTime();
	}
	
	/**
	 * 获取指定日期凌晨的毫秒数
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static long getSS(String date) throws ParseException {
		return sdf.parse(date).getTime();
	}
	
	
	/**
	 * 是否睡醒
	 * @param context
	 * @return
	 */
	public static boolean isWeak(Context context,String date) {
		String state = DataUtils.getRecordState(context, date);
		if("4".equals(state)) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 获取加速度开启时间,也是我们检测进程应该开启的时间
	 * @return
	 * @throws ParseException 
	 */
	public static long getStartTime() throws ParseException {
		long currenttime = System.currentTimeMillis();
		if(getAllerateStartTime() > getAllerateStopTime()) {
			if( currenttime < (getSS(new Date())+getAllerateStopTime())) {
				return getSS(new Date())+getAllerateStartTime() - 24*60*60*1000;	//当t1大于t2时，当前时间在当天的t2之前，应该取上一天的t1
			}else {
				return getSS(new Date())+getAllerateStartTime();
			}
		}else {
			if( currenttime > (getSS(new Date())+getAllerateStopTime())) {
				return getSS(new Date())+getAllerateStartTime() + 24*60*60*1000; 	//当t1小于t2时，当前时间大于当天的t2时，应该取第二天的t1
			}else {
				return getSS(new Date())+getAllerateStartTime();
			}
		}
	}
	
	/**
	 * 单位为毫秒
	 * @return
	 */
	public static long getAllerateStartTime() {		
		Long start = ((SleepInfo.SET_STARTTIME - SleepInfo.BEFORE_SLEEP)*60*1000);
		if(start < 0) {
			start = 24*60*60*1000 - Math.abs(start);
		}
		return start;
	}
	
	/**
	 * 获取真实的开始时间
	 * @param starttime
	 * @return
	 */
	public static long getRealStartTime(int starttime) {
		Long start = ((starttime - SleepInfo.BEFORE_SLEEP)*60*1000);
		if(start < 0) {
			start = 24*60*60*1000 - Math.abs(start);
		}
		return start;
	}
	
	/**
	 * 获得加速度结束时间
	 * @return
	 * @throws ParseException 
	 */
	public static long getStopTime() throws ParseException {
		long currenttime = System.currentTimeMillis();
		if(getAllerateStartTime() > getAllerateStopTime()) {
			if( currenttime > (getSS(new Date())+getAllerateStopTime())) {
				return getSS(new Date())+getAllerateStopTime() + 24*60*60*1000;		//当t1大于t2,而且当前时间在当天的t2之后，就应该取第二天的t2
			}else {
				return getSS(new Date())+getAllerateStopTime();
			}
		}else {
			if( currenttime < (getSS(new Date())+getAllerateStopTime())) {
				return getSS(new Date())+getAllerateStopTime();
			}else {
				return getSS(new Date())+getAllerateStopTime() + 24*60*60*1000;	//当t1小于t2,而且当天时间再当天的t2之后，就应该取第二天的
			}
		}
	}
	
	public static long getAllerateStopTime() {
		Long end = (SleepInfo.SET_ENDTIME + SleepInfo.AFTER_SLEEP)*60*1000;
		if(end > 24*60*60*1000) {
			end = end - 24*60*60*1000;
		}
		return end;
	}
	
	/**
	 * 获取真实的结束时间
	 * @param endtime
	 * @return
	 */
	public static long getRealEndtime(int endtime) {
		Long end = (endtime + SleepInfo.AFTER_SLEEP)*60*1000;
		if(end > 24*60*60*1000) {
			end = end - 24*60*60*1000;
		}
		return end;
	}
	
	/**
	 * 获取当年周期日期
	 * @return 返回格式yyyy-MM-dd
	 * @throws ParseException 
	 */
	public static String getRecordDate(Date date) throws ParseException {
		if(getAllerateStopTime() <= getAllerateStartTime() || getAllerateStartTime() < 4*60*60*1000) {
			if((date.getTime() - getSS(sdf.format(date))) <= getAllerateStopTime()) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.add(Calendar.DAY_OF_YEAR, -1);
				return sdf.format(calendar.getTime());
			}else {
				return sdf.format(date);
			}
		}else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			if((date.getTime() - getSS(sdf.format(date))) > getAllerateStopTime()) {
				calendar.add(Calendar.DAY_OF_YEAR, 1);
				return sdf.format(calendar.getTime());
			}else {
				return sdf.format(date);	
			}
		}
	}
	private static String getYesterDay(){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
//			cal.setTime(sdf.parse(date));
			cal.add(Calendar.DAY_OF_MONTH, -1);
			return sdf.format(cal.getTime());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return "";
	}
	
	/**
	 * 获取前三十天的日期
	 * @param date
	 * @return
	 * @throws ParseException 
	 */
	public static String getLastMonthDay(String date) throws ParseException {
		Long time = sdf.parse(date).getTime() + 29*24*60*60*1000;
		return sdf.format(new Date(time));
	}
	
	/**
	 * 处理二位数的表达方式，比如9分钟，则返回“09”
	 * @return
	 */
	public static String dealData(int data) {
		if(data < 10) {
			return "0"+data;
		}else{
			return data + "";
		}
	}
	
	/**
	 * 获取下一次持久线程的开启时间
	 * @return
	 * @throws ParseException 
	 */
	public static long getNextPermanetServiceStartTime() throws ParseException {
		long time = 0;
		if(SleepInfo.SET_STARTTIME*60*1000 - SleepInfo.BEFORE_SLEEP *60* 1000 >= 0) {
			time = getSS(new Date()) + SleepInfo.SET_STARTTIME*60*1000 - SleepInfo.BEFORE_SLEEP *60* 1000;	
		}else {
			time = getSS(new Date()) + SleepInfo.SET_STARTTIME*60*1000 - SleepInfo.BEFORE_SLEEP *60* 1000 + 24*60*60*1000;
		}
		if(System.currentTimeMillis() >= time) {
			return time + 24*60*60*1000;
		}else {
			return time;	
		}
	}
	
	/**
	 * 获取当日的开启时间
	 * @return
	 * @throws ParseException 
	 */
	public static long getPermanetServiceStartTime() throws ParseException {
		//在当天的开始时间之前
		if(getAllerateStartTime() < getAllerateStopTime() && System.currentTimeMillis() > getStopTime()) {
			return getStartTime() + 24*60*60*1000;
		}else {
			return getStartTime();
		}
	}

	/**
	 * 获取下一次闹钟提醒时间
	 * @return
	 * @throws ParseException 
	 */
	public static long getNextAlertTime(Context context) throws ParseException {
		SharedPreferences sp = context.getSharedPreferences(SleepInfo.SLEEP_SETTIME, context.getApplicationContext().MODE_APPEND);
		String date = getRecordDate(new Date());
		long befortime = sp.getLong(SleepInfo.NOTIFICATION_TIME, SleepInfo.NOTIFICATION_SPACE);
		long firstTime = getT1(date) + 1*60*60*1000 - befortime;
		long lasttime = getT1(date) + 1*60*60*1000;		
		if(System.currentTimeMillis() >= lasttime) {
			return firstTime + 24*60*60*1000;
		}else if(System.currentTimeMillis() >= firstTime) {
			return System.currentTimeMillis() + SleepInfo.NOTIFICATION_INTERUPT;
		}else {
			return firstTime;
		}
	}
	
	/**
	 * 判断睡眠时间是否在设置时间内
	 * @param result
	 * @return
	 */
	public static boolean isOutOfTime(SleepResult result) {
		String[] sleepTime = result.getSleeptime().split(":");
		String[] upTime = result.getUptime().split(":");
		String[] setSleepTime = result.getStarttime().split(":");
		String[] setUptime = result.getEndtime().split(":");
		long sleep = Long.valueOf(sleepTime[0])*60+Long.valueOf(sleepTime[1]);
		long up = Long.valueOf(upTime[0])*60+Long.valueOf(upTime[1]);
		long setSleep = Long.valueOf(setSleepTime[0])*60+Long.valueOf(setSleepTime[1]) - SleepInfo.BEFORE_SLEEP;
		long setUp = Long.valueOf(setUptime[0])*60+Long.valueOf(setUptime[1]) + SleepInfo.AFTER_SLEEP; 
		return isOutOfTime(setSleep,setUp,sleep,up);
	}
	
	/**
	 * 
	 * @param setSleep
	 * @param setUp
	 * @param sleep
	 * @param up
	 * @return
	 */
	public static boolean isOutOfTime(long setSleep,long setUp,long sleep,long up){
		if(setSleep < 0) {
			setSleep = 24 - Math.abs(setSleep);
		}
		if(setUp >= 24*60) {
			setUp = setUp - 24*60;
		}
		if(setSleep <= setUp) {
			if(sleep < setSleep || up > setUp) {
				return true;
			}
		}else {
			if(sleep <= setUp && up > setUp) {
				return true;
			}else if(sleep > setUp && sleep < setSleep) {
				return true;
			}else if(sleep >= setSleep && up > setUp) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否超出指定时间范围
	 * @param starttime
	 * @param endtime
	 * @param rangeStarttime
	 * @param rangeEndtime
	 * @return
	 */
	public static boolean isInRange(long checkTime,long rangeStarttime,long rangeEndtime) {
		if(rangeStarttime <= rangeEndtime) {
			if(checkTime >= rangeStarttime && checkTime <= rangeEndtime) {
				return true;
			}
		}else {
			if(checkTime >= rangeStarttime || checkTime <= rangeEndtime) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否能够上传数据
	 * @param context
	 * @return
	 */
	public static boolean canUpload(Context context) {
		boolean state = PreManager.instance().getIsLogin(context.getApplicationContext());
		return state;
	}
	
	public static SimpleDateFormat ssdf = new SimpleDateFormat("yyyyMMddHHmm");
	/**
	 * 通过HH:mm格式的时间获得yyyy-MM-dd-HH-mm-ss时间
	 * @param sleeptime		HH:mm
	 * @param starttime  分钟数
	 * @param endtime	分钟数
	 * @param date		yyyy-MM-dd格式
	 * @return
	 * @throws ParseException 
	 */
	public static String getSleepOrUptime(String time,String starttime,String endtime,String date) throws ParseException {
		try {
			long result = 0;
			String startArray[] = starttime.split(":");
			long startLongtime = Long.valueOf(startArray[0])*60+Long.valueOf(startArray[1]);
			long start = ((startLongtime - SleepInfo.BEFORE_SLEEP)*60*1000);
			if(start < 0) {
				start = 24*60*60*1000 - Math.abs(start);
			}
			String endArray[] = starttime.split(":");
			long endLongtime = Long.valueOf(endArray[0])*60+Long.valueOf(endArray[1]);
			long end = ((endLongtime + SleepInfo.AFTER_SLEEP)*60*1000);
			if(end > 24*60*60*1000) {
				end = end - 24*60*60*1000;
			}
			String timeArray[] = time.split(":");
			long timeLongtime = (Long.valueOf(timeArray[0]) * 60 + Long.valueOf(timeArray[1]))*60*1000;
			if(start < end) {
				if(startLongtime >= 4*60) {
					result = sdf.parse(date).getTime() + timeLongtime;
				}else {
					result = sdf.parse(date).getTime() + timeLongtime + 24*60*60*1000;
				}
			}else {
				if(timeLongtime >= start) {
					result = sdf.parse(date).getTime() + timeLongtime;
				}else {
					result = sdf.parse(date).getTime() + timeLongtime + 24*60*60*1000;
				}
			}
			return ssdf.format(new Date(result));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
	
	/**
	 * 获取睡眠时长，单位分钟数
	 * @param date
	 * @param context
	 * @return
	 */
	public static String getSleepLength(String date,Context context) {
		String result = "0";
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPTIME);
		List<SleepResult> srList;
		try {
			srList = (List<SleepResult>) operate.query(new String[]{"date","sleeplength"}, "date = ?", new String[]{date},"date");
			if(srList.size() > 0) {
				result = srList.get(0).getSleepLength();	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		operate.close();	
		return "".equals(result)?"0":result;
	}
	
	/**
	 * 排名数据获取日期，如果当前日期没有睡醒，就获取前一个有效数据的日期。
	 * @param sysdate	当前系统date
	 * @param context	传入全文参数getapplicatonContext
	 * @return
	 */
	public static String getRankDate(Context context,String date) {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		try {
			if(!isWeak(context,date)) {
				MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPTIME);
				@SuppressWarnings("unchecked")
				List<SleepResult> list = (List<SleepResult>) operate.query(new String[]{MyTabList.TableDay.DATE.getCloumn(),
																						MyTabList.TableDay.RECORD_STATE.getCloumn()}, 
						MyTabList.TableDay.RECORD_STATE.getCloumn()+" = ?", new String[]{"4"}, MyTabList.TableDay.DATE.getCloumn());
				if(list.size() >= 1) {	//因为之前就会插入上一个周期的数据，所以数据库中最少也会有一条数据
					Collections.reverse(list);
					for(int i = 0 ; i < list.size(); i++) {
						if(DataUtils.compareString(date,list.get(i).getDate())) {
							date = list.get(i).getDate();
							break;
						}						
					}
				}	
				if (list == null || list.size() ==0) {
					date = getYesterDay();
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			date = getYesterDay();
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			date = getYesterDay();
		}
		return date;
	}
	
	/**
	 * 根据职业编号获取职业名称
	 * 
	 * @param occupationId
	 * @return
	 */
	public static String getOccupation(String occupationId) {
		if (occupationId.equals("200000000")) {
			return "未分类";
		} else if (occupationId.equals("200000001")) {
			return "IT";
		} else if (occupationId.equals("200000002")) {
			return "金融";
		} else if (occupationId.equals("200000003")) {
			return "人事行政";
		} else if (occupationId.equals("200000004")) {
			return "教育法律";
		} else if (occupationId.equals("200000005")) {
			return "销售";
		} else if (occupationId.equals("200000006")) {
			return "房地产/建筑";
		} else if (occupationId.equals("200000007")) {
			return "文化传媒";
		} else if (occupationId.equals("200000008")) {
			return "物流";
		} else if (occupationId.equals("200000009")) {
			return "制造生产";
		} else if (occupationId.equals("200000010")) {
			return "医疗";
		} else if (occupationId.equals("200000011")) {
			return "服务业";
		} else if (occupationId.equals("200000012")) {
			return "学生/其它";
		}
		return null;
	}
	
	/**
	 * 判断当前是否有网络连接
	 * @param context
	 * @return
	 */
	public static boolean isNetExits(Context context) {
		boolean netStatus = false;
		try{
			ConnectivityManager connectManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectManager.getActiveNetworkInfo();
			if(activeNetworkInfo != null) {
				if(activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected()) {
					netStatus = true;
				}
			}	
		}catch(Exception e) {
			e.printStackTrace();
		}
		return netStatus;
	}
	
	/**
	 * 获取当日文案id，返回为null，则需要自行生成并运行插入代码
	 * @param context
	 * @param date
	 * @param sleeptime
	 * @param sleeplength
	 * @param uptime
	 * @return
	 */
	public static String queryDayDocid(Context context,String date,String sleeptime,String sleeplength,String uptime) {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPTIME);
		String docid = operate.querDocid(date,sleeptime,uptime,sleeplength);
		if(!TextUtils.isEmpty(docid)) {
			operate.close();
			return docid == null ? "" : docid;
		}
		operate.close();
		return "";
	}
	
	/**
	 * 插入当日文案id
	 * @param context
	 * @param date
	 * @param docid
	 */
	public static void insertDayDocid(Context context,String date,String docid) {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPTIME);
		ContentValues cv = new ContentValues();
		cv.put("date", date);
		cv.put("fileid", docid);
		operate.update(cv, "date = ?", new String[]{date});
		operate.close();
	}
	
	/**
	 * 判断周表中是否有该周的数据
	 * @param context
	 * @param weeknumber
	 * @return
	 */
	public static boolean isWeekExits(Context context,String weeknumber) {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.WEEK);
		int count = operate.queryCount(new String[]{weeknumber});
		if(count > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取入睡时间文案id，返回为null，则需要自行生成并运行插入代码
	 * @param context
	 * @param weeknumber	周数
	 * @param sleeptime		平均入睡时间
	 * @return
	 */
	public static String queryWeekSleepId(Context context,String weeknumber,String sleeptime) {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.WEEK);
		if(isWeekExits(context,weeknumber)) {
			String weekXFileid = operate.querySleepid(weeknumber,sleeptime);
			if(TextUtils.isEmpty(weekXFileid)) {
				operate.close();
				return "";
			}else {
				operate.close();
				return weekXFileid == null ? "" : weekXFileid;
			}
		}
		operate.close();
		return "";
	}
	
	/**
	 * 插入平均入睡时间文案id
	 * @param context
	 * @param weeknumber	周数
	 * @param sleepid		平均入睡文案id
	 */
	public static void insertWeekSleepId(Context context,String weeknumber,String sleepid,String sleeptime) {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.WEEK);
		ContentValues cv = new ContentValues();
		cv.put("sleepid", sleepid);
		cv.put("weeknumber", weeknumber);
		cv.put("sleeptime", sleeptime);
		if(isWeekExits(context,weeknumber)) {
			operate.update(cv, "weeknumber = ?", new String[]{weeknumber});
		}else {
			operate.insert(cv);	
		}
		operate.close();
	}
	
	/**
	 * 获取平均睡眠时长文案id，返回为null，则需要自行生成并运行插入代码
	 * @param context
	 * @param weeknumber	周数
	 * @param avgtime	平均睡眠时长
	 * @return
	 */
	public static String queryWeekAvgId(Context context,String weeknumber,String avgtime) {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.WEEK);
		if(isWeekExits(context,weeknumber)) {
			String weekXFileid = operate.queryAvgid(weeknumber,avgtime);
			if(TextUtils.isEmpty(weekXFileid)) {
				operate.close();
				return "";
			}else {
				operate.close();
				return weekXFileid == null ? "" : weekXFileid;
			}
		}
		operate.close();
		return "";
	}
	
	/**
	 * 插入平均睡眠时长文案id
	 * @param context
	 * @param weeknumber	周数
	 * @param avgid		平均睡眠时长文案id
	 */
	public static void insertWeekAvgId(Context context,String weeknumber,String avgid,String avgtime) {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.WEEK);
		ContentValues cv = new ContentValues();
		cv.put("avgid", avgid);
		cv.put("weeknumber", weeknumber);
		cv.put("avgtime", avgtime);
		if(isWeekExits(context,weeknumber)) {
			operate.update(cv, "weeknumber = ?", new String[]{weeknumber});
		}else {
			operate.insert(cv);	
		}
		operate.close();
	}
	
	/**
	 * 获取平均起床时间文案id，返回为null，则需要自行生成并运行插入代码
	 * @param context
	 * @param weeknumber	周数
	 * @param uptime	平均起床时间
	 * @return
	 */
	public static String queryWeekUpId(Context context,String weeknumber,String uptime) {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.WEEK);
		if(isWeekExits(context,weeknumber)) {
			String weekXFileid = operate.queryUpid(weeknumber,uptime);
			if(TextUtils.isEmpty(weekXFileid)) {
				operate.close();
				return "";
			}else {
				operate.close();
				return weekXFileid == null ? "" : weekXFileid;
			}
		}
		operate.close();
		return "";
	}
	
	/**
	 * 插入平均起床时间文案id
	 * @param context
	 * @param weeknumber	周数
	 * @param upid		起床文案id
	 */
	public static void insertWeekUpId(Context context,String weeknumber,String upid,String uptime) {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.WEEK);
		ContentValues cv = new ContentValues();
		cv.put("upid", upid);
		cv.put("weeknumber", weeknumber);
		cv.put("uptime", uptime);
		if(isWeekExits(context,weeknumber)) {
			operate.update(cv, "weeknumber = ?", new String[]{weeknumber});
		}else {
			operate.insert(cv);	
		}
		operate.close();
	}
	
	/**
	 * 比较两个字符串的asc码值，返回true则表示str1大于等于str2，false则表示str1小于str2
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean compareString(String str1,String str2) {
		if(str1.length() == str2.length()) {
			for(int i=0;i<str1.length();i++) {
				if(str1.charAt(i) > str2.charAt(i)) {
					return true;
				}
				if(str1.charAt(i) < str2.charAt(i)) {
					return false;
				}
			}
		}
		if(str1.length() > str2.length()) {
			return true;
		}
		if(str1.length() < str2.length()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断数据库中是否有date这一天的结果数据
	 * @param context
	 * @param date
	 * @return
	 */
	public static boolean isDateExits(Context context,String date) {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPTIME);
		String result = operate.query(MyTabList.TableDay.DATE.getCloumn(), MyTabList.TableDay.DATE.getCloumn()+" = ?", new String[]{date});
		operate.close();
		if(!TextUtils.isEmpty(result)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取date周期的开始时间t1
	 * @param date 格式为yyyy-MM-dd
	 * @param context
	 * @return 当为空则返回""，否则返回t1的毫秒数的字符串，是以1990年为准
	 * @throws ParseException 
	 * @throws NumberFormatException 
	 */
	public static String getT1(String date,Context context) throws NumberFormatException, ParseException {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPTIME);
		String t1 = operate.query(new String[]{MyTabList.TableDay.DATE.getCloumn(),
												MyTabList.TableDay.STARTTIME.getCloumn()}, 
												MyTabList.TableDay.DATE.getCloumn()+" = ?", 
												new String[]{date}, 1);
		if(TextUtils.isEmpty(t1)) {
			return "";
		}else {
			return getSS(date) + (Long.valueOf(t1) * 60 * 1000) + "";
		}
	}
	
	/**
	 * 获取date周期的结束时间t2
	 * @param date 格式为yyyy-MM-dd
	 * @param context
	 * @return 当为空则返回""，否则返回t2的毫秒数的字符串，是以1990年为准
	 * @throws ParseException 
	 * @throws NumberFormatException 
	 */
	public static String getT2(String date,Context context) throws NumberFormatException, ParseException {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPTIME);
		String t2 = operate.query(new String[]{MyTabList.TableDay.DATE.getCloumn(),
												MyTabList.TableDay.ENDTIME.getCloumn()}, 
												MyTabList.TableDay.DATE.getCloumn()+" = ?", 
												new String[]{date}, 1);
		if(TextUtils.isEmpty(t2)) {
			return "";
		}else {
			return getSS(date) + (Long.valueOf(t2) * 60 * 1000) + "";
		}
	}
	
	
	/**
	 * 获取指定周期的指定时间
	 * @param date
	 * @param specifiedTime  单位为毫秒
	 * @return
	 */
	public static long getCycleTime(String date,long specifiedTime) {
		
		return 0;
	}
	
	/**
	 * 根据当前的时间设置获取指定日期的指定时间点的毫秒
	 * @param date
	 * @param specifiedTime  单位为毫秒
	 * @return
	 * @throws ParseException 
	 */
	public static long getCurrentTime(String date,long specifiedTime) throws ParseException {
		long start = getAllerateStartTime();
		long end = getAllerateStopTime();
		if(start >= end || start < (4*60*60*1000)) {
			if(specifiedTime <= end) {
				return getSS(date) + specifiedTime + 24*60*60*1000;
			}else {
				return getSS(date) + specifiedTime;
			}

		}else {
			
		}
		return 0;
	}	
	
	/**
	 * 按照当前的时间设置获取date周期的t1
	 * @param date
	 * @return
	 * @throws ParseException 
	 */
	public static long getT1(String date) throws ParseException {
		long ss = getSS(date);
		if(getAllerateStartTime() < 4*60*60*1000) {
			return ss + getAllerateStartTime() + 24*60*60*1000;
		}else {
			return ss + getAllerateStartTime();
		}
	}
	
	/**
	 * 按照当前的时间设置获取date周期的t2
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static long getT2(String date) throws ParseException {
		long ss = getSS(date);
		if(getAllerateStartTime() > getAllerateStopTime() || getAllerateStartTime() < 4*60*60*1000) {
			return ss + getAllerateStopTime() + 24*60*60*1000;
		}else {
			return ss + getAllerateStopTime();
		}
	}
	
	/**
	 * 根据设置的t1、t2来获取date周期的t1
	 * @param date
	 * @param t1	单位为分钟数
	 * @param t2	单位为分钟数
	 * @return 单位毫秒
	 * @throws ParseException
	 */
	public static long getT1(String date,long t1,long t2) throws ParseException {
		long ss = getSS(date);
		if(t1 < 4*60) {
			return ss + t1 * 60 * 1000 + 24*60*60*1000;
		}else {
			return ss + t1 * 60 * 1000;
		}
	}
	
	/**
	 * 根据设置的t1、t2来获取date周期的t2
	 * @param date
	 * @param t1
	 * @param t2
	 * @return 单位毫秒
	 * @throws ParseException
	 */
	public static long getT2(String date,long t1,long t2) throws ParseException {
		long ss = getSS(date);
		if(t1 > t2 || t1 < 4*60) {
			return ss + t2 * 60 * 1000 + 24*60*60*1000;
		}else {
			return ss + t2 * 60 * 1000;
		}
	}

	
	/**
	 * 清除数据
	 * @throws ParseException 
	 */
	public static void deleteData(Context context) throws ParseException {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPDATA);
		MytabOperate timeoperate = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPTIME);
		String date = DataUtils.getRecordDate(new Date());
		operate.delete(MyTabList.TableSleepData.DATE.getCloumn()+" = ?", new String[]{date});
		String str = timeoperate.query(MyTabList.TableDay.DATE.getCloumn(), MyTabList.TableDay.DATE.getCloumn()+" = ?",
				new String[]{date} );
		if(TextUtils.isEmpty(str)) {
			ContentValues cv = new ContentValues();
			cv.put(MyTabList.TableDay.DATE.getCloumn(), date);
			cv.put(MyTabList.TableDay.STARTTIME.getCloumn(), SleepInfo.SET_STARTTIME);
			cv.put(MyTabList.TableDay.ENDTIME.getCloumn(), SleepInfo.SET_ENDTIME);
			timeoperate.insert(cv);
		}else {
			ContentValues cv = new ContentValues();
			cv.put(MyTabList.TableDay.DIAGRAMDATA.getCloumn(), "");
			cv.put(MyTabList.TableDay.SLEEPTIME.getCloumn(), "");
			timeoperate.update(cv,MyTabList.TableDay.DATE.getCloumn()+" = ?", new String[]{date});
		}
		operate.close();
		timeoperate.close();
	}

	public static SimpleDateFormat breakSdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	/**
	 * 插入数据
	 * @throws ParseException 
	 */
	public static void newData(long starttime,long endtime,Context context,String date) throws ParseException {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		MytabOperate dataOperte = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPDATA);
		SleepData data = dataOperte.queryInsertData();
		ContentValues cv = new ContentValues();
		cv.put(MyTabList.TableSleepData.DATE.getCloumn(), date);
		cv.put(MyTabList.TableSleepData.X.getCloumn(), data.getX());
		cv.put(MyTabList.TableSleepData.Y.getCloumn(), data.getY());
		cv.put(MyTabList.TableSleepData.Z.getCloumn(), data.getZ());
		for(long i = starttime; i < endtime; i = i + (SleepInfo.ADDATA_TIME)) {
			cv.put(MyTabList.TableSleepData.TIME.getCloumn(), i);
			dataOperte.insert(cv);
		}
		dataOperte.close();
		PreManager.instance().saveDownTime(context.getApplicationContext(), System.currentTimeMillis()+"");
	}
	
	
	/**
	 * 直接设置睡点和起点
	 * @throws ParseException 
	 */
	public static void stritSetTime(Context context,String sleepTime,String upTime,String date) throws ParseException {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		MytabOperate timeOperte = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPTIME);
		String str = timeOperte.query(MyTabList.TableDay.DATE.getCloumn(), MyTabList.TableDay.DATE.getCloumn()+" = ?",
				new String[]{date} );
		if(TextUtils.isEmpty(str)) {
			ContentValues cv = new ContentValues();
			cv.put(MyTabList.TableDay.DATE.getCloumn(), date);
			cv.put(MyTabList.TableDay.STARTTIME.getCloumn(), SleepInfo.SET_STARTTIME);
			cv.put(MyTabList.TableDay.ENDTIME.getCloumn(), SleepInfo.SET_ENDTIME);
			cv.put(MyTabList.TableDay.SLEEPTIME.getCloumn(), sleepTime);
			cv.put(MyTabList.TableDay.UPTIME.getCloumn(), upTime);
			cv.put(MyTabList.TableDay.ISCHANGE.getCloumn(), "1");
			cv.put(MyTabList.TableDay.ORGSLEEPTIME.getCloumn(),sleepTime);
			cv.put(MyTabList.TableDay.ORGUPTIME.getCloumn(),upTime);
			cv.put(MyTabList.TableDay.RECORD_STATE.getCloumn(), "4");
			timeOperte.insert(cv);
		}else {
			ContentValues cv = new ContentValues();
			cv.put(MyTabList.TableDay.STARTTIME.getCloumn(), SleepInfo.SET_STARTTIME);
			cv.put(MyTabList.TableDay.ENDTIME.getCloumn(), SleepInfo.SET_ENDTIME);
			cv.put(MyTabList.TableDay.SLEEPTIME.getCloumn(), sleepTime);
			cv.put(MyTabList.TableDay.UPTIME.getCloumn(), upTime);
			cv.put(MyTabList.TableDay.ISCHANGE.getCloumn(), "1");
			cv.put(MyTabList.TableDay.ORGSLEEPTIME.getCloumn(),sleepTime);
			cv.put(MyTabList.TableDay.ORGUPTIME.getCloumn(),upTime);
			cv.put(MyTabList.TableDay.RECORD_STATE.getCloumn(), "4");
			timeOperte.update(cv,MyTabList.TableDay.DATE.getCloumn()+" = ?", new String[]{date});
		}
		timeOperte.close();
	}
	
	/**
	 * 删除指定志气前指定天数的历史数据
	 * @param context
	 * @param dayCounts
	 */
	public static void deleteHistoricalData(Context context, Date date,int dayCounts) {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPDATA);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, - dayCounts);
		String whereClause = MyTabList.TableSleepData.TIME.getCloumn()+" <= ?";
		String[] whereArgs = new String[]{calendar.getTimeInMillis()+""};
		operate.delete(whereClause, whereArgs);
		operate.close();
	}
	
	/**
	 * 将指定日期的指定时间之前的数据删除
	 * @param context
	 * @param deleteTime
	 * @param date
	 */
	public static void deleteData(Context context,long deleteTime,String date) {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPDATA);
		String whereClause = MyTabList.TableSleepData.TIME.getCloumn()+" < ? and "+MyTabList.TableSleepData.DATE+" = ?";
		String[] whereArgs = new String[]{deleteTime+"",date};
		operate.delete(whereClause, whereArgs);
		operate.close();
	}
	
	/**
	 * 获取date（格式为yyyy-MM-dd）的记录状态
	 * @param date
	 */
	public static String getRecordState(Context context , String date) {
		String record_state = "";
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPTIME);
		record_state = operate.query(new String[]{MyTabList.TableDay.RECORD_STATE.getCloumn(),
									MyTabList.TableDay.DATE.getCloumn()}, 
									MyTabList.TableDay.DATE.getCloumn()+" = ?", 
									new String[]{date}, 0);
		operate.close();
		return record_state;
	}
	
	/**
	 * 保存date（格式为yyyy-MM-dd）的记录状态
	 * @param context
	 * @param date
	 * @return
	 */
	public static void saveRecordState(Context context , String date,String record_state) {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPTIME);
		ContentValues cv = new ContentValues();
		cv.put(MyTabList.TableDay.RECORD_STATE.getCloumn(), record_state);
		operate.update(cv, 
				MyTabList.TableDay.DATE.getCloumn()+"= ?", 
				new String[]{date});
		operate.close();
	}	
	
	public static JudgeObject getJudge(String fault_state,String date,Context context) throws ParseException {
		JudgeObject judgeObject = new JudgeObject();
		long starttime = 0;
		long endtime = 0;
		long currenttime = System.currentTimeMillis();
		if(TextUtils.isEmpty(fault_state)) {
			return null;
		}else if(fault_state.equals(FaultState.A.getState())) {
			starttime = getT1(date);
			endtime = getT2(date);
			
			judgeObject.setJudgeStarttime(starttime);
			judgeObject.setJudgeEndtime(endtime < currenttime ? endtime : currenttime);
		}else if(fault_state.equals(FaultState.B.getState())) {
			return null;
		}else if(fault_state.equals(FaultState.C.getState())) {
			starttime = Long.valueOf(PreManager.instance().getRestartTime(context));
			endtime = getT2(date);
			
			judgeObject.setJudgeStarttime(starttime);
			judgeObject.setJudgeEndtime(endtime < currenttime ? endtime : currenttime);
		}else if(fault_state.equals(FaultState.D.getState())) {
			starttime = getT1(date) + SleepInfo.BEFORE_SLEEP*60*1000;
			endtime = getT2(date);
			
			judgeObject.setJudgeStarttime(starttime);
			judgeObject.setJudgeEndtime(endtime < currenttime ? endtime : currenttime);
		}else if(fault_state.equals(FaultState.E.getState())) {
			starttime = getT1(date);
			endtime = getT2(date) - SleepInfo.AFTER_SLEEP*60*1000;
			
			judgeObject.setJudgeStarttime(starttime);
			judgeObject.setJudgeEndtime(endtime);
		}else if(fault_state.equals(FaultState.F.getState())) {
			starttime = getT1(date);
			endtime = getT2(date) - SleepInfo.AFTER_SLEEP*60*1000;
			
			judgeObject.setJudgeStarttime(starttime);
			judgeObject.setJudgeEndtime(endtime);
		}else if(fault_state.equals(FaultState.G.getState())) {
			starttime = getT1(date);
			endtime = getT2(date);
			
			judgeObject.setJudgeStarttime(starttime);
			judgeObject.setJudgeEndtime(endtime);
		}else if(fault_state.equals(FaultState.H.getState())) {
			starttime = getT1(date);
			endtime = getT2(date);
			
			judgeObject.setJudgeStarttime(starttime);
			judgeObject.setJudgeEndtime(endtime < currenttime ? endtime : currenttime);
		}else if(fault_state.equals(FaultState.I.getState())) {
			starttime = getT1(date);
			endtime = getT2(date);
			
			judgeObject.setJudgeStarttime(starttime);
			judgeObject.setJudgeEndtime(endtime < currenttime ? endtime : currenttime);
//		}else if(fault_state.equals(FaultState.J.getState())) {
//			starttime = Long.valueOf(PreManager.instance().getRestartTime(context));
//			endtime = getT2(date);
//			
//			judgeObject.setJudgeStarttime(starttime);
//			judgeObject.setJudgeEndtime(endtime < currenttime ? endtime : currenttime);
		}else if(fault_state.equals(FaultState.K.getState())) {
			starttime = getT1(date);
			endtime = getT2(date) - SleepInfo.AFTER_SLEEP*60*1000;
			
			judgeObject.setJudgeStarttime(starttime);
			judgeObject.setJudgeEndtime(endtime);
		}else if(fault_state.equals(FaultState.L.getState())) {
			starttime = getT1(date);
			endtime = getT2(date);
			
			judgeObject.setJudgeStarttime(starttime);
			judgeObject.setJudgeEndtime(endtime < currenttime ? endtime : currenttime);
		}else if(fault_state.equals(FaultState.M.getState())) {
			starttime = getT1(date);
			endtime = getT2(date);
			
			judgeObject.setJudgeStarttime(starttime);
			judgeObject.setJudgeEndtime(endtime < currenttime ? endtime : currenttime);
		}else if(fault_state.equals(FaultState.N.getState())) {
			starttime = getT1(date);
			endtime = getT2(date);
			
			judgeObject.setJudgeStarttime(starttime);
			judgeObject.setJudgeEndtime(endtime < currenttime ? endtime : currenttime);
		}else if(fault_state.equals(FaultState.O.getState())) {
			starttime = PreManager.instance().getOldT1(context);
			endtime = Long.valueOf(PreManager.instance().getRestartTime(context));
			
			judgeObject.setJudgeStarttime(starttime);
			judgeObject.setJudgeEndtime(endtime);
		}else if(fault_state.equals(FaultState.P.getState())) {
			starttime =PreManager.instance().getOldT1(context);
			endtime = Long.valueOf(PreManager.instance().getRestartTime(context));
			
			judgeObject.setJudgeStarttime(starttime);
			judgeObject.setJudgeEndtime(endtime);
		}else if(fault_state.equals(FaultState.Q.getState())) {
			starttime = Long.valueOf(PreManager.instance().getRestartTime(context));
			endtime = getT2(date);
			
			judgeObject.setJudgeStarttime(starttime);
			judgeObject.setJudgeEndtime(endtime < currenttime ? endtime : currenttime);
		}else if(fault_state.equals(FaultState.R.getState())) {
			starttime = getT1(date);
			endtime = getT2(date);
			
			judgeObject.setJudgeStarttime(starttime);
			judgeObject.setJudgeEndtime(endtime < currenttime ? endtime : currenttime);
		}else if(fault_state.equals(FaultState.S.getState())) {
			starttime = Long.valueOf(PreManager.instance().getRestartTime(context));
			endtime = getT2(date);
			
			judgeObject.setJudgeStarttime(starttime);
			judgeObject.setJudgeEndtime(endtime < currenttime ? endtime : currenttime);
		}else if(fault_state.equals(FaultState.T.getState())) {
			starttime = getT1(date);
			endtime = getT2(date);
			
			judgeObject.setJudgeStarttime(starttime);
			judgeObject.setJudgeEndtime(endtime < currenttime ? endtime : currenttime);
		}
		return judgeObject;
	}
	
	/**
	 * 首次安装需要把所有状态置1
	 * @param context
	 */
	public static void initState(Context context) {
		PreManager.instance().saveAnalysisState(context.getApplicationContext(), "1");
		PreManager.instance().saveAmendState(context.getApplicationContext(), "1");
		PreManager.instance().saveAccState(context.getApplicationContext(), "1");
		PreManager.instance().saveSetupState(context.getApplicationContext(), "1");
		PreManager.instance().saveMainState(context.getApplicationContext(), "1");
	}
	
	/**
	 * 获取最近一个月的有效周期
	 * @param context
	 * @param date
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static List<SleepResult> getResult(Context context,String currentDate) throws Exception {
		List<SleepResult> list = new ArrayList<SleepResult>();
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPTIME);
		String lastMonthDate = DataUtils.getLastMonthDay(currentDate);
		String[] columns = new String[]{MyTabList.TableDay.DATE.getCloumn(), 
										MyTabList.TableDay.RECORD_STATE.getCloumn(),
										MyTabList.TableDay.ISUPLOAD.getCloumn()};
		String selection = MyTabList.TableDay.DATE.getCloumn()+" <= ? and "+
										MyTabList.TableDay.DATE.getCloumn()+" >= ? and "+
										MyTabList.TableDay.RECORD_STATE.getCloumn()+" = ? and "+
										MyTabList.TableDay.ISUPLOAD.getCloumn()+" = ?";
		String[] selectionArgs = new String[]{currentDate,lastMonthDate,"4","0"};
//		String selection = MyTabList.TableDay.RECORD_STATE.getCloumn()+" = ? and "+
//				MyTabList.TableDay.ISUPLOAD.getCloumn()+" = ?";
//		String[] selectionArgs = new String[]{"4","0"};
		list = (List<SleepResult>) operate.query(columns, selection, selectionArgs, MyTabList.TableDay.DATE.getCloumn());
		return list;
	}
	
	/**
	 * 
	 * @param oldDate  格式yyyy-MM-dd
	 * @return 返回格式"MM月dd日"
	 */
	public static String dateFomate(String oldDate) {
		String newString = oldDate.substring(oldDate.indexOf("-") + 1,oldDate.length());
		String monthString = newString.substring(0,newString.indexOf("-"));
		if(monthString.startsWith("0")) {
			monthString = monthString.substring(1,monthString.length());
		}
		String dayString = newString.substring(newString.indexOf("-") + 1,newString.length());
		if(dayString.startsWith("0")) {
			dayString = dayString.substring(1,dayString.length());
		}
		return monthString + "月" + dayString + "日";
	}
	
	
	public static String dateFomate(List<DaySleepMsg> list, int position) {
		if(position == list.size() - 1) {
			return "昨";
		} else {
			return dateFomate(list.get(position).getDate());
		}
	}
	
	/**
	 * 
	 * @param oldDate  日期格式
	 * @return 返回格式"MM月dd日"
	 */
	public static String dateFomate(Date oldDate) {
		SimpleDateFormat weakSdf = new SimpleDateFormat("MM-dd");
		String startDate = weakSdf.format(oldDate.getTime());
		String endDate = weakSdf.format(oldDate.getTime());
		startDate = startDate.substring(0,startDate.indexOf("-"));
		endDate = endDate.substring(endDate.indexOf("-")+1,endDate.length());
		if(startDate.startsWith("0")) {
			startDate = startDate.substring(1,startDate.length());
		} else {
			startDate = startDate.substring(0, startDate.length());
		}
		if(endDate.startsWith("0")) {
			endDate = endDate.substring(1,endDate.length());
		} else {
			endDate = endDate.substring(0,endDate.length());
		}
		return startDate + "月" + endDate + "日";
	}	
}
