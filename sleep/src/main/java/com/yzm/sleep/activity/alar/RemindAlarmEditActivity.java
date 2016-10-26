package com.yzm.sleep.activity.alar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.background.AlarmDBOperate;
import com.yzm.sleep.background.AlarmService.ListAlarmTime;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.model.DialogFragmentWindow;
import com.yzm.sleep.model.RingtoneInfo;
import com.yzm.sleep.model.DialogFragmentWindow.OnMusicSelectedListener;
import com.yzm.sleep.model.RollPickerDialog;
import com.yzm.sleep.model.RollPickerDialog.RollCallBack;
import com.yzm.sleep.utils.AlarmConstants;
import com.yzm.sleep.utils.MyTextWatcher;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.SleepUtils;
import com.yzm.sleep.utils.ToastManager;

public class RemindAlarmEditActivity extends BaseActivity implements  OnMusicSelectedListener, RollCallBack{

	//titlebar
	private Button btnLeft, btnRight;
	private TextView title;
	
	private LinearLayout LinRemindTime, linRemindRings, linRemindContext;
	private TextView tvRemindTime , tvRingsName;
	private EditText EdtRemindTitle, edtRemindConten;
	private RollPickerDialog selectDialog;
	private String reMindDate = "00:00";
	private CheckBox cb_monday, cb_tuesday, cb_wednesday, cb_thursday,
	cb_friday, cb_saturday, cb_sunday;
	private boolean isEdit = false;
	private boolean isAlarm= false;
	private ProgressUtils pb;
	
