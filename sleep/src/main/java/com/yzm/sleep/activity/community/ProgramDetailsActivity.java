package com.yzm.sleep.activity.community;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.activity.ShareActivity;
import com.yzm.sleep.activity.WebViewActivity;
import com.yzm.sleep.bean.CommunityEventDetailBean;
import com.yzm.sleep.bean.ShareClassParam;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.EventDetailParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceEventDetailCallback;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.URLUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengProcClass;

/**
 * 活动详情
 * 
 * @author masong
 * @param String
 *            id; 活动id
 */
public class ProgramDetailsActivity extends BaseActivity {
	private ImageView image_progran;
	private WebView webView;
	private Button btn_sign;
	private String id;
	private int screenWidht;
	private ProgressUtils pb;
	private String SIGNRUL = "/admin.php?mod=hdbaom&id=";// 报名地址
	private String SHAREURL = "/share/hd/huodongdetail.php?id=";
	private CommunityEventDetailBean detailInfo = null;
	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_program);
		screenWidht = getScreenWidth();
		initView();
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("Community_Activity_Detail");
		MobclickAgent.onResume(this); 
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("Community_Activity_Detail"); 
		MobclickAgent.onPause(this);
	}
	
	private void initView() {
		title = getIntent().getStringExtra("title");
		id = getIntent().getStringExtra("id");
		webView = (WebView) findViewById(R.id.webViewProgram);
		findViewById(R.id.back).setOnClickListener(this);
		btn_sign = (Button) findViewById(R.id.btn_sign);
		btn_sign.setOnClickListener(this);
		btn_sign.setVisibility(View.GONE);
		((TextView) findViewById(R.id.title)).setText("活动详情");
		image_progran = (ImageView) findViewById(R.id.image_progran);

		HttpPrData();
	}

	private void HttpPrData() {
		pb = new ProgressUtils(ProgramDetailsActivity.this);
		pb.setCanceledOnTouchOutside(false);
		pb.setMessage("请稍等");
		pb.show();
		EventDetailParamClass class1 = new EventDetailParamClass();
		class1.my_int_id = PreManager.instance().getUserId(
				ProgramDetailsActivity.this);
		class1.id = id;
		new XiangchengProcClass(ProgramDetailsActivity.this).eventDetail(           
				class1, new InterfaceEventDetailCallback() {

					@Override
					public void onSuccess(int icode,
							CommunityEventDetailBean detail) {
						webView.setVisibility(View.VISIBLE);
						image_progran.setVisibility(View.VISIBLE);
						pb.dismiss();
						if (detail != null) {
							detailInfo = detail;
							setData();
						} else {
							toastMsg("请求失败");
						}
						btn_sign.setVisibility(View.VISIBLE);
						reeorDataInfo();
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						pb.dismiss();
						toastMsg(strErrMsg);
						reeorDataInfo();
						webView.setVisibility(View.GONE);
						image_progran.setVisibility(View.GONE);
					}
				});

	}

	@SuppressLint("SetJavaScriptEnabled")
	private void setData() {
		LayoutParams lp = (LayoutParams) image_progran.getLayoutParams();
		lp.width = screenWidht;
		lp.height = lp.width * 1 / 2;
		image_progran.setLayoutParams(lp);

		ImageLoader.getInstance().displayImage(detailInfo.getPicture(),
				detailInfo.getPicturekey(), image_progran,
				MyApplication.defaultOption);

		webView.getSettings().setJavaScriptEnabled(true);

		loadUserAgree(detailInfo.getContent());
	}
	/**
	 *
	 */
	private void reeorDataInfo(){
		if(detailInfo == null || detailInfo.getOver().equals("1")){
			btn_sign.setClickable(false);
			btn_sign.setBackgroundResource(R.drawable.button_usergroup);
			btn_sign.setText("活动已过期");
		}else{
			((ImageButton) findViewById(R.id.ib_share)).setVisibility(View.VISIBLE);
	        ((ImageButton) findViewById(R.id.ib_share)).setOnClickListener(this);
		}
	}

	/**
	 * 加载富文本
	 */
	private void loadUserAgree(String text) {
		webView.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sign:
			String url = URLUtil.DN_BASEURL + SIGNRUL + id;
			Intent it = new Intent(ProgramDetailsActivity.this,
					WebViewActivity.class);
			it.putExtra("title", "活动");
			it.putExtra("type", 1);
			it.putExtra("url", url);
			startActivity(it);
			break;
		case R.id.ib_share:// 分享
			if (Util.checkNetWork(ProgramDetailsActivity.this)) {
				String shareUrl = URLUtil.DN_BASEURL + SHAREURL + id;
				Intent intent = new Intent(this,ShareActivity.class);
				ShareClassParam param = new ShareClassParam();
				param.targetUrl = shareUrl;
				param.shareTitle = title;
				param.shareSummary = "活动详情";
				param.sharePictureUrl = detailInfo.getPicture() == null ? "" : detailInfo.getPicture();
				intent.putExtra("from", Constant.SHARE_FROM_COMMUNITY_EVENT);
				intent.putExtra("shareData", param);
				startActivity(intent);
			} else {
				toastMsg("请检查您的网络");
			}
			break;
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;

		default:
			break;
		}
	}
}
