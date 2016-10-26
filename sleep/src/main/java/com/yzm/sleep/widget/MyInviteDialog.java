package com.yzm.sleep.widget;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AccessTokenKeeper;
import com.yzm.sleep.Constant;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.HomeActivity;
import com.yzm.sleep.adapter.ShareViewAdapter;
import com.yzm.sleep.bean.ShareViewBean;
import com.yzm.sleep.model.ShareObject;
import com.yzm.sleep.utils.ImageCompressUtil;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.SleepConstants;
import com.yzm.sleep.utils.ToastManager;
import com.yzm.sleep.utils.URLUtil;
import com.yzm.sleep.utils.Util;

public class MyInviteDialog extends Dialog implements
		android.view.View.OnClickListener, IWeiboHandler.Response,
		IWXAPIEventHandler {
	/** 普通邀请 SHARETYPE_COMM_INVITE = 0 */
	public static final int SHARETYPE_COMM_INVITE = 0;
	/** 分享睡眠数据 SHARETYTPE_TOPIC_EVENT = 1 */
	public static final int SHARETYTPE_TOPIC_EVENT = 1;
	private Activity activity;
	private Window window;
	public View view;
	private List<ShareViewBean> inits;
	public ShareClassParam mShareClassParam;
	/** 默认分享标题 */
	private String defaultShareTitle = "香橙—银河系首家睡眠管理神器";
	/** 默认分享副标题 */
	private String defaultShareSummary = "让睡觉都变得好玩的睡眠管理APP";
	

	/** 微信分享类型 ，0 分享给微信好友 1分享到朋友圈 */
	private int wechartflag = 1;

	public static class ShareClassParam {
		/**
		 * 分享类型 （所有分享类型的必须传）
		 * 普通邀请   0
		 * 分享睡眠数据   1
		 * 分享话题   2
		 * 分享小组   3
		 */
		public int shareType = -1;
		/** 链接地址（所有分享类型必须传） */
		public String targetUrl = "";
		/** 分享标题（话题、小组分享暂时不用传）*/
		public String shareTitle = "";
		/** 分享副标题 （话题、小组分享暂时不用传）*/
		public String shareSummary = "";
		/** 分享网络图片地址 （不用传）*/
		public String sharePictureUrl = "";
		/** 分享本地图片路径 （不用传）*/
		public String sharePicturePath = "";
		/** 分享图片ID （不用传）*/
		public int sharePictureID;
		/**分享 话题 专家类还是普通类  1-普通，2-专家*/
		public String topicType ;
	}

	/**
	 * 
	 * @param activity
	 * @param x
	 * @param y
	 * @param layoutResId
	 * @param animationStyle
	 * @param gravityId
	 * @param width
	 * @param height
	 * @param shareType
	 *            分享类型 普通邀请 SHARETYPE_COMM_INVITE = 0; 分享睡眠数据
	 *            SHARETYTPE_SLEEP_DATA = 1;
	 */
	public MyInviteDialog(Activity activity, int x, int y, int layoutResId,
			int animationStyle, int gravityId, float width, float height,
			ShareClassParam shareClassParam) {
		super(activity, R.style.CircularCornerDialog);
		this.activity = activity;
		this.mShareClassParam = shareClassParam;
		if (TextUtils.isEmpty(mShareClassParam.sharePictureUrl)) {
			mShareClassParam.sharePictureUrl = URLUtil.XIANGCHENG_ICON_URL;
		}
		if (shareClassParam.shareType == SHARETYPE_COMM_INVITE) {
			initCommShareClassParam();
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		windowDisplay(x, y, animationStyle, gravityId, layoutResId, width,
				height);

	}

	public void windowDisplay(int x, int y, int animationStyle, int gravityId,
			int layoutResId, float width, float height) {
		window = this.getWindow();

		WindowManager.LayoutParams params = window.getAttributes();
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);

		this.view = LayoutInflater.from(activity).inflate(
				R.layout.dialog_invitation, null);
		window.setContentView(this.view);
		if (animationStyle != 0) {
			window.setWindowAnimations(animationStyle);
		}
		window.setBackgroundDrawable(MyApplication.instance().getResources()
				.getDrawable(R.color.transparent));
		if (width == 0 && height != 0) {
			window.setLayout(LayoutParams.WRAP_CONTENT,
					(int) (Constant.screenHeight * height));
		} else if (width != 0 && height == 0) {
			window.setLayout((int) (Constant.screenWidht * width),
					LayoutParams.WRAP_CONTENT);
		} else if (width != 0 && height != 0) {
			window.setLayout((int) (Constant.screenWidht * width),
					(int) (Constant.screenHeight * height));
		} else if (width == 0 && height == 0) {
			window.setLayout(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}

		window.setGravity(gravityId);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = x;
		wl.y = y;
		window.setAttributes(wl);
	}

	/**
	 * 初始化普通分享信息
	 */
	private void initCommShareClassParam() {
		mShareClassParam.targetUrl = URLUtil.XIANGCHENG_APP_DOWNLOAD_URL;
		mShareClassParam.sharePictureID = R.drawable.icon_58x58;
		mShareClassParam.shareTitle = defaultShareTitle;
		mShareClassParam.shareSummary = defaultShareSummary;
		mShareClassParam.sharePictureUrl = URLUtil.XIANGCHENG_ICON_URL;
		mShareClassParam.sharePicturePath = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/xiangcheng/pic/xiangcheng.png";
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inits = new ArrayList<ShareViewBean>();
		ShareViewBean initInfoClass = null;
		for (int i = 0; i < 5; i++) {
			initInfoClass = new ShareViewBean();
			if (i == 0) {
				initInfoClass.type = "微信";
				initInfoClass.id = R.drawable.ic_wx;
			} else if (i == 1) {
				initInfoClass.type = "朋友圈";
				initInfoClass.id = R.drawable.ic_pyq;
			} else if (i == 2) {
				initInfoClass.type = "微博";
				initInfoClass.id = R.drawable.ic_wb;
			} else if (i == 3) {
				initInfoClass.type = "QQ好友";
				initInfoClass.id = R.drawable.ic_qq;
			} else if (i == 4) {
				initInfoClass.type = "QQ空间";
				initInfoClass.id = R.drawable.ic_qzone;
			}

			if (initInfoClass != null) {
				inits.add(initInfoClass);
			}
		}
		findViewById(R.id.dialog_invite_bg).getBackground().setAlpha(245);
		GridView gridView = (GridView) findViewById(R.id.gridView);
		gridView.setOverScrollMode(GridView.OVER_SCROLL_NEVER);
		findViewById(R.id.btn_cancel).setOnClickListener(this);
		gridView.setAdapter(new ShareViewAdapter(activity, inits));
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					if (!wxapi.isWXAppInstalled()) {
						ToastManager.getInstance(activity).show("请先安装微信客户端");
						return;
					}
					initShareObject("weixin", "微信分享信息");
					shareAcessAll();
					doWeixinShare(0);
					if (mShareClassParam.shareType == SHARETYPE_COMM_INVITE) {
						MobclickAgent.onEvent(activity, "500");
					} else if (mShareClassParam.shareType == SHARETYTPE_TOPIC_EVENT) {
					}
					break;
				case 1:
					if (!wxapi.isWXAppInstalled()) {
						ToastManager.getInstance(activity).show("请先安装微信客户端");
						return;
					}
					initShareObject("weixin", "微信朋友圈分享信息");
					shareAcessTitle();
					doWeixinShare(1);
					if (mShareClassParam.shareType == SHARETYPE_COMM_INVITE) {
						MobclickAgent.onEvent(activity, "501");
					} else if (mShareClassParam.shareType == SHARETYTPE_TOPIC_EVENT) {
					}
					break;
				case 2:
//					if (!mWeiboShareAPI.isWeiboAppInstalled()) {
//						ToastManager.getInstance(activity).show("请先安装微博客户端");
//						return;
//					}
					initShareObject("weibo", "微博分享信息");
					shareAcessTitle();
					doWeiboShare();
					if (mShareClassParam.shareType == SHARETYPE_COMM_INVITE) {
						MobclickAgent.onEvent(activity, "502");
					} else if (mShareClassParam.shareType == SHARETYTPE_TOPIC_EVENT) {
					}
					break;
				case 3:
					if (!Util.isQQInstalled(activity)) {
						Util.show(activity, "请先安装qq客户端");
						return;
					}
					initShareObject("qq", "qq分享信息");
					shareAcessAll();
					doQQShare();
					if (mShareClassParam.shareType == SHARETYPE_COMM_INVITE) {
						MobclickAgent.onEvent(activity, "503");
					} else if (mShareClassParam.shareType == SHARETYTPE_TOPIC_EVENT) {
					}
					break;
				case 4:
					if (!Util.isQQInstalled(activity)) {
						Util.show(activity, "请先安装qq客户端");
						return;
					}
					initShareObject("qq", "qq空间分享信息");
					shareAcessAll();
					doQQQzoneShare();
					if (mShareClassParam.shareType == SHARETYPE_COMM_INVITE) {
						MobclickAgent.onEvent(activity, "504");
					} else if (mShareClassParam.shareType == SHARETYTPE_TOPIC_EVENT) {
					}
					break;

				default:
					break;
				}
				cancel();
			}
		});

