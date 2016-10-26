package com.yzm.sleep.bean;

import java.io.Serializable;

public class DoctorBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private String zjuid;//        专家id
	private String profile;//         专家头像地址
	private String profile_key;//      专家头像地址key
	private String name;//       名字
	private String zhicheng;//    职称
	private String intro;//          介绍
	private String danwei;//       单位
	private String address;//       地址
	private String phone;//        电话
	private String zhuanye;//       专业
	private String location_y;
	private String location_x;
	
	
	private boolean isMore = false;
	public String getZjuid() {
		return zjuid;
	}
	public void setZjuid(String zjuid) {
		this.zjuid = zjuid;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getProfile_key() {
		return profile_key;
	}
	public void setProfile_key(String profile_key) {
		this.profile_key = profile_key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getZhicheng() {
		return zhicheng;
	}
	public void setZhicheng(String zhicheng) {
		this.zhicheng = zhicheng;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getDanwei() {
		return danwei;
	}
	public void setDanwei(String danwei) {
		this.danwei = danwei;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getZhuanye() {
		return zhuanye;
	}
	public void setZhuanye(String zhuanye) {
		this.zhuanye = zhuanye;
	}
	public boolean isMore() {
		return isMore;
	}
	public void setMore(boolean isMore) {
		this.isMore = isMore;
	}
	public String getLocation_y() {
		return location_y;
	}
	public void setLocation_y(String location_y) {
		this.location_y = location_y;
	}
	public String getLocation_x() {
		return location_x;
	}
	public void setLocation_x(String location_x) {
		this.location_x = location_x;
	}
	
	
	
}
