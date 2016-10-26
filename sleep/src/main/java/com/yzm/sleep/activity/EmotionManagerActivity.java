package com.yzm.sleep.activity;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.utils.InterfaceMallUtillClass.MoodManageCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.MoodManageParams;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ToastManager;
import com.yzm.sleep.utils.XiangchengMallProcClass;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 情绪管理类
 * @author chen
 *
 */
public class EmotionManagerActivity extends BaseActivity {
	
	private Button titleRightBtn;
	private EditText edThinks, edtFeels, edtPositive, edtHopful;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_emotion_manager);
		
		initviews();
		//请求数据，请求成之后。先判断对应的输入框里面有无输入，如果有输入：请求回来的数据添加在已输入的字段之前+
	}

	private void initviews() {
		titleRightBtn = (Button) findViewById(R.id.btn_title_right);
		findViewById(R.id.back).setOnClickListener(this);
		((TextView)findViewById(R.id.title)).setText("情绪管理");
		titleRightBtn.setVisibility(View.VISIBLE);
		titleRightBtn.setText("保存");
		titleRightBtn.setOnClickListener(this);
		
		edThinks = (EditText) findViewById(R.id.sleep_thinking_edt);
		edtFeels = (EditText) findViewById(R.id.sleep_feeling_edt);
		edtPositive = (EditText) findViewById(R.id.sleep_positive_edt);
		edtHopful = (EditText) findViewById(R.id.sleep_hopeful_edt);
		
		edThinks.setText(getIntent().getStringExtra("thinking"));
		edtFeels.setText(getIntent().getStringExtra("feeling"));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.btn_title_right:
			//提交到服务器
			String thinkStr = edThinks.getText().toString();
			String feelsStr = edtFeels.getText().toString();
			String positiStr= edtPositive.getText().toString();
			String HopfStr = edtHopful.getText().toString();
			if(TextUtils.isEmpty(thinkStr)||TextUtils.isEmpty(feelsStr)||TextUtils.isEmpty(positiStr)||TextUtils.isEmpty(HopfStr)){
				ToastManager.getInstance(EmotionManagerActivity.this).show("内容不完整");
			}else{
				StringBuffer sb = new StringBuffer();
				sb.append("ti_1##");
				sb.append(thinkStr);
				sb.append("&ti_2##");
				sb.append(feelsStr);
				sb.append("&ti_3##");
				sb.append(positiStr);
				sb.append("&ti_4##");
				sb.append(HopfStr);
				commitEmotions(sb.toString());
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			AppManager.getAppManager().finishActivity();
			return false;
		}else
		return super.onKeyDown(keyCode, event);
	}
	//
	
	private void commitEmotions(String emoStr){
		MoodManageParams mParams = new MoodManageParams();
		mParams.my_int_id = PreManager.instance().getUserId(this);
		mParams.content = emoStr;
		new XiangchengMallProcClass(EmotionManagerActivity.this).moodManage(mParams, new MoodManageCallBack() {
			
			@Override
			public void onSuccess(int icode, String strSuccMsg) {
				ToastManager.getInstance(EmotionManagerActivity.this).show(strSuccMsg);
				AppManager.getAppManager().finishActivity(EmotionManagerActivity.class);
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				ToastManager.getInstance(EmotionManagerActivity.this).show(strErrMsg);				
			}
		});
	}
}
