package com.yzm.sleep.bean;

import java.io.Serializable;

public class LocalRingsaBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String path;
	private int state;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}

	

}
