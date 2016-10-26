package com.yzm.sleep.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.yzm.sleep.R;
import com.yzm.sleep.utils.InterFaceUtilsClass.AddDeleteFriendParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.AddFriendFromContectParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.FriendRstClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceAddDeleteFriendCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceAddFriendFromContectCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceCheckUserStatusCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDowmloadQiniuFileCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetUserInfoByIdCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceNearbyUserHideListCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceOwerSaveOtherAccURLCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceOwerSavePhoneNumberCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceSaveOtherUsersCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceSearchNickNameCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUpdateUserBasicInfoCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUpdateUserProfileCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUpdateuserNicknameCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadSleepTimeSettingCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUserProfileUpdateCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.OtherUsersParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.OtherUsersReturn4031RstClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.OtherUsersReturn4032RstClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.OwerSaveOtherAccParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.OwerSavePhoneNumberParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.OwerSearchedUserRstListClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.SearchNickNameParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UpdateUserBasicInfoParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UpdateUserNicknameParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UpdateUserProfileParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadSleepTimeSettingParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UserBasicInfoClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UserHideParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UserProfileUpdateParamClass;
import com.yzm.sleep.utils.UploadUtil.OnUploadProcessListener;

public class UserManagerProcClass extends HttpDataTranUtils {
	private static final String LOG_TAG = "UserManagerProcClass";

	private Context m_context = null;

	public UserManagerProcClass(Context context) {
		m_context = context;
	}

	// ---------------------------------------------------------------------------------------------------------------------------
	// save_user_info.php 用于保存来自第三方的用户数据及好友列表 开始

	private String SaveOtherUsersURL = g_BaseSiteURL + g_BaseSecond
			+ "save_user_info.php?";
	private InterfaceSaveOtherUsersCallBack m_InterfaceSaveOtherUserscallBack;

