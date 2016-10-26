package com.yzm.sleep.bean;

import java.io.Serializable;

public class ReleaseUpLoadPicBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -313259725468335819L;
	private int 	id;		    //
	private String key;		    //图片的key
	private String path;		//图片的路劲
	private String url;		    //图片在本地的路径
	private double percent;		//图片上传的进度
	private boolean isupload;	//图片是否开始上传
	private boolean iscomplete;	//图片是否上传成功
	private boolean iscanceled;	//图片是否取消上传
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public double getPercent() {
		return percent;
	}
	public void setPercent(double percent) {
		this.percent = percent;
	}
	public boolean isIsupload() {
		return isupload;
	}
	public void setIsupload(boolean isupload) {
		this.isupload = isupload;
	}
	public boolean isIscomplete() {
		return iscomplete;
	}
	public void setIscomplete(boolean iscomplete) {
		this.iscomplete = iscomplete;
	}
	public boolean isIscanceled() {
		return iscanceled;
	}
	public void setIscanceled(boolean iscanceled) {
		this.iscanceled = iscanceled;
	}
	
	
}
