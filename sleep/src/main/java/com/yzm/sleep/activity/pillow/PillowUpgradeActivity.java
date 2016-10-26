package com.yzm.sleep.activity.pillow;

import java.io.File;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;




import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.yzm.sleep.Constant;
import com.yzm.sleep.MyApplication;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.HttpHandler;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.adapter.MyDownloadRingAdapter.ViewHolder;
import com.yzm.sleep.bean.RingtoneBean;
import com.yzm.sleep.bluetoothBLE.PillowBean;
import com.yzm.sleep.bluetoothNRF.DFUCallBack;
import com.yzm.sleep.bluetoothNRF.DFUHelper;
import com.yzm.sleep.bluetoothNRF.DFUManager;
import com.yzm.sleep.dao.AudioDAO.DownloadAudioInfoClass;
import com.yzm.sleep.utils.BluetoothUtil;
import com.yzm.sleep.utils.FileUtil;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.StringUtil;
import com.yzm.sleep.utils.ToastManager;
import com.yzm.sleep.utils.InterFaceUtilsClass.DownLoadRomInfoClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetDownLoadRomInfoParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDownLoadRomCallBack;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.SleepDataProClass;

public class PillowUpgradeActivity extends BaseActivity{
	private ProgressBar pb_load_web;
	private ImageView iv_upgrade_failed;
	private LinearLayout layout_upgrade_button;
	private TextView tv_upgrade_title;
	private DFUHelper dfuhelper = null;
	private PillowBean mypillow = null;
	private TextView percentofupload, tvTips;
	private RelativeLayout load_layout;
	public BluetoothAdapter mBluetoothAdapter;
	private BluetoothManager mBluetoothManager;
	private String mac = "";
	public boolean isdfuStarted = false;
//	private MyDialog syncDataDialog;
	private ImageView btoothClosedImageView;
	private Button cancleBtn;
	private boolean isFromBund = false;
	private String newVersionString = "";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 
		setContentView(R.layout.activity_upgrade_pillow);
		isFromBund = getIntent().getBooleanExtra("frombund", false);
		initView();
		
		getpillowinfo(PreManager.instance().getUserId(this), this);
		
//		showBlutoothAskDialog(0);
		boolean isOpen = BluetoothUtil.bluetoothIsOn(PillowUpgradeActivity.this);
		if(!isOpen){
			showBluetoothClose();
		}else{
		
		if(PillowUpgradeActivity.this.checkNetWork(PillowUpgradeActivity.this)){
			getnewversion(mypillow.getHardVersion());
		}else{
			handler.sendEmptyMessage(14);
		}
		}
		//注册蓝牙变化监听
		registerReceiver(bluetoothState,new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
	}
	
	private void initView() {
		findViewById(R.id.layout_half_trapor).getBackground().setAlpha(180);//0~255透明度值 
		
		tv_upgrade_title = (TextView) findViewById(R.id.tv_upgrade_title);
		
		pb_load_web = (ProgressBar) findViewById(R.id.pb_load_web);
		
		iv_upgrade_failed = (ImageView) findViewById(R.id.iv_upgrade_failed);
		
		layout_upgrade_button = (LinearLayout) findViewById(R.id.layout_upgrade_button);
		
		percentofupload = (TextView) findViewById(R.id.pb_load_percent);
		
		btoothClosedImageView = (ImageView) findViewById(R.id.iv_failed_bluetooth_close);
		
		cancleBtn = (Button) findViewById(R.id.btn_upgrade_cancle);
		
		tvTips = (TextView) findViewById(R.id.layout_upgrade_tips);
		
		load_layout = (RelativeLayout) findViewById(R.id.pb_load_layout);
		
		cancleBtn.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); 
		
		findViewById(R.id.btn_again_upgrade).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				upgradeView();
				if(PillowUpgradeActivity.this.checkNetWork(PillowUpgradeActivity.this)){
					getnewversion(mypillow.getHardVersion());
				}else{
					handler.sendEmptyMessage(14);
				}
			}
		});
		
		cancleBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isFromBund){
					Intent intent = new Intent(PillowUpgradeActivity.this, BundPillowInfoActivity.class);
					startActivity(intent);
				}
				finish();			
			}
		});
		
		TextView tv_later_upgrade = (TextView) findViewById(R.id.tv_later_upgrade);
		tv_later_upgrade.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);  
		tv_later_upgrade.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				toastMsg("升级完成");
				if(isFromBund){
					Intent intent = new Intent(PillowUpgradeActivity.this, BundPillowInfoActivity.class);
					startActivity(intent);
				}
				finish();
			}
		});
		
		
		
	}
	
	@SuppressLint("HandlerLeak") private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				tv_upgrade_title.setText("升级失败，你的手机不支持此功能");
				layout_upgrade_button.setVisibility(View.VISIBLE);
				pb_load_web.setVisibility(View.INVISIBLE);
				iv_upgrade_failed.setVisibility(View.VISIBLE);
				break;
			case 2://传输失败，传输被打断，设备没找到（传输失败，传输被打断 又包含蓝牙被关闭）
				upgradeFailedView();
				percentofupload.setText("");
				break;
			case 3:
				StartDevUpload();
				break;
			case 4://提示开始下载文件
				tv_upgrade_title.setText("开始下载升级文件");
				break;
			case 5://提示文件下载完成
				tv_upgrade_title.setText("升级文件下载"+msg.obj.toString()+"%");
