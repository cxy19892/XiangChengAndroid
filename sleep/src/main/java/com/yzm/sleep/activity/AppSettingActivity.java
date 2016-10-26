package com.yzm.sleep.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.pillow.PillowDataOpera;
import com.yzm.sleep.background.DataUtils;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.background.MytabOperate;
import com.yzm.sleep.background.SleepInfo;
import com.yzm.sleep.model.DaySleepMsg;
import com.yzm.sleep.model.MyAlertDialog;
import com.yzm.sleep.model.MyAlertDialog.MyOnClickListener;
import com.yzm.sleep.model.PillowDataModel;
import com.yzm.sleep.model.SmartRemindBean;
import com.yzm.sleep.render.GetSleepResultValueClass.SleepDataHead;
import com.yzm.sleep.utils.BluetoothDataFormatUtil;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceOpenJPushCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.OpenJPushParamClass;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.SmartNotificationUtil;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengProcClass;

@SuppressLint("SimpleDateFormat")
public class AppSettingActivity extends BaseActivity implements
		OnClickListener, OnCheckedChangeListener {
	private CheckBox cbForumMessage, cbReceiverChatMessage, cbRemindSleep;
	private TextView tvVersionsName;
	private SharedPreferences sp;
	private MyAlertDialog versionsDetectionDialog;

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_setting);
		((TextView) findViewById(R.id.title)).setText("应用设置");
		findViewById(R.id.back).setOnClickListener(this);

		findViewById(R.id.layoutForumMessage).setOnClickListener(this);
		findViewById(R.id.layoutReceiverChatMessage).setOnClickListener(this);
		findViewById(R.id.layoutRemindSleep).setOnClickListener(this);
		findViewById(R.id.layoutCommunityAccount).setOnClickListener(this);
		findViewById(R.id.layoutVersionsDetection).setOnClickListener(this);
		findViewById(R.id.layoutFiveStarComment).setOnClickListener(this);
		findViewById(R.id.layoutAboutXiangcheng).setOnClickListener(this);

		cbForumMessage = (CheckBox) findViewById(R.id.cbForumMessage);
		cbReceiverChatMessage = (CheckBox) findViewById(R.id.cbReceiverChatMessage);
		cbRemindSleep = (CheckBox) findViewById(R.id.cbRemindSleep);
		cbForumMessage.setOnCheckedChangeListener(this);
		cbReceiverChatMessage.setOnCheckedChangeListener(this);
		cbRemindSleep.setOnCheckedChangeListener(this);

		tvVersionsName = (TextView) findViewById(R.id.tvVersionsName);

		ImageView ivUpdate = (ImageView) findViewById(R.id.iv_update_message);
		if (PreManager.instance().getIsUpdateVersion(this)) {
			ivUpdate.setVisibility(View.VISIBLE);
		} else {
			ivUpdate.setVisibility(View.INVISIBLE);
		}

		// 论坛消息
		if (PreManager.instance().getIsOpenFormuInform(AppSettingActivity.this).equals("1")) {
			cbForumMessage.setChecked(true);
		} else {
			cbForumMessage.setChecked(false);
		}

		// 聊天通知
		if (PreManager.instance().getIsOpenChatInform(AppSettingActivity.this) == 1) {
			cbReceiverChatMessage.setChecked(true);
		} else {
			cbReceiverChatMessage.setChecked(false);
		}

		// 睡前提醒
		sp = getApplicationContext().getSharedPreferences(
				SleepInfo.SLEEP_SETTIME, getApplicationContext().MODE_APPEND);
		if ("".equals(sp.getString(SleepInfo.REMIND_BEFORE_SLEEP, ""))) {// Int(SleepInfo.REMIND_TYPE_NIGHT,
																			// 0x10)
																			// ==
																			// 0x10)
																			// {
			cbRemindSleep.setChecked(true);
		} else {
			cbRemindSleep.setChecked(false);
		}

		// 检测版本
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (info != null) {
			tvVersionsName.setText(info.versionName);
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.layoutForumMessage:
			if (!Util.checkNetWork(this)) {
				Util.show(this, "网络连接错误");
				return;
			}
			if(!PreManager.instance().getIsLogin(this)){
				startActivity(new Intent(this, LoginActivity.class));
			}else {
				if (cbForumMessage.isChecked()) {
					openJpush("2");
				}else {
					openJpush("1");
				}
			}
			break;
		case R.id.layoutReceiverChatMessage:
			cbReceiverChatMessage
					.setChecked(!cbReceiverChatMessage.isChecked());
			break;
		case R.id.layoutRemindSleep:
			cbRemindSleep.setChecked(!cbRemindSleep.isChecked());
			break;
		case R.id.layoutCommunityAccount:
			if (!PreManager.instance().getIsLogin(AppSettingActivity.this)) {
				intent = new Intent(this, LoginActivity.class);
			} else {
				intent = new Intent(this, CommunityAccountActivity.class);
			}
			break;
		case R.id.layoutVersionsDetection:
			versionsDetection();
			break;
		case R.id.layoutFiveStarComment:
			goToGuanwang();
			break;
		case R.id.layoutAboutXiangcheng:
			intent = new Intent(this, AboutActivity.class);
			break;

		default:
			break;
		}

		if (intent != null) {
			startActivity(intent);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.cbReceiverChatMessage:
			int c2 = 0;
			if (isChecked) {
				c2 = 1;
			}
			PreManager.instance().saveIsOpenChatInform(AppSettingActivity.this,
					c2);
			break;
		case R.id.cbRemindSleep:
			if (isChecked) {
				openTheNightRemind();
				getSleepDataAndSetSmartAlarm(1);
			} else {
				closeTheNightRemind();
			}
			break;

		default:
			break;
		}

	}
	
	private void openJpush(final String c1) {
		OpenJPushParamClass mParam = new OpenJPushParamClass();
		mParam.uid = PreManager.instance().getUserId(this);
		mParam.flag = c1;
		new XiangchengProcClass(AppSettingActivity.this).openJPush(mParam, new InterfaceOpenJPushCallback() {
			
			@Override
			public void onSuccess(int icode, String strSuccMsg) {
				PreManager.instance().saveIsOpenFormuInform(
						AppSettingActivity.this, c1);
				if (c1.equals("1")) {
					cbForumMessage.setChecked(true);
				}else {
					cbForumMessage.setChecked(false);
				}
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				Util.show(AppSettingActivity.this, strErrMsg);
				if (c1.equals("1")) {
					cbForumMessage.setChecked(false);
				}else {
					cbForumMessage.setChecked(true);
				}
			}
		});
	}

	/**
	 * 跳转至香橙官网
	 */
	private void goToGuanwang(){
		try {
			Uri newUri = Uri.parse("http://www.appxiangcheng.com");
			Intent newIntent = new Intent(Intent.ACTION_VIEW, newUri);
			newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(newIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void openTheNightRemind() {
		Editor edt = sp.edit();
		edt.putString(SleepInfo.REMIND_BEFORE_SLEEP, "");
		edt.commit();
	}

	private void closeTheNightRemind() {
		Editor edt = sp.edit();
		edt.putString(SleepInfo.REMIND_BEFORE_SLEEP, "0");
		edt.commit();
	}

	// 获取昨天的日期
	@SuppressLint("SimpleDateFormat")
	private String getYesterdayTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		Date curDate = new Date(System.currentTimeMillis() - 24 * 60 * 60000);// 获取当前时间

		return formatter.format(curDate);
	}

	/**
	 * @param remind_style
	 *            0 午间提醒 1 睡前提醒
	 */
	@SuppressLint("SimpleDateFormat")
	private void getSleepDataAndSetSmartAlarm(int remind_style) {
		getDbData();
		// 判断是否开启智能闹钟
		// 如果开启则则去取得提醒然后设置提醒时间
		SmartRemindBean mSmartRemindBean = null;
		@SuppressWarnings("static-access")
		SharedPreferences sp = this.getApplicationContext()
				.getSharedPreferences(SleepInfo.SLEEP_SETTIME,
						getApplicationContext().MODE_APPEND);

		String startTime = "";
		String endTime = "";
		String suggestSleepTime = PreManager.instance().getSleepTime_Setting(
				this);
		String remind_time_style = sp.getString(SleepInfo.REMIND_BEFORE_SLEEP,
				"");
		if (remind_time_style.equals("")) {
			// 如果绑定了硬件 就使用硬件的数据
			if (!TextUtils.isEmpty(PreManager.instance()
					.getBundbluetoothPillow(this))) {
				PillowDataModel pModel = PillowDataOpera.queryDataFromSQL(this
						.getApplicationContext(), TimeFormatUtil.getYesterDay(
						new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
						"yyyy-MM-dd"));
				if (pModel != null) {
					LogUtil.i("chen", "-->pModel != null");
					if (pModel.getBfr() != null) {
						LogUtil.i("chen", "-->pModel.getBfr() != null");
						SleepDataHead datahead = BluetoothDataFormatUtil
								.format3(pModel.getBfr(), 10);
						startTime = TimeFormatUtil.formatTime1(
								datahead.InSleepTime, "HH:mm");
						endTime = TimeFormatUtil.formatTime1(
								datahead.OutSleepTime, "HH:mm");
					}
				}
				if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
					mSmartRemindBean = SmartNotificationUtil
							.GetSmartNotifications(
									remind_style,
									PreManager.instance().getGetupTime_Setting(
											this),
									PreManager.instance().getSleepTime_Setting(
											this), suggestSleepTime);
				} else {
					// suggestSleepTime = startTime;
					mSmartRemindBean = SmartNotificationUtil
							.GetSmartNotifications(remind_style, endTime,
									startTime, suggestSleepTime);
				}
			} else {// 如果没有硬件的时候使用软件的数据
				DaySleepMsg mDaySleepMsg = getDaySleepData(getYesterdayTime());
				if (null != mDaySleepMsg) {
					startTime = mDaySleepMsg.getSleep_time();
					endTime = mDaySleepMsg.getup_time;
					if (null == startTime) {
						startTime = "";
					}
					if (null == endTime) {
						endTime = "";
					}
				}

				if (startTime.equals("") || endTime.equals("")) {
					startTime = sp.getString(SleepInfo.STARTTIME, "");
					endTime = sp.getString(SleepInfo.ENDTIME, "");

					if (startTime.equals("") || endTime.equals("")) {
						mSmartRemindBean = SmartNotificationUtil
								.GetSmartNotifications(
										remind_style,
										PreManager
												.instance()
												.getGetupTime_Setting(
														AppSettingActivity.this),
										PreManager
												.instance()
												.getSleepTime_Setting(
														AppSettingActivity.this),
										suggestSleepTime);
					} else {
						int start_time = Integer.parseInt(startTime);
						int end_time = Integer.parseInt(endTime);
						startTime = start_time
								/ 60
								+ ":"
								+ (start_time % 60 == 0 ? "00"
										: start_time % 60);
						endTime = end_time / 60 + ":"
								+ (end_time % 60 == 0 ? "00" : end_time % 60);
						mSmartRemindBean = SmartNotificationUtil
								.GetSmartNotifications(remind_style, endTime,
										startTime, suggestSleepTime);
					}

				} else {
					mSmartRemindBean = SmartNotificationUtil
							.GetSmartNotifications(remind_style, endTime,
									startTime, suggestSleepTime);
				}
			}
		}
		if (mSmartRemindBean != null
				&& mSmartRemindBean.SuggestRemindTime != null) {
			// 重新设置睡觉提醒时间
			Editor edt = sp.edit();
			edt.putString(SleepInfo.REMIND_MSG, mSmartRemindBean.Remindmsgs);
			if (remind_style == 0) {
				edt.putString(SleepInfo.REMIND_TIME_DAY,
						mSmartRemindBean.SuggestRemindTime);
			} else {
				edt.putString(SleepInfo.REMIND_TIME_NIGHT,
						mSmartRemindBean.SuggestRemindTime);
			}
			edt.putString(SleepInfo.REMIND_TIME, mSmartRemindBean.SuggestRemindTime);
			edt.commit();
//			openRemindAlarm(mSmartRemindBean.SuggestRemindTime);
			String datee = TimeFormatUtil.getTodayTime() + " "	+ mSmartRemindBean.SuggestRemindTime;
			String datee1 = TimeFormatUtil.getTomaDay("yyyy-MM-dd") + " " + mSmartRemindBean.SuggestRemindTime;
			
			SimpleDateFormat Day = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm");
			Date TimeSet, TimeSet1;
			try {
				TimeSet = Day.parse(datee);
				TimeSet1 = Day.parse(datee1);
				
				long PlantTime = TimeSet.getTime();
				long PlantTime1 = TimeSet1.getTime();
				
				long currSysTime = System.currentTimeMillis();
	
				if (currSysTime - PlantTime > 0)// 表示当前系统时间已经超过预设时间
				{
					openRemindAlarm(PlantTime1);
					
				} else {
					openRemindAlarm(PlantTime);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*LogUtil.d("chen",
					"mSmartRemindBean!=null,mSmartRemindBean.SuggestRemindTime="
							+ mSmartRemindBean.SuggestRemindTime
							+ "，mSmartRemindBean.Remindmsgs="
							+ mSmartRemindBean.Remindmsgs);
			String datee = TimeFormatUtil.getTodayTime() + " "
					+ mSmartRemindBean.SuggestRemindTime;

			SimpleDateFormat Day = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date TimeSet;
			try {
				TimeSet = Day.parse(datee);
				long PlantTime = TimeSet.getTime();
				long currSysTime = System.currentTimeMillis();

				if (currSysTime - PlantTime > 0)// 表示当前系统时间已经超过预设时间
				{
					return;

				} else {
					openRemindAlarm(mSmartRemindBean.SuggestRemindTime);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}*/
		}
	}

	public ArrayList<DaySleepMsg> list = new ArrayList<DaySleepMsg>();

	private void getDbData() {
		if (!this.isFinishing()) {
			MyDatabaseHelper helper = MyDatabaseHelper
					.getInstance(getApplicationContext());
			MytabOperate operate = new MytabOperate(
					helper.getWritableDatabase(), MyTabList.SLEEPTIME);
			try {
				list.clear();
				ArrayList<DaySleepMsg> tmplist = operate.queryDisplayDateList(
						DataUtils.getRecordDate(new

						Date()), getApplicationContext());
				if (tmplist != null) {
					list.addAll(tmplist);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			} finally {
				operate.close();
			}
		}

	}

	// 设置提醒闹钟
	private void openRemindAlarm(long remindtime) {
//		String[] times = remindtime.split(":");
		Intent intent = new Intent("com.yzm.sleep.alarm.MART_NOTIFY");
		PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

		Calendar calendar = Calendar.getInstance();
//		calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
//		calendar.set(Calendar.MINUTE, Integer.parseInt(times[1]));
//		calendar.set(Calendar.SECOND, 10);
		calendar.setTimeInMillis(remindtime);
		long remindTime = calendar.getTimeInMillis();
		// Schedule the alarm!
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, remindTime, sender);
	}

	/**
	 * 获取当天的睡眠数据
	 * 
	 * @return
	 */
	private DaySleepMsg getDaySleepData(String date) {
		// if (this == null)
		// return null;
		try {
			MyDatabaseHelper helper = MyDatabaseHelper.getInstance(this);
			SQLiteDatabase db = helper.getWritableDatabase();
			Cursor cursor = db.rawQuery("select * from " + MyTabList.SLEEPTIME
					+ " where date=?", new String[] { date });

			cursor.moveToFirst();
			String sleepTime = cursor.getString(cursor
					.getColumnIndex(MyTabList.TableDay.SLEEPTIME.getCloumn()));
			String getUpTime = cursor.getString(cursor
					.getColumnIndex(MyTabList.TableDay.UPTIME.getCloumn()));
			String dateString = cursor.getString(cursor
					.getColumnIndex(MyTabList.TableDay.DATE.getCloumn()));
			// String sleep_time_setting =
			// cursor.getString(cursor.getColumnIndex(MyTabList.TableDay.ORGSLEEPTIME.getCloumn()));
			DaySleepMsg dayData = new DaySleepMsg();
			dayData.setSleep_time(sleepTime);// (sleepTime);
			dayData.setGetup_time(getUpTime);// GetUpTime(getUpTime);
			dayData.setDate(dateString);
			// dayData.setSleep_time_setting(sleep_time_setting);
			cursor.close();

			return dayData;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 版本更新
	 */
	private void versionsDetection() {
		if (PreManager.instance().getIsUpdateVersion(AppSettingActivity.this)) {
			if (null == versionsDetectionDialog) {
				versionsDetectionDialog = new MyAlertDialog(this,
						R.style.bottom_animation);
			}
			versionsDetectionDialog.show();
			versionsDetectionDialog.setTV1("有新版本了");
			versionsDetectionDialog.setTV2("去更新", new MyOnClickListener() {

				@Override
				public void Onclick(View v) {
					Intent intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					Uri content_url = Uri.parse("http://www.maimaiys.com:8095");
					intent.setData(content_url);
					startActivity(intent);
					versionsDetectionDialog.dismiss();
				}
			}, View.VISIBLE);
			versionsDetectionDialog.setTV3("", null, View.GONE);
			versionsDetectionDialog.setTV4("", null, View.GONE);
			versionsDetectionDialog.setTVbottom("取消", new MyOnClickListener() {

				@Override
				public void Onclick(View v) {

					versionsDetectionDialog.dismiss();
				}

			}, View.VISIBLE);
		} else {
			Util.show(AppSettingActivity.this, "已经是最新版本");
		}
	}
}
