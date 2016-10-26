package com.yzm.sleep.bean;

import java.io.Serializable;

public class ThumbUpUserBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String uid;                    //用户id ;
	private String name;                  //用户名字；
	private String z_dateline;              //点赞时间； 
	private String author_profile;           //用户头像 ；
	private String author_profile_key;       //用户头像key；
	private String is_zj;//是否是专家     是否是专家。1是；0不是
	
	
	public String getIs_zj() {
		return is_zj;
	}
	public void setIs_zj(String is_zj) {
		this.is_zj = is_zj;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getZ_dateline() {
		return z_dateline;
	}
	public void setZ_dateline(String z_dateline) {
		this.z_dateline = z_dateline;
	}
	public String getAuthor_profile() {
		return author_profile;
	}
	public void setAuthor_profile(String author_profile) {
		this.author_profile = author_profile;
	}
	public String getAuthor_profile_key() {
		return author_profile_key;
	}
	public void setAuthor_profile_key(String author_profile_key) {
		this.author_profile_key = author_profile_key;
	}
	
	

}
