package com.yzm.sleep.bean;

import java.io.Serializable;

public class CommunityGroupBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private String gid;   //小组id
	private String uid;     //作者id
	private String is_zj;    //是否是专家。1是；0不是
	private String g_name;    //小组名字
	private String g_intro;    //小组介绍
	private String g_ico;      //小组头像地址
	private String g_ico_key;    //小组头像key
	private String g_threads;     //小组话题数
	private String g_focus_num;    //小组关注人数
	private String g_thumb_key;     //小组背景图片key
	private String g_thumb;        //小组背景图片地址
	private String title;         //标题
	private boolean iscode;
	private String flag;
	private String g_type;
	
	
	public boolean isIscode() {
		return iscode;
	}
	public void setIscode(boolean iscode) {
		this.iscode = iscode;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getIs_zj() {
		return is_zj;
	}
	public void setIs_zj(String is_zj) {
		this.is_zj = is_zj;
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
	public String getG_threads() {
		return g_threads;
	}
	public void setG_threads(String g_threads) {
		this.g_threads = g_threads;
	}
	public String getG_focus_num() {
		return g_focus_num;
	}
	public void setG_focus_num(String g_focus_num) {
		this.g_focus_num = g_focus_num;
	}
	public String getG_thumb_key() {
		return g_thumb_key;
	}
	public void setG_thumb_key(String g_thumb_key) {
		this.g_thumb_key = g_thumb_key;
	}
	public String getG_thumb() {
		return g_thumb;
	}
	public void setG_thumb(String g_thumb) {
		this.g_thumb = g_thumb;
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
