package com.yzm.sleep.activity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.widget.LoginButton;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AccessTokenKeeper;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.ScreenShot;
import com.yzm.sleep.bean.MyWeiBoFriendBaean;
import com.yzm.sleep.bean.UserInfoBean;
import com.yzm.sleep.bean.UserInfoPhotoBean;
import com.yzm.sleep.utils.CommunityProcClass;
import com.yzm.sleep.utils.InterFaceThirdUtilsClass.GetWeiboFanParamClass;
import com.yzm.sleep.utils.InterFaceThirdUtilsClass.InterfaceGetWeiboFanCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetTiaocanCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceRequestRegCodeCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceSaveOtherUsersCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadSleepTimeSettingCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.OtherUsersParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.OtherUsersReturn4031RstClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.OtherUsersReturn4032RstClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadSleepTimeSettingParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfacePhoneLoginCallback;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PhoneProcClass;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.SleepConstants;
import com.yzm.sleep.utils.SleepUtils;
import com.yzm.sleep.utils.StringUtil;
import com.yzm.sleep.utils.ToastManager;
import com.yzm.sleep.utils.URLUtil;
import com.yzm.sleep.utils.UserInfoUtil;
import com.yzm.sleep.utils.UserManagerProcClass;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.WeiboDataProClass;
import com.yzm.sleep.utils.XiangchengProcClass;

//import com.yzm.sleep.activity.alar.PublishAudioActivity;

@SuppressLint("SimpleDateFormat")
public class LoginActivity extends BaseActivity implements IWXAPIEventHandler {
	Context context;
	InputMethodManager inputMethodManager;
	private EditText et_account, et_pwd;
	private Button btn_login;
	private Button btn_get_code;
	// ImageView state_account_state, state_pwd_state;
	private String platformId = "";
	private IWXAPI wxapi;
	/** 新浪微博登录按钮 */
	private LoginButton mLoginWeibo;
	/** 新浪授权认证信息 */
	private AuthInfo authInfo;
	/** 新浪access_token */
	private Oauth2AccessToken mAccessToken;
	/** 新浪登陆认证对应的listener */
	private AuthListener mLoginWeiboListener = new AuthListener();
	/** 新浪需要查询用户的ID */
	private long uid;

	private static Tencent mTencent;
	private UserInfo mQQUserInfo;
	private static String token;
	private static String openId;

	String my_int_occupation = "";
	private String userNickName = "";
	private String userProfile = "";

	/** 保存微博登录用户信息 */
	private static final int QQ_LOGIN_SUCCESS = 61;
	private static final int SAVE_THIRD_PARTY_INFO = 101;
	private ArrayList<String> qqFriendIdStrings;
	private ProgressUtils pb;
	/** 微博用户信息 */
	private User user;
	private TimeCount timeCount;

