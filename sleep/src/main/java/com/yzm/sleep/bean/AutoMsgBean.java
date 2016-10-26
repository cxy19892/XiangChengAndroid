package com.yzm.sleep.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/***
 * 动态列表接口  动态消息类
 * @author Administrator
 *
 */
public class AutoMsgBean implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4327305419714774484L;
	
	private String id;//   消息id 
	private String type;//  消息类型，1为评论回复消息；2为点赞消息。
	private String tid;//    通知所属话题id
//	private String thread_pic;//    通知所属话题原图地址（对应gui中动态右边的内容）
//	private String thread_pic_key;//    通知所属话题原图地址（对应gui中动态右边的内容）key
//	private String thread_pic_thump;//    通知所属话题缩略图地址
//	private String thread_pic_thump_key;//    通知所属话题缩略图地址key
	private String thread_message;//      通知所属话题内容
	private String dateline;//           通知的发布时间
	private String notice_uid;//         评论或者点赞用户id
	private String user_profile;//       评论或者点赞用户头像
	private String user_profile_key;//    评论或者点赞用户头像key
	private String nickname;//            评论或者点赞用户名     
	private String notice;//               评论内容（如果类型是点赞通知，则notice为空）
	
	private String g_type;//  通知所属话题所属小组类型 （1为普通类；2为咨询类）
	private String t_subject;//             通知所属话题标题 ;
	private String t_author;//              通知所属话题作者名；
	private String iswenzhang;      //  1    0 
	
	/** 话题内容图片*/
	private List<ContentAttachment> images = new ArrayList<ContentAttachment>();
	
	
	
	
	public String getIswenzhang() {
		return iswenzhang;
	}
	public void setIswenzhang(String iswenzhang) {
		this.iswenzhang = iswenzhang;
	}
	public List<ContentAttachment> getImages() {
		return images;
	}
	public void setImages(List<ContentAttachment> images) {
		this.images = images;
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
	public String getT_author() {
		return t_author;
	}
	public void setT_author(String t_author) {
		this.t_author = t_author;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getThread_message() {
		return thread_message;
	}
	public void setThread_message(String thread_message) {
		this.thread_message = thread_message;
	}
	public String getDateline() {
		return dateline;
	}
	public void setDateline(String dateline) {
		this.dateline = dateline;
	}
	public String getNotice_uid() {
		return notice_uid;
	}
	public void setNotice_uid(String notice_uid) {
		this.notice_uid = notice_uid;
	}
	public String getUser_profile() {
		return user_profile;
	}
	public void setUser_profile(String user_profile) {
		this.user_profile = user_profile;
	}
	public String getUser_profile_key() {
		return user_profile_key;
	}
	public void setUser_profile_key(String user_profile_key) {
		this.user_profile_key = user_profile_key;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
	
}
