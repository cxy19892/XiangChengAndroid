package com.yzm.sleep.render;

import java.io.Serializable;

public class ShareDataBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String date;
	private String sleepTime;
	private String getUpTime;
	private String sleepLength;
	private String targetSleepLength;
	private String beautyTime;
	private String medal;
	private String totalSleepLength;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
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
	public String getSleepLength() {
		return sleepLength;
	}
	public void setSleepLength(String sleepLength) {
		this.sleepLength = sleepLength;
	}
	public String getTargetSleepLength() {
		return targetSleepLength;
	}
	public void setTargetSleepLength(String targetSleepLength) {
		this.targetSleepLength = targetSleepLength;
	}
	public String getBeautyTime() {
		return beautyTime;
	}
	public void setBeautyTime(String beautyTime) {
		this.beautyTime = beautyTime;
	}
	public String getMedal() {
		return medal;
	}
	public void setMedal(String medal) {
		this.medal = medal;
	}
	public String getTotalSleepLength() {
		return totalSleepLength;
	}
	public void setTotalSleepLength(String totalSleepLength) {
		this.totalSleepLength = totalSleepLength;
	}
	
}
