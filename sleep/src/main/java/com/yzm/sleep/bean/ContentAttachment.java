package com.yzm.sleep.bean;

import java.io.Serializable;

public class ContentAttachment implements Serializable{

	private static final long serialVersionUID = 1L;
	private String content_attachment;// 话题内容图片地址
	private String t_attachment_key;//话题内容图片key
	private String content_attachment_sl;//话题内容图片缩略图地址
	private String t_attachment_key_sl;//话题内容图片缩略图key
	
	
	
	public String getContent_attachment() {
		return content_attachment;
	}
	public void setContent_attachment(String content_attachment) {
		this.content_attachment = content_attachment;
	}
	public String getT_attachment_key() {
		return t_attachment_key;
	}
	public void setT_attachment_key(String t_attachment_key) {
		this.t_attachment_key = t_attachment_key;
	}
	public String getContent_attachment_sl() {
		return content_attachment_sl;
	}
	public void setContent_attachment_sl(String content_attachment_sl) {
		this.content_attachment_sl = content_attachment_sl;
	}
	public String getT_attachment_key_sl() {
		return t_attachment_key_sl;
	}
	public void setT_attachment_key_sl(String t_attachment_key_sl) {
		this.t_attachment_key_sl = t_attachment_key_sl;
	}
	
	
}
