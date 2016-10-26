package com.yzm.sleep.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SleepSignDataBean implements Serializable {
	
	private String xiaolv;//        睡眠效率     
	private String sleep;//        (入睡时间)    
	private String wakeup;   //     （起床时间）    
	private String sleeplong;//   （睡眠时长）
	private String bedlong;//     （在床时长，题8-题1）
	public String getBedlong() {
		return bedlong;
	}
	public void setBedlong(String bedlong) {
		this.bedlong = bedlong;
	}
	private String date;     //   数据所属日期
	public String getXiaolv() {
		return xiaolv;
	}
	public void setXiaolv(String xiaolv) {
		this.xiaolv = xiaolv;
	}
	public String getSleep() {
		return sleep;
	}
	public void setSleep(String sleep) {
		this.sleep = sleep;
	}
	public String getWakeup() {
		return wakeup;
	}
	public void setWakeup(String wakeup) {
		this.wakeup = wakeup;
	}
	public String getSleeplong() {
		return sleeplong;
	}
	public void setSleeplong(String sleeplong) {
		this.sleeplong = sleeplong;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

}
