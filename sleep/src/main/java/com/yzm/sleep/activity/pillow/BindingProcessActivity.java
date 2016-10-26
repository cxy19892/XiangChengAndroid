package com.yzm.sleep.activity.pillow;

import java.net.URL;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceGetBuyUrlCallback;
import com.yzm.sleep.utils.URLUtil;
import com.yzm.sleep.utils.XiangchengProcClass;

/**
 * 枕扣绑定说明
 * 
 * @author MS
 * 
 */
public class BindingProcessActivity extends BaseActivity implements
		OnClickListener {

	private BindingProcessActivity activity;
	private Button btnBuyDev;
	private Button butBinding;
	private Button butbreak;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_binding);
		activity = this;
		initView();
	}

	private void initView() {
		((TextView) findViewById(R.id.title)).setText("香橙智能枕扣");
		btnBuyDev  = (Button) findViewById(R.id.btn_buying);
		butBinding = (Button) findViewById(R.id.butBinding);
		butBinding.setOnClickListener(this);
		butbreak = (Button) findViewById(R.id.back);
		butbreak.setOnClickListener(this);
		btnBuyDev.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == butBinding) {
			Intent intent = new Intent(activity, SensitiveSetActivity.class);
			startActivity(intent);
			AppManager.getAppManager().finishActivity();
		} else if (v == butbreak) {
			AppManager.getAppManager().finishActivity();
		} else if (v == btnBuyDev) {
			if(checkNetWork(activity)){
			new XiangchengProcClass(activity).getBuyUrlData(new InterfaceGetBuyUrlCallback() {
				
				@Override
				public void onSuccess(int icode, String strSucMsg, String url) {
					Uri uri = Uri.parse(url);
					Intent it = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(it);
				}
				
				@Override
				public void onError(int icode, String strErrMsg) {
					toastMsg(strErrMsg);
				}
			});
			}else{
				toastMsg("网络连接失败,请稍后在试");
			}
		}
	}

}
