package com.yzm.sleep.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SleepCaseBean implements Serializable {
	
	private String start_time; //23:25",
	private String end_time; //23:55",
	private String title;//睡前放松",
	private String shichang; //  时长
	private String suggest;
	
	private String selsectPo; // 1选中; 其它 未选中
	
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getSelsectPo() {
		return selsectPo;
	}
	public void setSelsectPo(String selsectPo) {
		this.selsectPo = selsectPo;
	}
	public String getSuggest() {
		return suggest;
	}
	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}
	public String getShichang() {
		return shichang;
	}
	public void setShichang(String shichang) {
		this.shichang = shichang;
	}
	
	
}
