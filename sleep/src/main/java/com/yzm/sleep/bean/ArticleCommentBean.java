package com.yzm.sleep.bean;

import java.io.Serializable;

public class ArticleCommentBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String pid; //评论id  ;  
	private String p_uid;      //评论用户id ；
	private String p_author;   //评论用户名； 
	private String p_dateline;    ///评论时间；
	private String is_zj;   //是否是专家；1是专家，0不是专家
	private String p_message;   //评论内容 ；
	private String p_touid;       //被回复用户id；
	private String p_toauthor;   //被回复用户名； 
	private String touxiang; //评论用户 头像 url
	private String touxiang_key; //评论用户 头像 key
	
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getP_uid() {
		return p_uid;
	}
	public void setP_uid(String p_uid) {
		this.p_uid = p_uid;
	}
	public String getP_author() {
		return p_author;
	}
	public void setP_author(String p_author) {
		this.p_author = p_author;
	}
	public String getP_dateline() {
		return p_dateline;
	}
	public void setP_dateline(String p_dateline) {
		this.p_dateline = p_dateline;
	}
	public String getIs_zj() {
		return is_zj;
	}
	public void setIs_zj(String is_zj) {
		this.is_zj = is_zj;
	}
	public String getP_message() {
		return p_message;
	}
	public void setP_message(String p_message) {
		this.p_message = p_message;
	}
	public String getP_touid() {
		return p_touid;
	}
	public void setP_touid(String p_touid) {
		this.p_touid = p_touid;
	}
	public String getP_toauthor() {
		return p_toauthor;
	}
	public void setP_toauthor(String p_toauthor) {
		this.p_toauthor = p_toauthor;
	}
	public String getTouxiang() {
		return touxiang;
	}
	public void setTouxiang(String touxiang) {
		this.touxiang = touxiang;
	}
	public String getTouxiang_key() {
		return touxiang_key;
	}
	public void setTouxiang_key(String touxiang_key) {
		this.touxiang_key = touxiang_key;
	}
	
	

}
