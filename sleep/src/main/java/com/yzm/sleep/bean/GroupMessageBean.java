package com.yzm.sleep.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取小组信息  小组信息类
 * @author EnidHO 2015-07-29
 *
 */
public class GroupMessageBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String is_create;//  是否是创建者（1是创建者；0不是） 
	private String is_member;//是否是小组成员   针对资讯类
	private String g_name;//   小组名称
	private String uid;//       小组创建者用户id
	private String grouper;//  小组创建者用户名
	private String g_intro;//    小组简介
	private List<HotTagBean> g_tag;//     小组标签
	private String newthread;//  今日新增话题
	private String g_threads;//    总话题
	private String gid;//小组id
	private String focus;//
	private String g_ico;//小组图标
	private String g_ico_key;//    小组标志图片地址key
	private String g_focus_num;//话题关注人数
	private String g_fangwen_num;//话题访问人数
	private List<GroupDoctorBean> doctor_list = new ArrayList<GroupDoctorBean>();//   小组医生列表，咨询类小组才有。
	private List<SpecialistBean> zj_list = new ArrayList<SpecialistBean>();//   专家列表
	
	
	
	
	public String getG_fangwen_num() {
		return g_fangwen_num;
	}
	public void setG_fangwen_num(String g_fangwen_num) {
		this.g_fangwen_num = g_fangwen_num;
	}
	public List<SpecialistBean> getZj_list() {
		return zj_list;
	}
	public void setZj_list(List<SpecialistBean> zj_list) {
		this.zj_list = zj_list;
	}
	public String getG_focus_num() {
		return g_focus_num;
	}
	public void setG_focus_num(String g_focus_num) {
		this.g_focus_num = g_focus_num;
	}
//	public String getFlag() {
//		return flag;
//	}
//	public void setFlag(String flag) {
//		this.flag = flag;
//	}
	public String getG_name() {
		return g_name;
	}
	public void setG_name(String g_name) {
		this.g_name = g_name;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getGrouper() {
		return grouper;
	}
	public void setGrouper(String grouper) {
		this.grouper = grouper;
	}
	public String getG_intro() {
		return g_intro;
	}
	public void setG_intro(String g_intro) {
		this.g_intro = g_intro;
	}
	
	public List<HotTagBean> getG_tag() {
		return g_tag;
	}
	public void setG_tag(List<HotTagBean> g_tag) {
		this.g_tag = g_tag;
	}
	public String getNewthread() {
		return newthread;
	}
	public void setNewthread(String newthread) {
		this.newthread = newthread;
	}
	public String getG_threads() {
		return g_threads;
	}
	public void setG_threads(String g_threads) {
		this.g_threads = g_threads;
	}
	public List<GroupDoctorBean> getDoctor_list() {
		return doctor_list;
	}
	public void setDoctor_list(List<GroupDoctorBean> doctor_list) {
		this.doctor_list = doctor_list;
	}
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public String getFocus() {
		return focus;
	}
	public void setFocus(String focus) {
		this.focus = focus;
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
	public String getIs_member() {
		return is_member;
	}
	public void setIs_member(String is_member) {
		this.is_member = is_member;
	}
	
	
	
}
