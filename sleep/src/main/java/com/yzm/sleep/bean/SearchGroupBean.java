package com.yzm.sleep.bean;

import java.io.Serializable;

/**
 * 搜索小组话题   小组类
 * @author EnidHO 2015-07-29
 *
 */
public class SearchGroupBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String gid;// 小组id ;
	private String g_name;// 小组名字 ;
	private String g_intro;// 小组简介 ;
	private String flag;
	private String g_type;
	
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public String getG_name() {
		return g_name;
	}
	public void setG_name(String g_name) {
		this.g_name = g_name;
	}
	public String getG_intro() {
		return g_intro;
	}
	public void setG_intro(String g_intro) {
		this.g_intro = g_intro;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getG_type() {
		return g_type;
	}
	public void setG_type(String g_type) {
		this.g_type = g_type;
	}
	
	
	
}
