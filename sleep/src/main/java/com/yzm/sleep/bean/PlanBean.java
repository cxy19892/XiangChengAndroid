package com.yzm.sleep.bean;

import java.io.Serializable;

public class PlanBean implements Serializable {
	private String planid; // 计划ID
	private String name;   //计划名字 
	private String type;   // 所属分类 ( 1生活习惯 ； 2 作息规律；3卧室环境；4 心理活动)  
	private String isfinish;// 是否完成（1完成； 0未完成）
	private String isshow;  // 是否显示（1显示；0不显示） 
	
	
	public String getPlanid() {
		return planid;
	}
	public void setPlanid(String planid) {
		this.planid = planid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIsfinish() {
		return isfinish;
	}
	public void setIsfinish(String isfinish) {
		this.isfinish = isfinish;
	}
	public String getIsshow() {
		return isshow;
	}
	public void setIsshow(String isshow) {
		this.isshow = isshow;
	}
	
	
}
