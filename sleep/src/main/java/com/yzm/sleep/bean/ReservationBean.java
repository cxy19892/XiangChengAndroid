package com.yzm.sleep.bean;

import java.io.Serializable;

public class ReservationBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;//          预约id
	private String yname;//      预约的医生名字
	private String ytime;//       预约时间   ****年**月**日   **:**
	private String flag; // 为1需要跳转到详情；为0不需要进入详情
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getYname() {
		return yname;
	}
	public void setYname(String yname) {
		this.yname = yname;
	}
	public String getYtime() {
		return ytime;
	}
	public void setYtime(String ytime) {
		this.ytime = ytime;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
}
