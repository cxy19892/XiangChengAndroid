package com.yzm.sleep.bean;

import java.io.Serializable;

public class ClinicBean implements Serializable{
	private static final long serialVersionUID = 1L;

	private String ygid;//     医馆id
	private String name;//     名字
	private String address;//    地址
	private String phone;//     电话
	private String picture;//     背景图片url地址
	private String picture_key;//    图片七牛key
	private String kefuuid;//       医馆的客服uid
	private String location_x;
	private String location_y;
	private String juli;
	public String getYgid() {
		return ygid;
	}
	public void setYgid(String ygid) {
		this.ygid = ygid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getKefuuid() {
		return kefuuid;
	}
	public void setKefuuid(String kefuuid) {
		this.kefuuid = kefuuid;
	}
	public String getLocation_x() {
		return location_x;
	}
	public void setLocation_x(String location_x) {
		this.location_x = location_x;
	}
	public String getLocation_y() {
		return location_y;
	}
	public void setLocation_y(String location_y) {
		this.location_y = location_y;
	}
	public String getJuli() {
		return juli;
	}
	public void setJuli(String juli) {
		this.juli = juli;
	}
	
	
}
