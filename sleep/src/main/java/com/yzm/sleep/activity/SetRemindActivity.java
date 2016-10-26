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
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.background.DataUtils;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.background.MytabOperate;
import com.yzm.sleep.background.SignInDBOperation;
import com.yzm.sleep.background.SleepInfo;
import com.yzm.sleep.bean.SleepStatusBean;
import com.yzm.sleep.model.RollPickerDialog;
import com.yzm.sleep.model.RollPickerDialog.RollCallBack;
import com.yzm.sleep.model.SignInData;
import com.yzm.sleep.model.SmartRemindBean;
import com.yzm.sleep.render.GetSleepAvgTimeValueClass;
import com.yzm.sleep.render.GetSleepAvgTimeValueClass.AvgTimeResult;
import com.yzm.sleep.render.GetSleepAvgTimeValueClass.GetAvgTimeParamItem;
import com.yzm.sleep.tools.DealSetTimeUtils;
import com.yzm.sleep.utils.CalenderUtil;
import com.yzm.sleep.utils.GetTimeParamItem;
import com.yzm.sleep.utils.GetTimeParamItem.GetTimeResult;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetSignInDataCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetSignInDataParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.UpLoadSetTimeCallback;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.SmartNotificationUtil;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;

/**
 * 设置提醒
 * @author Administrator
 * @params dataType 1软件 ; 2硬件
 */
@SuppressLint("SimpleDateFormat")
public class SetRemindActivity extends BaseActivity {
	
	private Context mContext;
	private CheckBox cbRemind;
	private TextView getupTime, sleepTime;
	private RollPickerDialog dialog;
	private String sleepDate/*入睡时间*/,getupDate/*起床时间*/;
	private TextView averageGetup, averageSleep, setremindHint;
	private ArrayList<SoftDayData> softList;
	private AvgTimeResult avgResult; //计算平均
	private GetTimeResult getTimeResult;// 最早 最迟  返回数据
	private int hasDataCount;
	private SharedPreferences sp;
	private ProgressUtils pro;
	private String type;
	
	/**
	 * 显示进度
	 */
	private void showPro(){
		if(pro==null){
			pro = new ProgressUtils(this);
		}
		pro.show();
	}
	