	//====================闹钟信息===================
	private ArrayList<ListAlarmTime> alarmData = new ArrayList<ListAlarmTime>();
	private RingtoneInfo currentRingtoneInfo ;
	private ListAlarmTime alarmInfo;
	private ListAlarmTime mAlarm = new ListAlarmTime();
	//====================闹钟信息===================
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remind_alarm_edit);
		Intent i = getIntent();
		currentRingtoneInfo = MyApplication.instance().getCurrentSelectRingInfo();
		isEdit = i.getBooleanExtra(AlarmConstants.IS_EDIT, false);
		
		if(isEdit){
			mAlarm = (ListAlarmTime) i.getSerializableExtra("Alarm");
			if(mAlarm.isRemind == 1){
				isAlarm = false;
			}else{
				isAlarm = true;
			}
		}else {
			isAlarm = false;
		}
		
		if(isAlarm){
			alarmInfo = (ListAlarmTime)i.getSerializableExtra("Alarm");
		}else{
			
		}
		Initviews();
		initData();
	}

	private void Initviews() {
		btnLeft = (Button) findViewById(R.id.back);
		btnRight= (Button) findViewById(R.id.btn_title_right);
		title   = (TextView) findViewById(R.id.title);
		
		LinRemindTime = (LinearLayout) findViewById(R.id.lin_remind_time);
		linRemindRings= (LinearLayout) findViewById(R.id.lin_remind_rins);
		linRemindContext = (LinearLayout) findViewById(R.id.lin_remind_content);
		tvRemindTime  = (TextView) findViewById(R.id.tv_remind_time);
		EdtRemindTitle= (EditText) findViewById(R.id.edt_remind_title);
		edtRemindConten= (EditText) findViewById(R.id.edt_reind_content);
		
		cb_sunday = (CheckBox) findViewById(R.id.cb_sunday);
		cb_monday = (CheckBox) findViewById(R.id.cb_monday);
		cb_tuesday = (CheckBox) findViewById(R.id.cb_tuesday);
		cb_wednesday = (CheckBox) findViewById(R.id.cb_wednesday);
		cb_thursday = (CheckBox) findViewById(R.id.cb_thursday);
		cb_friday = (CheckBox) findViewById(R.id.cb_friday);
		cb_saturday = (CheckBox) findViewById(R.id.cb_saturday);
		tvRingsName = (TextView) findViewById(R.id.tv_remind_rings_name);
		
		LinRemindTime.setOnClickListener(this);
		linRemindRings.setOnClickListener(this);
		btnLeft.setOnClickListener(this);
		btnRight.setOnClickListener(this);
		title.setText("事项编辑");
		
		reMindDate = getTodayTime();
		tvRemindTime.setText(reMindDate);
		
		btnRight.setVisibility(View.VISIBLE);
		btnRight.setCompoundDrawables(null, null, null, null);
		if(isAlarm){
			linRemindRings.setVisibility(View.VISIBLE);
			linRemindContext.setVisibility(View.GONE);
			btnRight.setText("完成");
		}else{
			linRemindRings.setVisibility(View.GONE);
			linRemindContext.setVisibility(View.VISIBLE);
			btnRight.setText("完成");
		}
		
		EdtRemindTitle.addTextChangedListener(new MyTextWatcher(EdtRemindTitle, this, 5, null));
		edtRemindConten.addTextChangedListener(new MyTextWatcher(edtRemindConten, this, 20, null));
		if(isAlarm){
			EdtRemindTitle.setText(TextUtils.isEmpty(alarmInfo.remindTitle)?(alarmInfo.AlarmID==1?"睡觉提醒":"早起提醒"):alarmInfo.remindTitle);
		}
	}
	
	//获取今天的日期
		private String getTodayTime(){
			SimpleDateFormat formatter = new SimpleDateFormat ("HH:mm");
			Date curDate = new Date(System.currentTimeMillis());//获取当前时间
			return formatter.format(curDate);
		}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			AppManager.getAppManager().finishActivity();
			overridePendingTransition(R.anim.alpha_in, R.anim.dialog_bottom_out);
			return true;
		}else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lin_remind_time:
			
			selectDialog=new RollPickerDialog(this, this); 
			String timeString = tvRemindTime.getText().toString();
			int hourset=0,miunset=0;
			try {
				hourset = Integer.parseInt(timeString.split(":")[0]);
				miunset = Integer.parseInt(timeString.split(":")[1]);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			selectDialog.SetData(1, "时间", new int[]{0 , 23}, hourset, new int[]{0 , 59}, miunset, null, 0);//Data(1, "时间", hourData, hourset, minData, miunset, null, 0, this);
			selectDialog.show();
			break;
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			overridePendingTransition(R.anim.alpha_in, R.anim.dialog_bottom_out);
			break;
		case R.id.btn_title_right:
			if(isAlarm){
				if(!TextUtils.isEmpty(EdtRemindTitle.getText().toString())){
					getCurrentAlarmInfo();
				}else{
					toastMsg("请输入提醒事项标题");
				}
			}else{
				if(!TextUtils.isEmpty(edtRemindConten.getText().toString()) && !TextUtils.isEmpty(EdtRemindTitle.getText().toString())){
					getCurrentAlarmInfo();
				}else if(!TextUtils.isEmpty(edtRemindConten.getText().toString())){
					toastMsg("请输入提醒事项标题");
				}else {
					toastMsg("请输入提醒事项内容");
				}
			}

			break;
		case R.id.lin_remind_rins:
			new DialogFragmentWindow().show(getSupportFragmentManager(), "");
			break;

		default:
			break;
		}
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	public void setResaultRoll(int select1, int select2, int select3) {
		StringBuffer sb = new StringBuffer();
		if(select1<10)
			sb.append("0").append(select1);
		else 
			sb.append(select1);
		sb.append(":");
		if(select2<10)
			sb.append("0").append(select2);
		else 
			sb.append(select2);
		tvRemindTime.setText(sb);
		reMindDate=sb.toString();
	}

