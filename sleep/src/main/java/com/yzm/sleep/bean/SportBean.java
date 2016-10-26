package com.yzm.sleep.bean;

import java.io.Serializable;
import java.util.List;

public class SportBean implements Serializable {
	private String message;//            文字信息
	private List<SportLists> sport_lists;//           本周期运动时间及次数列表：        
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

	public List<SportLists> getSport_lists() {
		return sport_lists;
	}

	public void setSport_lists(List<SportLists> sport_lists) {
		this.sport_lists = sport_lists;
	}

	public class SportLists{
		private String  title;//     对应的标题
		private String nums;//    次数
		
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getNums() {
			return nums;
		}
		public void setNums(String nums) {
			this.nums = nums;
		}
		
		
	}                      
}
