package com.yzm.sleep.bean;

import java.io.Serializable;

public class EnvironmentBean implements Serializable {
	
	private String suggest;       //专家建议
	private String title;   //标题
	private String picture;       // 图片（服务器图片）
	private String showSuggest; //是否显示意见 1显示; 其他不显示;
	private String flag;       //(1达标；0不达标);
	
	public String getSuggest() {
		return suggest;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}
	
	public String getPicture() {
		return picture;
	}
	
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public String getShowSuggest() {
		return showSuggest;
	}
	
	public void setShowSuggest(String showSuggest) {
		this.showSuggest = showSuggest;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
}
