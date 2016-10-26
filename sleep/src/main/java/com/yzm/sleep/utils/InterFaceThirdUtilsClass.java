package com.yzm.sleep.utils;

import java.util.List;

import com.yzm.sleep.bean.MyWeiBoFriendBaean;
import com.yzm.sleep.bean.WeiboFriendsBean;

public class InterFaceThirdUtilsClass {
	
	
	public static class GetWeiboFanParamClass{
		public String source;//source	false	string	采用OAuth授权方式不需要此参数，其他授权方式为必填参数，数值为应用的AppKey。
		public String access_token;//		access_token	false	string	采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得。
		public long uid;//		uid	true	int64	需要获取双向关注列表的用户UID。
		public int count;//count	false	int	单页返回的记录条数，默认为50。
		public int page;//page	false	int	返回结果的页码，默认为1。
		public int sort;//sort	false	int	排序类型，0：按关注时间最近排序，默认为0。
		
	}
	public interface InterfaceGetWeiboFanCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode,List<MyWeiBoFriendBaean> mFriendsList,int totapage);
	}
	
	
	
	//获取微信token
	public interface InterfaceGetWeixinAccessTokenCallBack {
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int iCode,WeixinAccessTokenRstClass data);
	}
	public static class GetWeixinAccessTokenParamClass{
		public String code;
		public String appid;
		public String secret;
		public String grant_type;
	}
	public static class WeixinAccessTokenRstClass{
		public String access_token = "";
		public String openid = "";
		public String refresh_token = "";
	}
	
	
	//刷新微信token
	public interface InterfaceRefreshTokenCallBack {
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int iCode,RefreshTokenRstClass data);
	}
	public static class RefreshTokenParamClass{
		public String appid;
		public String grant_type;
		public String refresh_token;
	}
	public static class RefreshTokenRstClass{
		public String access_token = "";
		public String openid = "";
	}
	
	//获取微信用户信息
	public interface InterfaceGetWeixinUserInfoCallBack {
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int iCode,GetWeixinUserInfoRstClass data);
	}
	public static class GetWeixinUserInfoRstClass{
		public String nickname = "";
		public String headimgurl = "";
		public String unionid = "";
	}
}
