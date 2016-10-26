package com.yzm.sleep.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.PlanBean;
import com.yzm.sleep.model.myProsessDialog;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetPlanListCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetPlanListParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetSleepReportCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetSleepReportParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceEstimateCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSleepIndexCallback;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.URLUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.CustomDialog;

/**
 * 评估进行页面
 * @author chen
 * 需要传入的信息：<br>
 * @params type ：0.睡眠评估;5.睡眠反馈;6.睡眠报告
 * @params date 日期 格式 yyyyMMdd 睡眠反馈  需要
 * @params datatype 1表示软件数据；2表示硬件数据
 */
public class EstimateWebActivity extends BaseActivity {

	private WebView webView;
	private ProgressBar progerssbar;
	private String url;
	private TextView tvLoadFailed;
	private String Type;
	private myProsessDialog mdialog;
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_estimate_web);
		this.mContext = this;
		String title = getIntent().getStringExtra("title");
//		url = getIntent().getStringExtra("url");
		initWebView();
		tvLoadFailed = (TextView) findViewById(R.id.esti_tvLoadFailed);
		Type = getIntent().getStringExtra("type");
		if("0".equals(Type)){
			title = "睡眠评估";
			url = URLUtil.SLEEPINDEX_URL+"uid="+PreManager.instance().getUserId(EstimateWebActivity.this)+"#";
		}/*else if("1".equals(Type)){
			title = "生活习惯";
			url = URLUtil.SLEEP_SHXG_URL+"uid="+PreManager.instance().getUserId(EstimateWebActivity.this)+"#";
		}else if("2".equals(Type)){
			title = "睡眠规律";
			url = URLUtil.SLEEP_SMGL_URL+"uid="+PreManager.instance().getUserId(EstimateWebActivity.this)+"#";
		}else if("3".equals(Type)){
			title = "卧室环境";
			url = URLUtil.SLEEP_SMHJ_URL+"uid="+PreManager.instance().getUserId(EstimateWebActivity.this)+"#";
		}else if("4".equals(Type)){
			title = "心理活动";
			url = URLUtil.SLEEP_XLHD_URL+"uid="+PreManager.instance().getUserId(EstimateWebActivity.this)+"#";
		}*/else if("5".equals(Type)){
			title = "睡眠反馈";
			url = URLUtil.SLEEP_FEEKBACK_URL+"date="+getIntent().getStringExtra("date")+"&uid="+PreManager.instance().getUserId(this)+"&type="+getIntent().getStringExtra("datatype");
		}else if("6".equals(Type)){
			title = "睡眠报告";
			getSleepReport();
		}
		if(title != null)
			((TextView)findViewById(R.id.title)).setText(title);

		if(url != null){
			webView.loadUrl(url);
		}
		else{
			
		}
	}
	
	/**
	 * 获取睡眠报告
	 */
	@SuppressLint("SimpleDateFormat")
	private void getSleepReport(){
		if(!checkNetWork(mContext)){
			webView.setVisibility(View.GONE);
			tvLoadFailed.setVisibility(View.VISIBLE);
		}else{
			GetSleepReportParams mParams = new GetSleepReportParams();
			try {
				mParams.my_int_id = PreManager.instance().getUserId(mContext);
				mParams.date = TimeFormatUtil.getYesterdayTimeyyyyMMdd();//new SimpleDateFormat("yyyMMdd").format(new Date());
			} catch (Exception e) {
				webView.setVisibility(View.GONE);
			}

			new XiangchengMallProcClass(mContext).getSleepReport(mParams, new GetSleepReportCallBack() {

				@Override
				public void onSuccess(int icode, String content) {
					webView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);
					webView.setVisibility(View.VISIBLE);
				}

				@Override
				public void onError(int icode, String strErrMsg) {

					webView.setVisibility(View.GONE);
					tvLoadFailed.setVisibility(View.VISIBLE);
					Util.show(mContext, strErrMsg);
				}
			});
		}
	}
	
	@SuppressLint("SetJavaScriptEnabled") 
	private void initWebView(){
		progerssbar = (ProgressBar) findViewById(R.id.esti_progerssbar);
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (webView.canGoBack())  
			        webView.goBack();  
			    else
			    	AppManager.getAppManager().finishActivity();
			    
			}
		});
		webView = (WebView) findViewById(R.id.esti_web_View);
		webView.setBackgroundResource(R.color.bg_color);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setAppCacheEnabled(false);
		webView.setWebChromeClient(new WebChromeClient(){

			
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				if(!Util.isFastClick()){
					/*if("0".equals(message)){
						showDialog();
						getEstimateData(message);
					}else if("1".equals(message) || "2".equals(message) || "3".equals(message) || "4".equals(message)){
						showDialog();
						getEstiData(message);
					}*/
					if("1".equals(message)){
						showDialog();
						getEstimateData(message);
						
					}else if("fankui".equals(message)){
						if("0".equals(getIntent().getStringExtra("opennew"))){
							setResult(RESULT_OK);
						}else{
							sendFeedBackBroadcast();
							Intent intent = new Intent(EstimateWebActivity.this, SleepReportActivity.class);
							startActivity(intent);
						}
						AppManager.getAppManager().finishActivity();
					}else{
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
				LogUtil.e("chen", result.toString());
				
				return true;
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				progerssbar.setProgress(newProgress);
				if(newProgress == 100){
					//当webview加载缓存完毕调用
					webView.setVisibility(View.VISIBLE);
					progerssbar.setVisibility(View.GONE);
				}else{
					webView.setVisibility(View.INVISIBLE);
					progerssbar.setVisibility(View.VISIBLE);
					
				}
			}
			
			
			
		});
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				LogUtil.e("chen", "errorCode="+errorCode+description);
//				super.onReceivedError(view, errorCode, description, failingUrl);
				webView.setVisibility(View.GONE);
				tvLoadFailed.setVisibility(View.VISIBLE);
			}
		});
	}
	
	private void getResault(final String type){
		Intent intent = new Intent(EstimateWebActivity.this, EstimateBaseActivity.class);
		startActivity(intent);
//		if("0".equals(type)){
//			Intent intent = new Intent(EstimateWebActivity.this, EstimateBaseActivity.class);
//			startActivity(intent);
//		}else{
//			Intent intent = new Intent(EstimateWebActivity.this, EstiRresultsWebActivity.class);
//			intent.putExtra("type", type);
//			startActivity(intent);
//		}
		cancleDialog();
		AppManager.getAppManager().finishActivity();
	}
	
	private void showDialog(){
		if(mdialog ==null){
			mdialog = new myProsessDialog(EstimateWebActivity.this, null);
		}
		mdialog.show();
	}
	
	private void cancleDialog(){
		if(mdialog !=null){
			if(mdialog.isShowing()){
				mdialog.dismiss();
			}
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
	}
	
	
	private void classifyData(List<PlanBean> mlist) {
		StringBuffer buffer = new StringBuffer();
		List<PlanBean> showList = new ArrayList<PlanBean>();
		buffer.append("{");
		int size = mlist.size();
		for (int i = 0; i < size; i++) {
			PlanBean bean = mlist.get(i);
			if("1".equals(bean.getIsshow()))
				showList.add(bean);
		}
		Gson gson = new Gson();
		gson.toJson(showList);
		PreManager.instance().saveSleepPlan(EstimateWebActivity.this, gson.toJson(showList));
	}
	
	
	
	private void getEstimateData(final String message){
		new XiangchengMallProcClass(this).getSleepIndex(PreManager.instance().getUserId(this), new InterfaceSleepIndexCallback() {
			
			@Override
			public void onSuccess(int icode, String sleep_pg, String shxg_pg,
					String smgl_pg, String smhj_pg, String xlhd_pg) {
				PreManager.instance().saveSMPGResult(EstimateWebActivity.this, sleep_pg);
				PreManager.instance().saveSMGLPGResult(EstimateWebActivity.this, smgl_pg);
				PreManager.instance().saveSHXGPGResult(EstimateWebActivity.this, shxg_pg);
				PreManager.instance().saveSMHJPGResult(EstimateWebActivity.this, smhj_pg);
				PreManager.instance().saveXLHDPGResult(EstimateWebActivity.this, xlhd_pg);
				
				PreManager.instance().saveIsCompleteSleepPg(EstimateWebActivity.this, true);
				getResault(message);
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				toastMsg(strErrMsg);
				EstimateWebActivity.this.sendBroadcast(new Intent(Constant.PINGGU_DEAL_ACTION));
			}
		});
	}
	
	private void getEstiData(final String type){
			new XiangchengMallProcClass(this).getEstimateResult(PreManager.instance().getUserId(this), type, new InterfaceEstimateCallback() {

				@Override
				public void onSuccess(int icode, JSONObject response) {
					if("1".equals(Type) && !TextUtils.isEmpty(response.toString())){
						PreManager.instance().saveJSON_SHXGPGResult(EstimateWebActivity.this, response.toString());
					}else if("2".equals(Type)  && !TextUtils.isEmpty(response.toString()) ){
						PreManager.instance().saveJSON_SMGLPGResult(EstimateWebActivity.this, response.toString());
					}else if("3".equals(Type) && !TextUtils.isEmpty(response.toString())){
						PreManager.instance().saveJSON_SMHJPGResult(EstimateWebActivity.this, response.toString());
					}else if("4".equals(Type) && !TextUtils.isEmpty(response.toString())){
						PreManager.instance().saveJSON_XLHDPGResult(EstimateWebActivity.this, response.toString());
					}
					getResault(type);
				}

				@Override
				public void onError(int icode, String strErrMsg) {
					toastMsg(strErrMsg);
				}
			});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (webView.canGoBack() && event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  
//	        webView.goBack();  
//	        return true;  
//	    } 
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			if("1".equals(Type)){
				dialogForExit();
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}else
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 当有输入信息的时候提示是否放弃本次评估
	 */
	private void dialogForExit() {
			final CustomDialog dialog = new CustomDialog(
					this);
			dialog.show();
			dialog.setTitle("是否放弃本次评估？");
			dialog.setSubGone();
			dialog.setOnLeftClickListener("继续评估", new

			com.yzm.sleep.widget.CustomDialog.MyOnClickListener() {

				@Override
				public void Onclick(View v) {

					dialog.dismiss();
				}
			});
			dialog.setOnRightClickListener("放弃评估", new

			com.yzm.sleep.widget.CustomDialog.MyOnClickListener() {

				@Override
				public void Onclick(View v) {
					dialog.dismiss();
					if("0".equals(Type))
						setResult(RESULT_OK);
					AppManager.getAppManager().finishActivity();
				}
			});
	}

	private void sendFeedBackBroadcast(){
		Intent intent = new Intent(Constant.WEEK_FEED_BACK_SUC);
//		PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent,
//				0);
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeInMillis(System.currentTimeMillis() + 2000);
//		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
//		am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
		sendBroadcast(intent);
	}
	
}