	@SuppressLint("HandlerLeak")
	public Handler mHandler = new Handler() {

		private JSONObject response;

		@Override
		public void handleMessage(Message msg) {
			PreManager.instance().saveUpdateUserInfoState(context, "0");
			if (msg.what == QQ_LOGIN_SUCCESS) {
				response = (JSONObject) msg.obj;

				if (response.has("nickname")) {
					try {
						String my_ext_profile = "";
						if (response.has("figureurl")) {
							my_ext_profile = response
									.getString("figureurl_qq_2");
							userProfile = !TextUtils.isEmpty(my_ext_profile) ? my_ext_profile
									: URLUtil.XIANGCHENG_ICON_URL;
						}
						String gender = response.getString("gender");
						if (gender.equals("男")) {
							PreManager.instance().saveUserGender(context, "01");
						} else if (gender.equals("女")) {
							PreManager.instance().saveUserGender(context, "02");
						}
						String platform = platformId;
						String my_ext_acc = openId;
						userNickName = !TextUtils.isEmpty(response
								.getString("nickname")) ? response
								.getString("nickname") : "xc_"
								+ SleepUtils.getFormatedDateTime("yyyyMMdd",
										System.currentTimeMillis());

						OtherUsersParamClass otherUsersParamClass = new OtherUsersParamClass();
						otherUsersParamClass.platform = platform;
						otherUsersParamClass.my_ext_acc = my_ext_acc;
						otherUsersParamClass.my_ext_nickname = userNickName;
						otherUsersParamClass.my_ext_profile = my_ext_profile;

						String userBirthday = PreManager.instance()
								.getUserBirthday(context);
						try {
							otherUsersParamClass.my_int_age = PreManager
									.birthDayToAge(userBirthday);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						otherUsersParamClass.my_int_gender = PreManager
								.instance().getUserGender(context);
						otherUsersParamClass.my_int_occupation = PreManager
								.instance().getUserProfession(context) + "";
						otherUsersParamClass.friend_num = String
								.valueOf(qqFriendIdStrings.size());
						otherUsersParamClass.friendacc_x = qqFriendIdStrings;
						// 保存QQ登录用户信息
						saveThirdPartyUserInfo(otherUsersParamClass);
					} catch (JSONException e) {
						cancelProgressDialog();
					}
				}
			} else if (msg.what == SAVE_THIRD_PARTY_INFO) {
				OtherUsersParamClass infoClass = (OtherUsersParamClass) msg.obj;
				saveThirdPartyUserInfo(infoClass);
			}
		}
	};

	private BroadcastReceiver myReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Constant.RECEVER_LOGIN_ACTION)) {// 登录成功
				cancelProgressDialog();
				// 登录成功后判断是否跳转到关联手机界面
				UserInfoUtil.isGotoBoundPhone(LoginActivity.this);
				AppManager.getAppManager().finishActivity(LoginActivity.class);
			}
		}

	};

	private void register() {
		try {
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(Constant.RECEVER_LOGIN_ACTION);
			registerReceiver(myReceiver, intentFilter);
		} catch (Exception e) {
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		register();
		context = this;
		initView();
		timeCount = new TimeCount(60000, 1000);
		qqFriendIdStrings = new ArrayList<String>();
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		// 微博登录
		String scope = "email,direct_messages_read,direct_messages_write,"
				+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
				+ "invitation_write";// follow_app_official_microblog
		authInfo = new AuthInfo(this, SleepConstants.SINA_SLEEP_APP_KEY,
				"http://www.sina.com", scope);
		mLoginWeibo.setWeiboAuthInfo(authInfo, mLoginWeiboListener);// 为按钮设置授权认证信息

		// QQ空间登录
		if (mTencent == null) {
			mTencent = Tencent.createInstance(
					SleepConstants.TENCENT_SLEEP_APP_ID, this);
		}

		// 微信登录
		wxapi = WXAPIFactory.createWXAPI(this,
				SleepConstants.WECHAT_SLEEP_APP_ID, false);// 通过WXAPIFactory工厂，获取IWXAPI的实例
		wxapi.registerApp(SleepConstants.WECHAT_SLEEP_APP_ID); // 将该app注册到微信
		wxapi.handleIntent(getIntent(), this);

		PreManager.instance().saveOtherPlatformLoginState(context, "0");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(myReceiver);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (LoginActivity.this.getCurrentFocus() != null
					&& LoginActivity.this.getCurrentFocus().getWindowToken() != null) {
				inputMethodManager.hideSoftInputFromWindow(LoginActivity.this
						.getCurrentFocus().getWindowToken(), 0);
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		PreManager.instance().saveRegiserReady(context, false);
		MobclickAgent.onPageStart("Login"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this); // 统计时长
		if ("1".equals(PreManager.instance()
				.getOtherPlatformLoginState(context))) {
			cancelProgressDialog();
		}
		if (PreManager.instance().getUserPlatform(this)
				.equals(SleepConstants.PLATFORM_WEIXIN)) {
			cancelProgressDialog();
		}

		// 第三方登录时，弹出第三方软件登录窗口，点击取消，回到登录界面，软键盘未消失，进度框一直显示
		try {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			View view = getCurrentFocus();
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		((TextView) findViewById(R.id.title)).setText("登录");
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.layout_root).setOnClickListener(this);
		findViewById(R.id.login_qq).setOnClickListener(this);// QQ登录按钮
		findViewById(R.id.btn_login_wechat).setOnClickListener(this);// 微信登录按钮
		mLoginWeibo = (LoginButton) findViewById(R.id.login_button_weibo);// 微博登录按钮
		mLoginWeibo.setExternalOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				weiboLogin();
			}
		});
		btn_get_code = (Button) findViewById(R.id.btn_get_code);
		btn_get_code.setOnClickListener(this);
		mLoginWeibo.setBackgroundResource(R.drawable.ic_wb);
		et_account = (EditText) findViewById(R.id.et_account);// 用户账号账号(手机号码)输入框
		et_pwd = (EditText) findViewById(R.id.et_pwd);// 用户密码输入框
		btn_login = (Button) findViewById(R.id.btn_login);// 登录按钮

		btn_login.setOnClickListener(this);

		String loginUserName = PreManager.instance().getLoginUserName(context);
		if (!TextUtils.isEmpty(loginUserName)) {
			et_account.setText(loginUserName);
			et_pwd.requestFocus();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.layout_root:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			break;
		case R.id.login_qq:// QQ 登录
			MobclickAgent.onEvent(this, "613");
			PreManager.instance().saveUserPlatform(context,
					SleepConstants.PLATFORM_QQ);
			platformId = "300000003";
			if (Util.isQQInstalled(context)) {
				showProgressDialog();
				loginByQQ();
			} else {
				Util.show(context, "请先安装qq客户端");
			}
			break;
		case R.id.btn_login_wechat:// 微信登录
			showProgressDialog();
			MobclickAgent.onEvent(this, "611");
			PreManager.instance().saveUserPlatform(context,
					SleepConstants.PLATFORM_WEIXIN);
			SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "wechat" + new Random(100).nextInt();
			boolean sendReq = wxapi.sendReq(req);
			if (!wxapi.isWXAppInstalled()) {
				cancelProgressDialog();
				ToastManager.getInstance(LoginActivity.this).show("请先安装微信客户端");
				return;
			}
			break;
		case R.id.btn_login:// 香橙登录
			MobclickAgent.onEvent(this, "610");
			String username = et_account.getText().toString();
			String pwd = et_pwd.getText().toString();

			if (TextUtils.isEmpty(username)) {
				Toast.makeText(LoginActivity.this, "用户名不能为空",
						Toast.LENGTH_SHORT).show();
			} else if (TextUtils.isEmpty(pwd)) {
				Toast.makeText(LoginActivity.this, "验证码不能为空",
						Toast.LENGTH_SHORT).show();
			} else if (pwd.length() != 4) {
				Toast.makeText(LoginActivity.this, "验证码输入不对。请重新输入",
						Toast.LENGTH_SHORT).show();
			} else if (StringUtil.isMobile(username)) {
				if (SleepUtils.isConnect(context)) {
					PreManager.instance().saveUserPlatform(context,
							SleepConstants.PLATFORM_OWN);
					doLogins(username, pwd); 
				}
			} else {
				if(username.startsWith("#")){
					if (SleepUtils.isConnect(context)) {
						PreManager.instance().saveUserPlatform(context,
								SleepConstants.PLATFORM_OWN);
						doLogins(username.replace("#", ""), pwd); 
					}
				}else{
					Toast.makeText(context, "手机号码不对。请重新输入", Toast.LENGTH_SHORT)
					.show();
				}
			}
			break;
		case R.id.btn_get_code:
			String phon = et_account.getText().toString();
			if (!TextUtils.isEmpty(phon)) {
				if (phon.length() < 11) {
					ToastManager.getInstance(LoginActivity.this).show(
							"请输入正确的手机号码");
				} else {
					if (StringUtil.isMobile(phon)) {
						getVerifyCode(phon);
					} else {
						ToastManager.getInstance(LoginActivity.this).show(
								"手机号码输入有误");
					}
				}
			} else {
				ToastManager.getInstance(LoginActivity.this).show("手机号码不能为空");
			}
			break;
		default:
			break;
		}
	}

	private void weiboLogin() {
		showProgressDialog();
		MobclickAgent.onEvent(this, "612");
		PreManager.instance().saveUserPlatform(context,
				SleepConstants.PLATFORM_WEIBO);
		platformId = "300000002";
	}

	private void showProgressDialog() {
		if (pb == null) {
			pb = new ProgressUtils(context);
			pb.setCanceledOnTouchOutside(false);
		}
		pb.show();
	}

	private void cancelProgressDialog() {
		if (pb != null) {
			pb.dismiss();
		}
	}

	/**
	 * QQ登录
	 */
	private void loginByQQ() {
		if (!mTencent.isSessionValid()) {
			mTencent.login(LoginActivity.this, "all", loginQQListener);
		} else {
			mTencent.logout(this);
			updateUserInfo();
		}
	}

	IUiListener loginQQListener = new BaseUiListener() {
		@Override
		protected void doComplete(JSONObject values) {
			initQQOpenidAndToken(values);
			updateUserInfo();
		}
	};

	/**
	 * qq登录 当自定义的监听器实现IUiListener接口后，必须要实现接口的三个方法， onComplete onCancel onError
	 * 分别表示第三方登录成功，取消 ，错误。
	 * 
	 **/
	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			if (null == response) {
				return;
			}
			JSONObject jsonResponse = (JSONObject) response;
			if (null != jsonResponse && jsonResponse.length() == 0) {
				return;
			}
			doComplete((JSONObject) response);

		}

		protected void doComplete(JSONObject values) {
			Util.toastMessage(LoginActivity.this, "登录成功");
			updateUserInfo();
		}

		@Override
		public void onError(UiError e) {
			Util.toastMessage(LoginActivity.this, "登录失败");
			Util.dismissDialog();
			cancelProgressDialog();
		}

		@Override
		public void onCancel() {
			Util.toastMessage(LoginActivity.this, "登录取消");
			Util.dismissDialog();
			cancelProgressDialog();
		}
	}

	/**
	 * 获取QQ用户openid
	 * 
	 * @param access_token
	 */
	@SuppressWarnings("unused")
	private void getOpenId(final String access_token) {
	}

	public void initQQOpenidAndToken(JSONObject jsonObject) {
		try {
			token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
			String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
			openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
			if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
					&& !TextUtils.isEmpty(openId)) {
				mTencent.setAccessToken(token, expires);
				mTencent.setOpenId(openId);
			}
		} catch (Exception e) {
			cancelProgressDialog();
		}
	}

	private void updateUserInfo() {
		if (mTencent != null && mTencent.isSessionValid()) {
			IUiListener listener = new IUiListener() {

				@Override
				public void onError(UiError e) {
					cancelProgressDialog();
				}

				@Override
				public void onComplete(final Object response) {
					Message msg = new Message();
					msg.obj = response;
					msg.what = QQ_LOGIN_SUCCESS;
					mHandler.sendMessage(msg);
				}

				@Override
				public void onCancel() {
					cancelProgressDialog();
				}
			};
			mQQUserInfo = new UserInfo(this, mTencent.getQQToken());// 获取UserInfo对象
			mQQUserInfo.getUserInfo(listener);// 得到了userInfo对象后，调用它的getUserInfo（iuilistener）方法就可以得到用户的基本信息

		} else {
			cancelProgressDialog();
		}
	}

	/**
	 * 当 SSO 授权 Activity 退出时，该函数被调用。
	 * 
	 * @see {@link Activity#onActivityResult}
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (mLoginWeibo != null) {
			if (mLoginWeibo instanceof LoginButton) {
				((LoginButton) mLoginWeibo).onActivityResult(requestCode,
						resultCode, data);
			}
		}

		if (requestCode == Constants.REQUEST_API) {
			if (resultCode == Constants.RESULT_LOGIN) {
				Tencent.handleResultData(data, loginQQListener);
			}
		} else if (requestCode == Constants.REQUEST_APPBAR) { // app内应用吧登录
			if (resultCode == Constants.RESULT_LOGIN) {
				updateUserInfo();
			}
		}

		if (mTencent != null) {
			mTencent.onActivityResult(requestCode, resultCode, data);
		}
	}

	/**
	 * 清空输入状态
	 */
	public void reSetState() {
		et_account.setText("");
		et_pwd.setText("");
	}

	/**
	 * 获取微博用户信息
	 */
	private void getWeiboUserMsg() {
		UsersAPI mUsersAPI = new UsersAPI(context,
				SleepConstants.SINA_SLEEP_APP_KEY, mAccessToken);// 获取用户信息接口

		uid = Long.parseLong(mAccessToken.getUid());
		LogUtil.i("Enid", "------微博登录uid:" + uid);
		mUsersAPI.show(uid, mListener);

	}

	/**
	 * 微博登入按钮的监听器，接收授权结果。
	 */
	private class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken != null && mAccessToken.isSessionValid()) {
				LogUtil.i("Enid", "微博官方登录按钮监听");
				AccessTokenKeeper.writeAccessToken(getApplicationContext(),
						mAccessToken);
				getWeiboUserMsg();
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			cancelProgressDialog();
			Toast.makeText(LoginActivity.this, "微博授权失败", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onCancel() {
			cancelProgressDialog();
			Toast.makeText(LoginActivity.this, "取消授权", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * 微博登录实现异步请求接口回调，并在回调中直接解析User信息
	 */
	private RequestListener mListener = new RequestListener() {

		@Override
		public void onWeiboException(WeiboException e) {
			toastMsg("获取用户信息失败");
			cancelProgressDialog();
		}

		@Override
		public void onComplete(String response) {
			if (!TextUtils.isEmpty(response)) {
				user = User.parse(response);
				PreManager.instance().saveUpdateUserInfoState(context, "0");
				userNickName = !TextUtils.isEmpty(user.screen_name) ? user.screen_name
						: "xc_"
								+ SleepUtils.getFormatedDateTime("yyyyMMdd",
										System.currentTimeMillis());
				userProfile = !TextUtils.isEmpty(user.profile_image_url) ? user.profile_image_url
						: URLUtil.XIANGCHENG_ICON_URL;
				GetWeiboFanParamClass mParams = new GetWeiboFanParamClass();
				mParams.access_token = mAccessToken.getToken();
				mParams.count = 100;
				mParams.page = 1;
				mParams.sort = 0;
				mParams.uid = uid;
				getWeiboFriends(mParams);
			}
		}

	};

	/**
	 * 获取微博好友
	 * 
	 * @param mParams
	 */
	private void getWeiboFriends(GetWeiboFanParamClass mParams) {
		new WeiboDataProClass(LoginActivity.this).getWeiboFan(mParams,
				new InterfaceGetWeiboFanCallBack() {

					@Override
					public void onSuccess(int iCode,
							List<MyWeiBoFriendBaean> mFriendsList, int totapage) {
						if (mFriendsList != null) {
							OtherUsersParamClass otherUsersParamClass = new OtherUsersParamClass();
							otherUsersParamClass.platform = SleepConstants.PLATFORM_WEIBO;
							otherUsersParamClass.my_ext_acc = uid + "";
							otherUsersParamClass.my_ext_nickname = user.screen_name;
							otherUsersParamClass.my_ext_profile = user.profile_image_url;

							String userBirthday = PreManager.instance()
									.getUserBirthday(context);
							try {
								otherUsersParamClass.my_int_age = PreManager
										.birthDayToAge(userBirthday);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							otherUsersParamClass.my_int_gender = PreManager
									.instance().getUserGender(
											LoginActivity.this)
									+ "";
							otherUsersParamClass.my_int_occupation = PreManager
									.instance().getUserProfession(context) + "";
							// 微博好友微博uid集合
							ArrayList<String> ids = new ArrayList<String>();
							for (MyWeiBoFriendBaean friend : mFriendsList) {
								ids.add(friend.id);
							}

							otherUsersParamClass.friend_num = ids.size() + "";
							otherUsersParamClass.friendacc_x = ids;

							Message msg = new Message();
							msg.what = SAVE_THIRD_PARTY_INFO;
							msg.obj = otherUsersParamClass;
							mHandler.sendMessage(msg);
						}
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						cancelProgressDialog();
					}
				});
	}

	/**
	 * 
	 * 微博注销按钮的监听器，接收注销处理结果。（API请求结果的监听器）
	 */
	@SuppressWarnings("unused")
	private class LogOutRequestListener implements RequestListener {
		@Override
		public void onComplete(String response) {
			if (!TextUtils.isEmpty(response)) {
				try {
					JSONObject obj = new JSONObject(response);
					String value = obj.getString("result");

					if ("true".equalsIgnoreCase(value)) {
						AccessTokenKeeper.clear(LoginActivity.this);

						mAccessToken = null;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
		}
	}

	/**
	 * 平台登陆新街口
	 */
	public void doLogins(final String my_phone_num, final String my_int_pwd) {
		pb = new ProgressUtils(context);
		pb.setCanceledOnTouchOutside(false);
		pb.setMessage("正在登录");
		pb.show();
		new XiangchengProcClass(LoginActivity.this).phoneLogin(my_phone_num, my_int_pwd,
				new InterfacePhoneLoginCallback() {

					@Override
					public void onSuccess(int icode, UserInfoBean userInfo, List<UserInfoPhotoBean> photos) {

						if (userInfo.getIsaddinfo().equals("1")) {// 完善个人信息
							pb.dismiss();
							Intent intent = new Intent(LoginActivity.this,
									PerfectUserDataActivity.class);
							intent.putExtra("userName", userInfo.getNickname());
							intent.putExtra("userAge", userInfo.getAge());
							intent.putExtra("userGeander", userInfo.getGender());
							startActivity(intent);
						} else {
							loginHX(userInfo.getInt_id(), "123456");
						}
						doFileData();
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						pb.dismiss();
						toastMsg(strErrMsg);

					}
				});
	}

	/**
	 * 获取文件下载路径
	 */
	private void doFileData() {
		final String uid = PreManager.instance().getUserId(context);
		new CommunityProcClass(context).getTiaocanFileUrl(uid,
				new InterfaceGetTiaocanCallBack() {

					@Override
					public void onSuccess(int icode, String tiaocanfile) {
						ScreenShot screenShot = new ScreenShot();
						screenShot.doDownloadFile(tiaocanfile);
					}

					@Override
					public void onError(int icode, String strErrMsg) {
					}
				});
	}

	/**
	 * 保存第三方登录好友信息
	 * 
	 */
	private void saveThirdPartyUserInfo(
			OtherUsersParamClass otherUsersParamClass) {
		new UserManagerProcClass(context).CheckPhoneRegStat(
				otherUsersParamClass, new InterfaceSaveOtherUsersCallBack() {

					@Override
					public void onReturn4032(
							OtherUsersReturn4032RstClass rst4032Class) {

						PreManager.instance().saveUserId(context,
								rst4032Class.int_id);
						PreManager.instance().savePlatformBoundMsg(
								context,
								rst4032Class.user_ext_acc_weibo
										+ rst4032Class.user_ext_acc_wechat
										+ rst4032Class.user_ext_acc_qq
										+ rst4032Class.user_ext_acc_cellphone);
						if (rst4032Class.user_ext_acc_cellphone_new != null) {
							PreManager.instance().saveBoundPhoneNumber(context,
									rst4032Class.user_ext_acc_cellphone_new);
						}
						PreManager.instance().saveUserNickname(context, userNickName);
						PreManager.instance().saveUserProfileUrl(context, userProfile);
						PreManager.instance().saveUserProfileKey(context, rst4032Class.profile_key);
						PreManager.instance().saveUserIsExpert(context, rst4032Class.is_zj);
						PreManager.instance().saveUserOccupation(context, rst4032Class.occupation == null ? "" : rst4032Class.occupation);
						PreManager .instance() .saveUserBirthday(context, rst4032Class.user_internal_birthday == null ? "" : rst4032Class.user_internal_birthday);
						PreManager.instance().saveUserHeight(
								context, rst4032Class.user_internal_height == null ? "" : rst4032Class.user_internal_height);
						PreManager.instance().saveUserWeight(context,
								rst4032Class.user_internal_weight == null ? "" : rst4032Class.user_internal_weight);
						PreManager.instance().saveUserIsAssess(
								context, "1".equals(rst4032Class.ispinggu) ? "1" : "0");
						PreManager.instance().saveIsCompleteSleepPg(context, "1".equals(rst4032Class.sleep_pg) ? true : false);
						PreManager.instance().saveUserDadaDays(context, rst4032Class.dakadays);
						PreManager.instance().saveIsOpenFormuInform(context, rst4032Class.jgtssz);
						PreManager.instance().saveGetupTime_Setting(context, rst4032Class.wakeup);
						PreManager.instance().saveSleepTime_Setting(context, rst4032Class.sleep);
						if (rst4032Class.isaddinfo.equals("1")) {
							Intent intent = new Intent(LoginActivity.this, PerfectUserDataActivity.class);
							startActivity(intent);
						} else {
							loginHX(rst4032Class.int_id, "123456");
							doFileData();
						}
					}

					@Override
					public void onReturn4031(
							OtherUsersReturn4031RstClass rst4031Class) {
						System.out.println("rst4031Class:" + "int_id"
								+ rst4031Class.int_id);
						System.out.println("rst4031Class:" + "体重"
								+ rst4031Class.user_internal_weight);
						System.out.println("rst4031Class:" + "身高"
								+ rst4031Class.user_internal_height);
						PreManager.instance().saveUserId(context,
								rst4031Class.int_id);
						PreManager.instance().savePlatformBoundMsg(
								context,
								rst4031Class.user_ext_acc_weibo
										+ rst4031Class.user_ext_acc_wechat
										+ rst4031Class.user_ext_acc_qq
										+ rst4031Class.user_ext_acc_cellphone);
						if (rst4031Class.user_ext_acc_cellphone_new != null) {
							PreManager.instance().saveBoundPhoneNumber(context,
									rst4031Class.user_ext_acc_cellphone_new);
						}
						PreManager.instance().saveUserNickname(context,
								rst4031Class.nickname);
						PreManager.instance().saveUserProfileUrl(context,
								rst4031Class.profile);
						PreManager.instance().saveUserProfileKey(context,
								rst4031Class.profile_key);
						PreManager.instance().saveUserIsExpert(context,
								rst4031Class.is_zj);
						PreManager.instance().saveUserOccupation(
								context,
								rst4031Class.occupation == null ? ""
										: rst4031Class.occupation);
						PreManager
								.instance()
								.saveUserBirthday(
										context,
										rst4031Class.user_internal_birthday == null ? ""
												: rst4031Class.user_internal_birthday);
						PreManager.instance().saveUserHeight(
								context,
								rst4031Class.user_internal_height == null ? ""
										: rst4031Class.user_internal_height);
						PreManager.instance().saveUserWeight(
								context,
								rst4031Class.user_internal_weight == null ? ""
										: rst4031Class.user_internal_weight);
						PreManager.instance().saveUserIsAssess(
								context,
								"1".equals(rst4031Class.ispinggu) ? "1"
										: "0");
						
						PreManager.instance().saveIsCompleteSleepPg(context, "1".equals(rst4031Class.sleeppg) ? true : false);
						PreManager.instance().saveUserDadaDays(context,
								rst4031Class.dakadays);
						PreManager.instance().saveIsOpenFormuInform(context,
								rst4031Class.jgtssz);
						PreManager.instance().saveGetupTime_Setting(context, rst4031Class.wakeup);
						PreManager.instance().saveSleepTime_Setting(context, rst4031Class.sleep);
						if (rst4031Class.isaddinfo.equals("1")) {
							Intent intent = new Intent(LoginActivity.this,
									PerfectUserDataActivity.class);
							startActivity(intent);
						} else {
							loginHX(rst4031Class.int_id, "123456");
							doFileData();
						}
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						ToastManager.getInstance(context).show(strErrMsg);
						cancelProgressDialog();
					}

					@Override
					public void onSuccess(int iCode) {
						cancelProgressDialog();
					}
				});
	}

	/**
	 * 环信登录
	 * 
	 * @param userName
	 * @param password
	 */
	private void loginHX(String userName, String password) {
		EMChatManager.getInstance().login(userName, password, new EMCallBack() {// 回调
					@Override
					public void onSuccess() {
						if (pb != null) {
							pb.dismiss();
						}

						runOnUiThread(new Runnable() {
							public void run() {
								uploadSleepTimeSetting(context);
								toastMsg("登录成功");
								PreManager.instance()
										.saveIsLogin(context, true);// 保存登录成功信息
								EMChatManager.getInstance()
										.loadAllConversations();
								// ------
								if (getIntent() != null) {
									if (getIntent().getBooleanExtra("is_goto_ublish_activity", false)) {
									}
								}
								Intent intent = new Intent();
								intent.setAction(Constant.RECEVER_LOGIN_ACTION);
								sendBroadcast(intent);
								AppManager.getAppManager().finishActivity(LoginActivity.class);
							}
						});
					}

					@Override
					public void onProgress(int progress, String status) {
					}

					@Override
					public void onError(int code, String message) {
						runOnUiThread(new Runnable() {
							public void run() {
								toastMsg("登录失败");
								if (pb != null) 
									pb.dismiss();
							}
						});
					}
				});
	}

	@Override
	public void onReq(BaseReq arg0) {

	}

	@Override
	public void onResp(BaseResp arg0) {

	}

	public static void uploadSleepTimeSetting(Context context) {
		UploadSleepTimeSettingParamClass mParam = new UploadSleepTimeSettingParamClass();
		mParam.my_int_id = PreManager.instance().getUserId(context);
		mParam.sleep_date = PreManager.instance().getSleepTime_Setting(context);
		mParam.wakeup_date = PreManager.instance()
				.getGetupTime_Setting(context);
		new UserManagerProcClass(context).UploadSleepTimeSetting(mParam,
				new InterfaceUploadSleepTimeSettingCallBack() {

					@Override
					public void onSuccess(int iCode, String strSuccMsg) {
					}

					@Override
					public void onError(int icode, String strErrMsg) {
					}
				});
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("Login"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
											// onPageEnd 在onPause
											// 之前调用,因为 onPause
											// 中会保存信息
		MobclickAgent.onPause(this);
	}

	/**
	 * 获取验证码--调用internal_signup_1上传手机号
	 * 
	 * @param phoneNumber
	 *            手机号码
	 */
	private void getVerifyCode(final String phoneNumber) {

		new PhoneProcClass(context).RequestRegCode(phoneNumber,
				new InterfaceRequestRegCodeCallBack() {

					@Override
					public void onSuccess(int iCode) {
						timeCount.start();
					}

					@Override
					public void onError(int icode, String strErrMsg) {// 获取验证码失败，提示手机号无效，清空输入数据
						ToastManager.getInstance(context).show("手机注册无效，请重新输入");
					}
				});
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			btn_get_code.setText("获取验证码");
			btn_get_code.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			btn_get_code.setClickable(false);
			btn_get_code.setText("重新发送" + millisUntilFinished / 1000 + "s");
		}
	}
}
