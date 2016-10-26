package com.yzm.sleep.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  构建 String的数组
 * @author Administrator
 *
 */
public class BuildStringListIndexBean implements Serializable{
	
	private int index;//默认位置
	private List<String> contentList=new ArrayList<String>();
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public List<String> getContentList() {
		return contentList;
	}
	public void setContentList(List<String> contentList) {
		this.contentList = contentList;
	}
}
