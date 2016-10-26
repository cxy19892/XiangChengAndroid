package com.yzm.sleep.bean;

import java.io.Serializable;

public class UserInfoPhotoBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String uid;   
	private String pid;       
	private String attachment;            //照片图片地址  
	private String attachment_key;        // 图片地址key
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getAttachment_key() {
		return attachment_key;
	}
	public void setAttachment_key(String attachment_key) {
		this.attachment_key = attachment_key;
	}
	
	
}
