package com.yzm.sleep.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.model.DaySleepMsg;

public class DateOperaUtil {

	@SuppressLint("SimpleDateFormat") 
	public static SoftDayData comperaDate(SoftDayData softDayData){
		try {
			String date = softDayData.getDate();
			String inSleepTime = softDayData.getSleepTime();
			String outSleepTime = softDayData.getGetUpTime();
			String inSleepTimeLong = softDayData.getSleepTimeLong();
			String outSleepTimeLong = softDayData.getGetUpTimeLong();
			
			//获取归属日期
			Calendar calendar0 = Calendar.getInstance();
			calendar0.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
			
			long t1 = new SimpleDateFormat("HH:mm").parse("00:00").getTime();
			long t2 = new SimpleDateFormat("HH:mm").parse("12:00").getTime();
			long st = new SimpleDateFormat("HH:mm").parse(inSleepTime).getTime();
			if(st > t1 && st < t2){
				Calendar calendar_in = Calendar.getInstance();
				calendar_in.setTime(new Date(Long.parseLong(inSleepTimeLong)));
				Calendar calendar_out = Calendar.getInstance();
				calendar_out.setTime(new Date(Long.parseLong(outSleepTimeLong)));
				
				Calendar calendar1 = Calendar.getInstance();
				calendar1.setTime(new Date(Long.parseLong(inSleepTimeLong)));
				calendar1.add(Calendar.DAY_OF_MONTH, -1);
				calendar1.getTime();
				String stDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar1.getTime());
				if(!stDate.equals(date)){
					Calendar calendar2 = Calendar.getInstance();
					calendar2.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(stDate));
					
					int days = (int) ((calendar0.getTime().getTime() - calendar2.getTime().getTime()) / (24 * 60 * 60 * 1000));
					calendar_in.add(Calendar.DAY_OF_MONTH, days);
					calendar_out.add(Calendar.DAY_OF_MONTH, days);
					
					softDayData.setSleepTimeLong(String.valueOf(calendar_in.getTime().getTime()));
					softDayData.setGetUpTimeLong(String.valueOf(calendar_out.getTime().getTime()));
					
					softDayData.setChange(true);
				}
			}
			