	/**
	 * 取消进度
	 */
	private void cancelPro(){
		if(pro!=null){
			pro.dismiss();
			pro=null;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setremind);
		findViewById(R.id.back).setOnClickListener(this);
		this.mContext = this;
		softList = new ArrayList<SoftDayData>();
		((TextView)findViewById(R.id.title)).setText("设置提醒");
		if(TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(this)))
			type = "1";
		else
			type = "2";
		initView();
	}
	
	private void initView(){
		getupDate=PreManager.instance().getGetupTime_Setting(this);
		sleepDate=PreManager.instance().getSleepTime_Setting(this);
		cbRemind = (CheckBox) findViewById(R.id.cb_remind);
		setremindHint = (TextView) findViewById(R.id.setremind_hint);
		cbRemind.setOnClickListener(this);
		getApplicationContext();
		sp = getApplicationContext().getSharedPreferences(SleepInfo.SLEEP_SETTIME, Context.MODE_APPEND);
		cbRemind.setChecked("".equals(sp.getString(SleepInfo.REMIND_BEFORE_SLEEP, "")));

		findViewById(R.id.rl_remind).setOnClickListener(this);
		findViewById(R.id.rl_getuptime).setOnClickListener(this);
		findViewById(R.id.rl_sleeptime).setOnClickListener(this);
		sleepTime = (TextView) findViewById(R.id.setsleep_time);
		getupTime = (TextView) findViewById(R.id.setgetup_time);
		getupTime.setText(getupDate);
		sleepTime.setText(sleepDate);
		averageGetup = (TextView) findViewById(R.id.average_getup);
		averageSleep = (TextView) findViewById(R.id.average_sleep);
		findViewById(R.id.data_count2).setOnClickListener(this);
		findViewById(R.id.data_count3).setOnClickListener(this);
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		String date = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
		setremindHint.setText(Html.fromHtml(Constant.setRemindHint));
		getDatas(date);
	}
	
	
	/**
	 * 获取数据
	 * @param date
	 */
	private void getDatas(String date){
		if(!Util.checkNetWork(this)){
			Util.show(this, "检查网络设置");
			averageGetup.setText("无数据");
			averageSleep.setText("无数据");
			return;
		}
		showPro();
		GetSignInDataParams mParams = new GetSignInDataParams();
		mParams.my_int_id = PreManager.instance().getUserId(this);
		mParams.date = date;
		mParams.days = "7";
		mParams.type = type;
		new XiangchengMallProcClass(this).getSignInData(mParams, new GetSignInDataCallBack() {
			
			@Override
			public void onSuccess(int icode, List<SleepStatusBean> list) {
				cancelPro();
				doSignInDatas(list);
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				cancelPro();
				averageGetup.setText("无数据");
				averageSleep.setText("无数据");
				Util.show(mContext, strErrMsg);
			}
		});
	}

	/**
	 * 判断 元素 是否在集合里
	 * @param date
	 * @param list
	 * @return 返回 在集合的位置 不在返回-1
	 */
	private int findIndex(String date, List<SleepStatusBean> list){
		for (int i = 0; i < list.size(); i++) {
			if(date.equals(list.get(i).getDatestring())){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 逻辑处理数据
	 * @param list
	 */
	private void doSignInDatas(List<SleepStatusBean> mlist){
		cancelPro();
		CalenderUtil.getSevenDay(7);
		ArrayList<GetAvgTimeParamItem> list1 = new ArrayList<GetAvgTimeParamItem>();
		ArrayList<GetTimeParamItem> tTimelist = new ArrayList<GetTimeParamItem>();
		String[] dates = CalenderUtil.getSevenDay(7);
		for(int i=0; i < dates.length; i++){
			String str= dates[i];
			SoftDayData soft = new SoftDayData();
			soft.setDate(str);
			int index = findIndex(str.replace("-", ""), mlist);
			if(index >= 0){
				try {
					SleepStatusBean data = mlist.get(index);
					if(Long.parseLong(data.getWakeup()) > 0 && Long.parseLong(data.getSleep()) > 0 
							&& Long.parseLong(data.getSleeplong()) > 0){
						soft.setSleepTime(new SimpleDateFormat("HH:mm").format(Long.parseLong(data.getSleep()) * 1000));
						soft.setGetUpTime(new SimpleDateFormat("HH:mm").format(Long.parseLong(data.getWakeup()) * 1000));
						soft.setSleepTimeLong(String.valueOf(Long.parseLong(data.getSleep()) * 1000));
						soft.setGetUpTimeLong(String.valueOf(Long.parseLong(data.getWakeup()) * 1000));
					
						//据算平均
						GetAvgTimeParamItem item = new GetAvgTimeParamItem();
						int inSleepTime = TimeFormatUtil.timeToMiss(soft.getSleepTime());
						int outSleepTime = TimeFormatUtil.timeToMiss(soft.getGetUpTime());
						item.iInSleepTime = inSleepTime;
						item.iOutSleepTime = outSleepTime;
						list1.add(item);
						
						// 提取数据 便于计算最早最迟
						GetTimeParamItem item1 = new GetTimeParamItem();
						item1.iBelongDate = new SimpleDateFormat("yyyy-MM-dd").parse(soft.getDate()).getTime();
						String s = new SimpleDateFormat("yyyy-MM-dd").format(Long.parseLong(soft.getSleepTimeLong()));
						item1.iInSleepDate = new SimpleDateFormat("yyyy-MM-dd").parse(s).getTime();

						item1.iInSleepTime = new SimpleDateFormat("HH:mm").parse(new SimpleDateFormat("HH:mm").format(Long.parseLong(soft.getSleepTimeLong()))).getTime();
						String s1 = new SimpleDateFormat("yyyy-MM-dd").format(Long.parseLong(soft.getGetUpTimeLong()));

						item1.iOutSleepDate = new SimpleDateFormat("yyyy-MM-dd").parse(s1).getTime();
						item1.iOutSleepTime = new SimpleDateFormat("HH:mm").parse(new SimpleDateFormat("HH:mm").format(Long.parseLong(soft.getGetUpTimeLong()))).getTime();
						tTimelist.add(item1);
					
						hasDataCount++;
					}
				} catch (Exception e) {
				}
			}
			softList.add(soft);
		}
		
		avgResult = new GetSleepAvgTimeValueClass().getAVG(list1);
		getTimeResult = GetTimeParamItem.ProcTimeData(tTimelist);// 最早 最迟  返回数据
		if(avgResult != null){
			int avgInSleepTime = avgResult.iAvgInSleepTime;
			int avgOutSleepTime = avgResult.iAvgOutSleepTime;
			String avgInSleepTimeFormat = ((avgInSleepTime / 3600) < 10 ? "0"+ (avgInSleepTime / 3600) : (avgInSleepTime / 3600))+ ":"+ (((avgInSleepTime / 60) % 60) < 10 ? "0"
					+ ((avgInSleepTime / 60) % 60): ((avgInSleepTime / 60) % 60));
			String avgOutSleepTimeFormat = ((avgOutSleepTime / 3600) < 10 ? "0"+ (avgOutSleepTime / 3600) : (avgOutSleepTime / 3600))
			+ ":"+ (((avgOutSleepTime / 60) % 60) < 10 ? "0"+ ((avgOutSleepTime / 60) % 60): ((avgOutSleepTime / 60) % 60));
			
			averageGetup.setText(avgOutSleepTimeFormat);
			averageSleep.setText(avgInSleepTimeFormat);
		}else{
			averageGetup.setText("无数据");
			averageSleep.setText("无数据");
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
	
	private void complete(){
		new XiangchengMallProcClass(this).upLoadDate(PreManager.instance().getUserId(this), sleepDate, getupDate, new UpLoadSetTimeCallback() {
			
			@Override
			public void onSuccess(String icode, String strSuccMsg) {
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
			}
		});
		
		try{
			String getupHore=getupDate.split(":")[0], getupMin=getupDate.split(":")[1];
			int getupH=Integer.parseInt(getupHore);
			int getupM=Integer.parseInt(getupMin);
			//保存旧区间
			long oldt11 = DataUtils.getAllerateStartTime() / (60*1000);
			long oldt21 = DataUtils.getAllerateStopTime() / (60*1000);
			long oldt1 = DataUtils.getStartTime();
			long oldt2 = DataUtils.getStopTime();
			PreManager.instance().saveOldT1(this, oldt1);
			PreManager.instance().saveOldT2(this, oldt2);
			
			//处理新区间
			PreManager.instance().saveSleepTime_Setting(this, sleepDate);
			PreManager.instance().saveGetupTime_Setting(this, getupDate);
		
			//从上班模式中获取数据并保存数据
			final int startTime = getupH * 60 + getupM;
			String sleepHore=sleepDate.split(":")[0], sleepMin=sleepDate.split(":")[1];
			final int endTime = Integer.parseInt(sleepHore) * 60 + Integer.parseInt(sleepMin);
			setTime(endTime,startTime);
			
			try{
				toastMsg("正在重新设置时间，请稍等");
				String date = DataUtils.getRecordDate(new Date());
				long newt1 = DataUtils.getAllerateStartTime() / (60*1000);
				long newt2 = DataUtils.getAllerateStopTime() / (60*1000);
				DealSetTimeUtils.getInstance().setTime(this, date, System.currentTimeMillis(), oldt11, oldt21, newt1, newt2);
			}catch(Exception e) {
			}
			getSleepDataAndSetSmartAlarm(1);
		}catch(Exception e){}
	}

	/**
	 * 设置时间
	 * @param startTime
	 * @param endTime
	 */
	private void setTime(int startTime,int endTime) {
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
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.data_count2:
			startActivity(new Intent(this, AvgTimeDetailActivity.class)
			.putExtra("softList", softList)
			.putExtra("avgResult", avgResult)
			.putExtra("getTimeResult", getTimeResult)
			.putExtra("hasDataCount", hasDataCount)
			.putExtra("title", "平均入睡时间点")
			.putExtra("isSleep", true));
			break;
		case R.id.data_count3:
			startActivity(new Intent(this, AvgTimeDetailActivity.class)
			.putExtra("softList", softList)
			.putExtra("avgResult", avgResult)
			.putExtra("getTimeResult", getTimeResult)
			.putExtra("hasDataCount", hasDataCount)
			.putExtra("title", "平均醒来时间点")
			.putExtra("isSleep", false));
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
					getupDate=(select1 < 10 ? ("0"+select1) : select1+"")+ ":" +(select2 < 10 ? ("0"+select2) : select2 +"");
					getupTime.setText(getupDate);
					dialog.dismiss();
					complete();
				}

				@Override
				public void setResaultRollString(String select1,String select2, String select3) {
				}
			});
			int hourset=0,miunset=0;
			try {
				String[] date=getupDate.split(":");
				hourset=Integer.parseInt(date[0]);
				miunset=Integer.parseInt(date[1]);
			} catch (Exception e) {
			}
					
			dialog.SetData(1, "起床时间", new int[]{0, 23}, hourset, new int[]{0, 59}, miunset, null, 0);
			dialog.show();
			break;
		case R.id.rl_sleeptime:
			dialog=new RollPickerDialog(this, new RollCallBack() {
				
				@Override
				public void setResaultRoll(int select1, int select2, int select3) {
					sleepDate=(select1 < 10 ? ("0"+select1) : select1+"")+ ":" +(select2 < 10 ? ("0"+select2) : select2 +"");
					sleepTime.setText(sleepDate);
					dialog.dismiss();
					complete();
				}

				@Override
				public void setResaultRollString(String select1,String select2, String select3) {
				}
			});
			int hourset1=0,miunset1=0;
			try {
				String[] date=sleepDate.split(":");
				hourset1=Integer.parseInt(date[0]);
				miunset1=Integer.parseInt(date[1]);
			} catch (Exception e) {
			}
					
			dialog.SetData(1, "入睡时间", new int[]{0, 23}, hourset1, new int[]{0, 59}, miunset1, null, 0);
			dialog.show();
			break;
		default:
			break;
		}
	}
	
	// 获取昨天的日期
	@SuppressLint("SimpleDateFormat")
	private String getYesterdayTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(new Date());
	}
	
	/**
	 * @param remind_style   0 午间提醒 1 睡前提醒
	 */
	@SuppressLint("SimpleDateFormat")
	private void getSleepDataAndSetSmartAlarm(int remind_style) {
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
//		calendar.set(Calendar.SECOND, 0);
		calendar.setTimeInMillis(remindtime);

		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
	}
}
