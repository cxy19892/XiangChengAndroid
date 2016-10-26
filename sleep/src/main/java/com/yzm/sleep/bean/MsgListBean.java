package com.yzm.sleep.bean;

import java.io.Serializable;

public class MsgListBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2118300749299334509L;
	private String userId;
	private String userName;
	private String userKey;
	private String userUrl;
	private String message;
	private long date;
	private int msgCount; //  未读消息数
	private String type;
	private String msgListTyp;// 1.是小秘书 其它不是
	private String kefu; //1是客服 其他不是
	
	public String getKefu() {
		return kefu;
	}
	public void setKefu(String kefu) {
		this.kefu = kefu;
	}
	public String getMsgListTyp() {
		return msgListTyp;
	}
	public void setMsgListTyp(String msgListTyp) {
		this.msgListTyp = msgListTyp;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	public String getUserUrl() {
		return userUrl;
	}
	public void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public int getMsgCount() {
		return msgCount;
	}
	public void setMsgCount(int msgCount) {
		this.msgCount = msgCount;
	}
	
}
