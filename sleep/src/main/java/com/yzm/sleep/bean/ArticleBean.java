package com.yzm.sleep.bean;

import java.io.Serializable;
import java.util.List;

public class ArticleBean implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tid;                    	//文章id ;
	private String listtype;    			//类型（1话题；2文章）;
	private String t_subject;             	// 标题 ;
	private String t_author;              	//作者名；
	private String uid;                   	//作者id   
	private String t_dateline;              //发布时间； 
	private String author_profile;          //作者头像 ；
	private String author_profile_key;    	//作者头像key； 
	private String t_view;               	//阅读数量
	private String bjpic;
	private String bjpickey;
	private String is_zj;//是否是专家     是否是专家。1是；0不是
	private String t_views;               	//阅读数量/////
	private String t_message;            	//内容； 
	private List<ArticleImageBean> images;         // 图片格式为：
	private String status;     //当前登录用户是否对话题点赞（1已经点赞；0表示没有点赞）
	
	private String object_id;//": "165",
	private String term_id;//": "1",
	private String post_title;//": "吸烟对睡眠的影响比喝咖啡更大",
	private String post_modified;//": "2016-02-18 13:21:39",
	private String post_date;//": "2016-02-18 13:22:12",
	private String post_keywords;//": "",
	private String post_excerpt;//": "吸烟对睡眠的影响只能用“万万想不到‘来形容，它的真实威力比喝咖啡更大！",
	private String istop;//": "0",
	private String recommended;//": "0",
	private String listorder;//": "0",
	private String post_hits;//": "0"
	
	
	
	public String getObject_id() {
		return object_id;
	}
	public void setObject_id(String object_id) {
		this.object_id = object_id;
	}
	public String getTerm_id() {
		return term_id;
	}
	public void setTerm_id(String term_id) {
		this.term_id = term_id;
	}
	public String getPost_title() {
		return post_title;
	}
	public void setPost_title(String post_title) {
		this.post_title = post_title;
	}
	public String getPost_modified() {
		return post_modified;
	}
	public void setPost_modified(String post_modified) {
		this.post_modified = post_modified;
	}
	public String getPost_date() {
		return post_date;
	}
	public void setPost_date(String post_date) {
		this.post_date = post_date;
	}
	public String getPost_keywords() {
		return post_keywords;
	}
	public void setPost_keywords(String post_keywords) {
		this.post_keywords = post_keywords;
	}
	public String getPost_excerpt() {
		return post_excerpt;
	}
	public void setPost_excerpt(String post_excerpt) {
		this.post_excerpt = post_excerpt;
	}
	public String getIstop() {
		return istop;
	}
	public void setIstop(String istop) {
		this.istop = istop;
	}
	public String getRecommended() {
		return recommended;
	}
	public void setRecommended(String recommended) {
		this.recommended = recommended;
	}
	public String getListorder() {
		return listorder;
	}
	public void setListorder(String listorder) {
		this.listorder = listorder;
	}
	public String getPost_hits() {
		return post_hits;
	}
	public void setPost_hits(String post_hits) {
		this.post_hits = post_hits;
	}
	public String getListtype() {
		return listtype;
	}
	public void setListtype(String listtype) {
		this.listtype = listtype;
	}
	public String getIs_zj() {
		return is_zj;
	}
	public void setIs_zj(String is_zj) {
		this.is_zj = is_zj;
	}
	public String getBjpic() {
		return bjpic;
	}
	public void setBjpic(String bjpic) {
		this.bjpic = bjpic;
	}
	public String getBjpickey() {
		return bjpickey;
	}
	public void setBjpickey(String bjpickey) {
		this.bjpickey = bjpickey;
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
	public String getT_view() {
		return t_view;
	}
	public void setT_view(String t_view) {
		this.t_view = t_view;
	}
	public String getT_message() {
		return t_message;
	}
	public void setT_message(String t_message) {
		this.t_message = t_message;
	}
	public List<ArticleImageBean> getImages() {
		return images;
	}
	public void setImages(List<ArticleImageBean> images) {
		this.images = images;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getT_views() {
		return t_views;
	}
	public void setT_views(String t_views) {
		this.t_views = t_views;
	}
	
	
}
