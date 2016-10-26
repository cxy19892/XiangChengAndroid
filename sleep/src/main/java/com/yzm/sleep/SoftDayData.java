package com.yzm.sleep;

import java.io.Serializable;

public class SoftDayData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String sleepTime;
	private String getUpTime;
	private String startTime;
	private String endTime;
	private String date;
	private boolean yesterDay;
	
	private String sleepTimeLong;
	private String getUpTimeLong;
	
	private boolean isChange;
	
	private float sleepLength;  //睡眠时长 单位小时
	
	
	private int TotalSleepTime = 0;//睡眠时长  分钟数
	
	
	public int getTotalSleepTime() {
		return TotalSleepTime;
	}
	public void setTotalSleepTime(int totalSleepTime) {
		TotalSleepTime = totalSleepTime;
	}
	public String getSleepTime() {
		return sleepTime;
	}
	public void setSleepTime(String sleepTime) {
		this.sleepTime = sleepTime;
	}
	public String getGetUpTime() {
		return getUpTime;
	}
	public void setGetUpTime(String getUpTime) {
		this.getUpTime = getUpTime;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public boolean isYesterDay() {
		return yesterDay;
	}
	public void setYesterDay(boolean yesterDay) {
		this.yesterDay = yesterDay;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getSleepTimeLong() {
		return sleepTimeLong;
	}
	public void setSleepTimeLong(String sleepTimeLong) {
		this.sleepTimeLong = sleepTimeLong;
	}
	public String getGetUpTimeLong() {
		return getUpTimeLong;
	}
	public void setGetUpTimeLong(String getUpTimeLong) {
		this.getUpTimeLong = getUpTimeLong;
	}
	public boolean isChange() {
		return isChange;
	}
	public void setChange(boolean isChange) {
		this.isChange = isChange;
	}
	public float getSleepLength() {
		return sleepLength;
	}
	public void setSleepLength(float sleepLength) {
		this.sleepLength = sleepLength;
	}
	
}
