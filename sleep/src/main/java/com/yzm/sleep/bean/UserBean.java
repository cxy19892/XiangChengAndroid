package com.yzm.sleep.bean;

import java.io.Serializable;

public class UserBean implements Serializable {
	
	private String pic;//            用户头像地址
	private String pic_key;//        用户头像地址key
	private String name;//         用户名
	private String id;//             用户ID
	
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getPic_key() {
		return pic_key;
	}
	public void setPic_key(String pic_key) {
		this.pic_key = pic_key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
