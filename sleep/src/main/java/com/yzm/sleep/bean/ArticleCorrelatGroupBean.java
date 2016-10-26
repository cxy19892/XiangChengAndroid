package com.yzm.sleep.bean;

import java.io.Serializable;

public class ArticleCorrelatGroupBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String gid;
	private String bjpic;       //小组背景图片地址
	private String bjpic_key;   //小组背景图片key
	private String g_intro;     //小组介绍
	private String g_ico;
	private String g_ico_key;
	private String g_name;
	
	
	
	public String getG_ico() {
		return g_ico;
	}
	public void setG_ico(String g_ico) {
		this.g_ico = g_ico;
	}
	public String getG_ico_key() {
		return g_ico_key;
	}
	public void setG_ico_key(String g_ico_key) {
		this.g_ico_key = g_ico_key;
	}
	public String getG_name() {
		return g_name;
	}
	public void setG_name(String g_name) {
		this.g_name = g_name;
	}
	public String getBjpic() {
		return bjpic;
	}
	public void setBjpic(String bjpic) {
		this.bjpic = bjpic;
	}
	public String getBjpic_key() {
		return bjpic_key;
	}
	public void setBjpic_key(String bjpic_key) {
		this.bjpic_key = bjpic_key;
	}
	public String getG_intro() {
		return g_intro;
	}
	public void setG_intro(String g_intro) {
		this.g_intro = g_intro;
	}
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	
	
	
}
