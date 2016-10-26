package com.yzm.sleep.bean;

public class BePushBean {
	private String type;		// 跳转类型 ( 1活动详情 ; 2话题详情  ;  3文章详情 ; 4 小组 )  
	private String tjid ;		//  跳转的ID
	private String title;		//    标题
	private String intro;		//    介绍
	private String picture;		//    图片
	private String picture_key;	//   图片key
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTjid() {
		return tjid;
	}
	public void setTjid(String tjid) {
		this.tjid = tjid;
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
	public String getPicture_key() {
		return picture_key;
	}
	public void setPicture_key(String picture_key) {
		this.picture_key = picture_key;
	}

}
