package com.yzm.sleep.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.background.DataUtils;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.background.MytabOperate;
import com.yzm.sleep.background.SleepInfo;
import com.yzm.sleep.background.SleepResult;
import com.yzm.sleep.model.UpdateDataObject;
import com.yzm.sleep.utils.HttpUtils;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.URLUtil;

public abstract class BaseActivity extends FragmentActivity implements OnClickListener{
	
	private InputMethodManager imm;
	public boolean isShowKeyboard = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 

		
		if(Constant.screenWidht == 0 || Constant.screenHeight == 0){
			WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics dm = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(dm);
			Constant.screenWidht = dm.widthPixels;
			Constant.screenHeight = dm.heightPixels;
		}	
		
		//禁止默认的页面统计方式，这样将不会再自动统计Activity。
		MobclickAgent.openActivityDurationTrack(false);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AppManager.getAppManager().finishActivity();
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	public void hideSoftInput(IBinder windowToken,int flags){
		imm.hideSoftInputFromWindow(windowToken, flags);
	}
	
	public void toggleSoftInput(Activity activity){
		 imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	} 

	public int getScreenWidth() {	
		return Constant.screenWidht;
	}

	public int getScreenHeight() {
		return Constant.screenHeight;
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (isShouldHideInput(v, ev)) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
			return super.dispatchTouchEvent(ev);
		}
		// 必不可少，否则所有的组件都不会有TouchEvent了
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return onTouchEvent(ev);
	}
	
	private boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null ) {
			if (v instanceof EditText) {
				int[] leftTop = { 0, 0 };
				//获取输入框当前的location位置
				v.getLocationInWindow(leftTop);
				int left = leftTop[0];
				int top = leftTop[1];
				int bottom = top + v.getHeight();
				int right = left + v.getWidth();
				if (event.getX() > left && event.getX() < right
						&& event.getY() > top && event.getY() < bottom) {
					// 点击的是输入框区域，保留点击EditText的事件
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}

	protected void setImmerseLayout(View view) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
                /*window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*/
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

			/*int statusBarHeight = getStatusBarHeight(this.getBaseContext());
			view.setPadding(0, statusBarHeight, 0, 0);*/
		}
	}


	@Override //适配系统字体更改导致的错误
    public Resources getResources() {
        Resources res = super.getResources();  
        Configuration config=new Configuration();  
        config.setToDefaults();  
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }
	
	/**
	 * 显示键盘
	 * @param inputView 接收输入的view
	 */
	public void showKeyboard(EditText inputView){
		inputView.setFocusable(true);
		inputView.setFocusableInTouchMode(true);
		inputView.requestFocus();
		if(null==imm){
			imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		}
		imm.showSoftInput(inputView, InputMethodManager.SHOW_FORCED);
	}
	
	/**
	 * 隐藏键盘
	 * @param inputView
	 */
	public boolean hideKeyboardB(EditText inputView){
		if(null==imm)
			return true;
		try{
			imm.hideSoftInputFromWindow(inputView.getWindowToken(), 0);
		}catch(Exception e){
			
		}
		return true;
	}
	
	/**
	 * 检查网络是否连接可用
	 * 
	 * @param context
	 * @return
	 */
	public boolean checkNetWork(Context context) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	
	private LocationClient mLocationClient = null;
	private BDLocationListener myListener = new MyLocationListener();
	/**
	 * 注册百度地图api
	 */
	protected void initBaiduMap() {
		mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
		mLocationClient.registerLocationListener( myListener );    //注册监听函数
		
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(1000);//设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);//返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
	}
	
	/**
	 * 开启获取百度地图定位，如果是首次调用需要先调用initBaiduMap注册百度地图api
	 */
	protected void startBaiduMap() {
		mLocationClient.start();
		mLocationClient.requestLocation();
	}
	
	/**
	 * 获取坐标
	 */
	protected void getLocation() {
		initBaiduMap();
		startBaiduMap();
	}
	
	/**
	 * 百度地图回调监听器
	 * @author Administrator
	 *
	 */
	private class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			String latitude = location.getLatitude()+"";
			String longitude = location.getLongitude()+"";
			boolean success = false;
			if("4.9E-324".equals(longitude) || "4.9E-324".endsWith(latitude)) {
				longitude = SleepInfo.LOCATION_X;
				latitude = SleepInfo.LOCATION_Y;
			} else if(location.getLatitude() == 0 && location.getLongitude() == 0) {
				longitude = SleepInfo.LOCATION_X;
				latitude = SleepInfo.LOCATION_Y;
			} else {
				success = true;
			}
			SharedPreferences sp = getSharedPreferences(SleepInfo.FILENAME_USER, MODE_APPEND);
			Editor edt = sp.edit();
			edt.putString(SleepInfo.KEY_LOCATION_X, longitude);
			edt.putString(SleepInfo.KEY_LOCATION_Y, latitude);
			edt.commit();
			
			if(success) {
				mLocationClient.stop();
				mLocationClient.unRegisterLocationListener(this);	
			}
		}
	}	
	
	
	@Override
	protected void onDestroy() {
		if(mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.stop();
			if(myListener != null) {
				mLocationClient.unRegisterLocationListener(myListener);		
			}
		}
		super.onDestroy();
	}
	
	//上传机制
	/**
	 * 检查是否已经上传当天数据，如果没有上传则上传
	 */
	private void checkUpload(String date) {
		if(DataUtils.canUpload(getApplicationContext())) {
			uploadLocation(date);
		}
	}
	
	/**
	 * 上传当天的地理位置
	 */
	private void uploadLocation(final String date){
		new Thread(){

			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				final MyDatabaseHelper helper = MyDatabaseHelper.getInstance(getApplicationContext());
				MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPTIME);
				List<SleepResult> srList;
				try {
					if("4".equals(DataUtils.getRecordState(getApplicationContext(), date))) {
						srList = (List<SleepResult>) operate.query(new String[]{"date","isupload"}, "date = ? and ispunch = ?", new String[]{date, "1"},"date");
						String isupload = srList.get(0).getUpload();
						if("0".equals(isupload)) {
							UpdateDataObject dataObject;
							dataObject = getUpdateData(date);
							ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
							BasicNameValuePair bp1 = new BasicNameValuePair("my_int_id",dataObject.getMy_int_id());
							BasicNameValuePair bp2 = new BasicNameValuePair("date_of_data",dataObject.getDate_of_data());
							BasicNameValuePair bp3 = new BasicNameValuePair("sleep_point",dataObject.getSleep_point());
							BasicNameValuePair bp4 = new BasicNameValuePair("wakeup_point",dataObject.getWakeup_point());
							BasicNameValuePair bp5 = new BasicNameValuePair("user_location_x",dataObject.getUser_location_x());
							BasicNameValuePair bp6 = new BasicNameValuePair("user_location_y",dataObject.getUser_location_y());
							BasicNameValuePair bp7 = new BasicNameValuePair("sleep_duration",dataObject.getSleep_duration());
							BasicNameValuePair bp8 = new BasicNameValuePair("my_int_occupation",dataObject.getMy_int_occupation());
							data.add(bp1);
							data.add(bp2);
							data.add(bp3);
							data.add(bp4);
							data.add(bp5);
							data.add(bp6);
							data.add(bp7);
							data.add(bp8);
							String result = HttpUtils.doPost(URLUtil.UPLOAD_SLEEP_DATA_URL, data);
							JSONObject jo = new JSONObject(result);
							if(jo.has("response")) {
								String code = jo.getString("response");
								if(code.contains("4036") || code.contains("4037")) {
									//数据上传成功
									ContentValues cv = new ContentValues();
									cv.put("isupload", "1");
									operate.update(cv, "date = ?", new String[]{date});
								}else{
									checkUpload(date);
								}
							}
						}
						operate.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}.start();
	}
	
	
	/**
	 * 获取每日上传数据
	 * @return
	 * @throws Exception 
	 */
	private UpdateDataObject getUpdateData(String date) throws Exception {
		UpdateDataObject dataObject = new UpdateDataObject();
		SharedPreferences sp = getApplicationContext().getSharedPreferences(SleepInfo.FILENAME_USER, Context.MODE_APPEND);
		String userid = PreManager.instance().getUserId(getApplicationContext());
		String locationx = sp.getString(SleepInfo.KEY_LOCATION_X, "0");
		String locationy = sp.getString(SleepInfo.KEY_LOCATION_Y, "0");
		String occupation = PreManager.instance().getUserProfession(getApplicationContext()) + "";
		dataObject.setMy_int_id(userid);
		dataObject.setDate_of_data(date.replaceAll("-", ""));
		dataObject.setUser_location_x(locationx);
		dataObject.setUser_location_y(locationy);
		dataObject.setMy_int_occupation(occupation);
		
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(getApplicationContext());
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPTIME);
		List<SleepResult> srList =  (List<SleepResult>) operate.query(new String[]{"date","sleeptime","uptime","sleeplength","healthlength","starttime","endtime","sleepacce","upacce","diagramdata","ischange","isupload"}, "date = ?", new String[]{date},"date");
		for(SleepResult result : srList) {
			String sleep_point = result.getSleeptime() == null || "".equals(result.getSleeptime()) ? "0" : DataUtils.getSleepOrUptime(result.getSleeptime(),result.getStarttime(),result.getEndtime(),result.getDate());
			String wakup_point = result.getUptime() == null || "".equals(result.getSleeptime()) ? "0" : DataUtils.getSleepOrUptime(result.getUptime(),result.getStarttime(),result.getEndtime(),result.getDate());;
			dataObject.setSleep_point(sleep_point);
			dataObject.setWakeup_point(wakup_point);
			String sleepLength = (Long.valueOf(result.getSleepLength() == null || "".equals(result.getSleeptime()) ? "0" : result.getSleepLength())/60.0f) + "";
			dataObject.setSleep_duration(sleepLength.substring(0,sleepLength.indexOf(".")+2));
		}
		return dataObject;
	}	

	
    public void toastMsg(String msg){
    	Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}
