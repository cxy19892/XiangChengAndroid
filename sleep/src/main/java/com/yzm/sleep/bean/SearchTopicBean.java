package com.yzm.sleep.bean;

import java.io.Serializable;

/**
 * 搜索小组话题   话题类
 * @author EnidHO 2015-07-29
 *
 */
public class SearchTopicBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String tid;//  话题id
	private String g_type;//话题所属小组的类型
	private String t_subject;//   话题标题
	private String t_message;
	
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getG_type() {
		return g_type;
	}
	public void setG_type(String g_type) {
		this.g_type = g_type;
	}
	public String getT_subject() {
		return t_subject;
	}
	public void setT_subject(String t_subject) {
		this.t_subject = t_subject;
	}
	public String getT_message() {
		return t_message;
	}
	public void setT_message(String t_message) {
		this.t_message = t_message;
	}
	
	
	
}
