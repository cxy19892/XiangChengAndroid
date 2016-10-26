package com.yzm.sleep.activity.alar;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.yzm.sleep.R;
import com.yzm.sleep.background.AlarmService;
import com.yzm.sleep.background.AnalyzeData;
import com.yzm.sleep.background.DataUtils;
import com.yzm.sleep.background.JudgFuction;
import com.yzm.sleep.utils.PreManager;

/**
 * Alarm Clock alarm alert: pops visible indicator and plays alarm tone. This
 * activity is the full screen version which shows over the lock screen with the
 * wallpaper as the background.
 */
public class AlarmAlertFullScreen extends Activity {

	// These defaults must match the values in res/xml/settings.xml
	private static final String DEFAULT_SNOOZE = "10";
	private static final String DEFAULT_VOLUME_BEHAVIOR = "2";
	protected static final String SCREEN_OFF = "screen_off";

	private MediaPlayer mMediaPlayer;
	private int mVolumeBehavior;

	private int AlarmID = 0;

	private int AlarmDelay = 0;

	private String AlarmTitle = "";

	private String AlarmAudio = "";

	private String AlarmTime = "";

	private String AlarmAudioCover = "";
	
	private String AudioCoverKey = "";

	private boolean isCalculate = false;
	private boolean isCancelSleepRecord = false;

	// Receives the ALARM_KILLED action from the AlarmKlaxon,
	// and also ALARM_SNOOZE_ACTION / ALARM_DISMISS_ACTION from other
	// applications
//	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			String action = intent.getAction();
//			if (action.equals(Alarms.ALARM_SNOOZE_ACTION)) {
//				snooze();
//			} else if (action.equals(Alarms.ALARM_DISMISS_ACTION)) {
//				dismiss(false);
//			} else {
//
//			}
//		}
//	};
//	private Button snooze;
	private Button dismiss;

	public void startPlay() {

		// Looper.prepare();
		// TODO Auto-generated method stub

		String fileName = AlarmAudio;
		// String fileName = Environment.getExternalStorageDirectory()
		// .toString() + "/testAAC.aac";

		File file = new File(fileName);

		if (file.exists()) {
			try {
				mMediaPlayer = MediaPlayer.create(AlarmAlertFullScreen.this,
						Uri.parse(fileName));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(mMediaPlayer == null){
				mMediaPlayer = MediaPlayer.create(
						AlarmAlertFullScreen.this,
						Uri.parse("android.resource://" + getPackageName() + "/"
								+ R.raw.default_audio));
			}
		} else {
			mMediaPlayer = MediaPlayer.create(
					AlarmAlertFullScreen.this,
					Uri.parse("android.resource://" + getPackageName() + "/"
							+ R.raw.default_audio));
		}
		mMediaPlayer.setLooping(true);
		mMediaPlayer.start();

		// Looper.loop();

		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {}
		// }).start();

	}

	public void stopPlay() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			try {
				mMediaPlayer.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		checkSleep();

		AlarmID = getIntent().getIntExtra("AlarmID", 0);

		AlarmDelay = getIntent().getIntExtra("AlarmDelay", 0);

		AlarmTitle = getIntent().getStringExtra("AlarmTitle");

		AlarmAudio = getIntent().getStringExtra("AlarmAudio");

		AlarmTime = getIntent().getStringExtra("AlarmTime");

		AlarmAudioCover = getIntent().getStringExtra("AlarmAudioCover");
		
		AudioCoverKey = getIntent().getStringExtra("AudioCoverKey");

		if(TextUtils.isEmpty(AlarmTitle)){
			AlarmTitle = "闹钟提醒";
		}
		requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);

		final Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		// Turn on the screen unless we are being launched from the AlarmAlert
		// subclass.
		if (!getIntent().getBooleanExtra(SCREEN_OFF, false)) {
			win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
					| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
					| WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
		}

		updateLayout();

		// Register to get the alarm killed/snooze/dismiss intent.
		// IntentFilter filter = new IntentFilter(Alarms.ALARM_KILLED);
		// filter.addAction(Alarms.ALARM_SNOOZE_ACTION);
		// filter.addAction(Alarms.ALARM_DISMISS_ACTION);
		// registerReceiver(mReceiver, filter);

		startPlay();

	}

	private void setTitle(String AlarmTitle) {
		// String label = mAlarm.getLabelOrDefault(this);
		TextView title = (TextView) findViewById(R.id.alertTitle);
		title.setText(AlarmTitle);
	}
	
	private void setTime(String AlarmTime) {
		TextView title = (TextView) findViewById(R.id.alertTime);
		title.setText(AlarmTime);
	}

