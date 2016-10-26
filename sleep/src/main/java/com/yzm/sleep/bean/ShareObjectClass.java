package com.yzm.sleep.bean;

import java.io.Serializable;

public class ShareObjectClass implements Serializable{
	private static final long serialVersionUID = 1L;
	
		/** 链接地址（所有分享类型必须传） */
		private String targetUrl = "";
		/** 分享标题（话题、小组分享暂时不用传）*/
		private String shareTitle = "";
		/** 分享副标题 （话题、小组分享暂时不用传）*/
		private String shareSummary = "";
		/** 分享网络图片地址 （不用传）*/
		private String sharePictureUrl = "";
		/** 分享本地图片路径*/
		private String sharePicturePath = "";
		
		private String platform = "";
		private String messeage = "分享";
		
		public String getSharePicturePath() {
			return sharePicturePath;
		}
		public void setSharePicturePath(String sharePicturePath) {
			this.sharePicturePath = sharePicturePath;
		}
		public String getTargetUrl() {
			return targetUrl;
		}
		public void setTargetUrl(String targetUrl) {
			this.targetUrl = targetUrl;
		}
		public String getShareTitle() {
			return shareTitle;
		}
		public void setShareTitle(String shareTitle) {
			this.shareTitle = shareTitle;
		}
		public String getShareSummary() {
			return shareSummary;
		}
		public void setShareSummary(String shareSummary) {
			this.shareSummary = shareSummary;
		}
		public String getSharePictureUrl() {
			return sharePictureUrl;
		}
		public void setSharePictureUrl(String sharePictureUrl) {
			this.sharePictureUrl = sharePictureUrl;
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
		
		
		
		
}
