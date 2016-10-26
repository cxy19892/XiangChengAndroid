package com.yzm.sleep.bean;

public class SleepDayDataBean {
	/**0-睡眠时长   1-睡觉时间  2- 起床时间,3-平均睡眠时间*/
	private int sleepType =0;
	private float sleepLength;
	private String sleepTime;
	private String getupTime;
	public int getSleepType() {
		return sleepType;
	}
	public void setSleepType(int sleepType) {
		this.sleepType = sleepType;
	}
	public float getSleepLength() {
		return sleepLength;
	}
	public void setSleepLength(float sleepLength) {
		this.sleepLength = sleepLength;
	}
	public String getSleepTime() {
		return sleepTime;
	}
	public void setSleepTime(String sleepTime) {
		this.sleepTime = sleepTime;
	}
	public String getGetupTime() {
		return getupTime;
	}
	public void setGetupTime(String getupTime) {
		this.getupTime = getupTime;
	}
	
	
}
