package com.yzm.sleep.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;
public class ShareObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String platform;
	private String messeage;
	private String imgUrl;
	private String score;

	public ShareObject(JSONObject jo) throws JSONException {
		if(jo.has("platform")) {
			setPlatform(jo.getString("platform"));
		}
		if(jo.has("messeage")) {
			setMesseage(jo.getString("messeage"));
		}
		if(jo.has("imgurl")) {
			setImgUrl(jo.getString("imgurl"));
		}
		if(jo.has("score")) {
			setScore("0");
		}
	}
	public ShareObject(String platform,String message)  {
			setPlatform(platform);
			setMesseage(message);
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getMesseage() {
		return messeage;
	}

	public void setMesseage(String messeage) {
		this.messeage = messeage;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
}
