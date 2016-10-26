package com.yzm.sleep.bean;

import java.io.Serializable;

public class ReportDataBean implements Serializable {
	
	private String xiaolv;//        睡眠效率  
	private String sleep;//        (入睡时间)    
	private String wakeup;//     （起床时间）    
	private String sleeplong;//   （入睡时长）
	private String bedlong;//     （在床时长）
	private String ti_1;//         1. 昨晚几点上床
	private String ti_2;//         2. 昨晚几点开始尝试睡觉
	private String ti_3;//         3. 花了多长时间入睡
	private String ti_4;//         4. 中途会醒来几次
	private String ti_5;//         5. 中途醒来的总时长
	private String ti_6;//         6. 早晨几点醒来
	private String ti_7;//         7. 比计划早醒多久
	private String ti_8;//         8. 几点离床
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
	public String getBedlong() {
		return bedlong;
	}
	public void setBedlong(String bedlong) {
		this.bedlong = bedlong;
	}
	public String getTi_1() {
		return ti_1;
	}
	public void setTi_1(String ti_1) {
		this.ti_1 = ti_1;
	}
	public String getTi_2() {
		return ti_2;
	}
	public void setTi_2(String ti_2) {
		this.ti_2 = ti_2;
	}
	public String getTi_3() {
		return ti_3;
	}
	public void setTi_3(String ti_3) {
		this.ti_3 = ti_3;
	}
	public String getTi_4() {
		return ti_4;
	}
	public void setTi_4(String ti_4) {
		this.ti_4 = ti_4;
	}
	public String getTi_5() {
		return ti_5;
	}
	public void setTi_5(String ti_5) {
		this.ti_5 = ti_5;
	}
	public String getTi_6() {
		return ti_6;
	}
	public void setTi_6(String ti_6) {
		this.ti_6 = ti_6;
	}
	public String getTi_7() {
		return ti_7;
	}
	public void setTi_7(String ti_7) {
		this.ti_7 = ti_7;
	}
	public String getTi_8() {
		return ti_8;
	}
	public void setTi_8(String ti_8) {
		this.ti_8 = ti_8;
	}
	
	
}
