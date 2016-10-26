package com.yzm.sleep.utils;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.easemob.chat.EMChatManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yzm.sleep.Constant;
import com.yzm.sleep.background.DataUtils;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MytabOperate;
import com.yzm.sleep.background.SleepInfo;
import com.yzm.sleep.bean.LifeHabitBean;
import com.yzm.sleep.bean.OccpationBean;
import com.yzm.sleep.bean.OccpationType;

/**
 * 数据存储类 封装保存数据到手机的各种方法，如文件存储，sharePreference,网络存储
 * 
 * @author
 * 
 */

@SuppressLint("SimpleDateFormat") 
public class PreManager {
	// private final static String LOG_TAG = PreManager.class.getSimpleName();
	private static PreManager preManager;

	private PreManager() {

	}

	public static synchronized PreManager instance() {
		if (preManager == null)
			preManager = new PreManager();
		return preManager;
	}

	/**
	 * 保存登录账号
	 * 
	 * @param context
	 * @param flag
	 */
	public void saveLoginUserName(Context context, String username) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("login_username", username).commit();
	}

	/**
	 * 获取登录账号
	 * 
	 * @param context
	 * @param flag
	 */
	public String getLoginUserName(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("login_username", "");
	}

	/**
	 * 保存是否设置睡眠时间
	 * 
	 * @param context
	 * @param flag
	 */
	public void saveHasSetAlarm(Context context, boolean flag) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean("first_set_alarm", flag).commit();
	}

	/**
	 * 获取是否设置睡眠时间
	 * 
	 * @param context
	 * @param flag
	 */
	public boolean getHasSetAlarm(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("first_set_alarm", false);
	}

	/**
	 * 保存生日（待定）
	 * 
	 * @param context
	 * @param birthday
	 */
	@SuppressLint("SimpleDateFormat")
	public void saveUserBirthday(Context context, String birthday) {
		if(birthday==null){
			return;
		}
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString("birthday", birthday).commit();
	}

	/**
	 * 获取生日
	 * 
	 * @param context
	 * @return
	 */
	public String getUserBirthday(Context context) {
		String birthday = PreferenceManager.getDefaultSharedPreferences(context)
				.getString("birthday", "19900101");
		
		return birthday.length() < 8 ? birthday + "0101" : birthday;
	}

	/**
	 * 保存是否第一次使用app
	 *
	 * @param context
	 * @param flag
	 */
	public void saveIsFirstUse(Context context, boolean flag) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean("new_app_first", flag).commit();
	}

	/**
	 * 获得是否第一次使用v2.2 默认铃音版本
	 * 
	 * @param context
	 * @return
	 */
	public boolean getIsFirstUseDefaultAudio(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("new_app_first_default_audio", true);
	}

	/**
	 * 保存是否第一次使用v2.2 默认铃音版本
	 * 
	 * @param context
	 * @param flag
	 */
	public void saveIsFirstUseDefaultAudio(Context context, boolean flag) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean("new_app_first_default_audio", flag).commit();
	}
	/**
	 * 获取是否第一次使用v3.2 智能枕头版本
	 * 
	 * @param context
	 * @return
	 */
	public boolean getIsFirstUseSmartPillow(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("first_use_smartpillow", true);
	}
	
	/**
	 * 保存是否第一次使用v3.2 智能枕头版本
	 * 
	 * @param context
	 * @param flag
	 */
	public void saveIsFirstUseSmartPillow(Context context, boolean flag) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putBoolean("first_use_smartpillow", flag).commit();
	}

	/**
	 * 获得是否是第一次使用
	 * 
	 * @param context
	 * @return
	 */
	public boolean getIsFirstUse(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("new_app_first", true);
	}

	public void saveShowGuide(Context context){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("is_show_guide", Constant.IS_SHOW_GUIDE).commit();
	}

	public boolean getShowGuide(Context context){
		try{
			String showStr = PreferenceManager.getDefaultSharedPreferences(context).getString("is_show_guide", "");
			return Constant.IS_SHOW_GUIDE .equals(showStr);
		}catch (Exception e){
			return  false;
		}

	}

	/**
	 * 保存是否进入过闹钟
	 * 
	 * @param context
	 * @param flag
	 */
	public void saveHasGotoAlarm(Context context, boolean flag) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean("into_alarm_fragment_first", flag).commit();
	}

	/**
	 * 获得是否进入过闹钟
	 * 
	 * @param context
	 * @return
	 */
	public boolean getHasGotoAlarm(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("into_alarm_fragment_first", false);
	}

	/**
	 * 保存是否第一次录音
	 * 
	 * @param context
	 * @param flag
	 */
	public void setIsFirstRecord(Context context, boolean flag) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean("first_record", flag).commit();
	}

	/** 获取是否是第一次录音 */
	public boolean isFirstRecord(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("first_record", true);
	}

	/**
	 * 保存用户设置的睡眠时刻
	 * 
	 * @param context
	 * @param s
	 */
	public void saveSleepTime_Setting(Context context, String s) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("sleep_time", s).commit();
	}

	/**
	 * 获取设置的睡眠时刻
	 * 
	 * @param context
	 * @return
	 */
	public String getSleepTime_Setting(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("sleep_time", "00:30");
	}

	/**
	 * 保存设置的起床时刻
	 * 
	 * @param context
	 * @param s
	 */
	public void saveGetupTime_Setting(Context context, String s) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("getup_time", s).commit();
	}

	/**
	 * 获取设置的起床时刻
	 * 
	 * @param context
	 * @return
	 */
	public String getGetupTime_Setting(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("getup_time", "08:00");
	}

	/**
	 * 保存用户性别
	 * 
	 * @param context
	 * @param gender
	 *            性别：1-男性 2-女性
	 */
	public void saveUserGender(Context context, String gender) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("user_gender", gender).commit();
	}

	/**
	 * 获取用户性别
	 * 
	 * @param context
	 * @return 性别：01-男性 02-女性
	 */
	public String getUserGender(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("user_gender", "02");
	}


	/**保存用身高 @param context*/
	public void saveUserHeight(Context context, String height) {
		if(height == null || height.equals("")){
			return;
		}
		try{
			PreferenceManager.getDefaultSharedPreferences(context).edit()
					.putFloat("user_height", Float.parseFloat(height)).commit();
		}catch(Exception e){}
	}

	/**获取身高*/
	public float getUserHeight(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getFloat(
				"user_height", 0f);
	}

	/**保存用体重*/
	public void saveUserWeight(Context context, String weight) {
		if(weight == null || weight.equals("") ||".".equals(weight)){
			return;
		}
		try{
			PreferenceManager.getDefaultSharedPreferences(context).edit()
					.putFloat("user_weight", Float.parseFloat(weight)).commit();
		}catch(Exception e){}
	}

	/**获取体重*/
	public float getUserWeight(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getFloat(
				"user_weight", 0.0f);
	}

	/**
	 * 保存用户职业 编码
	 * @param context
	 */
	public void saveUserOccupation(Context context, String occupation) {
		if(occupation == null || occupation.equals("")){
			return;
		}
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("user_occupation", occupation).commit();

	}

	/**
	 * 获取用户职业编码
	 * @param context
	 */
	public String getUserOccupation(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString("user_occupation", "300000000");
	}
	/**
	 * 保存是否是专家
	 * @param context
	 */
	public void saveUserIsExpert(Context context, String occupation) {
		if(occupation == null || occupation.equals("")){
			return;
		}
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("user_is_expert", occupation).commit();
		
	}
	
	/**
	 * 获取是否是专家
	 * @param context
	 */
	public String getUserIsExpert(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString("user_is_expert", "0");
	}

	/**
	 * 获取职业数据
	 */
	public List<OccpationType> getOccupationTypes() {
		String types = "[{'type':[{'code':'200000001','vocation':'IT'},{'code':'200000013','vocation':'通讯'},{'code':'200000014','vocation':'电信运营'},{'code':'200000015','vocation':'网络游戏'},{'code':'200000016','vocation':'互联网'}],'typeName':'信息技术'},{'type':[{'code':'200000017','vocation':'投资'},{'code':'200000018','vocation':'股票/基金'},{'code':'200000019','vocation':'保险'},{'code':'200000020','vocation':'银行'},{'code':'200000021','vocation':'信托/担保'}],'typeName':'金融保险'},{'type':[{'code':'200000022','vocation':'咨询'},{'code':'200000023','vocation':'个体经营'},{'code':'200000024','vocation':'美容美发'},{'code':'200000025','vocation':'旅游'},{'code':'200000026','vocation':'酒店餐饮'},{'code':'200000027','vocation':'休闲娱乐'},{'code':'200000028','vocation':'贸易'},{'code':'200000029','vocation':'汽车'},{'code':'200000030','vocation':'房地产'},{'code':'200000031','vocation':'物业管理'},{'code':'200000032','vocation':'装修/装潢'}],'typeName':'商业服务'},{'type':[{'code':'200000033','vocation':'建筑'},{'code':'200000034','vocation':'土木工程'},{'code':'200000035','vocation':'机械制造'},{'code':'200000036','vocation':'电子'},{'code':'200000037','vocation':'生物医药'},{'code':'200000038','vocation':'食品'},{'code':'200000039','vocation':'服装'},{'code':'200000040','vocation':'能源'}],'typeName':'工程制造'},{'type':[{'code':'200000041','vocation':'航空'},{'code':'200000042','vocation':'铁路'},{'code':'200000043','vocation':'航运/船舶'}],'typeName':'交通运输'},{'type':[{'code':'200000044','vocation':'媒体出版'},{'code':'200000045','vocation':'设计'},{'code':'200000046','vocation':'文化传播'},{'code':'200000047','vocation':'广告创意'},{'code':'200000048','vocation':'动漫'},{'code':'200000049','vocation':'公关/会展'},{'code':'200000050','vocation':'摄影'}],'typeName':'文化传媒'},{'type':[{'code':'200000051','vocation':'影视'},{'code':'200000052','vocation':'运动体育'},{'code':'200000053','vocation':'音乐'},{'code':'200000054','vocation':'模特'}],'typeName':'娱乐体育'},{'type':[{'code':'200000010','vocation':'医疗'},{'code':'200000055','vocation':'法律'},{'code':'200000056','vocation':'教育'},{'code':'200000057','vocation':'政府机关'},{'code':'200000058','vocation':'科研'},{'code':'200000059','vocation':'公益'}],'typeName':'公共事业'},{'type':[{'code':'200000012','vocation':'学生'}],'typeName':'学生'},{'type':[{'code':'200000060','vocation':'无业游民'}],'typeName':'无业游民'}]";
		try {
			Gson gson = new Gson();
			List<OccpationType> occpationTypes = gson.fromJson(types,
					new TypeToken<List<OccpationType>>() {
					}.getType());
			return occpationTypes;
		} catch (Exception e) {
			return new ArrayList<OccpationType>();
		}
	}

	/**
	 * 更具职业编码获取职业名称
	 * @param code
	 * @return
	 */
	public String getUserOccupationName(String code) {
		List<OccpationBean> occpation = getOccpations();
		for (int i = 0; i < occpation.size(); i++) {
			if (code.equals(occpation.get(i).getCode())) {
				return occpation.get(i).getVocation();
			}
		}
		return "未知";
	}

	private List<OccpationBean> getOccpations() {
		String types = "[{'code':'300000000','vocation':'未知'},{'code':'200000001','vocation':'IT'},{'code':'200000013','vocation':'通讯'},{'code':'200000014','vocation':'电信运营'},{'code':'200000015','vocation':'网络游戏'},{'code':'200000016','vocation':'互联网'},{'code':'200000017','vocation':'投资'},{'code':'200000018','vocation':'股票/基金'},{'code':'200000019','vocation':'保险'},{'code':'200000020','vocation':'银行'},{'code':'200000021','vocation':'信托/担保'},{'code':'200000022','vocation':'咨询'},{'code':'200000023','vocation':'个体经营'},{'code':'200000024','vocation':'美容美发'},{'code':'200000025','vocation':'旅游'},{'code':'200000026','vocation':'酒店餐饮'},{'code':'200000027','vocation':'休闲娱乐'},{'code':'200000028','vocation':'贸易'},{'code':'200000029','vocation':'汽车'},{'code':'200000030','vocation':'房地产'},{'code':'200000031','vocation':'物业管理'},{'code':'200000032','vocation':'装修/装潢'},{'code':'200000033','vocation':'建筑'},{'code':'200000034','vocation':'土木工程'},{'code':'200000035','vocation':'机械制造'},{'code':'200000036','vocation':'电子'},{'code':'200000037','vocation':'生物医药'},{'code':'200000038','vocation':'食品'},{'code':'200000039','vocation':'服装'},{'code':'200000040','vocation':'能源'},{'code':'200000041','vocation':'航空'},{'code':'200000042','vocation':'铁路'},{'code':'200000043','vocation':'航运/船舶'},{'code':'200000044','vocation':'媒体出版'},{'code':'200000045','vocation':'设计'},{'code':'200000046','vocation':'文化传播'},{'code':'200000047','vocation':'广告创意'},{'code':'200000048','vocation':'动漫'},{'code':'200000049','vocation':'公关/会展'},{'code':'200000050','vocation':'摄影'},{'code':'200000051','vocation':'影视'},{'code':'200000052','vocation':'运动体育'},{'code':'200000053','vocation':'音乐'},{'code':'200000054','vocation':'模特'},{'code':'200000010','vocation':'医疗'},{'code':'200000055','vocation':'法律'},{'code':'200000056','vocation':'教育'},{'code':'200000057','vocation':'政府机关'},{'code':'200000058','vocation':'科研'},{'code':'200000059','vocation':'公益'},{'code':'200000012','vocation':'学生'},{'code':'200000060','vocation':'无业游民'}]";
		try {
			Gson gson = new Gson();
			List<OccpationBean> occpation = gson.fromJson(types,
					new TypeToken<List<OccpationBean>>() {
					}.getType());
			return occpation;
		} catch (Exception e) {
			return new ArrayList<OccpationBean>();
		}
	}

	/**
	 * 更具职业编码获取职业名称
	 * @param code
	 * @return
	 */
	public String getUserOccupationName(Context context) {
		List<OccpationBean> occpation = getOccpations();
		for (int i = 0; i < occpation.size(); i++) {
			if (getUserOccupation(context).equals(occpation.get(i).getCode())) {
				return occpation.get(i).getVocation();
			}
		}
		return "未填写";
	}

	/**
	 * 保存用户类型
	 * 
	 * @param context
	 * @param type
	 *            0-上班族 1-夜猫派 2-学生党
	 */
	public void saveUserType(Context context, int type) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putInt("user_type", type).commit();
	}

	public int getUserType(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getInt(
				"user_type", 0);
	}

	/**
	 * 保存用户职业
	 * 
	 * @param context
	 * @param profession
	 *            0-IT，1-金融，2-人事行政，3-教育法律，4-销售，5-房地产/建筑
	 *            6-文化传媒，7-物流，8-制造生产，9-医疗，10-服务业，11-学生/其他
	 */
	public void saveUserProfession(Context context, int profession) {

		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putInt("user_profession", profession).commit();
	}

	/**
	 * 获取用户职业 默认为其他
	 * 
	 * @param context
	 * @return
	 */
	public int getUserProfession(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getInt(
				"user_profession", 200000001);
	}

	public void saveRegiserReady(Context context, boolean state) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean("register_state", state).commit();
	}

	public boolean getRegiserReady(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("register_state", false);
	}

	public void saveLoginReady(Context context, boolean state) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean("login_state", state).commit();
	}

	public boolean getLoginReady(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("login_state", true);
	}

	/**
	 * 保存平台绑定信息，(1表示绑定，0表示未绑定) 0000--第一位代表微博绑定，第二位代表微信绑定，第三位代表QQ绑定，第四位代表手机绑定
	 * 
	 * @param context
	 * @param platformCode
	 */
	public void savePlatformBoundMsg(Context context, String platformCode) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("platform_msg", platformCode).commit();
	}

	/**
	 * 获取平台绑定信息，(1表示绑定，0表示未绑定) 0000--第一位代表微博绑定，第二位代表微信绑定，第三位代表QQ绑定，第四位代表手机绑定
	 * 
	 * @param context
	 * @return
	 */
	public String getPlatformBoundMsg(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("platform_msg", "0000");
	}

	/**
	 * 保存是否已登录
	 * 
	 * @param context
	 * @param isLogin
	 */
	public void saveIsLogin(Context context, boolean isLogin) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean("is_login", isLogin).commit();
	}

	/**
	 * 获取是否已登录
	 * 
	 * @param context
	 * @return
	 */
	public synchronized boolean getIsLogin(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("is_login", false);
	}

	/**
	 * 保存用户id
	 * 
	 * @param context
	 * @param userid
	 */
	public void saveUserId(Context context, String userid) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("userid", userid).commit();
	}

	/**
	 * 获取用户id
	 * 
	 * @param context
	 * @return
	 */
	public String getUserId(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("userid", "");
	}

	/**
	 * 保存用户登录平台
	 * 
	 * @param context
	 * @param userid
	 */
	public void saveUserPlatform(Context context, String userPlatform) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("userPlatform", userPlatform).commit();
	}

	/**
	 * 获取用户登录平台
	 * 
	 * @param context
	 * @return
	 */
	public String getUserPlatform(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("userPlatform", "");
	}

	/**
	 * 保存用户隐身状态
	 * 
	 * @param context
	 * @param ishide
	 */
	public void saveUserHide(Context context, boolean ishide) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean("userhide", ishide).commit();
	}

	/**
	 * 获取用户隐身状态
	 * 
	 * @param context
	 * @return
	 */
	public boolean getUserHide(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("userhide", false);
	}

	/**
	 * 保存用户昵称
	 * 
	 * @param context
	 * @param nickname
	 */
	public void saveUserNickname(Context context, String nickname) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("nickname", nickname).commit();
	}

	/**
	 * 获取用户昵称
	 * 
	 * @param context
	 * @return
	 */
	public String getUserNickname(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("nickname", "");
	}

	/**
	 * 保存用户头像地址
	 * 
	 * @param context
	 * @param url
	 */
	public void saveUserProfileUrl(Context context, String url) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("profile_url", url).commit();
	}

	/**
	 * 获取用户头像地址
	 * 
	 * @param context
	 * @return
	 */
	public String getUserProfileUrl(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("profile_url", "");
	}

	/**
	 * 保存用户头像地址key
	 * 
	 * @param context
	 * @param url
	 */
	public void saveUserProfileKey(Context context, String url) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("profile_url_key", url).commit();
	}

	/**
	 * 获取用户头像地址
	 * 
	 * @param context
	 * @return
	 */
	public String getUserProfileKey(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("profile_url_key", "");
	}

	public String getLocation_x(Context context) {
		return context
				.getApplicationContext()
				.getSharedPreferences(SleepInfo.FILENAME_USER,
						Context.MODE_APPEND)
				.getString(SleepInfo.KEY_LOCATION_X, "0");
	}

	public String getLocation_y(Context context) {
		return context
				.getApplicationContext()
				.getSharedPreferences(SleepInfo.FILENAME_USER,
						Context.MODE_APPEND)
				.getString(SleepInfo.KEY_LOCATION_Y, "0");
	}

	/**
	 * 保存未读信息数量
	 * 
	 * @param context
	 * @param count
	 */
	public void saveNoReadMessageCount(Context context, int count) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putInt("friend_noread_message_count", count).commit();
	}

	/**
	 * 获取未读信息数量
	 * 
	 * @param context
	 * @return
	 */
	public int getNoReadMessageCount(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getInt(
				"friend_noread_message_count", 0);
	}

	/**
	 * 保存好友请求信息数量
	 * 
	 * @param context
	 * @param count
	 */
	public void saveMessageCount(Context context, int count) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putInt("message_count", count).commit();
	}

	/**
	 * 获取好友请求信息数量
	 * 
	 * @param context
	 * @return
	 */
	public int getMessageCount(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getInt(
				"message_count", 0);
	}

	/**
	 * 保存未操作的专属铃音数量
	 * 
	 * @param context
	 * @param count
	 */
	public void saveOperatorReceiverCount(Context context, int count) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putInt("receiver_operator_count", count).commit();
	}

	/**
	 * 获取未操作的专属铃音数量
	 * 
	 * @param context
	 * @return
	 */
	public int getOperatorReceiverCount(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getInt(
				"receiver_operator_count", 0);
	}

	/**
	 * 保存修改用户信息状态，如果为“1”则表示修改过，但是还未在服务器修改成功。其它则表示已经成功
	 * 
	 * @param context
	 * @param state
	 */
	public void saveUpdateUserInfoState(Context context, String state) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("update_user_info_state", state).commit();
	}

	/**
	 * 获取当前用户信息是否与服务器同步成功
	 * 
	 * @param context
	 * @return
	 */
	public boolean getUpdateUserInfoState(Context context) {
		String state = PreferenceManager.getDefaultSharedPreferences(context)
				.getString("update_user_info_state", "0");
		if (!"1".endsWith(state)) {
			return true;
		}
		return false;
	}

	/**
	 * 保存波形图置零加速度
	 * 
	 * @param context
	 * @param zeroacce
	 */
	public void saveZeroAcce(Context context, float zeroacce) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putFloat("zero_acce", zeroacce).commit();
	}

	/**
	 * 获取波形图置零加速度
	 * 
	 * @param context
	 * @return
	 */
	public float getZeroAcce(Context context) {
		float zeroacce = PreferenceManager.getDefaultSharedPreferences(context)
				.getFloat("zero_acce", 0);
		return zeroacce;
	}

	/**
	 * 保存判断函数的执行状态，1为不在执行，2为正在执行
	 * 
	 * @param context
	 * @param state
	 */
	public void saveAnalysisState(Context context, String state) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("analysis_state", state).commit();
	}

	/**
	 * 获取判断函数的执行状态，1为不在执行，2为正在执行
	 * 
	 * @param context
	 * @return
	 */
	public String getAnalysisState(Context context) {
		String state = PreferenceManager.getDefaultSharedPreferences(context)
				.getString("analysis_state", "1");
		return state;
	}

	/**
	 * 保存容错进程的执行状态，1为不在执行，2为正在执行
	 * 
	 * @param context
	 * @param state
	 */
	public void saveAmendState(Context context, String state) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("amend_state", state).commit();
	}

	/**
	 * 获取容错进程的执行状态，1为不在执行，2为正在执行
	 * 
	 * @param context
	 * @return
	 */
	public String getAmendState(Context context) {
		String state = PreferenceManager.getDefaultSharedPreferences(context)
				.getString("amend_state", "1");
		return state;
	}

	/**
	 * 保存自启主进程的执行状态，1为不在执行，2为正在执行
	 * 
	 * @param context
	 * @param state
	 */
	public void saveMainState(Context context, String state) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("main_state", state).commit();
	}

	/**
	 * 获取自启主进程的执行状态，1为不在执行，2为正在执行
	 * 
	 * @param context
	 * @return
	 */
	public String getMainState(Context context) {
		String state = PreferenceManager.getDefaultSharedPreferences(context)
				.getString("main_state", "1");
		return state;
	}

	/**
	 * 保存自启主进程的执行状态，1为不在执行，2为正在执行
	 * 
	 * @param context
	 * @param state
	 */
	public void saveAccState(Context context, String state) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("acc_state", state).commit();
	}

	/**
	 * 获取自启主进程的执行状态，1为不在执行，2为正在执行
	 * 
	 * @param context
	 * @return
	 */
	public String getAccState(Context context) {
		String state = PreferenceManager.getDefaultSharedPreferences(context)
				.getString("acc_state", "1");
		return state;
	}

	/**
	 * 保存预设时间重置模块执行状态，1为不在执行，2为正在执行
	 * 
	 * @param context
	 * @param state
	 */
	public void saveSetupState(Context context, String state) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("setup_state", state).commit();
	}

	/**
	 * 获取预设时间重置模块执行状态，1为不在执行，2为正在执行
	 * 
	 * @param context
	 * @return
	 */
	public String getSetupState(Context context) {
		String state = PreferenceManager.getDefaultSharedPreferences(context)
				.getString("setup_state", "1");
		return state;
	}

	/**
	 * 保存错误类型，具体类型见枚举
	 * 
	 * @param context
	 * @param state
	 */
	public void saveFaultState(Context context, String state) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("fault_state", state).commit();
	}

	/**
	 * 获取错误类型，具体类型见枚举
	 * 
	 * @param context
	 * @return
	 */
	public String getFaultState(Context context) {
		String state = PreferenceManager.getDefaultSharedPreferences(context)
				.getString("fault_state", "0");
		return state;
	}

	/**
	 * 保存错误发生日期
	 * 
	 * @param context
	 * @param state
	 */
	public void saveFaultDate(Context context, String date) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("fault_date", date).commit();
	}

	/**
	 * 获取错误发生日期
	 * 
	 * @param context
	 * @return
	 */
	public String getFaultDate(Context context) {
		String date = PreferenceManager.getDefaultSharedPreferences(context)
				.getString("fault_date", "");
		return date;
	}

	/**
	 * 保存程序down掉的时间
	 * 
	 * @param context
	 * @param state
	 */
	@SuppressLint("SimpleDateFormat")
	public void saveDownTime(Context context, String downtime) {
		if (!TextUtils.isEmpty(downtime)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
			MytabOperate operate = new MytabOperate(
					helper.getWritableDatabase(), "test");
			ContentValues cv = new ContentValues();
			cv.put("downtime", sdf.format(new Date(Long.valueOf(downtime))));
			operate.insert(cv);
			operate.close();
		}
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("down_time", downtime).commit();
	}

	/**
	 * 获取程序down掉的时间
	 * 
	 * @param context
	 * @return
	 */
	public String getDownTime(Context context) {
		String downtime = PreferenceManager
				.getDefaultSharedPreferences(context).getString("down_time",
						"0");
		return downtime;
	}

	/**
	 * 保存程序重启掉的时间
	 * 
	 * @param context
	 * @param state
	 */
	public void saveRestartTime(Context context, String restarttime) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("restart_time", restarttime).commit();
	}

	/**
	 * 获取程序重启掉的时间
	 * 
	 * @param context
	 * @return
	 */
	public String getRestartTime(Context context) {
		String restarttime = PreferenceManager.getDefaultSharedPreferences(
				context).getString("restart_time", "0");
		return restarttime;
	}

	/**
	 * boss算法所需，保存预设新的入睡时间之前入睡的时间
	 * 
	 * @param context
	 * @param oldT1
	 *            之前的入睡时间，单位为毫秒
	 */
	public void saveOldT1(Context context, long oldT1) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putLong("old_t1", oldT1).commit();
	}

	/**
	 * boss算法所需，获取预设新的入睡时间之前入睡的时间
	 * 
	 * @param context
	 * @return oldT1 之前的入睡时间，单位为毫秒
	 */
	public long getOldT1(Context context) {
		long oldT1 = PreferenceManager.getDefaultSharedPreferences(context)
				.getLong("old_t1",
						DataUtils.getAllerateStartTime() / (60 * 1000));
		return oldT1;
	}

	/**
	 * boss算法所需，保存预设新的起床时间之前起床的时间
	 * 
	 * @param context
	 * @param oldT2
	 *            之前的入睡时间，单位为单位为毫秒
	 */
	public void saveOldT2(Context context, long oldT2) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putLong("old_t2", oldT2).commit();
	}

	/**
	 * boss算法所需，获取预设新的起床时间之前起床的时间
	 * 
	 * @param context
	 * @return oldT2 之前的入睡时间，单位为单位为毫秒
	 */
	public long getOldT2(Context context) {
		long oldT2 = PreferenceManager.getDefaultSharedPreferences(context)
				.getLong("old_t2",
						DataUtils.getAllerateStopTime() / (60 * 1000));
		return oldT2;
	}

	/**
	 * 保存3进程同步时间
	 * 
	 * @param context
	 * @param threadDownTime
	 */
	@SuppressLint("SimpleDateFormat")
	public void saveThreadDownTime(Context context, String threadDownTime) {
		if (!TextUtils.isEmpty(threadDownTime)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
			MytabOperate operate = new MytabOperate(
					helper.getWritableDatabase(), "test");
			ContentValues cv = new ContentValues();
			cv.put("downtimeinfo",
					sdf.format(new Date(Long.valueOf(threadDownTime))));
			operate.insert(cv);
			operate.close();
		}
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("threaddowntime", threadDownTime).commit();
	}

	/**
	 * 获取3进程同步时间
	 * 
	 * @param context
	 * @return
	 */
	public String getThreadDownTime(Context context) {
		String threaddowntime = PreferenceManager.getDefaultSharedPreferences(
				context).getString("threaddowntime", "0");
		return threaddowntime;
	}

	/**
	 * 保存3进程同步时间
	 * 
	 * @param context
	 * @param threadDownTime
	 */
	@SuppressLint("SimpleDateFormat")
	public void saveAccStartTime(Context context, String starttime) {
		if (!TextUtils.isEmpty(starttime)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
			MytabOperate operate = new MytabOperate(
					helper.getWritableDatabase(), "test");
			ContentValues cv = new ContentValues();
			cv.put("accstarttime",
					sdf.format(new Date(Long.valueOf(starttime))));
			operate.insert(cv);
			operate.close();
		}
	}

	/**
	 * 保存闹钟唤醒状态，0代表没有响过，1代表已经响过并选择了再过15分钟再响，2代表已经响过并选择了马上入睡
	 * 
	 * @param context
	 * @param oldT1
	 *            之前的入睡时间，单位为毫秒
	 */
	public void saveAlarmState(Context context, String alarmstate) {
		if (alarmstate.equals("0") || alarmstate.equals("1")
				|| alarmstate.endsWith("2")) {
			PreferenceManager.getDefaultSharedPreferences(context).edit()
					.putString("alarmstate", alarmstate).commit();
		}
	}

	/**
	 * 获取闹钟唤醒状态，0代表没有响过，1代表已经响过并选择了再过15分钟再响，2代表已经响过并选择了马上入睡
	 * 
	 * @param context
	 * @return oldT1 之前的入睡时间，单位为毫秒
	 */
	public String getAlarmState(Context context) {
		String alarmstate = PreferenceManager.getDefaultSharedPreferences(
				context).getString("alarmstate", "0");
		return alarmstate;
	}

	/**
	 * 保存闹钟响起时间
	 * 
	 * @param context
	 * @param threadDownTime
	 */
	public void saveAlarmTime(Context context, long alarmtime) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putLong("alarmtime", alarmtime).commit();
	}

	/**
	 * 获取闹钟响起时间
	 * 
	 * @param context
	 * @return
	 */
	public Long getAlarmTime(Context context) {
		long alarmtime = PreferenceManager.getDefaultSharedPreferences(context)
				.getLong("alarmtime", 0);
		return alarmtime;
	}

	/**
	 * 保存数据上传时间
	 * 
	 * @param context
	 * @param threadDownTime
	 */
	public void saveUploadTime(Context context, long uploadtime) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putLong("uploadtime", uploadtime).commit();
	}

	/**
	 * 获取数据上传时间
	 * 
	 * @param context
	 * @return
	 */
	public Long getUploadTime(Context context) {
		long uploadtime = PreferenceManager
				.getDefaultSharedPreferences(context).getLong("uploadtime", 0);
		return uploadtime;
	}

	/**
	 * 保存应用是否需要更新
	 * 
	 * @param context
	 * @param isUpdate
	 */
	public void saveIsUpdateVersion(Context context, boolean isUpdate) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean("update_version", isUpdate).commit();
	}

	/**
	 * 获取应用是否需要更新
	 * 
	 * @param context
	 * @return
	 */
	public boolean getIsUpdateVersion(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("update_version", false);
	}

	/**
	 * 保存version_code
	 * 
	 * @param context
	 * @param version_code
	 */
	public void saveAppVersionCode(Context context, int version_code) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putInt("version_code", version_code).commit();
	}

	/**
	 * 获取version_code
	 * 
	 * @param context
	 * @return
	 */
	public int getAppVersionCode(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getInt(
				"version_code", 0);
	}

	/**
	 * 保存是否开启消息提示声音
	 * 
	 * @param context
	 * @param isUpdate
	 */
	public void saveSoundSwitch(Context context, boolean soundSwitch) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean("sound_switch", soundSwitch).commit();
	}

	/**
	 * 获取是否开启消息提示声音
	 * 
	 * @param context
	 * @return
	 */
	public boolean getSoundSwitch(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("sound_switch", true);
	}

	/**
	 * 保存是否开启消息提示震动
	 * 
	 * @param context
	 * @param isUpdate
	 */
	public void saveVirbleSwitch(Context context, boolean virbleSwitch) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean("virble_switch", virbleSwitch).commit();
	}

	/**
	 * 获取是否开启消息提示震动
	 * 
	 * @param context
	 * @return
	 */
	public boolean getVirbleSwitch(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("virble_switch", true);
	}

	/**
	 * 保存是否注册成功
	 * 
	 * @param context
	 * @param isRegister
	 */
	public void saveIsRegisterSuccess(Context context, boolean isRegister) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean("is_register_success", isRegister).commit();
	}

	/**
	 * 获取是否注册成功
	 * 
	 * @param context
	 * @return
	 */
	public boolean getIsRegisterSuccess(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("is_register_success", false);
	}

	/**
	 * 保存是否第一次选择铃声
	 * 
	 * @param context
	 * @param isRegister
	 */
	public void saveIsFirstChooseRingtone(Context context, boolean isfirst) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean("is_first_chooseringtone", isfirst).commit();
	}

	/**
	 * 获取是否第一次选择铃声
	 * 
	 * @param context
	 * @return
	 */
	public boolean getIsFirstChooseRingtone(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("is_first_chooseringtone", false);
	}

	/**
	 * 保存是否为编辑闹钟
	 * 
	 * @param context
	 * @param isRegister
	 */
	public void saveIsEditAlarm(Context context, boolean isedit) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean("is_edit_alarm", isedit).commit();
	}

	/**
	 * 获取是否为编辑闹钟
	 * 
	 * @param context
	 * @return
	 */
	public boolean getIsEditAlarm(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("is_edit_alarm", false);
	}

	/**
	 * 保存是否显示香橙使用说明提示框
	 * 
	 * @param context
	 * @param isUpdate
	 */
	public void saveShowHelpPopupwindow(Context context, boolean showHelp) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean("show_help_pop", showHelp).commit();
	}

	/**
	 * 获取是否显示香橙使用说明提示框
	 * 
	 * @param context
	 * @return
	 */
	public boolean getShowHelpPopupwindow(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("show_help_pop", true);
	}

	/**
	 * 保存是否显示闹钟说明提示框
	 * 
	 * @param context
	 * @param isUpdate
	 */
	public void saveShowAlarmExplain(Context context, boolean showExplain) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean("show_alarm_explain", showExplain).commit();
	}

	/**
	 * 获取是否显示闹钟说明提示框
	 * 
	 * @param context
	 * @return
	 */
	public boolean getShowAlarmExplain(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("show_alarm_explain", true);
	}

	/**
	 * 保存是否显示提示发布铃声按钮
	 * 
	 * @param context
	 * @param isUpdate
	 */
	public void saveIsRemindPublishAudio(Context context, boolean showExplain) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean("show_remind_publish_audio", showExplain).commit();
	}

	/**
	 * 获取是否显示提示发布铃声按钮
	 * 
	 * @param context
	 * @return
	 */
	public boolean getIsRemindPublishAudio(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("show_remind_publish_audio", true);
	}

	public void savePhotoPathString(Context context, String PhotoPathString) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("save_photo_path", PhotoPathString).commit();
	}

	public String getPhotoPathString(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("save_photo_path", "");
	}

	/**
	 * 保存设备的mac地址，用于判断是否绑定了设备
	 * 
	 * @param context
	 * @param pillow_mac
	 */
	public void setBundbluetoothPillow(Context context, String pillow_mac) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("save_pillow_bund", pillow_mac).commit();
	}

	/**
	 * 查询手机是否绑定了设备，如果已经绑定了设备，返回设备的mac地址，如果没有绑定返回“”
	 * 
	 * @param context
	 * @return
	 */
	public String getBundbluetoothPillow(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("save_pillow_bund", "");
	}

	/**
	 * 保存设备的电量
	 * 
	 * @param context
	 * @param BatteryValue
	 */
	public void setBluetoothPillowBatteryValue(Context context,
			String BatteryValue) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("save_pillow_battery", BatteryValue).commit();
	}

	/**
	 * 获取设备的电量
	 * 
	 * @param context
	 * @return
	 */
	public String getBluetoothPillowBatteryValue(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("save_pillow_battery", "20");
	}

	/**
	 * 保存设备的硬件版本
	 * 
	 * @param context
	 * @param FirmwareVersion
	 */
	public void setBluetoothPillowFirmwareVersion(Context context,
			String FirmwareVersion) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("save_pillow_firmwareversion", FirmwareVersion)
				.commit();
	}

	/**
	 * 获取设备的硬件版本
	 * 
	 * @param context
	 * @return
	 */
	public String getBluetoothPillowFirmwareVersion(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("save_pillow_firmwareversion", "1.00.01");
	}

	/**
	 * 保存是否提醒非wifi状态
	 * 
	 * @param context
	 * @param isRemind
	 */
	public void saveRemindNotWifi(Context context, boolean isRemind) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean("is_remind_not_wifi", isRemind).commit();
	}

	/**
	 * 获取是否提醒非wifi状态
	 * 
	 * @param context
	 * @return
	 */
	public boolean getRemindNotWifi(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("is_remind_not_wifi", false);
	}

	/**
	 * 保存是否直接展示硬件睡眠数据
	 * 
	 * @param context
	 * @param isRemind
	 */
	public void saveDirectShowHardwareData(Context context, boolean isGo) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean("direct_show_hardware_data", isGo).commit();
	}

	/**
	 * 获取保存是否直接展示硬件睡眠数据
	 * 
	 * @param context
	 * @return
	 */
	public boolean getDirectShowHardwareData(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("direct_show_hardware_data", false);
	}

	/**
	 * 保存绑定的手机号码
	 * 
	 * @param context
	 * @param phone
	 */
	public void saveBoundPhoneNumber(Context context, String phone) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("bound_phone_number", phone).commit();
	}

	/**
	 * 获取绑定的手机号码
	 * 
	 * @param context
	 * @return
	 */
	public String getBoundPhoneNumber(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("bound_phone_number", "");
	}

	/**
	 * 保存登录账号，所绑定微博的好友信息
	 * 
	 * @param context
	 * @param phone
	 */
	public void saveBoundWeiboFriendsMsg(Context context, String phone) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("bound_weibo_friends_info", phone).commit();
	}

	/**
	 * 获取登录账号，所绑定微博的好友信息
	 * 
	 * @param context
	 * @return
	 */
	public String getBoundWeiboFriendsMsg(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("bound_weibo_friends_info", "");
	}
	
	/**
	 * 保存枕扣的灵敏度
	 * @param context
	 * @param sensitive
	 */
	public void setBluetoothDevSensitive(Context context, String sensitive){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("bound_bluetooth_dev_sensitive", sensitive).commit();
	}
	
	/**
	 * 获取枕扣的灵敏度
	 * @param context
	 * @return
	 */
	public String getBluetoothDevSensitive(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("bound_bluetooth_dev_sensitive", "0");
	}
	/**
	 * 保存所有的灵敏度参数
	 * @param context
	 * @param sensitive
	 */
	public void setAllSensitives(Context context, String sensitive){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("bound_bluetooth_all_sensitive", sensitive).commit();
	}
	
	/**
	 * 获取所有的灵敏度参数
	 * @param context
	 */
	public String getAllSensitives(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
		.getString("bound_bluetooth_all_sensitive", "");
	}
	
	/**
	 * 保存绑定信息用于重新上传
	 * @param context
	 * @param sensitive
	 */
	public void setBluetoothBundInfo(Context context, String info){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("bound_bluetooth_bund_info", info).commit();
	}
	/**
	 * 获取绑定信息用于重新上传
	 * @param context
	 * @return  macaddr;btime;灵敏度;用户id
	 */
	public String getBluetoothBundInfo(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("bound_bluetooth_bund_info", "");
	}
	
	/**
	 * 保存解绑定信息用于重新上传
	 * @param context
	 * @param sensitive
	 */
	public void setBluetoothUnBundInfo(Context context, String info){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("bound_bluetooth_unbund_info", info).commit();
	}
	/**
	 * 获取解绑定信息用于重新上传
	 * @param context
	 * @return  macaddr;jbtime;灵敏度;用户id
	 */
	public String getBluetoothUnBundInfo(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("bound_bluetooth_unbund_info", "");
	}

	/**
	 * 获取用户是否完善信息
	 * 
	 * @param context
	 * @return
	 */
	public boolean getIsComleteInfo(Context context) {
		String gender = getUserGender(context);
		if (gender == null || "".endsWith(gender)) {
			return false;
		}

		float height = getUserHeight(context);
		if (height <= 0) {
			return false;
		}
		float weight = getUserWeight(context);
		if (weight <= 0.0f) {
			return false;
		}
		String occupation = getUserOccupation(context);
		if ("未知".equals(getUserOccupationName(occupation))) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @Title: getNetType
	 * @Description: TODO(获取当前网络类型)
	 * @param @param context
	 * @return int 设定文件 -1 网络为没有网络 1 代表 WIFI 其他情况都属于手机网络
	 * @throws
	 */

	public static int getNetType(Context context) {
		int iRst = -1;
		ConnectivityManager connectMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info = connectMgr.getActiveNetworkInfo();

		if (info != null) {
			iRst = info.getType();
		}

		return iRst;

	}
	/**
	 * 获取第三方绑定手机状态   返回为1时，登录失败 关闭对话框
	 * 
	 * @param context
	 * @return
	 */
	public String getOtherPlatformLoginState(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("other_platform_login_end", "0");
	}

	/**
	 * 保存第三方绑定手机状态   
	 * 
	 * @param context
	 * @param state   为1时，登录失败 关闭对话框
	 */
	public void saveOtherPlatformLoginState(Context context, String state) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("other_platform_login_end", state).commit();
	}
	/**
	 * 获取生成睡眠文案的日期 和文案编号 用：分隔
	 * 
	 * @param context
	 * @return
	 */
	public String getAnalyzeSleepDocumentDate(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("sleep_analyze_doc_date", "");
	}
	
	/**
	 * 保存生成睡眠文案的日期 和文案编号 用：分隔
	 * 	日期：睡眠时长分析：睡觉时间分析:起床时间分析
	 * 
	 * @param context
	 * 
	 */
	public void saveAnalyzeSleepDocumentDate(Context context, String state) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("sleep_analyze_doc_date", state).commit();
	}
	
	/**
	 * 保存睡眠时长的分析类型id和子段小标
	 */
	public void saveSleepLengthDocDate(Context context,String text ){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("sleep_length_doc", text).commit();
	}
	
	/**
	 * 保存睡眠时长的分析类型id和子段小标
	 */
	public String getSleepLengthDocDate(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
		.getString("sleep_length_doc", "");
	}
	
	/**
	 * 保存睡眠时长的分析类型id和子段小标
	 */
	public void saveSleepDocDate(Context context,String text ){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("sleep_doc", text).commit();
	}
	
	/**
	 * 保存睡眠时长的分析类型id和子段小标
	 */
	public String getSleepDocDate(Context context){
		 return PreferenceManager.getDefaultSharedPreferences(context)
		.getString("sleep_doc", "");
	}
	
	/**
	 * 保存睡眠时长的分析类型id和子段小标
	 */
	public void saveGetUpDocDate(Context context,String text ){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("getup_doc", text).commit();
	}
	
	/**
	 * 保存睡眠时长的分析类型id和子段小标
	 */
	public String  getGetUpDocDate(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
		.getString("getup_doc", "");
	}
	
	/**
	 * 获取同步硬件睡眠数据的操作日期
	 * 
	 * @param context
	 * @return
	 */
	public String getSyncHardwareSleepDate(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("sleep_sync_data_date", "");
	}
	
	/**
	 * 保存同步硬件睡眠数据的操作日期
	 * 	日期：yyyy-MM-dd
	 * 
	 * @param context
	 * 
	 */
	public void saveSyncHardwareSleepDate(Context context, String state) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("sleep_sync_data_date", state).commit();
	}
	/**
	 * 获取是否开启论坛消息通知
	 * @param context
	 * @return 0-关闭,  1-开启
	 */
	public String getIsOpenFormuInform(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("forum_inform_message", "1");
	}
	
	/**
	 *  保存是否开启论坛消息通知
	 * @param context
	 * @param state  1-开启 ，2-关闭
	 */
	public void saveIsOpenFormuInform(Context context, String state) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("forum_inform_message", state).commit();
	}
	/**
	 * 获取接收聊天信息
	 * @param context
	 * @return 0-关闭,  1-开启
	 */
	public int getIsOpenChatInform(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getInt("chat_inform_message", 1);
	}
	
	/**
	 * 保存接收聊天信息
	 * @param context
	 * @param state  0-关闭,  1-开启
	 */
	public void saveIsOpenChatInform(Context context, int state) {
		EMChatManager.getInstance().getChatOptions().setNotifyBySoundAndVibrate(state==1);
		
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putInt("chat_inform_message", state).commit();
	}
	
	/**
	 * 保存用户的位置信息
	 * @param context
	 * @param location 当前城市  
	 */
	public void saveUserLocationCity(Context context, String city){
		
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("user_location_city", city).commit();
	}
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public String getUserLocationCity(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("user_location_city", "成都");
	}
	
	/**
	 * 保存用户的位置信息
	 * @param context
	 * @param location 当前经度
	 */
	public void saveUserLongitude(Context context, String longitude){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("user_location_longitude", longitude).commit();
	}
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public String getUserLongitude(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("user_location_longitude", "");
	}
	
	/**
	 * 保存用户的位置信息
	 * @param context
	 * @param location 当前纬度
	 */
	public void saveUserLatitude(Context context, String latitude){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("user_location_latitude", latitude).commit();
	}
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public String getUserLatitude(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("user_location_latitude", "");
	}
	
	/**
	 * 注销登录，清空登录信息
	 * @param context
	 */
	public void logoutClearLoginMsg(Context context){
		PreManager manager = PreManager.instance();
		manager.saveIsLogin(context, false);
		manager.setBluetoothPillowBatteryValue(context, "");
		manager.setBluetoothPillowFirmwareVersion(context, "");
		manager.saveUserId(context, "");
		manager.saveUserNickname(context, "");
		manager.savePlatformBoundMsg(context, "0000");
		manager.setBundbluetoothPillow(context, "");
		manager.saveUserProfileUrl(context, "");
		manager.saveUserProfileKey(context, "");
		manager.saveBoundPhoneNumber(context, "");
		manager.saveBoundWeiboFriendsMsg(context, "");
		manager.saveUserHeight(context, "0.0");
		manager.saveUserWeight(context,  "0.0");
		manager.saveUserOccupation(context, "300000000");
		manager.saveUserIsAssess(context, "0");
		manager.saveGetupTime_Setting(context, "08:00");
		manager.saveSleepTime_Setting(context, "00:30");
		JPushInterface.setAlias(context, "", new TagAliasCallback() {
			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2) {
			}
		});
	}
	
	/**
	 * 根据年龄获取推荐的目标睡眠时长
	 * @param context
	 * @return 目标时长
	 */
	@SuppressLint("SimpleDateFormat") 
	public float  getRecommendTarget(Context context){
		int birthNumber = 0;
		float target=7;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			java.util.Date d = sdf.parse(PreManager.instance().getUserBirthday(context));
			SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
			birthNumber = Integer.parseInt(sdfYear.format(d.getTime()));
		} catch (Exception e) {}
		if (birthNumber > 1000) {
			Calendar c = Calendar.getInstance();
			int i = c.get(Calendar.YEAR);
			birthNumber = i - birthNumber;
		}
		
		if (birthNumber >= 0 && birthNumber < 12) {
			target=11;
		} else if (birthNumber >= 12 && birthNumber < 18) {
			target=8;
		} else if (birthNumber >= 18 && birthNumber <55) {
			target=7.5f;
		}else if(birthNumber>=55){
			target=6;
		}else{
			target=7;
		}
		
		return target;
	}
	
	public boolean getIsUpdata(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("user_info_update",false);
	}
	
	public void setIsUpdata(Context context,boolean isUpadate){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putBoolean("user_info_update", isUpadate).commit();
	}
	
	/**
	 * 保存用户是否评估过 
	 * @param context
	 * @param assess
	 */
	public void saveUserIsAssess(Context context,String assess){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("user_is_assess", assess).commit();
	}
	
	/**
	 * 获取用户是否评估信息
	 * @param context
	 * @param assess
	 */
	public String getUserIsAssess(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
		.getString("user_is_assess", "0");
	}
	/**
	 * 保存用户打卡天数
	 * @param context
	 * @param assess
	 */
	public void saveUserDadaDays(Context context,String assess){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("user_daka_days", assess).commit();
	}
	
	/**
	 * 获取用户打卡天数
	 * @param context
	 * @param assess
	 */
	public String getUserDakaDays(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("user_daka_days", "0");
	}
	
	/** 保存是否睡眠评估*/
	public void saveIsCompleteSleepPg(Context context, boolean isEvaluated){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putBoolean("evaluated", isEvaluated).commit();
	}
	
	/**
	 *  获取是否睡眠评估
	 * @param context
	 * @return
	 */
	public boolean getIsCompleteSleepPg(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("evaluated", false);
	}
	/**
	 * 保存睡眠评估的结果
	 * @param context
	 * @param score
	 */
	public void saveSMPGResult(Context context, String score){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("smpg_result", score).commit();
	}
	/**
	 * 获取睡眠评估的结果
	 * @param context
	 * @return
	 */
	public String getSMPGRsult(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("smpg_result", "");
	}
	/**
	 * 保存睡眠习惯的结果
	 * @param context
	 * @param score
	 */
	public void saveSHXGPGResult(Context context, String score){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("shxgpg_result", score).commit();
	}
	/**
	 * 获取睡眠习惯的结果
	 * @param context
	 * @return
	 */
	public String getSHXGPGRsult(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("shxgpg_result", "");
	}
	/**
	 * 保存睡眠规律的结果
	 * @param context
	 * @param score
	 */
	public void saveSMGLPGResult(Context context, String score){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("smglpg_result", score).commit();
	}
	/**
	 * 获取睡眠规律的结果
	 * @param context
	 * @return
	 */
	public String getSMGLPGRsult(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("smglpg_result", "");
	}
	/**
	 * 保存睡眠环境的结果
	 * @param context
	 * @param score
	 */
	public void saveSMHJPGResult(Context context, String score){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("smhjpg_result", score).commit();
	}
	/**
	 * 获取睡眠环境的结果
	 * @param context
	 * @return
	 */
	public String getSMHJPGRsult(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("smhjpg_result", "");
	}
	/**
	 * 保存心理活动的结果
	 * @param context
	 * @param score
	 */
	public void saveXLHDPGResult(Context context, String score){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("xlhdpg_result", score).commit();
	}
	/**
	 * 获取心理活动的结果
	 * @param context
	 * @return
	 */
	public String getXLHDPGRsult(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("xlhdpg_result", "");
	}
	
	
	/**
	 * 保存睡眠习惯的json字符串结果
	 * @param context
	 * @param score
	 */
	public void saveJSON_SHXGPGResult(Context context, String score){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("shxgpg_json_result", score).commit();
	}
	/**
	 * 获取睡眠习惯的json字符串结果
	 * @param context
	 * @return
	 */
	public String getJSON_SHXGPGRsult(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("shxgpg_json_result", "");
	}
	/**
	 * 保存睡眠规律的json字符串结果
	 * @param context
	 * @param score
	 */
	public void saveJSON_SMGLPGResult(Context context, String score){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("smglpg_json_result", score).commit();
	}
	/**
	 * 获取睡眠规律的json字符串结果
	 * @param context
	 * @return
	 */
	public String getJSON_SMGLPGRsult(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("smglpg_json_result", "");
	}
	/**
	 * 保存睡眠环境的json字符串结果
	 * @param context
	 * @param score
	 */
	public void saveJSON_SMHJPGResult(Context context, String score){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("smhjpg_json_result", score).commit();
	}
	/**
	 * 获取睡眠环境的json字符串结果
	 * @param context
	 * @return
	 */
	public String getJSON_SMHJPGRsult(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("smhjpg_json_result", "");
	}
	/**
	 * 保存心理活动的json字符串结果
	 * @param context
	 * @param score
	 */
	public void saveJSON_XLHDPGResult(Context context, String score){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("xlhdpg_json_result", score).commit();
	}
	/**
	 * 获取心理活动的json字符串结果
	 * @param context
	 * @return
	 */
	public String getJSON_XLHDPGRsult(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("xlhdpg_json_result", "");
	}
	
	
	/**
	 * 根据生日获取出生年份
	 * @param birthDay  yyyyMMdd
	 * @return
	 * @throws ParseException 
	 */
	@SuppressLint("SimpleDateFormat") 
	public static String birthDayToAge(String birthDay) throws ParseException{
		try {
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
			
			return sdf2.format(sdf1.parse(birthDay));
		} catch (Exception e) {
			e.printStackTrace();
			return "1990";
		}
	}
	
	/**
	 * 保存修改的7日计划  显示
	 * @param context
	 * @param plan
	 */
	public void saveSleepPlan(Context context, String plan){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("save_user_sleep_plan", plan).commit();
	}
	
	/**
	 * 获取7日计划
	 * @param context
	 * @return
	 */
	public String getSleepPlan(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("save_user_sleep_plan", "");
	}

	/**
	 * 保存一天的生活习惯
	 * @param context
	 * @param habit
	 */
	public void saveUserDayHabit(Context context, String habit){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("save_user_day_habit", habit).commit();
	}
	
	/**
	 * 获取一天的生活习惯
	 * @param context
	 * @return
	 */
	public LifeHabitBean getUserDayHabit(Context context){
		try {
			String habit = PreferenceManager.getDefaultSharedPreferences(context).getString("save_user_day_habit", "");
			Gson gson = new Gson();
			return gson.fromJson(habit, LifeHabitBean.class);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 保存睡眠改善计划
	 * @param context
	 * @param plans
	 */
	public void saveJSON_SLEEP_plans(Context context, String plans){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("sleep_plans_json_", plans).commit();
	}
	/**
	 * 获取睡眠改善计划
	 * @param context
	 * @return
	 */
	public String getJSON_SLEEP_plans(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("sleep_plans_json_", "");
	}
	
	/**
	 * 保存安慰信息的停留位置
	 * @param context
	 * @param choice
	 */
	public void saveComfortChoice(Context context, String choice){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("comfort_choice", choice).commit();
	}
	/**
	 * 获取安慰信息的保存信息
	 * @param context
	 * @return
	 */
	public String getComfortChoice(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("comfort_choice", "");
	}
	/**
	 * 知识搜索记录
	 * @param context
	 * @param keywd
	 */
	public void saveSearchKey(Context context, String keywd){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("search_key_wd", keywd).commit();
	}
	
	public String getSearchKey(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("search_key_wd", "");
	}
	/**
	 * 社区搜索历史记录
	 * @param context
	 * @param keywd
	 */
	public void saveCommunitySearchKey(Context context, String keywd){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("comm_search_key_wd", keywd).commit();
	}
	
	public String getCommunitySearchKey(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("comm_search_key_wd", "");
	}
	/**
	 * 保存首页数据
	 * @param context
	 * @param response
	 */
	public void saveRecordHomeData(Context context, String response){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
		.putString("record_home_data_response", response).commit();
	} 
	/**
	 * 获取首页数据
	 * @param context
	 * @return
	 */
	public String getRecordHomeData(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("record_home_data_response", "");
	}

	/**
	 * 保存提醒需要的信息
	 * @param context
	 * @param response
	 */
	public void saveSmartRemindData(Context context, String response){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString("remind_record_data_response", response).commit();
	}

	/**
	 * 获取提醒需要的信息
	 * @param context
	 * @return
	 */
	public String getSmartRemindData(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("remind_record_data_response", "");
	}
	
}