//				StartDevUpload();
				break;
			case 6://提示文件下载失败
				percentofupload.setText("");
				upgradeFailedView();
				tv_upgrade_title.setText("升级文件下载失败");
				break;
			case 7://文件存在
				if(BluetoothUtil.bluetoothIsOn(PillowUpgradeActivity.this)){
					tv_upgrade_title.setText("开始升级智能枕扣");
					percentofupload.setText("0%");
					StartDevUpload();
				}else{
//					showBlutoothAskDialog(7);
					showBluetoothClose();
				}
				break;
			case 11:
//				tv_upgrade_title.setText("开始升级智能枕扣");
				break;
			case 10://wancheng
				
//				tv_upgrade_title.setText("升级文件传输完成");
				
				break;
			case 13:
				percentofupload.setText("100%");
				tv_upgrade_title.setText("升级智能枕扣成功");
				String devVersion = mypillow.getHardVersion();
				String oldversion = devVersion.split("_").length >=1?devVersion.split("_")[0]:devVersion;
				devVersion = devVersion.replace(oldversion,newVersionString);
				PreManager.instance().setBluetoothPillowFirmwareVersion(PillowUpgradeActivity.this,devVersion);
				clearTheUpdatas();
				this.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						if(isFromBund){
							Intent intent = new Intent(PillowUpgradeActivity.this, BundPillowInfoActivity.class);
							startActivity(intent);
						}
						finish();
					}
				}, 2000);
//				
//				pb_load_web.setVisibility(View.INVISIBLE);
				break;
			case 12:
				tv_upgrade_title.setText("正在升级智能枕扣");
				percentofupload.setText(msg.obj.toString()+"%");
				
				break;
			case 14:
				upgradeFailedView();
				tv_upgrade_title.setText("请检查网络连接");
				break;
			}
			
			super.handleMessage(msg);
		}
	};
	
	private void upgradeView(){
		tv_upgrade_title.setText("正在升级智能枕扣");
		layout_upgrade_button.setVisibility(View.GONE);
		pb_load_web.setVisibility(View.VISIBLE);
		iv_upgrade_failed.setVisibility(View.INVISIBLE);
	}
	
	private void upgradeFailedView() {
		tv_upgrade_title.setText("枕扣升级失败...");
		layout_upgrade_button.setVisibility(View.GONE);
		pb_load_web.setVisibility(View.INVISIBLE);
		iv_upgrade_failed.setVisibility(View.VISIBLE);
		cancleBtn.setVisibility(View.VISIBLE);
		tvTips.setText("枕扣将自动进行修复\n（修复约3分钟且指示灯会\n保持长亮）请耐心等待。\n\n备注：修复不会造成您的睡眠数据丢失");
	}
	
	private void getpillowinfo(String user_id, Context mContext) {
//		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(mContext);
//		SQLiteDatabase db = helper.getWritableDatabase();
//		PillowDBOperate pillowDBOperate = new PillowDBOperate(db, MyTabList.USER_PILLOWS);
//		mypillow = pillowDBOperate.qurrayByuser(user_id);
		mypillow = new PillowBean();
		mypillow.setHardVersion(PreManager.instance().getBluetoothPillowFirmwareVersion(mContext));
		mypillow.setPillowMac(PreManager.instance().getBundbluetoothPillow(mContext));
		mypillow.setPowerValue(PreManager.instance().getBluetoothPillowBatteryValue(mContext));
		mypillow.setUserId(user_id);
	}
	
	//第一步查看本地是否存在数据，如果不存在则载数据
	private void getnewversion(final String the_DEV_version){
		
		final GetDownLoadRomInfoParamClass mParam = new GetDownLoadRomInfoParamClass();
		mParam.version = the_DEV_version.split("_").length >=1?the_DEV_version.split("_")[0]:the_DEV_version;//mypillow.getHardVersion();
		new SleepDataProClass(PillowUpgradeActivity.this).GetDownloadNewRomUrl(mParam, new InterfaceDownLoadRomCallBack(){
			//
				@Override
				public void onError(int icode, String strErrMsg) {
					//提示 获取rom包失败
					System.out.println(strErrMsg);
					toastMsg(strErrMsg);
					//发送时间同步 然后结束
				}
				@Override
				public void onSuccess(int iCode, final DownLoadRomInfoClass mResClass) {
					newVersionString = mResClass.version_new;
//					new Thread(new Runnable() {
//						@Override
//						public void run() {
					if(StringUtil.isNeedUpdate(the_DEV_version.split("_").length>=0?the_DEV_version.split("_")[0]:the_DEV_version, mResClass.version_new)){
						if(!FileUtil.getHexpath().equals("noSDcard")){
							if(!FileUtil.isHexFileExist("application.hex")){
								dodownload(mResClass.rom_url, FileUtil.getHexpath()+"/application.hex");
							}else{//存在也重新下载，可能存在的文件不是正确的文件
								FileUtil.deleteFile(FileUtil.getHexpath());
								dodownload(mResClass.rom_url, FileUtil.getHexpath()+"/application.hex");
							}
						}else{
							handler.sendEmptyMessage(6);
						}
					}else{
						toastMsg("当前版本是最新版本");
					}
							
//						}
//						}).start();
				}
			});
	}
	
	
	/**
	 * 下载流文件
	 * @param newUrlString url
	 * @param target       保存路径
	 */
