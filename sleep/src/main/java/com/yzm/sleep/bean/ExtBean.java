package com.yzm.sleep.bean;

import java.io.Serializable;

public class ExtBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ExtAttrBean attr = new ExtAttrBean();
	public ExtAttrBean getAttr() {
		return attr;
	}
	public void setAttr(ExtAttrBean attr) {
		this.attr = attr;
	}
	
	
}
