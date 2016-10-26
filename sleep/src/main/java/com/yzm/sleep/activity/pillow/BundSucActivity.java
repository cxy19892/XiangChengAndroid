package com.yzm.sleep.activity.pillow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.utils.CommunityProcClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.HardwareBoundParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceHardwareBoundCallBack;
import com.yzm.sleep.utils.PreManager;

public class BundSucActivity extends BaseActivity {
	private boolean isfromBund;
	private RelativeLayout mBundLayout;
	private TextView Title;
	private Button titleLeft, titleRight;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bund_suc);
		isfromBund = getIntent().getBooleanExtra("frombund", false);
		initViews();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (isfromBund) {
			MobclickAgent.onPageStart("PairSP_Succeed"); 
		}else {
			MobclickAgent.onPageStart("PairSP_Succeed_FAQ"); 
		}
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (isfromBund) {
			MobclickAgent.onPageEnd("PairSP_Succeed");
		}else {
			MobclickAgent.onPageEnd("PairSP_Succeed_FAQ");
		}
		MobclickAgent.onPause(this);
	}

	private void initViews() {
		Title = (TextView) findViewById(R.id.title);
		titleLeft   = (Button) findViewById(R.id.back);
		titleRight  = (Button) findViewById(R.id.btn_title_right);
		
		mBundLayout = (RelativeLayout) findViewById(R.id.bund_pillow_suc_rl);
		if(isfromBund){
			Title.setText("绑定成功");
			mBundLayout.setVisibility(View.VISIBLE);
			broadcastBundSuc();//发送绑定成功的广播
			titleRight.setVisibility(View.VISIBLE);
			titleLeft.setVisibility(View.GONE);
			titleRight.setCompoundDrawables(null, null, null, null);
			titleRight.setText("关闭");
			titleRight.setOnClickListener(this);
			//上传绑定信息
			if(checkNetWork(this)){
				uploadBundInfo();
			}else{
				String info = PreManager.instance().getBundbluetoothPillow(BundSucActivity.this)+";"+((int)(System.currentTimeMillis()/1000))+";"+PreManager.instance().getBluetoothDevSensitive(BundSucActivity.this)+";"+PreManager.instance().getUserId(BundSucActivity.this);
				PreManager.instance().setBluetoothBundInfo(BundSucActivity.this, info);
			}
		}else{
			Title.setText("常见问题");
			mBundLayout.setVisibility(View.GONE);
			titleLeft.setOnClickListener(this);
		}
		findViewById(R.id.light_rl_1).setOnClickListener(this);
		findViewById(R.id.light_rl_2).setOnClickListener(this);
		findViewById(R.id.light_rl_3).setOnClickListener(this);//
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.btn_title_right:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.light_rl_1:
			intent = new Intent(BundSucActivity.this, CommonProblemActiivity.class);
			intent.putExtra("select_id", "3");
			intent.putExtra("title", "如何安装香橙智能扣枕");
			break;
		case R.id.light_rl_2:
			intent = new Intent(BundSucActivity.this, CommonProblemActiivity.class);
			intent.putExtra("select_id", "2");
			intent.putExtra("title", "如何同步和查看数据");
			break;
		case R.id.light_rl_3:
			intent = new Intent(BundSucActivity.this, CommonProblemActiivity.class);
			intent.putExtra("select_id", "1");
			intent.putExtra("title", "常见问题及解答");//如何绑定智能扣枕
			break;
		default:
			break;
		}
		if(intent != null){
			startActivity(intent);
		}
	}
	
	/**
	 * 上传绑定硬件信息
	 */
	private void uploadBundInfo(){
		HardwareBoundParamClass mHardwareBoundParamClass = new HardwareBoundParamClass();
		mHardwareBoundParamClass.bdtime = ""+((int)(System.currentTimeMillis()/1000));
		mHardwareBoundParamClass.jbtime ="";
		mHardwareBoundParamClass.jystatus = "1";
		mHardwareBoundParamClass.my_int_id = PreManager.instance().getUserId(this);
		mHardwareBoundParamClass.yjlmd = PreManager.instance().getBluetoothDevSensitive(this);
		mHardwareBoundParamClass.macadd = PreManager.instance().getBundbluetoothPillow(this);
		new CommunityProcClass(this).hardwareBound(mHardwareBoundParamClass, new InterfaceHardwareBoundCallBack() {
			
			@Override
			public void onSuccess(int icode, String strSuccMsg) {
				PreManager.instance().setBluetoothBundInfo(BundSucActivity.this, "");
			}
			@Override
			public void onError(int icode, String strErrMsg) {
//				toastMsg(strErrMsg);
				String info = PreManager.instance().getBundbluetoothPillow(BundSucActivity.this)+";"+((int)(System.currentTimeMillis()/1000))+";"+PreManager.instance().getBluetoothDevSensitive(BundSucActivity.this)+";"+PreManager.instance().getUserId(BundSucActivity.this);
				PreManager.instance().setBluetoothBundInfo(BundSucActivity.this, info);
			}
		});
	}
	
	private void broadcastBundSuc() {
        Intent intent = new Intent(Constant.BUND_BLUETOOTH_CHANGE);
        intent.putExtra("isbund", true);
        sendBroadcast(intent);
    }
	
}
