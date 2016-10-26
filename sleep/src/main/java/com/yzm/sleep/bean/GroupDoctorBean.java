package com.yzm.sleep.bean;

import java.io.Serializable;

/**
 * 获取小组信息  小组医生信息类
 * @author EnidHO 2015-07-29
 * @see GroupMessageBean
 */
public class GroupDoctorBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String doctor_uid;//  小组医生用户id
	private String doctor_profile;//  小组医生用户头像
	private String doctor_profile_key;//   小组医生用户头像key
	private String i_username;
	
	public String getI_username() {
		return i_username;
	}
	public void setI_username(String i_username) {
		this.i_username = i_username;
	}
	public String getDoctor_uid() {
		return doctor_uid;
	}
	public void setDoctor_uid(String doctor_uid) {
		this.doctor_uid = doctor_uid;
	}
	public String getDoctor_profile() {
		return doctor_profile;
	}
	public void setDoctor_profile(String doctor_profile) {
		this.doctor_profile = doctor_profile;
	}
	public String getDoctor_profile_key() {
		return doctor_profile_key;
	}
	public void setDoctor_profile_key(String doctor_profile_key) {
		this.doctor_profile_key = doctor_profile_key;
	}
	
	
	
}
