package com.yzm.sleep.bean;

import java.io.Serializable;

public class PlanListBean implements Serializable {

	private String dateline;//      日期
	private float xiaolv;//        效率
	private int flag;
	public String getDateline() {
		return dateline;
	}
	public void setDateline(String dateline) {
		this.dateline = dateline;
	}
	public float getXiaolv() {
		return xiaolv;
	}
	public void setXiaolv(float xiaolv) {
		this.xiaolv = xiaolv;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
}