	/*private void setOther() {
		LinearLayout layout_time = (LinearLayout) findViewById(R.id.layout_time);
		TextView tv_date = (TextView) findViewById(R.id.tv_date);
		TextView tv_week = (TextView) findViewById(R.id.tv_week);

		String weeks[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		// layout_time.getBackground().setAlpha(26);
		Calendar calendar = Calendar.getInstance();
		tv_date.setText(SleepUtils.getFormatedDateTime("yy-MM-dd",
				calendar.getTimeInMillis()));
		String week = weeks[calendar.get(Calendar.DAY_OF_WEEK) - 1];
		tv_week.setText(week);

	}*/

	/**
	 * 弹出提问起床机制
	 */
	private void checkSleep() {
		try {
			final String date = DataUtils.getRecordDate(new Date());
			long currenttime = System.currentTimeMillis();
			String record_state = DataUtils.getRecordState(
					AlarmAlertFullScreen.this, date);
			if ("2".equals(record_state) && currenttime > DataUtils.getT1(date)
					&& currenttime < DataUtils.getT2(date)) {
				
				try {
					isCalculate = true;
					long starttime = DataUtils.getT1(date);
					long endtime = DataUtils.getT2(date);
					if (AnalyzeData.haveSleep(
							AlarmAlertFullScreen.this, date, starttime,
							endtime)) {
//						return new Integer(1);
						isCancelSleepRecord = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		Intent intent = new Intent();
		intent.setAction("com.xc.alarm.add.ALARM_UPDATE_SUCCESS");
		sendBroadcast(intent);
	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		}
		
	};
	
	/**
	 * 每隔0.5秒检查一次状态
	 */
	private long waittimes = 0;
	private void checkState(long time) {
		waittimes = waittimes + time;
		if(waittimes >= 6000) {			//循环出口1
			waittimes = 0;
		}else {
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if("1".equals(PreManager.instance().getAnalysisState(AlarmAlertFullScreen.this))
							&& "1".equals(PreManager.instance().getAmendState(AlarmAlertFullScreen.this))
							&& "1".equals(PreManager.instance().getAccState(AlarmAlertFullScreen.this))) {
						waittimes = 0;					//循环出口2
						String date;
						try {
							date = DataUtils.getRecordDate(new Date());
							DataUtils.saveRecordState(AlarmAlertFullScreen.this, date, "3");
							runJudge(date);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
					}else {
						checkState(500);
					}
				}
				
			}, 500);
		}
		
	}
	
	/**
	 * 异步执行判断函数
	 * @param date
	 */
	private void runJudge(final String date) {
		new AsyncTask<Object,Object,Object>(){

			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				try {
					new JudgFuction().judge(AlarmAlertFullScreen.this, date);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Object result) {
				// TODO Auto-generated method stub
//				getData();
//				adapter.notifyDataSetChanged();
//				if(list.size() > 0) {
//					listener.setSleepDate(DataUtils.dateFomate(list,list.size() - 1)+"晚");
//					viewpager.setCurrentItem(list.size() - 1);	
//				}
			}	
			
		}.execute(0);
	}

	/*@SuppressLint("NewApi")
	private void setCover(String cover,String coverKye) {
		HalfRoundAngleImageView imagView = (HalfRoundAngleImageView) findViewById(R.id.iv_theme_pic);
		if (!TextUtils.isEmpty(cover)) {
			if (cover.length()>4 && cover.substring(0, 4).equals("http")) {// 网络图片
				if (coverKye != null) {
					ImageLoaderUtils.getInstance().displayImage(cover,coverKye, imagView,
							ImageLoaderUtils.getOpthion());
				}else {
					ImageLoaderUtils.getInstance().displayImage(cover,cover, imagView,
							ImageLoaderUtils.getOpthion());
				}
			} else{ // 本地图片
				String string = (Uri.fromFile(new File(cover)).toString());
				ImageLoaderUtils.getInstance().displayImage(string, imagView,
						ImageLoaderUtils.getOpthion());
			}
		}
	}*/

	private void updateLayout() {
		LayoutInflater inflater = LayoutInflater.from(this);

		setContentView(inflater.inflate(R.layout.alarm_alert_new, null));


		dismiss = (Button) findViewById(R.id.dismiss);
		if (isCancelSleepRecord) {
			dismiss.setText("起床");
		} else {
			dismiss.setText("关闭");
		}
		dismiss.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				String dismissString = dismiss.getText().toString();
				if (dismissString.equals("起床")) {
					checkState(0);
					
					Intent intent = new Intent();
					intent.setAction("com.xiangcheng.record.STOP_SLEEP_RECORD");
					sendBroadcast(intent);
				}
				startGetup();
			}

		});

		/* Set the title from the passed in alarm */
		setTitle(AlarmTitle);
		setTime(AlarmTime);
