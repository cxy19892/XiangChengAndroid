package com.yzm.sleep.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SleepStatistics implements Serializable,Comparable<SleepStatistics>{
	private static final long serialVersionUID = 1L;
	private String date;					//当日日期
	private boolean isEmpty = true;		//当日是否有数据
	public String sleep_time = "00:00";				//入睡时间
	public String getup_time = "00:00";				//起床时间
	public float sleep_time_value;
	public float getup_time_value;
	public float sleep_hour;  //睡眠时长
	public float sleep_hour_value;
	private String sleepTimeSetting;    //睡觉时刻预设值
	private String getupTimeSetting;    //起床时刻预设值
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	@Override
	public int compareTo(SleepStatistics arg0) {
		// TODO Auto-generated method stub
		try {
			long thisDate = sdf.parse(this.date).getTime();
			long otherDate = sdf.parse(arg0.getDate()).getTime();
			if(thisDate - otherDate > 0) {
				return 1;
			}else if(thisDate - otherDate < 0) {
				return -1;
			}else {
				return 0;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSleep_time() {
		return sleep_time;
	}
	public void setSleep_time(String sleep_time) {
		this.sleep_time = sleep_time;
	}
	public String getGetup_time() {
		return getup_time;
	}
	public void setGetup_time(String getup_time) {
		this.getup_time = getup_time;
	}
	public float getSleep_time_value() {
		return sleep_time_value;
	}
	public void setSleep_time_value(float sleep_time_value) {
		this.sleep_time_value = sleep_time_value;
	}
	public float getGetup_time_value() {
		return getup_time_value;
	}
	public void setGetup_time_value(float getup_time_value) {
		this.getup_time_value = getup_time_value;
	}
	public boolean isEmpty() {
		return isEmpty;
	}
	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}
	public float getSleep_hour() {
		return sleep_hour;
	}
	public void setSleep_hour(float sleep_hour) {
		this.sleep_hour = sleep_hour;
	}
	public float getSleep_hour_value() {
		return sleep_hour_value;
	}
	public void setSleep_hour_value(float sleep_hour_value) {
		this.sleep_hour_value = sleep_hour_value;
	}
	public String getSleepTimeSetting() {
		return sleepTimeSetting;
	}
	public void setSleepTimeSetting(String sleepTimeSetting) {
		this.sleepTimeSetting = sleepTimeSetting;
	}
	public String getGetupTimeSetting() {
		return getupTimeSetting;
	}
	public void setGetupTimeSetting(String getupTimeSetting) {
		this.getupTimeSetting = getupTimeSetting;
	}
	
	
	
}
