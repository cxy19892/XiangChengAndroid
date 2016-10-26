package com.yzm.sleep.wxapi;

import java.text.ParseException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendAuth.Resp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.ScreenShot;
import com.yzm.sleep.activity.PerfectUserDataActivity;
import com.yzm.sleep.activity.ShareActivity;
import com.yzm.sleep.utils.CommunityProcClass;
import com.yzm.sleep.utils.InterFaceThirdUtilsClass.GetWeixinAccessTokenParamClass;
import com.yzm.sleep.utils.InterFaceThirdUtilsClass.GetWeixinUserInfoRstClass;
import com.yzm.sleep.utils.InterFaceThirdUtilsClass.InterfaceGetWeixinAccessTokenCallBack;
import com.yzm.sleep.utils.InterFaceThirdUtilsClass.InterfaceGetWeixinUserInfoCallBack;
import com.yzm.sleep.utils.InterFaceThirdUtilsClass.InterfaceRefreshTokenCallBack;
import com.yzm.sleep.utils.InterFaceThirdUtilsClass.RefreshTokenParamClass;
import com.yzm.sleep.utils.InterFaceThirdUtilsClass.RefreshTokenRstClass;
import com.yzm.sleep.utils.InterFaceThirdUtilsClass.WeixinAccessTokenRstClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetTiaocanCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceOwerSaveOtherAccURLCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceSaveOtherUsersCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.OtherUsersParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.OtherUsersReturn4031RstClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.OtherUsersReturn4032RstClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.OwerSaveOtherAccParamClass;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.SleepConstants;
import com.yzm.sleep.utils.SleepUtils;
import com.yzm.sleep.utils.ToastManager;
import com.yzm.sleep.utils.URLUtil;
import com.yzm.sleep.utils.UserManagerProcClass;
import com.yzm.sleep.utils.WeiXinDataProClass;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	Context context;
	private IWXAPI api;
	private ProgressUtils pb;
	
	private String nickname;
	private String headimgurl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry);
		this.context = this;

		api = WXAPIFactory.createWXAPI(this,
				SleepConstants.WECHAT_SLEEP_APP_ID, false);
		api.registerApp(SleepConstants.WECHAT_SLEEP_APP_ID);
		try {
			api.handleIntent(getIntent(), this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	// 微信发送的请求将回调到onReq方法
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			break;
		default:
			break;
		}
		finish();
	}

	// 发送到微信请求的响应结果将回调到onResp方法
	@Override
	public void onResp(BaseResp resp) {
		int type = resp.getType();
		Bundle bundle = new Bundle();
		String respMesg = "";
		LogUtil.i("Enid", "微信 resp.errCode："  + resp.errCode);
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			pb = new ProgressUtils(this);
			pb.setCanceledOnTouchOutside(false);
			pb.show();
			resp.toBundle(bundle);
			SendAuth.Resp sp = new Resp(bundle);
			String code = sp.code;
			if (type != ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX  ) {
				getAccessToken(code, SleepConstants.WECHAT_SLEEP_APP_ID,
						SleepConstants.WECHAT_SLEEP_APP_SECRET,
						"authorization_code");
			}
			if (type == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
				respMesg = "操作成功";
				AppManager.getAppManager().finishActivity(ShareActivity.class);
			}else {
				respMesg = "授权成功";
			}
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			respMesg = "已取消";
			loginOverEnd();
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			respMesg = "认证失败错误";
			loginOverEnd();
			break;
		case BaseResp.ErrCode.ERR_COMM:
			respMesg = "错误";
			loginOverEnd();
			break;
		case BaseResp.ErrCode.ERR_SENT_FAILED:
			respMesg = "发送失败";
			loginOverEnd();
			break;
		case BaseResp.ErrCode.ERR_UNSUPPORT:
			respMesg = "不支持错误";
			loginOverEnd();
			break;

		default:
			respMesg = "未知错误";
			loginOverEnd();
			break;
		}
		ToastManager.getInstance(getApplicationContext()).show(respMesg);
		if (type == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX ||
				resp.errCode != BaseResp.ErrCode.ERR_OK) {//分享信息到微信
			finish();
		}
	}

	/**
	 * 获取AccessToken
	 * 
	 * @param code
	 * @param AppID
	 * @param AppSecret
	 * @param grant_type
	 */
	
	private void getAccessToken(final String code, final String AppID,
			final String AppSecret, final String grant_type) {
		GetWeixinAccessTokenParamClass mParam = new GetWeixinAccessTokenParamClass();
		mParam.code = code;
		mParam.appid = AppID;
		mParam.secret = AppSecret;
		mParam.grant_type = grant_type;
		new WeiXinDataProClass(this).getAccessToken(mParam, new InterfaceGetWeixinAccessTokenCallBack() {
			
			@Override
			public void onSuccess(int iCode, WeixinAccessTokenRstClass data) {
				getRefreshToken(SleepConstants.WECHAT_SLEEP_APP_ID, "refresh_token", data.refresh_token);
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				loginOverEnd();
				WXEntryActivity.this.finish();
			}
		});
	}

	/**
	 * 刷新AccessToken
	 * 
	 * @param appId
	 * @param grantType
	 * @param refreshToken
	 */
	private void getRefreshToken(final String appId, final String grantType,
			final String refreshToken) {
		RefreshTokenParamClass mParam = new RefreshTokenParamClass();
		mParam.appid = appId;
		mParam.grant_type = grantType;
		mParam.refresh_token = refreshToken;
		new WeiXinDataProClass(this).refreshToken(mParam, new InterfaceRefreshTokenCallBack() {
			
			@Override
			public void onSuccess(int iCode, RefreshTokenRstClass data) {
				getWeiXinUserInfo(data.openid, data.access_token);
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				loginOverEnd();
				WXEntryActivity.this.finish();
			}
		});
	}

	/**
	 * 获取用户基本信息
	 * 
	 * @param openId
	 * @param accessToken
	 */
	private void getWeiXinUserInfo(final String openId, final String accessToken) {
		new WeiXinDataProClass(this).getWeixinUserInfo(openId, accessToken, new InterfaceGetWeixinUserInfoCallBack() {
			
			@Override
			public void onSuccess(int iCode, GetWeixinUserInfoRstClass data) {
				nickname = "";
				String sex = "";
				String age = "";
				headimgurl = "";
				String unionid = "";
				String occupation = "";
				if(!PreManager.instance().getIsLogin(context)){//未登录状态保存用户昵称、头像地址
					nickname = data.nickname;
					nickname = !TextUtils.isEmpty(nickname) ? nickname : "xc_"
							+ SleepUtils.getFormatedDateTime("yyyyMMdd",
									System.currentTimeMillis());
					headimgurl = data.headimgurl;
					headimgurl = !TextUtils.isEmpty(headimgurl) ? headimgurl :
						URLUtil.XIANGCHENG_ICON_URL;
					unionid = data.unionid;
				}
				sex = PreManager.instance().getUserGender(context);
				
				String userBirthday = PreManager.instance().getUserBirthday(context);
				try {
					age = PreManager.birthDayToAge(userBirthday);
				} catch (ParseException e) {
					e.printStackTrace();
					loginOverEnd();
					WXEntryActivity.this.finish();
				}
				occupation = PreManager.instance().getUserProfession(context)
						+ "";
				if (!PreManager.instance().getIsLogin(context)) {// 未登录
					saveThirdPartyUserInfo(SleepConstants.PLATFORM_WEIXIN,
							openId,unionid, nickname, headimgurl, age, sex, occupation,
							"0", new ArrayList<String>());
				} else {// 已登录
					threadPlatformBound(PreManager.instance()
							.getUserId(context),
							SleepConstants.PLATFORM_WEIXIN, openId, 0,
							new ArrayList<String>());
				}
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				loginOverEnd();
				WXEntryActivity.this.finish();
			}
		});
	}

	/**
	 * 保存第三方登录好友信息
	 * 
	 * @param platform
	 *            登录平台
	 * @param my_ext_acc
	 *            平台唯一标识
	 * @param my_ext_nickname
	 *            昵称
	 * @param my_ext_profile
	 *            头像地址
	 * @param my_int_age
	 *            年龄
	 * @param my_int_gender
	 *            性别
	 * @param my_int_occupation
	 *            职业
	 * @param friend_num
	 *            好友数
	 * @param friendacc_1
	 *            好友微信唯一标识
	 */
	private void saveThirdPartyUserInfo(String platform, String my_ext_acc, String my_ext_unionid,
			String my_ext_nickname, String my_ext_profile, String my_int_age,
			String my_int_gender, String my_int_occupation, String friend_num,
			ArrayList<String> friendacc_x) {

		OtherUsersParamClass otherUsersParamClass = new OtherUsersParamClass();
		otherUsersParamClass.platform = platform;
		otherUsersParamClass.my_ext_acc = my_ext_acc;
		otherUsersParamClass.my_ext_unionid = my_ext_unionid;
		otherUsersParamClass.my_ext_nickname = my_ext_nickname;
		otherUsersParamClass.my_ext_profile = my_ext_profile;
		otherUsersParamClass.my_int_age = my_int_age;
		otherUsersParamClass.my_int_gender = my_int_gender;
		otherUsersParamClass.my_int_occupation = my_int_occupation;
		otherUsersParamClass.friend_num = friend_num;
		otherUsersParamClass.friendacc_x = friendacc_x;

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
						PreManager.instance().saveUserNickname(context,
								nickname);
						PreManager.instance().saveUserProfileUrl(context,
								headimgurl);
						PreManager.instance().saveUserProfileKey(context,
								rst4032Class.profile_key);
						PreManager.instance().saveUserIsExpert(context,
								rst4032Class.is_zj);
						PreManager.instance().saveUserIsAssess(context,
								rst4032Class.ispinggu);
						PreManager.instance().saveUserOccupation(
								context,
								rst4032Class.occupation == null ? ""
										: rst4032Class.occupation);
						PreManager
								.instance()
								.saveUserBirthday(
										context,
										rst4032Class.user_internal_birthday == null ? ""
												: rst4032Class.user_internal_birthday);
						PreManager.instance().saveUserHeight(
								context,
								rst4032Class.user_internal_height == null ? ""
										: rst4032Class.user_internal_height);
						PreManager.instance().saveUserWeight(
								context,
								rst4032Class.user_internal_weight == null ? ""
										: rst4032Class.user_internal_weight);
						PreManager.instance().saveUserIsAssess(
								context,
								"1".equals(rst4032Class.ispinggu) ? "1"
										: "0");
						//睡眠评估
						PreManager.instance().saveIsCompleteSleepPg(context, "1".equals(rst4032Class.sleep_pg)?true:false);
						PreManager.instance().saveUserDadaDays(context, rst4032Class.dakadays);
						PreManager.instance().saveIsOpenFormuInform(context,rst4032Class.jgtssz);
						if (rst4032Class.isaddinfo.equals("1")) {
							Intent intent = new Intent(WXEntryActivity.this,
									PerfectUserDataActivity.class);
							startActivity(intent);
						} else {
							loginHX(rst4032Class.int_id, "123456");
							doFileData();
						}
					}

					@Override
					public void onReturn4031(
							OtherUsersReturn4031RstClass rst4031Class) {
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
						PreManager.instance().saveUserIsAssess(context,
								rst4031Class.ispinggu);
						//睡眠评估
						PreManager.instance().saveIsCompleteSleepPg(context, "1".equals(rst4031Class.sleeppg)?true:false);
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
						PreManager.instance().saveUserDadaDays(context, rst4031Class.dakadays);
						PreManager.instance().saveIsOpenFormuInform(context,rst4031Class.jgtssz);
						if (rst4031Class.isaddinfo.equals("1")) {
							Intent intent = new Intent(WXEntryActivity.this,
									PerfectUserDataActivity.class);
							startActivity(intent);
						} else {
							loginHX(rst4031Class.int_id, "123456");
							doFileData();
						}
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						cancelProgressDialog();
						WXEntryActivity.this.finish();
						ToastManager.getInstance(context).show(strErrMsg);
						loginOverEnd();
					}

					@Override
					public void onSuccess(int iCode) {
						cancelProgressDialog();
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
						LogUtil.i("masong-1", "登陆成功。获取下载文件地址：" + tiaocanfile);
						ScreenShot screenShot = new ScreenShot();
						screenShot.doDownloadFile(tiaocanfile);
					}

					@Override
					public void onError(int icode, String strErrMsg) {
//						ToastManager.getInstance(context).show(
//								"下载文件路径：" + strErrMsg);
					}
				});
	}
	
	private void cancelProgressDialog() {
		if (pb != null) {
			pb.dismiss();
		}
	}

	private void loginHX(String userName, String password) {
		EMChatManager.getInstance().login(userName, password, new EMCallBack() {// 回调
					@Override
					public void onSuccess() {
						runOnUiThread(new Runnable() {
							public void run() {
								cancelProgressDialog();
								Log.d("main", "登陆聊天服务器成功！");
								ToastManager.getInstance(context).show("登录成功");
								PreManager.instance().saveIsLogin(context, true);//// 保存登录成功信息
								try {
									EMChatManager.getInstance()
									.loadAllConversations();
								} catch (Exception e) {
									e.printStackTrace();
									loginOverEnd();
									WXEntryActivity.this.finish();
								}
								
								//通知LoginActivity登录成功
								Message msg =new Message();
								msg.what = 100;
								WXEntryActivity.this.finish();
								//发送登录成功的广播
								Intent intent = new Intent();
								intent.setAction(Constant.RECEVER_LOGIN_ACTION);
								sendBroadcast(intent);
							}
						});
					}

					@Override
					public void onProgress(int progress, String status) {

					}

					@Override
					public void onError(int code, String message) {
						Log.d("main", "登陆聊天服务器失败！");
						cancelProgressDialog();
						WXEntryActivity.this.finish();
						loginOverEnd();
					}
				});
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
	private void threadPlatformBound(String target_int_id,
			final String platfrom, String my_ext_acc, int friend_num,
			ArrayList<String> friendacc_x) {
		OwerSaveOtherAccParamClass owerSaveOtherAccParamClass = new OwerSaveOtherAccParamClass();
		owerSaveOtherAccParamClass.friend_num = friend_num + "";
		owerSaveOtherAccParamClass.friendacc_x = friendacc_x;
		owerSaveOtherAccParamClass.my_ext_acc = my_ext_acc;
		owerSaveOtherAccParamClass.platform = platfrom;
		owerSaveOtherAccParamClass.target_int_id = target_int_id;

		new UserManagerProcClass(context).OwerSaveOtherAcc(
				owerSaveOtherAccParamClass,
				new InterfaceOwerSaveOtherAccURLCallBack() {

					@Override
					public void onSuccess(int iCode, String iSum) {
						ToastManager.getInstance(context).show("绑定成功");
						String boundMsg = PreManager.instance()
								.getPlatformBoundMsg(context);
						String newBoundMsg = "";
						newBoundMsg = boundMsg.substring(0, 1) + "1"
								+ boundMsg.substring(2);
						PreManager.instance().savePlatformBoundMsg(context,
								newBoundMsg);
						cancelProgressDialog();
						WXEntryActivity.this.finish();
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						ToastManager.getInstance(context).show(strErrMsg);
						cancelProgressDialog();
						WXEntryActivity.this.finish();
					}
				});
	}
	
	private void loginOverEnd(){
		PreManager.instance().saveOtherPlatformLoginState(context, "1");
	}

}
