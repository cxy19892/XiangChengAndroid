package com.yzm.sleep.bean;

import java.io.Serializable;

public class WeightBean implements Serializable {
	private String message;//             文字信息
	private String weight1;//             本周期减重（KG）
	private String weight2;//              当前体重（KG）
	private String weight3;//              本周期最大减重（KG）
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
	public String getWeight1() {
		return weight1;
	}
	public void setWeight1(String weight1) {
		this.weight1 = weight1;
	}
	public String getWeight2() {
		return weight2;
	}
	public void setWeight2(String weight2) {
		this.weight2 = weight2;
	}
	public String getWeigth3() {
		return weight3;
	}
	public void setWeigth3(String weigth3) {
		this.weight3 = weigth3;
	}
	
	
}
