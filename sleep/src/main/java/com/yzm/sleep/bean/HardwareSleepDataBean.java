package com.yzm.sleep.bean;

import java.io.Serializable;

public class HardwareSleepDataBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String date;               //     睡眠数据所属日期
	private String xstart;             //    起始时间
	private String xstop;              //      结束时间
	private String ymax;               //       Y轴最大量化值 
	private String insleeptime;        //     入睡时刻
	private String outsleeptime;       //     醒来时刻 
	private String totalsleeptime;     //     睡眠时长
	private String deepsleep;          //      深度睡眠总时长
	private String shallowsleep;       //        浅度睡眠总时长
	private String awaketimesleep;     //       清醒时长（单位分钟）
	private String onbed;              //             在床时长
	private String tosleep;            //        入睡速度（单位分钟）
	private String awakecount;         //        清醒次数  
	private String awakenogetupcount;  //    赖床时间      
	private String gotobedtime;        //    上床时间
	private String getuptime;          //       起床时间      
	private String listlength;         //           每日数据集条数
	private String sleepbak1;          //    备用字段1
	private String sleepbak2;          //    备用字段1
	private String modelkey;           //      model文件下载地址
	private String user_location_x;    //     x坐标
	private String user_location_y;    //    y坐标
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getXstart() {
		return xstart;
	}
	public void setXstart(String xstart) {
		this.xstart = xstart;
	}
	public String getXstop() {
		return xstop;
	}
	public void setXstop(String xstop) {
		this.xstop = xstop;
	}
	public String getYmax() {
		return ymax;
	}
	public void setYmax(String ymax) {
		this.ymax = ymax;
	}
	public String getInsleeptime() {
		return insleeptime;
	}
	public void setInsleeptime(String insleeptime) {
		this.insleeptime = insleeptime;
	}
	public String getOutsleeptime() {
		return outsleeptime;
	}
	public void setOutsleeptime(String outsleeptime) {
		this.outsleeptime = outsleeptime;
	}
	public String getTotalsleeptime() {
		return totalsleeptime;
	}
	public void setTotalsleeptime(String totalsleeptime) {
		this.totalsleeptime = totalsleeptime;
	}
	public String getDeepsleep() {
		return deepsleep;
	}
	public void setDeepsleep(String deepsleep) {
		this.deepsleep = deepsleep;
	}
	public String getShallowsleep() {
		return shallowsleep;
	}
	public void setShallowsleep(String shallowsleep) {
		this.shallowsleep = shallowsleep;
	}
	public String getAwaketimesleep() {
		return awaketimesleep;
	}
	public void setAwaketimesleep(String awaketimesleep) {
		this.awaketimesleep = awaketimesleep;
	}
	public String getOnbed() {
		return onbed;
	}
	public void setOnbed(String onbed) {
		this.onbed = onbed;
	}
	public String getTosleep() {
		return tosleep;
	}
	public void setTosleep(String tosleep) {
		this.tosleep = tosleep;
	}
	public String getAwakecount() {
		return awakecount;
	}
	public void setAwakecount(String awakecount) {
		this.awakecount = awakecount;
	}
	public String getAwakenogetupcount() {
		return awakenogetupcount;
	}
	public void setAwakenogetupcount(String awakenogetupcount) {
		this.awakenogetupcount = awakenogetupcount;
	}
	public String getGotobedtime() {
		return gotobedtime;
	}
	public void setGotobedtime(String gotobedtime) {
		this.gotobedtime = gotobedtime;
	}
	public String getGetuptime() {
		return getuptime;
	}
	public void setGetuptime(String getuptime) {
		this.getuptime = getuptime;
	}
	public String getListlength() {
		return listlength;
	}
	public void setListlength(String listlength) {
		this.listlength = listlength;
	}
	public String getSleepbak1() {
		return sleepbak1;
	}
	public void setSleepbak1(String sleepbak1) {
		this.sleepbak1 = sleepbak1;
	}
	public String getSleepbak2() {
		return sleepbak2;
	}
	public void setSleepbak2(String sleepbak2) {
		this.sleepbak2 = sleepbak2;
	}
	public String getModelkey() {
		return modelkey;
	}
	public void setModelkey(String modelkey) {
		this.modelkey = modelkey;
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
	
	
	
	
	
}
