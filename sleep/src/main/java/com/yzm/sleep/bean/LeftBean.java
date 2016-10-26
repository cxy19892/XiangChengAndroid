package com.yzm.sleep.bean;

import java.io.Serializable;

public class LeftBean implements Serializable {
	
	private String id;
	private String title;
	private String pic;
	private String pic_key;
	
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
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getPic_key() {
		return pic_key;
	}
	public void setPic_key(String pic_key) {
		this.pic_key = pic_key;
	}
	
	
	
}
