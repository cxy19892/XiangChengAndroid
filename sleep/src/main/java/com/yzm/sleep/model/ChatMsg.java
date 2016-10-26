package com.yzm.sleep.model;

import java.io.Serializable;

import com.easemob.chat.EMConversation;

public class ChatMsg implements Comparable<ChatMsg>,Serializable{

	private static final long serialVersionUID = 1L;

	private String picUrl;
	private String nickName;
	private EMConversation conversation;
	private String is_zj;
	
	
	
	public String getIs_zj() {
		return is_zj;
	}
	public void setIs_zj(String is_zj) {
		this.is_zj = is_zj;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public EMConversation getConversation() {
		return conversation;
	}
	public void setConversation(EMConversation conversation) {
		this.conversation = conversation;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	@Override
	public int compareTo(ChatMsg another) {
		// TODO Auto-generated method stub
		try {
			long thisTimeMills = this.conversation.getMessage(this.conversation.getMsgCount()).getMsgTime();
			long compareTimeMills = another.conversation.getMessage(another.conversation.getMsgCount()).getMsgTime();
			if(thisTimeMills > compareTimeMills) {
				return 1;
			} else {
				return -1;
			}	
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}
	
	
}
