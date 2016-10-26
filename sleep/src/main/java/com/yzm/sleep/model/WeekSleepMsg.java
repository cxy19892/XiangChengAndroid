package com.yzm.sleep.model;

import java.util.ArrayList;

public class WeekSleepMsg implements Comparable<WeekSleepMsg>{
	private int weakNumber;				//当前周的周数
	private String weakTitle;			//周统计题目
	private boolean isSame = true;		//当周数据范围是否一致
	private String averageSleeptime;	//平均入睡时间
	private String averageUptime;		//平均起床时间
	private String averageSleeplength;	//平均睡眠时长
	private float sleepHourMax;         //一周睡眠最长时长
	public ArrayList<SleepStatistics> sleepStatistics = new ArrayList<SleepStatistics>();
	public ArrayList<SleepStatistics> getSleepStatistics() {
		return sleepStatistics;
	}
	public void setSleepStatistics(ArrayList<SleepStatistics> sleepStatistics) {
		this.sleepStatistics = sleepStatistics;
	}
	public int getWeakNumber() {
		return weakNumber;
	}
	public void setWeakNumber(int weakNumber) {
		this.weakNumber = weakNumber;
	}
	public String getWeakTitle() {
		return weakTitle;
	}
	public void setWeakTitle(String weakTitle) {
		this.weakTitle = weakTitle;
	}
	public boolean isSame() {
		return isSame;
	}
	public void setSame(boolean isSame) {
		this.isSame = isSame;
	}
	public String getAverageSleeptime() {
		return averageSleeptime;
	}
	public void setAverageSleeptime(String averageSleeptime) {
		this.averageSleeptime = averageSleeptime;
	}
	public String getAverageUptime() {
		return averageUptime;
	}
	public void setAverageUptime(String averageUptime) {
		this.averageUptime = averageUptime;
	}
	public String getAverageSleeplength() {
		return averageSleeplength;
	}
	public void setAverageSleeplength(String averageSleeplength) {
		this.averageSleeplength = averageSleeplength;
	}
	
	public float getSleepHourMax() {
		return sleepHourMax;
	}
	public void setSleepHourMax(float sleepHourMax) {
		this.sleepHourMax = sleepHourMax;
	}
	@Override
	public int compareTo(WeekSleepMsg arg0) {
		// TODO Auto-generated method stub
		if(this.weakNumber > arg0.getWeakNumber()) {
			return 1;
		}else if(this.weakNumber < arg0.getWeakNumber()) {
			return -1;
		}else {
			return 0;	
		}
	}
}