	public void CheckPhoneRegStat(OtherUsersParamClass otherUsersParamClass,
			InterfaceSaveOtherUsersCallBack callBack) {
		m_InterfaceSaveOtherUserscallBack = callBack;

		String friendacc_x = "";

		for (int i = 0; i < otherUsersParamClass.friendacc_x.size(); i++) {

			if (i != (otherUsersParamClass.friendacc_x.size() - 1))
				friendacc_x = friendacc_x + "friendacc_" + String.valueOf(i)
						+ "=" + otherUsersParamClass.friendacc_x.get(i) + "&";
			else {
				friendacc_x = friendacc_x + "friendacc_" + String.valueOf(i)
						+ "=" + otherUsersParamClass.friendacc_x.get(i);
			}

		}

		String newString = "";
		String strIDNickName = "";
		try {
			strIDNickName = URLEncoder.encode(
					otherUsersParamClass.my_ext_nickname, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		newString = SaveOtherUsersURL + "platform="
				+ otherUsersParamClass.platform + "&" + "my_ext_acc="
				+ otherUsersParamClass.my_ext_acc + "&" + "my_ext_unionid="
				+ otherUsersParamClass.my_ext_unionid + "&"
				+ "my_ext_nickname=" + strIDNickName + "&" + "my_ext_profile="
				+ otherUsersParamClass.my_ext_profile + "&" + "my_int_age="
				+ otherUsersParamClass.my_int_age + "&" + "my_int_gender="
				+ otherUsersParamClass.my_int_gender + "&"
				+ "my_int_occupation=" + otherUsersParamClass.my_int_occupation
				+ "&" + "friend_num=" + otherUsersParamClass.friend_num + "&"
				+ friendacc_x;
		System.out.println("save url :" + newString);
		super.requestJosnObjectData(m_context, newString);
	}

	private void SaveOtherUsersRstProc(JSONObject response) {
		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();

			if (idValue.equals("4027")) {
				m_InterfaceSaveOtherUserscallBack.onError(4027, "非法基本信息");
			} else if (idValue.equals("4028")) {
				m_InterfaceSaveOtherUserscallBack.onError(4028, "非法的平台信息");
			} else if (idValue.equals("4029")) {
				m_InterfaceSaveOtherUserscallBack.onError(4029, "非法的职业信息");
			} else if (idValue.equals("4030")) {
				m_InterfaceSaveOtherUserscallBack.onError(4030, "参数长度超限");
			} else if (idValue.equals("4031")) {

				OtherUsersReturn4031RstClass rst4031Class = new OtherUsersReturn4031RstClass();
				JSONObject obj = response.getJSONObject("user_info");
				rst4031Class.int_id = (String) obj.get("int_id").toString();
				rst4031Class.nickname = (String) obj.get("nickname").toString();

				String age = obj.get("user_internal_birthday").toString();
				rst4031Class.user_internal_birthday = age.length() < 8 ? age + "0101" : age;
				
				rst4031Class.gender = (String) obj.get("gender").toString();
				rst4031Class.occupation = (String) obj.get("occupation")
						.toString();
				rst4031Class.profile = (String) obj.get("profile").toString();
				rst4031Class.profile_key = (String) obj.get("profile_key")
						.toString();
				rst4031Class.user_ext_acc_qq = (String) obj.get(
						"user_ext_acc_qq").toString();
				rst4031Class.user_ext_acc_weibo = (String) obj.get(
						"user_ext_acc_weibo").toString();
				rst4031Class.user_ext_acc_wechat = (String) obj.get(
						"user_ext_acc_wechat").toString();
				rst4031Class.user_ext_acc_cellphone = (String) obj.get(
						"user_ext_acc_cellphone").toString();
				rst4031Class.user_ext_acc_cellphone_new = (String) obj.get(
						"user_ext_acc_cellphone_new").toString();
				rst4031Class.isaddinfo = (String) obj.get("isaddinfo")
						.toString();
				rst4031Class.user_internal_height = obj.getString(
						"user_internal_height").toString();
				rst4031Class.user_internal_weight = obj.getString(
						"user_internal_weight").toString();
				rst4031Class.sleeppg = obj.getString(
						"sleep_pg").toString();//==睡眠评估
				m_InterfaceSaveOtherUserscallBack.onReturn4031(rst4031Class);

			} else if (idValue.equals("4032")) {
				OtherUsersReturn4032RstClass rst4032Class = new OtherUsersReturn4032RstClass();
				JSONObject obj = response.getJSONObject("user_info");
				rst4032Class.int_id = (String) obj.get("int_id").toString();
				rst4032Class.nickname = (String) obj.get("nickname").toString();
				String age = obj.get("user_internal_birthday").toString();
				rst4032Class.user_internal_birthday = age.length() < 8 ? age + "0101" : age;
				
				rst4032Class.gender = (String) obj.get("gender").toString();
				rst4032Class.occupation = (String) obj.get("occupation")
						.toString();
				rst4032Class.profile = (String) obj.get("profile").toString();
				rst4032Class.profile_key = (String) obj.get("profile_key")
						.toString();
				rst4032Class.user_ext_acc_qq = (String) obj.get(
						"user_ext_acc_qq").toString();
				rst4032Class.user_ext_acc_weibo = (String) obj.get(
						"user_ext_acc_weibo").toString();
				rst4032Class.user_ext_acc_wechat = (String) obj.get(
						"user_ext_acc_wechat").toString();
				rst4032Class.user_ext_acc_cellphone = (String) obj.get(
						"user_ext_acc_cellphone").toString();
				rst4032Class.user_ext_acc_cellphone_new = (String) obj.get(
						"user_ext_acc_cellphone_new").toString();
				rst4032Class.isaddinfo = (String) obj.get("isaddinfo")
						.toString();
				rst4032Class.user_internal_height = obj.getString(
						"user_internal_height").toString();
				rst4032Class.user_internal_weight = obj.getString(
						"user_internal_weight").toString();
				m_InterfaceSaveOtherUserscallBack.onReturn4032(rst4032Class);

			} else if (idValue.equals("4032:2") || idValue.equals("4032:3")) {
				m_InterfaceSaveOtherUserscallBack.onError(40322, "注册失败：聊天注册失败");

			} else if (idValue.equals("4032:3")) {
				m_InterfaceSaveOtherUserscallBack.onError(40323, "注册失败：聊天无响应");

			} else {
				m_InterfaceSaveOtherUserscallBack.onError(0, m_context.getResources().getString(R.string.net_requet_error_tip));
			}

		} catch (Exception e) {
			m_InterfaceSaveOtherUserscallBack.onError(0, m_context.getResources().getString(R.string.net_requet_exception_tip));
			e.printStackTrace();
		}
	}

	// // save_user_info.php 用于保存来自第三方的用户数据及好友列表 结束

	// ---------------------------------------------------------------------------------------------------------------------------

	// trans_platform.php 用于已有账户绑定微博、qq、微信号 开始

	private String OwerSaveOtherAccURL = g_BaseSiteURL + g_BaseSecond
			+ "trans_platform_third.php?";
	private InterfaceOwerSaveOtherAccURLCallBack m_InterfaceOwerSaveOtherAccURLCallBack;

	public void OwerSaveOtherAcc(OwerSaveOtherAccParamClass owerSaveParam,
			InterfaceOwerSaveOtherAccURLCallBack callBack) {
		m_InterfaceOwerSaveOtherAccURLCallBack = callBack;
		String friendacc_x = "";
		for (int i = 0; i < owerSaveParam.friendacc_x.size(); i++) {

			if (i != (owerSaveParam.friendacc_x.size() - 1))
				friendacc_x = friendacc_x + "friendacc_" + String.valueOf(i)
						+ "=" + owerSaveParam.friendacc_x.get(i) + "&";
			else {
				friendacc_x = friendacc_x + "friendacc_" + String.valueOf(i)
						+ "=" + owerSaveParam.friendacc_x.get(i);
			}
		}

		String newString = OwerSaveOtherAccURL + "target_int_id="
				+ owerSaveParam.target_int_id + "&" + "platform="
				+ owerSaveParam.platform + "&" + "my_ext_acc="
				+ owerSaveParam.my_ext_acc + "&" + "friend_num="
				+ owerSaveParam.friend_num + "&" + friendacc_x;

		super.requestJosnObjectData(m_context, newString);

	};

	private void OwerSaveOtherAccRstProc(JSONObject response) {
		String idValue = "";
		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4051")) {

				m_InterfaceOwerSaveOtherAccURLCallBack.onError(4060, "非法基本信息");

			} else if (idValue.equals("4052")) {
				m_InterfaceOwerSaveOtherAccURLCallBack.onError(4052, "非法的平台信息");
			} else if (idValue.equals("4053")) {
				m_InterfaceOwerSaveOtherAccURLCallBack.onError(4053, "参数长度超限");
			} else if (idValue.equals("4054:1")) {
				m_InterfaceOwerSaveOtherAccURLCallBack.onError(4054,
						"该第三方平台已经与其他账户绑定过了");
			} else if (idValue.equals("4054:2")) {
				m_InterfaceOwerSaveOtherAccURLCallBack.onError(4054,
						"该第三方平台已经与该账户绑定过了");
			} else if (idValue.equals("4054:3")) {
				m_InterfaceOwerSaveOtherAccURLCallBack.onError(4054,
						"该目标账号已经绑定过一次该第三方平台的某账号，不能重复绑定");
			} else if (idValue.equals("4055")) {

				String strInt_addedSum = (String) response.get(
						"num_friend_added").toString();
				m_InterfaceOwerSaveOtherAccURLCallBack.onSuccess(4055,
						strInt_addedSum);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// trans_platform.php 用于已有账户绑定微博、qq、微信号 结束

	// ---------------------------------------------------------------------------------------------------------------------------

	// trans_platform_phone.php 用于完成对手机号的验证，并使将手机号绑定到某一账户上 开始

	private String OwerSavePhoneURL = g_BaseSiteURL + g_BaseSecond
			+ "trans_platform_phone.php?";
	private InterfaceOwerSavePhoneNumberCallBack m_InterfaceOwerSavePhoneNumberCallBack;

	public void RegUserByPhone(OwerSavePhoneNumberParamClass mPhoneParam,
			InterfaceOwerSavePhoneNumberCallBack callBack) {

		m_InterfaceOwerSavePhoneNumberCallBack = callBack;
		String newString = OwerSavePhoneURL + "my_phone_num="
				+ mPhoneParam.my_phone_num + "&" + "verification_code="
				+ mPhoneParam.verification_code + "&" + "target_int_id="
				+ mPhoneParam.target_int_id;

		super.requestJosnObjectData(m_context, newString);

	};

	private void OwerSavePhoneNumberRstProc(JSONObject response) {

		String idValue = "";
		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4071")) {
				// response：4062 invalid basic info 基本信息为空或手机号位数不对
				m_InterfaceOwerSavePhoneNumberCallBack.onError(4071,
						"目标ID不存在或为空");

			} else if (idValue.equals("4072")) {

				// response：4063 invalid occupation code 职业编号错误
				m_InterfaceOwerSavePhoneNumberCallBack
						.onError(4072, "电话号码位数不对");

			} else if (idValue.equals("4073")) {
				// response：4064 invalid verification code length 验证码位数不对
				m_InterfaceOwerSavePhoneNumberCallBack.onError(4073, "验证码位数不对");

			} else if (idValue.equals("4074")) {

				// response：4065 user nickname too long 用户昵称长度超限
				m_InterfaceOwerSavePhoneNumberCallBack.onError(4074,
						"该手机号已经注册过或与其他账户绑定过了");

			} else if (idValue.equals("4075")) {

				// response：4066 user age/gender/profile too long 年龄、性别、头像地址长度超限
				m_InterfaceOwerSavePhoneNumberCallBack.onError(4075,
						"该手机号与验证码不匹配");

			} else if (idValue.equals("4076")) {
				// response：4067 duplicate number 该手机号已经注册过或与其他账户绑定过了
				m_InterfaceOwerSavePhoneNumberCallBack.onError(4076, "验证码过期");

			} else if (idValue.equals("4077")) {
				// response：4068 cellphone number and verification code not
				// match
				// 该手机号与验证码不匹配
				m_InterfaceOwerSavePhoneNumberCallBack.onError(4077,
						"该目标账号已经绑定过一次手机号，不能重复绑定");

			} else if (idValue.equals("4078")) {
				// response：4069 verification code expires 验证码过期
				m_InterfaceOwerSavePhoneNumberCallBack.onSuccess(4078);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// trans_platform_phone.php 用于完成对手机号的验证，并使将手机号绑定到某一账户上 结束

	// ---------------------------------------------------------------------------------------------------------------------------

	// user_search.php 用于搜索用户 开始

	private String SearchNickNameURL = g_BaseSiteURL + g_BaseSecond
			+ "user_search.php?";
	private InterfaceSearchNickNameCallBack m_InterfaceSearchNickNameCallBack;

	public void SearchByNickName(SearchNickNameParamClass mPhoneParam,
			InterfaceSearchNickNameCallBack callBack) {

		String strID = "";
		try {
			strID = URLEncoder.encode(mPhoneParam.search_key_word, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		m_InterfaceSearchNickNameCallBack = callBack;
		String newString = SearchNickNameURL + "search_key_word=" + strID + "&"
				+ "my_int_id=" + mPhoneParam.my_int_id;

		super.requestJosnObjectData(m_context, newString);

	};

	private void SearchByNickNameRstProc(JSONObject response) {

		String idValue = "";
		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4087")) {

				m_InterfaceSearchNickNameCallBack.onError(4087,
						"错误：用户ID不存在或为空，搜索关键词为空");

			} else if (idValue.equals("4088")) {

				m_InterfaceSearchNickNameCallBack.onError(4088, "该手机号不存在系统中");

			} else if (idValue.equals("4089")) {

				Gson gson = new Gson();
				String listV = (String) response.get("search_list").toString();
				List<OwerSearchedUserRstListClass> ps = gson.fromJson(listV,
						new TypeToken<List<OwerSearchedUserRstListClass>>() {
						}.getType());
				m_InterfaceSearchNickNameCallBack.onSuccess(4089, ps);

			} else if (idValue.equals("4090")) {

				m_InterfaceSearchNickNameCallBack.onError(4090, "该昵称没有匹配的用户");

			} else if (idValue.equals("4091")) {

				Gson gson = new Gson();
				String listV = (String) response.get("search_list").toString();
				List<OwerSearchedUserRstListClass> ps = gson.fromJson(listV,
						new TypeToken<List<OwerSearchedUserRstListClass>>() {
						}.getType());
				m_InterfaceSearchNickNameCallBack.onSuccess(4091, ps);
				// m_InterfaceSearchNickNameCallBack.onError(4091,
				// "该手机号与验证码不匹配");

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// user_search.php 用于搜索用户 结束
	// /////////////////////////////////////////////////////////////////////////////////////////////////

	// add_phone_contact.php 用于将用户的通讯录用户直接添加成好友 开始

	private String AddFriendFromContectURL = g_BaseSiteURL + g_BaseSecond
			+ "add_phone_contact.php?";
	private InterfaceAddFriendFromContectCallBack m_InterfaceAddFriendFromContectCallBack;

	public void AddFriendFromContect(AddFriendFromContectParamClass mParam,
			InterfaceAddFriendFromContectCallBack callBack) {

		m_InterfaceAddFriendFromContectCallBack = callBack;

		String friendacc_x = "";

		for (int i = 0; i < mParam.friendacc_x.size(); i++) {

			if (i != (mParam.friendacc_x.size() - 1))
				friendacc_x = friendacc_x + "friendacc_" + String.valueOf(i)
						+ "=" + mParam.friendacc_x.get(i) + "&";
			else {
				friendacc_x = friendacc_x + "friendacc_" + String.valueOf(i)
						+ "=" + mParam.friendacc_x.get(i);
			}

		}

		String newString = AddFriendFromContectURL + "my_int_id="
				+ mParam.my_int_id + "&" + "friend_num=" + mParam.friend_num
				+ "&" + friendacc_x;

		super.requestJosnObjectData(m_context, newString);

	};

	private void AddFriendFromContectRstProc(JSONObject response) {

		String idValue = "";
		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4092")) {

				m_InterfaceAddFriendFromContectCallBack.onError(4092,
						"本人ID不存在或为空！或基本参数格式错误");

			} else if (idValue.equals("4093")) {

				String strInt_addedSum = (String) response.get(
						"num_friend_added").toString();
				String strContact_added = (String) response
						.get("contact_added").toString();
				FriendRstClass m_FriendRstClass = new FriendRstClass();
				m_FriendRstClass.num_friend_added = strInt_addedSum;
				m_FriendRstClass.contact_added = strContact_added;

				m_InterfaceAddFriendFromContectCallBack.onSuccess(4093,
						m_FriendRstClass);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block

			m_InterfaceAddFriendFromContectCallBack.onError(3002,
					e.getMessage());

			e.printStackTrace();
		}
	}

	// add_phone_contact.php 用于将用户的通讯录用户直接添加成好友 结束
	// //////////////////////////////////////////////////////////////////////////////////////////////////

	// update_user_nickname.php 用于更新用户昵称
	private String UpdateUserNicknameURL = g_BaseSiteURL + g_BaseSecond
			+ "update_user_nickname.php?";
	private InterfaceUpdateuserNicknameCallBack m_InterfaceUpdateuserNicknameCallBack;

	public void UpdateUserNickname(UpdateUserNicknameParamClass mParm,
			InterfaceUpdateuserNicknameCallBack callBack) {
		m_InterfaceUpdateuserNicknameCallBack = callBack;

		String newString = UpdateUserNicknameURL + "my_int_id="
				+ mParm.my_int_id + "&my_int_nickname=" + mParm.my_int_nickname;

		super.requestJosnObjectData(m_context, newString);

	};

	private void UpdateUserNicknameRstProc(JSONObject response) {

		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4117")) {
				m_InterfaceUpdateuserNicknameCallBack.onErrorListener(4117,
						"失败：用户ID不存在或格式错误");
			} else if (idValue.equals("4118")) {
				m_InterfaceUpdateuserNicknameCallBack.onErrorListener(4118,
						"失败：新昵称包括非法字符");

			} else if (idValue.equals("4119")) {
				m_InterfaceUpdateuserNicknameCallBack
						.onSuccess(4119, "成功：更新昵称");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ==========================================================================================
	// user_profile_update.php

	private String UserProfileUpdateURL = g_BaseSiteURL + g_BaseSecond
			+ g_BaseThread + "user_profile_update.php?";
	private InterfaceUserProfileUpdateCallBack m_InterfaceUserProfileUpdateCallBack;

	public void UserProfileUpdate(UserProfileUpdateParamClass mParm,
			InterfaceUserProfileUpdateCallBack callBack) {
		m_InterfaceUserProfileUpdateCallBack = callBack;

		String newString = UserProfileUpdateURL + "my_int_id="
				+ mParm.my_int_id + "&profile_key=" + mParm.profile_key;

		super.requestJosnObjectData(m_context, newString);

	};

	private void UserProfileUpdateRstProc(JSONObject response) {

		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4367")) {
				m_InterfaceUserProfileUpdateCallBack.onError(4367,
						"my_int_id不存在");
			} else if (idValue.equals("4368")) {
				m_InterfaceUserProfileUpdateCallBack.onError(4368,
						"profile_key为空");

			} else if (idValue.equals("4369:0")) {
				m_InterfaceUserProfileUpdateCallBack.onError(43690, "更新失败");

			} else if (idValue.equals("4369")) {
				String profileString = response.get("profile").toString();
				m_InterfaceUserProfileUpdateCallBack.onSuccess(4369,
						profileString);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 根据key 下载qiniu 图片 地址
	 */
	private String dowmloadQiniuFile = g_BaseSiteURL + g_BaseSecond
			+ g_BaseThread + "download_qiniu_file.php?";
	private InterfaceDowmloadQiniuFileCallBack m_InterfaceDowmloadQiniuFileCallBack;

	public void dowmloadQiniuFile(String key,
			InterfaceDowmloadQiniuFileCallBack callBack) {

		m_InterfaceDowmloadQiniuFileCallBack = callBack;
		String newString = dowmloadQiniuFile + "download_file_key=" + key;
		super.requestJosnObjectData(m_context, newString);

	};

	private void dowmloadQiniuFileProc(JSONObject response) {

		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4325")) {
				String profileString = response.get("downloadurl").toString();
				m_InterfaceDowmloadQiniuFileCallBack.onSuccess(4369,
						profileString);
			} else {
				m_InterfaceDowmloadQiniuFileCallBack.onError(4324, "key不存在");
			}

		} catch (Exception e) {
			m_InterfaceDowmloadQiniuFileCallBack.onError(4324, "其他错误");
		}

	}

	// ////////////////////////////////////old/////////////////////////////////////////////////////////////

	// update_user_profile.php 用于更新用户的头像

	private String UpdateUserProfileURL = g_BaseSiteURL + g_BaseSecond
			+ "update_user_profile.php";
	private InterfaceUpdateUserProfileCallBack m_InterfaceUpdateUserProfileCallBack;

	public void UpdateUserProfile(UpdateUserProfileParamClass mParam,
			InterfaceUpdateUserProfileCallBack callBack) {

		m_InterfaceUpdateUserProfileCallBack = callBack;

		// super.requestJosnObjectData(m_context, newString);

		Map<String, String> map = new HashMap<String, String>();

		map.put("my_int_id", mParam.my_int_id);

		UploadUtil.getInstance().uploadFile(mParam.strPathString, "file",
				UpdateUserProfileURL, map, new OnUploadProcessListener() {

					@Override
					public void onUploadProcess(int uploadSize) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onUploadDone(int responseCode, String message) {
						// TODO Auto-generated method stub

						try {

							JSONObject response = new JSONObject(message);
							UpdateUserProfileRstProc(response);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							ProcJSONDataOnErr(null);
							e.printStackTrace();
						}

					}

					@Override
					public void initUpload(int fileSize) {
						// TODO Auto-generated method stub

					}

					@Override
					public void UploadError(int responseCode, String message) {
						// TODO Auto-generated method stub
						ProcJSONDataOnErr(null);
					}
				});

		// super.requestJosnObjectData(m_context, newString);

	};

	private void UpdateUserProfileRstProc(JSONObject response) {

		String idValue = "";
		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4120")) {

				m_InterfaceUpdateUserProfileCallBack.onError(4120,
						"失败：用户ID不存在或格式错误	");

			} else if (idValue.equals("4121:0")) {

				m_InterfaceUpdateUserProfileCallBack.onError(41210,
						"失败：文件超过300kB的限制");

			} else if (idValue.equals("4121:1")) {

				m_InterfaceUpdateUserProfileCallBack.onError(41211,
						"失败：上传的文件超过了 php.ini 中 upload_max_filesize 选项限制的值");

			} else if (idValue.equals("4121:2")) {

				m_InterfaceUpdateUserProfileCallBack.onError(41212,
						"失败：上传文件的大小超过了 HTML 表单中 MAX_FILE_SIZE 选项指定的值");

			} else if (idValue.equals("4121:3")) {

				m_InterfaceUpdateUserProfileCallBack.onError(41213,
						"失败：文件只有部分被上传");

			} else if (idValue.equals("4121:4")) {

				m_InterfaceUpdateUserProfileCallBack.onError(41214,
						"失败：没有文件被上传");

			} else if (idValue.equals("4122")) {

				String strPathString = (String) response.get("profile_url")
						.toString();
				m_InterfaceUpdateUserProfileCallBack.onSuccess(4122,
						strPathString);

			} else if (idValue.equals("4123")) {

				m_InterfaceUpdateUserProfileCallBack.onError(4123, "失败：其他错误");

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block

			m_InterfaceUpdateUserProfileCallBack.onError(3002, e.getMessage());

			e.printStackTrace();
		}
	}

	// add_phone_contact.php 用于将用户的通讯录用户直接添加成好友 结束

	// ===========================================================================
	// user_info_by_id.php 返回指定用户id的用户相关信息
	private String GetUserBasicInfoURL = g_BaseSiteURL + g_BaseSecond
			+ g_BaseThread + "user_info_by_id.php?";
	private InterfaceGetUserInfoByIdCallBack m_InterfaceGetUserInfoByIdCallBack;

	public void GetUserBasicInfoByID(String my_int_id,
			InterfaceGetUserInfoByIdCallBack callBack) {
		m_InterfaceGetUserInfoByIdCallBack = callBack;

		String newString = GetUserBasicInfoURL + "my_int_id=" + my_int_id;

		super.requestJosnObjectData(m_context, newString);

	};

	private void GetUserBasicInfoRstProc(JSONObject response) {

		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4383")) {
				m_InterfaceGetUserInfoByIdCallBack.onError(4383,
						"失败：用户ID不存在或格式错误");
			} else if (idValue.equals("4384")) {

				Gson gson = new Gson();
				String listV = (String) response.get("datas").toString();
				// List<UserBasicInfoClass> m_RstClass = gson.fromJson(listV,
				// new TypeToken<List<UserBasicInfoClass>>(){}.getType());
				UserBasicInfoClass m_RstClass = gson.fromJson(listV,
						new TypeToken<UserBasicInfoClass>() {
						}.getType());

				m_InterfaceGetUserInfoByIdCallBack.onSuccess(4384, m_RstClass);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ==========================================================================================
	// user_set_date.php 用户设置睡觉，起床时间
	private String UploadUserSleepTimeSettingURL = g_BaseSiteURL + g_BaseSecond
			+ g_BaseThread + "user_set_date.php?";
	private InterfaceUploadSleepTimeSettingCallBack m_InterfaceUploadSleepTimeSettingCallBack;

	public void UploadSleepTimeSetting(UploadSleepTimeSettingParamClass mParam,
			InterfaceUploadSleepTimeSettingCallBack callBack) {
		m_InterfaceUploadSleepTimeSettingCallBack = callBack;

		String newString = UploadUserSleepTimeSettingURL + "my_int_id="
				+ mParam.my_int_id + "&sleep_date=" + mParam.sleep_date
				+ "&=wakeup_date" + mParam.wakeup_date;

		super.requestJosnObjectData(m_context, newString);

	}

	private void UploadUserSleepTimeSettingRstProc(JSONObject response) {

		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4405")) {
				m_InterfaceUploadSleepTimeSettingCallBack.onError(4405,
						"登录用户ID不存在或者格式错误");
			} else if (idValue.equals("4406")) {
				m_InterfaceUploadSleepTimeSettingCallBack.onError(4406,
						"起床时间和睡觉时间不能为空");
			} else if (idValue.equals("4407")) {
				m_InterfaceUploadSleepTimeSettingCallBack.onError(4407, "操作失败");
			} else if (idValue.equals("4408")) {

				m_InterfaceUploadSleepTimeSettingCallBack.onSuccess(4408,
						"操作成功");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ==========================================================================================
	// user_set_date.php 用户设置附近排行隐身
	private String userHideSettingURL = g_BaseSiteURL + g_BaseSecond
			+ g_BaseThread + "user_hiding_set.php?";
	private InterfaceNearbyUserHideListCallBack m_InterfaceNearbyUserHideListCallBack;

	public void UserHideSetting(UserHideParamClass mParam,
			InterfaceNearbyUserHideListCallBack callBack) {
		m_InterfaceNearbyUserHideListCallBack = callBack;

		String newString = userHideSettingURL + "my_int_id=" + mParam.my_int_id
				+ "&hiding=" + mParam.hiding;

		super.requestJosnObjectData(m_context, newString);

	}

	private void UserHideSettingRstProc(JSONObject response) {
		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4409")) {
				m_InterfaceNearbyUserHideListCallBack.onError(4409,
						"用户ID不存在或格式错误");
			} else if (idValue.equals("4410")) {
				m_InterfaceNearbyUserHideListCallBack.onError(4410, "其他错误");
			} else if (idValue.equals("4411")) {
				m_InterfaceNearbyUserHideListCallBack.onSuccess(4411, "成功");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ==========================================================================================
	// check_friend_status.php 检查用户之间的好友关系
	private String checkUserStatusURL = URLUtil.CHECK_FRIEND_STATUS_URL;
	private InterfaceCheckUserStatusCallBack m_InterfaceCheckUserStatusCallBack;

	public void checkUserStatus(AddDeleteFriendParamClass mParam,
			InterfaceCheckUserStatusCallBack callBack, int type) {
		m_InterfaceCheckUserStatusCallBack = callBack;
		RequestParams params = new RequestParams();
		params.put("my_int_id", mParam.my_int_id);
		params.put("friend_int_id", mParam.friend_int_id);
		super.postJosnObjectData(m_context, checkUserStatusURL, params);

	}

	private void checkUserStatusRstProc(JSONObject response) {
		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4038")) {
				m_InterfaceCheckUserStatusCallBack.onResult(4038, "基础参数非法");
			} else if (idValue.equals("4039")) {
				m_InterfaceCheckUserStatusCallBack.onResult(4039, "两者已经是好友关系");
			} else if (idValue.equals("4040")) {
				m_InterfaceCheckUserStatusCallBack.onResult(4040,
						"已经提交过申请，对方还未通过申请");
			} else if (idValue.equals("4041")) {
				m_InterfaceCheckUserStatusCallBack.onResult(4041,
						"对方向自己提交过好友申请，等待自己的确认");
			} else if (idValue.equals("4042")) {
				m_InterfaceCheckUserStatusCallBack.onResult(4042, "不是好友关系");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ==========================================================================================
	// add_delete_friend.php 用于添加好友，确认好友申请，删除好友

	// private String AddDeleteFriendURL = g_BaseSiteURL
	// +"add_delete_friend.php?";
	private String AddDeleteFriendURL = URLUtil.ADD_DELETE_FRIEND_URL;
	private InterfaceAddDeleteFriendCallBack m_InterfaceAddDeleteFriendCallBack;

	/**
	 * 
	 * @param mParam
	 * @param callBack
	 * @param type
	 *            -1：代表不传, 1：手机号, 2:微博
	 */
	public void AddDeleteFriend(AddDeleteFriendParamClass mParam,
			InterfaceAddDeleteFriendCallBack callBack, int type) {
		m_InterfaceAddDeleteFriendCallBack = callBack;

		RequestParams params = new RequestParams();
		params.put("my_int_id", mParam.my_int_id);
		params.put("friend_int_id", mParam.friend_int_id);
		params.put("operation_type", mParam.operation_type);
		if (type == 1) {
			params.put("laizi", "phone");
		} else if (type == 2) {
			params.put("laizi", "weibo");
		}
		super.postJosnObjectData(m_context, AddDeleteFriendURL, params);

	}

	private void AddDeleteFriendRstProc(JSONObject response) {
		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4008")) {
				m_InterfaceAddDeleteFriendCallBack.onError(4008,
						"对方已经是您的好友不能重复申请");
			} else if (idValue.equals("4009")) {
				m_InterfaceAddDeleteFriendCallBack.onError(4009,
						"好友申请已提交，等待对方确认");
			} else if (idValue.equals("4010")) {
				m_InterfaceAddDeleteFriendCallBack.onSuccess(4010, "你们已经是朋友啦");
			} else if (idValue.equals("4011")) {
				m_InterfaceAddDeleteFriendCallBack.onSuccess(4011, "已成功删除好友");
			} else if (idValue.equals("4012")) {
				m_InterfaceAddDeleteFriendCallBack.onError(4012, "两者非好友关系无法删除");
			} else if (idValue.equals("4013")) {
				m_InterfaceAddDeleteFriendCallBack.onError(4013,
						"好友申请已过期，已经是好友关系");
			} else if (idValue.equals("4014")) {
				m_InterfaceAddDeleteFriendCallBack.onSuccess(4014,
						"你已经申请对方为好友，请耐心等待对方的确认！");
			} else if (idValue.equals("4015")) {
				m_InterfaceAddDeleteFriendCallBack.onError(4015, "好友申请不存在");
			} else if (idValue.equals("4016")) {
				m_InterfaceAddDeleteFriendCallBack
						.onError(4016, "基本参数非法或ID不存在");
			} else if (idValue.equals("4046")) {
				m_InterfaceAddDeleteFriendCallBack.onSuccess(4046, "成功拒绝好友申请");
			} else if (idValue.equals("4047")) {
				m_InterfaceAddDeleteFriendCallBack
						.onError(4047, "好友申请不存在！无法拒绝");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// =====================================================================================================
	// 更新用户基本信息 update_user_info.php
	private String UpdateUserBasicInfoURL = g_BaseSiteURL + g_BaseSecond
			+ "update_user_info.php?";
	private InterfaceUpdateUserBasicInfoCallBack m_InterfaceUpdateUserBasicInfoCallBack;

	public void UpdateUserBasicInfo(UpdateUserBasicInfoParamClass mParam,
			InterfaceUpdateUserBasicInfoCallBack callBack) {
		m_InterfaceUpdateUserBasicInfoCallBack = callBack;

		String newString = UpdateUserBasicInfoURL + "my_int_id="
				+ mParam.my_int_id + "&my_int_age=" + mParam.my_int_age
				+ "&my_int_gender=" + mParam.my_int_gender
				+ "&my_int_occupation=" + mParam.my_int_occupation;
		super.requestJosnObjectData(m_context, newString);

	}

	private void UpdateUserBasicInfoRstProc(JSONObject response) {
		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4048")) {
				m_InterfaceUpdateUserBasicInfoCallBack.onError(4048, "基本信息不合法");
			} else if (idValue.equals("4049")) {
				m_InterfaceUpdateUserBasicInfoCallBack.onError(4049, "用户ID不存在");
			} else if (idValue.equals("4050")) {
				m_InterfaceUpdateUserBasicInfoCallBack
						.onSuccess(4050, "更新信息成功");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ===========================================================================
	// ===========================================================================

	public void ProcJSONData(JSONObject response) {

		String idValue;
		try {

			idValue = (String) response.get("response").toString();
			if (idValue.equals("4038") || idValue.equals("4039")
					|| idValue.equals("4041") || idValue.equals("4042")) {
				if (m_InterfaceCheckUserStatusCallBack != null) {
					checkUserStatusRstProc(response);
				}
				;
			} else if (idValue.equals("4083") || idValue.equals("4084")
					|| idValue.equals("4085") || idValue.equals("4086")) {
				if (m_InterfaceSaveOtherUserscallBack != null) {
					SaveOtherUsersRstProc(response);
				}
				;
			} else if (idValue.equals("4051") || idValue.equals("4052")
					|| idValue.equals("4053") || idValue.equals("4054")
					|| idValue.equals("4054:1") || idValue.equals("4054:2")
					|| idValue.equals("4054:3") || idValue.equals("4055")) {
				if (m_InterfaceOwerSaveOtherAccURLCallBack != null)
					OwerSaveOtherAccRstProc(response);
			} else if (idValue.equals("4071") || idValue.equals("4072")
					|| idValue.equals("4073") || idValue.equals("4074")
					|| idValue.equals("4075") || idValue.equals("4076")
					|| idValue.equals("4077") || idValue.equals("4078")) {
				if (m_InterfaceOwerSavePhoneNumberCallBack != null)
					OwerSavePhoneNumberRstProc(response);
			} else if (idValue.equals("4087") || idValue.equals("4088")
					|| idValue.equals("4089") || idValue.equals("4090")
					|| idValue.equals("4091")) {
				if (m_InterfaceSearchNickNameCallBack != null)
					SearchByNickNameRstProc(response);
			} else if (idValue.equals("4027") || idValue.equals("4028")
					|| idValue.equals("4029") || idValue.equals("4030")
					|| idValue.equals("4031") || idValue.equals("4032")
					|| idValue.equals("4032:2") || idValue.equals("4032:3")
					|| idValue.equals("4032:4")) {
				if (m_InterfaceSaveOtherUserscallBack != null)
					SaveOtherUsersRstProc(response);
			} else if (idValue.equals("4092") || idValue.equals("4093")) {
				if (m_InterfaceAddFriendFromContectCallBack != null)
					AddFriendFromContectRstProc(response);
			} else if (idValue.equals("4120") || idValue.equals("4121:0")
					|| idValue.equals("4121:1") || idValue.equals("4121:2")
					|| idValue.equals("4121:3") || idValue.equals("4121:")
					|| idValue.equals("4122") || idValue.equals("4123")) {
				if (m_InterfaceUpdateUserProfileCallBack != null)
					UpdateUserProfileRstProc(response);
			} else if (idValue.equals("4117") || idValue.equals("4118")
					|| idValue.equals("4119")) {
				if (m_InterfaceUpdateuserNicknameCallBack != null) {
					UpdateUserNicknameRstProc(response);
				}
			} else if (idValue.equals("4383") || idValue.equals("4384")) {
				if (m_InterfaceGetUserInfoByIdCallBack != null) {
					GetUserBasicInfoRstProc(response);
				}
			} else if (idValue.equals("4405") || idValue.equals("4406")
					|| idValue.equals("4407") || idValue.equals("4408")) {
				if (m_InterfaceUploadSleepTimeSettingCallBack != null) {
					UploadUserSleepTimeSettingRstProc(response);
				}
			} else if (idValue.equals("4409") || idValue.equals("4410")
					|| idValue.equals("4411")) {
				if (m_InterfaceNearbyUserHideListCallBack != null) {
					UserHideSettingRstProc(response);
				}
			} else if (idValue.equals("4367") || idValue.equals("4368")
					|| idValue.equals("4369：0") || idValue.equals("4369")) {
				if (m_InterfaceUserProfileUpdateCallBack != null) {
					UserProfileUpdateRstProc(response);
				}
			} else if (idValue.equals("4008") || idValue.equals("4009")
					|| idValue.equals("4010") || idValue.equals("4011")
					|| idValue.equals("4012") || idValue.equals("4013")
					|| idValue.equals("4014") || idValue.equals("4015")
					|| idValue.equals("4016") || idValue.equals("4046")
					|| idValue.equals("4047")) {
				if (m_InterfaceAddDeleteFriendCallBack != null) {
					AddDeleteFriendRstProc(response);
				}
			} else if (idValue.equals("4048") || idValue.equals("4049")
					|| idValue.equals("4050")) {
				if (m_InterfaceUpdateUserBasicInfoCallBack != null) {
					UpdateUserBasicInfoRstProc(response);
				}
			} else if ("4325".equals(idValue)) {
				if (m_InterfaceDowmloadQiniuFileCallBack != null) {
					dowmloadQiniuFileProc(response);
				}
			} else {
				operateError(Integer.parseInt(idValue) + ":" + m_context.getResources().getString(R.string.net_requet_error_tip),Integer.parseInt(idValue));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// /////////////////////////////////////////////////////////////////////////////////////////////////

	};

	// ///////////////////请求数据各种错误 和业务无关
	public void ProcJSONDataOnErr(VolleyError error) {
		String errmsg = "访问服务器失败";
		int code = 3001;

		operateError(errmsg,3001);

	}

	private void operateError(String errmsg,int code) {
		if (m_InterfaceSaveOtherUserscallBack != null) {
			m_InterfaceSaveOtherUserscallBack.onError(code, errmsg);
		}
		;

		if (m_InterfaceOwerSaveOtherAccURLCallBack != null) {
			m_InterfaceOwerSaveOtherAccURLCallBack.onError(code, errmsg);
		}
		;

		if (m_InterfaceOwerSavePhoneNumberCallBack != null) {
			m_InterfaceOwerSavePhoneNumberCallBack.onError(code, errmsg);
		}
		;

		if (m_InterfaceSearchNickNameCallBack != null) {
			m_InterfaceSearchNickNameCallBack.onError(code, errmsg);
		}
		;
		if (m_InterfaceAddFriendFromContectCallBack != null) {
			m_InterfaceAddFriendFromContectCallBack.onError(code, errmsg);
		}
		if (m_InterfaceUpdateUserProfileCallBack != null) {
			m_InterfaceUpdateUserProfileCallBack.onError(code, errmsg);

		}
		if (m_InterfaceGetUserInfoByIdCallBack != null) {
			m_InterfaceGetUserInfoByIdCallBack.onError(code, errmsg);

		}

		if (m_InterfaceUploadSleepTimeSettingCallBack != null) {
			m_InterfaceUploadSleepTimeSettingCallBack.onError(code, errmsg);
		}

		if (m_InterfaceNearbyUserHideListCallBack != null) {
			m_InterfaceNearbyUserHideListCallBack.onError(code, errmsg);
		}
		;

		if (m_InterfaceUserProfileUpdateCallBack != null) {
			m_InterfaceUserProfileUpdateCallBack.onError(code, errmsg);
		}

		if (m_InterfaceCheckUserStatusCallBack != null) {
			m_InterfaceCheckUserStatusCallBack.onError(code, errmsg);
		}

		if (m_InterfaceAddDeleteFriendCallBack != null) {
			m_InterfaceAddDeleteFriendCallBack.onError(code, errmsg);
		}

		if (m_InterfaceUpdateUserBasicInfoCallBack != null) {
			m_InterfaceUpdateUserBasicInfoCallBack.onError(code, errmsg);
		}

		if (m_InterfaceDowmloadQiniuFileCallBack != null) {
			m_InterfaceDowmloadQiniuFileCallBack.onError(code, errmsg);
		}
	};

}
