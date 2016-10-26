package com.yzm.sleep.bean;

import java.io.Serializable;

public class SecretaryMsgBean implements Serializable{

	private static final long serialVersionUID = 747348885934016618L;
	
	private String msgId;
	private String title;
	private String msg;
	private String date; //long型字符串
	private int type;
	private int scan;
	private String link;
	
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getScan() {
		return scan;
	}
	public void setScan(int scan) {
		this.scan = scan;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

}
