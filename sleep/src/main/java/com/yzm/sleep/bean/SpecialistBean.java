package com.yzm.sleep.bean;

import java.io.Serializable;

public class SpecialistBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
    private String uid;
	private String nickname;
	private String profile;
	private String profile_key;
	private String i_username;
	private String is_zj;
	private String id; 
	private String i_state;
	private String i_telephone;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getI_state() {
		return i_state;
	}
	public void setI_state(String i_state) {
		this.i_state = i_state;
	}
	public String getI_telephone() {
		return i_telephone;
	}
	public void setI_telephone(String i_telephone) {
		this.i_telephone = i_telephone;
	}
	public String getIs_zj() {
		return is_zj;
	}
	public void setIs_zj(String is_zj) {
		this.is_zj = is_zj;
	}
	public String getI_username() {
		return i_username;
	}
	public void setI_username(String i_username) {
		this.i_username = i_username;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
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
	
	

}