//	private void dodownload(String newUrlString, String target){
//		HttpUtils http = new HttpUtils();
//		HttpHandler handler1 = http.download(newUrlString,
//		target, 
//		false, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
//		false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
//	new RequestCallBack<File>() {
//		@Override
//		public void onStart() {
//			super.onStart();
//			handler.sendEmptyMessage(4);
//		}
//		
//		@Override
//		public void onLoading(long total, long current,
//				boolean isUploading) {
//			super.onLoading(total, current, isUploading);
//			if (total == 0) {
//				total = 1;
//			}
//				int Value=(int) (((float)current/total) * 100);
//				String str=String.format("%d", Value);
//				System.out.println("下载中"+ str+ "%");
//				Message dowm_msg= new Message();
//				dowm_msg.obj = str;
//				dowm_msg.what = 5;
//				handler.sendMessage(dowm_msg);
//		}
//		
//		@Override
//		public void onSuccess(ResponseInfo<File> arg0) {
//			System.out.println("下载完成");
//			handler.sendEmptyMessage(7);
//		}
//		
//		@Override
//		public void onFailure(HttpException arg0, String arg1) {
//			System.out.println("下载失败");
//			handler.sendEmptyMessage(6);
////			ToastManager.getInstance(mContext).show("下载失败");
////			ringinfo.state=RingtoneSelectActivity.AUDIO_STATE_NOT_DOWNLOAD;
////			mHandler.sendEmptyMessage(0);
//		}
//	});
//	}
	
	
	private void dodownload(String newUrlString, String target){

		File file = new File(target);
		new AsyncHttpClient().get(newUrlString, new FileAsyncHttpResponseHandler(file) {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, File arg2) {
				System.out.println("下载完成");
				handler.sendEmptyMessage(7);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, Throwable arg2, File arg3) {
				System.out.println("下载失败");
				handler.sendEmptyMessage(6);
			}

			@Override
			public void onProgress(int current, int total) {
				// TODO Auto-generated method stub
				super.onProgress(current, total);
				if (total == 0) {
					total = 1;
				}
					int Value=(int) (((float)current/total) * 100);
					String str=String.format("%d", Value);
					System.out.println("下载中"+ str+ "%");
					Message dowm_msg= new Message();
					dowm_msg.obj = str;
					dowm_msg.what = 5;
					handler.sendMessage(dowm_msg);
			}
		});
	}
	
	//第二步升级
	
	public void StartDevUpload(){
		Log.e("DFUHelper", "StartDevUpload");
		setBluetoothAdapter();
		dfuhelper = new DFUHelper(handler, this, dfucallback);
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
						Log.e("DFUHelper", "StartconnectDFU");
						// TODO Auto-generated method stub
						mac = PreManager.instance().getBundbluetoothPillow(PillowUpgradeActivity.this);
						dfuhelper.StartconnectDFU(mBluetoothAdapter, mac);
			}
		}, 5000);

	}
	
	private DFUCallBack dfucallback = new DFUCallBack() {
		
		@Override
		public void DFUuploadcallback(int dfucode, String dfumsg) {
			DFUManager.log("PillowUpgradeActivity", dfucode+"::"+dfumsg);	
			if(dfucode<0 ){
				if(dfucode == -4){
					handler.sendEmptyMessage(1);
				}else{
//				upgradeFailedView();
				handler.sendEmptyMessage(2);
				}
			}else{
				switch (dfucode) {
				case 10://wancheng
					handler.sendEmptyMessage(10);
					break;
				case 11://kaishi
					handler.sendEmptyMessage(10);
					break;
				case 12://shuanshu
					Message msg = new Message();
					msg.what = 12;
					msg.obj = dfumsg;
					handler.sendMessage(msg);
					break;
				case 13://wancheng
					handler.sendEmptyMessage(13);
					break;
				default:
					break;
				}
			}
		}
	};
	
	/**
	 * 获得蓝牙适配器
	 */
	@SuppressLint("NewApi") 
	private void setBluetoothAdapter() {
		mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = mBluetoothManager.getAdapter();
	}

		@Override
		protected void onDestroy() {
			//注消蓝牙变化监听
			unregisterReceiver(bluetoothState);
			super.onDestroy();
		}
		
