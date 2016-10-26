package com.yzm.sleep.bean;

import java.io.Serializable;

public class ArticleThumbUpUserBean implements Serializable{

	private static final long serialVersionUID = -2046431730423495466L;
	private String uid;     
	private String nickname ; 
	private String author_profile;     
	private String author_profile_key;
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
