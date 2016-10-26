package com.yzm.sleep.bean;

import java.io.Serializable;
import java.util.List;

public class ArticleDetailBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tid;              //文章id ;
	private String gid;
	private String uid;              //作者id;
	private String t_subject;        //标题 ;
	private String t_message;      //内容（html）；
	private String t_lastpost;
	private String t_lastposter;
	private String t_author;         //作者名；
	private String groupcreateuid;  //文章所属小组创建者用户id
	private String t_dateline;          //发布时间； 
	private String author_profile;       //作者头像 ；
	private String author_profile_key;   //作者头像key； 
	private String t_view;      //浏览数
	private String t_replies;    //回复数 ; 
	private String t_zans;      //点赞数量 ;
	private String t_displayorder;
	private List<ArticleThumbUpUserBean> t_zan_user;   //点赞用户列表（3个用户）
	private String status;     //当前登录用户是否对话题点赞（1已经点赞；0表示没有点赞）
	private String quanxian;    //当前登录用户对话题的操作权限 (-1表示没有任何权限； 1 删除话题权限 ； 2 置顶话题权限 ； 3 举报话题权限)
	private List<ArticleCorrelatGroupBean> connect_group;  //相关小组列表 (最多4个)：
	private String t_isshare;
	private String t_recommend;
	private String is_zj; //是否专家
	private String intro; //简介
	
	private String name; //小组名称
	private String tx; //小组头像
	private String tx_key; //小组头像key
	
	private List<ArticleImageBean> images;
	
	public List<ArticleImageBean> getImages() {
		return images;
	}
	public void setImages(List<ArticleImageBean> images) {
		this.images = images;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTx() {
		return tx;
	}
	public void setTx(String tx) {
		this.tx = tx;
	}
	public String getTx_key() {
		return tx_key;
	}
	public void setTx_key(String tx_key) {
		this.tx_key = tx_key;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getT_recommend() {
		return t_recommend;
	}
	public void setT_recommend(String t_recommend) {
		this.t_recommend = t_recommend;
	}
	public String getIs_zj() {
		return is_zj;
	}
	public void setIs_zj(String is_zj) {
		this.is_zj = is_zj;
	}
	public String getT_isshare() {
		return t_isshare;
	}
	public void setT_isshare(String t_isshare) {
		this.t_isshare = t_isshare;
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
	public String getGroupcreateuid() {
		return groupcreateuid;
	}
	public void setGroupcreateuid(String groupcreateuid) {
		this.groupcreateuid = groupcreateuid;
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
	public String getT_view() {
		return t_view;
	}
	public void setT_view(String t_view) {
		this.t_view = t_view;
	}
	public String getT_replies() {
		return t_replies;
	}
	public void setT_replies(String t_replies) {
		this.t_replies = t_replies;
	}
	public String getT_zans() {
		return t_zans==null ? "0" : t_zans;
	}
	public void setT_zans(String t_zans) {
		this.t_zans = t_zans;
	}

	public List<ArticleThumbUpUserBean> getT_zan_user() {
		return t_zan_user;
	}
	public void setT_zan_user(List<ArticleThumbUpUserBean> t_zan_user) {
		this.t_zan_user = t_zan_user;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getQuanxian() {
		return quanxian;
	}
	public void setQuanxian(String quanxian) {
		this.quanxian = quanxian;
	}
	public List<ArticleCorrelatGroupBean> getConnect_group() {
		return connect_group;
	}
	public void setConnect_group(List<ArticleCorrelatGroupBean> connect_group) {
		this.connect_group = connect_group;
	}
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public String getT_lastpost() {
		return t_lastpost;
	}
	public void setT_lastpost(String t_lastpost) {
		this.t_lastpost = t_lastpost;
	}
	public String getT_lastposter() {
		return t_lastposter;
	}
	public void setT_lastposter(String t_lastposter) {
		this.t_lastposter = t_lastposter;
	}
	public String getT_displayorder() {
		return t_displayorder;
	}
	public void setT_displayorder(String t_displayorder) {
		this.t_displayorder = t_displayorder;
	}
	
}
