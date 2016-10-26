package com.yzm.sleep.bean;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


public class ComfortShowsBean {
	private List<ComfortStrBean> contentList;
	private String laString;
	public List<ComfortStrBean> getContentList() {
		return contentList;
	}
	public void setContentList(List<ComfortStrBean> contentList) {
		this.contentList = contentList;
	}
	public String getLaString() {
		return laString;
	}
	public void setLaString(String laString) {
		this.laString = laString;
	}

	@Override
	public String toString() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("laString", laString);
			JSONArray jsonAry = new JSONArray();
			if(contentList != null){
				for (ComfortStrBean iterable : contentList) {
					JSONObject jsonObjItem = new JSONObject();
					if(iterable != null){
						jsonObjItem.put("content", iterable.getContent());
						jsonObjItem.put("isread", iterable.isIsread());
					}
					jsonAry.put(jsonObjItem);
				}
			}
			jsonObject.put("contentList", jsonAry);
		}catch(Exception e){
			
		}
		return jsonObject.toString();
	}
}
