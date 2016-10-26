package com.yzm.sleep.bean;

import java.io.Serializable;

public class WinBean implements Serializable {
	private String message;//             文字信息
	private String win_nums;//            本周期睡前4小时饮酒次数
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
	public String getWin_nums() {
		return win_nums;
	}
	public void setWin_nums(String win_nums) {
		this.win_nums = win_nums;
	}
	
	
}
