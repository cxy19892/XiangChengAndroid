package com.yzm.sleep.bean;

import java.io.Serializable;

public class PushClasifyBean implements Serializable{
	
	private String tagid;
	private String tagname;
	private String tagpicture;
	private String tagpicture_key;
	public String getTagid() {
		return tagid;
	}
	public void setTagid(String tagid) {
		this.tagid = tagid;
	}
	public String getTagname() {
		return tagname;
	}
	public void setTagname(String tagname) {
		this.tagname = tagname;
	}
	public String getTagpicture() {
		return tagpicture;
	}
	public void setTagpicture(String tagpicture) {
		this.tagpicture = tagpicture;
	}
	public String getTagpicture_key() {
		return tagpicture_key;
	}
	public void setTagpicture_key(String tagpicture_key) {
		this.tagpicture_key = tagpicture_key;
	}

	
	
}
