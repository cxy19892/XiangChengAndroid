package com.yzm.sleep.bean;

import java.io.Serializable;

public class HotTagBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private String tagid;//             标签id
	private String tagname;//              标签内容；
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
}
