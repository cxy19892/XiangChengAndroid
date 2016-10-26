package com.yzm.sleep.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.widget.LoginButton;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendAuth.Resp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AccessTokenKeeper;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.MyWeiBoFriendBaean;
import com.yzm.sleep.model.WeiboFriendsInfo;
import com.yzm.sleep.utils.HLog;
import com.yzm.sleep.utils.InterFaceThirdUtilsClass.GetWeiboFanParamClass;
import com.yzm.sleep.utils.InterFaceThirdUtilsClass.InterfaceGetWeiboFanCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceOwerSaveOtherAccURLCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.OwerSaveOtherAccParamClass;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.SleepConstants;
import com.yzm.sleep.utils.ToastManager;
import com.yzm.sleep.utils.UserInfoUtil;
import com.yzm.sleep.utils.UserManagerProcClass;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.WeiboDataProClass;

public class CommunityAccountActivity extends BaseActivity implements
		IWXAPIEventHandler {
	/** IWXAPI 是第三方app和微信通信的openapi接口 */
	private IWXAPI wxapi;
	private String platform = null;
	private ProgressUtils pb = null;
	
	/** 新浪授权认证信息 */
	private AuthInfo authInfo;
	/** 新浪access_token */
	private Oauth2AccessToken mAccessToken;
	/** 新浪登陆认证对应的listener */
	private AuthListener mLoginWeiboListener = new AuthListener();
	/** 新浪需要查询用户的ID */
	private long uid;
	private LoginButton btn_bound_weibo;
	private Button btn_bound_weixin,btn_bound_qq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_community_account);
		((TextView)(findViewById(R.id.title))).setText("社交账户");
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AppManager.getAppManager().finishActivity();
			}
		});
		initViews();
		initThreadBoundData();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (pb != null) {
			pb.cancel();
		}
	}
	
	private void initViews(){
//		findViewById(R.id.rl_bound_weixin).setOnClickListener(new PlatformBoundOnClick(SleepConstants.PLATFORM_WEIXIN, true));
//		findViewById(R.id.rl_bound_qq).setOnClickListener(new PlatformBoundOnClick(SleepConstants.PLATFORM_WEIXIN, true));
		btn_bound_weibo = (LoginButton)findViewById(R.id.btn_bound_weibo);
		btn_bound_weixin = (Button)findViewById(R.id.btn_bound_weixin);
		btn_bound_weixin.setOnClickListener(new PlatformBoundOnClick(SleepConstants.PLATFORM_WEIXIN, true));
		btn_bound_qq = (Button)findViewById(R.id.btn_bound_qq);
		btn_bound_qq.setOnClickListener(new PlatformBoundOnClick(SleepConstants.PLATFORM_QQ, true));
		
		btn_bound_weibo.setExternalOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				weiboLogin();
			}
		});
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("LinkSocialApp"); 
		MobclickAgent.onResume(this);
		initCouumunityAccount();
		
		if ("1".equals(PreManager.instance()
				.getOtherPlatformLoginState(this))) {
		}
		cancelProgressDialog();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("LinkSocialApp");
		MobclickAgent.onPause(this);
	}

	private void initCouumunityAccount() {
		String platformBoundMsg = PreManager.instance().getPlatformBoundMsg(
				CommunityAccountActivity.this);
		boolean isWeiBoBound = UserInfoUtil.isCommunityPlatformBound(
				platformBoundMsg, 0);
		boolean isWeixinBound = UserInfoUtil.isCommunityPlatformBound(
				platformBoundMsg, 1);
		boolean isQQBound = UserInfoUtil.isCommunityPlatformBound(
				platformBoundMsg, 2);
		int colorYes = getResources().getColor(R.color.fct_color);
		int colorNo = getResources().getColor(R.color.ct_color);
		btn_bound_weibo.setBackgroundDrawable(null);
		
		if (isWeiBoBound) {
			btn_bound_weibo.setText("已关联");
			btn_bound_weibo.setTextColor(colorYes);
		}else {
			btn_bound_weibo.setText("关联");
			btn_bound_weibo.setTextColor(colorNo);
		}
		
		if (isWeixinBound) {
			btn_bound_weixin.setText("已关联");
			btn_bound_weixin.setTextColor(colorYes);
		}else {
			btn_bound_weixin.setText("关联");
			btn_bound_weixin.setTextColor(colorNo);
		}
		
		if (isQQBound) {
			btn_bound_qq.setText("已关联");
			btn_bound_qq.setTextColor(colorYes);
		}else {
			btn_bound_qq.setText("关联");
			btn_bound_qq.setTextColor(colorNo);
		}

	}
	
	
	
	private void initThreadBoundData() {
		// 微博登录
		String scope = "email,direct_messages_read,direct_messages_write,"
				+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
				+ "follow_app_official_microblog," + "invitation_write";
		authInfo = new AuthInfo(CommunityAccountActivity.this, SleepConstants.SINA_SLEEP_APP_KEY,
				"http://www.sina.com", scope);
		btn_bound_weibo.setWeiboAuthInfo(authInfo, mLoginWeiboListener);// 为按钮设置授权认证信息

		// QQ空间登录
		if (mTencent == null) {
			mTencent = Tencent.createInstance(
					SleepConstants.TENCENT_SLEEP_APP_ID, CommunityAccountActivity.this);
		}

		// 微信登录
		wxapi = WXAPIFactory.createWXAPI(CommunityAccountActivity.this,
				SleepConstants.WECHAT_SLEEP_APP_ID, false);// 通过WXAPIFactory工厂，获取IWXAPI的实例
		if (!wxapi.isWXAppInstalled()) {
			// 提醒用户没有按照微信
			return;
		}
		wxapi.registerApp(SleepConstants.WECHAT_SLEEP_APP_ID); // 将该app注册到微信
		wxapi.handleIntent(getIntent(), this);
	}

	/**
	 * 社交平台绑定监听
	 * 
	 * @author Administrator
	 * 
	 */
	public class PlatformBoundOnClick implements OnClickListener {
		private String platform = null;
		private boolean isOtherPlatform = true;

		public PlatformBoundOnClick(String platformID, boolean isOtherPlatform) {
			this.isOtherPlatform = isOtherPlatform;
			platform = platformID;
		}

		@Override
		public void onClick(View v) {
			MobclickAgent.onEvent(CommunityAccountActivity.this, "623");
			if (isOtherPlatform) {// 用于已有账户绑定微博、qq、微信号
				if (platform.equals(SleepConstants.PLATFORM_WEIBO)) {

				} else if (platform.equals(SleepConstants.PLATFORM_WEIXIN)) {
//					if (wxapi.isWXAppInstalled()) {
//						showProgressDialog();
//						SendAuth.Req req = new SendAuth.Req();
//						req.scope = "snsapi_userinfo";
//						req.state = "wechat_sdk_demo_test";
//						wxapi.sendReq(req);
//					}else {
//						Util.show(CommunityAccountActivity.this, "请先安装微信客户端");
//					}
					
					showProgressDialog();
					SendAuth.Req req = new SendAuth.Req();
					req.scope = "snsapi_userinfo";
					req.state = "wechat" + new Random(100).nextInt();
					wxapi.sendReq(req);
					if (!wxapi.isWXAppInstalled()) {
						cancelProgressDialog();
						ToastManager.getInstance(CommunityAccountActivity.this).show("请先安装微信客户端");
						return;
					}
				} else if (platform.equals(SleepConstants.PLATFORM_QQ)) {
					if (Util.isQQInstalled(CommunityAccountActivity.this)) {
						showProgressDialog();
						QQ_Login();
					}else {
						Util.show(CommunityAccountActivity.this, "请先安装qq客户端");
					}
				}
			} 

		}

	}
	
	private void weiboLogin() {
		showProgressDialog();
	}
	
	private void showProgressDialog() {
		pb = new ProgressUtils(this);
		pb.setCanceledOnTouchOutside(false);
		pb.show();
	}

	private void cancelProgressDialog() {
		if (pb != null) {
			pb.cancel();
		}
	}
	/**
	 * 第三方平台绑定，调用ttrans_platform.php接口，上传用户int_id及需要绑定的平台编号及相关平台返回唯一编号
	 * 
	 * @param target_int_id
	 *            绑定目标主体的内部ID号
	 * @param platform
	 *            第三方平台编号
	 * @param my_ext_acc
	 *            第三方账户账号
	 * @param friend_num
	 *            第三方平台上用户好友的数量
	 * @param friendacc_x
	 *            第三方平台上用户好友的账户账号
	 */
	private void threadPlatformBound(String target_int_id, final String platfrom,
			String my_ext_acc, int friend_num, ArrayList<String> friendacc_x) {
		OwerSaveOtherAccParamClass owerSaveOtherAccParamClass = new OwerSaveOtherAccParamClass();
		owerSaveOtherAccParamClass.friend_num = friend_num + "";
		owerSaveOtherAccParamClass.friendacc_x = friendacc_x;
		owerSaveOtherAccParamClass.my_ext_acc = my_ext_acc;
		owerSaveOtherAccParamClass.platform = platfrom;
		owerSaveOtherAccParamClass.target_int_id = target_int_id;

		new UserManagerProcClass(CommunityAccountActivity.this).OwerSaveOtherAcc(
				owerSaveOtherAccParamClass,
				new InterfaceOwerSaveOtherAccURLCallBack() {

					@Override
					public void onSuccess(int iCode, String iSum) {
						cancelProgressDialog();
						String boundMsg = PreManager.instance().getPlatformBoundMsg(CommunityAccountActivity.this);
						String newBoundMsg = "";
						if (platform.equals(SleepConstants.PLATFORM_WEIBO)) {
							newBoundMsg = "1" +boundMsg.substring(1);
						}else if(platform.equals(SleepConstants.PLATFORM_QQ)){
							newBoundMsg = boundMsg.substring(0,2) + "1" + boundMsg.substring(3);
						}
						
						PreManager.instance().savePlatformBoundMsg(CommunityAccountActivity.this, newBoundMsg);
						toastMsg("绑定成功");
						initCouumunityAccount();
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						cancelProgressDialog();
						toastMsg(strErrMsg);
					}
				});
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////
	// ============================QQ绑定方法调用开始============================

	private static Tencent mTencent;
	private UserInfo mQQUserInfo;
	private static String openId;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		private JSONObject response;

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				response = (JSONObject) msg.obj;
				if (response.has("nickname")) {
					String int_id = PreManager.instance().getUserId(
							CommunityAccountActivity.this);
					platform = SleepConstants.PLATFORM_QQ;
					// QQ绑定
					if (!TextUtils.isEmpty(int_id)
							&& !TextUtils.isEmpty(platform)) {
						threadPlatformBound(int_id, platform, openId, 0,
								new ArrayList<String>());
					}
				}
			} else if (msg.what == 2) {
				// Bitmap bitmap = (Bitmap) msg.obj;
			}
		}
	};

	/**
	 * QQ登录
	 */
	private void QQ_Login() {
		if (!mTencent.isSessionValid()) {
			mTencent.login(CommunityAccountActivity.this, "all", loginQQListener);
		} else {
			mTencent.logout(CommunityAccountActivity.this);
			updateUserInfo();
			// updateLoginButton();
		}
	}

	IUiListener loginQQListener = new BaseUiListener() {
		@Override
		protected void doComplete(JSONObject values) {
			Log.d("SDKQQAgentPref",
					"AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
			initQQOpenidAndToken(values);
			updateUserInfo();
			getOpenId(token);
			getQQFriends(token, SleepConstants.TENCENT_SLEEP_APP_ID, openId);
			// updateLoginButton();
		}
	};

	private static String token;

	/**
	 * qq登录 当自定义的监听器实现IUiListener接口后，必须要实现接口的三个方法， onComplete onCancel onError
	 * 分别表示第三方登录成功，取消 ，错误。
	 * 
	 **/

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			if (null == response) {
				// Util.showResultDialog(MainActivity.this, "返回为空", "登录失败");
				return;
			}
			JSONObject jsonResponse = (JSONObject) response;
			if (null != jsonResponse && jsonResponse.length() == 0) {
				// Util.showResultDialog(MainActivity.this, "返回为空", "登录失败");
				return;
			}
			// Util.showResultDialog(MainActivity.this, response.toString(),
			// "登录成功");
			doComplete((JSONObject) response);

		}

		protected void doComplete(JSONObject values) {
			// Util.toastMessage(activity, "登录成功" );
			updateUserInfo();
		}

		@Override
		public void onError(UiError e) {
			cancelProgressDialog();
		}

		@Override
		public void onCancel() {
			cancelProgressDialog();
		}
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
					msg.what = 1;
					mHandler.sendMessage(msg);
				}

				@Override
				public void onCancel() {
					cancelProgressDialog();
				}
			};
			mQQUserInfo = new UserInfo(CommunityAccountActivity.this, mTencent.getQQToken());// 获取UserInfo对象
			mQQUserInfo.getUserInfo(listener);// 得到了userInfo对象后，调用它的getUserInfo（iuilistener）方法就可以得到用户的基本信息

		} else {
			cancelProgressDialog();
		}
	}

	/**
	 * 获取QQ用户openid
	 * 
	 * @param access_token
	 */
	private void getOpenId(final String access_token) {

	}

	/**
	 * 获取QQ好友信息
	 * 
	 * @param access_token
	 * @param oauth_consumer_key
	 * @param openid
	 */
	private void getQQFriends(final String access_token,
			final String oauth_consumer_key, final String openid) {
	}

	// ============================QQ绑定方法调用结束============================

	// ============================微博绑定方法调用开始============================
	/**
	 * 当 SSO 授权 Activity 退出时，该函数被调用。
	 * 
	 * @see {@link Activity#onActivityResult}
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (btn_bound_weibo != null) {
			if (btn_bound_weibo instanceof LoginButton) {
				MobclickAgent.onEvent(CommunityAccountActivity.this, "623");
				((LoginButton) btn_bound_weibo).onActivityResult(requestCode,
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
				// updateLoginButton();
			}
		}

	}

	public void getDataForWeiboBoundResult(int requestCode, int resultCode,
			Intent data) {

		if (btn_bound_weibo != null) {
			if (btn_bound_weibo instanceof LoginButton) {
				((LoginButton) btn_bound_weibo).onActivityResult(requestCode,
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
				// updateLoginButton();
			}
		}

	}

	/**
	 * 微博登入按钮的监听器，接收授权结果。
	 */
	private class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken != null && mAccessToken.isSessionValid()) {

				// String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
				// .format(new java.util.Date(mAccessToken
				// .getExpiresTime()));
				// String format = "Token：%1$s \n有效期：%2$s";
				AccessTokenKeeper.writeAccessToken(CommunityAccountActivity.this
						.getApplicationContext(), mAccessToken);
				getWeiboUserMsg();
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			cancelProgressDialog();
			Toast.makeText(CommunityAccountActivity.this, "微博授权失败", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel() {
			cancelProgressDialog();
			Toast.makeText(CommunityAccountActivity.this, "取消授权", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 获取微博用户信息
	 */
	private void getWeiboUserMsg() {
		UsersAPI mUsersAPI = new UsersAPI(this, SleepConstants.SINA_SLEEP_APP_KEY, mAccessToken);// 获取用户信息接口

		uid = Long.parseLong(mAccessToken.getUid());
		mUsersAPI.show(uid, mListener);

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
						AccessTokenKeeper.clear(CommunityAccountActivity.this);
						mAccessToken = null;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			// mTokenView.setText("注销失败");
		}
	}

	/**
	 * 微博登录实现异步请求接口回调，并在回调中直接解析User信息
	 */
	// private User user;
	private RequestListener mListener = new RequestListener() {

		@Override
		public void onWeiboException(WeiboException e) {
			Util.show(CommunityAccountActivity.this, "获取用户信息失败");
			cancelProgressDialog();
		}

		@Override
		public void onComplete(String response) {
			if (!TextUtils.isEmpty(response)) {
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
	 * 获取微博好友uid
	 * 
	 * @param access_token
	 * @param uid
	 */
	private void getWeiboFriends11111(final String access_token, final long uid) {
		AsyncTask<Void, Void, WeiboFriendsInfo> task = new AsyncTask<Void, Void, WeiboFriendsInfo>() {

			@Override
			protected WeiboFriendsInfo doInBackground(Void... params) {

				return null;
			}

			@Override
			protected void onPostExecute(WeiboFriendsInfo result) {
				super.onPostExecute(result);
				if (result != null) {
					String int_id = PreManager.instance().getUserId(
							CommunityAccountActivity.this);
					platform = SleepConstants.PLATFORM_WEIBO;
					// 微博绑定
					if (!TextUtils.isEmpty(int_id)
							&& !TextUtils.isEmpty(platform)) {
						threadPlatformBound(int_id, platform, uid + "",
								result.ids.size(), result.ids);
					}
				}
			}
		};

		task.execute();
		GetWeiboFanParamClass mParams = new GetWeiboFanParamClass();
		mParams.access_token = mAccessToken.getToken();
		mParams.count = 100;
		mParams.page = 1;
		mParams.sort = 0;
		mParams.uid = uid;
		getWeiboFriends(mParams);
	}
	
	/**
	 * 获取微博好友
	 * 
	 * @param mParams
	 */
	private void getWeiboFriends(GetWeiboFanParamClass mParams) {
		new WeiboDataProClass(CommunityAccountActivity.this).getWeiboFan(mParams,
				new InterfaceGetWeiboFanCallBack() {

					@Override
					public void onSuccess(int iCode,
							List<MyWeiBoFriendBaean> mFriendsList, int totapage) {
						if (mFriendsList != null) {
							String int_id = PreManager.instance().getUserId(
									CommunityAccountActivity.this);
							platform = SleepConstants.PLATFORM_WEIBO;
							
							// 微博好友微博uid集合
							ArrayList<String> ids = new ArrayList<String>();
							for (MyWeiBoFriendBaean friend : mFriendsList) {
								ids.add(friend.id);
							}
							// 微博绑定
							if (!TextUtils.isEmpty(int_id)
									&& !TextUtils.isEmpty(platform)) {
								threadPlatformBound(int_id, platform, uid + "",
										mFriendsList.size(), ids);
							}
						}
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						cancelProgressDialog();
					}
				});
	}

	// ============================微博绑定方法调用结束============================

	// ============================微信绑定方法调用开始============================

	@Override
	public void onReq(BaseReq arg0) {

	}

	@Override
	public void onResp(BaseResp resp) {
		Bundle bundle = new Bundle();
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			resp.toBundle(bundle);
			SendAuth.Resp sp = new Resp(bundle);
			String code = sp.code;
			HLog.i("RankingFragment", "code:" + code);
			break;

		default:
			break;
		}
	}

	// ============================微信绑定方法调用结束============================
}
