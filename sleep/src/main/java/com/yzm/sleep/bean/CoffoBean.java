package com.yzm.sleep.bean;

import java.io.Serializable;

public class CoffoBean implements Serializable {
	private String message;//             文字信息
	private String coffo_nums;//            本周期睡前4小时饮酒次数
	private String isshow;//          是否显示（1显示；0不显示）
	
	public String getIsshow() {
		return isshow;
	}
	public void setIsshow(String isshow) {
		this.isshow = isshow;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCoffo_nums() {
		return coffo_nums;
	}
	public void setCoffo_nums(String coffo_nums) {
		this.coffo_nums = coffo_nums;
	}
	
	
	
}
