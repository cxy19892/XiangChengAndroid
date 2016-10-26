package com.yzm.sleep.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LifeHabitBean implements Serializable {
	
	private String pic;// 图片（服务器中的图片）
	private String title;// 标题名
	private String isEdit; // 1为不可修改
	
	private List<CustomerItem> choice = new ArrayList<CustomerItem>();// 对应的选项情况列表

	public String getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
	}
	
	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<CustomerItem> getChoice() {
		return choice;
	}

	public void setChoice(List<CustomerItem> choice) {
		this.choice = choice;
	}

	public class CustomerItem {
		private String choice_id;// 选项ID
		private String type;// 所属类型(1烟;2酒;3咖啡因;4运动;5减肥) 
		private String title;// 选项名字
		private String flag;// 选项是否被选择（0没有；1选择）
		
		private List<String> suggest = new ArrayList<String>();// 专家建议

		public List<String> getSuggest() {
			return suggest;
		}

		public void setSuggest(List<String> suggest) {
			this.suggest = suggest;
		}
		
		public String getChoice_id() {
			return choice_id;
		}
		public void setChoice_id(String choice_id) {
			this.choice_id = choice_id;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
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
		
	}
	
}
