package com.yzm.sleep.bean;

import java.io.Serializable;
import java.util.List;

public class EnvBean implements Serializable {
	private List<String> message;//              文字信息
	private List<EnvList> env_list;//               环境列表

	public List<String> getMessage() {
		return message;
	}

	public void setMessage(List<String> message) {
		this.message = message;
	}

	public List<EnvList> getEnv_list() {
		return env_list;
	}

	public void setEnv_list(List<EnvList> env_list) {
		this.env_list = env_list;
	}

	public class EnvList{
		private String title;//     对应的标题
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
