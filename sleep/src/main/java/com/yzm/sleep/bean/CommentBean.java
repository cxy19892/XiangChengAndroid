package com.yzm.sleep.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 评论列表接口    评论类
 * @author Administrator
 *
 */
public class CommentBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String pid ;//评论id  ;  
	private String uid;//   评论用户id ；
	private String p_author;// 评论用户名； 
	private String p_message;// 评论内容 ；
	private String p_dateline;// 评论时间 ； 
	private String plprofile;// 评论用户头像 ；
	private String plprofile_key;//  评论用户头像key  ; 
	private String is_zj;
	
	
	public String getIs_zj() {
		return is_zj;
	}
	public void setIs_zj(String is_zj) {
		this.is_zj = is_zj;
	}
	/**
	 * 评论用户名
	 */
	private String user_internal_nickname;
	
	private List<ReplyCommentBean> reply_list = new ArrayList<ReplyCommentBean>();//  该评论下的回复列表 ;
	
	
	public String getUser_internal_nickname() {
		return user_internal_nickname;
	}
	public void setUser_internal_nickname(String user_internal_nickname) {
		this.user_internal_nickname = user_internal_nickname;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
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
	public String getP_dateline() {
		return p_dateline;
	}
	public void setP_dateline(String p_dateline) {
		this.p_dateline = p_dateline;
	}
	public String getPlprofile() {
		return plprofile;
	}
	public void setPlprofile(String plprofile) {
		this.plprofile = plprofile;
	}
	public String getPlprofile_key() {
		return plprofile_key;
	}
	public void setPlprofile_key(String plprofile_key) {
		this.plprofile_key = plprofile_key;
	}
	public List<ReplyCommentBean> getReply_list() {
		return reply_list;
	}
	public void setReply_list(List<ReplyCommentBean> reply_list) {
		this.reply_list = reply_list;
	}
	
	
}
