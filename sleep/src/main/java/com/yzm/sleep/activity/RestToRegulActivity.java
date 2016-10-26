package com.yzm.sleep.activity;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 减压调节
 * @author Administrator
 *
 */
public class RestToRegulActivity extends BaseActivity {

	private ImageView imgThingk, imgWrite, imgSee;
	private TextView tvThingkTitle, tvWriteTitle, tvSeeTitle, tvThingkCon, tvWriteCon, tvSeeCon;
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rest_to_regul);
		initViews();
		mContext = this;
	}
	
	private void initViews() {
		findViewById(R.id.rel_thinking).setOnClickListener(this);
		findViewById(R.id.rel_writing).setOnClickListener(this);
		findViewById(R.id.rel_seeingg).setOnClickListener(this);
		imgThingk = (ImageView) findViewById(R.id.img_thinking);
		imgWrite = (ImageView) findViewById(R.id.img_writing);
		imgSee = (ImageView) findViewById(R.id.img_seeing);
		tvThingkTitle = (TextView) findViewById(R.id.tv_title_thinking);
		tvWriteTitle = (TextView) findViewById(R.id.tv_title_writing);
		tvSeeTitle = (TextView) findViewById(R.id.tv_title_seeing);
		tvThingkCon = (TextView) findViewById(R.id.tv_context_thinking);
		tvWriteCon = (TextView) findViewById(R.id.tv_context_writing);
		tvSeeCon = (TextView) findViewById(R.id.tv_context_seeing);
		((TextView)findViewById(R.id.title)).setText("快速入睡");
		findViewById(R.id.back).setOnClickListener(this);
		tvThingkTitle.setText("催眠音频");
		tvWriteTitle.setText("思绪清零");
		tvSeeTitle.setText("认知重构");
		tvThingkCon.setText("睡不着又放松不了，睡眠引导音频帮你快速入眠。");
		tvWriteCon.setText("将此刻困扰你的事情写下来，让今天到此为止。");
		tvSeeCon.setText("找出你的消极想法，用更实际、更准确的想法来取而代之。");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			AppManager.getAppManager().finishActivity();
			return false;
		}else
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.rel_thinking:
			intent = new Intent(mContext, PlayMusicActivity.class); 
			break;
		case R.id.rel_writing:
			intent = new Intent(mContext, WriteNoteActivity.class);
			break;
		case R.id.rel_seeingg:
			intent = new Intent(mContext, ComfortActivity.class);
			break;
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		default:
			break;
		}
		if(intent != null){
			startActivity(intent);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	
	
}
