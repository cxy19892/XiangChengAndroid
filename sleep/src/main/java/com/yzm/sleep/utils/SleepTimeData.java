package com.yzm.sleep.utils;

public class SleepTimeData{
	private boolean state;
	private String errMsg;
	private String date;
	
	/*HH:mm格式的字符串*/
	private String gbStr;
	private String tsStr;
	private String wuStr;
	private String obStr;
	/*long型的时间戳*/
	private long goBedTime;
	private long trySleepTime;
	private long wakeUpTime;
	private long outBedTime;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public long getGoBedTime() {
		return goBedTime;
	}
	public void setGoBedTime(long goBedTime) {
		this.goBedTime = goBedTime;
	}
	public long getTrySleepTime() {
		return trySleepTime;
	}
	public void setTrySleepTime(long trySleepTime) {
		this.trySleepTime = trySleepTime;
	}
	public long getWakeUpTime() {
		return wakeUpTime;
	}
	public void setWakeUpTime(long wakeUpTime) {
		this.wakeUpTime = wakeUpTime;
	}
	public long getOutBedTime() {
		return outBedTime;
	}
	public void setOutBedTime(long outBedTime) {
		this.outBedTime = outBedTime;
	}
	public String getGbStr() {
		return gbStr;
	}
	public void setGbStr(String gbStr) {
		this.gbStr = gbStr;
	}
	public String getTsStr() {
		return tsStr;
	}
	public void setTsStr(String tsStr) {
		this.tsStr = tsStr;
	}
	public String getWuStr() {
		return wuStr;
	}
	public void setWuStr(String wuStr) {
		this.wuStr = wuStr;
	}
	public String getObStr() {
		return obStr;
	}
	public void setObStr(String obStr) {
		this.obStr = obStr;
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
}
