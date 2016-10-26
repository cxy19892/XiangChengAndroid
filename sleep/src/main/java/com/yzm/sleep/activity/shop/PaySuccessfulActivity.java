package com.yzm.sleep.activity.shop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.utils.BottomPopDialog;
import com.yzm.sleep.utils.BottomPopDialog.PopDialogClickListener;
import com.yzm.sleep.utils.Util;

/**
 * 购买成功
 * 
 * @author tianxun
 * @params kefu String //客服电话
 */
public class PaySuccessfulActivity extends BaseActivity {
	private Button btn_title_right2;
	private BottomPopDialog popDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paysuccessful);
		initView();
	}

	private void initView() {
		btn_title_right2 = (Button) findViewById(R.id.btn_title_right2);
		btn_title_right2.setVisibility(View.VISIBLE);
		btn_title_right2.setText("客服");
		btn_title_right2.setOnClickListener(this);

		findViewById(R.id.back).setOnClickListener(this);
		((TextView) findViewById(R.id.title)).setText("支付成功");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.btn_title_right2:
			final String kefu = getIntent().getStringExtra("kefu");
			if(!TextUtils.isEmpty(kefu)){
				if(popDialog == null){
					popDialog = new BottomPopDialog(this, new PopDialogClickListener() {
						
						@Override
						public void PopDialogClick(int clickid) {
							if(1 == clickid){
								toastMsg("拨打客服电话");
								 Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+kefu));  
					           startActivity(intent);  
							}
							if(!PaySuccessfulActivity.this.isFinishing()){
								if(popDialog!=null){
									popDialog.cancel();
								}
							}
						}
					});
				}
				popDialog.show();
				popDialog.setShowViews(1, "确定拨打客服电话\n"+kefu, "");
			}else{
				Util.show(this, "没有留下电话");
			}
			break;
		default:
			break;
		}
	}
}
