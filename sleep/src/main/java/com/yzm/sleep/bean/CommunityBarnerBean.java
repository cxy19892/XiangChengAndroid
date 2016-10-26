package com.yzm.sleep.bean;

import java.io.Serializable;

public class CommunityBarnerBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String picture;     //封面图地址
	private String picturekey;   //封面图key
	private String title;
	private String intro;
	private String urlid;   //跳转的话题或者文章id
	private String urltype;
	
	
	public String getUrlid() {
		return urlid;
	}
	public void setUrlid(String urlid) {
		this.urlid = urlid;
	}
	public String getUrltype() {
		return urltype;
	}
	public void setUrltype(String urltype) {
		this.urltype = urltype;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
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
	
}
