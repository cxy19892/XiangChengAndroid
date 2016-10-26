package com.yzm.sleep.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.pillow.PillowDataOpera;
import com.yzm.sleep.background.DataUtils;
//import com.yzm.sleep.background.PermanentService;
import com.yzm.sleep.background.SignInDBOperation;
import com.yzm.sleep.background.SleepInfo;
import com.yzm.sleep.model.PillowDataModel;
import com.yzm.sleep.model.SignInData;
import com.yzm.sleep.model.SmartRemindBean;
import com.yzm.sleep.model.Version;
import com.yzm.sleep.render.GetSleepResultValueClass.SleepDataHead;
import com.yzm.sleep.tools.DealFaulseUtils;
import com.yzm.sleep.utils.BluetoothDataFormatUtil;
import com.yzm.sleep.utils.CommunityProcClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.HardwareBoundParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetHardwareSensitivityCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceHardwareBoundCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadHardwareAllDayCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadHardwareDataCallBack1;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadHardwareAllDayParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadHardwareDataParamClass1;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSignInCallBack4_2_1;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.SleepConstants;
import com.yzm.sleep.utils.SleepDataProClass;
import com.yzm.sleep.utils.SleepUtils;
import com.yzm.sleep.utils.SmartNotificationUtil;
import com.yzm.sleep.utils.StringUtil;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.URLUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;

