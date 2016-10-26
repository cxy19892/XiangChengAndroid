package com.yzm.sleep.bean;

import java.io.Serializable;

public class FirstRecommentGroupBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private String gid;//              小组id
	private String g_ico;//          小组标志图片
	private String g_ico_key;//        小组标志图片地址key；
	private String is_create;//  是否是创建者（1表示登录用户是创建者；0不是） 
	private String g_name;//          小组名称；
	private String g_intro;//          小组介绍
	private String g_thumb;//        小组封面图片地址
	private String g_thumb_key;//     小组封面图片key   
	private String flag;//      是否能够发布话题（1可以，0不可以）,未登录用户flag = 0.  
	private String focus;//   登录用户是否关注（1关注；0没关注）.未登录用户focus=0.    
	private String g_focus_num;//   小组关注人数
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
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
	public String getIs_create() {
		return is_create;
	}
	public void setIs_create(String is_create) {
		this.is_create = is_create;
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
	public String getG_thumb() {
		return g_thumb;
	}
	public void setG_thumb(String g_thumb) {
		this.g_thumb = g_thumb;
	}
	public String getG_thumb_key() {
		return g_thumb_key;
	}
	public void setG_thumb_key(String g_thumb_key) {
		this.g_thumb_key = g_thumb_key;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getFocus() {
		return focus;
	}
	public void setFocus(String focus) {
		this.focus = focus;
	}
	public String getG_focus_num() {
		return g_focus_num;
	}
	public void setG_focus_num(String g_focus_num) {
		this.g_focus_num = g_focus_num;
	}
	
	
	
	
}
