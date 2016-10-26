package com.yzm.sleep.activity.pillow;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.activity.WelcomeActivity;
import com.yzm.sleep.model.MyDialog;
import com.yzm.sleep.utils.BluetoothUtil;
import com.yzm.sleep.utils.CommunityProcClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.HardwareBoundParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceHardwareBoundCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceMofifyHardwareSensitivityCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.MofifyHardwareSensitivityParamClass;
import com.yzm.sleep.utils.PreManager;

public class SensitiveSetActivity extends BaseActivity {
	private TextView Title;
	private Button titleLeft, titleRight;
	private RelativeLayout type1_rl, type2_rl, person1_rl, person2_rl;
	private int typeselect = 0;
	private int personselect = 0;
	private ImageView type1_check, type2_check, person1_check, person2_check;
	private boolean isbund = false;
	private boolean ISNEED_TO_OPEN_BLUETOOTH = false;
	private TextView pillowSoft, pillowHard, peopleOne, peopleTwo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_sensitive_set);
		isbund = getIntent().getBooleanExtra("isbund", false);
		initViews();
		UploadFailedBundInfo();
		SetDefaultType(PreManager.instance().getBluetoothDevSensitive(this));
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("PairSP_SensitivityCalibration"); 
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("PairSP_SensitivityCalibration");
		MobclickAgent.onPause(this);
	}

	private void initViews() {
		Title = (TextView) findViewById(R.id.title);
		titleLeft   = (Button) findViewById(R.id.back);
		titleRight  = (Button) findViewById(R.id.btn_title_right);
		Title.setText("睡眠环境");
		titleRight.setVisibility(View.VISIBLE);
		titleRight.setCompoundDrawables(null, null, null, null);
		titleRight.setText("下一步");
		titleLeft.setOnClickListener(this);
		titleRight.setOnClickListener(this);
		type1_rl = (RelativeLayout) findViewById(R.id.pillow_type_ro1);
		type2_rl = (RelativeLayout) findViewById(R.id.pillow_type_ro2);
		
		person1_rl = (RelativeLayout) findViewById(R.id.pillow_persons_ro1);
		person2_rl = (RelativeLayout) findViewById(R.id.pillow_persons_ro2);
		
		type1_check = (ImageView) findViewById(R.id.pillow_type1_img);
		type2_check = (ImageView) findViewById(R.id.pillow_type2_img);
		person1_check = (ImageView) findViewById(R.id.oneperson_img);
		person2_check = (ImageView) findViewById(R.id.twoperson_img);
		
		pillowSoft = (TextView) findViewById(R.id.tv_pillow_soft);
		pillowHard = (TextView) findViewById(R.id.tv_pillow_hard);
		peopleOne = (TextView) findViewById(R.id.tv_peoples_one);
		peopleTwo = (TextView) findViewById(R.id.tv_peoples_two);
		
		type1_rl.setOnClickListener(this);
		type2_rl.setOnClickListener(this);
		person1_rl.setOnClickListener(this);
		person2_rl.setOnClickListener(this);
		if(isbund){
			titleRight.setText("确认");
		}else{
			titleRight.setText("下一步");
		}
		registerReceiver(bluetoothState,new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
	}

	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.btn_title_right:
			//saveModify
			if(!isbund){//如果还没绑定
				if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
		            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
		        }else{
		        	if(TextUtils.isEmpty(gettType()) || "0".equals(gettType())){
		        		Toast.makeText(this, "请选择睡眠环境", Toast.LENGTH_SHORT).show();
		        	}else{
		        		MobclickAgent.onEvent(this, "651");
		        		PreManager.instance().setBluetoothDevSensitive(this, gettType());
		        		boolean isOpen = BluetoothUtil.bluetoothIsOn(this);
		        		if(isOpen){
		        			//		    			toastMsg(gettType());
		        			Intent intentTosetsensitiveIntent = new Intent(this, SearchPillowActivity.class);
		        			startActivity(intentTosetsensitiveIntent);
//		        			AppManager.getAppManager().finishActivity();
		        		}else{
		        			showOpenbluetoothDialog();
		        		}
		        	}
		        }
			}else{
				saveModify();
			}
			break;
		case R.id.pillow_type_ro1:
			typeselect = 0x0000;
			type1_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_selected));//setVisibility(View.VISIBLE);
			type2_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_normal));//setVisibility(View.INVISIBLE);
			pillowHard.setTextColor(getResources().getColor(R.color.onet_color));
			pillowSoft.setTextColor(getResources().getColor(R.color.fct_color));
			break;
		case R.id.pillow_type_ro2:
			typeselect = 0x0100;
			type1_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_normal));//setVisibility(View.INVISIBLE);
			type2_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_selected));//setVisibility(View.VISIBLE);
			pillowHard.setTextColor(getResources().getColor(R.color.fct_color));
			pillowSoft.setTextColor(getResources().getColor(R.color.onet_color));
			break;
		case R.id.pillow_persons_ro1:
			personselect = 0x00;
			person2_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_normal));//setVisibility(View.INVISIBLE);
			person1_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_selected));//setVisibility(View.VISIBLE);
			peopleTwo.setTextColor(getResources().getColor(R.color.fct_color));
			peopleOne.setTextColor(getResources().getColor(R.color.onet_color));
			break;
		case R.id.pillow_persons_ro2:
			personselect = 0x01;
			person1_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_normal));//setVisibility(View.INVISIBLE);
			person2_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_selected));//setVisibility(View.VISIBLE);
			peopleTwo.setTextColor(getResources().getColor(R.color.onet_color));
			peopleOne.setTextColor(getResources().getColor(R.color.fct_color));
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(bluetoothState);
		super.onDestroy();
	}
	
	private void showOpenbluetoothDialog() {
		
		final MyDialog exitDialog = new MyDialog(this, 
				0, 
				0, 
				R.layout.dialog_open_blutooth,
				R.style.bottom_animation, 
				Gravity.BOTTOM, 
				1.0f,
				0.0f);
		exitDialog.setCanceledOnTouchOutside(false);

		// 得到view中的控件
		TextView dialog_title = (TextView) exitDialog.findViewById(R.id.dialog_title);
		
		dialog_title.setText("香橙应用想使用蓝牙功能，是否允许？");
		
		Button btn_allow = (Button) exitDialog.findViewById(R.id.btn_allow_open_bluetooth);
		Button btn_refuse= (Button) exitDialog.findViewById(R.id.btn_refuse_open_bluetooth);

		btn_allow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
				ISNEED_TO_OPEN_BLUETOOTH = true;
				BluetoothAdapter mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
				mBluetoothAdapter.enable();
				exitDialog.cancel();
			}
		});

		btn_refuse.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				exitDialog.cancel();
			}
		});

	// 显示
		exitDialog.show();
	}


	private String gettType(){
		String typeset = "0";
		switch (typeselect + personselect) {
			case 0x0000://一个人硬枕头
				typeset = "2";
				break;
			case 0x0001://两个人硬枕头
				typeset = "4";	
				break;
			case 0x0100://一个人软枕头
				typeset = "1";
				break;
			case 0x0101://两个人软枕头
				typeset = "3";
				break;
			case 0x1111:
				typeset = "0";
			default:
				break;
		}
		
		return typeset;
		
	}
	
	private void SettType(){
		switch (typeselect + personselect) {
			case 0x0000://一个人硬枕头--2
				type1_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_selected));//setVisibility(View.VISIBLE);
				type2_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_normal));//setVisibility(View.INVISIBLE);
				person1_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_selected));//setVisibility(View.VISIBLE);
				person2_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_normal));//setVisibility(View.INVISIBLE);
				
				pillowHard.setTextColor(getResources().getColor(R.color.onet_color));
				pillowSoft.setTextColor(getResources().getColor(R.color.fct_color));
				peopleTwo.setTextColor(getResources().getColor(R.color.fct_color));
				peopleOne.setTextColor(getResources().getColor(R.color.onet_color));
				break;
			case 0x0001://两个人硬枕头--4
				type1_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_selected));//setVisibility(View.VISIBLE);
				type2_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_normal));//setVisibility(View.INVISIBLE);
				person1_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_normal));//setVisibility(View.INVISIBLE);
				person2_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_selected));//setVisibility(View.VISIBLE);
				pillowHard.setTextColor(getResources().getColor(R.color.onet_color));
				pillowSoft.setTextColor(getResources().getColor(R.color.fct_color));
				peopleOne.setTextColor(getResources().getColor(R.color.fct_color));
				peopleTwo.setTextColor(getResources().getColor(R.color.onet_color));
				break;
			case 0x0100://一个人软枕头--1
				type1_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_normal));//setVisibility(View.INVISIBLE);
				type2_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_selected));//setVisibility(View.VISIBLE);
				person1_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_selected));//setVisibility(View.VISIBLE);
				person2_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_normal));//setVisibility(View.INVISIBLE);
				pillowHard.setTextColor(getResources().getColor(R.color.fct_color));
				pillowSoft.setTextColor(getResources().getColor(R.color.onet_color));
				peopleOne.setTextColor(getResources().getColor(R.color.onet_color));
				peopleTwo.setTextColor(getResources().getColor(R.color.fct_color));
				break;
			case 0x0101://两个人软枕头--3
				type1_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_normal));//setVisibility(View.INVISIBLE);
				type2_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_selected));//setVisibility(View.VISIBLE);
				person1_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_normal));//setVisibility(View.INVISIBLE);
				person2_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_selected));//setVisibility(View.VISIBLE);
				pillowHard.setTextColor(getResources().getColor(R.color.fct_color));
				pillowSoft.setTextColor(getResources().getColor(R.color.onet_color));
				peopleOne.setTextColor(getResources().getColor(R.color.fct_color));
				peopleTwo.setTextColor(getResources().getColor(R.color.onet_color));
				break;
			case 0x1111:
				type1_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_normal));//setVisibility(View.INVISIBLE);
				type2_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_normal));//setVisibility(View.INVISIBLE);
				person1_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_normal));//setVisibility(View.INVISIBLE);
				person2_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_normal));//setVisibility(View.INVISIBLE);
				pillowHard.setTextColor(getResources().getColor(R.color.fct_color));
				pillowSoft.setTextColor(getResources().getColor(R.color.fct_color));
				peopleOne.setTextColor(getResources().getColor(R.color.fct_color));
				peopleTwo.setTextColor(getResources().getColor(R.color.fct_color));
				break;
			default:
				break;
		}
	}
	
	private void SetDefaultType(String Sensitive){
		if(TextUtils.isEmpty(Sensitive)||Sensitive.equals("0")){
			typeselect = 0x1100;
			personselect = 0x11;
		}else if(Sensitive.equals("1")){
			typeselect = 0x0100;
			personselect = 0x00;
		}else if(Sensitive.equals("2")){
			typeselect = 0x0000;
			personselect = 0x00;
		}else if(Sensitive.equals("3")){
			typeselect = 0x0100;
			personselect = 0x01;
		}else if(Sensitive.equals("4")){
			typeselect = 0x0000;
			personselect = 0x01;
		}
		
		SettType();
	}
	
	private void saveModify(){
		if(gettType().equals(PreManager.instance().getBluetoothDevSensitive(this))){
			toastMsg("未做修改操作");
		}else{
			if(!checkNetWork(this)){
				PreManager.instance().setBluetoothDevSensitive(SensitiveSetActivity.this, gettType());
				toastMsg("成功");
				AppManager.getAppManager().finishActivity();
			}else{
				PreManager.instance().setBluetoothDevSensitive(SensitiveSetActivity.this, gettType());
				MofifyHardwareSensitivityParamClass mParamClass = new MofifyHardwareSensitivityParamClass();
				mParamClass.my_int_id = PreManager.instance().getUserId(this);
				mParamClass.yjlmd = gettType();
				new CommunityProcClass(this).mofifyHardwareSensitivity(mParamClass, new InterfaceMofifyHardwareSensitivityCallBack() {
	
					@Override
					public void onError(int icode, String strErrMsg) {
						if(icode==4710){
							uploadBundInfo(gettType());
						}else{
							toastMsg(strErrMsg);
						}
					}
	
					@Override
					public void onSuccess(int icode, String strSuccess) {
						toastMsg(strSuccess);
						AppManager.getAppManager().finishActivity();
					}
	
	
				});
			}
		}
	}
	
	/**
	 * 上传绑定硬件信息
	 */
	private void uploadBundInfo(String newlmd){
		HardwareBoundParamClass mHardwareBoundParamClass = new HardwareBoundParamClass();
		mHardwareBoundParamClass.bdtime = ""+((int)(System.currentTimeMillis()/1000));
		mHardwareBoundParamClass.jbtime ="";
		mHardwareBoundParamClass.jystatus = "1";
		mHardwareBoundParamClass.my_int_id = PreManager.instance().getUserId(this);
		mHardwareBoundParamClass.yjlmd = newlmd;
		mHardwareBoundParamClass.macadd = PreManager.instance().getBundbluetoothPillow(this);
		new CommunityProcClass(this).hardwareBound(mHardwareBoundParamClass, new InterfaceHardwareBoundCallBack() {
			
			@Override
			public void onSuccess(int icode, String strSuccMsg) {
				PreManager.instance().setBluetoothBundInfo(SensitiveSetActivity.this, "");
				toastMsg(strSuccMsg);
				AppManager.getAppManager().finishActivity();
			}
			@Override
			public void onError(int icode, String strErrMsg) {
				toastMsg(strErrMsg);
				String info = PreManager.instance().getBundbluetoothPillow(SensitiveSetActivity.this)+";"+((int)(System.currentTimeMillis()/1000))+";"+PreManager.instance().getBluetoothDevSensitive(SensitiveSetActivity.this)+";"+PreManager.instance().getUserId(SensitiveSetActivity.this);
				PreManager.instance().setBluetoothBundInfo(SensitiveSetActivity.this, info);
			}
		});
	}
	
	
	/**
	 * 重新上传绑定信息（用于之前上传的时候上传失败）
	 */
	private void UploadFailedBundInfo(){
		String bundInfo = PreManager.instance().getBluetoothBundInfo(SensitiveSetActivity.this);
		if(!TextUtils.isEmpty(bundInfo)){
			String[] infoarr = bundInfo.split(";");
			if(infoarr.length==4){
				uploadBundInfo(infoarr[0], infoarr[1], infoarr[2], infoarr[3], false);
			}
		}
	}
	
	
	/**
	 * 上传绑定硬件信息
	 */
	private void uploadBundInfo(String mac, String btime, String lmd, String userId, final boolean flag){
		HardwareBoundParamClass mHardwareBoundParamClass = new HardwareBoundParamClass();
		mHardwareBoundParamClass.bdtime = btime;
		mHardwareBoundParamClass.jbtime ="";
		mHardwareBoundParamClass.jystatus = "1";
		mHardwareBoundParamClass.my_int_id = userId;
		mHardwareBoundParamClass.yjlmd = lmd;
		mHardwareBoundParamClass.macadd = mac;
		new CommunityProcClass(this).hardwareBound(mHardwareBoundParamClass, new InterfaceHardwareBoundCallBack() {
			
			@Override
			public void onSuccess(int icode, String strSuccMsg) {
				PreManager.instance().setBluetoothBundInfo(SensitiveSetActivity.this, "");
				if(flag){
					toastMsg(strSuccMsg);
					AppManager.getAppManager().finishActivity();
				}
			}
			@Override
			public void onError(int icode, String strErrMsg) {
				if(flag){
					toastMsg(strErrMsg);
				}
			}
		});
	}
	
	BroadcastReceiver bluetoothState = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			if(!SensitiveSetActivity.this.isFinishing()){
				String stateExtra = BluetoothAdapter.EXTRA_STATE;
				int state = intent.getIntExtra(stateExtra, -1);
				switch(state) {
				case BluetoothAdapter.STATE_TURNING_ON:
					break;
				case BluetoothAdapter.STATE_ON:
					if(ISNEED_TO_OPEN_BLUETOOTH){
						ISNEED_TO_OPEN_BLUETOOTH = false;
						if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
				            Toast.makeText(SensitiveSetActivity.this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
				            AppManager.getAppManager().finishActivity();
				        }else{
				        	Intent intentTosetsensitiveIntent = new Intent(SensitiveSetActivity.this, SearchPillowActivity.class);
			    			startActivity(intentTosetsensitiveIntent);
			    			AppManager.getAppManager().finishActivity();
				        }
					}
					break;
				case BluetoothAdapter.STATE_TURNING_OFF:
					break;
				case BluetoothAdapter.STATE_OFF:
					break;
				}
			}
		}
	};
	
	
	

}
