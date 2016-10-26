package com.yzm.sleep.model;

import java.util.ArrayList;

public class DaySleepMsg{
	private static final long serialVersionUID = 1L;
	public String date = "";
	/**睡眠时间是否达到8小时*/
	public boolean isAchieve = false;
	/**睡眠时间达到的小时值*/
	public float sleeping_hours = 0;
	/**用户设置的睡觉时刻*/
	public String sleep_time_setting = "";
	/**用户设置的起床时刻*/
	public String getup_time_setting = "";
	/**实际睡觉时刻*/
	public String sleep_time = "";
	/**实际起床时刻*/
	public String getup_time = "";
	/**实际睡觉时刻加速度*/
	public float sleep_acce;
	/**实际起床时刻加速度*/
	public float getup_acce;
	public String beauty_sleep = "";
	/**手机活动及睡眠时间分布数据*/
	public ArrayList<SleepDistributionInfo> dists = new ArrayList<SleepDistributionInfo>();
	/**是否显示分析详情*/
	public boolean isShowDetail = false;
	
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public boolean isAchieve() {
		return isAchieve;
	}
	public void setAchieve(boolean isAchieve) {
		this.isAchieve = isAchieve;
	}
	public float getSleeping_hours() {
		return sleeping_hours;
	}
	public void setSleeping_hours(float sleeping_hours) {
		this.sleeping_hours = sleeping_hours;
	}
	public String getSleep_time_setting() {
		return sleep_time_setting;
	}
	public void setSleep_time_setting(String sleep_time_setting) {
		this.sleep_time_setting = sleep_time_setting;
	}
	public String getGetup_time_setting() {
		return getup_time_setting;
	}
	public void setGetup_time_setting(String getup_time_setting) {
		this.getup_time_setting = getup_time_setting;
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
	public float getSleep_acce() {
		return sleep_acce;
	}
	public void setSleep_acce(float sleep_acce) {
		this.sleep_acce = sleep_acce;
	}
	public float getGetup_acce() {
		return getup_acce;
	}
	public void setGetup_acce(float getup_acce) {
		this.getup_acce = getup_acce;
	}
	public String getBeauty_sleep() {
		return beauty_sleep;
	}
	public void setBeauty_sleep(String beauty_sleep) {
		this.beauty_sleep = beauty_sleep;
	}
	public ArrayList<SleepDistributionInfo> getDists() {
		return dists;
	}
	public void setDists(ArrayList<SleepDistributionInfo> dists) {
		this.dists = dists;
	}
	public boolean getShowDetail() {
		return isShowDetail;
	}
	public void setShowDetail(boolean isShowDetail) {
		this.isShowDetail = isShowDetail;
	}
}
