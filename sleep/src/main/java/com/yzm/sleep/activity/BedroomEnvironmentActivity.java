package com.yzm.sleep.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.adapter.BedroomEnvAdapter;
import com.yzm.sleep.bean.EnvironmentBean;
import com.yzm.sleep.model.BedroomSuggestDialog;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSleepEnvironmentCallBack;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;

/**
 *升级卧室
 * @author 
 */
public class BedroomEnvironmentActivity extends BaseActivity {
	
	private int requestCode = 100;
	private GridView mGridView;
	private BedroomEnvAdapter mAdapter;
	private View noNet;
	private BedroomSuggestDialog dialog;
	private ProgressUtils pro;
	/**
	 * 显示进度
	 */
	private void showPro() {
		if (pro == null) {
			pro = new ProgressUtils(this);
		}
		pro.show();
	}

	/**
	 * 取消进度
	 */
	private void cancelPro() {
		if (pro != null) {
			pro.dismiss();
			pro = null;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bedroom_environment);
		initView();
		getDatas();
	}
	
	private void initView(){
		findViewById(R.id.back).setOnClickListener(this);
		((TextView)findViewById(R.id.title)).setText("卧室检测");
		Button right = (Button) findViewById(R.id.btn_title_right);
		right.setVisibility(View.VISIBLE);
		right.setText("重评");
		right.setOnClickListener(this);
		mGridView = (GridView) findViewById(R.id.gridView);
		mAdapter = new BedroomEnvAdapter(this);
		mGridView.setAdapter(mAdapter);
		noNet = findViewById(R.id.no_net);
		((TextView)findViewById(R.id.text)).setText("请检查网络设置");
		
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				dialog = new BedroomSuggestDialog(BedroomEnvironmentActivity.this);
				dialog.setData(mAdapter.getDatas().get(position));
				dialog.show();
			}
		});
	}
	
	private void getDatas(){
		if(!Util.checkNetWork(this)){
			noNet.setVisibility(View.VISIBLE);
			return;
		}
		showPro();
		new XiangchengMallProcClass(this).sleepEnvironment(PreManager.instance().getUserId(this), new InterfaceSleepEnvironmentCallBack() {
			
			@Override
			public void onSuccess(String icode, List<EnvironmentBean> datas) {
				cancelPro();
				mAdapter.setDatas(datas);
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
				cancelPro();
				Util.show(BedroomEnvironmentActivity.this, strErrMsg);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		getDatas();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.back:
				AppManager.getAppManager().finishActivity();
				break;
			case R.id.btn_title_right:
				startActivityForResult(new Intent(this, EstimateWebActivity.class).putExtra("type", "0"), requestCode);
				break;
			default:
				break;
		}
	}
	
}
