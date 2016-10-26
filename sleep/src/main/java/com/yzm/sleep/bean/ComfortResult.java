package com.yzm.sleep.bean;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ComfortResult {
	
	private List<ComfortShowsBean> list;

	public List<ComfortShowsBean> getList() {
		return list;
	}

	public void setList(List<ComfortShowsBean> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArry = new JSONArray();
		try {
			if(list != null){
				for (ComfortShowsBean mComfortShowsBean : list) {
					JSONObject jsonOb = new JSONObject();
					jsonOb.put("laString", mComfortShowsBean.getLaString());
					JSONArray jsonAry = new JSONArray();
					if(mComfortShowsBean.getContentList() != null){
						for (ComfortStrBean iterable : mComfortShowsBean.getContentList()) {
							JSONObject jsonObjItem = new JSONObject();
							if(iterable != null){
								jsonObjItem.put("content", iterable.getContent());
								jsonObjItem.put("isread", iterable.isIsread());
							}
							jsonAry.put(jsonObjItem);
						}
					}
					jsonOb.put("contentList", jsonAry);
					jsonArry.put(jsonOb);
				}
				jsonObject.put("list", jsonArry);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return jsonObject.toString();
	}
}
