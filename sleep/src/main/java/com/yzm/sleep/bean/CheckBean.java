package com.yzm.sleep.bean;

import java.io.Serializable;

public class CheckBean implements Serializable {
	private String choice_title;//
	private String choice_flag;//

	public String getChoice_title() {
		return choice_title;
	}

	public void setChoice_title(String choice_title) {
		this.choice_title = choice_title;
	}

	public String getChoice_flag() {
		return choice_flag;
	}

	public void setChoice_flag(String choice_flag) {
		this.choice_flag = choice_flag;
	}

}
