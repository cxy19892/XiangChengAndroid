package com.yzm.sleep.bean;

import java.io.Serializable;

public class UserSleepDataBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String date;     //数据所属日期，格式为：20151014
	private String sleep_point;  //睡觉时间，格式为：201510150813
	private String wakeup_point; //起床时间，格式为：201510150813
	private String bueatysleep_duration; // 睡眠总时长
	private String bueatysleep_duration1; //睡眠总时长 分钟
	private String user_location_x;    //x坐标
	private String user_location_y;    //y坐标
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSleep_point() {
		return sleep_point;
	}
	public void setSleep_point(String sleep_point) {
		this.sleep_point = sleep_point;
	}
	public String getWakeup_point() {
		return wakeup_point;
	}
	public void setWakeup_point(String wakeup_point) {
		this.wakeup_point = wakeup_point;
	}
	public String getBueatysleep_duration() {
		return bueatysleep_duration;
	}
	public void setBueatysleep_duration(String bueatysleep_duration) {
		this.bueatysleep_duration = bueatysleep_duration;
	}
	public String getUser_location_x() {
		return user_location_x;
	}
	public void setUser_location_x(String user_location_x) {
		this.user_location_x = user_location_x;
	}
	public String getUser_location_y() {
		return user_location_y;
	}
	public void setUser_location_y(String user_location_y) {
		this.user_location_y = user_location_y;
	}
	public String getBueatysleep_duration1() {
		return bueatysleep_duration1;
	}
	public void setBueatysleep_duration1(String bueatysleep_duration1) {
		this.bueatysleep_duration1 = bueatysleep_duration1;
	}
	
	
}