public class WelcomeActivity extends BaseActivity {
	private Handler mHandler = new Handler();
	private LocationClient mLocClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		initView();
		PreManager.instance().saveRemindNotWifi(WelcomeActivity.this, true);// 开启app时，标识是否提醒非WiFi网络为true
		checkAppUpdataTask(getVersionCode());
		MobclickAgent.updateOnlineConfig(this);
		getDevSensitive();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (PreManager.instance().getShowGuide(WelcomeActivity.this)||PreManager.instance().getIsFirstUse(WelcomeActivity.this)) {
					DataUtils.initState(WelcomeActivity.this);
					getLocation();
					startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
					AppManager.getAppManager().finishActivity();
				} else {
					checkState(0);
				}
			}
		}, 800);
		updateVersionInfo();
		//检查未上传的硬件数据，继续上传
		checkOrangeDataUploadState();
		//上传绑定信息和解绑信息
		UploadFailedBundInfo();
		UploadFailedUnBundInfo();
		doFileData();
		getPillowSigInFaildData();//对打卡失败的硬件日期进行打卡
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setAddrType("all");
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}
	

	/**
	 * 定位监听
	 */
	private BDLocationListener myListener = new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if(mLocClient != null)
				mLocClient.stop();
			if(location !=null ){
				String city = TextUtils.isEmpty(location.getCity()) ? "成都" : location.getCity();
				PreManager.instance().saveUserLocationCity(WelcomeActivity.this, city.replace("市", ""));
				PreManager.instance().saveUserLatitude(WelcomeActivity.this, location.getLatitude()+"");
				PreManager.instance().saveUserLongitude(WelcomeActivity.this, location.getLongitude()+"");
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initView() {

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		// 获得屏幕宽
		int displayWidth = metrics.widthPixels;

		MyApplication.instance().displayWidth = displayWidth;
		/** 0 - 乐商店首发 , 1 - 360首发,联合首发 */
		int[] appMarketIds = { R.drawable.leshangdian_sf,
				R.drawable.welcome_sf360 ,R.drawable.welcome_thre_platform_icon};
		ImageView iv_app_market_type = (ImageView) findViewById(R.id.iv_app_market_type);

		iv_app_market_type.setVisibility(View.GONE);
		iv_app_market_type.setBackgroundResource(appMarketIds[2]);

	}

	private void updateVersionInfo() {

		PackageInfo info = null;
		try {
			info = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0);
			System.out.println("当前应用版本信息：versionCode=" + info.versionCode
					+ ",versionName" + info.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (info != null) {
			if (info.versionCode > 7) {// 如果版本号为1.4及以上
				if (!PreferenceManager.getDefaultSharedPreferences(WelcomeActivity.this)
						.contains("version_code")) {// 如果未保存版本信息
					// 注销登录信息
					PreManager.instance().saveIsLogin(WelcomeActivity.this, false);
					PreManager.instance().saveUserId(WelcomeActivity.this, "");
					PreManager.instance().savePlatformBoundMsg(WelcomeActivity.this, "0000");
					PreManager.instance().saveAppVersionCode(WelcomeActivity.this,
							info.versionCode);
				}
			}
		}
	}

	/**
	 * 每隔0.5秒检查一次状态
	 */
	private long waittimes = 0;

	private void checkState(long time) {
		waittimes = waittimes + time;
		if (waittimes >= 8000) { // 循环出口1
			waittimes = 0;
			DataUtils.initState(WelcomeActivity.this);
			// 测试
			faultTolerant();
		} else {
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					if ("1".equals(PreManager.instance().getAnalysisState(
							WelcomeActivity.this))
							&& "1".equals(PreManager.instance().getAmendState(
							WelcomeActivity.this))
							&& "1".equals(PreManager.instance().getAccState(
							WelcomeActivity.this))
							&& "1".equals(PreManager.instance().getSetupState(
							WelcomeActivity.this))) {
						waittimes = 0; // 循环出口2
						faultTolerant();
					} else {
						checkState(500);
					}
				}

			}, 500);
		}

	}

	/**
	 * 容错机制调用
	 */
	private void faultTolerant() {
		final long restartTime = System.currentTimeMillis();
		final long downTime = Long.valueOf(PreManager.instance().getDownTime(
				WelcomeActivity.this));
		final long timeSpace = restartTime - downTime;
		new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... params) {
				try {
					if (timeSpace < 0 || timeSpace > SleepInfo.FAULT_JUDGETIME) {
						PreManager.instance().saveMainState(WelcomeActivity.this, "2");
						DealFaulseUtils.getInstance().dealBreak(WelcomeActivity.this);
						PreManager.instance().saveMainState(WelcomeActivity.this, "1");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Object result) {
				getLocation();
				startActivity(new Intent(WelcomeActivity.this, HomeActivity.class)
				.putExtra("jpushmsgtomsglist", getIntent().getBooleanExtra("jpushmsgtomsglist", false))
				.putExtra("chattoMsgList", getIntent().getBooleanExtra("chattoMsgList", false)));
				AppManager.getAppManager().finishActivity();
			}

		}.execute(3);
	}

	/**
	 * 获取应用版本号
	 * 
	 * @return
	 */
	private String getVersionCode() {
		PackageInfo info = null;
		try {
			info = WelcomeActivity.this.getPackageManager().getPackageInfo(
					WelcomeActivity.this.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (info != null) {
			return info.versionName;
		}
		return null;

	}

	/**
	 * 检查程序是否有新版本
	 */
	private void checkAppUpdataTask(final String currentVersion) {
		String crrenVersion = getVersionCode();
		getNewstVersion(crrenVersion);
	}

	private void getNewstVersion(final String crrenVersion) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.setConnectTimeout(5000);
		String url = URLUtil.BASEURL + "/check_version_update.php?system_type=2&current_version="
				+ crrenVersion;
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String response = new String(arg2);
				dealData(response, crrenVersion);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				if (arg2 != null) {
					String response = new String(arg2);
					dealData(response, crrenVersion);
				}
			}
		});
	}

	private void dealData(String response, String crrenVersion) {
		try {
			Gson gson = new Gson();
			Version version = gson.fromJson(response, Version.class);
			if ("4000:1".equals(version.response)) {// 平台类型错误

			} else if ("4000:2".equals(version.response)) {// 当前版本号为空

			} else if ("4000:3".equals(version.response)) {// 已经是最高版本了
				PreManager.instance().saveIsUpdateVersion(getApplicationContext(),
						false);
			} else if ("4000:4".equals(version.response)
					&& version.latest_version != null) {// 不是最新版本，返回最新版本号
				String newVersion = version.latest_version;
				if(StringUtil.isNeedUpdate(crrenVersion, newVersion)){
					PreManager.instance().saveIsUpdateVersion(getApplicationContext(),
							true);
				}else{
					PreManager.instance().saveIsUpdateVersion(getApplicationContext(),
							false);
				}
				
			} else {

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 获取灵敏度
	 */
	private void getDevSensitive(){
		if(checkNetWork(WelcomeActivity.this)){
			new CommunityProcClass(WelcomeActivity.this).getHardwareSensitivity(PreManager.instance().getUserId(WelcomeActivity.this), new InterfaceGetHardwareSensitivityCallBack() {

				@Override
				public void onSuccess(int icode, String selectLMD, String allLMD) {
					if(!WelcomeActivity.this.isFinishing()){
						if(!TextUtils.isEmpty(allLMD)){
							PreManager.instance().setAllSensitives(WelcomeActivity.this, allLMD);
						}
						if(!TextUtils.isEmpty(selectLMD)){
							if("-1".equals(selectLMD)){
								selectLMD = "0";
							}
							PreManager.instance().setBluetoothDevSensitive(WelcomeActivity.this, selectLMD);
						}else{
							PreManager.instance().setBluetoothDevSensitive(WelcomeActivity.this, "0");
						}
					}
				}
				
				@Override
				public void onError(int icode, String strErrMsg) {
					
					
				}
			});
		}
	}
	
	/**
	 * 检查硬件数据未上传的数据重新上传
	 */
	@SuppressLint("SimpleDateFormat") 
	private void checkOrangeDataUploadState(){
		List<PillowDataModel> list = PillowDataOpera.queryAllDataFromSQL(getApplicationContext());
		for (PillowDataModel pillowDataModel : list) {
			if(pillowDataModel.getDatIsUpload() != null && pillowDataModel.getDatIsUpload().equals("0")){
				String filePath = PillowDataOpera.getPillowDataPath(pillowDataModel.getDatFileName());
				File file = new File(filePath);
				if(!TextUtils.isEmpty(filePath) && file.exists()){
					String date = "";
					try {
						date = String.valueOf(TimeFormatUtil.formatTime(new SimpleDateFormat("yyyy-MM-dd").parse(pillowDataModel.getDate()).getTime(), "yyyyMMddHHmmss"));
					} catch (Exception e) {
						date = String.valueOf(TimeFormatUtil.formatTime(System.currentTimeMillis(), "yyyyMMddHHmmss"));
					}
					uploadDatFile(filePath, date, pillowDataModel.getDate());
				}
			}
			
			if(pillowDataModel.getIsUpload() != null && pillowDataModel.getIsUpload().equals("0")){
				String filePath = PillowDataOpera.getPillowDataPath(pillowDataModel.getFileName());
				File file = new File(filePath);
				if(!TextUtils.isEmpty(filePath) && file.exists()){
					byte[] value = PillowDataOpera.readDataToSDcard(pillowDataModel.getFileName());
					String date = "";
					try {
						date = String.valueOf(new SimpleDateFormat("yyyy-MM-dd").parse(pillowDataModel.getDate()).getTime() / 1000);
					} catch (Exception e) {
						date = String.valueOf(System.currentTimeMillis() / 1000);
					}
					uploadDataToService(value, date, filePath, pillowDataModel.getDate());
				}
			}
		}
	}
	
	/**上传dat文件@param datPath*/
	private void uploadDatFile(String datPath, String date, final String sqlDate){
		UploadHardwareAllDayParamClass mParam=new UploadHardwareAllDayParamClass();
		mParam.path=datPath;
		mParam.my_int_id=PreManager.instance().getUserId(this);
		mParam.date_of_data= date;
		new SleepDataProClass(this).UploadHardwareAllDay(mParam, new InterfaceUploadHardwareAllDayCallBack() {
			@Override
			public void onSuccess(int iCode, String strSuccMsg) {
				PillowDataModel model = new PillowDataModel();
				model.setDatIsUpload("1");
				model.setDate(sqlDate);
				PillowDataOpera.updateDataToSQL(WelcomeActivity.this, model);
			}
			@Override
			public void onError(int icode, String strErrMsg) {
			}
		});
	}
	
	/**
	 * 上传数据到服务器备份
	 * 
	 * @param modelsValues
	 * @param date
	 */
	private void uploadDataToService(byte[] modelsValues, String date, String modelPath, final String sqlDate) {
		SleepDataHead datahead = BluetoothDataFormatUtil.format3(modelsValues,
				10);
		UploadHardwareDataParamClass1 param = new UploadHardwareDataParamClass1();
		param.awakecount = String.valueOf(datahead.AwakeCount);
		param.awakenogetupcount = String.valueOf(datahead.AwakeNoGetUpCount);
		param.awaketimesleep = String.valueOf(datahead.AwakeTime_Sleep);
		param.date = date;
		param.deepsleep = String.valueOf(datahead.Deep_Sleep);
		param.file = modelPath;
		param.getuptime = String.valueOf(datahead.GetUpTime);
		param.gotobedtime = String.valueOf(datahead.GoToBedTime);
		param.insleeptime = String.valueOf(datahead.InSleepTime);
		param.listlength = "0";
		param.my_int_id = PreManager.instance().getUserId(this);
		param.onbed = String.valueOf(datahead.OnBed);
		param.outsleeptime = String.valueOf(datahead.OutSleepTime);
		param.shallowsleep = String.valueOf(datahead.Shallow_Sleep);
		param.sleepbak1 = String.valueOf(datahead.SleepBak1);
		param.Sleepbak2 = String.valueOf(datahead.SleepBak2);
		param.tosleep = String.valueOf(datahead.ToSleep);
		param.totalsleeptime = String.valueOf(datahead.TotalSleepTime);
		param.user_location_x = PreManager.instance().getLocation_x(this);
		param.user_location_y = PreManager.instance().getLocation_y(this);
		param.xstart = String.valueOf(datahead.XStart);
		param.xstop = String.valueOf(datahead.XStop);
		param.ymax = String.valueOf(datahead.YMax);
		new CommunityProcClass(this).UploadHardwareSleepData(param,
				new InterfaceUploadHardwareDataCallBack1() {

					@Override
					public void onError(int icode, String strErrMsg) {
					}

					@Override
					public void onSuccess(int icode, String strSuccMsg) {
						PillowDataModel model = new PillowDataModel();
						model.setIsUpload("1");
						model.setDate(sqlDate);
						PillowDataOpera.updateDataToSQL(WelcomeActivity.this, model);
					}
				});
	}
	/**
	 * 重新上传绑定信息（用于之前上传的时候上传失败）
	 */
	private void UploadFailedBundInfo(){
		String bundInfo = PreManager.instance().getBluetoothBundInfo(WelcomeActivity.this);
		if(!TextUtils.isEmpty(bundInfo)){
			String[] infoarr = bundInfo.split(";");
			if(infoarr.length==4){
				uploadBundInfo(infoarr[0], infoarr[1], infoarr[2], infoarr[3]);
			}
		}
	}
	
	
	/**
	 * 上传绑定硬件信息
	 */
	private void uploadBundInfo(String mac, String btime, String lmd, String userId){
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
				PreManager.instance().setBluetoothBundInfo(WelcomeActivity.this, "");
			}
			@Override
			public void onError(int icode, String strErrMsg) {
			}
		});
	}
	
	/**
	 * 重新上传解绑信息（用于之前解绑上传失败）
	 */
	private void UploadFailedUnBundInfo(){
		String unBundInfo = PreManager.instance().getBluetoothBundInfo(WelcomeActivity.this);
		if(!TextUtils.isEmpty(unBundInfo)){
			if(!TextUtils.isEmpty(unBundInfo)){
				String[] infoarr = unBundInfo.split(";");
				if(infoarr.length==4){
					uploadUnBundInfo(infoarr[0], infoarr[1], infoarr[2], infoarr[3]);
				}
			}
		}
	}
	
	/**
	 * 上传解除绑定硬件信息
	 */
	private void uploadUnBundInfo(String mac, String unbtime, String lmd, String userId){
		HardwareBoundParamClass mHardwareBoundParamClass = new HardwareBoundParamClass();
		mHardwareBoundParamClass.bdtime = "";
		mHardwareBoundParamClass.jbtime = unbtime;
		mHardwareBoundParamClass.jystatus = "2";
		mHardwareBoundParamClass.my_int_id = userId;
		mHardwareBoundParamClass.yjlmd = lmd;
		mHardwareBoundParamClass.macadd= mac;
		new CommunityProcClass(this).hardwareBound(mHardwareBoundParamClass, new InterfaceHardwareBoundCallBack() {
			
			@Override
			public void onSuccess(int icode, String strSuccMsg) {
				PreManager.instance().setBluetoothUnBundInfo(WelcomeActivity.this, "");
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
			}
		});
	}
	
	/**
	 * 获取文件下载路径
	 */
	private void doFileData() {
		String url = URLUtil.BASEURL +"/api.php?mod=yjtiaocan";
		String uid = PreManager.instance().getUserId(WelcomeActivity.this);
		final String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/data";
		File file = new File(filePath);
		if(!file.exists())
			file.mkdirs();
		if(!TextUtils.isEmpty(uid)){
			StringBuffer sb = new StringBuffer();
			sb.append(url);
			sb.append("&my_int_id=" + uid);
			sb.append("&xctoken=" + getRequestToken("yjtiaocan"));
			String newString = sb.toString();
			new AsyncHttpClient().get(newString, new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					// TODO Auto-generated method stub
					try {
						JSONObject jsonObj = new JSONObject(new String(arg2));
						if(jsonObj.getString("response").equals("4704")){
							String url = jsonObj.getString("tiaocanfile");
							new AsyncHttpClient().get(url, new FileAsyncHttpResponseHandler(new File(filePath, "profile.pa")) {
								
								@Override
								public void onSuccess(int arg0, Header[] arg1, File arg2) {
									// TODO Auto-generated method stub
								}
								
								@Override
								public void onFailure(int arg0, Header[] arg1, Throwable arg2, File arg3) {
									// TODO Auto-generated method stub
								}
							});
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}
				
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					// TODO Auto-generated method stub
					
				}
			});
			
		}
		
	}
	
	/**
	 * 获取签名参数值
	 * @param interfaceName
	 * @return
	 */
	private String getRequestToken(String interfaceName){
		String encryption = Util.encryption(interfaceName + //接口名
				SleepUtils.getFormatedDateTime("yyyyMMdd", System.currentTimeMillis()) + //日期
				SleepConstants.NET_REQUEST_KEY);//加密密钥
		return encryption;
	}

	@Override
	protected void onPause() {
		super.onPause();
		try {
			if(mLocClient != null)
				mLocClient.stop();
			mLocClient.unRegisterLocationListener(myListener);
		} catch (Exception e) {
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void getPillowSigInFaildData(){
		List<SignInData> list = SignInDBOperation.initDB(WelcomeActivity.this).getUnUploadData();
		if(list.size()>0){
			for(SignInData mSignInData : list){
				signInPillowData(mSignInData);
			}
		}
	}
	
	
	private void signInPillowData(final SignInData signIn){
		if(signIn != null){
			new XiangchengMallProcClass(WelcomeActivity.this).signIn4_2_1(PreManager.instance().getUserId(WelcomeActivity.this), "2", signIn, new InterfaceSignInCallBack4_2_1() {
				
				@Override
				public void onSuccess(String icode, JSONObject response) {
					try {
						signIn.setUpload(0);
						SignInDBOperation.initDB(WelcomeActivity.this).updateSignInData(signIn, "1");//0软件1硬件
					} catch (Exception e) {
					}
				}
				
				@Override
				public void onError(String icode, String strErrMsg) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}
		
}