//		initQQShare();
		initWBShare(savedInstanceState);
		initWXShare();
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
			cancel();
			break;

		default:
			break;
		}
	}

	// ==============================分享=================================================================
	// qq分享变量
	private int shareTypeQzone = QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;
	private int shareTypeQQ = QQShare.SHARE_TO_QQ_TYPE_DEFAULT;
//	private Tencent mTencent;

	// 微博分享变量
	private IWeiboShareAPI mWeiboShareAPI = null;
	private final static int ASK_ERROR = 0;
	private final static int ASK_IMG = 1;
	private final static int ASK_PAGE = 2;

	// 微信分享变量
	private static final String WXAPP_ID = SleepConstants.WECHAT_SLEEP_APP_ID;
	private IWXAPI wxapi;
	private final static int ASK_WXPAGE = 3;

	private ShareObject shareObject;
	private WXMediaMessage mediamsg;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ASK_ERROR:
				ToastManager.getInstance(activity).show("请求分享图片失败");
				break;

			case ASK_IMG:
				weiboMultiMessage.imageObject = (ImageObject) msg.obj;
				synchronized (weiboMultiMessage) {
					if (weiboMultiMessage.mediaObject != null) {
						SendMultiMessageToWeiboRequest multirequest = new SendMultiMessageToWeiboRequest();
						multirequest.transaction = String.valueOf(System
								.currentTimeMillis());
						multirequest.multiMessage = weiboMultiMessage;
//						mWeiboShareAPI.sendRequest(activity,multirequest);
						
						AuthInfo authInfo = new AuthInfo(activity, SleepConstants.SINA_SLEEP_APP_KEY, SleepConstants.REDIRECT_URL, SleepConstants.SCOPE);
			            Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(activity.getApplicationContext());
			            String token = "";
			            if (accessToken != null) {
			                token = accessToken.getToken();
			            }
			            mWeiboShareAPI.sendRequest(activity, multirequest, authInfo, token, new WeiboAuthListener() {
			                
			                @Override
			                public void onWeiboException( WeiboException arg0 ) {
			                	System.out.println("---->WeiboException 2");
			                }
			                
			                @Override
			                public void onComplete( Bundle bundle ) {
			                	System.out.println("---->onComplete 2");
			                    Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
			                    AccessTokenKeeper.writeAccessToken(activity, newToken);
			                    Toast.makeText(activity.getApplicationContext(), "onAuthorizeComplete token = " + newToken.getToken(), 0).show();
			                }
			                
			                @Override
			                public void onCancel() {
			                	System.out.println("---->onCancel 2");
			                	Util.show(activity,"取消分享.");
			                }
			            });
						weiboMultiMessage = null;
					}
				}
				break;

			case ASK_PAGE:
				weiboMultiMessage.mediaObject = (WebpageObject) msg.obj;
				synchronized (weiboMultiMessage) {
					if (weiboMultiMessage.imageObject != null) {
						SendMultiMessageToWeiboRequest multirequest = new SendMultiMessageToWeiboRequest();
						multirequest.transaction = String.valueOf(System
								.currentTimeMillis());
						multirequest.multiMessage = weiboMultiMessage;
//						mWeiboShareAPI.sendRequest(activity,multirequest);
						AuthInfo authInfo = new AuthInfo(activity, SleepConstants.SINA_SLEEP_APP_KEY, SleepConstants.REDIRECT_URL, SleepConstants.SCOPE);
			            Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(activity);
			            String token = "";
			            if (accessToken != null) {
			                token = accessToken.getToken();
			            }
			            mWeiboShareAPI.sendRequest(activity, multirequest, authInfo, token, new WeiboAuthListener() {
			                
			                @Override
			                public void onWeiboException( WeiboException arg0 ) {
			                	System.out.println("---->WeiboException 1");
			                }
			                
			                @Override
			                public void onComplete( Bundle bundle ) {
			                	System.out.println("---->onComplete 1");
			                    Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
			                    AccessTokenKeeper.writeAccessToken(activity.getApplicationContext(), newToken);
			                    Toast.makeText(activity.getApplicationContext(), "onAuthorizeComplete token = " + newToken.getToken(), 0).show();
			                }
			                
			                @Override
			                public void onCancel() {
			                	System.out.println("---->onCancel 1");
			                	Util.show(activity,"取消分享。");
			                }
			            });
						weiboMultiMessage = null;
					}
				}
				break;

			case ASK_WXPAGE:
				mediamsg = (WXMediaMessage) msg.obj;
				synchronized (mediamsg) {
					SendMessageToWX.Req req = new SendMessageToWX.Req();
					req.transaction = String
							.valueOf(System.currentTimeMillis());
					req.message = mediamsg;
					// req.scene = SendMessageToWX.Req.WXSceneTimeline;
					req.scene = wechartflag == 0 ? SendMessageToWX.Req.WXSceneSession
							: SendMessageToWX.Req.WXSceneTimeline;
					wxapi.sendReq(req);
					mediamsg = null;
				}
				break;
			}
		};
	};

	private void initShareObject(String platform, String message) {
		shareObject = new ShareObject(platform, message);
	}

