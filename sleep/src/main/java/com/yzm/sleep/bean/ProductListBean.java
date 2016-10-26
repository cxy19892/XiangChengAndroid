package com.yzm.sleep.bean;

import java.io.Serializable;
/**
 * 产品缩略图
 * @author hetonghua
 *
 */
public class ProductListBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String img = "";//      缩略图
	 private String img_key = "";//    缩略图key
	 private String img_title = "";//     介绍
	 private String img_num = "";//     数量
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getImg_key() {
		return img_key;
	}
	public void setImg_key(String img_key) {
		this.img_key = img_key;
	}
	public String getImg_title() {
		return img_title;
	}
	public void setImg_title(String img_title) {
		this.img_title = img_title;
	}
	public String getImg_num() {
		return img_num;
	}
	public void setImg_num(String img_num) {
		this.img_num = img_num;
	}
	 
	 
}
