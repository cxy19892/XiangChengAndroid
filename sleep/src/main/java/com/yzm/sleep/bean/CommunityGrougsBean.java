package com.yzm.sleep.bean;

import java.io.Serializable;

/**
 * 我的话题列表按小组,我的小组推荐列表   小组类
 * @author Administrator
 *
 */
public class CommunityGrougsBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	//server=====================
	
	/**小组id*/
	private String gid;
	private String uid;
	/**小组名称*/
	private String g_name;
	/**更新的话题数量*/
	private String num;
	/**小组标志图片地址*/
	private String g_ico;
	private String g_ico_key;
	/**对应小组的话题 , 最多显示两条话题*/
	/**话题数量*/
	private String g_threads;
	/**小组封面图片地址*/
	private String g_thumb;
	/**小组封面图片Key*/
	private String g_thumb_key;
	/**小组类型*/
	private String g_type;
	
	private String is_create;
	
	/**
	 * 能发文章的权限 1 可以 0不可以
	 */
	private String flag;
	
	
	private String g_intro;//           小组介绍
	private String focus;//   登录用户是否关注（1关注；0没关注）.  未登录用户focus=0.
	private String g_focus_num ;//  话题关注人数
	
	
	public String getIs_create() {
		return is_create;
	}
	public void setIs_create(String is_create) {
		this.is_create = is_create;
	}
	public String getG_ico_key() {
		return g_ico_key;
	}
	public void setG_ico_key(String g_ico_key) {
		this.g_ico_key = g_ico_key;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getG_focus_num() {
		return g_focus_num;
	}
	public void setG_focus_num(String g_focus_num) {
		this.g_focus_num = g_focus_num;
	}
	public String getG_intro() {
		return g_intro;
	}
	public void setG_intro(String g_intro) {
		this.g_intro = g_intro;
	}
	public String getFocus() {
		return focus;
	}
	public void setFocus(String focus) {
		this.focus = focus;
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
	public String getG_type() {
		return g_type;
	}
	public void setG_type(String g_type) {
		this.g_type = g_type;
	}
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
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getG_ico() {
		return g_ico;
	}
	public void setG_ico(String g_ico) {
		this.g_ico = g_ico;
	}
	public String getG_threads() {
		return g_threads;
	}
	public void setG_threads(String g_threads) {
		this.g_threads = g_threads;
	}
	public String getG_thumb() {
		return g_thumb;
	}
	public void setG_thumb(String g_thumb) {
		this.g_thumb = g_thumb;
	}

	
}
