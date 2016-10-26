package com.yzm.sleep.activity.pillow;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.utils.URLUtil;

public class CommonProblemActiivity extends BaseActivity {

	private Button titleLeft;
	private TextView title;
	private WebView mWebView;
	private ProgressBar progerssbar;
	private String selectid = "1";
	private String titleName = "常见问题";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_problem);
		selectid = getIntent().getStringExtra("select_id");
		titleName = getIntent().getStringExtra("title");
		if(null == selectid){//获取mac地址不正确
			selectid = "1";
		}
		if(null == titleName){
			titleName = "常见问题";
		}
		initWebView();
	}

	@SuppressLint("SetJavaScriptEnabled") private void initWebView(){
		title = (TextView) findViewById(R.id.title);
		titleLeft = (Button) findViewById(R.id.back);
		titleLeft.setOnClickListener(this);
		title.setText(titleName);
		mWebView = (WebView) findViewById(R.id.problem_webView);
		progerssbar = (ProgressBar) findViewById(R.id.progerssbar);
		mWebView.getSettings().setJavaScriptEnabled(true); 
		mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.setWebChromeClient(new WebChromeClient(){

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
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
		mWebView.setWebViewClient(new WebViewClient(){

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				mWebView.loadUrl(url);
				return true;
			}
			
		});
		
		System.out.println(URLUtil.WEB_COMMON_PROBLEM_URL);
		if("1".equals(selectid)){
			mWebView.loadUrl(URLUtil.WEB_PROBLEM_AND_SOLVE);
		}else
		mWebView.loadUrl(URLUtil.WEB_COMMON_PROBLEM_URL + selectid);
		
	}
	
	@Override
	public void onClick(View v) {
		if(v == titleLeft){
			if(mWebView.canGoBack()){
				mWebView.goBack();
			}else
			AppManager.getAppManager().finishActivity();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(mWebView.canGoBack()){
				mWebView.goBack();
			}else
			AppManager.getAppManager().finishActivity();
		}
		return true;
	}
}
