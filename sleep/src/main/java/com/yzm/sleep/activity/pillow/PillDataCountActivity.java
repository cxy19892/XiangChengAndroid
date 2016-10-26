package com.yzm.sleep.activity.pillow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.model.PillowDataModel;
import com.yzm.sleep.render.GetSleepAvgTimeValueClass;
import com.yzm.sleep.render.GetSleepAvgTimeValueClass.AvgTimeResult;
import com.yzm.sleep.render.GetSleepAvgTimeValueClass.GetAvgTimeParamItem;
import com.yzm.sleep.render.GetSleepResultValueClass.SleepDataHead;
import com.yzm.sleep.utils.BluetoothDataFormatUtil;
import com.yzm.sleep.utils.CalenderUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.SleepUtils;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.widget.WaveView;

/**
 * 枕头数据统计
 */
@SuppressLint("SimpleDateFormat")
public class PillDataCountActivity extends BaseActivity {

	private TextView average1, average2, average3,sleepHealthState, sleepComplate,
			getupComplate;
	private String alarmSleepTime, alarmGetUpTime,deepSleep="00小时00分",shallowSleep="00小时00分";
	private ArrayList<SoftDayData> softList;
	private int hasDataCount=0,complateSleepCount=0,complateGetUpCount=0,totalTime =0;
	private AvgTimeResult result;
	private ArrayList<byte[]> listByte;
	private WaveView waveView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_datacount);
		findViewById(R.id.back).setOnClickListener(this);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("睡眠趋势");
		initView();
		getByDateSleepDate();
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SP_WeeklyReport"); //add
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
		MobclickAgent.onPageEnd("SP_WeeklyReport");//add
		MobclickAgent.onPause(this);
	}

	private void initView() {
		average1 = (TextView) findViewById(R.id.average_1);
		average2 = (TextView) findViewById(R.id.average_2);
		average3 = (TextView) findViewById(R.id.average_3);
		waveView = (WaveView)findViewById(R.id.wave);
		waveView.setWaveColor(0x7f3c3c5e);
		waveView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				waveView.updateProgress(0.8f);
			}
		}, 1000);
		findViewById(R.id.data_count1).setOnClickListener(this);
		findViewById(R.id.data_count2).setOnClickListener(this);
		findViewById(R.id.data_count3).setOnClickListener(this);
		sleepHealthState=(TextView) findViewById(R.id.sleep_health_state);
		sleepComplate = (TextView) findViewById(R.id.sleep_complate);
		getupComplate = (TextView) findViewById(R.id.getup_complate);
	}
	
	/**
	 * 枕头睡眠数据
	 */
	private void getByDateSleepDate(){
		softList=new ArrayList<SoftDayData>();
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DATE, -1);
		SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
		alarmSleepTime = PreManager.instance().getSleepTime_Setting(this);
		alarmGetUpTime = PreManager.instance().getGetupTime_Setting(this);
