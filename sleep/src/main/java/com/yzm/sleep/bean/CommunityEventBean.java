package com.yzm.sleep.bean;

import java.io.Serializable;

public class CommunityEventBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;         //活动id
	private String picturekey; //图片key
	private String picture;    //活动图片地址
	private String title;      //活动标题
	private String numbers;    //活动参与人数
	private String urltype;    //type为2时，urltype为跳转类型：1表示跳转到文章详情;2跳转到话题详情
	private String type;       //1为线下活动（以前的）； 2为线上活动（跳转到文章和话题）
	private String urlid;      //type为2时，urlid为跳转到文章或者话题的ID
	private boolean loaded;
	
	
	
	public boolean isLoaded() {
		return loaded;
	}
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPicturekey() {
		return picturekey;
	}
	public void setPicturekey(String picturekey) {
		this.picturekey = picturekey;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getNumbers() {
		return numbers;
	}
	public void setNumbers(String numbers) {
		this.numbers = numbers;
	}
	public String getUrltype() {
		return urltype;
	}
	public void setUrltype(String urltype) {
		this.urltype = urltype;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrlid() {
		return urlid;
	}
	public void setUrlid(String urlid) {
		this.urlid = urlid;
	}
	
}
