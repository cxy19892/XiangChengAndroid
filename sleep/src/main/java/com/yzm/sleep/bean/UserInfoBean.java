package com.yzm.sleep.bean;

import java.io.Serializable;

public class UserInfoBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String int_id;       //用户id
	private String isaddinfo;    //是否需要完善信息。1需要；0不需要    
	private String nickname;     //用户昵称
	private String age;          //用户生日
	private String gender;       //用户性别  
	private String occupation;   //用户职业
	private String profile;      //用户头像
	private String profile_key;   // 用户头像key
	private String hiding;    
	private String user_ext_acc_qq;
	private String user_ext_acc_weibo; 
	private String user_ext_acc_wechat;
	private String user_ext_acc_cellphone;
	private String height;
	private String weight;
	private String ispinggu;
	private String dakadays;
	private String is_zj;
	private String jgtssz;//开启或关闭极光推送  1-开启，2-关闭
	private String sleep_pg; // 是否完成睡眠评估 1表示参与过；0没有
	private String sleep;//计划入睡时间点
	private String wakeup;//计划起床时间点

	public String getSleep() {
		return sleep;
	}

	public void setSleep(String sleep) {
		this.sleep = sleep;
	}

	public String getWakeup() {
		return wakeup;
	}

	public void setWakeup(String wakeup) {
		this.wakeup = wakeup;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getInt_id() {
		return int_id;
	}
	
	public void setInt_id(String int_id) {
		this.int_id = int_id;
	}
	public String getIsaddinfo() {
		return isaddinfo;
	}
	public void setIsaddinfo(String isaddinfo) {
		this.isaddinfo = isaddinfo;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getProfile_key() {
		return profile_key;
	}
	public void setProfile_key(String profile_key) {
		this.profile_key = profile_key;
	}
	public String getHiding() {
		return hiding;
	}
	public void setHiding(String hiding) {
		this.hiding = hiding;
	}
	public String getUser_ext_acc_qq() {
		return user_ext_acc_qq;
	}
	public void setUser_ext_acc_qq(String user_ext_acc_qq) {
		this.user_ext_acc_qq = user_ext_acc_qq;
	}
	public String getUser_ext_acc_weibo() {
		return user_ext_acc_weibo;
	}
	public void setUser_ext_acc_weibo(String user_ext_acc_weibo) {
		this.user_ext_acc_weibo = user_ext_acc_weibo;
	}
	public String getUser_ext_acc_wechat() {
		return user_ext_acc_wechat;
	}
	public void setUser_ext_acc_wechat(String user_ext_acc_wechat) {
		this.user_ext_acc_wechat = user_ext_acc_wechat;
	}
	public String getUser_ext_acc_cellphone() {
		return user_ext_acc_cellphone;
	}
	public void setUser_ext_acc_cellphone(String user_ext_acc_cellphone) {
		this.user_ext_acc_cellphone = user_ext_acc_cellphone;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getIspinggu() {
		return ispinggu;
	}

	public void setIspinggu(String ispinggu) {
		this.ispinggu = ispinggu;
	}

	public String getDakadays() {
		return dakadays;
	}

	public void setDakadays(String dakadays) {
		this.dakadays = dakadays;
	}

	public String getIs_zj() {
		return is_zj;
	}

	public void setIs_zj(String is_zj) {
		this.is_zj = is_zj;
	}

	public String getJgtssz() {
		return jgtssz;
	}

	public void setJgtssz(String jgtssz) {
		this.jgtssz = jgtssz;
	}

	public String getSleep_pg() {
		return sleep_pg;
	}

	public void setSleep_pg(String sleep_pg) {
		this.sleep_pg = sleep_pg;
	}
	
	
}