			//新补丁以此判断日期超出时间范围的入睡时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			String newInSleepTimes = softDayData.getSleepTimeLong();
			String newOutSleepTimes = softDayData.getGetUpTimeLong();
			String d1 = sdf.format(new Date(Long.parseLong(newInSleepTimes)));
			String d2 = sdf.format(new Date(Long.parseLong(newOutSleepTimes)));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(date));
			
			Calendar calendar00 = Calendar.getInstance();
			calendar00.setTime(sdf.parse(d1));
			
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(sdf.parse(date));
			calendar1.add(Calendar.DAY_OF_MONTH, 1);
			
			if(calendar00.getTime().getTime() < calendar.getTime().getTime() || calendar00.getTime().getTime() > calendar1.getTime().getTime()){
				if(st > t1 && st < t2){
					newInSleepTimes = String.valueOf(sdf1.parse(sdf.format(calendar1.getTime()) + " " + inSleepTime).getTime());
				}else{
					newInSleepTimes = String.valueOf(sdf1.parse(date + " " + inSleepTime).getTime());
				}
				softDayData.setSleepTimeLong(newInSleepTimes);
				softDayData.setGetUpTimeLong(newOutSleepTimes);
				softDayData.setChange(true);
			}
			
			//如果起床时间小于入睡时间
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(new Date(Long.parseLong(newInSleepTimes)));
			Calendar calendar3 = Calendar.getInstance();
			calendar3.setTime(new Date(Long.parseLong(newOutSleepTimes)));
			if(calendar3.getTime().getTime() < calendar2.getTime().getTime()){
				newOutSleepTimes = String.valueOf(sdf1.parse(sdf.format(calendar1.getTime()) + " " + outSleepTime).getTime());
				softDayData.setSleepTimeLong(newInSleepTimes);
				softDayData.setGetUpTimeLong(newOutSleepTimes);
				softDayData.setChange(true);
			}
			
			Calendar calendar11 = Calendar.getInstance();
			calendar11.setTime(calendar1.getTime());
			calendar11.add(Calendar.DAY_OF_MONTH, 1);
			
			// 如果起床时间大于正常时间范围
			if(calendar3.getTime().getTime() > (calendar11.getTime().getTime() - 1000)){
				newOutSleepTimes = String.valueOf(sdf1.parse(sdf.format(calendar1.getTime()) + " " + outSleepTime).getTime());
				softDayData.setSleepTimeLong(newInSleepTimes);
				softDayData.setGetUpTimeLong(newOutSleepTimes);
				softDayData.setChange(true);
				
				if(Long.parseLong(newOutSleepTimes) < Long.parseLong(newInSleepTimes)){
					Calendar calendar_new = Calendar.getInstance();
					calendar_new.setTime(new Date(Long.parseLong(newInSleepTimes)));
					calendar_new.add(Calendar.DAY_OF_MONTH, -1);
					
					newInSleepTimes = String.valueOf(sdf1.parse(sdf.format(calendar_new.getTime()) + " " + inSleepTime).getTime());
					softDayData.setSleepTimeLong(newInSleepTimes);
				}
			}
			
			Calendar calendar12 = Calendar.getInstance();
			calendar12.setTime(new Date(Long.parseLong(newInSleepTimes)));
			calendar12.add(Calendar.DAY_OF_MONTH, -1);
			if(sdf.format(calendar12.getTime()).equals(date)){
				if(st > t2){
					Calendar calendar_newo = Calendar.getInstance();
					calendar_newo.setTime(new Date(Long.parseLong(newOutSleepTimes)));
					calendar_newo.add(Calendar.DAY_OF_MONTH, -1);
					
					newInSleepTimes = String.valueOf(calendar12.getTime().getTime());
					newOutSleepTimes = String.valueOf(calendar_newo.getTime().getTime());
					softDayData.setSleepTimeLong(newInSleepTimes);
					softDayData.setGetUpTimeLong(newOutSleepTimes);
					softDayData.setChange(true);
				}
			}
			
			
			//补丁3
			String dateStr = softDayData.getDate();
			String inSleepStr = softDayData.getSleepTime();
			String outSleepStr = softDayData.getGetUpTime();
			long insleepLong = Long.parseLong(softDayData.getSleepTimeLong());
			long outsleepLong = Long.parseLong(softDayData.getGetUpTimeLong());
			
			Calendar calendar8 = Calendar.getInstance();
			calendar8.setTime(sdf.parse(dateStr));
			
			Calendar calendar81 = Calendar.getInstance();
			calendar81.setTime(sdf.parse(sdf.format(new Date(insleepLong))));
			Calendar calendar82 = Calendar.getInstance();
			calendar82.setTime(sdf.parse(sdf.format(new Date(outsleepLong))));
			if(calendar81.getTime().getTime() > calendar8.getTime().getTime() && calendar82.getTime().getTime() > calendar8.getTime().getTime()){
				if(new SimpleDateFormat("HH:mm").parse(inSleepStr).getTime() > t1 && new SimpleDateFormat("HH:mm").parse(inSleepStr).getTime() < t2 &&
						new SimpleDateFormat("HH:mm").parse(outSleepStr).getTime() > t1 && new SimpleDateFormat("HH:mm").parse(outSleepStr).getTime() < t2
						){
					if(new SimpleDateFormat("HH:mm").parse(inSleepStr).getTime() > new SimpleDateFormat("HH:mm").parse(outSleepStr).getTime()){
						calendar8.add(Calendar.DAY_OF_MONTH, 1);
						String secondStr = sdf.format(calendar8.getTime());
						softDayData.setSleepTimeLong(String.valueOf(sdf1.parse(dateStr + " " + inSleepStr).getTime()));
						softDayData.setGetUpTimeLong(String.valueOf(sdf1.parse(secondStr + " " + outSleepStr).getTime()));
					}
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return softDayData;
	}
	
	/**
	 * 打开签到的数据日期纠正
	 * @param data   传入参数date、gbStr、obStr、tsStr、wuStr 5个值必填
	 * @return       时间会带日期格式已 goBedTime、trySleepTime、wakeUpTime、outBedTime的值传出来.
	 * 				  如果时间格式正确，则返回 state参数为true，反之为false，并且有errMsg打印错误信息
	 */
	@SuppressLint("SimpleDateFormat") 
	public static SleepTimeData comperaTime(SleepTimeData data){
		SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-ddHH:mm");
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String currtday = data.getDate();
			Calendar c = Calendar.getInstance();
			c.setTime(sdf3.parse(currtday));
			c.add(Calendar.DAY_OF_MONTH, 1);
			String tomorrday = sdf3.format(c.getTime());
			
			String gbStr = data.getGbStr();
			String obStr = data.getObStr();
			String tsStr = data.getTsStr();
			String wuStr = data.getWuStr();
			long t = sdf1.parse("12:00").getTime();
			long gbStrLn = sdf1.parse(gbStr).getTime();
			long obStrLn = sdf1.parse(obStr).getTime();
			long tsStrLn = sdf1.parse(tsStr).getTime();
			long wuStrLn = sdf1.parse(wuStr).getTime();
			if(gbStrLn > t){
				data.setGoBedTime(sdf2.parse(currtday + gbStr).getTime());
				if(obStrLn > gbStrLn){
					data.setOutBedTime(sdf2.parse(currtday + obStr).getTime());
					if(tsStrLn >= gbStrLn && tsStrLn < obStrLn){
						data.setTrySleepTime(sdf2.parse(currtday + tsStr).getTime());
					}else{
						data.setState(false);
						data.setErrMsg("尝试睡觉时间不合理");
						return data;
					}
					
					if(wuStrLn > tsStrLn && wuStrLn <= obStrLn){
						data.setWakeUpTime(sdf2.parse(currtday + wuStr).getTime());
					}else{
						data.setState(false);
						data.setErrMsg("醒来时间不合理");
						return data;
					}
				}else{
					data.setOutBedTime(sdf2.parse(tomorrday + obStr).getTime());
					if(tsStrLn >= gbStrLn){
						data.setTrySleepTime(sdf2.parse(currtday + tsStr).getTime());
						
						if(wuStrLn > tsStrLn){
							data.setWakeUpTime(sdf2.parse(currtday + wuStr).getTime());
						}else if(wuStrLn <= obStrLn){
							data.setWakeUpTime(sdf2.parse(tomorrday + wuStr).getTime());
						}
						else{
							data.setState(false);
							data.setErrMsg("醒来时间不合理");
							return data;
						}
					}else if(tsStrLn < obStrLn){
						data.setTrySleepTime(sdf2.parse(tomorrday + tsStr).getTime());
						
						if(wuStrLn > tsStrLn && wuStrLn <= obStrLn){
							data.setWakeUpTime(sdf2.parse(tomorrday + wuStr).getTime());
						}
						else{
							data.setState(false);
							data.setErrMsg("醒来时间不合理");
							return data;
						}
					}else{
						data.setState(false);
						data.setErrMsg("尝试睡觉时间不合理");
						return data;
					}
					
					
				}
				
			}else{
				if(obStrLn > gbStrLn){
					data.setGoBedTime(sdf2.parse(tomorrday + gbStr).getTime());
					data.setOutBedTime(sdf2.parse(tomorrday + obStr).getTime());
					if(tsStrLn >= gbStrLn && tsStrLn < obStrLn){
						data.setTrySleepTime(sdf2.parse(tomorrday + tsStr).getTime());
					}else{
						data.setState(false);
						data.setErrMsg("尝试睡觉时间不合理");
						return data;
					}
					
					if(wuStrLn > tsStrLn && wuStrLn <= obStrLn){
						data.setWakeUpTime(sdf2.parse(tomorrday + wuStr).getTime());
					}else{
						data.setState(false);
						data.setErrMsg("醒来时间不合理");
						return data;
					}
				}else{
					data.setGoBedTime(sdf2.parse(currtday + gbStr).getTime());
					data.setOutBedTime(sdf2.parse(tomorrday + obStr).getTime());
					if(tsStrLn >= gbStrLn){
						data.setTrySleepTime(sdf2.parse(currtday + tsStr).getTime());
						
						if(wuStrLn > tsStrLn){
							data.setWakeUpTime(sdf2.parse(currtday + wuStr).getTime());
						}else if(wuStrLn <= obStrLn){
							data.setWakeUpTime(sdf2.parse(tomorrday + wuStr).getTime());
						}
						else{
							data.setState(false);
							data.setErrMsg("醒来时间不合理");
							return data;
						}
					}else if(tsStrLn < obStrLn){
						data.setTrySleepTime(sdf2.parse(tomorrday + tsStr).getTime());
						
						if(wuStrLn > tsStrLn && wuStrLn <= obStrLn){
							data.setWakeUpTime(sdf2.parse(tomorrday + wuStr).getTime());
						}
						else{
							data.setState(false);
							data.setErrMsg("醒来时间不合理");
							return data;
						}
					}else{
						data.setState(false);
						data.setErrMsg("尝试睡觉时间不合理");
						return data;
					}
					
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		data.setState(true);
		return data;
	}
	
	/**
	 * 获取当天的睡眠数据
	 * @return
	 */
	public static  DaySleepMsg getDaySleepData(Context context, String date) {
		if (context == null) 
			return null;
		try {
			MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
			SQLiteDatabase db = helper.getWritableDatabase();
			Cursor cursor = db.rawQuery("select * from " + MyTabList.SLEEPTIME + " where date=?", new String[]{date});
			
			cursor.moveToFirst();
			String sleepTime = cursor.getString(cursor.getColumnIndex(MyTabList.TableDay.SLEEPTIME.getCloumn()));
			String getUpTime = cursor.getString(cursor.getColumnIndex(MyTabList.TableDay.UPTIME.getCloumn()));
			String dateString = cursor.getString(cursor.getColumnIndex(MyTabList.TableDay.DATE.getCloumn()));
//			String sleep_time_setting = cursor.getString(cursor.getColumnIndex(MyTabList.TableDay.ORGSLEEPTIME.getCloumn()));
			DaySleepMsg dayData = new DaySleepMsg();
			dayData.setSleep_time(sleepTime);//(sleepTime);
			dayData.setGetup_time(getUpTime);//GetUpTime(getUpTime);
			dayData.setDate(dateString);
//			dayData.setSleep_time_setting(sleep_time_setting);
			cursor.close();
			
			return dayData;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
}
