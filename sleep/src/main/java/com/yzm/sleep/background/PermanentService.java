package com.yzm.sleep.background;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.yzm.sleep.R;
import com.yzm.sleep.model.UpdateDataObject;
import com.yzm.sleep.tools.DealFaulseUtils;
import com.yzm.sleep.tools.InsertTodayUtils;
import com.yzm.sleep.utils.HttpUtils;
import com.yzm.sleep.utils.PreManager;

@SuppressLint("SimpleDateFormat") public class PermanentService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		// 记录第三方进程拉起永久进程时间
		
		//闹钟服务拉起
		startAlarmService();
		
		try {
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/database/oncreate.txt";
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			RandomAccessFile rank = new RandomAccessFile(path, "rw");
			rank.seek(rank.length());
			StringBuffer sb = new StringBuffer();
			long currenttime = System.currentTimeMillis();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			sb.append(sdf.format(new Date(currenttime))).append("\r\n");
			rank.writeBytes(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (MyDatabaseHelper.getVersionCode(getApplicationContext()) >= MyTabList.DATABASEVERSION) {
			permanentMethod();
		}
	}

	/**
	 * 启动闹钟进程
	 */
	private void startAlarmService() {
		Intent newIntent = new Intent(this,AlarmService.class);
    	startService(newIntent);
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.stop();
			mLocationClient.unRegisterLocationListener(myListener);
		}

		if (run != null) {
			mHandler.removeCallbacks(run);
		}
		if (permanetRunnable != null) {
			mHandler.removeCallbacks(permanetRunnable);
		}
		if (mSensorManager != null && listener != null) {
			mSensorManager.unregisterListener(listener);
		}
	}

	private Runnable permanetRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			permanentMethod();
		}
	};

	/**
	 * 每隔5分钟执行一次
	 */
	private void permanentMethod() {
		System.out.println("加速度重装测试：持久程序启动");
		try {
			long currentTime = System.currentTimeMillis();
			String date = DataUtils.getRecordDate(new Date());
			String beforeDate = DataUtils.getBeforeDate(date);
			InsertTodayUtils.getInstance()
					.insert(getApplicationContext(), date);
			if ("1".equals(PreManager.instance().getAnalysisState(
					getApplicationContext()))
					&& "1".equals(PreManager.instance().getAmendState(
							getApplicationContext()))
					&& "1".equals(PreManager.instance().getAccState(
							getApplicationContext()))
					&& "1".equals(PreManager.instance().getSetupState(
							getApplicationContext()))) {
				System.out.println("加速度循环开始执行容错");
				dealFault(date);
			}

			long currentThreadTime = System.currentTimeMillis();
			PreManager.instance().saveThreadDownTime(getApplicationContext(),
					currentThreadTime + "");
			// t2到t1之间记录循环tdown
			if (currentTime < DataUtils.getT1(date)
					&& currentTime > DataUtils.getT2(beforeDate)) {
				PreManager.instance().saveDownTime(getApplicationContext(),
						currentTime + "");
			}
			System.out.println("重装测试：analyzestate:"
					+ PreManager.instance().getAnalysisState(
							getApplicationContext())
					+ "amendstate:"
					+ PreManager.instance().getAmendState(
							getApplicationContext())
					+ "mainstate:"
					+ PreManager.instance().getMainState(
							getApplicationContext())
					+ "setupstate:"
					+ PreManager.instance().getSetupState(
							getApplicationContext()));
			if ("1".equals(PreManager.instance().getAnalysisState(
					getApplicationContext()))
					&& "1".equals(PreManager.instance().getAmendState(
							getApplicationContext()))
					&& "1".equals(PreManager.instance().getMainState(
							getApplicationContext()))
					&& "1".equals(PreManager.instance().getSetupState(
							getApplicationContext()))) {
				PreManager.instance()
						.saveAccState(getApplicationContext(), "2");
				// 插入当天数据
				if (currentTime >= DataUtils.getT2(beforeDate)) {
					InsertTodayUtils.getInstance().insert(
							getApplicationContext(), date);
				}
				// 根据时间修改状态
				if (currentTime >= DataUtils.getT1(date)
						&& currentTime <= DataUtils.getT2(date)) {
					if ("1".equals(DataUtils.getRecordState(
							getApplicationContext(), date))) {
						DataUtils.saveRecordState(getApplicationContext(),
								date, "2");
					}
				}
				// 统计上一周期数据
				if (currentTime >= DataUtils.getT2(beforeDate)) {
					if ("2".equals(DataUtils.getRecordState(
							getApplicationContext(), beforeDate))
							|| "3".equals(DataUtils.getRecordState(
									getApplicationContext(), beforeDate))) {
						DataUtils.saveRecordState(getApplicationContext(),
								beforeDate, "3");
						new JudgFuction().judge(getApplicationContext(),
								beforeDate);
						DataUtils.saveRecordState(getApplicationContext(),
								beforeDate, "4");
					}
				}
				// 控制加速度
				System.out.println("重装测试：当前时间" + currentTime + " 周期t1:"
						+ DataUtils.getT1(date) + " 周期t2:"
						+ DataUtils.getT2(date));
				if (currentTime >= DataUtils.getT1(date)
						&& currentTime <= DataUtils.getT2(date)) {
					System.out.println("重装测试：测试日期："
							+ date
							+ "  当前record——state"
							+ DataUtils.getRecordState(getApplicationContext(),
									date));
					if ("2".equals(DataUtils.getRecordState(
							getApplicationContext(), date))) {
						startAcc();
					}
				} else {
					stopAcc();
				}
			}
			PreManager.instance().saveAccState(getApplicationContext(), "1");
			startTicker();
			upload();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mHandler.postDelayed(permanetRunnable, SleepInfo.CIRCULATE_TIME);
	}

	/**
	 * @throws Exception
	 * 
	 */
	private void upload() throws Exception {
		if (PreManager.instance().getIsLogin(getApplicationContext())) {
			long currenttime = System.currentTimeMillis();
			long lastUploadtime = PreManager.instance().getUploadTime(getApplicationContext());
			if (Math.abs(currenttime - lastUploadtime) >= 30 * 60 * 1000) {
				String currentDate = DataUtils.getRecordDate(new Date(
						currenttime));
				List<SleepResult> list = DataUtils.getResult(
						getApplicationContext(), currentDate);
				for (SleepResult result : list) {
					Message msg = new Message();
					msg.what = 1;
					msg.obj = result.getDate();
					mHandler.sendMessage(msg);
				}
				PreManager.instance().saveUploadTime(getApplicationContext(), currenttime);
			}
		}
	}

	/**
	 * 容错机制
	 * 
	 * @param date
	 */
	private void dealFault(final String date) {
		final long restartTime = System.currentTimeMillis();
		final long downTime = Long.valueOf(PreManager.instance().getDownTime(
				getApplicationContext()));
		final long timeSpace = restartTime - downTime;
		try {
			if (timeSpace < 0 || timeSpace > SleepInfo.FAULT_JUDGETIME) {
				PreManager.instance().saveAmendState(getApplicationContext(),
						"2");
				DealFaulseUtils.getInstance()
						.dealBreak(getApplicationContext());
//				InsertEmptyDataUtils.getInstance().insert(date,
//						getApplicationContext());
				PreManager.instance().saveAmendState(getApplicationContext(),
						"1");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	LocationClient mLocationClient = null;
	BDLocationListener myListener = new MyLocationListener();

	/**
	 * 获取定位坐标
	 */
	private void getLocation() {
		System.out.println("***开始获取坐标信息");
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(1000);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);

		mLocationClient.start();
		mLocationClient.requestLocation();
	}

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			String latitude = location.getLatitude()+"";
			String longitude = location.getLongitude()+"";
			//测试代码
			System.out.println("经度："+longitude+" 维度："+latitude);
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
			SharedPreferences sp = getSharedPreferences(SleepInfo.FILENAME_USER, getApplicationContext().MODE_APPEND);
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

	private long lastTime;

	/**
	 * 加速度监听器
	 * 
	 * @author Administrator
	 * 
	 */
	private class Listener implements SensorEventListener {

		private float[] accValue = new float[3];

		@Override
		public void onSensorChanged(SensorEvent event) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				long currenttime = System.currentTimeMillis();
				try {
					String date = DataUtils.getRecordDate(new Date());
					long t1 = DataUtils.getT1(date);
					long t2 = DataUtils.getT2(date);
					if (!"2".equals(DataUtils.getRecordState(
							getApplicationContext(), date))) {
						mSensorManager.unregisterListener(listener);
						return;
					}
					if(currenttime >= t1
							&& currenttime <= t2) {
						System.out.println("加速度取值。。。");
						if (Math.abs(currenttime - lastTime) >= SleepInfo.ACCINSERT_TIME) {
							try {
								long tDown = Long.valueOf(PreManager.instance()
										.getDownTime(getApplicationContext()));
								if ((currenttime - tDown) > SleepInfo.FAULT_JUDGETIME) {
									if (t1 >= tDown) {
										DataUtils.newData(t1, currenttime,
												getApplicationContext(), date);
									} else {
										DataUtils.newData(tDown, currenttime,
												getApplicationContext(), date);
									}
								}
								System.out
										.println("加速度开始记录，但是还未经过过滤条件： recordstate："
												+ DataUtils.getRecordState(
														getApplicationContext(),
														date) + " currenttime:"
												+ currenttime + " t1:"
												+ DataUtils.getT1(date) + " t2:"
												+ DataUtils.getT2(date));
								if (mSensorManager != null
										&& listener != null) {
									accValue = event.values;
									System.out.println("加速度取值:" + accValue[0]
											+ "  y:" + accValue[1] + "  z:"
											+ accValue[2]);
									insert(accValue);
									lastTime = currenttime;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					lastInsertTime = System.currentTimeMillis();
					// threadRunTime = System.currentTimeMillis();
					System.out.println("关闭加速度。。。");
					mSensorManager.unregisterListener(listener);
				}
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}

	}

	private ContentValues AccCv;

	private void insert(float data[]) throws ParseException {
		Date date = new Date();
		if (date.getTime() < DataUtils.getStopTime()
				&& date.getTime() > DataUtils.getStartTime()) {
			AccCv = new ContentValues();
			AccCv.put("time", date.getTime());
			AccCv.put("date", DataUtils.getRecordDate(date));
			AccCv.put("x", dataOption(data[0]));
			AccCv.put("y", dataOption(data[1]));
			AccCv.put("z", dataOption(data[2]));

			AccOperater.insert(AccCv);

			System.out.println("插入加速度:" + dataOption(data[0]) + "  y:"
					+ dataOption(data[1]) + "  z:" + dataOption(data[2]));
			PreManager.instance().saveDownTime(getApplicationContext(),
					date.getTime() + "");
		}
		AccOperater.close();
	}

	private String result;

	private String dataOption(float data) {
		String test = data + "";
		result = test.substring(0, test.indexOf(".") + 2);
		return result;
	}

	private SensorManager mSensorManager;
	private Listener listener;
	private final int DELAY = SensorManager.SENSOR_DELAY_NORMAL;
	// private final int DELAY = 5000000;
	private IntentFilter filter;
	private MyDatabaseHelper AccHelper;
	private MytabOperate AccOperater;
	private boolean isThreadRun = false;
	private long lastInsertTime = 0;
	private long threadRunTime = 0;

	/**
	 * 开启加速度
	 */
	private void startAcc() {
		System.out.println("重装测试：开启加速度");
		if (AccHelper == null) {
			AccHelper = MyDatabaseHelper.getInstance(this
					.getApplicationContext());
		}
		AccOperater = new MytabOperate(AccHelper.getWritableDatabase(),
				MyTabList.SLEEPDATA);

		long currentTime = System.currentTimeMillis();
		if ((currentTime - threadRunTime) > 20000
				|| (currentTime - lastInsertTime) > 20000) {
			listenAcc();
		}
		PreManager.instance().saveAccStartTime(getApplicationContext(),
				System.currentTimeMillis() + "");
	}

	private Thread run = new Thread() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (mSensorManager == null) {
				mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
			}
			if (listener == null) {
				listener = new Listener();
			}
			mSensorManager.registerListener(listener,
					mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					DELAY);

			threadRunTime = System.currentTimeMillis();
			mHandler.postDelayed(this, 5000);
		}

	};

	private void listenAcc() {
		threadRunTime = System.currentTimeMillis();

		if (mSensorManager == null) {
			mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		}
		if (listener == null) {
			listener = new Listener();
		}
		mSensorManager.unregisterListener(listener);
		mSensorManager.registerListener(listener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				DELAY);

		mHandler.removeCallbacks(run);
		mHandler.postDelayed(run, 5000);
	}

	/**
	 * 停止加速度
	 */
	private void stopAcc() {
		System.out.println("重装测试：结束加速度");
		if (run != null) {
			mHandler.removeCallbacks(run);
		}
		if (mSensorManager != null && listener != null) {
			mSensorManager.unregisterListener(listener);
		}
	}

	private Handler mHandler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				final String date = (String) msg.obj;
				new AsyncTask<Object, Object, Object>() {

					@Override
					protected Object doInBackground(Object... params) {
						// TODO Auto-generated method stub
						final MyDatabaseHelper helper = MyDatabaseHelper
								.getInstance(getApplicationContext());
						MytabOperate operate = new MytabOperate(
								helper.getWritableDatabase(),
								MyTabList.SLEEPTIME);
						try {
							UpdateDataObject dataObject;
							dataObject = getUpdateData(date);
							ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
							BasicNameValuePair bp1 = new BasicNameValuePair(
									"my_int_id", dataObject.getMy_int_id());
							BasicNameValuePair bp2 = new BasicNameValuePair(
									"date_of_data",
									dataObject.getDate_of_data());
							BasicNameValuePair bp3 = new BasicNameValuePair(
									"sleep_point", dataObject.getSleep_point());
							BasicNameValuePair bp4 = new BasicNameValuePair(
									"wakeup_point",
									dataObject.getWakeup_point());
							BasicNameValuePair bp5 = new BasicNameValuePair(
									"user_location_x",
									dataObject.getUser_location_x());
							BasicNameValuePair bp6 = new BasicNameValuePair(
									"user_location_y",
									dataObject.getUser_location_y());
							BasicNameValuePair bp7 = new BasicNameValuePair(
									"sleep_duration",
									dataObject.getSleep_duration());
							BasicNameValuePair bp8 = new BasicNameValuePair(
									"my_int_occupation",
									dataObject.getMy_int_occupation());
							data.add(bp1);
							data.add(bp2);
							data.add(bp3);
							data.add(bp4);
							data.add(bp5);
							data.add(bp6);
							data.add(bp7);
							data.add(bp8);
//							String result = httpUtils.doPost(
//									URLUtil.UPLOAD_SLEEP_DATA_URL, data);
//							JSONObject jo = new JSONObject(result);
//							if (jo.has("response")) {
//								String code = jo.getString("response");
//								// 测试代码
//								if (code.contains("4036")
//										|| code.contains("4037")) {
//									// 数据上传成功
//									ContentValues cv = new ContentValues();
//									cv.put("isupload", "1");
//									operate.update(cv, "date = ?",
//											new String[] { date });
//								}
//							}
							operate.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}

				}.execute(0);

				break;
			}
		}

	};
	private HttpUtils httpUtils = new HttpUtils();

	/**
	 * 获取每日上传数据
	 * 
	 * @return
	 * @throws Exception
	 */
	private UpdateDataObject getUpdateData(String date) throws Exception {
		UpdateDataObject dataObject = new UpdateDataObject();
		SharedPreferences sp = getApplicationContext().getSharedPreferences(
				SleepInfo.FILENAME_USER, Context.MODE_APPEND);
		String userid = PreManager.instance()
				.getUserId(getApplicationContext());
		String locationx = sp.getString(SleepInfo.KEY_LOCATION_X, "0");
		String locationy = sp.getString(SleepInfo.KEY_LOCATION_Y, "0");
		String occupation = PreManager.instance().getUserProfession(
				getApplicationContext())
				+ "";
		dataObject.setMy_int_id(userid);
		dataObject.setDate_of_data(date.replaceAll("-", ""));
		dataObject.setUser_location_x(locationx);
		dataObject.setUser_location_y(locationy);
		dataObject.setMy_int_occupation(occupation);

		MyDatabaseHelper helper = MyDatabaseHelper
				.getInstance(getApplicationContext());
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),
				MyTabList.SLEEPTIME);
		@SuppressWarnings("unchecked")
		List<SleepResult> srList = (List<SleepResult>) operate.query(
				new String[] { "date", "sleeptime", "uptime", "sleeplength",
						"healthlength", "starttime", "endtime", "sleepacce",
						"upacce", "diagramdata", "ischange", "isupload" },
				"date = ?", new String[] { date }, "date");
		for (SleepResult result : srList) {
			String sleep_point = result.getSleeptime() == null
					|| "".equals(result.getSleeptime()) ? "0" : DataUtils
					.getSleepOrUptime(result.getSleeptime(),
							result.getStarttime(), result.getEndtime(),
							result.getDate());
			String wakup_point = result.getUptime() == null
					|| "".equals(result.getUptime()) ? "0" : DataUtils
					.getSleepOrUptime(result.getUptime(),
							result.getStarttime(), result.getEndtime(),
							result.getDate());
			;
			dataObject.setSleep_point(sleep_point);
			dataObject.setWakeup_point(wakup_point);
			String sleepLength = (Long.valueOf(result.getSleepLength() == null
					|| "".equals(result.getSleepLength()) ? "0" : result
					.getSleepLength()) / 60.0f) + "";
			
			sleepLength = "0".equals(sleepLength) ? "0.0" : sleepLength;
			int pointLength = sleepLength.indexOf(".");
			int length = sleepLength.length();
			if((length - pointLength) > 3) {
				dataObject.setSleep_duration(sleepLength.substring(0,
						sleepLength.indexOf(".") + 3));	
			} else {
				dataObject.setSleep_duration(sleepLength);
			}
		}
		// 测试代码
		System.out.println("上传数据   " + dataObject.getDate_of_data() + " "
				+ dataObject.getMy_int_id() + " "
				+ dataObject.getMy_int_occupation() + "  "
				+ dataObject.getSleep_duration() + "  "
				+ dataObject.getSleep_point() + "  "
				+ dataObject.getUser_location_x() + " "
				+ dataObject.getUser_location_y() + "  "
				+ dataObject.getWakeup_point());
		return dataObject;
	}

	/**
	 * 开启定时通知
	 * 
	 * @throws ParseException
	 */
	private void startTicker() throws ParseException {
		SharedPreferences sp = getApplicationContext().getSharedPreferences(
				SleepInfo.SLEEP_SETTIME, MODE_APPEND);
		boolean isOpen = sp.getBoolean(SleepInfo.OPEN_SET, false);
		String date = DataUtils.getRecordDate(new Date());
		long t10 = DataUtils.getT1(date) + 1*60*60*1000;
		System.out.println("测试闹钟：开启闹钟");
//		if (manager == null) {
//			manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//		}
		if (isOpen) {
			System.out.println("测试闹钟：闹钟是打开的");
			long befortime = sp.getLong(SleepInfo.NOTIFICATION_TIME,
					SleepInfo.NOTIFICATION_SPACE);
			System.out.println("测试闹钟：闹钟预设时间： "+(befortime/60000)+" 分钟");
			long currenttime = System.currentTimeMillis();
			int remindStyle = sp.getInt(SleepInfo.REMIND_TYPE, -1);
			String remindmsg = sp.getString(SleepInfo.REMIND_MSG, "您设置的睡觉时间快到了，准备好睡觉了么?");
			
			if (currenttime >= ( t10 - befortime) && currenttime < t10) {
				System.out.println("测试闹钟：在预设范围以内");
//				if (notificationIntent == null) {
//					notificationIntent = new Intent(
//							"background.NotificationService");
//				}
//				if (mPendingIntent == null) {
//					mPendingIntent = PendingIntent.getService(this, 0,
//							notificationIntent,
//							PendingIntent.FLAG_UPDATE_CURRENT);
//				}
				String alarmState = PreManager.instance().getAlarmState(getApplicationContext());
				long alarmTime = PreManager.instance().getAlarmTime(getApplicationContext());
				if("0".equals(alarmState)) {
					System.out.println("测试闹钟：状态为0立刻响起");
//					manager.set(AlarmManager.RTC_WAKEUP,
//							System.currentTimeMillis(), mPendingIntent);
					PreManager.instance().saveAlarmTime(getApplicationContext(), currenttime);
					PreManager.instance().saveAlarmState(getApplicationContext(), "2");
					inidNotification( befortime );
				} else if ("1".equals(alarmState)) {
					System.out.println("测试闹钟：状态为1，判断上一次响起时间");
					if(Math.abs(currenttime - alarmTime) >= SleepInfo.NOTIFICATION_INTERUPT) {
						System.out.println("测试闹钟：响起时间间隔大于15分钟，立马响起");
//						manager.set(AlarmManager.RTC_WAKEUP,
//								System.currentTimeMillis(), mPendingIntent);
						PreManager.instance().saveAlarmTime(getApplicationContext(), currenttime);
						PreManager.instance().saveAlarmState(getApplicationContext(), "2");
						inidNotification( befortime );
					}
				}
			} else {
				System.out.println("测试闹钟：不在预设范围内,将状态设为0");
				PreManager.instance().saveAlarmState(getApplicationContext(), "0");
			}
		}
	}
	
	private RemoteViews mRemoteView = null;
	private RemoteViews mRemoteViewno = null;
	private void inidNotification( long time) throws ParseException {
		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification mNotification = new Notification();  
		mNotification.flags |= Notification.FLAG_AUTO_CANCEL; 
        mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;
		mNotification.sound = Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.default_audio);
		mNotification.defaults |= Notification.DEFAULT_VIBRATE;
		
		mNotification.icon = R.drawable.logo;
		mNotification.tickerText = "您该睡觉了哦";
		mNotification.when = System.currentTimeMillis();
		
		if( time < ( 2 * SleepInfo.NOTIFICATION_INTERUPT)) {
			System.out.println("测试闹钟：小于了36分钟 " + 2*SleepInfo.NOTIFICATION_INTERUPT + " 预设时间："+time);
			if(mRemoteViewno == null) {
				mRemoteViewno = new RemoteViews("com.yzm.sleep",R.layout.notification_info_no);	
			}
			Intent intent = new Intent("background.NotificationReceiver");
			PendingIntent mPendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			mRemoteViewno.setOnClickPendingIntent(R.id.sleep, mPendingIntent);
			
			mNotification.contentView =  mRemoteViewno;
			
		} else {
			System.out.println("测试闹钟：小于了36分钟 " + 2 * SleepInfo.NOTIFICATION_INTERUPT + " 预设时间："+time);
			if(mRemoteView == null) {
				mRemoteView = new RemoteViews("com.yzm.sleep",R.layout.notification_info);	
			}
			Intent intent = new Intent("background.NotificationReceiver");
			PendingIntent mPendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			mRemoteView.setOnClickPendingIntent(R.id.sleep, mPendingIntent);
			
			Intent waitintent = new Intent("background.WaitNotificationReceiver");
			PendingIntent ingIntent = PendingIntent.getBroadcast(this, 0, waitintent, PendingIntent.FLAG_UPDATE_CURRENT);
			mRemoteView.setOnClickPendingIntent(R.id.wait, ingIntent);
			
			mNotification.contentView =  mRemoteView;
		}
		
		manager.notify(1, mNotification);
	}
	
}