//		setCover(AlarmAudioCover,AudioCoverKey);
//		setOther();
	}


	private void startGetup() {
		stopPlay();
		if (AlarmDelay > 0) {
			/* 发送收到delay 类型广播 */
			SharedPreferences sp = AlarmAlertFullScreen.this
					.getSharedPreferences("AlarmDelay", MODE_PRIVATE);

			long sysTime = System.currentTimeMillis();

			int iAlarmID = sp.getInt("AlarmID", -1);
			long lAlarmCurrTime = sp.getLong("AlarmCurrTime", -1);
			int iAlarmDelay = sp.getInt("AlarmDelay", -1);
			String sAlarmTitle = sp.getString("AlarmTitle", "");
			String sAlarmAudio = sp.getString("AlarmAudio", "");

			String sAlarmTime = sp.getString("AlarmTime", "");

			String sAlarmAudioCover = sp.getString("AlarmAudioCover", "");
			
			String sAudioCoverKey  = sp.getString("AudioCoverKey", "");

			Intent intent = new Intent(AlarmAlertFullScreen.this,
					AlarmReceiver.class);

			intent.setAction(AlarmService.ACTION_ALARM);

			intent.putExtra("AlarmID", 9999);

			intent.putExtra("AlarmTitle", sAlarmTitle);

			intent.putExtra("AlarmDelay", iAlarmDelay);

			intent.putExtra("AlarmAudio", sAlarmAudio);

			intent.putExtra("AlarmTime", sAlarmTime);

			intent.putExtra("AlarmAudioCover", sAlarmAudioCover);			
			
			intent.putExtra("AudioCoverKey", sAudioCoverKey);
			
			
			PendingIntent sender = PendingIntent.getBroadcast(
					AlarmAlertFullScreen.this, 9999, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);

			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

			am.cancel(sender);

//			System.out.println("关闭小睡。");

			// 存入数据
			Editor editor = sp.edit();

			editor.putInt("AlarmID", -1);

			editor.commit();

		}

		dismiss(false);
	}

	// Attempt to snooze this alert.
	private void snooze() {
		// Do not snooze if the snooze button is disabled.
		if (!findViewById(R.id.snooze).isEnabled()) {
			dismiss(false);
			return;
		}
		finish();
	}

	private NotificationManager getNotificationManager() {
		return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

	// Dismiss the alarm.
	private void dismiss(boolean killed) {
		// The service told us that the alarm has been killed, do not modify
		// the notification or stop the service.
		if (!killed) {
			// Cancel the notification and stop playing the alarm
			// NotificationManager nm = getNotificationManager();
			// nm.cancel(mAlarm.id);
			// stopService(new Intent(Alarms.ALARM_ALERT_ACTION));
		}
		finish();
	}

	/**
	 * this is called when a second alarm is triggered while a previous alert
	 * window is still active.
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		Log.v("wangxianming", "AlarmAlert.OnNewIntent()");

		// mAlarm = intent.getParcelableExtra(Alarms.ALARM_INTENT_EXTRA);

		setTitle(AlarmTitle);
		setTime(AlarmTime);
//		setCover(AlarmAudioCover,AudioCoverKey);
//		setOther();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onStop() {
		stopPlay();
		super.onStop();
		dismiss(false);
	}

	@Override
	public void onDestroy() {
		stopPlay();
		super.onDestroy();
		Log.v("wangxianming", "AlarmAlert.onDestroy()");
		// No longer care about the alarm being killed.

		

		// unregisterReceiver(mReceiver);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// Do this on key down to handle a few of the system keys.
		boolean up = event.getAction() == KeyEvent.ACTION_UP;
		switch (event.getKeyCode()) {
		// Volume keys and camera keys dismiss the alarm
		case KeyEvent.KEYCODE_VOLUME_UP:
		case KeyEvent.KEYCODE_VOLUME_DOWN:
		case KeyEvent.KEYCODE_CAMERA:
		case KeyEvent.KEYCODE_FOCUS:
//		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_MENU:
		case KeyEvent.KEYCODE_HOME:
			if (up) {
				stopPlay();
				dismiss(false);
			}
			return false;
		default:
			break;
		}
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			return false;
		}else
		return super.dispatchKeyEvent(event);
	}

	@Override
	public void onBackPressed() {
		// Don't allow back to dismiss. This method is overriden by AlarmAlert
		// so that the dialog is dismissed.
		return;
	}
}
