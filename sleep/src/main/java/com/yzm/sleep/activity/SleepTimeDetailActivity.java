package com.yzm.sleep.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.utils.CalenderUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.SleepDocumentUtils;
import com.yzm.sleep.utils.SleepUtils;
import com.yzm.sleep.widget.SoftSleepLengthTable;

/**
 * 平均睡眠时长
 * 
 * @author tianxun
 * 
 */
public class SleepTimeDetailActivity extends BaseActivity {

	private SoftSleepLengthTable softSleepLengthTable;
	private TextView tvSleepLenght, tipsDes,sleepState; //time2, time4, 
	private List<SoftDayData> weekDatas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sleeptime_detail);
		findViewById(R.id.back).setOnClickListener(this);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("平均睡眠时长");
		Button right = (Button) findViewById(R.id.btn_title_right);
		right.setCompoundDrawables(null, null, null, null);
		right.setText("更多");
		right.setOnClickListener(this);
		right.setVisibility(View.VISIBLE);
		initView();
		initData();
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("App_WeeklyReport_Avg_SleepLength"); 
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("App_WeeklyReport_Avg_SleepLength");
		MobclickAgent.onPause(this);
	}

	private void initView() {
		softSleepLengthTable=(SoftSleepLengthTable) findViewById(R.id.softsleeplengthform);
		tvSleepLenght = (TextView) findViewById(R.id.sleeptlenght);
		sleepState=(TextView) findViewById(R.id.sleep_state);
		tipsDes = (TextView) findViewById(R.id.tips_description);
	}

	@SuppressLint("SimpleDateFormat") 
	private void initData() {
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DATE, -1);
		weekDatas = new ArrayList<SoftDayData>();
		List<String> list = CalenderUtil.getLastSevenDay(7);
		for (String string : list) {
			weekDatas.add(getDaySleepData(string));
		}
		softSleepLengthTable.setData(weekDatas);
		
		float target=PreManager.instance().getRecommendTarget(this);
		
	    int countDay=0;
	    for (int i=0;i<(weekDatas==null? 0:weekDatas.size());i++) {
			if(weekDatas.get(i)!=null && !TextUtils.isEmpty(weekDatas.get(i).getSleepTime())&& !TextUtils.isEmpty(weekDatas.get(i).getGetUpTime())){
				countDay++;
			}
		}
	    
	    if(countDay==0){
	    	tipsDes.setText("睡眠作为生命所必须的过程，是机体复原、整合和巩固记忆的重要环节，是健康不可缺少的组成部分。");
	    	tvSleepLenght.setText("平均睡眠时长: 00小时00分");
	    }else{
	    	try {
	    		String aveLength=SleepUtils.getAverageSleepLengthString(this, weekDatas);
	    		String[] sleepgetup = SleepUtils.getAvgSleepTimeAndGetupTime(weekDatas);
				String[] times = aveLength.split(":");
				tvSleepLenght.setText("平均睡眠时长: "+times[0]+"小时"+times[1]+"分");
		    	int sleepM=Integer.parseInt(times[0])*60+Integer.parseInt(times[1]);
		    	if(sleepM < (target-1)*60){
		    		sleepState.setText("偏短");
		    		sleepState.setTextColor(0xff4471FE);
				}else if(sleepM > (target+1)*60){
					sleepState.setText("偏长");
					sleepState.setTextColor(0xff4471FE);
				}else{
					sleepState.setText("健康");
					sleepState.setTextColor(0xff8644FD);
				}
		    	
		    	float avgLenght=((float)sleepM)/60;
		    	initSleepAnalyzeDocumen(sleepgetup[0],sleepgetup[1],avgLenght);
	    	} catch (Exception e) {
			}
	    }
	}
	
	/**
	 * 显示睡眠分析结果
	 */
	private void initSleepAnalyzeDocumen(String sleepTime,String getupTime,float aveSleepLength) {
		try {
			//睡眠分析结果数据，0 睡觉时间分析结果 ，1睡眠时长分析结果，2起床时间分析结果
			List<String> analyzeResultBySleepTime = new SleepDocumentUtils().getAnalyzeResultBySleepTime(this,sleepTime,aveSleepLength,getupTime);
			if (analyzeResultBySleepTime != null && analyzeResultBySleepTime.size() == 3) {
				tipsDes.setText(analyzeResultBySleepTime.get(1));
			}
		} catch (Exception e) {
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
				+ " where date=? and ispunch = '1' ", new String[] { date }); 
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.btn_title_right:
			startActivity(new Intent(this,MonthDataAnalysisActivity.class).putExtra("type", "0").putExtra("dataType", "1"));
			break;
		default:
			break;
		}
	}
}
