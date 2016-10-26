package com.yzm.sleep.bean;

import java.io.Serializable;

public class TaocanBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private String tcid = "";//   套餐id
	private String picture = "";//   套餐封面图
	private String picture_key = "";//    图片key
	private String title = "";//       套餐标题
	private String tcsalenum = "0";//       已经销售数量
	private String tc_status;
	private String kefu;
	
	public String getTc_status() {
		return tc_status;
	}
	public void setTc_status(String tc_status) {
		this.tc_status = tc_status;
	}
	public String getTcid() {
		return tcid;
	}
	public void setTcid(String tcid) {
		this.tcid = tcid;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getPicture_key() {
		return picture_key;
	}
	public void setPicture_key(String picture_key) {
		this.picture_key = picture_key;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTcsalenum() {
		return tcsalenum;
	}
	public void setTcsalenum(String tcsalenum) {
		this.tcsalenum = tcsalenum;
	}
	public String getKefu() {
		return kefu;
	}
	public void setKefu(String kefu) {
		this.kefu = kefu;
	}
	
	
	

}
