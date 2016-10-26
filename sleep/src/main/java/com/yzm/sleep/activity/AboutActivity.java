package com.yzm.sleep.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.component.view.AttentionComponentView;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelbiz.JumpToBizProfile;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AccessTokenKeeper;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.SleepConstants;
import com.yzm.sleep.utils.Util;

@SuppressLint("Recycle") 
public class AboutActivity extends BaseActivity {
//	private Oauth2AccessToken mAccessToken;
//	private AttentionComponentView mAttentionView;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_about);
		initView();
	}

	@Override
	protected void onResume() {
		MobclickAgent.onPageStart("UserCenter_Setting_About"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this); // 统计时长
		super.onResume();
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("UserCenter_Setting_About"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
																// onPageEnd
																// 在onPause
																// 之前调用,因为
																// onPause
																// 中会保存信息
		MobclickAgent.onPause(this);
	}

	private void initView() {
		((TextView) (findViewById(R.id.title))).setText(getResources()
				.getString(R.string.about_xiangcheng));
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.rl_wx).setOnClickListener(this);
		RelativeLayout rlWebo = (RelativeLayout) findViewById(R.id.rl_wb);
		rlWebo.setOnClickListener(this);
		findViewById(R.id.rl_qq).setOnClickListener(this);
		findViewById(R.id.tvUserPrivacyPolicy).setOnClickListener(this);

		((TextView) findViewById(R.id.tvWeiboAdmin)).setText("香橙APP");
		((TextView) findViewById(R.id.tvWeiXinAdmin))
				.setText("xiangchengsleep");
		((TextView) findViewById(R.id.tvQQAdmin)).setText("183351707");

		// wb
		// 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
		// 第一次启动本应用，AccessToken 不可用
//		mAccessToken = AccessTokenKeeper.readAccessToken(this);
		/*mAttentionView = (AttentionComponentView) findViewById(R.id.attentionView);
		mAttentionView.setAttentionParam(AttentionComponentView.RequestParam
				.createRequestParam(SleepConstants.SINA_SLEEP_APP_KEY,
						mAccessToken.getToken(), "5602245729", "",
						new WeiboAuthListener() {
							@Override
							public void onWeiboException(WeiboException arg0) {
							}

							@Override
							public void onComplete(Bundle arg0) {
								Toast.makeText(
										AboutActivity.this,
										"auth   acess_token:"
												+ Oauth2AccessToken
														.parseAccessToken(arg0)
														.getToken(), 0).show();
							}

							@Override
							public void onCancel() {
							}
						}));
		expandViewTouchDelegate(mAttentionView, 200, 200, 2000, 2000);*/
	}
	public static void expandViewTouchDelegate(final View view, final int top,
            final int bottom, final int left, final int right) {
 
        ((View) view.getParent()).post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                view.setEnabled(true);
                view.getHitRect(bounds);
 
                bounds.top -= top;
                bounds.bottom += bottom;
                bounds.left -= left;
                bounds.right += right;
 
                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);
 
                if (View.class.isInstance(view.getParent())) {
                    ((View) view.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });
    }

	@SuppressLint({ "NewApi", "Recycle" })
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.rl_wx:
//			attentionWexin();
			break;
		case R.id.rl_wb:
			break;
		case R.id.rl_qq:
//			if (HomeActivity.mTencent == null) {
//				HomeActivity.mTencent = Tencent.createInstance(
//						SleepConstants.TENCENT_SLEEP_APP_ID, this);
//			}
//			HomeActivity.mTencent.joinQQGroup(this,
//					"2VOVTPlKW45mHfBG5adH_DxJCwsvxjTn");
			if (Util.isQQInstalled(this)) {
				joinQQGroup("2VOVTPlKW45mHfBG5adH_DxJCwsvxjTn");
			}else{
				Util.show(this, "请先安装qq客户端");
			}
			break;
		case R.id.tvUserPrivacyPolicy:
			Intent intent1 = new Intent(this,WebViewActivity.class);
			intent1.putExtra("title", "用户隐私协议");
			intent1.putExtra("type", 0);
			startActivity(intent1);
			break;
		default:
			break;
		}
	}

	public boolean joinQQGroup(String key) {
	    Intent intent = new Intent();
	    intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
	   // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
	    try {
	        startActivity(intent);
	        return true;
	    } catch (Exception e) {
	        // 未安装手Q或安装的版本不支持
	        return false;
	    }
	}
	
	/** 邀请微信好友*/
	private IWXAPI wxapi;
	private void attentionWexin() {
		String WXAPP_ID = SleepConstants.WECHAT_SLEEP_APP_ID;
//		String WXAPP_ID = "wx8a2dffb1f88f682e";
		wxapi = WXAPIFactory.createWXAPI(this, WXAPP_ID, true);
		wxapi.registerApp(WXAPP_ID);
//		wxapi.handleIntent(getIntent(), new IWXAPIEventHandler() {
//			
//			@Override
//			public void onResp(BaseResp arg0) {
//			}
//			
//			@Override
//			public void onReq(BaseReq arg0) {
//			}
//		}); 
		if (!wxapi.isWXAppInstalled()){
			Util.show(this, "请先安装微信客户端");
		}
		
		try {
			JumpToBizProfile.Req req = new JumpToBizProfile.Req();
			req.toUserName = "gh_b669843f7c96"; //公众号原始ID
			req.profileType = JumpToBizProfile.JUMP_TO_NORMAL_BIZ_PROFILE;
//			req.profileType = JumpToBizProfile.JUMP_TO_HARD_WARE_BIZ_PROFILE;
//			req.extMsg = "";
			wxapi.sendReq(req);
		} catch (Exception e) {
			e.printStackTrace();
			Util.show(this, "关注失败");
		}
		
	}

}
