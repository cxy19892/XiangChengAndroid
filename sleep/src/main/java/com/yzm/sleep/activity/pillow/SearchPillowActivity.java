package com.yzm.sleep.activity.pillow;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.bluetoothBLE.CopyOfPillowHelper;
import com.yzm.sleep.bluetoothBLE.PillowCallback;
import com.yzm.sleep.utils.BluetoothUtil;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.widget.SyncAlertDialog;
import com.yzm.sleep.widget.SyncAlertDialog.MySyncOnClickListener;

@SuppressLint("NewApi") public class SearchPillowActivity extends BaseActivity implements MySyncOnClickListener {

	private TextView tipse;
	private TextView tipse2;
	private Button  cancleButton;
//	private Handler handler = new Handler();
	private BluetoothAdapter mBluetoothAdapter;
	private boolean mScanning;
	private final long SCAN_PERIOD = 30000;
	
//	private Handler mHandler = null;
	public CopyOfPillowHelper pillowserver = null;
	private boolean isbund = false;
	private boolean isconnect = false;
	private boolean isneedUpLoad = false;
	private boolean selectUpLoad = false;
	private ImageView imgStart, imgSingle, imgPhone;
	private AnimationDrawable animationDrawable;  
	private boolean isGetBlutoothDev = false;
//	private MyDialog syncDataDialog;
//	private MyDialog exitDialog;
	private boolean ISBLUETOOTHSTATE_ON = true;
	private SyncAlertDialog syncDialog;
	
//	private boolean isFind = false;
	private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
//        	LogUtil.w("chen", isGetBlutoothDev+"发现新设备"+device.getAddress()+"设备名称："+device.getName());
        	if(!isGetBlutoothDev && (device != null) && device.getName() != null && device.getName().equals("OrangePillow") && !TextUtils.isEmpty(device.getAddress())){//找到设备
//        		LogUtil.w("chen", isGetBlutoothDev+"发现新设备"+device.getAddress());
        		isGetBlutoothDev = true;
        		startToConnectDev(device);
      	    }

        }
    };
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 
		setContentView(R.layout.activity_search_pillow);
		initViews();
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            AppManager.getAppManager().finishActivity();
        }
		final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            AppManager.getAppManager().finishActivity();
            return;
        }
        String sensitive = "6,2,2";
        sensitive = Util.getSensitive(PreManager
				.instance().getAllSensitives(this), Integer.parseInt(PreManager
					.instance().getBluetoothDevSensitive(this)));
        
		pillowserver = CopyOfPillowHelper.Getinstance(this, callback, sensitive);

		if (ISBLUETOOTHSTATE_ON) {
			mHandler.sendEmptyMessageDelayed(0, 500);
		}

		startAnim();
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("PairSP_Searching");
		MobclickAgent.onPause(this);
	}
	
	@SuppressLint("HandlerLeak") 
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				StartToSearchDev();
			}else if(msg.what == 1){
				mScanning = false;
	            mBluetoothAdapter.stopLeScan(mLeScanCallback);
	            
	            if(!mScanning){
	            	isGetBlutoothDev = false;
	           	 	mScanning = true;
	                mBluetoothAdapter.startLeScan(mLeScanCallback);
	           }
			}/*else if(msg.what == 2){
				if(syncDialog.isShowing()){
					syncDialog.upDataBundtimerBtn(timingValue);
				}
			}else if(msg.what == 3){
				OnSyncClick(6);
			}*/else if(msg.what == 4){
				tipse2.setVisibility(View.GONE);
			}
		}
		
	};
	
	private void reStartSearchDev(){
		if(pillowserver == null){
			String sensitive = "6,2,2";
	        sensitive = Util.getSensitive(PreManager
					.instance().getAllSensitives(this), Integer.parseInt(PreManager
						.instance().getBluetoothDevSensitive(this)));
	        
			pillowserver = CopyOfPillowHelper.Getinstance(this, callback, sensitive);
		}
		
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				if (ISBLUETOOTHSTATE_ON) {
					mHandler.sendEmptyMessage(1);
				}
				
			}
		}, 5000);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("PairSP_Searching"); 
		MobclickAgent.onResume(this);
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		mBluetoothAdapter.stopLeScan(mLeScanCallback);
		if(pillowserver!=null)
			pillowserver.Stop_server(this);
		unregisterReceiver(bluetoothState);
		super.onDestroy();


	}

	private void initViews() {
		tipse = (TextView) findViewById(R.id.sarch_pillow_tips);
		tipse2= (TextView) findViewById(R.id.sarch_pillow_tips_2);
		cancleButton = (Button) findViewById(R.id.btn_cancle);
		imgStart = (ImageView) findViewById(R.id.search_device_dev);
		imgSingle= (ImageView) findViewById(R.id.search_device_sigl);
		imgPhone = (ImageView) findViewById(R.id.search_device_phone);
//		cancleButton.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
		cancleButton.setOnClickListener(this);
		imgStart.setOnClickListener(this);
		tipse2.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				tipse2.setVisibility(View.GONE);
			}
		}, 30*1000);
		registerReceiver(bluetoothState,new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
	}
	
	private void startAnim(){
		imgSingle.setImageResource(R.drawable.search_dev_animation);  
        animationDrawable = (AnimationDrawable) imgSingle.getDrawable();  
        animationDrawable.start();
	}
	private void stopAnim(){
//		animationDrawable = (AnimationDrawable)imgSingle.getDrawable();  
        animationDrawable.stop();
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancle:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.search_device_dev:
			
			break;
		default:
			break;
		}
	}
	
	private  void StartToSearchDev(){
		if(!this.isFinishing()){
			boolean isOpen = BluetoothUtil.bluetoothIsOn(this);
			if(isOpen){
				if(!mScanning){//开始搜索
		               scanLeDeviceStart();//(true);
		          }else{//重新搜索
		               scanLeDeviceReStart();//(false);
		          }
			}else{
				tipse.setText("未打开蓝牙，无法搜索");
				imgPhone.setImageResource(R.drawable.bluetooth_closed_in_img_and_text);
			}
		}
	}
	
	
	
	
    
    private void startToConnectDev(final BluetoothDevice device){
    		mHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					pillowserver.startConnectDev(2, device.getAddress());
					mScanning = false;
		            mBluetoothAdapter.stopLeScan(mLeScanCallback);
				}
			}, 50);
    }
    /**
     * 开始搜索设备
     */
	private void scanLeDeviceStart(){
		LogUtil.w("chen", isGetBlutoothDev+"=isGetBlutoothDev+scanLeDeviceStart+mScanning="+mScanning);
        if(!mScanning){
        	isGetBlutoothDev = false;
        	 mScanning = true;
             mBluetoothAdapter.startLeScan(mLeScanCallback);
        }
	}
	/**
	 * 重新开始搜索设备
	 */
	private void scanLeDeviceReStart(){
		LogUtil.w("chen", isGetBlutoothDev+"=isGetBlutoothDev+scanLeDeviceReStart");
		if(mScanning){
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            
            if(!mScanning){
            	isGetBlutoothDev = false;
           	 	mScanning = true;
                mBluetoothAdapter.startLeScan(mLeScanCallback);
           }
    	}
	}

	
	private PillowCallback callback = new PillowCallback() {
		/**8000:传入数据有误，提示需要先绑定在同步<br>
		 * 8001:没有找到设备<br>
		 * 8002:找到了多个设备<br>
		 * 8003:找到设备开始链接<br>
		 * 8004:未连接GATT服务端<br>
		 * 8005:未发现GATT服务<br>
		 * 8006:DEVICE_DOES_NOT_SUPPORT_UART<br>
		 * 9000:密码校验失败<br>
		 * 9001:密码校验成功<br>
		 * 9002:更换绑定成功<br>
		 * 9003:更滑绑定失败<br>
		 * 9004:电池电量返回 msg.obj<br>
		 * 9005:固件版本返回 msg.obj<br>
		 * 9006:数据包个数     msg.obj<br>
		 * 9007:固件收到发送的升级命令返回<br>
		 * 9008:数据包接受中 百分比 <br>
		 * 9009:数据包接收结束<br>
		 * 9010:时间同步完成<br>
		 * 9011:恢复出厂设置完成<br>
		 * 9012:异常结束<br>
		 * 9020:询问是否继续绑定
		 */
		@Override
		public void getPillowcallback(Message msg) {
			LogUtil.w("chen", "getPillowcallback " + msg.what);
			System.out.println("getPillowcallback " + msg.what);
				switch (msg.what) {
				case 9020:
					LogUtil.e("chen", "----询问是否继续绑定");
					showRemindDialog();
					
					break;
				case 8003:
//					btnStart.setText(getResources().getString(
//							R.string.bind_pillow_text17));
					break;
				case 8004://gatt 断开
					LogUtil.e("chen", "gatt 断开  isbund="+isbund+";isconnect="+isconnect);
					if (isbund && isconnect/* && disconnect*/) {
//						mHandler.postDelayed(new Runnable() {
//							
//							@Override
//							public void run() {
								if(isbund){
									
									Toast toast = Toast
											.makeText(
													SearchPillowActivity.this,
													"绑定成功",
													Toast.LENGTH_LONG);
									toast.setGravity(Gravity.CENTER, 0, 0);
									toast.show();
									
									mBluetoothAdapter.stopLeScan(mLeScanCallback);
							    	if(pillowserver!=null)
							    		pillowserver.Stop_server(SearchPillowActivity.this);
							    	if(!SearchPillowActivity.this.isFinishing()){
								    	if(syncDialog!=null)
											syncDialog.cancel();
								    	}
							    	if(isneedUpLoad && selectUpLoad){
							    		//选择了升级就只是关闭当前页面
							    	}else{
							    		//从引导页面进入绑定枕头成功后自动进入主界面显示硬件数据展示
							    		Intent sucIntent = new Intent(SearchPillowActivity.this, BundPillowInfoActivity.class);
										sucIntent.putExtra("frombund", true);
										startActivity(sucIntent);
							    	}
							    	AppManager.getAppManager().finishActivity(SensitiveSetActivity.class);
							    	AppManager.getAppManager().finishActivity(SearchPillowActivity.this);
									
								}
//							}
//						}, 200);
						
					} else if(isconnect) {
						StartToSearchDev();
					}

					break;
				case 8005:
					break;
				case 8006:
//					StartToSearchDev();
					break;
				case 8007:
					isconnect = true;
					break;
				case 9002:
					isbund = true;
					//网络请求版本
					
					break;
				case 9009:
					isbund = true;
					
					break;
				case 9010:
					isbund = true;
					break;
				case 9016:// 有新的升级信息
//					PreManager.instance().saveSyncHardwareSleepDate(SearchPillowActivity.this, PreManager.instance().getUserId(SearchPillowActivity.this) + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
					isneedUpLoad = true;
					selectUpLoad = false;
					syncDialog.updataViews(3);
				default:
					break;
				}
		}

		@Override
		public void getPillowError(int errorcode, String errormsg) {
		}

		@Override
		public void pillowData(byte[] buffer) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private void showRemindDialog(){
		isneedUpLoad = false;
		if(syncDialog == null){
			syncDialog = new SyncAlertDialog(SearchPillowActivity.this, R.style.bottom_animation,this);
		}
		syncDialog.show();
		syncDialog.setCanceledOnTouchOutside(false);
		syncDialog.updataViews(4);
		startTime();
		syncDialog.setOnKeyListener(new OnKeyListener() {  

			@Override
			public boolean onKey(android.content.DialogInterface dialog,
					int keyCode, android.view.KeyEvent event) {
				if (keyCode == android.view.KeyEvent.KEYCODE_BACK  
	                     && event.getRepeatCount() == 0) {  
					MobclickAgent.onEvent(SearchPillowActivity.this, "653");
					if(!SearchPillowActivity.this.isFinishing()){
						if(syncDialog!=null){
							syncDialog.cancel();
							}
					}
					if(pillowserver!=null){
						pillowserver.cancleAutoControl();
					}
				}  
				return false;
			}  
	     });  
		
	}


	
	BroadcastReceiver bluetoothState = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			if(!SearchPillowActivity.this.isFinishing()){
				String stateExtra = BluetoothAdapter.EXTRA_STATE;
				int state = intent.getIntExtra(stateExtra, -1);
				switch(state) {
				case BluetoothAdapter.STATE_TURNING_ON:
					break;
				case BluetoothAdapter.STATE_ON:
					if(!ISBLUETOOTHSTATE_ON){
						ISBLUETOOTHSTATE_ON = true;
						tipse.setText(R.string.tips_sarch_pillow_1);
						imgPhone.setImageResource(R.drawable.linellae_phone_img);
						reStartSearchDev();
						startAnim();
					}
					break;
				case BluetoothAdapter.STATE_TURNING_OFF:
					if(syncDialog!=null && syncDialog.isShowing()){
						syncDialog.dismiss();
					}
					break;
				case BluetoothAdapter.STATE_OFF:
					ISBLUETOOTHSTATE_ON = false;
					mHandler.sendEmptyMessage(4);
					mScanning = false;
		            mBluetoothAdapter.stopLeScan(mLeScanCallback);
					tipse.setText("未打开蓝牙，无法搜索");
					tipse2.setVisibility(View.GONE);
					imgPhone.setImageResource(R.drawable.bluetooth_closed_in_img_and_text);
					
					if(pillowserver!=null){
						pillowserver.Stop_server(SearchPillowActivity.this);
						pillowserver = null;
					}
					stopAnim();
					break;
				}
			}
		}
	};


	@Override
	public void OnSyncClick(int value) {
		switch (value) {
		case 3:   //取消升级
			MobclickAgent.onEvent(SearchPillowActivity.this, "655");
			pillowserver.cancleDevUpload();
			if(syncDialog!=null)
				syncDialog.cancel();
			break;
		case 4:   //确认升级
			selectUpLoad = true;
			pillowserver.ensureDevUpload();
			MobclickAgent.onEvent(SearchPillowActivity.this, "654");
			Intent intent = new Intent(SearchPillowActivity.this,
					PillowUpgradeActivity.class);
			intent.putExtra("frombund", true);
			startActivity(intent);
			
			if(syncDialog!=null)
				syncDialog.cancel();
			break;
		case 5:  //绑定
			MobclickAgent.onEvent(SearchPillowActivity.this, "652");
			if(pillowserver!=null){
				pillowserver.continuToBund();
			}
			if(syncDialog!=null){
				syncDialog.showWaitingView();
			}
			break;
		case 6:  //取消
			MobclickAgent.onEvent(SearchPillowActivity.this, "653");
			if(pillowserver!=null){
				pillowserver.cancleAutoControl();
			}
			if(syncDialog!=null){
				syncDialog.cancel();
			}
			break;

		default:
			break;
		}
		
	}
	
	/**
	 * 开始倒计时
	 */
	@SuppressLint("HandlerLeak") 
	private void startTime(){
		final Handler handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				if(msg.what == 1 && syncDialog != null && syncDialog.isShowing()){
					if(msg.arg1 >= 0)
						syncDialog.upDataBundtimerBtn(msg.arg1);
					else{
						syncDialog.upDataBundtimerBtn(-1);
						if(syncDialog.isShowBundCancle()){
							OnSyncClick(6);
						}
					}
				}
			}
			
		};
		new Thread(){

			@Override
			public void run() {
				try {
					int i = 10;
					while (i >= -1) {
						Message msg = Message.obtain();
						msg.what = 1;
						msg.arg1 = i;
						handler.sendMessage(msg);
						i --;
						sleep(1000);
					}
				} catch (Exception e) {
				}
			}
			
		}.start();
	}
	
}
