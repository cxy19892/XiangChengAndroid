package com.yzm.sleep.bean;

import java.io.Serializable;

public class UserDeliveryBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String realname = "";//     收货人
	private String phone = "";//   收货电话
	private String address = "";//   收货地址
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	

}
