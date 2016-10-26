package com.yzm.sleep.bean;

import java.util.List;

public class ComfortBean {
	
	public List<String> content;
	
	public String laString;
	
	public String point;
	
	
	private boolean selsectUse;
	
	private int selectPosition = 1;

	public int getSelectPosition() {
		return selectPosition;
	}

	public void setSelectPosition(int selectPosition) {
		this.selectPosition = selectPosition;
	}

	public boolean isSelsectUse() {
		return selsectUse;
	}

	public void setSelsectUse(boolean selsectUse) {
		this.selsectUse = selsectUse;
	}
	
	
}
 