package com.yzm.sleep.bean;

import java.io.Serializable;

public class ReservationDetailBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String ygname;//        预约的医馆名字
	private String address;//        地址
	private String yname;//         预约的医生名字
	private String ytime;//        预约时间   ****年**月**日   **:**
	private String yphone;//       医生或者医馆的电话
	public String getYgname() {
		return ygname;
	}
	public void setYgname(String ygname) {
		this.ygname = ygname;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String getYphone() {
		return yphone;
	}
	public void setYphone(String yphone) {
		this.yphone = yphone;
	}
	
	

}
