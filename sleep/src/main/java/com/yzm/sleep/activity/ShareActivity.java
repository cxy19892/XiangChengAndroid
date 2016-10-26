package com.yzm.sleep.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
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
import com.yzm.sleep.AccessTokenKeeper;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.CircleImageView;
import com.yzm.sleep.Constant;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.activity.pillow.PillowDataOpera;
import com.yzm.sleep.adapter.ShareViewAdapter;
import com.yzm.sleep.bean.ShareClassParam;
import com.yzm.sleep.bean.ShareObjectClass;
import com.yzm.sleep.bean.ShareViewBean;
import com.yzm.sleep.model.PillowDataModel;
import com.yzm.sleep.render.GetSleepResultValueClass.SleepDataHead;
import com.yzm.sleep.utils.BluetoothDataFormatUtil;
import com.yzm.sleep.utils.ImageCompressUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.QiNiuUploadTool;
import com.yzm.sleep.utils.ShareUtil;
import com.yzm.sleep.utils.SleepConstants;
import com.yzm.sleep.utils.SleepUtils;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.ToastManager;
import com.yzm.sleep.utils.URLUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.widget.DayDataCircle;

public class ShareActivity extends BaseActivity implements IWeiboHandler.Response {
	/** 普通邀请 SHARETYPE_COMM_INVITE = 0 */
	public static final int SHARETYPE_COMM_INVITE = 0;
	/** 分享睡眠数据 SHARETYTPE_TOPIC_EVENT = 1 */
	public static final int SHARETYTPE_TOPIC_EVENT = 1;
	private Context mContext;
	private RelativeLayout layoutShare;
	private String picPath = "";
	/** 微博微博分享接口实例 */
	private IWeiboShareAPI mWeiboShareAPI = null;
	/** 邀请微信好友 */
	private IWXAPI wxapi;
	private ProgressUtils pro;
	/**
	 * 0-分享睡眠数据, 1- 分享话题或活动
	 */
	private int from;
	private ShareClassParam mShareClassParam;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		mContext = this;
		try {
			from = getIntent().getIntExtra("from", -1);
		} catch (Exception e1) {
		}

