package com.yzm.sleep.bean;

import java.io.Serializable;

public class ShareClassParam implements Serializable{
	private static final long serialVersionUID = 1L;
	/** 链接地址（所有分享类型必须传） */
	public String targetUrl = "";
	/** 分享标题（话题、小组分享暂时不用传）*/
	public String shareTitle = "";
	/** 分享副标题 */
	public String shareSummary = "";
	/** 分享网络图片地址 ）*/
	public String sharePictureUrl = "";
}
