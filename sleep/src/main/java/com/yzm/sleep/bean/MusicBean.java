package com.yzm.sleep.bean;

import java.io.Serializable;

public class MusicBean implements Serializable {

	private String id;//,
	private String title; //"Ocean Deep",
	private String intro; //"有助于舒缓紧张的情绪",
	private String url; //http://7xq4sz.com2.z0.glb.qiniucdn.com/EVNOiR_LmQlSJYpfeOpc1XiGLvY=/lr7JCvYE9LavjSq8trWc1BmCPnXO",
	private String flag; //"1",
	private String dateline; //"1"
	
	private String pic;//       图片地址
	private String pickey;//    图片key
	
	private String real_url;//    下载地址
	
	
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getPickey() {
		return pickey;
	}
	public void setPickey(String pickey) {
		this.pickey = pickey;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getDateline() {
		return dateline;
	}
	public void setDateline(String dateline) {
		this.dateline = dateline;
	}
	public String getReal_url() {
		return real_url;
	}
	public void setReal_url(String real_url) {
		this.real_url = real_url;
	}
	
	
}
