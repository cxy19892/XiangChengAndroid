package com.yzm.sleep.bean;

import java.io.Serializable;

public class DayReportBean implements Serializable {
	private String qiandaoid;     //签到ID
	private String days;     //还有多少天出周报
	private String feeling;   //昨晚睡眠感觉（1感觉好；-1感觉差；0没有选择 )
	private String xiaolv;        //睡眠效率     
	private String sleep;        //(入睡时间)    
	private String wakeup;     //（起床时间）    
	private String sleeplong;   //（睡眠时长）
	private String report;       //日报告文字内容
	private String urltype;   // 跳转类型( 1活动详情 ; 2话题详情  ;  3文章详情 ; 4 小组 )      
	private String urlid;       //跳转的id
	
	public String getQiandaoid() {
		return qiandaoid;
	}
	public void setQiandaoid(String qiandaoid) {
		this.qiandaoid = qiandaoid;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getFeeling() {
		return feeling;
	}
	public void setFeeling(String feeling) {
		this.feeling = feeling;
	}
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
	public String getReport() {
		return report;
	}
	public void setReport(String report) {
		this.report = report;
	}
	public String getUrltype() {
		return urltype;
	}
	public void setUrltype(String urltype) {
		this.urltype = urltype;
	}
	public String getUrlid() {
		return urlid;
	}
	public void setUrlid(String urlid) {
		this.urlid = urlid;
	}
	
	
}
