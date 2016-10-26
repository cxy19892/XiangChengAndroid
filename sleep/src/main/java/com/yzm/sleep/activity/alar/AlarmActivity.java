package com.yzm.sleep.activity.alar;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.activity.HomeActivity;
import com.yzm.sleep.adapter.AlarmAdapter;
import com.yzm.sleep.background.AlarmDBOperate;
import com.yzm.sleep.background.AlarmService.ListAlarmTime;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.background.SleepInfo;
import com.yzm.sleep.dao.AudioDAO;
import com.yzm.sleep.model.DownloadAudioInfo;
import com.yzm.sleep.model.MyAlertDialog;
import com.yzm.sleep.model.RingtoneInfo;
import com.yzm.sleep.model.MyAlertDialog.MyOnClickListener;
import com.yzm.sleep.model.MyDialog;
import com.yzm.sleep.utils.AlarmConstants;
import com.yzm.sleep.utils.AlarmUtils;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.SleepUtils;
import com.yzm.sleep.widget.CustomListView;

public class AlarmActivity extends BaseActivity implements OnClickListener {
	public static final String PREFERENCES = "AlarmClock";
	private Context mContext;
	private CustomListView lv_alarm;
	AlarmAdapter alarmAdapter = null;
	private ArrayList<ListAlarmTime> alarmData = new ArrayList<ListAlarmTime>();
	private View footerView;
	private ImageView rightBtn;
	private MyAlertDialog myAlertDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_alarm_info);
		initView();
		registerReceiver();
		MyApplication.instance().setCurrentSelectRingInfo(null);
		/* 首次点卡闹钟界面，保存默认闹钟 */
		if (!PreManager.instance().getHasGotoAlarm(mContext)) {
			AlarmUtils.saveDefaultAlarm(mContext);
			PreManager.instance().saveHasGotoAlarm(mContext, true);
		}
		getAwakeAlarm();
	}

	/**
	 * 刷新闹钟时间
	 */
	public void refreshAlarmTime() {
		AlarmUtils.saveDefaultAlarm(mContext);
		getAwakeAlarm();
	}

	@Override
	public void onResume() {
		super.onResume();
		// registerReceiver();
		// 友盟的统计
		MobclickAgent.onPageStart("SleepAlarm");
		getRemindAlarm();
		MyApplication.instance().setCurrentSendFriend(null);// 置空发送铃声的对象

	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SleepAlarm");
	}

	private MyReceiver myReceiver;

	private void initView() {
		findViewById(R.id.back).setOnClickListener(this);
		((TextView) (findViewById(R.id.title))).setText("事项提醒");

		rightBtn = (ImageView) findViewById(R.id.btn_right);
		rightBtn.setOnClickListener(this);
		footerView = LayoutInflater.from(mContext).inflate(
				R.layout.layout_view_line, null);
//		findViewById(R.id.ib_add_new).setOnClickListener(this);
		lv_alarm = (CustomListView) findViewById(R.id.lv_alarm);
		lv_alarm.setSelector(R.drawable.comm_bg_listview_item);
		alarmAdapter = new AlarmAdapter(mContext);
		lv_alarm.addFooterView(footerView);
		lv_alarm.addHeaderView(footerView);
		lv_alarm.setAdapter(alarmAdapter);
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setImageDrawable(getResources().getDrawable(R.drawable.btn_add_icon_click));
		lv_alarm.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int position = arg2 - lv_alarm.getHeaderViewsCount();
				if (arg2 >= lv_alarm.getHeaderViewsCount()) {
					ListAlarmTime alarmInfo = alarmData.get(position);
					Intent intent = new Intent(mContext,
							RemindAlarmEditActivity.class);
					intent.putExtra(AlarmConstants.IS_EDIT, true);
					intent.putExtra("Alarm", alarmInfo);
					intent.putExtra("from", "alarm_list");

					RingtoneInfo currntRingtoneInfo = new RingtoneInfo();
					currntRingtoneInfo.title = alarmInfo.AlarmTitle;
					currntRingtoneInfo.nickname = alarmInfo.AlarmUserNickname;
					currntRingtoneInfo.profile = alarmInfo.AlarmProfile;
					currntRingtoneInfo.profileKey = alarmInfo.UserProfileKey;
					currntRingtoneInfo.ringtonePath = alarmInfo.AlarmAudio;
					currntRingtoneInfo.int_id = alarmInfo.AlarmUserId;
					currntRingtoneInfo.themePicString = alarmInfo.AlarmAudioCover;
					currntRingtoneInfo.coverKey = alarmInfo.AudioCoverKey;
					currntRingtoneInfo.fileKey = alarmInfo.AudioKey;
					if (alarmInfo.AudioIsLocalRecord == 1) {
						currntRingtoneInfo.isLocalRecord = true;
					} else {
						currntRingtoneInfo.isLocalRecord = false;
					}
					MyApplication.instance().setCurrentSelectRingInfo(
							currntRingtoneInfo);
					MyApplication.instance().setAddStart(false);
					PreManager.instance().saveIsEditAlarm(mContext, true);
					PreManager.instance().saveIsFirstChooseRingtone(mContext,
							false);
					startActivity(intent);
					overridePendingTransition(R.anim.dialog_bottom_in, R.anim.alpha_out);
				}
			}
		});
		
		lv_alarm.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				int myposition = position - lv_alarm.getHeaderViewsCount();
				ListAlarmTime alarmInfo = alarmData.get(myposition);
				if(alarmInfo.isRemind==1){
					showPopWindow(myposition);
				}
				return true;
			}
		});
	}
	
	/**
	 * 长按操作 提示删除或者编辑
	 * @param position
	 */
	private void showPopWindow(final int position) {
		if (null == myAlertDialog) {
			myAlertDialog = new MyAlertDialog(this,
					R.style.bottom_animation);
		}
		myAlertDialog.show();
		myAlertDialog.setTV1("请选择操作");
		myAlertDialog.setTV2("删除", new MyOnClickListener() {

			@Override
			public void Onclick(View v) {
				final ListAlarmTime mAlarm = alarmData.get(position);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						AlarmDBOperate alarmDBOperate = new AlarmDBOperate(MyDatabaseHelper.
								getInstance(AlarmActivity.this).getWritableDatabase(), MyTabList.ALARM_TIME);
						
						ListAlarmTime m_ListAlarmTime = new ListAlarmTime();
						m_ListAlarmTime.AlarmTime = mAlarm.AlarmTime;
						m_ListAlarmTime.AlarmRepeat = mAlarm.AlarmRepeat;
						m_ListAlarmTime.AlarmPlant = mAlarm.AlarmPlant;
						m_ListAlarmTime.AlarmTitle = mAlarm.AlarmTitle;
						m_ListAlarmTime.AlarmDay = mAlarm.AlarmDay;
						m_ListAlarmTime.AlarmDelay = mAlarm.AlarmDelay;
						
						m_ListAlarmTime.AlarmAudio = mAlarm.AlarmAudio;
						m_ListAlarmTime.AlarmOnOff = mAlarm.AlarmOnOff;
						m_ListAlarmTime.AlarmProfile = mAlarm.AlarmProfile;
						m_ListAlarmTime.AlarmUserNickname = mAlarm.AlarmUserNickname;
						m_ListAlarmTime.AlarmUserId = mAlarm.AlarmUserId;
						m_ListAlarmTime.AlarmDownloads = mAlarm.AlarmDownloads;
						
						m_ListAlarmTime.AlarmAudioId = mAlarm.AlarmAudioId;
						m_ListAlarmTime.AlarmAudioCover = mAlarm.AlarmAudioCover;
						
						alarmDBOperate.deleteAwakeAlarm(AlarmActivity.this, mAlarm.AlarmID +"",
								m_ListAlarmTime);
						
						
//						ToastManager.getInstance(AlarmActivity.this).show("删除成功");
						Intent intent = new Intent();
						intent.setAction("com.xc.alarm.add.ALARM_UPDATE_SUCCESS");
						sendBroadcast(intent);
					}
				}).start();
				myAlertDialog.dismiss();
			}
		}, View.VISIBLE);
		myAlertDialog.setTV3("编辑", new MyOnClickListener() {

			@Override
			public void Onclick(View v) {
				ListAlarmTime alarmInfo = alarmData.get(position);
				Intent intent = new Intent(mContext,
						RemindAlarmEditActivity.class);
				intent.putExtra(AlarmConstants.IS_EDIT, true);
				intent.putExtra("Alarm", alarmInfo);
				intent.putExtra("from", "alarm_list");

				RingtoneInfo currntRingtoneInfo = new RingtoneInfo();
				currntRingtoneInfo.title = alarmInfo.AlarmTitle;
				currntRingtoneInfo.nickname = alarmInfo.AlarmUserNickname;
				currntRingtoneInfo.profile = alarmInfo.AlarmProfile;
				currntRingtoneInfo.profileKey = alarmInfo.UserProfileKey;
				currntRingtoneInfo.ringtonePath = alarmInfo.AlarmAudio;
				currntRingtoneInfo.int_id = alarmInfo.AlarmUserId;
				currntRingtoneInfo.themePicString = alarmInfo.AlarmAudioCover;
				currntRingtoneInfo.coverKey = alarmInfo.AudioCoverKey;
				currntRingtoneInfo.fileKey = alarmInfo.AudioKey;
				if (alarmInfo.AudioIsLocalRecord == 1) {
					currntRingtoneInfo.isLocalRecord = true;
				} else {
					currntRingtoneInfo.isLocalRecord = false;
				}
				MyApplication.instance().setCurrentSelectRingInfo(
						currntRingtoneInfo);
				MyApplication.instance().setAddStart(false);
				PreManager.instance().saveIsEditAlarm(mContext, true);
				PreManager.instance().saveIsFirstChooseRingtone(mContext,
						false);
				startActivity(intent);
				myAlertDialog.dismiss();
			}

		}, View.VISIBLE);
		myAlertDialog.setTV4("", null, View.GONE);
		myAlertDialog.setTVbottom("取消", new MyOnClickListener() {

			@Override
			public void Onclick(View v) {

				myAlertDialog.dismiss();
			}

		}, View.VISIBLE);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			Intent mIntent = new Intent(AlarmActivity.this, HomeActivity.class);
			startActivity(mIntent);
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.btn_right:
			AlarmUtils.saveDefaultCurrentAudio(mContext);
			Intent intent = new Intent(mContext, RemindAlarmEditActivity.class);
			intent.putExtra(AlarmConstants.IS_EDIT, false);
			intent.putExtra("from", "alarm_list");
			PreManager.instance().saveIsFirstChooseRingtone(mContext, true);
			MyApplication.instance().setAddStart(true);
			PreManager.instance().saveIsEditAlarm(mContext, false);
			startActivity(intent);
			overridePendingTransition(R.anim.dialog_bottom_in, R.anim.alpha_out);
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent mIntent = new Intent(AlarmActivity.this, HomeActivity.class);
			startActivity(mIntent);
			AppManager.getAppManager().finishActivity();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onDestroy() {

		if (myReceiver != null)
			mContext.unregisterReceiver(myReceiver);
		super.onDestroy();
	}

	/**
	 * 设置listview的高度
	 * scrollView嵌套listview因无法正确计算listview的大小导致只能显示第一条数据，position一直为0
	 * 
	 * @param listView
	 */
	public static void reSetListViewHeight(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 读取闹钟信息
	 */
	public void getAwakeAlarm() {
		alarmData.clear();
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(mContext
				.getApplicationContext());
		SQLiteDatabase db = helper.getWritableDatabase();
		AlarmDBOperate alarmDBOperate = new AlarmDBOperate(db,
				MyTabList.ALARM_TIME);
		ArrayList<ListAlarmTime> alarmTimes = (ArrayList<ListAlarmTime>) alarmDBOperate
				.GetALarmData();

		ListAlarmTime alarmInfo;
		// 专属铃音 神秘铃音响应后 铃音类型更改为专属铃音
		for (int i = 0; i < alarmTimes.size(); i++) {
			alarmInfo = alarmTimes.get(i);
			/*if (alarmInfo.AlarmType == 2) {// 如果是专属铃音闹钟//   --已经没有了专属铃音盒本地录制铃音
				SimpleDateFormat dateformat1 = new SimpleDateFormat( 
						"yyyy-MM-dd HH:mm");
				// 日期，同下，格式yyyy-MM-dd
				Date setDate = null;
				try {
					setDate = dateformat1.parse(alarmInfo.AlarmDay + " "
							+ alarmInfo.AlarmTime);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				Date mDate = new Date(System.currentTimeMillis());

				if (setDate != null && mDate != null) {

					if (mDate.getTime() > setDate.getTime()) {// mdate 在
																// setDate之后，闹钟过期
						alarmInfo.AlarmType = 1; // 闹钟过时后专属铃音自动变成非专属铃音
						updateAlarmAudioType(alarmInfo);
					} else { // mdate 在 setDate之前,闹钟未过期
					}
				}
			}
			// 获取闹钟铃音信息
			if (alarmInfo.AudioIsLocalRecord == 1) {// 本地录制的铃音
				RecordAudioInfo recordAudioInfo = AudioDAO
						.getRecordAudioInfoByPath(mContext,
								alarmInfo.AlarmAudio);
				if (recordAudioInfo != null) {

					alarmInfo.AlarmUserId = recordAudioInfo.userId;
					alarmInfo.AlarmTitle = recordAudioInfo.title;
					alarmInfo.AlarmAudio = recordAudioInfo.path;
					alarmInfo.AlarmAudioCover = recordAudioInfo.ThemePicpath;
				}
			} else {*/
				DownloadAudioInfo downloadAudioInfo = AudioDAO
						.getDownloadAudioInfoByPath(mContext,
								alarmInfo.AlarmAudio);
				if (downloadAudioInfo != null) {
					alarmInfo.AlarmUserId = downloadAudioInfo.AudioUserId;
					alarmInfo.AlarmTitle = downloadAudioInfo.title;
					alarmInfo.AlarmAudio = downloadAudioInfo.path;
					alarmInfo.AlarmAudioCover = downloadAudioInfo.AudioCover;

					alarmInfo.AlarmProfile = downloadAudioInfo.AudioUserProfile;
					alarmInfo.AudioKey = downloadAudioInfo.AudioKey;
					alarmInfo.AudioCoverKey = downloadAudioInfo.AudioCoverKey;
					alarmInfo.UserProfileKey = downloadAudioInfo.UserProfileKey;

					alarmInfo.AlarmUserNickname = downloadAudioInfo.AudioUserNickName;
				}
//			}

			alarmData.add(alarmInfo);
		}

		if (alarmAdapter != null) {
			alarmAdapter.setDate(alarmData);//notifyDataSetChanged();
		}
	}

	private void updateAlarmAudioType(ListAlarmTime alarmInfo) {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(mContext);
		SQLiteDatabase db = helper.getWritableDatabase();
		AlarmDBOperate alarmDBOperate = new AlarmDBOperate(db,
				MyTabList.ALARM_TIME);
		alarmDBOperate.updateAwakeAlarm(mContext,
				String.valueOf(alarmInfo.AlarmID), alarmInfo);

		AudioDAO audioDAO = new AudioDAO(db, MyTabList.DOWNLOAD_AUDIO);
		audioDAO.updateDownloadInfo(mContext, alarmInfo.AlarmAudio, 0);
	}

	/**
	 * 获取智能提醒的闹钟
	 */
	public void getRemindAlarm() {
		SharedPreferences sp = mContext.getApplicationContext()
				.getSharedPreferences(SleepInfo.SLEEP_SETTIME,
						mContext.getApplicationContext().MODE_APPEND);
		long value = sp.getLong(SleepInfo.NOTIFICATION_TIME,
				SleepInfo.NOTIFICATION_SPACE);
		int forwdMinut = (int) (value / (60 * 1000));
		String sleepTimeSetting = PreManager.instance().getSleepTime_Setting(
				mContext);
		int totalMinut = SleepUtils.getHourValue(sleepTimeSetting) * 60
				+ SleepUtils.getMinutValue(sleepTimeSetting);
		int remidMinute = totalMinut - forwdMinut;
		if (remidMinute > 0) {
		} else {
			remidMinute += 24 * 60;
		}
	}

	private MyDialog alarmSuccDialog;
	private CheckBox cb;

	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("com.xc.alarm.add.ALARM_SAVE_SUCCESS")) {
//				initRemindPublishButton();
				getAwakeAlarm();
//				if (PreManager.instance().getShowAlarmExplain(mContext)
//						&& alarmSuccDialog == null) {
//					// if(null != alarmSuccDialog){
//					alarmSuccDialog = new MyDialog(mContext, 0, 0,
//							R.layout.dialog_save_alarm_success, 0,
//							Gravity.CENTER, 0.96f, 0.0f);
//					cb = (CheckBox) alarmSuccDialog
//							.findViewById(R.id.cb_no_more);
//					alarmSuccDialog.findViewById(R.id.btn_i_know)
//							.setOnClickListener(new OnClickListener() {
//
//								@Override
//								public void onClick(View v) {
//									if (alarmSuccDialog != null && cb != null) {
//										if (cb.isChecked()) {
//											PreManager.instance()
//													.saveShowAlarmExplain(
//															mContext, false);
//										}
//										alarmSuccDialog.cancel();
//									}
//								}
//							});
//					alarmSuccDialog.show();
//				}
			} else if (action.equals("com.xc.alarm.add.ALARM_UPDATE_SUCCESS")) {
				// System.out.println("alarm--->ALARM_UPDATE_SUCCESS");
				getAwakeAlarm();
			}
		}

	}

	/**
	 * 注册广播 com.xc.alarm.add.ALARM_SAVE_SUCCESS 闹钟保存成功
	 * com.xc.alarm.add.ALARM_UPDATE_SUCCESS 闹钟更新成功
	 */
	private void registerReceiver() {
		myReceiver = new MyReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.xc.alarm.add.ALARM_SAVE_SUCCESS");
		intentFilter.addAction("com.xc.alarm.add.ALARM_UPDATE_SUCCESS");
		mContext.registerReceiver(myReceiver, intentFilter);
	}
}
