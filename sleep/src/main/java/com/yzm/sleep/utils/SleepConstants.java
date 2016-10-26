package com.yzm.sleep.utils;


/**
 * 常量类
 * 
 * @author Administrator
 * 
 */
public class SleepConstants {
	/**新浪睡眠应用APP_KEY*/
	public static final String SINA_SLEEP_APP_KEY = "1601763683";
	public static final String SINA_SLEEP_APP_SECRET = "06583526b618dfdcebae773eb937fdf0";
	
    /** 
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * 
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String REDIRECT_URL = "http://www.sina.com";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     * 
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     * 
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * 
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE = 
            "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
	
	/**QQ空间睡眠应用APP_ID*/
	public static final String TENCENT_SLEEP_APP_ID = "1103438039";
	/**QQ空间睡眠应用APP_KEY*/
	public static final String TENCENT_SLEEP_APP_KEY = "Z1DAwA0k3o8oYng3";
	
	/**微信香橙应用  APP_ID*/
	public static final String WECHAT_SLEEP_APP_ID = "wx6bd5de3c891c60ba";
	/**微信香橙应用 APP_SECRET*/
	public static final String WECHAT_SLEEP_APP_SECRET = "d4624c36b6795d1d99dcf0547af5443d";
	
	/**网络请求加密密钥*/
	public static final String NET_REQUEST_KEY = "$hd2880!@$xc";
	
	
	/**用户登录*/
	public static final String DO_SIGNUP = "%s?my_int_username=%s&my_int_nickname=%s&my_int_pwd=%s&my_int_age=%s&my_int_gender=%s&my_int_occupation=%s&my_int_profile=%s" ;
	/**提交用户信息*/
	public static final String ADD_USER_MESSAGE = "%s?platform=%s&my_ext_acc=%s&my_ext_nickname=%s&my_ext_profile=%s&my_int_age=%s&my_int_gender&my_int_occupation=%s&friend_num=%s&friendacc_1=%s" ;
	/**获取微博好友列表*/
	public static final String GET_WEIBO_FEIENDS = "%s?access_token=%s&uid=%s";
	/**QQ获取应用好友列表*/
	public static final String GET_QQ_FRIENDS = "%s?access_token=%s&oauth_consumer_key=%s&openid=%s&format=json" ;
	/**QQ获取用户openid*/
	public static final String QQ_GETOPENID = "%s?access_token=%s";
	/**微信获取access_token地址*/
	public static final String WX_GET_ACCESSTOKEN_URL = "%s?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
	/**android检查版本是否可更新地址*/
	public static final String CHECK_VERSION_UPDATE = "%s?system_type=%s&current_version=%s";

	
	//登录平台
	/**自平台*/
	public static final String PLATFORM_OWN = "300000000";
	/**微信*/
	public static final String PLATFORM_WEIXIN = "300000001";
	/**微博*/
	public static final String PLATFORM_WEIBO = "300000002";
	/**QQ*/
	public static final String PLATFORM_QQ = "300000003";
	
	//用户职业
	public static final String PROFESSION_NONE = "200000000";//未分类
	
	public static final String PROFESSION_IT = "200000001";//IT
	public static final String PROFESSION_FINANCIAL = "200000002";//金融
	public static final String PROFESSION_PERSONNEL = "200000003";//人事行政
	public static final String PROFESSION_EDUCATION = "200000004";//教育法律
	
	public static final String PROFESSION_MARKET = "200000005";//销售
	public static final String PROFESSION_BUILDING = "200000006";//房地产/建筑
	public static final String PROFESSION_CULTURE = "200000007";//文化传媒
	public static final String PROFESSION_LOGISTICS = "200000008";//物流
	
	public static final String PROFESSION_MANUFACTURE = "200000009";//制造生产
	public static final String PROFESSION_MEDICAL_TREATMENT = "200000010";//医疗
	public static final String PROFESSION_SERVE = "200000011";//服务业
	public static final String PROFESSION_OTHER = "200000012";//学生/其他
	
	public static final String IS_FIRST_USE = "isFirstUse";
	
	
	public static final int USER_TYPE_WORKING = 0;
	public static final int USER_TYPE_NIGHT_CAT = 1;
	public static final int USER_TYPE_STUDENT = 2;

	
	public static  final int REQUEST_CODE = 1;
	
	/**
	 * 下载管理广播的action
	 */
	public static final String DOWNLOADMANAGEACTION = "com.wpy.multithreadeddownload.DownloadManageActivity";
	
	/**
	 * 下载的线程数量
	 */
	public static final int THREADCOUNT = 3;
	
	
	public static final int FRAGEMNT_HOME = 0;
	public static final int FRAGEMNT_RANKING = 1;
	public static final int FRAGEMNT_ALARM = 2;
	public static final int FRAGEMNT_MESSAGE = 3;
	public static final int FRAGEMNT_PERSONAL = 4;
	public static final int FRAGEMNT_HARDWARE_DATA = 5;
}
