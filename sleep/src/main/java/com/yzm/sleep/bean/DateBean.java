package com.yzm.sleep.bean;

import java.io.Serializable;

public class DateBean implements Serializable{
	
	private String date;
	private String state;
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	/**
	 * 状态
	 * @return 0亚健康(未达标),1健康(达标),其它 无
	 */
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