//		private void showBlutoothAskDialog(final int msgwhat) {
//			boolean isOpen = BluetoothUtil.bluetoothIsOn(PillowUpgradeActivity.this);
//			if(!isOpen){
//				showBluetoothClose();
//				
//			final MyDialog exitDialog = new MyDialog(PillowUpgradeActivity.this, 
//					0, 
//					0, 
//					R.layout.dialog_open_blutooth,
//					R.style.bottom_animation, 
//					Gravity.BOTTOM, 
//					0.96f,
//					0.0f);
//			exitDialog.setCanceledOnTouchOutside(false);
//
//			// 得到view中的控件
//			TextView dialog_title = (TextView) exitDialog.findViewById(R.id.dialog_title);
//			
//			dialog_title.setText("香橙应用想使用蓝牙功能，是否允许？");
//			
//			Button btn_allow = (Button) exitDialog.findViewById(R.id.btn_allow_open_bluetooth);
//			Button btn_refuse= (Button) exitDialog.findViewById(R.id.btn_refuse_open_bluetooth);
//
//			btn_allow.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
////					startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
//					mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
//					mBluetoothAdapter.enable();
//					
//					showSyncDialog();
//					
//					
//					handler.postDelayed(new Runnable() {
//						
//						@Override
//						public void run() {
//							if(syncDataDialog != null){
//								syncDataDialog.cancel();
//							}
//							if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
//					            Toast.makeText(PillowUpgradeActivity.this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
//					        }else{
////					        	if(msgwhat!=0){
////									handler.sendEmptyMessage(msgwhat);
////								}
//					        }
//						}
//					}, 2000);
//					
//					exitDialog.cancel();
//				}
//			});
//
//			btn_refuse.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//					if(msgwhat!=0){
//						handler.sendEmptyMessage(2);
//					}
//					exitDialog.cancel();
//				}
//			});
//
//		// 显示
//			exitDialog.show();
//			}
//		}
		
//		private void showSyncDialog() {
//
//			syncDataDialog = new MyDialog(this, 
//					0, 
//					0, 
//					R.layout.dialog_openning_bluetooth,
//					R.style.bottom_animation, 
//					Gravity.BOTTOM, 
//					1.0f,
//					0.0f);
//			syncDataDialog.setCanceledOnTouchOutside(false);
//
//		// 显示
//			syncDataDialog.show();
//		}

		
		
		private void clearTheUpdatas() {
			FileUtil.deleteFile(FileUtil.getHexpath()+"/application.hex");
			
		}
		
		

		BroadcastReceiver bluetoothState = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				if(!PillowUpgradeActivity.this.isFinishing()){
					String stateExtra = BluetoothAdapter.EXTRA_STATE;
					int state = intent.getIntExtra(stateExtra, -1);
					switch(state) {
					case BluetoothAdapter.STATE_TURNING_ON:
						break;
					case BluetoothAdapter.STATE_ON:
						
						break;
					case BluetoothAdapter.STATE_TURNING_OFF:
						break;
					case BluetoothAdapter.STATE_OFF:
						showBluetoothClose();
//						showBlutoothAskDialog(0);
						break;
					}
				}
			}
		};
		
		
		private void showBluetoothClose(){
			
			findViewById(R.id.layout_upgrade_img).setVisibility(View.GONE);
			tv_upgrade_title.setText("未打开蓝牙，无法搜索...");
			layout_upgrade_button.setVisibility(View.GONE);
			pb_load_web.setVisibility(View.GONE);
			iv_upgrade_failed.setVisibility(View.GONE);
			tvTips.setVisibility(View.GONE);
			btoothClosedImageView.setVisibility(View.VISIBLE);
			cancleBtn.setVisibility(View.VISIBLE);
		}
		
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if(keyCode == KeyEvent.KEYCODE_BACK){
				if(isFromBund){
					Intent intent = new Intent(PillowUpgradeActivity.this, BundPillowInfoActivity.class);
					startActivity(intent);
				}
				finish();
				return false;
			}else
			return super.onKeyDown(keyCode, event);
		}
		
}
