package com.yzm.sleep.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.adapter.SecretaryListAdapter;
import com.yzm.sleep.background.SecretaryDBOperate;

public class XcSecretaryActivity extends BaseActivity {

	private ListView mListView;
	private SecretaryListAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xc_secretary);
		mListView = (ListView) findViewById(R.id.secretary_msglist);
		((TextView) findViewById(R.id.title)).setText("小秘书");
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppManager.getAppManager().finishActivity();
			}
		});
		
		mAdapter=new SecretaryListAdapter(this,SecretaryDBOperate.queryMsg(getApplicationContext()));
		mListView.setAdapter(mAdapter);
		SecretaryDBOperate.scanAllMsg(getApplicationContext());
		
	}
}
