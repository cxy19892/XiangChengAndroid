package com.yzm.sleep.utils;

public class URLUtil {
	
	public static final String BASEURL="http://115.29.187.126/orangesleep_debug";//云服务器测试链接地址
//	public static final String BASEURL="http://115.29.187.126/orangesleep";//服务器地址

	public static final String DN_BASEURL="http://m.apporange.cn/orangesleep_debug";//云服务器测试域名地址
//	public static final String DN_BASEURL="http://m.apporange.cn/orangesleep";//服务器域名地址
	
	
	/**香橙app下载地址*/
	public static final String XIANGCHENG_APP_DOWNLOAD_URL = "http://a.app.qq.com/o/simple.jsp?pkgname=com.yzm.sleep";
	/**香橙图标地址*/
	public static String XIANGCHENG_ICON_URL = "http://115.29.187.126/orangesleep/upload_file/xiangcheng.png";
	/**香橙分享睡眠数据图标地址*/
	public static String SHARE_DATA_ICON_URL = "http://115.29.187.126/orangesleep/upload_file/share.png";
	
	
	/**内部登录地址*/
	public static final String LOGIN_URL = BASEURL +"/internal_login.php";
	/**内部注册地址*/
	public static final String SIGNUP_URL = BASEURL +"/internal_signup.php";
	/**添加好友，确认好友申请，删除好友地址*/
	public static final String ADD_DELETE_FRIEND_URL = BASEURL +"/add_delete_friend.php";
	/**下载好友某日的睡眠数据地址*/
	public static final String FRIENDS_RANKING_URL = BASEURL +"/download_friends_data.php";
	/**下载附近某日的睡眠数据地址*/
	public static final String NEARBY_RANKING_URL = BASEURL +"/download_nearby_data.php";
	/**下载某日的用户睡眠时间的同行排名地址*/
	public static final String OCCUPATION_RANKING_URL = BASEURL +"/download_daily_statistics.php";
	/**保存来自第三方的用户数据及好友列表地址*/
	public static final String SAVE_THIRDPARTY_USERINFO_URL = BASEURL +"/save_user_info.php";
	/**上传用户某日的睡眠数据地址*/
	public static final String UPLOAD_SLEEP_DATA_URL = BASEURL +"/upload_user_data.php";
	/**查询是否有好友请求地址*/
	public static final String CHECK_FRIEND_REQUEST_URL = BASEURL +"/check_friend_request.php";
	/**查询两者的好友状态*/
	public static final String CHECK_FRIEND_STATUS_URL = BASEURL +"/check_friend_status.php";
	/**上传用户资料*/
	public static final String UPDATE_USER_INFO_URL = BASEURL +"/update_user_info.php";
	
	/**微博获取好友UID列表地址*/
	public static final String WEIBO_GETFEIENDS_URL = "https://api.weibo.com/2/friendships/followers/ids.json";
	/**QQ获取应用好友列表地址*/
	public static final String QQ_GET_FRIENDS_URL = "https://graph.qq.com/user/get_app_friends";
	/**QQ获取用户openid*/
	public static final String QQ_GETOPENID_URL = "https://graph.qq.com/oauth2.0/me";
	/**QQ获取openid和openkey的CanvasURL*/
	public static final String QQ_Canvas_URL = "http://rc.qzone.qq.com/main";
	
	
	/**微信获取access_token地址*/
	public static final String GET_WX_ACCESSTOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
	/**刷新access_token*/
	public static final String REFRESH_WX_ACCESSTOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
	/**获取微信用户信息*/
	public static final String GET_WX_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo";
	
	public static String SESSIONID ;
	
	public static String WEB_NODATA_INTENT_URL = "http://m.apporange.cn/shuoming/shouji.php?phone=";
	/** 常见问题的地址*/
	public static String WEB_COMMON_PROBLEM_URL = "http://m.apporange.cn/yingjianshuoming2/index.php?id=";
	/**常见问题及解决*/
	public static String WEB_PROBLEM_AND_SOLVE = "http://m.apporange.cn/faq/";
	/**购买链接*/
	public static String WEB_BUYING_URL = "https://shop137501116.taobao.com/?spm=2013.1.0.0.zCkev7";
	/**评估链接*/
	public static final String SLEEPINDEX_URL = URLUtil.BASEURL+"/pinggu/index2.php?";
	public static final String SLEEP_SHXG_URL = URLUtil.BASEURL+"/pinggu/shenghuoxiguan.php?";
	public static final String SLEEP_SMGL_URL = URLUtil.BASEURL+"/pinggu/shueimiangueilv.php?";
	public static final String SLEEP_SMHJ_URL = URLUtil.BASEURL+"/pinggu/shueimianhuanjing.php?";
	public static final String SLEEP_XLHD_URL = URLUtil.BASEURL+"/pinggu/xinlihuodong.php?";
	public static final String SLEEP_FEEKBACK_URL = URLUtil.BASEURL+"/fankuei/index.php?";
}