		try {
			mShareClassParam = (ShareClassParam) getIntent()
					.getSerializableExtra("shareData");// 分享话题或活动的数据
		} catch (Exception e) {
		}
		if (from == Constant.SHARE_FROM_SLEEP_DATA) {
			picPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/DCIM/shoot.png";
		} else if (from == Constant.SHARE_FROM_COMMUNITY_EVENT) {
			if (TextUtils.isEmpty(mShareClassParam.sharePictureUrl)) {
				mShareClassParam.sharePictureUrl = URLUtil.XIANGCHENG_ICON_URL;
			}
		}
		initView();
		initShareView();
		initWBShare(savedInstanceState);
		initWeiXinShare();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		cancelPro();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_cancel:
			AppManager.getAppManager().finishActivity();
			break;
		default:
			break;
		}
	}

	@SuppressLint("SimpleDateFormat")
	private void initView() {
		DayDataCircle dataCircle = (DayDataCircle) findViewById(R.id.soft_day_data_view);
		dataCircle.crop(true);
		findViewById(R.id.share_back).getBackground().setAlpha(160);
		layoutShare = (RelativeLayout) findViewById(R.id.layout_share);
		final CircleImageView ivUserIcon = (CircleImageView) findViewById(R.id.ivUserIcon);
		TextView tvUserNickname = (TextView) findViewById(R.id.tvUserNickname);
		TextView tvSleepDate = (TextView) findViewById(R.id.tvSleepDate);

		tvUserNickname.setText(PreManager.instance().getUserNickname(mContext));
		TextView tvDakaDays = (TextView) findViewById(R.id.tvDakaDays);
		if (TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(ShareActivity.this))) {
			tvDakaDays.setText("我已经连续打卡" + PreManager.instance().getUserDakaDays(mContext) + "天");
			tvDakaDays.setVisibility(View.VISIBLE);
		}
		final String profileKey = PreManager.instance().getUserProfileKey(
				mContext);
		ImageLoader.getInstance().displayImage(
				PreManager.instance().getUserProfileUrl(mContext), profileKey,
				ivUserIcon, MyApplication.headPicOptn,
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String arg0, View arg1) {

					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {
						QiNiuUploadTool.getInstance().refreshDisCache(
								ShareActivity.this, profileKey, ivUserIcon);
					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							Bitmap arg2) {

					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {

					}
				});

		if (from == Constant.SHARE_FROM_SLEEP_DATA) {
			SoftDayData daySleep = null;
			try {
				daySleep = (SoftDayData) getIntent().getSerializableExtra("sleep_data");
			} catch (Exception e) {
			}

			if (!TextUtils.isEmpty(PreManager.instance()
					.getBundbluetoothPillow(mContext))) {

				PillowDataModel pModel = PillowDataOpera.queryDataFromSQL(
						ShareActivity.this, TimeFormatUtil.getYesterDay(
								new SimpleDateFormat("yyyy-MM-dd")
										.format(new Date()), "yyyy-MM-dd"));
				if (pModel != null) {
					if (pModel.getBfr() != null) {
						SleepDataHead datahead = BluetoothDataFormatUtil
								.format3(pModel.getBfr(), 10);
						pModel.getBfr();
						daySleep.setTotalSleepTime(datahead.TotalSleepTime);
					}
				}
			}
			
			dataCircle.setData(
					PreManager.instance().getSleepTime_Setting(mContext),
					PreManager.instance().getGetupTime_Setting(mContext),
					daySleep);

			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, -1);
			tvSleepDate.setText(sdf1.format(c.getTime()));
			layoutShare.setVisibility(View.VISIBLE);
		} else {
			layoutShare.setVisibility(View.GONE);
		}
	}

	private void initShareView() {
		findViewById(R.id.btn_cancel).setOnClickListener(this);
		GridView gridView = (GridView) findViewById(R.id.gridView);
		gridView.setOverScrollMode(GridView.OVER_SCROLL_NEVER);
		List<ShareViewBean> inits = new ArrayList<ShareViewBean>();
		ShareViewBean initInfoClass = null;
		int m = 4;
		if (from == Constant.SHARE_FROM_COMMUNITY_EVENT) {
			m = 5;
		}
		for (int i = 0; i < m; i++) {
			initInfoClass = new ShareViewBean();
			if (i == 0) {
				initInfoClass.type = "微信好友";
				initInfoClass.id = R.drawable.ic_wx;
			} else if (i == 1) {
				initInfoClass.type = "微信朋友圈";
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

		ShareViewAdapter shareAdapter = new ShareViewAdapter(mContext, inits);
		gridView.setAdapter(shareAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (from == Constant.SHARE_FROM_SLEEP_DATA) {
					shareSleepData(position);
				} else if (from == Constant.SHARE_FROM_COMMUNITY_EVENT) {
					shareCommunityEvent(position);
				}
			}
		});
	}

	/**
	 * 显示进度
	 */
	private void showPro() {
		if (pro == null) {
			pro = new ProgressUtils(this);
			pro.setCanceledOnTouchOutside(false);
		}
		pro.show();
	}

	/**
	 * 取消进度
	 */
	private void cancelPro() {
		if (pro != null) {
			pro.dismiss();
			pro = null;
		}
	}
	
	/**
	 * 分享睡眠数据
	 * 
	 * @param position
	 */
	private void shareSleepData(int position) {
		shareObject = initShareObject();
		switch (position) {
		case 0:// 分享给微信好友
			if (checkWeixinInstall()) {
				showPro();
				ShareUtil.doWeixinShareImage(ShareActivity.this, 0, 1, shareObject, wxapi);
			}
			break;
		case 1:// 分享到微信朋友圈
			if (checkWeixinInstall()) {
				showPro();
				ShareUtil.doWeixinShareImage(ShareActivity.this, 1, 1, shareObject, wxapi);
			}
			break;
		case 2:// 分享到微博
			showPro();
			ShareUtil.doWeiboShare(ShareActivity.this, mWeiboShareAPI, shareObject, false, true, false, false, false, false);
			break;
		case 3:// 分享到QQ好友
			if (Util.isQQInstalled(mContext)) {
				showPro();
				ShareUtil.doQQShareImag(ShareActivity.this, shareObject, new BaseUiListener());
			} else {
				Util.show(mContext, "请先安装qq客户端");
			}
			break;
		case 4:// 分享到QQ空间
			showPro();
			ShareUtil.doQQQzoneShareImage(ShareActivity.this, shareObject, 1, new BaseUiListener());
			break;
		default:
			break;
		}
	}

	/**
	 * 社区分享
	 * 
	 * @param position
	 */
	private void shareCommunityEvent(int position) {
		switch (position) {
		case 0:
			if (!wxapi.isWXAppInstalled()) {
				Util.show(this, "请先安装微信客户端");
				return;
			}
			showPro();
			initShareObject("weixin", "微信分享信息");
			shareAcessAll();
			doWeixinShare(0);
			break;
		case 1:
			if (!wxapi.isWXAppInstalled()) {
				Util.show(this, "请先安装微信客户端");
				return;
			}
			showPro();
			initShareObject("weixin", "微信朋友圈分享信息");
			shareAcessTitle();
			doWeixinShare(1);
			break;
		case 2:
			showPro();
			initShareObject("weibo", "微博分享信息");
			shareAcessTitle();
			doWeiboShare();
			break;
		case 3:
			if (!Util.isQQInstalled(this)) {
				Util.show(this, "请先安装qq客户端");
				return;
			}
			showPro();
			initShareObject("qq", "qq分享信息");
			shareAcessAll();
			doQQShare();
			break;
		case 4:
			if (!Util.isQQInstalled(this)) {
				Util.show(this, "请先安装qq客户端");
				return;
			}
			showPro();
			initShareObject("qq", "qq空间分享信息");
			shareAcessAll();
			doQQQzoneShare();
			break;

		default:
			break;
		}
	}

	/**
	 * 检查是否安装微信客户端
	 * 
	 * @return
	 */
	private boolean checkWeixinInstall() {
		if (!wxapi.isWXAppInstalled()) {
			ToastManager.getInstance(this).show("请先安装微信客户端");
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 分享按钮点击监听
	 * 
	 * @author Administrator
	 * 
	 */
	private ShareObjectClass shareObject;

	class ShareOnClickListener implements OnClickListener {

		public ShareOnClickListener() {
		}

		@Override
		public void onClick(View view) {
			shareObject = initShareObject();
			switch (view.getId()) {
			case 0:// 分享给微信好友
				showPro();
				ShareUtil.doWeixinShareImage(ShareActivity.this, 0, 1,
						shareObject, wxapi);
				break;
			case 1:// 分享到微信朋友圈
				showPro();
				ShareUtil.doWeixinShareImage(ShareActivity.this, 1, 1,
						shareObject, wxapi);
				break;
			case 2:// 分享到微博
				if (!mWeiboShareAPI.isWeiboAppInstalled()) {
					Util.show(mContext, "请先安装微博客户端");
				} else {
					showPro();
					ShareUtil.doWeiboShare(ShareActivity.this, mWeiboShareAPI, shareObject, false, true, false, false, false,
							false);
				}
				break;
			case 3:// 分享到QQ好友
				showPro();
				ShareUtil.doQQShareImag(ShareActivity.this, shareObject, new BaseUiListener());
				break;
			case 4:// 分享到QQ空间
				showPro();
				ShareUtil.doQQQzoneShareImage(ShareActivity.this, shareObject, 1, new BaseUiListener());
				break;
			default:
				break;
			}
		}

	}

	private void getShareImage(View layoutShare, String picPath) {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Util.show(mContext, "未检测到sd卡");
		}
		int width = layoutShare.getWidth();
		int height = layoutShare.getHeight();
		if (width > 0 && height > 0) {
			SleepUtils.getBitmapByView(layoutShare, picPath);
		}
	}

	/**
	 * 分享类型信息类
	 * 
	 * @author Administrator
	 * 
	 */
	class InitClass {
		public int id;
		public String type;
	}

	/**
	 * 初始化微博分享
	 */
	private void initWBShare(Bundle savedInstanceState) {
		// 创建微博分享接口实例
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this,SleepConstants.SINA_SLEEP_APP_KEY);
		mWeiboShareAPI.registerApp();
		if (savedInstanceState != null) {
			mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
		}
	}
	
	private void initWeiXinShare() {
		String WXAPP_ID = SleepConstants.WECHAT_SLEEP_APP_ID;
		wxapi = WXAPIFactory.createWXAPI(this, WXAPP_ID, true);
		wxapi.registerApp(WXAPP_ID);
		wxapi.handleIntent(getIntent(), new mIWXAPIEventHandler());
	}

	/**
	 * 微博分享回调方法
	 * 
	 * @param baseResp
	 */
	@Override
	public void onResponse(BaseResponse baseResp) {
		cancelPro();
		switch (baseResp.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			toastMsg("分享成功");
			AppManager.getAppManager().finishActivity();
			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			toastMsg("分享取消");
			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			toastMsg("分享失败");
			break;
		}
	}

	/**
	 * QQ分享回调
	 * @author Administrator
	 *
	 */
	private class BaseUiListener implements IUiListener {
		@Override
		public void onError(UiError e) {
			cancelPro();
			Util.show(ShareActivity.this, "分享出错");
		}

		@Override
		public void onCancel() {
			cancelPro();
			Util.show(ShareActivity.this, "分享取消");
		}

		@Override
		public void onComplete(Object arg0) {
			cancelPro();
			Util.show(ShareActivity.this, "分享完成");
			AppManager.getAppManager().finishActivity();
		}
	}

	/**
	 * 微信回调接口
	 * 
	 * @author Administrator
	 * 
	 */
	private class mIWXAPIEventHandler implements IWXAPIEventHandler {

		@Override
		public void onReq(BaseReq arg0) {
		}

		@Override
		public void onResp(BaseResp arg0) {
		}

	}

	private ShareObjectClass initShareObject() {
		getShareImage(layoutShare, picPath);
		ShareObjectClass shareObject = new ShareObjectClass();
		shareObject.setTargetUrl(URLUtil.XIANGCHENG_APP_DOWNLOAD_URL);
		shareObject.setSharePictureUrl(URLUtil.XIANGCHENG_ICON_URL);
		shareObject.setShareTitle("香橙—好玩的睡眠工具");
		shareObject.setShareSummary("让睡觉变的好玩！中国使用人数最多的睡眠管理软件。");
		shareObject.setSharePicturePath(picPath);

		return shareObject;
	}

	// ----------------community share
	/** 微信分享类型 ，0 分享给微信好友 1分享到朋友圈 */
	private int wechartflag = 1;
	private WXMediaMessage mediamsg;
	private WeiboMultiMessage weiboMultiMessage;
	// qq分享变量
	private int shareTypeQzone = QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;
	private int shareTypeQQ = QQShare.SHARE_TO_QQ_TYPE_DEFAULT;
	// 微博分享变量
	// private IWeiboShareAPI mWeiboShareAPI = null;
	private final static int ASK_ERROR = 0;
	private final static int ASK_IMG = 1;
	private final static int ASK_PAGE = 2;

	// 微信分享变量
	private final static int ASK_WXPAGE = 3;

	private void initShareObject(String platform, String message) {
		shareObject = new ShareObjectClass();
	}

	/**
	 * 微信分享
	 * 
	 * @param flag
	 *            0 分享给微信好友 ，1分享到朋友圈
	 */
	private void doWeixinShare(int flag) {
		getWXWebpage(shareObject, flag);
	}

	private void getWXWebpage(final ShareObjectClass object, int flag) {
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
					byte[] data = getImageData(1);
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
	 * 新浪微博分享
	 */
	private void doWeiboShare() {
		if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
			int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
			if (supportApi >= 10351 /* ApiUtils.BUILD_INT_VER_2_2 */) {
				weiboMultiMessage = new WeiboMultiMessage();
				weiboMultiMessage.textObject = getTextObj();
				getImageObject(0);
				getWebpageObj();
			} else {
				ToastManager.getInstance(this).show("您的sdk版本过低，不支持分享多媒体信息。");
			}
		} else {
			if (android.os.Build.VERSION.SDK_INT > 8) {
				weiboMultiMessage = new WeiboMultiMessage();
				weiboMultiMessage.textObject = getTextObj();
				getImageObject(0);
				getWebpageObj();
			} else {
				ToastManager.getInstance(this).show("您的sdk版本过低，不支持分享多媒体信息。");
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
		params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);

		if (HomeActivity.mTencent != null) {
			HomeActivity.mTencent.shareToQzone(this, params,
					new BaseUiListener());
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
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
				mShareClassParam.sharePictureUrl);
		if (HomeActivity.mTencent != null) {
			HomeActivity.mTencent.shareToQQ(this, params, new BaseUiListener());
		}

	}

	private TextObject getTextObj() {
		TextObject textObject = new TextObject();
		textObject.text = "分享";
		return textObject;
	}

	private void getImageObject(final int isSmall) {
		new Thread() {

			@Override
			public void run() {
				Message msg = new Message();
				ImageObject imageObject = new ImageObject();
				try {
					Bitmap map = null;
					map = getImageBitmap();
					if (map != null) {
						imageObject.setImageObject(map);
						msg.what = ASK_IMG;
						msg.obj = imageObject;
						mHandler.sendMessage(msg);
					}
				} catch (IOException e) {
					msg.what = ASK_ERROR;
					mHandler.sendMessage(msg);
				}
			}

		}.start();
	}

	private void getWebpageObj() {
		new Thread() {

			@Override
			public void run() {
				Message msg = new Message();
				WebpageObject mediaObject = new WebpageObject();
				byte[] data;
				try {
					data = getImageData(0);
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
				}
			}

		}.start();
	}

	private Bitmap getImageBitmap() throws IOException {
		byte[] data = new byte[1024];
		ByteArrayOutputStream bos = null;
		URL url = new URL(mShareClassParam.sharePictureUrl);
		bos = new ByteArrayOutputStream();
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		InputStream in = conn.getInputStream();
		int len = 0;
		while ((len = in.read(data)) != -1) {
			bos.write(data, 0, len);
		}
		byte result[] = bos.toByteArray();
		Bitmap map = BitmapFactory.decodeByteArray(result, 0, result.length);
		return map;
	}

	private byte[] getImageData(int isSmall) throws IOException {
		byte[] data = new byte[1024];
		ByteArrayOutputStream bos = null;
		URL url = new URL(mShareClassParam.sharePictureUrl);
		bos = new ByteArrayOutputStream();
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		InputStream in = conn.getInputStream();
		int len = 0;
		while ((len = in.read(data)) != -1) {
			bos.write(data, 0, len);
		}
		byte result[] = bos.toByteArray();
		return ImageCompressUtil.comp32k(BitmapFactory.decodeByteArray(result,
				0, result.length));
	}

	/**
	 * 支持标题和内容
	 */
	private void shareAcessAll() {
		if (mShareClassParam.shareTitle == null
				|| "test".equals(mShareClassParam.shareTitle)
				|| TextUtils.isEmpty(mShareClassParam.shareTitle)) {// 普通
			mShareClassParam.shareTitle = Util.getAssignLenght(
					mShareClassParam.shareSummary, 20);
			mShareClassParam.shareSummary = Util.getAssignLenght(
					mShareClassParam.shareSummary, 40);
		} else {// 专家
			mShareClassParam.shareTitle = Util.getAssignLenght(
					mShareClassParam.shareTitle, 20);
			mShareClassParam.shareSummary = Util.getAssignLenght(
					mShareClassParam.shareSummary, 40);
		}
	}

	/**
	 * 支持标题
	 */
	private void shareAcessTitle() {
		if (mShareClassParam.shareTitle == null
				|| "test".equals(mShareClassParam.shareTitle)
				|| TextUtils.isEmpty(mShareClassParam.shareTitle)) {// 普通
			mShareClassParam.shareTitle = Util.getAssignLenght(
					mShareClassParam.shareSummary, 20);
			mShareClassParam.shareSummary = Util.getAssignLenght(
					mShareClassParam.shareSummary, 40);
		} else {// 专家
			mShareClassParam.shareTitle = Util.getAssignLenght(
					mShareClassParam.shareTitle, 20);
			mShareClassParam.shareSummary = Util.getAssignLenght(
					mShareClassParam.shareSummary, 40);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ASK_ERROR:
				Util.show(ShareActivity.this, "请求分享图片失败");
				break;

			case ASK_IMG:
				if (weiboMultiMessage == null || msg.obj == null)
					return;
				weiboMultiMessage.imageObject = (ImageObject) msg.obj;
				synchronized (weiboMultiMessage) {
					if (weiboMultiMessage.mediaObject != null) {
						SendMultiMessageToWeiboRequest multirequest = new SendMultiMessageToWeiboRequest();
						multirequest.transaction = String.valueOf(System
								.currentTimeMillis());
						multirequest.multiMessage = weiboMultiMessage;
						AuthInfo authInfo = new AuthInfo(ShareActivity.this,
								SleepConstants.SINA_SLEEP_APP_KEY,
								SleepConstants.REDIRECT_URL,
								SleepConstants.SCOPE);
						Oauth2AccessToken accessToken = AccessTokenKeeper
								.readAccessToken(ShareActivity.this
										.getApplicationContext());
						String token = "";
						if (accessToken != null) {
							token = accessToken.getToken();
						}
						mWeiboShareAPI.sendRequest(ShareActivity.this,
								multirequest, authInfo, token,
								new WeiboAuthListener() {

									@Override
									public void onWeiboException(WeiboException arg0) {
										cancelPro();
									}

									@Override
									public void onComplete(Bundle bundle) {
										cancelPro();
										Oauth2AccessToken newToken = Oauth2AccessToken
												.parseAccessToken(bundle);
										AccessTokenKeeper.writeAccessToken(
												ShareActivity.this, newToken);
										Util.show(ShareActivity.this,
												"onAuthorizeComplete token = "
														+ newToken.getToken());
									}

									@Override
									public void onCancel() {
										cancelPro();
										Util.show(ShareActivity.this, "取消分享");
									}
								});
						weiboMultiMessage = null;
					}
				}
				break;

			case ASK_PAGE:
				if (weiboMultiMessage == null || msg.obj == null)
					return;
				weiboMultiMessage.mediaObject = (WebpageObject) msg.obj;
				synchronized (weiboMultiMessage) {
					if (weiboMultiMessage.imageObject != null) {
						SendMultiMessageToWeiboRequest multirequest = new SendMultiMessageToWeiboRequest();
						multirequest.transaction = String.valueOf(System
								.currentTimeMillis());
						multirequest.multiMessage = weiboMultiMessage;
						AuthInfo authInfo = new AuthInfo(ShareActivity.this,
								SleepConstants.SINA_SLEEP_APP_KEY,
								SleepConstants.REDIRECT_URL,
								SleepConstants.SCOPE);
						Oauth2AccessToken accessToken = AccessTokenKeeper
								.readAccessToken(ShareActivity.this);
						String token = "";
						if (accessToken != null) {
							token = accessToken.getToken();
						}
						mWeiboShareAPI.sendRequest(ShareActivity.this,
								multirequest, authInfo, token,
								new WeiboAuthListener() {

									@Override
									public void onWeiboException(WeiboException arg0) {
										cancelPro();
									}

									@Override
									public void onComplete(Bundle bundle) {
										cancelPro();
										Oauth2AccessToken newToken = Oauth2AccessToken
												.parseAccessToken(bundle);
										AccessTokenKeeper.writeAccessToken(
												ShareActivity.this
														.getApplicationContext(),
												newToken);
										Util.show(ShareActivity.this,
												"onAuthorizeComplete token = "
														+ newToken.getToken());
									}

									@Override
									public void onCancel() {
										cancelPro();
										Util.show(ShareActivity.this, "取消分享");
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
					req.scene = wechartflag == 0 ? SendMessageToWX.Req.WXSceneSession
							: SendMessageToWX.Req.WXSceneTimeline;
					wxapi.sendReq(req);
					mediamsg = null;
				}
				break;
			}
		};
	};
}