//		// 数据库有当天睡眠计划的取睡眠计划
		
		List<String> listDayDates = CalenderUtil.getLastSevenDay(7);
		GetSleepAvgTimeValueClass avgTimeCls = new GetSleepAvgTimeValueClass();
		ArrayList<GetAvgTimeParamItem> list1 = new ArrayList<GetAvgTimeParamItem>();
		int totalDeeptime=0,totalshallowSleep=0;
		try {
			listByte = new ArrayList<byte[]>();
			for (int i = 0; i < listDayDates.size(); i++) {
				//获取一天的睡眠数据
				PillowDataModel pModel = PillowDataOpera.queryDataFromSQL(PillDataCountActivity.this, listDayDates.get(i));
				byte[] bfr = pModel.getBfr();
				SoftDayData softData = new SoftDayData();
				softData.setDate(listDayDates.get(i));
				listByte.add(bfr);
				try {
					if (bfr != null) {
						hasDataCount++;
						SleepDataHead datahead = BluetoothDataFormatUtil.format3(bfr, 10);
						softData.setSleepTime(TimeFormatUtil.formatTime1(datahead.InSleepTime, "HH:mm"));
						softData.setGetUpTime(TimeFormatUtil.formatTime1(datahead.OutSleepTime, "HH:mm"));
						softData.setSleepTimeLong(String.valueOf(datahead.InSleepTime * 1000));
						softData.setGetUpTimeLong(String.valueOf(datahead.OutSleepTime * 1000));
						
						int inSleepTime = TimeFormatUtil.timeToMiss(TimeFormatUtil.formatTime1(datahead.InSleepTime, "HH:mm"));
						int outSleepTime = TimeFormatUtil.timeToMiss(TimeFormatUtil.formatTime1(datahead.OutSleepTime, "HH:mm"));
						totalTime += datahead.TotalSleepTime;
						totalDeeptime+=datahead.Deep_Sleep;
						totalshallowSleep+=datahead.Shallow_Sleep;
						
						GetAvgTimeParamItem item = new GetAvgTimeParamItem();
						// 以下3个值必须都有，没有的话，传0
						item.iInSleepTime = inSleepTime;
						item.iOutSleepTime = outSleepTime;
						item.iSleepCountTime = totalTime;
						list1.add(item);
						
						//统计入睡达标的天数
						long targetS=TimeFormatUtil.timeToMiss(alarmSleepTime);//目标入睡时间
						long temp=inSleepTime-targetS;
						if(temp >= -15*60 && temp <= 15*60)
							complateSleepCount+=1;
						
						//统计起床达标的天数
						long targetu=TimeFormatUtil.timeToMiss(alarmGetUpTime);
						long tempup=outSleepTime-targetu;
						if(tempup>=-15*60 && tempup<=15*60)
							complateGetUpCount+=1;
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				softList.add(softData);
			}
		} catch (Exception e) {
		}
		
		result = avgTimeCls.getAVG(list1);
		
		if(result!=null){
			int avgInSleepTime = result.iAvgInSleepTime;
			int avgOutSleepTime = result.iAvgOutSleepTime;
			int avgTotalSleepTime = totalTime / hasDataCount;
			int avgTotalDeepTime=totalDeeptime/hasDataCount;
			int avgTotalshallowSleep=totalshallowSleep/hasDataCount;

			String avgInSleepTimeFormat = ((avgInSleepTime / 3600) < 10 ? "0"+ (avgInSleepTime / 3600) : (avgInSleepTime / 3600))+ ":"+ (((avgInSleepTime / 60) % 60) < 10 ? "0"
							+ ((avgInSleepTime / 60) % 60): ((avgInSleepTime / 60) % 60));
			String avgOutSleepTimeFormat = ((avgOutSleepTime / 3600) < 10 ? "0"+ (avgOutSleepTime / 3600) : (avgOutSleepTime / 3600))
					+ ":"+ (((avgOutSleepTime / 60) % 60) < 10 ? "0"+ ((avgOutSleepTime / 60) % 60): ((avgOutSleepTime / 60) % 60));
			String avgTotalTimeFormat = ((avgTotalSleepTime / 60) < 10 ? "0"+ (avgTotalSleepTime / 60) : (avgTotalSleepTime / 60))+ ":"
					+ ((avgTotalSleepTime % 60) < 10 ? "0"+ (avgTotalSleepTime % 60): (avgTotalSleepTime % 60));
			
			deepSleep= ((avgTotalDeepTime / 60) < 10 ? "0"+ (avgTotalDeepTime / 60) : (avgTotalDeepTime / 60))+ "小时"
					+ ((avgTotalDeepTime % 60) < 10 ? "0"+ (avgTotalDeepTime % 60): (avgTotalDeepTime % 60))+"分";
			
			shallowSleep=((avgTotalshallowSleep / 60) < 10 ? "0"+ (avgTotalshallowSleep / 60) : (avgTotalshallowSleep / 60))+ "小时"
					+ ((avgTotalshallowSleep % 60) < 10 ? "0"+ (avgTotalshallowSleep % 60): (avgTotalshallowSleep % 60))+"分";
			try {
				if (avgInSleepTimeFormat != null && avgInSleepTimeFormat.length()>0) {
					average1.setText(new SimpleDateFormat("HH小时mm分").format(new SimpleDateFormat("HH:mm").parse(avgTotalTimeFormat).getTime()));
					average2.setText(avgInSleepTimeFormat);
					sleepComplate.setText("计划完成:"+((int)((((double)complateSleepCount)/hasDataCount)*100))+"%".replace(".0", ""));
					average3.setText(avgOutSleepTimeFormat);
					getupComplate.setText("计划完成:"+((int)(((double)complateGetUpCount/hasDataCount)*100))+"%".replace(".0", ""));
					String[] sleepLength = avgTotalTimeFormat.split(":");
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
		}else{
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

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.back){
			AppManager.getAppManager().finishActivity();
		}else{
			Intent intent = new Intent(this, PillowWeeklyActivity.class);
			intent.putExtra("alarmSleepTime", alarmSleepTime);
			intent.putExtra("alarmGetUpTime", alarmGetUpTime);
			intent.putExtra("softList", softList);
			intent.putExtra("hasDataCount", hasDataCount);
			intent.putExtra("complateSleepCount", complateSleepCount);
			intent.putExtra("complateGetUpCount", complateGetUpCount);
			intent.putExtra("result", result);
			intent.putExtra("totalTime", totalTime);
			intent.putExtra("deepSleep", deepSleep);
			intent.putExtra("shallowSleep", shallowSleep);
			switch (v.getId()) {
			case R.id.data_count1:
				intent.putExtra("listByte", listByte);
				intent.putExtra("type", 0);
				break;
			case R.id.data_count2:
				intent.putExtra("type", 1);
				break;
			case R.id.data_count3:
				intent.putExtra("type", 2);
				break;
			default:
				break;
			}
	
			if (intent != null)
				startActivity(intent);
		}
	}

	
}
