package com.yzm.sleep.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 获取话题列表、话题详情、发现、我的话题     话题类
 * @author EnidHO 2015-07-29
 * 
 * @see 
 *
 */
public class CommunityTopicBean implements Serializable{
	private static final long serialVersionUID = 1L;
	/** 所属小组类型 （1为普通类；2为咨询类）*/
	private String type = "1";
	private String t_type;
	/** 话题id */
	private String tid;
	/** 小组id */
	private String gid;
	private String g_ico;
	private String g_ico_key;
	private String flag;
	/** 话题标题 */
	private String t_subject;
	/** 话题作者名*/
	private String t_author;
	/** 话题作者id   */
	private String uid;
	/** 话题发布时间 */
	private String t_dateline;
	/** 话题作者头像 */
	private String author_profile;
	/** 话题作者头像key*/
	private String author_profile_key;
	/** 话题内容 */
	private String t_message;
	/** 话题内容图片*/
	private List<ContentAttachment> images = new ArrayList<ContentAttachment>();
	/** 话题回复数 */
	private String t_replies;
	/** 话题点赞数量*/
	private String t_zans;
	/** 当前登录用户是否对话题点赞（1已经点赞；0表示没有点赞）*/
	private String status;
	/** 评论列表,这里只取最近的三条评论*/
	private List<TopicCommentBean> pl_list= new ArrayList<TopicCommentBean>();
	/**小组名称*/
	private String g_name;
	
	private int focus;
	/**小组封面图片地址*/
	private String g_thumb;
	/**小组封面图片地址*/
	private String g_thumb_key;
	private String user_internal_nickname;
	/**是否展开全文*/
	private boolean isshow;
	private String t_attachment_num;//话题图片数量
	/**话题所属小组创建者用户id*/
	private String groupcreateuid;
	/**当前登录用户对话题的操作权限 (-1表示没有任何权限； 1 删除话题权限 ； 2 置顶话题权限 ； 3 举报话题权限)*/
	private String quanxian;
	//是否是医生
	private String is_zj;
	
	
	
	public String getIs_zj() {
		return is_zj;
	}
	public void setIs_zj(String is_zj) {
		this.is_zj = is_zj;
	}
	public String getQuanxian() {
		return quanxian;
	}
	public void setQuanxian(String quanxian) {
		this.quanxian = quanxian;
	}
	public String getGroupcreateuid() {
		return groupcreateuid;
	}
	public void setGroupcreateuid(String groupcreateuid) {
		this.groupcreateuid = groupcreateuid;
		
		
		
		
	}
	public String getT_attachment_num() {
		return t_attachment_num;
	}
	public void setT_attachment_num(String t_attachment_num) {
		this.t_attachment_num = t_attachment_num;
	}
	public String getG_ico_key() {
		return g_ico_key;
	}
	public void setG_ico_key(String g_ico_key) {
		this.g_ico_key = g_ico_key;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getG_ico() {
		return g_ico;
	}
	public void setG_ico(String g_ico) {
		this.g_ico = g_ico;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
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
	public String getUser_internal_nickname() {
		return user_internal_nickname;
	}
	public void setUser_internal_nickname(String user_internal_nickname) {
		this.user_internal_nickname = user_internal_nickname;
	}
	public boolean isIsshow() {
		return isshow;
	}
	public void setIsshow(boolean isshow) {
		this.isshow = isshow;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getT_type() {
		return t_type;
	}
	public void setT_type(String t_type) {
		this.t_type = t_type;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
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
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getT_dateline() {
		return t_dateline;
	}
	public void setT_dateline(String t_dateline) {
		this.t_dateline = t_dateline;
	}
	public String getAuthor_profile() {
		return author_profile;
	}
	public void setAuthor_profile(String author_profile) {
		this.author_profile = author_profile;
	}
	public String getAuthor_profile_key() {
		return author_profile_key;
	}
	public void setAuthor_profile_key(String author_profile_key) {
		this.author_profile_key = author_profile_key;
	}
	public String getT_message() {
		return t_message;
	}
	public void setT_message(String t_message) {
		this.t_message = t_message;
	}
	public String getT_replies() {
		return t_replies;
	}
	public void setT_replies(String t_replies) {
		this.t_replies = t_replies;
	}
	public String getT_zans() {
		return t_zans;
	}
	public void setT_zans(String t_zans) {
		this.t_zans = t_zans;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<TopicCommentBean> getPl_list() {
		return pl_list;
	}
	public void setPl_list(List<TopicCommentBean> pl_list) {
		this.pl_list = pl_list;
	}
	public int getFocus() {
		return focus;
	}
	public void setFocus(int focus) {
		this.focus = focus;
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
	public List<ContentAttachment> getImages() {
		return images;
	}
	public void setImages(List<ContentAttachment> images) {
		this.images = images;
	}
	
	
	
	
}
