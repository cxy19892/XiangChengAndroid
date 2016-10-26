package com.yzm.sleep.background;

import java.io.Serializable;
import java.util.ArrayList;

import com.yzm.sleep.model.SleepDistributionInfo;
import com.yzm.sleep.utils.SleepUtils;

public class SleepResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String date;
	private String starttime;
	private String endtime;
	private String sleeptime;
	private String uptime;
	private boolean haveSleep = true;
	private String sleepLength = "0";
	private String healthSleep = "0";
	private float sleep_acce = 0;	//入睡时刻加速度
	private float getup_acce = 0;	//起床时刻加速度
	private float maxacce = 0;
	private String update = "0";
	private String upload = "0";
	private String orgsleeptime;
	private String orguptime;
	private String sleeptimelong;
	private String uptimelong;
	private String recordState;
	private String fileid;
	private ArrayList<SleepDistributionInfo> infoList = new ArrayList<SleepDistributionInfo>();		//曲线图各个显示点数据
	
	public float getMaxacce() {
		return maxacce;
	}
	public void setMaxacce(float maxacce) {
		this.maxacce = maxacce;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public boolean isHaveSleep() {
		return haveSleep;
	}
	public void setHaveSleep(boolean haveSleep) {
		this.haveSleep = haveSleep;
	}
	public String getSleepLength() {
		return sleepLength;
	}
	public void setSleepLength(String sleepLength) {
		this.sleepLength = sleepLength;
	}
	public String getHealthSleep() {
		return healthSleep;
	}
	public void setHealthSleep(String healthSleep) {
		this.healthSleep = healthSleep;
	}
	public float getSleep_acce() {
		return sleep_acce;
	}
	public void setSleep_acce(float sleep_acce) {
		this.sleep_acce = sleep_acce;
	}
	public float getGetup_acce() {
		return getup_acce;
	}
	public void setGetup_acce(float getup_acce) {
		this.getup_acce = getup_acce;
	}
	public ArrayList<SleepDistributionInfo> getInfoList() {
		return infoList;
	}
	public void setInfoList(ArrayList<SleepDistributionInfo> infoList) {
		this.infoList = infoList;
	}
	public String getUpdate() {
		return update;
	}
	public void setUpdate(String update) {
		this.update = update;
	}
	public String getUpload() {
		return upload;
	}
	public void setUpload(String upload) {
		this.upload = upload;
	}
	public String getOrgsleeptime() {
		return orgsleeptime;
	}
	public void setOrgsleeptime(String orgsleeptime) {
		this.orgsleeptime = orgsleeptime;
	}
	public String getSleeptimelong() {
		return sleeptimelong;
	}
	public void setSleeptimelong(String sleeptimelong) {
		this.sleeptimelong = sleeptimelong;
	}
	public String getUptimelong() {
		return uptimelong;
	}
	public void setUptimelong(String uptimelong) {
		this.uptimelong = uptimelong;
	}
	public String getOrguptime() {
		return orguptime;
	}
	public void setOrguptime(String orguptime) {
		this.orguptime = orguptime;
	}
	public String getFileid() {
		return fileid;
	}
	public void setFileid(String fileid) {
		this.fileid = fileid;
	}
	public String getRecordState() {
		return recordState;
	}
	public void setRecordState(String recordState) {
		this.recordState = recordState;
	}
	public boolean haveEmpty() {
		if(this.infoList.size() == 0 || SleepUtils.isEmpty(this.date) || SleepUtils.isEmpty(this.endtime) || SleepUtils.isEmpty(this.sleepLength) || SleepUtils.isEmpty(this.sleeptime)
				|| SleepUtils.isEmpty(this.starttime) || SleepUtils.isEmpty(this.uptime) || SleepUtils.isEmpty(this.healthSleep)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 是否入睡时间和起床时间都存在，因为在某一天没有数据的时候，如果只修改了其中一个时间是没法画图的
	 * @return
	 */
	public boolean isUpdate() {
		if(!SleepUtils.isEmpty(this.sleeptime) && !SleepUtils.isEmpty(this.uptime)) {
			return true;
		}
		return false;
	}
	
	public boolean haveUpdate() {
		if("1".equals(this.update)) {
			return true;
		}
		return false;
	}
	
	public void copy(SleepResult sr) {
		this.date = sr.getDate();
		this.endtime = sr.getEndtime();
		this.getup_acce = sr.getGetup_acce();
		this.haveSleep = sr.isHaveSleep();
		this.healthSleep = sr.getHealthSleep();
		this.infoList = sr.getInfoList();
		this.maxacce = sr.getMaxacce();
		this.sleep_acce = sr.getSleep_acce();
		this.sleepLength = sr.getSleepLength();
		this.sleeptime = sr.getSleeptime();
		this.starttime = sr.getStarttime();
		this.update = sr.getUpdate();
		this.uptime = sr.getUptime();
	}
}
