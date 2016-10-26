package com.yzm.sleep.bean;

import java.io.Serializable;

public class UserMessageBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String uid;     //用户id
	private String nickname;     //用户昵称
	private String profile;         //用户头像地址
	private String profile_key;      //用户头像地址key
	private String gender;          //用户性别
	private String is_zj;     //是否是专家，1是专家，0不是
	private String zhuanye;       //专家专业
	private String zhicheng;       //专家职业
	private String danwei;          //专家单位
	private String zjintro;          //专家介绍
	private String sleepdays;        //香橙睡眠天数
	private String askString;//医馆 预约界面输入的问题
	
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getIs_zj() {
		return is_zj;
	}
	public void setIs_zj(String is_zj) {
		this.is_zj = is_zj;
	}
	public String getZhuanye() {
		return zhuanye;
	}
	public void setZhuanye(String zhuanye) {
		this.zhuanye = zhuanye;
	}
	public String getZhicheng() {
		return zhicheng;
	}
	public void setZhicheng(String zhicheng) {
		this.zhicheng = zhicheng;
	}
	public String getDanwei() {
		return danwei;
	}
	public void setDanwei(String danwei) {
		this.danwei = danwei;
	}
	public String getZjintro() {
		return zjintro;
	}
	public void setZjintro(String zjintro) {
		this.zjintro = zjintro;
	}
	public String getSleepdays() {
		return sleepdays;
	}
	public void setSleepdays(String sleepdays) {
		this.sleepdays = sleepdays;
	}
	public String getAskString() {
		return askString;
	}
	public void setAskString(String askString) {
		this.askString = askString;
	}
	
	
	
}
