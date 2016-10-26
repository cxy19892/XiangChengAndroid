package com.yzm.sleep.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.background.DataUtils;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.background.MytabOperate;
import com.yzm.sleep.background.SignInDBOperation;
import com.yzm.sleep.background.SleepInfo;
import com.yzm.sleep.bean.SleepCaseBean;
import com.yzm.sleep.model.RollPickerDialog;
import com.yzm.sleep.model.RollPickerDialog.RollCallBack;
import com.yzm.sleep.model.SignInData;
import com.yzm.sleep.model.SmartRemindBean;
import com.yzm.sleep.tools.DealSetTimeUtils;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSetSleepTimeCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.SetSleepTimeParams;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.SmartNotificationUtil;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;

/**
 * 设置睡眠时间
 */
@SuppressLint("SimpleDateFormat")
public class SetSleepTimeActivity extends BaseActivity {
	
	private CheckBox cbRemind;
	private TextView getupTime, sleepLength;
	private RollPickerDialog dialog;
	private String sleepDate/*入睡时间*/,getupDate/*起床时间*/;
	private SharedPreferences sp;
	private ArrayList<String> listM, listH;
	private String length1, length2;
	private List<SleepCaseBean> resuldatas;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setsleep_time);
		findViewById(R.id.back).setOnClickListener(this);
		((TextView)findViewById(R.id.title)).setText("设置");
		listM = new ArrayList<String>();
		listH = new ArrayList<String>();
		resuldatas = new ArrayList<SleepCaseBean>();
		initView();
		inidDialogData();
	}
	
	private void inidDialogData(){
		for (int i = 0; i < 13; i++) 
			listH.add((i < 10 ? "0"+i : i)+"小时");
		for (int i = 0; i < 60; i++) 
			listM.add((i < 10 ? "0"+i : i)+"分");
	}
	
	private void initView(){
		getupDate=PreManager.instance().getGetupTime_Setting(this);
		sleepDate=PreManager.instance().getSleepTime_Setting(this);
		cbRemind = (CheckBox) findViewById(R.id.cb_remind);
		cbRemind.setOnClickListener(this);
		getApplicationContext();
		sp = getApplicationContext().getSharedPreferences(SleepInfo.SLEEP_SETTIME, Context.MODE_APPEND);
		cbRemind.setChecked("".equals(sp.getString(SleepInfo.REMIND_BEFORE_SLEEP, "")));

		findViewById(R.id.rl_remind).setOnClickListener(this);
		findViewById(R.id.rl_getuptime).setOnClickListener(this);
		findViewById(R.id.rl_sleeplenght).setOnClickListener(this);
		sleepLength = (TextView) findViewById(R.id.setsleep_lenght);
		getupTime = (TextView) findViewById(R.id.setgetup_time);
		getupTime.setText(getupDate);
		int sleepLenghtM = countLengthByTime(sleepDate, getupDate);
		length1 = (sleepLenghtM/60 < 10 ? ("0"+sleepLenghtM/60):sleepLenghtM/60) +"小时";
		length2 = (sleepLenghtM%60 < 10 ? ("0"+sleepLenghtM%60):sleepLenghtM%60)+"分";
		sleepLength.setText(length1 + length2);
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
	
	private void complete(){
		try{
			String getupHore=getupDate.split(":")[0],getupMin=getupDate.split(":")[1];
			String sleepLength = new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH小时mm分").parse(length1 + length2).getTime());
			int getupH=Integer.parseInt(getupHore);
			int getupM=Integer.parseInt(getupMin);
			int sleepMinH=Integer.parseInt(sleepLength.split(":")[0]);
			int sleepMinM=Integer.parseInt(sleepLength.split(":")[1]);
			//提交到服务器
			SetSleepTimeParams mParams = new SetSleepTimeParams();
			mParams.my_int_id = PreManager.instance().getUserId(this);
			mParams.wakeup = getupDate;
			mParams.sleeplong = String.valueOf(sleepMinH*60 + sleepMinM);
			new XiangchengMallProcClass(this).setSleepTime(mParams, new InterfaceSetSleepTimeCallBack() {
				@Override
				public void onSuccess(String icode, List<SleepCaseBean> datas) {
					resuldatas = datas;
				}
				@Override
				public void onError(String icode, String strErrMsg) {
				}
			});
			
			int tempM=getupM-sleepMinM;
			if(tempM<0){
				getupH-=1;
				tempM=tempM+60;
			}
			int tempH=getupH-sleepMinH;
			if(tempH<0){
				tempH=tempH+24;
			}
			//搬移的以前的代码
			//保存旧区间
			long oldt11 = DataUtils.getAllerateStartTime() / (60*1000);
			long oldt21 = DataUtils.getAllerateStopTime() / (60*1000);
			long oldt1 = DataUtils.getStartTime();
			long oldt2 = DataUtils.getStopTime();
			PreManager.instance().saveOldT1(this, oldt1);
			PreManager.instance().saveOldT2(this, oldt2);
			
			//处理新区间
			PreManager.instance().saveSleepTime_Setting(this,(tempH<10?("0"+tempH):tempH)+":"+(tempM<10?("0"+tempM):tempM));
			PreManager.instance().saveGetupTime_Setting(this,getupDate);
		
			//从上班模式中获取数据并保存数据
			final int startTime =tempH * 60 + tempM;
			final int endTime =Integer.parseInt(getupHore) * 60 + getupM;
			setTime(startTime,endTime);
			try{
				Util.show(this, "正在重新设置时间，请稍等");
				String date = DataUtils.getRecordDate(new Date());
				long newt1 = DataUtils.getAllerateStartTime() / (60*1000);
				long newt2 = DataUtils.getAllerateStopTime() / (60*1000);
				DealSetTimeUtils.getInstance().setTime(this, date, System.currentTimeMillis(), oldt11, oldt21, newt1, newt2);
			}catch(Exception e) {
			}
		}catch(Exception e){}
	}

	/**
	 * 设置时间
	 * @param startTime
	 * @param endTime
	 */
	private void setTime(int startTime,int endTime) {
		getSleepDataAndSetSmartAlarm(1);
		//第一次安装设定时间
		SleepInfo.SET_STARTTIME = startTime;
		SleepInfo.SET_ENDTIME = endTime;
		
		SharedPreferences sp = getSharedPreferences(SleepInfo.SLEEP_SETTIME, MODE_APPEND);
		Editor edit = sp.edit();
		edit.putString(SleepInfo.STARTTIME, startTime+"");
		edit.putString(SleepInfo.ENDTIME, endTime + "");
		edit.commit();
		
		//将数据保存到本地数据库
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(this.getApplicationContext());
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPTIME);
		String str;
		try {
			String date = DataUtils.getRecordDate(new Date());
			str = operate.query("date", "date = ?",new String[]{date} );
			if("".equals(str)) {
				ContentValues cv = new ContentValues();
				cv.put("date", date);
				cv.put("starttime", SleepInfo.SET_STARTTIME);
				cv.put("endtime", SleepInfo.SET_ENDTIME);
				operate.insert(cv);
			}else {
				ContentValues cv = new ContentValues();
				cv.put("date", date);
				cv.put("starttime", SleepInfo.SET_STARTTIME);
				cv.put("endtime", SleepInfo.SET_ENDTIME);
				cv.put("orgsleeptime","");
				cv.put("orguptime","");
				operate.update(cv, "date = ?",new String[]{date});
			}
		} catch (Exception e) {
		} finally {
			if(operate != null) {
				operate.close();
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			Gson gson = new Gson();
			setResult(RESULT_OK, getIntent().putExtra("resule", gson.toJson(resuldatas)));
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.cb_remind:
		case R.id.rl_remind:
			cbRemind.setChecked(!cbRemind.isChecked());
			if (cbRemind.isChecked()) {
				openTheNightRemind();
				getSleepDataAndSetSmartAlarm(1);
			} else {
				closeTheNightRemind();
			}
			break;
		case R.id.rl_getuptime:
			dialog=new RollPickerDialog(this, new RollCallBack() {
				
				@Override
				public void setResaultRoll(int select1, int select2, int select3) {
					String temp = (select1 < 10 ? "0" + select1 : select1)+":" +(select2 < 10 ? "0" + select2 : select2);
					if(!temp.equals(getupDate)){
						getupDate = temp;
						getupTime.setText(getupDate);
						complete();
					}
				}

				@Override
				public void setResaultRollString(String select1,String select2, String select3) {
				}
			});
			int hourset=0, miunset=0;
			try {
				String[] date=getupDate.split(":");
				hourset=Integer.parseInt(date[0]);
				miunset=Integer.parseInt(date[1]);
			} catch (Exception e) {
			}
					
			dialog.SetData(1, "起床时间", new int[]{0, 23}, hourset, new int[]{0, 59}, miunset, null, 0);
			dialog.show();
			break;
		case R.id.rl_sleeplenght:
			dialog=new RollPickerDialog(this, new RollCallBack() {
				
				@Override
				public void setResaultRoll(int select1, int select2, int select3) {
				}

				@Override
				public void setResaultRollString(String select1,String select2, String select3) {
					String temp = select1 + select1;
					if(!temp.equals(length1 + length2)){
						length1 = select1;
						length2 = select2;
						sleepLength.setText(length1 + length2);
						complete();
					}
				}
			});
			
			int hof = 0, mif = 0;
			for (int i = 0; i < listH.size(); i++) {
				if(length1.equals(listH.get(i))){
					hof = i;
					break;
				}
			}
			for (int i = 0; i < listM.size(); i++) {
				if(length2.equals(listM.get(i))){
					mif = i;
					break;
				}
			}
			
			dialog.SetStrDate(2, "选择睡眠时长", listH, hof, listM, mif, null, 0, true);
			dialog.show();
			break;
		default:
			break;
		}
	}

	/**
	 * @param remind_style   0 午间提醒 1 睡前提醒
	 */
	@SuppressLint("SimpleDateFormat")
	private void getSleepDataAndSetSmartAlarm(int remind_style) {
		// 判断是否开启智能闹钟
		// 如果开启则则去取得提醒然后设置提醒时间
		SmartRemindBean mSmartRemindBean = null;
		SharedPreferences sp = this.getApplicationContext().getSharedPreferences(SleepInfo.SLEEP_SETTIME, getApplicationContext().MODE_APPEND);
		String startTime = "";
		String endTime = "";
		String suggestSleepTime = PreManager.instance().getSleepTime_Setting(this);
		String remind_time_style = sp.getString(SleepInfo.REMIND_BEFORE_SLEEP, "");
		if(remind_time_style.equals("")){
			SignInData mSignInData= SignInDBOperation.initDB(this).querySignInData(TimeFormatUtil.getYesterdayTime(), 
					TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(this))?"0":"1");
			if(!TextUtils.isEmpty(mSignInData.getTrySleepTime()) && !TextUtils.isEmpty(mSignInData.getWakeUpTime())){
				try {
					startTime = TimeFormatUtil.formatTime(Long.parseLong(mSignInData.getTrySleepTime()), "HH:mm");
					endTime = TimeFormatUtil.formatTime(Long.parseLong(mSignInData.getWakeUpTime()), "HH:mm");
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(startTime.equals("")||endTime.equals("")){
				startTime = sp.getString(SleepInfo.STARTTIME, "");
				endTime = sp.getString(SleepInfo.ENDTIME, "");

				if(startTime.equals("")||endTime.equals("")){
					mSmartRemindBean = SmartNotificationUtil.GetSmartNotifications(remind_style, PreManager.instance().getGetupTime_Setting(this), PreManager.instance().getSleepTime_Setting(this), suggestSleepTime);
				}else{
					int start_time = Integer.parseInt(startTime);
					int end_time = Integer.parseInt(endTime);
					startTime = start_time/60+":"+(start_time%60 == 0?"00":start_time%60);
					endTime = end_time/60+":"+(end_time%60== 0?"00":end_time%60);
					
					mSmartRemindBean = SmartNotificationUtil.GetSmartNotifications(remind_style, endTime, startTime, suggestSleepTime);
				}

			}else{
				mSmartRemindBean = SmartNotificationUtil.GetSmartNotifications(remind_style, endTime, startTime, suggestSleepTime);
			}
		}
		if (mSmartRemindBean != null
				&& mSmartRemindBean.SuggestRemindTime != null) {
			// 重新设置睡觉提醒时间
			LogUtil.d("chen", "睡前提醒 SuggestRemindTime = "+mSmartRemindBean.SuggestRemindTime +"\n" +mSmartRemindBean.Remindmsgs);
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
			/*String datee = TimeFormatUtil.getTodayTime() + " "
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
			}*/
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
//		calendar.set(Calendar.MILLISECOND, 0);
		calendar.setTimeInMillis(remindtime);
		long remindTime = calendar.getTimeInMillis();
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, remindTime, sender);
	}
	
	
	private static int countLengthByTime(String sleepTime, String getUpTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		try {
			int sleepTimeLong = (int) sdf.parse(sleepTime).getTime();
			int getUpTimeLong = (int) sdf.parse(getUpTime).getTime();
			int totalTime = getUpTimeLong - sleepTimeLong;
			if(totalTime < 0)
				totalTime += 24 * 60 * 60 * 1000;
			return totalTime/(60 * 1000);       
		} catch (Exception e) {
		}
		return 0;
	}
}
