package com.yzm.sleep.model;

import java.io.Serializable;

import android.R.integer;

public class SignInData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8206282760809581681L;
	
	/*所属日期*/
	private String date;
	/*签到Id*/
	private String signInId;
	/*上床时间*/
	private String goBedTime;
	/*尝试入睡时间*/
	private String trySleepTime;
	/*多长时间入睡 ，单位:分钟*/
	private int howLongSleepTime;
	/*醒来次数*/
	private int wakeCount;
	/*醒来总时长， 单位：分钟*/
	private int howLongWakeTime;
	/*醒来时间*/
	private String wakeUpTime;
	/*比技术提前多久醒来：单位：分钟*/
	private int wakeEarlyTime;
	/*起床时间*/
	private String outBedTime;
	/*软件还是硬件 0代表软件1代表硬件*/
	private String softOrOrange;
	/*硬件数据是否签到成功， 1表示签到失败，0表示签到成功*/
	private int upload;
	/*浅度睡眠时长 minutes*/
	private int shallowsleep;
	/*深度睡眠时长 minutes*/
	private int deepsleep;
	/*签到的返回结果*/
	private String result;
	
	
	/**
	 * 是否已经从网络获取
	 */
	private boolean isLoad;
	
	public boolean isLoad() {
		return isLoad;
	}
	public void setLoad(boolean isLoad) {
		this.isLoad = isLoad;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getGoBedTime() {
		return goBedTime;
	}
	public void setGoBedTime(String goBedTime) {
		this.goBedTime = goBedTime;
	}
	public String getTrySleepTime() {
		return trySleepTime;
	}
	public void setTrySleepTime(String trySleepTime) {
		this.trySleepTime = trySleepTime;
	}
	public int getHowLongSleepTime() {
		return howLongSleepTime;
	}
	public void setHowLongSleepTime(int howLongSleepTime) {
		this.howLongSleepTime = howLongSleepTime;
	}
	public int getWakeCount() {
		return wakeCount;
	}
	public void setWakeCount(int wakeCount) {
		this.wakeCount = wakeCount;
	}
	public int getHowLongWakeTime() {
		return howLongWakeTime;
	}
	public void setHowLongWakeTime(int howLongWakeTime) {
		this.howLongWakeTime = howLongWakeTime;
	}
	public String getWakeUpTime() {
		return wakeUpTime;
	}
	public void setWakeUpTime(String wakeUpTime) {
		this.wakeUpTime = wakeUpTime;
	}
	public int getWakeEarlyTime() {
		return wakeEarlyTime;
	}
	public void setWakeEarlyTime(int wakeEarlyTime) {
		this.wakeEarlyTime = wakeEarlyTime;
	}
	public String getOutBedTime() {
		return outBedTime;
	}
	public void setOutBedTime(String outBedTime) {
		this.outBedTime = outBedTime;
	}
	public String getSignInId() {
		return signInId;
	}
	public void setSignInId(String signInId) {
		this.signInId = signInId;
	}
	public int getShallowsleep() {
		return shallowsleep;
	}
	public void setShallowsleep(int shallowsleep) {
		this.shallowsleep = shallowsleep;
	}
	public int getDeepsleep() {
		return deepsleep;
	}
	public void setDeepsleep(int deepsleep) {
		this.deepsleep = deepsleep;
	}
	public String getSoftOrOrange() {
		return softOrOrange;
	}
	public void setSoftOrOrange(String softOrOrange) {
		this.softOrOrange = softOrOrange;
	}
	public int getUpload() {
		return upload;
	}
	public void setUpload(int upload) {
		this.upload = upload;
	}
	
}
