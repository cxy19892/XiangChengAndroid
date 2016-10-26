package com.yzm.sleep.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.activity.LoginActivity;
import com.yzm.sleep.utils.InterFaceUtilsClass.CheckCellPhoneLoginStatParam;
import com.yzm.sleep.utils.InterFaceUtilsClass.CheckCellPhoneLoginStatRst;
import com.yzm.sleep.utils.InterFaceUtilsClass.CheckFriendsByCellPhoneParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.FriendsByCellPhoneClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceCheckCellPhoneLoginStatCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceCheckFriendsByCellPhoneCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceCheckPhoneNumCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceRegUserByPhoneCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceRequestRegCodeCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceResetPassWordRstCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceVerificationCodeRstCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.PhoneRegParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.ResetPassWordParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UserInfo;
import com.yzm.sleep.utils.InterFaceUtilsClass.VerificationCodeParamClass;

public class PhoneProcClass extends HttpDataTranUtils {
	private Context m_context = null;
	private String phon;
	private Activity activity;
	private LoginActivity log;

	public PhoneProcClass(Context context) {
		m_context = context;
	}

	// ---------------------------------------------------------------------------------------------------------------------------
	// internal_signup_1.php 用于验证手机号是否已被绑定/注册过 开始

	private String urlCheckPhone = 	g_BaseSiteURL+g_BaseSecond+"internal_signup_1.php?target_int_id=signup";
	private InterfaceCheckPhoneNumCallBack m_InterfaceCheckPhoneNumcallBack;

	public void CheckPhoneRegStat(String strPhoneNumString,
			InterfaceCheckPhoneNumCallBack callBack) {
		m_InterfaceCheckPhoneNumcallBack = callBack;
		MobclickAgent.onEvent(m_context, "614");
		String newString = urlCheckPhone + "&" + "my_phone_num="
				+ strPhoneNumString;

		super.requestJosnObjectData(m_context, newString);

	};

	private void CheckPhoneNumber(JSONObject response) {

		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4056")) {

				m_InterfaceCheckPhoneNumcallBack.onError(4056,"手机号位数不对或内部ID为空");

			} else if (idValue.equals("4057")) {

				m_InterfaceCheckPhoneNumcallBack.onError(4057,"用户ID不存在");

			} else if (idValue.equals("4058")) {

				m_InterfaceCheckPhoneNumcallBack.onError(4058,"该手机号已经与某账户绑定过了");

			} else if (idValue.equals("4058:1")) {

				m_InterfaceCheckPhoneNumcallBack.onError(4058,"该手机号已经与其他账户绑定过了");

			} else if (idValue.equals("4058:2")) {

				m_InterfaceCheckPhoneNumcallBack.onError(4058,"该手机号已经与该账户绑定过了");

			} else if (idValue.equals("4059")) {

				m_InterfaceCheckPhoneNumcallBack.onSuccess(4059);

			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

	}

	// internal_signup_1.php 用于验证手机号是否已被绑定/注册过 结束

	// ---------------------------------------------------------------------------------------------------------------------------

	// internal_signup_2.php 用于生成手机验证码，调用SDK发给手机 开始

	private String RequestRegCodeurl = g_BaseSiteURL+g_BaseSecond+"internal_signup_2.php?";
	private InterfaceRequestRegCodeCallBack m_InterfaceRequestRegCodeCallBack;

	public void RequestRegCode(String strPhoneNumString,
			InterfaceRequestRegCodeCallBack callBack) {
		m_InterfaceRequestRegCodeCallBack = callBack;

		String newString = RequestRegCodeurl + "my_phone_num="
				+ strPhoneNumString;

		super.requestJosnObjectData(m_context, newString);

	};

	private void RequstRegCodeRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4060")) {

