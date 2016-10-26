package com.yzm.sleep.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取话题列表、话题详情      评论类
 * @author EnidHO 2015-07-29
 * @see CommunityTopicBean
 *
 */
public class TopicCommentBean implements Serializable{
	private static final long serialVersionUID = 1L;
	/**评论id*/
	private String pid ;
	/**评论用户id*/
	private String p_uid;
	/**评论用户名*/
	private String p_author;
	/**评论内容*/
	private String p_message;
	/**被回复用户id*/
	private String p_touid;
	/**被回复用户名*/
	private String p_toauthor;
	
	//话题详情 评论 多加一下参数
	/**评论时间 */
	private String p_dateline;
	/**评论用户头像*/
	private String plprofile;
	/**评论用户头像key*/
	private String plprofile_key;
	/**评论总页数*/
	private String totalpage;  
	/**该评论下的回复列表*/
	private List<ReplyCommentBean> reply_list = new ArrayList<ReplyCommentBean>();
	
	private String is_zj;
	
	public String getIs_zj() {
		return is_zj;
	}
	public void setIs_zj(String is_zj) {
		this.is_zj = is_zj;
	}
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
	public String getTotalpage() {
		return totalpage;
	}
	public void setTotalpage(String totalpage) {
		this.totalpage = totalpage;
	}
	public List<ReplyCommentBean> getReply_list() {
		return reply_list;
	}
	public void setReply_list(List<ReplyCommentBean> reply_list) {
		this.reply_list = reply_list;
	}
	
	
	
}
