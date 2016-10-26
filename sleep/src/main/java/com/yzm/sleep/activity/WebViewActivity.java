package com.yzm.sleep.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.doctor.MyReservationActivity;
import com.yzm.sleep.bean.SecureBean;
import com.yzm.sleep.bean.UserMessageBean;
import com.yzm.sleep.im.ChatActivity;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.Util;

/**
 * 用户使用协议
 * 
 * @author Administrator
 * 
 *  title  页面标题
 *  type  0 用户协议;  1 传入的是一个外部网页, 2预约;
 *  url   当type传1的时候要传入一个url的网址
 */
public class WebViewActivity extends BaseActivity {
	private WebView webView;
	private ProgressBar progerssbar;
	private int pageType;
	private final String USER_AGREE_URL = "http://www.apporange.cn/copyright2.html";
	private TextView tvLoadFailed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agreement);
		String title = getIntent().getStringExtra("title");
		int pageType = getIntent().getIntExtra("type", -1);
		if(title != null)
			((TextView)findViewById(R.id.title)).setText(title);
		
		tvLoadFailed = (TextView) findViewById(R.id.tvLoadFailed);
		
		initWebView();
		
		if(pageType == 0)
			webView.loadUrl(USER_AGREE_URL);
		else{
			String url = getIntent().getStringExtra("url");
			if(url.startsWith("http://"))
				webView.loadUrl(url);
		}
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		if(pageType == 1){
			MobclickAgent.onPageStart("Community_Activity_Detail_Signup"); 
			MobclickAgent.onResume(this);
		}else if(pageType == 0){
			MobclickAgent.onPageStart("UserAgreement"); 
			MobclickAgent.onResume(this);
		}
	
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(pageType == 1){
			MobclickAgent.onPageEnd("Community_Activity_Detail_Signup");
			MobclickAgent.onPause(this);	
		}else if(pageType == 0){
			MobclickAgent.onPageEnd("UserAgreement");
			MobclickAgent.onPause(this);
		}
	}
	
	@SuppressLint("SetJavaScriptEnabled") 
	private void initWebView(){
		progerssbar = (ProgressBar) findViewById(R.id.progerssbar);
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AppManager.getAppManager().finishActivity();
			}
		});
		webView = (WebView) findViewById(R.id.web_View);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false); 
		webView.setWebChromeClient(new WebChromeClient(){


			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				if(!Util.isFastClick()){
					LogUtil.i("tx", message);
					if(message.contains("type")){
						JSONObject baoxianObject,mJson;
						try {
							baoxianObject = new JSONObject(message);
							mJson = (JSONObject) baoxianObject.get("baoxian");
							int response = (int) mJson.getInt("response");
							String msg = mJson.get("message").toString();
							switch (response) {//0 表单填写不完整 ;  -5 用户不存在； -1 保险单号已经存在； -2 用户有保险单正在进行中 ；  -3 失败；  1成功
							case 0:
							case -1:
							case -2:
							case -3:
							case -5:
								toastMsg(msg);
								break;
							case 1:
								String data = (String) mJson.getString("date");
								Gson gson = new Gson();
								SecureBean mSecureBean = gson.fromJson(data, new TypeToken<SecureBean>() {
								}.getType());
								UserMessageBean userBean = new UserMessageBean();
								userBean.setAskString(mSecureBean.getInfoString());
								userBean.setUid(mSecureBean.getKefuuid());
								userBean.setNickname("客服");
								Intent intent = new Intent(WebViewActivity.this, ChatActivity.class);
								intent.putExtra("userBean", userBean);
								startActivity(intent);
								AppManager.getAppManager().finishActivity(WebViewActivity.this);
								break;

							default:
								break;
							}
							
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}else{
						if("1".equals(message)){
							startActivity(new Intent(WebViewActivity.this, MyReservationActivity.class));
							AppManager.getAppManager().finishActivity(WebViewActivity.this);
						}else
							toastMsg(message);
					}
				}
				result.cancel();
				return true;
			}

			@Override
			public boolean onJsBeforeUnload(WebView view, String url,
					String message, JsResult result) {
				// TODO Auto-generated method stub
				return super.onJsBeforeUnload(view, url, message, result);
			}

			@Override
			public boolean onJsConfirm(WebView view, String url,
					String message, JsResult result) {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				progerssbar.setProgress(newProgress);
				if(newProgress == 100){
					//当webview加载缓存完毕调用
					progerssbar.setVisibility(View.GONE);
				}else{
					progerssbar.setVisibility(View.VISIBLE);
				}
			}
			
		});
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				tvLoadFailed.setVisibility(View.VISIBLE);
			}
		});
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (webView.canGoBack() && event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  
	        webView.goBack();  
	        return true;  
	    } 
		return super.onKeyDown(keyCode, event);
	}
	
	
	
}