//	private void initQQShare() {
//		mTencent = Tencent.createInstance(SleepConstants.TENCENT_SLEEP_APP_ID,
//				activity);
//	}

	private void initWBShare(Bundle savedInstanceState) {
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(activity,
				SleepConstants.SINA_SLEEP_APP_KEY);// 创建微博分享接口实例
		if (mWeiboShareAPI.isWeiboAppInstalled()) {

			mWeiboShareAPI.registerApp();
		}
		// if (savedInstanceState != null) {
		// mWeiboShareAPI.handleWeiboResponse(activity.getIntent(), this);
		// }
	}

	private void initWXShare() {
		regToWx();
		wxapi.handleIntent(activity.getIntent(), this);
	}

	private void regToWx() {
		wxapi = WXAPIFactory.createWXAPI(activity, WXAPP_ID, true);
		wxapi.registerApp(WXAPP_ID);
	}

	// ----------iuiListener QQ 分享回调方法------
	
	private class BaseUiListener implements IUiListener {

		@Override
		public void onError(UiError e) {
			LogUtil.i("Enid", "分享出错");
			ToastManager.getInstance(activity).show("分享出错");
		}

		@Override
		public void onCancel() {
			LogUtil.i("Enid", "分享取消");
			ToastManager.getInstance(activity).show("分享取消");
		}

		@Override
		public void onComplete(Object arg0) {
			LogUtil.i("Enid", "分享完成");
			ToastManager.getInstance(activity).show("分享完成");
		}
	}

	// ----------iuListener------

	// ---IWeiboHandler.Response 微博分享回调方法----
	@Override
	public void onResponse(BaseResponse baseResp) {
		System.out.println("========微博分享回调方法==========");
		System.out.println("baseResp.errCode:" + baseResp.errCode);
		switch (baseResp.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			ToastManager.getInstance(activity).show("分享成功");
			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			ToastManager.getInstance(activity).show("分享取消");
			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			ToastManager.getInstance(activity).show("分享失败");
			break;
		}

	}

	// ------IWXAPIEventHandler 微信分享回调方法------
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			ToastManager.getInstance(activity).show("正在请求微信服务...");
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			ToastManager.getInstance(activity).show("微信返回信息...");
			break;
		default:
			break;
		}

	}

	@Override
	public void onResp(BaseResp resp) {
		wxapi.handleIntent(activity.getIntent(), this);
		String result = "微信返回信息：";
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = result + "分享成功";
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = result + "分享取消";
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = result + "分享权限不足";
			break;
		default:
			result = result + "DEFAULT";
			break;
		}
		ToastManager.getInstance(activity).show(result);

	}

	// ------IWXAPIEventHandler------

	// -----
	private WeiboMultiMessage weiboMultiMessage;

	/**
	 * 新浪微博分享
	 */
	private void doWeiboShare() {
		if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
			int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
			if (supportApi >= 10351 /* ApiUtils.BUILD_INT_VER_2_2 */) {
				weiboMultiMessage = new WeiboMultiMessage();
				weiboMultiMessage.textObject = getTextObj(shareObject);
				getImageObject(shareObject,0);
				getWebpageObj(shareObject);
			} else {
				ToastManager.getInstance(activity)
				.show("您的sdk版本过低，不支持分享多媒体信息。");
			}
		} else {
			if (android.os.Build.VERSION.SDK_INT > 8) {
				weiboMultiMessage = new WeiboMultiMessage();
				weiboMultiMessage.textObject = getTextObj(shareObject);
				getImageObject(shareObject,0);
				getWebpageObj(shareObject);
			} else {
				ToastManager.getInstance(activity)
				.show("您的sdk版本过低，不支持分享多媒体信息。");
			}
		}
	}

	/**
	 * 分享到QQ空间
	 */
	private void doQQQzoneShare() {
		final Bundle params = new Bundle();
		params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, shareTypeQzone);
		
		params.putString(QzoneShare.SHARE_TO_QQ_TITLE,
				mShareClassParam.shareTitle);
		params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,
				mShareClassParam.shareSummary);
		params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,
				mShareClassParam.targetUrl);
		// QQ空间分享网络图片
		ArrayList<String> imageUrls = new ArrayList<String>();
		imageUrls.add(mShareClassParam.sharePictureUrl);
		params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,
				imageUrls);

		if(HomeActivity.mTencent != null){
			HomeActivity.mTencent.shareToQzone(activity, params, new BaseUiListener());
		}
		
	}

	/**
	 * 分享给QQ好友
	 * */
	private void doQQShare() {
		final Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, shareTypeQQ);
		params.putString(QQShare.SHARE_TO_QQ_TITLE, mShareClassParam.shareTitle);
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY,
				mShareClassParam.shareSummary);
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,
				mShareClassParam.targetUrl);
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,mShareClassParam.sharePictureUrl);
		if(HomeActivity.mTencent != null){
			HomeActivity.mTencent.shareToQQ(activity, params, new BaseUiListener());
		}
		
	}

	/**
	 * 微信分享
	 * @param flag
	 *            0 分享给微信好友 ，1分享到朋友圈
	 */
	private void doWeixinShare(int flag) {
		getWXWebpage(shareObject, flag);
	}

	private TextObject getTextObj(ShareObject object) {
		TextObject textObject = new TextObject();
		textObject.text = object.getMesseage();
		return textObject;
	}

	private void getImageObject(final ShareObject object,final int isSmall) {
		new Thread() {

			@Override
			public void run() {
				Message msg = new Message();
				ImageObject imageObject = new ImageObject();
//				byte[] data;
				try {
//					data = getImageData(object,isSmall);
					Bitmap map = null;
					map = getImageBitmap(object);
					if (map != null) {
						imageObject.setImageObject(map);
						msg.what = ASK_IMG;
						msg.obj = imageObject;
						mHandler.sendMessage(msg);
					}
				} catch (IOException e) {
					e.printStackTrace();
					msg.what = ASK_ERROR;
					mHandler.sendMessage(msg);
					System.out.println("---->IOException 1");
				}
			}

		}.start();
	}

	private byte[] getImageData(ShareObject object,int isSmall) throws IOException {
		 byte[] data = new byte[1024];
		 ByteArrayOutputStream bos = null;
		 URL url = new URL(mShareClassParam.sharePictureUrl);
		 bos = new ByteArrayOutputStream();
		 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		 InputStream in = conn.getInputStream();
		 int len = 0;
		 while((len = in.read(data)) != -1) {
			 bos.write(data,0,len);
		 }
		 byte result[] = bos.toByteArray();
		return ImageCompressUtil.comp32k(BitmapFactory.decodeByteArray(result, 0, result.length));
	}
	private Bitmap getImageBitmap(ShareObject object) throws IOException {
		byte[] data = new byte[1024];
		ByteArrayOutputStream bos = null;
		URL url = new URL(mShareClassParam.sharePictureUrl);
		bos = new ByteArrayOutputStream();
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		InputStream in = conn.getInputStream();
		int len = 0;
		while((len = in.read(data)) != -1) {
			bos.write(data,0,len);
		}
		byte result[] = bos.toByteArray();
		Bitmap map = BitmapFactory.decodeByteArray(result, 0, result.length);
//		Bitmap map = BitmapFactory.decodeResource(activity.getResources(),
//					mShareClassParam.sharePictureID, null);
		return map;
	}

	private void getWebpageObj(final ShareObject object) {
		new Thread() {

			@Override
			public void run() {
				Message msg = new Message();
				WebpageObject mediaObject = new WebpageObject();
				byte[] data;
				try {
					data = getImageData(object,0);
					mediaObject.thumbData = data;
					mediaObject.actionUrl = mShareClassParam.targetUrl;
					mediaObject.title = mShareClassParam.shareTitle;
					mediaObject.description = mShareClassParam.shareSummary;
					
					mediaObject.identify = Utility.generateGUID();
					msg.what = ASK_PAGE;
					msg.obj = mediaObject;
					mHandler.sendMessage(msg);
				} catch (IOException e) {
					e.printStackTrace();
					msg.what = ASK_ERROR;
					mHandler.sendMessage(msg);
					System.out.println("---->IOException 2");
				}
			}

		}.start();
	}

	private void getWXWebpage(final ShareObject object, int flag) {
		wechartflag = flag;
		new Thread() {

			@Override
			public void run() {
				Message msg = new Message();
				WXWebpageObject wxWebpage = new WXWebpageObject();
				mediamsg = new WXMediaMessage(wxWebpage);
				wxWebpage.webpageUrl = mShareClassParam.targetUrl;
				mediamsg.title = mShareClassParam.shareTitle;
				mediamsg.description = mShareClassParam.shareSummary;
				try {
					byte[] data = getImageData(object,1);
					mediamsg.thumbData = data;
					msg.what = ASK_WXPAGE;
					msg.obj = mediamsg;
					mHandler.sendMessage(msg);
				} catch (IOException e) {
					e.printStackTrace();
					msg.what = ASK_ERROR;
					mHandler.sendMessage(msg);
				}
			}

		}.start();
	}
	
	/**
	 * 支持标题和内容
	 */
	private void shareAcessAll(){
		if (mShareClassParam.shareType == SHARETYTPE_TOPIC_EVENT) {
			if (mShareClassParam.shareTitle == null || "test".equals(mShareClassParam.shareTitle) 
					|| TextUtils.isEmpty(mShareClassParam.shareTitle)) {//普通
				mShareClassParam.shareTitle = Util.getAssignLenght(mShareClassParam.shareSummary, 20);
				mShareClassParam.shareSummary = Util.getAssignLenght(mShareClassParam.shareSummary, 40);
			}else{//专家
				mShareClassParam.shareTitle = Util.getAssignLenght(mShareClassParam.shareTitle, 20);
				mShareClassParam.shareSummary = Util.getAssignLenght(mShareClassParam.shareSummary, 40);
			}
		}
	}
	/**
	 * 支持标题
	 */
	private void shareAcessTitle(){
		if (mShareClassParam.shareType == SHARETYTPE_TOPIC_EVENT) {
			if (mShareClassParam.shareTitle == null || "test".equals(mShareClassParam.shareTitle) 
					|| TextUtils.isEmpty(mShareClassParam.shareTitle)) {//普通
				mShareClassParam.shareTitle = Util.getAssignLenght(mShareClassParam.shareSummary, 20);
				mShareClassParam.shareSummary = Util.getAssignLenght(mShareClassParam.shareSummary, 40);
			}else{//专家
				mShareClassParam.shareTitle = Util.getAssignLenght(mShareClassParam.shareTitle, 20);
				mShareClassParam.shareSummary = Util.getAssignLenght(mShareClassParam.shareSummary, 40);
			}
		}
	}
	
}
