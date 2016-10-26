package com.yzm.sleep.bean;

import java.io.Serializable;

public class SleepStatusBean implements Serializable {

	private String id;      //
	private String uid;          //
	private String dateline;          //
	private String datestring;         //
	private String answer;         // 
	private String sleeplong;      //睡眠时长 分钟
	private String xiaolv;          //睡眠效率  小数
	private String sleep1;          //(入睡是否正常)   文字   
	private String sleep2;         //（中途是否易醒）    文字 
	private String sleep3;         //（早晨是否早醒） 文字 
	private String ribao;          //日报   文字 
	private String sleep;          //(入睡时间)     
	private String wakeup;         //（起床时间） 
	private String feeling; 	//感觉
	private String flag;         //（中途是否易醒）    
	private String type; 
	private String deepsleep; //深度睡眠时长 ; 分钟
	private String qiansleep; //浅度睡眠时长 ; 分钟
	
	private String sleepspeed;  //   入睡速度（分钟）
	private String wakelong;//      中途醒来时长（分钟）
	private String wakezao;//         早晨早醒时长（分钟）
    	 
	public String getSleepspeed() {
		return sleepspeed;
	}
	public void setSleepspeed(String sleepspeed) {
		this.sleepspeed = sleepspeed;
	}
	public String getWakelong() {
		return wakelong;
	}
	public void setWakelong(String wakelong) {
		this.wakelong = wakelong;
	}
	public String getWakezao() {
		return wakezao;
	}
	public void setWakezao(String wakezao) {
		this.wakezao = wakezao;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getDateline() {
		return dateline;
	}
	public void setDateline(String dateline) {
		this.dateline = dateline;
	}
	public String getDatestring() {
		return datestring;
	}
	public void setDatestring(String datestring) {
		this.datestring = datestring;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getRibao() {
		return ribao;
	}
	public void setRibao(String ribao) {
		this.ribao = ribao;
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
	public String getFeeling() {
		return feeling;
	}
	public void setFeeling(String feeling) {
		this.feeling = feeling;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSleeplong() {
		return sleeplong;
	}
	public void setSleeplong(String sleeplong) {
		this.sleeplong = sleeplong;
	}
	public String getXiaolv() {
		return xiaolv;
	}
	public void setXiaolv(String xiaolv) {
		this.xiaolv = xiaolv;
	}
	public String getSleep1() {
		return sleep1;
	}
	public void setSleep1(String sleep1) {
		this.sleep1 = sleep1;
	}
	public String getSleep2() {
		return sleep2;
	}
	public void setSleep2(String sleep2) {
		this.sleep2 = sleep2;
	}
	public String getSleep3() {
		return sleep3;
	}
	public void setSleep3(String sleep3) {
		this.sleep3 = sleep3;
	}
	public String getDeepsleep() {
		return deepsleep;
	}
	public void setDeepsleep(String deepsleep) {
		this.deepsleep = deepsleep;
	}
	public String getQiansleep() {
		return qiansleep;
	}
	public void setQiansleep(String qiansleep) {
		this.qiansleep = qiansleep;
	}
	
	
	
}
