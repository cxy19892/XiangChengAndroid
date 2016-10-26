package com.yzm.sleep.bean;

import java.io.Serializable;

public class PeopleSinaInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String nickname;
	private String profile;
	private String profileKey;
	private String my_int_id;
	private String relationshipType;
	
	
	
	public String getMy_int_id() {
		return my_int_id;
	}
	public void setMy_int_id(String my_int_id) {
		this.my_int_id = my_int_id;
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
	
	public String getProfileKey() {
		return profileKey;
	}
	public void setProfileKey(String profileKey) {
		this.profileKey = profileKey;
	}
	public String getRelationshipType() {
		return relationshipType;
	}
	public void setRelationshipType(String relationshipType) {
		this.relationshipType = relationshipType;
	}
	
}
