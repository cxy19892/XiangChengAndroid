package com.yzm.sleep.bean;

import java.io.Serializable;

public class CommunityEventDetailBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String picture; 
	private String picturekey;
	private String content;
	private String over;
	
	
	public String getOver() {
		return over;
	}
	public void setOver(String over) {
		this.over = over;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getPicturekey() {
		return picturekey;
	}
	public void setPicturekey(String picturekey) {
		this.picturekey = picturekey;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	

}
