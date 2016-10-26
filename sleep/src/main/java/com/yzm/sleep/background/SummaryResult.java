package com.yzm.sleep.background;

/**
 * 表“sleep_result”记录的数据，不是最终的统计数据
 * @author Administrator
 *
 */
public class SummaryResult {
	private String date;
	private float startacce;
	private float endacce;
	private String starttime;
	private String endtime;
	private String sleeptime;
	private String uptime;
	
	public String getSleeptime() {
		return sleeptime;
	}
	public void setSleeptime(String sleeptime) {
		this.sleeptime = sleeptime;
	}
	public String getUptime() {
		return uptime;
	}
	public void setUptime(String uptime) {
		this.uptime = uptime;
	}
	public float getStartacce() {
		return startacce;
	}
	public void setStartacce(float startacce) {
		this.startacce = startacce;
	}
	public float getEndacce() {
		return endacce;
	}
	public void setEndacce(float endacce) {
		this.endacce = endacce;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	
	
}
