package com.yzm.sleep.bean;

import java.io.Serializable;

/**
 * 话题详情    回复评论类
 * @author EnidHO 2015-07-29
 * @see TopicCommentBean
 */
public class ReplyCommentBean implements Serializable{
	private static final long serialVersionUID = 1L;
	/**评论id*/
	private String pid ;
	/**回复用户id*/
	private String p_uid ;
	private String uid;
	/**回复用户名*/
	private String p_author;
	/**评论内容*/
	private String p_message;
	/**被回复用户id*/
	private String p_touid;
	/**被回复用户名*/
	private String p_toauthor;
	
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getP_uid() {
		return p_uid;
	}
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
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
	
	
	
}