				m_InterfaceRequestRegCodeCallBack.onError(4060,"手机号位数不对");

			} else if (idValue.equals("4061")) {
				m_InterfaceRequestRegCodeCallBack.onSuccess(4061);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	

	}

	// internal_signup_2.php 用于生成手机验证码，调用SDK发给手机 结束

	// ---------------------------------------------------------------------------------------------------------------------------

	// internal_signup_3.php 用于完成对手机号的验证，并使用手机号直接注册内部账号 开始

	private String RegUserByPhoneURL = g_BaseSiteURL+g_BaseSecond+"internal_signup_3.php?";
	private InterfaceRegUserByPhoneCallBack m_InterfaceRegUserByPhoneCallBack;

	public void RegUserByPhone(PhoneRegParamClass mPhoneParam,
			InterfaceRegUserByPhoneCallBack callBack) {

		m_InterfaceRegUserByPhoneCallBack = callBack;
		
		
		String strIDNickName="";
		 try {
			 strIDNickName=	URLEncoder.encode(mPhoneParam.my_int_nickname, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String newString = RegUserByPhoneURL + "my_phone_num="
				+ mPhoneParam.my_phone_num + "&" + "verification_code="
				+ mPhoneParam.verification_code + "&" + "my_int_nickname="
				+ strIDNickName + "&" + "my_int_pwd="
				+ mPhoneParam.my_int_pwd + "&" + "my_int_age="
				+ mPhoneParam.my_int_age + "&" + "my_int_gender="
				+ mPhoneParam.my_int_gender + "&" + "my_int_occupation="
				+ mPhoneParam.my_int_occupation + "&" + "my_int_profile="
				+ mPhoneParam.my_int_profile;

		super.requestJosnObjectData(m_context, newString);

	};

	private void RegUserByPhoneRstProc(JSONObject response) {

		String idValue = "";
		try {

			idValue = (String) response.get("response").toString();
	

		if (idValue.equals("4062")) {
			// response：4062 invalid basic info 基本信息为空或手机号位数不对
			m_InterfaceRegUserByPhoneCallBack.onError(4062, "基本信息为空或手机号位数不对");

		} else if (idValue.equals("4063")) {

			// response：4063 invalid occupation code 职业编号错误
			m_InterfaceRegUserByPhoneCallBack.onError(4063, "职业编号错误");

		} else if (idValue.equals("4064")) {
			// response：4064 invalid verification code length 验证码位数不对
			m_InterfaceRegUserByPhoneCallBack.onError(4064, "验证码位数不对");

		} else if (idValue.equals("4065")) {

			// response：4065 user nickname too long 用户昵称长度超限
			m_InterfaceRegUserByPhoneCallBack.onError(4065, "用户昵称长度超限");

		} else if (idValue.equals("4066")) {

			// response：4066 user age/gender/profile too long 年龄、性别、头像地址长度超限
			m_InterfaceRegUserByPhoneCallBack.onError(4066, "年龄、性别、头像地址长度超限");

		} else if (idValue.equals("4067")) {
			// response：4067 duplicate number 该手机号已经注册过或与其他账户绑定过了
			m_InterfaceRegUserByPhoneCallBack.onError(4067,
					"该手机号已经注册过或与其他账户绑定过了");

		} else if (idValue.equals("4068")) {
			// response：4068 cellphone number and verification code not match
			// 该手机号与验证码不匹配
			m_InterfaceRegUserByPhoneCallBack.onError(4068, "该手机号与验证码不匹配");

			} else if (idValue.equals("4069")) {
				// response：4069 verification code expires 验证码过期
				m_InterfaceRegUserByPhoneCallBack.onError(4069, "验证码过期");
			} else if (idValue.equals("4070:2")) {
				// response：4069 verification code expires 验证码过期
				m_InterfaceRegUserByPhoneCallBack.onError(40702, "环信注册失败");
			} else if (idValue.equals("4070:3")) {
				// response：4069 verification code expires 验证码过期
				m_InterfaceRegUserByPhoneCallBack.onError(40703, "环信注册失败");
			} else if (idValue.equals("4070:4")) {
				// response：4069 verification code expires 验证码过期
				m_InterfaceRegUserByPhoneCallBack.onError(40704, "环信无响应");
			}	
			else if (idValue.equals("4070:1")) {
				// response：4070 int_id：xxxxxxxx succeed_signup 注册成功，同时返回用户内部ID

				String strInt_ID = "";
				try {

					strInt_ID = (String) response.get("int_id").toString();

				} catch (Exception e) {

					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				m_InterfaceRegUserByPhoneCallBack.onSuccess(40701, strInt_ID);

			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// internal_signup_3.php 用于完成对手机号的验证，并使用手机号直接注册内部账号 结束
	// ---------------------------------------------------------------------------------------------------------------------------
	
	
	//verify_code.php	用于验证手机号和验证码是否匹配  开始

	
	//入口参数	参数含义	参数要求
	//my_phone_num	用户的电话号码	非空，且为11位纯数字，且经过验证
	//verification_code	验证码	非空，4位纯数字
	
	private String VerifyCodeURL=g_BaseSiteURL+g_BaseSecond+"verify_code.php?";
	private InterfaceVerificationCodeRstCallBack m_InterfaceVerificationCodeRstCallBack;

	public void VerifyCode(VerificationCodeParamClass m_verificationCode,
			InterfaceVerificationCodeRstCallBack callBack) {
		m_InterfaceVerificationCodeRstCallBack = callBack;

		String newString = VerifyCodeURL + "my_phone_num="
				+m_verificationCode.my_phone_num+"&"+"verification_code=" +m_verificationCode.verification_code;

		super.requestJosnObjectData(m_context, newString);

	};

	private void VerificationCodeRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4079:1")) {
				m_InterfaceVerificationCodeRstCallBack.onError(40791,"手机号位数不对");
			} else if (idValue.equals("4079:2")) {
				m_InterfaceVerificationCodeRstCallBack.onError(40792,"验证码位数不对");
			}else if (idValue.equals("4080")) {
				m_InterfaceVerificationCodeRstCallBack.onError(4080,"该手机号与验证码不匹配");
			}else if (idValue.equals("4081")) {
				m_InterfaceVerificationCodeRstCallBack.onError(4081,"	验证码过期");
			}else  if (idValue.equals("4082")) {
				m_InterfaceVerificationCodeRstCallBack.onSuccess(4082);
			}	
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	//verify_code.php	用于验证手机号和验证码是否匹配   结束
//-----------------------------------------------------------------------------------------------------------------------
	
	
	//verify_code.php	用于重设手机号对应账户的密码

	
	private String ResetPasswordURL=g_BaseSiteURL+g_BaseSecond+"reset_pwd.php?";
	private InterfaceResetPassWordRstCallBack m_InterfaceResetPassWordRstCallBack;

	public void VerifyCode(ResetPassWordParamClass m_ResetPasswordCode,
			InterfaceResetPassWordRstCallBack callBack) {
		m_InterfaceResetPassWordRstCallBack = callBack;

		String newString = ResetPasswordURL + "my_phone_num="
				+m_ResetPasswordCode.my_phone_num+"&"+"my_int_pwd=" +m_ResetPasswordCode.my_int_pwd;

		super.requestJosnObjectData(m_context, newString);

	};

	private void ResetPassWordProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4083")) {
				m_InterfaceResetPassWordRstCallBack.onError(4078,"手机号位数不对");
			} else if (idValue.equals("4084")) {
				m_InterfaceResetPassWordRstCallBack.onError(4079,"密码太短（短于6位）");
			}else if (idValue.equals("4085")) {
				m_InterfaceResetPassWordRstCallBack.onError(4080,"该手机号不存在系统中");
			}else  if (idValue.equals("4086")) {
				m_InterfaceResetPassWordRstCallBack.onSuccess(4086);
			}	
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
		
	//verify_code.php	用于重设手机号对应账户的密码   结束
	
	// /////////////////////////////////////////////////////////////////////////////////////////////////
	
	// /////////////////////////////////////////////////////////////////////////////////////////////////
	//cellphone_login.php	用于验证用户登录信息  开始
	
	private String CheckCellPhoneLoginStatURL=g_BaseSiteURL+g_BaseSecond+"cellphone_login.php?";
	private InterfaceCheckCellPhoneLoginStatCallBack m_InterfaceCheckCellPhoneLoginStatCallBack;

	public void CheckCellPhoneLoginStat(CheckCellPhoneLoginStatParam m_CheckCellPhoneLoginStat,Activity act,
			InterfaceCheckCellPhoneLoginStatCallBack callBack) {
		
		m_InterfaceCheckCellPhoneLoginStatCallBack = callBack;

		String newString = CheckCellPhoneLoginStatURL + "my_phone_num="
				+ m_CheckCellPhoneLoginStat.my_phone_num+"&"+"my_int_pwd=" +m_CheckCellPhoneLoginStat.my_int_pwd;
		activity = act;
		phon = m_CheckCellPhoneLoginStat.my_phone_num;
		super.requestJosnObjectData(m_context, newString);

	};

	private void CheckCellPhoneLoginProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4001:1")) {
				m_InterfaceCheckCellPhoneLoginStatCallBack.onError(40011,"密码为空或电话号码位数、格式不对");
			} else if (idValue.equals("4001:2")) {
				m_InterfaceCheckCellPhoneLoginStatCallBack.onError(40012,"用户名不存在或密码错误");
			}else if (idValue.equals("4002")) {
				
				
			//	response：4002  user_info:   int_id：xxxxxxxx   nickname：xxxxx  age：xxxx gender:xx  occupation:xxxxxx  profile:xxxxxx
			String strID = (String) response.get("user_info").toString();
			LogUtil.i("masong_login", strID);
			//JSONObject infoJsonObject=response.getJSONObject("user_info");
				
				if(strID!=null)
				{
					Gson gson=new Gson();
					
					UserInfo userInfo = gson.fromJson(strID, UserInfo.class);
					
				
					   CheckCellPhoneLoginStatRst rst=new CheckCellPhoneLoginStatRst();
					 
					 	rst.int_id=userInfo.int_id;
						rst.nickname=userInfo.nickname;
						rst.age=userInfo.age;
						rst.gender=userInfo.gender;
						rst.occupation=userInfo.occupation;
						rst.profile=userInfo.profile;
						rst.hide=userInfo.hiding;
						rst.profile_key = userInfo.profile_key;
						rst.user_ext_acc_qq=String.valueOf(userInfo.user_ext_acc_qq);
						rst.user_ext_acc_weibo=String.valueOf(userInfo.user_ext_acc_weibo);
						rst.user_ext_acc_wechat=String.valueOf(userInfo.user_ext_acc_wechat);
						rst.user_ext_acc_cellphone=String.valueOf(userInfo.user_ext_acc_cellphone);
						rst.is_zj = userInfo.is_zj;
						PreManager.instance().saveUserHeight(m_context, userInfo.user_internal_height);
						PreManager.instance().saveUserWeight(m_context, userInfo.user_internal_weight);
						PreManager.instance().saveUserBirthday(m_context, userInfo.user_internal_birthday);
						PreManager.instance().saveUserOccupation(m_context, userInfo.occupation);
						PreManager.instance().saveUserIsExpert(m_context, userInfo.is_zj);
						//保存平台绑定信息
						PreManager.instance().savePlatformBoundMsg(
								m_context,
								rst.user_ext_acc_weibo
										+ rst.user_ext_acc_wechat
										+ rst.user_ext_acc_qq
										+ rst.user_ext_acc_cellphone);
						GetUserData(phon, rst);
						m_InterfaceCheckCellPhoneLoginStatCallBack.onSuccess(4002, rst);
						
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceCheckCellPhoneLoginStatCallBack.onError(40012,"数据格式错误");
		}

		
	}
	
	
	
	
	
	//=======================================================================================================
	//check_friends_by_phones.php 判断手机通讯录中的手机号跟当前用户是不是好友
	
	private String CheckFriendsByCellURL = g_BaseSiteURL+g_BaseVersionThread+"check_friends_by_phones.php?";
	private InterfaceCheckFriendsByCellPhoneCallBack m_InterfaceCheckFriendsByCellPhoneCallBack;
	
	public void CheckFriendsByCell(CheckFriendsByCellPhoneParamClass m_Params,
			InterfaceCheckFriendsByCellPhoneCallBack callBack) {
		
		m_InterfaceCheckFriendsByCellPhoneCallBack = callBack;
		
		RequestParams params = new RequestParams();
		params.put("my_int_id", m_Params.my_int_id);
		params.put("phones", new Gson().toJson(m_Params.phones));
		super.postJosnObjectData(m_context, CheckFriendsByCellURL, params);

	};
	private void CheckFriendsByCellRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4466")) {
				m_InterfaceCheckFriendsByCellPhoneCallBack.onError(4466,"ID不存在或者格式错误");
			} else if (idValue.equals("4467")) {
				m_InterfaceCheckFriendsByCellPhoneCallBack.onError(4467,"通讯录数据为空");
			}else if (idValue.equals("4468")) {
				
				String string = (String) response.get("datas").toString();
				
				//JSONObject infoJsonObject=response.getJSONObject("user_info");
					
				if(string!=null)
				{
					Gson gson=new Gson();
	
					List<FriendsByCellPhoneClass> list = gson.fromJson(string, new TypeToken<List<FriendsByCellPhoneClass>>(){}.getType());
					
					m_InterfaceCheckFriendsByCellPhoneCallBack.onSuccess(4468, list);
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	
	
	
	//cellphone_login.php	用于验证用户登录信息  
	
	public void ProcJSONData(JSONObject response) {

		String idValue;
		try {
			idValue = (String) response.get("response").toString();

			// /////////////////////////////////////////////////////////////////////////////////////////////////

			if (idValue.equals("4056") || idValue.equals("4057")
					|| idValue.equals("4058") || idValue.equals("4058:1")
					|| idValue.equals("4058:2") || idValue.equals("4059")) {

				if (m_InterfaceCheckPhoneNumcallBack != null) {
					CheckPhoneNumber(response);
				}
				;
			} else if (idValue.equals("4060") || idValue.equals("4061")) {
				if (m_InterfaceRequestRegCodeCallBack != null) {
					RequstRegCodeRstProc(response);
				}
				;
			} else if (idValue.equals("4062") || idValue.equals("4063")
					|| idValue.equals("4064") || idValue.equals("4065")
					|| idValue.equals("4066") || idValue.equals("4067")
					|| idValue.equals("4068") || idValue.equals("4069")
					|| idValue.equals("4070:1")|| idValue.equals("4070:2")
					|| idValue.equals("4070:3")|| idValue.equals("4070:4")) {

				if (m_InterfaceRegUserByPhoneCallBack != null) {
					RegUserByPhoneRstProc(response);
				}
				;
			} else if (idValue.equals("4083") || idValue.equals("4084")
					|| idValue.equals("4085") || idValue.equals("4086")) {

				if (m_InterfaceResetPassWordRstCallBack != null) {
					ResetPassWordProc(response);
				}
			} else if (idValue.equals("4079:1") || idValue.equals("4079:2")
					|| idValue.equals("4080") || idValue.equals("4081")
					|| idValue.equals("4082")) {
				if (m_InterfaceVerificationCodeRstCallBack != null) {
					VerificationCodeRstProc(response);
				}
				;
			}else if (idValue.equals("4001:1") || idValue.equals("4001:2")
					|| idValue.equals("4002") ) {
				if (m_InterfaceCheckCellPhoneLoginStatCallBack != null) {
					CheckCellPhoneLoginProc(response);
				}
			}else if (idValue.equals("4466") || idValue.equals("4467")
					|| idValue.equals("4468") ) {
				if (m_InterfaceCheckFriendsByCellPhoneCallBack != null) {
					CheckFriendsByCellRstProc(response);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	};

	// ///////////////////请求数据各种错误 和业务无关
	public void ProcJSONDataOnErr(VolleyError error) {
		String errmsg = "访问服务器失败";
		
		if (m_InterfaceCheckPhoneNumcallBack != null) {
			m_InterfaceCheckPhoneNumcallBack.onError(3001, errmsg);
		}
		;
		// /////////////////////////////////////////////////////////////////////////////////////////////////
		if (m_InterfaceRequestRegCodeCallBack != null) {
			m_InterfaceRequestRegCodeCallBack.onError(3001, errmsg);
		}
		;
		// /////////////////////////////////////////////////////////////////////////////////////////////////
		if (m_InterfaceRegUserByPhoneCallBack != null) {
			m_InterfaceRegUserByPhoneCallBack.onError(3001, errmsg);
		};	
		// /////////////////////////////////////////////////////////////////////////////////////////////////
		if (m_InterfaceVerificationCodeRstCallBack != null) {
			m_InterfaceVerificationCodeRstCallBack.onError(3001, errmsg);
		};
		
		
		if (m_InterfaceResetPassWordRstCallBack != null) {
			m_InterfaceResetPassWordRstCallBack.onError(3001,errmsg);
		}

		if (m_InterfaceCheckCellPhoneLoginStatCallBack != null) {
			m_InterfaceCheckCellPhoneLoginStatCallBack.onError(3001,errmsg);

		}
		
		if (m_InterfaceCheckFriendsByCellPhoneCallBack != null) {
			m_InterfaceCheckFriendsByCellPhoneCallBack.onError(3001,errmsg);
			
		}
				// m_InterfaceRegUserByPhoneCallBack
		Log.d(TAG, error.getMessage(), error);

	};
	private void GetUserData(String my_phone_num,CheckCellPhoneLoginStatRst m_RstClass){
		
		PreManager.instance().saveUserId(m_context,
				m_RstClass.int_id);
		PreManager.instance().saveUserNickname(m_context,
				m_RstClass.nickname);
		PreManager.instance().saveUserProfession(m_context,
				Integer.parseInt(m_RstClass.occupation));
		String profile = m_RstClass.profile;
		PreManager.instance().saveUserProfileUrl(m_context,
				profile);
		PreManager.instance().saveUserProfileKey(m_context,
				m_RstClass.profile_key);
//		PreManager.instance().saveUserAge(m_context,
//				m_RstClass.age);
//		PreManager.instance().saveUserAge(m_context,
//				m_RstClass.age);
		PreManager.instance().saveUserGender(m_context,
				m_RstClass.gender);
		if (m_RstClass.hide != null
				&& m_RstClass.hide.equals("1"))
			PreManager.instance().saveUserHide(m_context, true);
		else
			PreManager.instance().saveUserHide(m_context, false);
		PreManager.instance().savePlatformBoundMsg(
				m_context,
				m_RstClass.user_ext_acc_weibo
						+ m_RstClass.user_ext_acc_wechat
						+ m_RstClass.user_ext_acc_qq
						+ m_RstClass.user_ext_acc_cellphone);
		PreManager.instance().saveBoundPhoneNumber(m_context,
				my_phone_num);

		PreManager.instance().saveLoginUserName(m_context,
				my_phone_num);
	}
}
