package com.yzm.sleep.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FankuiDataBean implements Serializable {

	private String title;// 标题
	private String fankui_key; //提叫key
	private String flag;// 1有效；0无效
	private List<CheckBean> check = new ArrayList<CheckBean>();
	
	public String getFankui_key() {
		return fankui_key;
	}

	public void setFankui_key(String fankui_key) {
		this.fankui_key = fankui_key;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public List<CheckBean> getCheck() {
		return check;
	}

	public void setCheck(List<CheckBean> check) {
		this.check = check;
	}
	
}
