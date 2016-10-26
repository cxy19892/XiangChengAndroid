package com.yzm.sleep.model;

import java.io.Serializable;

public class PlayRingInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	public String id;
	public String title;
	public String duration;
	public String size;
	public String url;
	public PlayRingInfo(String id, String title, String duration, String size,String url) {
		super();
		this.id = id;
		this.title = title;
		this.duration = duration;
		this.size = size;
		this.url = url;
	}
	
}
