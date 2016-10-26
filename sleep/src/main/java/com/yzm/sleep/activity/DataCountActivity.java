package com.yzm.sleep.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.utils.CalenderUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.SleepUtils;
import com.yzm.sleep.widget.WaveView;

/**
 * 数据统计
 * 
 * @author tianxun
 */
public class DataCountActivity extends BaseActivity {

	private TextView average1, average2, average3,sleepHealthState, sleepComplate,
			getupComplate;
	private String alarmSleepTime, alarmGetUpTime;
	private List<SoftDayData> weekDatas;
	private WaveView waveView ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_datacount);
		findViewById(R.id.back).setOnClickListener(this);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("睡眠趋势");
		initView();
		initdata();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("App_WeeklyReport");
		MobclickAgent.onResume(this);
		if(waveView!=null){
			waveView.postDelayed(new Runnable() {
				@Override
				public void run() {
					waveView.updateProgress(0.8f);
				}
			}, 1000);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("App_WeeklyReport");
		MobclickAgent.onPause(this);
	}

	private void initView() {
		average1 = (TextView) findViewById(R.id.average_1);
		average2 = (TextView) findViewById(R.id.average_2);
		average3 = (TextView) findViewById(R.id.average_3);
		sleepHealthState=(TextView) findViewById(R.id.sleep_health_state);
		sleepComplate = (TextView) findViewById(R.id.sleep_complate);
		getupComplate = (TextView) findViewById(R.id.getup_complate);

		findViewById(R.id.data_count1).setOnClickListener(this);
		findViewById(R.id.data_count2).setOnClickListener(this);
		findViewById(R.id.data_count3).setOnClickListener(this);
		
		waveView = (WaveView)findViewById(R.id.wave);
		waveView.setWaveColor(0x7f3c3c5e);
		waveView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				waveView.updateProgress(0.8f);
			}
		}, 1000);
	}

	@SuppressLint("SimpleDateFormat")
	private void initdata() {
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DATE, -1);
		alarmSleepTime = PreManager.instance().getSleepTime_Setting(this);
		alarmGetUpTime = PreManager.instance().getGetupTime_Setting(this);
		weekDatas = new ArrayList<SoftDayData>();
		List<String> list = CalenderUtil.getLastSevenDay(7);
		for (String string : list) {
			weekDatas.add(getDaySleepData(string));
		}

		int hasDataCount = 0;
		double sleeComp=0, getUpComp=0;
		for (int i = 0; i < (weekDatas == null ? 0 : weekDatas.size()); i++) {
			if (weekDatas.get(i) != null && !TextUtils.isEmpty(weekDatas.get(i).getGetUpTime())&& !TextUtils.isEmpty(weekDatas.get(i).getSleepTime())) {
				
				hasDataCount += 1;
				// 计算按时起床天数
				try {
					SimpleDateFormat sdfday = new SimpleDateFormat("HH:mm");
					// 统计入睡达标的天数
					long targets = sdfday.parse(alarmSleepTime).getTime();// 目标入睡时间
					int targetsM = (int) (targets / (60 * 1000));
					long sleepTimes = sdfday.parse(
							weekDatas.get(i).getSleepTime()).getTime();// 实际入睡时间
					int sleepTimeM = (int) (sleepTimes / (60 * 1000));
					int temp = sleepTimeM - targetsM;
					if (temp >= -15 && temp <= 15)
						sleeComp += 1;

					// 统计起床达标的天数
					long targetu = sdfday.parse(alarmGetUpTime).getTime();
					int targetMu = (int) (targetu / (60 * 1000));
					long sleepTimeu = sdfday.parse(weekDatas.get(i).getGetUpTime()).getTime();
					int upTimeM = (int) (sleepTimeu / (60 * 1000));
					int tempup = upTimeM - targetMu;
					if (tempup >= -15 && tempup <= 15)
						getUpComp += 1;

				} catch (Exception e) {
				}
			}
		}

		if (hasDataCount > 0) {
			try {
				String aveLength=SleepUtils.getAverageSleepLengthString(DataCountActivity.this, weekDatas);
				String averageSleepLength = new SimpleDateFormat("HH小时mm分").format(new SimpleDateFormat("HH:mm").parse(aveLength).getTime());
				
				String[] times = SleepUtils.getAvgSleepTimeAndGetupTime(weekDatas);
				average1.setText(averageSleepLength);
				String[] sleepLength = aveLength.split(":");
		    	int sleepM=Integer.parseInt(sleepLength[0])*60+Integer.parseInt(sleepLength[1]);
				float target=PreManager.instance().getRecommendTarget(this);
		    	if(sleepM <(target-1)*60){
		    		sleepHealthState.setText("偏短");
		    		sleepHealthState.setTextColor(0Xff4472FE);
				}else if(sleepM > (target+1)*60){
					sleepHealthState.setText("偏长");
					sleepHealthState.setTextColor(0Xff4472FE);
				}else{
					sleepHealthState.setText("健康");
					sleepHealthState.setTextColor(0Xff8644FD);
				}
				average2.setText(times[0]);
				sleepComplate.setText("计划完成:"+((int)((sleeComp/hasDataCount)*100))+"%");
				average3.setText(times[1]);
				getupComplate.setText("计划完成:"+((int)((getUpComp/hasDataCount)*100))+"%");
				
			} catch (Exception e) {
				sleepHealthState.setText("");
				sleepComplate.setText("计划完成:0%");
				getupComplate.setText("计划完成:0%");
				average2.setText("无数据");
				average2.setTextColor(0Xffd9d9d9);
				average1.setText("无数据");
				average1.setTextColor(0Xffd9d9d9);
				average3.setText("无数据");
				average3.setTextColor(0Xffd9d9d9);
			}
		} else {
			sleepHealthState.setText("");
			sleepComplate.setText("计划完成:0%");
			getupComplate.setText("计划完成:0%");
			average2.setText("无数据");
			average2.setTextColor(0Xffd9d9d9);
			average1.setText("无数据");
			average1.setTextColor(0Xffd9d9d9);
			average3.setText("无数据");
			average3.setTextColor(0Xffd9d9d9);
		}
	}

	/**
	 * 获取当天的睡眠数据
	 * 
	 * @return
	 */
	private SoftDayData getDaySleepData(String date) {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(this);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from " + MyTabList.SLEEPTIME
				+ " where date=? and ispunch = '1'", new String[] { date });
		SoftDayData dayData = new SoftDayData();
		dayData.setDate(date);
		try {
			cursor.moveToFirst();
			String sleepTime = cursor.getString(cursor
					.getColumnIndex(MyTabList.TableDay.SLEEPTIME.getCloumn()));
			String getUpTime = cursor.getString(cursor
					.getColumnIndex(MyTabList.TableDay.UPTIME.getCloumn()));
			String startTime = cursor.getString(cursor
					.getColumnIndex(MyTabList.TableDay.STARTTIME.getCloumn()));
			String endTime = cursor.getString(cursor
					.getColumnIndex(MyTabList.TableDay.ENDTIME.getCloumn()));
			String sleepTimeLong = cursor.getString(cursor
					.getColumnIndex(MyTabList.TableDay.SLEEPTIMELONG.getCloumn()));
			String getUpTimeLong = cursor.getString(cursor
					.getColumnIndex(MyTabList.TableDay.UPTIMELONG.getCloumn()));
			
			dayData.setSleepTime(sleepTime);
			dayData.setGetUpTime(getUpTime);
			dayData.setStartTime(startTime);
			dayData.setEndTime(endTime);
			dayData.setSleepTimeLong(sleepTimeLong);
			dayData.setGetUpTimeLong(getUpTimeLong);
		} catch (Exception e) {
		}finally{
			cursor.close();
		}
		
		return dayData;
	}

	/**
	 * 获取所有的睡眠数据
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	private List<SoftDayData> getSleepData() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String crrentDate = sdf
					.format(new Date(System.currentTimeMillis()));
			MyDatabaseHelper helper = MyDatabaseHelper.getInstance(this);
			SQLiteDatabase db = helper.getWritableDatabase();
			Cursor cursor = db.rawQuery("SELECT * FROM " + MyTabList.SLEEPTIME
					+ " where date != '" + crrentDate
					+ "' and record_state='4' order by date(date) asc", null);
			List<SoftDayData> data = new ArrayList<SoftDayData>();
			while (cursor.moveToNext()) {
				String sleepTime = cursor.getString(cursor
						.getColumnIndex(MyTabList.TableDay.SLEEPTIME
								.getCloumn()));
				String getUpTime = cursor.getString(cursor
						.getColumnIndex(MyTabList.TableDay.UPTIME.getCloumn()));
				String date = cursor.getString(cursor
						.getColumnIndex(MyTabList.TableDay.DATE.getCloumn()));
				String sleepTimeLong = cursor.getString(cursor
						.getColumnIndex("sleeptimelong"));
				String getUpTimeLong = cursor.getString(cursor
						.getColumnIndex("uptimelong"));

				SoftDayData dayData = new SoftDayData();
				dayData.setSleepTime(sleepTime);
				dayData.setGetUpTime(getUpTime);
				dayData.setDate(date);
				dayData.setSleepTimeLong(sleepTimeLong);
				dayData.setGetUpTimeLong(getUpTimeLong);
				data.add(dayData);
			}
			cursor.close();
			return data;
		} catch (Exception e) {
		}
		return new ArrayList<SoftDayData>();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AppManager.getAppManager().finishActivity();
		}
		return true;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.data_count1:
			intent = new Intent(this, SleepTimeDetailActivity.class);
			break;
		case R.id.data_count2:
			if (weekDatas != null && weekDatas.size() > 0) {
				intent = new Intent(this, SleepOrGetUpAvgTimeDetail.class);
				intent.putExtra("title", "平均入睡时间点");
				intent.putExtra("isSleep", true);
			}
			break;
		case R.id.data_count3:
			if (weekDatas != null && weekDatas.size() > 0) {
				intent = new Intent(this, SleepOrGetUpAvgTimeDetail.class);
				intent.putExtra("title", "平均醒来时间点");
				intent.putExtra("isSleep", false);
			}
			break;
		default:
			break;
		}

		if (intent != null)
			startActivity(intent);
	}

}
