package com.yzm.sleep.bean;

import java.io.Serializable;


public class SmokeBean implements Serializable{
	
	private String message;//         文字信息
	private String smoke_nums;//     本周期吸烟总量
	private String smoke_days;//      本周期有效天数
	//（本周期平均抽烟数量等于smoke_nums/smoke_days）
	private String smoke_shang_nums;//     上周期吸烟总量
	private String smoke_shang_days;//     上周期有效天数
	private String shuiqian_cishu;//          本周期睡前一小时抽烟次数
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
	public String getSmoke_nums() {
		return smoke_nums;
	}
	public void setSmoke_nums(String smoke_nums) {
		this.smoke_nums = smoke_nums;
	}
	public String getSmoke_days() {
		return smoke_days;
	}
	public void setSmoke_days(String smoke_days) {
		this.smoke_days = smoke_days;
	}
	public String getSmoke_shang_nums() {
		return smoke_shang_nums;
	}
	public void setSmoke_shang_nums(String smoke_shang_nums) {
		this.smoke_shang_nums = smoke_shang_nums;
	}
	public String getSmoke_shang_days() {
		return smoke_shang_days;
	}
	public void setSmoke_shang_days(String smoke_shang_days) {
		this.smoke_shang_days = smoke_shang_days;
	}
	public String getShuiqian_cishu() {
		return shuiqian_cishu;
	}
	public void setShuiqian_cishu(String shuiqian_cishu) {
		this.shuiqian_cishu = shuiqian_cishu;
	}

	
}
