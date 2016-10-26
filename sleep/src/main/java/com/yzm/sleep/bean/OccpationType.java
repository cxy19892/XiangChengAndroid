package com.yzm.sleep.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OccpationType implements Serializable {
	private String typeName;
	private boolean isSelset;

	private List<OccpationBean> type = new ArrayList<OccpationBean>();

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public List<OccpationBean> getType() {
		return type;
	}

	public void setType(List<OccpationBean> type) {
		this.type = type;
	}

	public boolean isSelset() {
		return isSelset;
	}

	public void setSelset(boolean isSelset) {
		this.isSelset = isSelset;
	}

}
