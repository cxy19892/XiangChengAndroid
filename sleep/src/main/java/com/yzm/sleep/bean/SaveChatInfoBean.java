package com.yzm.sleep.bean;

import java.io.Serializable;

public class SaveChatInfoBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id = ""; //: "132897500334916208",
	private String type = ""; //: "chat",
	private String from = ""; //: "100116794",
	private String to = ""; //: "100103833",
	private String data = ""; //: "冬天手脚冰凉",
	private ExtBean ext = new ExtBean();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public ExtBean getExt() {
		return ext;
	}
	public void setExt(ExtBean ext) {
		this.ext = ext;
	}
	
	
}
