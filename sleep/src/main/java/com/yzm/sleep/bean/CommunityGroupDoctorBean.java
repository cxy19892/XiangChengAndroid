package com.yzm.sleep.bean;

import java.io.Serializable;

/**
 * 小组医生列表 小组医生信息类
 * @author EnidHO 2015-07-29
 * 
 */
public class CommunityGroupDoctorBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;  //  医生列表唯一id
	private String uid;//   医生用户id
	private String profile;//    医生用户头像
	private String profile_key;//   医生用户头像key
	private String i_username;//    医生名字
	private String i_telephone;//    关注用户电话
	private String i_state;//    状态（1为正常；4为已邀请）
	private String is_zj;
	
	public String getIs_zj() {
		return is_zj;
	}
	public void setIs_zj(String is_zj) {
		this.is_zj = is_zj;
	}
	private boolean isselect = false;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
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
	public String getI_username() {
		return i_username;
	}
	public void setI_username(String i_username) {
		this.i_username = i_username;
	}
	public String getI_telephone() {
		return i_telephone;
	}
	public void setI_telephone(String i_telephone) {
		this.i_telephone = i_telephone;
	}
	public String getI_state() {
		return i_state;
	}
	public void setI_state(String i_state) {
		this.i_state = i_state;
	}
	public boolean isIsselect() {
		return isselect;
	}
	public void setIsselect(boolean isselect) {
		this.isselect = isselect;
	}
	
	
}
