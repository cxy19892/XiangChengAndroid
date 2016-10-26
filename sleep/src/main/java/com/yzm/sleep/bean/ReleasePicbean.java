package com.yzm.sleep.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReleasePicbean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2815498228667549355L;
//	private List<String> selectImgs ; 
//	private List<String> suolueImgs ; 
	private List<ReleaseUpLoadPicBean> upLoadImags;
	private boolean isReadyCommit;
	
	public List<String> getSelectImgs() {
		List<String> selectImgs = new ArrayList<String>();
		if(upLoadImags!=null){
			for(ReleaseUpLoadPicBean mReleaseUpLoadPicBean : upLoadImags){
				if(!"add".equals(mReleaseUpLoadPicBean.getKey()))
				selectImgs.add(mReleaseUpLoadPicBean.getUrl());
			}
		}
		return selectImgs;
	}
//	public void setSelectImgs(List<String> selectImgs) {
//		this.selectImgs = selectImgs;
//	}
	public List<ReleaseUpLoadPicBean> getUpLoadImags() {
		return upLoadImags;
	}
	public void setUpLoadImags(List<ReleaseUpLoadPicBean> upLoadImags) {
		this.upLoadImags = upLoadImags;
	}
	public boolean isReadyCommit() {
		return isReadyCommit;
	}
	public void setReadyCommit(boolean isReadyCommit) {
		this.isReadyCommit = isReadyCommit;
	}
//	public List<String> getSuolueImgs() {
//		return suolueImgs;
//	}
//	public void setSuolueImgs(List<String> suolueImgs) {
//		this.suolueImgs = suolueImgs;
//	}

	
}
