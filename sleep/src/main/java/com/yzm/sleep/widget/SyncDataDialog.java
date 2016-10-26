package com.yzm.sleep.widget;

import com.yzm.sleep.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class SyncDataDialog extends AlertDialog {

	private TextView tvSyncData, tvAbstruct1, tvAbstruct2, tvProgree;
	private String loadString;
	private ProgressBar bar;
	private ImageView iv;
	private RelativeLayout rela;
	
	@SuppressLint("InlinedApi") 
	public SyncDataDialog(Context context) {
		super(context, R.style.sync_dialog);
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 
		setContentView(R.layout.sync_pillow_data);
		tvSyncData = (TextView) findViewById(R.id.sync_title);
		rela = (RelativeLayout) findViewById(R.id.count_rel_layout);
		tvAbstruct1 = (TextView) findViewById(R.id.sync_abstruct1);
		tvAbstruct2 = (TextView) findViewById(R.id.sync_abstruct2);
		tvProgree = (TextView) findViewById(R.id.sync_progree);
		bar = (ProgressBar) findViewById(R.id.sync_progreebar);
		iv = (ImageView) findViewById(R.id.sync_img);
		loadString = "正在连接香橙智能枕扣";
		animationLoading(tvSyncData);
		
	}

	public void changeSyncLoading(){
		loadString = "同步数据中";
		if(iv.getVisibility() != View.GONE)
			iv.setVisibility(View.GONE);
		if(tvAbstruct1.getVisibility() != View.GONE)
			tvAbstruct1.setVisibility(View.GONE);
		if(tvAbstruct2.getVisibility() != View.GONE)
			tvAbstruct2.setVisibility(View.GONE);
		if(bar.getVisibility() != View.VISIBLE){
			bar.setVisibility(View.VISIBLE);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			lp.topMargin = 50;
			rela.setLayoutParams(lp);
			int width = (int) tvSyncData.getPaint().measureText("同步数据中...");
			RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT);
			lp1.addRule(RelativeLayout.CENTER_HORIZONTAL);
			lp1.addRule(RelativeLayout.ABOVE, rela.getId());
			tvSyncData.setLayoutParams(lp1);
			tvSyncData.setPadding(0, 0, 0, 0);
		}
		if(tvProgree.getVisibility() != View.VISIBLE)
			tvProgree.setVisibility(View.VISIBLE);
		if(tvProgree.getVisibility() != View.VISIBLE)
			tvProgree.setVisibility(View.VISIBLE);
	}
	
	public void setProgreeText(String loadNum){
		tvProgree.setText(loadNum);
	}
	
	private boolean go = true;
	
	@SuppressLint("HandlerLeak")
	private void animationLoading(final TextView tv){
		final Handler handle = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				tv.setText(loadString + msg.obj.toString());
				super.handleMessage(msg);
			}
			
		};
		
		new Thread(){

			@Override
			public void run() {
				int i = 1;
				while (go) {
					try {
						sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					int what = i % 3 + 1;
					StringBuffer sb = new StringBuffer();
					for (int j = 0; j < what; j++) {
						sb.append(".");
					}
					Message msg = handle.obtainMessage();
					msg.obj = sb;
					handle.sendMessage(msg);
					i++;
				}
			}
			
		}.start();
		
	}


	@Override
	public void cancel() {
		int width = (int) tvSyncData.getPaint().measureText("正在连接香橙智能枕扣...");
		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT);
		lp1.addRule(RelativeLayout.CENTER_HORIZONTAL);
		lp1.addRule(RelativeLayout.ABOVE, rela.getId());
		tvSyncData.setLayoutParams(lp1);
		tvSyncData.setPadding(0, 0, 0, 0);
		super.cancel();
	}
	
	
	
	
}