//	@Override
//	public void onClick(int type, String select1, String select2, String select3) {
//		if(type==1){
//			tvRemindTime.setText(select1+":"+select2);
//			reMindDate=(select1+":"+select2);
//		}
//		if(selectDialog!=null){
//			selectDialog.dismiss();
//			selectDialog=null;
//		}
//	}
	
	
	
	void initData() {
		
		if (isEdit) {
			//设置重复周期
			if (mAlarm.AlarmRepeat == 1) {
				if (mAlarm.AlarmPlant.contains("1")) {
					cb_monday.setChecked(true);
				}else {
					cb_monday.setChecked(false);
				}
				if (mAlarm.AlarmPlant.contains("2")) {
					cb_tuesday.setChecked(true);
				}else {
					cb_tuesday.setChecked(false);
				}
				if (mAlarm.AlarmPlant.contains("3")) {
					cb_wednesday.setChecked(true);
				}else {
					cb_wednesday.setChecked(false);
				}
				if (mAlarm.AlarmPlant.contains("4")) {
					cb_thursday.setChecked(true);
				}else {
					cb_thursday.setChecked(false);
				}
				if (mAlarm.AlarmPlant.contains("5")) {
					cb_friday.setChecked(true);
				}else {
					cb_friday.setChecked(false);
				}
				if (mAlarm.AlarmPlant.contains("6")) {
					cb_saturday.setChecked(true);
				}else {
					cb_saturday.setChecked(false);
				}
				if (mAlarm.AlarmPlant.contains("7")) {
					cb_sunday.setChecked(true);
				}else {
					cb_sunday.setChecked(false);
				}
			}
			reMindDate = mAlarm.AlarmTime;
			tvRemindTime.setText(mAlarm.AlarmTime);
			if(null!=mAlarm.remindTitle)
				EdtRemindTitle.setText(mAlarm.remindTitle);
			if(isAlarm){
				tvRingsName.setText(mAlarm.AlarmTitle);
			}else{
				if(null != mAlarm.remindContext)
					edtRemindConten.setText(mAlarm.remindContext);
			}
		}
	}
	//===================================闹钟=================================================
	/**
	 * 读取闹钟信息
	 */
	public void getAwakeAlarm() {
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				alarmData.clear();
				MyDatabaseHelper helper = MyDatabaseHelper.getInstance(RemindAlarmEditActivity.this
						.getApplicationContext());
				SQLiteDatabase db = helper.getWritableDatabase();
				AlarmDBOperate alarmDBOperate = new AlarmDBOperate(db,
						MyTabList.ALARM_TIME);
				alarmData.addAll((ArrayList<ListAlarmTime>) alarmDBOperate
						.GetALarmData());
				db.close();
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
			}
		};
		task.execute();
	}
	
	/**
	 * 提交的时候
	 * 获取当前的闹钟信息
	 */
	private void getCurrentAlarmInfo() {
		/*1.叫醒时间*/
		String AlarmTime = "";
		AlarmTime = reMindDate;
		
		/*2.是否重复- 0表示不重复， 1表示重复*/
		int AlarmRepeat = 0;
		
		/*3.重复周期*/
		String AlarmPlant = "";
		StringBuffer sb = new StringBuffer();
		if (cb_monday.isChecked()) {
			sb.append(AlarmConstants.MONDAY);
			sb.append(",");
		}
		if (cb_tuesday.isChecked()) {
			sb.append(AlarmConstants.TUESDAY);
			sb.append(",");
		}
		if (cb_wednesday.isChecked()) {
			sb.append(AlarmConstants.WEDNESDAY);
			sb.append(",");
		}
		if (cb_thursday.isChecked()) {
			sb.append(AlarmConstants.THURSDAY);
			sb.append(",");
		}
		if (cb_friday.isChecked()) {
			sb.append(AlarmConstants.FRIDAY);
			sb.append(",");
		}
		if (cb_saturday.isChecked()) {
			sb.append(AlarmConstants.SATURDAY);
			sb.append(",");
		}
		if (cb_sunday.isChecked()) {
			sb.append(AlarmConstants.SUNDAY);
			sb.append(",");
		}
		AlarmPlant = sb.toString();
		if (TextUtils.isEmpty(AlarmPlant)) {
			AlarmRepeat = 0;	//不重复
		}else {
			AlarmRepeat = 1;	//重复
			AlarmPlant = AlarmPlant.substring(0,AlarmPlant.lastIndexOf(","));
		}
		/*4.闹钟标题*/
		String AlarmTitle = currentRingtoneInfo.title;
		/*5.不重复时闹钟启动日期*/
		String AlarmDay = "";
				
		if (AlarmRepeat == 0) {//如果闹钟不重复
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(reMindDate.split(":")[0]));
			calendar.set(Calendar.MINUTE, Integer.parseInt(reMindDate.split(":")[1]));
			if (calendar.getTimeInMillis()// 如果闹钟设置时间还没过期
					- System.currentTimeMillis() > 0) {
				AlarmDay = SleepUtils.getFormatedDateTime("yyyy-MM-dd", calendar.getTimeInMillis());
			} else {// 闹钟时间过期
				AlarmDay = SleepUtils.getFormatedDateTime("yyyy-MM-dd", calendar.getTimeInMillis() + 24*60*60*1000);
			}
		}
		/*6.小睡时间  为0时表示不小睡*/
		int AlarmDelay = 0;
		/*7.闹钟铃声本地路径*/
		String AlarmAudio = currentRingtoneInfo.ringtonePath;
		/*8.闹启动开关 -0 表示未启动  1表示启动*/
		int AlarmOnOff = 1;
		
		/*9.铃声对应用户头像*/
		String AlarmProfile = currentRingtoneInfo.profile;
		/*10.铃声对应用户昵称*/
		String AlarmUserNickname = currentRingtoneInfo.nickname;
		String AlarmUserId = currentRingtoneInfo.int_id;
		/*11.铃声历史下载次数*/
		int AlarmDownloads = currentRingtoneInfo.downloads;
		
		ListAlarmTime m_ListAlarmTime = new ListAlarmTime();
		m_ListAlarmTime.AlarmTime = AlarmTime;
		m_ListAlarmTime.AlarmRepeat = AlarmRepeat;
		m_ListAlarmTime.AlarmPlant = AlarmPlant;
		m_ListAlarmTime.AlarmTitle = AlarmTitle;
		m_ListAlarmTime.AlarmDay = AlarmDay;
		m_ListAlarmTime.AlarmDelay = AlarmDelay;
		
		m_ListAlarmTime.AlarmAudio = AlarmAudio;
		m_ListAlarmTime.AlarmOnOff = AlarmOnOff;
		m_ListAlarmTime.AlarmProfile = AlarmProfile;
		m_ListAlarmTime.AlarmUserNickname = AlarmUserNickname;
		m_ListAlarmTime.AlarmUserId = AlarmUserId;
		m_ListAlarmTime.AlarmDownloads = AlarmDownloads;
		m_ListAlarmTime.AlarmAudioCover = currentRingtoneInfo.themePicString;
		if (currentRingtoneInfo.coverKey != null) {
			m_ListAlarmTime.AudioCoverKey = currentRingtoneInfo.coverKey;
		}else {
			m_ListAlarmTime.AudioCoverKey  = currentRingtoneInfo.themePicString;
		}
		
		if (alarmInfo != null) {
			m_ListAlarmTime.AlarmType = alarmInfo.AlarmType;
		}
		
		
		if (currentRingtoneInfo.isLocalRecord) {
			m_ListAlarmTime.AudioIsLocalRecord = 1;
		}else {
			m_ListAlarmTime.AudioIsLocalRecord = 0;
		}
		
		//===========================
		if(isAlarm){
			m_ListAlarmTime.isRemind = 0;
			m_ListAlarmTime.remindContext = "";
			m_ListAlarmTime.remindTitle = EdtRemindTitle.getText().toString();
		}else{
			m_ListAlarmTime.isRemind = 1;
			m_ListAlarmTime.remindContext = edtRemindConten.getText().toString();
			m_ListAlarmTime.remindTitle = EdtRemindTitle.getText().toString();
		}
		if (!isEdit) {//是新建闹钟
			saveAwakeAlarm(m_ListAlarmTime);
		}else {//编辑闹钟 直接更新闹钟
			updateAwakeAlarm(mAlarm.AlarmID+"", m_ListAlarmTime);
		}
	}
		
	/**
	 * 保存闹钟信息到数据库
	 */
	private void saveAwakeAlarm(final ListAlarmTime m_ListAlarmTime) {
		pb = new ProgressUtils(RemindAlarmEditActivity.this);
		pb.setCanceledOnTouchOutside(false);
		pb.setMessage("正在保存闹钟");
		pb.show();
		AsyncTask<Void, Void, Void> task  = new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... params) {
				MyDatabaseHelper helper = MyDatabaseHelper.getInstance(getApplicationContext());
				SQLiteDatabase db = helper.getWritableDatabase();
				AlarmDBOperate alarmDBOperate = new AlarmDBOperate(db, MyTabList.ALARM_TIME);
				//设置闹钟类型
				m_ListAlarmTime.AlarmType = 0;//非专属铃音
				
				alarmDBOperate.saveAwakeAlarm(RemindAlarmEditActivity.this,m_ListAlarmTime);
				db.close();
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				if(isAlarm){
					ToastManager.getInstance(RemindAlarmEditActivity.this).show("闹钟保存成功");
				}else {
					ToastManager.getInstance(RemindAlarmEditActivity.this).show("提醒事项保存成功");
				}
				
//				AppManager.getAppManager().finishAllActivityExceptOne(HomeActivity.class);
//				startActivity(new Intent(RemindAlarmEditActivity.this, AlarmActivity.class));
//				overridePendingTransition(R.anim.alpha_in, R.anim.dialog_bottom_out);
				if (pb != null) {
					pb.cancel();
				}
				//发送新建闹钟完成广播，弹出闹钟说明对话框
				Intent intent = new Intent();
				intent.setAction("com.xc.alarm.add.ALARM_SAVE_SUCCESS");
				sendBroadcast(intent);
				AppManager.getAppManager().finishActivity();
				overridePendingTransition(R.anim.alpha_in, R.anim.dialog_bottom_out);
			}
		};
			task.execute();
	}
	
	/**
	 * 更新闹钟信息
	 * @param id
	 * @param m_ListAlarmTime
	 */
	private void updateAwakeAlarm(final String id,final ListAlarmTime m_ListAlarmTime) {
		AsyncTask<Void, Void, Void> task  = new AsyncTask<Void, Void, Void>(){
			
			@Override
			protected Void doInBackground(Void... params) {
				MyDatabaseHelper helper = MyDatabaseHelper.getInstance(getApplicationContext());
				SQLiteDatabase db = helper.getWritableDatabase();
				AlarmDBOperate alarmDBOperate = new AlarmDBOperate(db, MyTabList.ALARM_TIME);
				alarmDBOperate.updateAwakeAlarm(RemindAlarmEditActivity.this,id, m_ListAlarmTime);
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				if(isAlarm){
					ToastManager.getInstance(RemindAlarmEditActivity.this).show("闹钟修改成功");
				}else{
					ToastManager.getInstance(RemindAlarmEditActivity.this).show("修改成功");
				}
				Intent intent = new Intent();
				intent.setAction("com.xc.alarm.add.ALARM_UPDATE_SUCCESS");
				sendBroadcast(intent);
//				AppManager.getAppManager().finishAllActivityExceptOne(HomeActivity.class);
//				startActivity(new Intent(RemindAlarmEditActivity.this, AlarmActivity.class));
//				overridePendingTransition(R.anim.alpha_in, R.anim.dialog_bottom_out);
				AppManager.getAppManager().finishActivity();
				overridePendingTransition(R.anim.alpha_in, R.anim.dialog_bottom_out);
			}
		};
		task.execute();
	}
	//======================================闹钟=============================================

	@Override
	public void onMusicSelected(RingtoneInfo mRingtoneInfo) {
		if(mRingtoneInfo != null){
			currentRingtoneInfo = mRingtoneInfo;
			MyApplication.instance().setCurrentSelectRingInfo(mRingtoneInfo);
			tvRingsName.setText(currentRingtoneInfo.title);
		}
	}

	@Override
	public void setResaultRollString(String select1, String select2,
			String select3) {
		// TODO Auto-generated method stub
		
	}


}
