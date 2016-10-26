package com.yzm.sleep.activity.pillow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.NewRoundProgressBar;
import com.yzm.sleep.R;
import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.activity.MonthDataAnalysisActivity;
import com.yzm.sleep.render.GetSleepAvgTimeValueClass.AvgTimeResult;
import com.yzm.sleep.utils.CalenderUtil;
import com.yzm.sleep.utils.GetTimeParamItem;
import com.yzm.sleep.utils.GetTimeParamItem.GetTimeResult;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.SleepDocumentUtils;
import com.yzm.sleep.widget.OrangeSleepLengthTable;
import com.yzm.sleep.widget.SleepTimePointTable;

/**
 * 睡眠枕头周报
 * 
 * String title 头部标题 int type; 显示哪一种柱状图 sleepTime 平均睡眠时长
 * 
 * @author Administrator
 * 
 */
@SuppressLint("SimpleDateFormat")
public class PillowWeeklyActivity extends BaseActivity implements
		OnClickListener {
	private TextView tvConclusion;// 总结
	private TextView time1,time2, time3,time4, time5,tvComplete, tvGoalBefore,avgShallowerSleeptime,avgDeepSleeptime,
			tvGoalLater, tvGoalBetween, tvPro;// 显示深度睡眠数据
	private TextView tvAverage,tvAvgText;// 显示平均时长
	private OrangeSleepLengthTable pillowWeekLength;// 显示柱状图
	private SleepTimePointTable pillowWeekTime;
	private int type = -1;
	private SeekBar my_LengthSeekbar;
	private int timeLength = 4;
	private NewRoundProgressBar progerssbar;
	private String[] str = { "睡眠作为生命所必须的过程，是机体复原、整合和巩固记忆的重要环节，是健康不可缺少的组成部分。",
			"好的睡眠习惯，能够保证白天精力充沛、容颜娇好。", "好的睡眠习惯，能够保证白天精力充沛、容颜娇好。" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pilloweek);
		type = getIntent().getIntExtra("type", 0);
		Button right=(Button) findViewById(R.id.btn_title_right);
		right.setCompoundDrawables(null, null, null, null);
		right.setText("更多");
		right.setOnClickListener(this);
		right.setVisibility(View.VISIBLE);
		if (type == 0) {
			((TextView) findViewById(R.id.title)).setText("平均睡眠时长");
		} else if (type == 1) {
			((TextView) findViewById(R.id.title)).setText("平均入睡时间点");
		} else if (type == 2) {
			((TextView) findViewById(R.id.title)).setText("平均醒来时间点");
		}

		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (type == 0) {
			MobclickAgent.onPageStart("SP_WeeklyReport_Avg_SleepLength");
		} else if (type == 1) {
			MobclickAgent.onPageStart("SP_WeeklyReport_Avg_SleepTime");
		} else if (type == 2) {
			MobclickAgent.onPageStart("App_WeeklyReport_Avg_WakeupTime");
		}
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (type == 0) {
			MobclickAgent.onPageEnd("SP_WeeklyReport_Avg_SleepLength");
		} else if (type == 1) {
			MobclickAgent.onPageEnd("SP_WeeklyReport_Avg_SleepTime");
		} else if (type == 2) {
			MobclickAgent.onPageEnd("App_WeeklyReport_Avg_WakeupTime");
		}
		MobclickAgent.onPause(this);
	}

	private void initView() {
		avgShallowerSleeptime=(TextView) findViewById(R.id.avg_shallower_sleeptime);
		avgDeepSleeptime=(TextView) findViewById(R.id.avg_deep_sleeptime);
		my_LengthSeekbar = (SeekBar) findViewById(R.id.sb_time_value);
		pillowWeekTime = (SleepTimePointTable) findViewById(R.id.pillow_week1);
		tvConclusion = (TextView) findViewById(R.id.tvConclusion); 
		tvAverage = (TextView) findViewById(R.id.tvAverage);
		time1 = (TextView) findViewById(R.id.time1);
		time2 = (TextView) findViewById(R.id.time2);
		time3 = (TextView) findViewById(R.id.time3);
		time4 = (TextView) findViewById(R.id.time4);
		time5 = (TextView) findViewById(R.id.time5);
		tvAvgText=(TextView) findViewById(R.id.tv_avgText);
		progerssbar = (NewRoundProgressBar) findViewById(R.id.progerssbar);

		tvComplete = (TextView) findViewById(R.id.tv_complete);
		tvGoalBefore = (TextView) findViewById(R.id.tv_goal_before);
		tvGoalLater = (TextView) findViewById(R.id.tv_goal_later);
		tvGoalBetween = (TextView) findViewById(R.id.tv_goal_between);
		tvPro = (TextView) findViewById(R.id.tv_pro);
		pillowWeekLength= (OrangeSleepLengthTable) findViewById(R.id.pillow_week);
		
		LinearLayout.LayoutParams lp = (LayoutParams) pillowWeekLength.getLayoutParams();
		int screenWidth = getScreenWidth();
		lp.width = screenWidth;
		lp.height = screenWidth * 3/5;
		pillowWeekLength.setLayoutParams(lp);
		findViewById(R.id.back).setOnClickListener(this);
		
		my_LengthSeekbar.setEnabled(false);
		
	}
	
	@SuppressWarnings({ "unchecked" })
	private void initData() {
		ArrayList<SoftDayData> softList=(ArrayList<SoftDayData>) getIntent().getSerializableExtra("softList");
		String alarmSleepTime=getIntent().getStringExtra("alarmSleepTime");
		String alarmGetUpTime=getIntent().getStringExtra("alarmGetUpTime");
		int hasDataCount=getIntent().getIntExtra("hasDataCount", 0);
		int complateSleepCount=getIntent().getIntExtra("complateSleepCount", 0);
		int complateGetUpCount=getIntent().getIntExtra("complateGetUpCount", 0);
		int totalTime=getIntent().getIntExtra("totalTime", 0);
		AvgTimeResult result=(AvgTimeResult) getIntent().getSerializableExtra("result");
		ArrayList<GetTimeParamItem> tTimelist = new ArrayList<GetTimeParamItem>();
		try {
			for (int i=0;i<(softList==null?0:softList.size());i++) {
				SoftDayData softData=softList.get(i);
				if(!TextUtils.isEmpty(softData.getSleepTimeLong()) &&!TextUtils.isEmpty(softData.getGetUpTimeLong()) ){
					// 提取数据 便于计算最早最迟
					GetTimeParamItem item = new GetTimeParamItem();
					item.iBelongDate = new SimpleDateFormat("yyyy-MM-dd").parse(softData.getDate()).getTime();
					String s = new SimpleDateFormat("yyyy-MM-dd").format(Long.parseLong(softData.getSleepTimeLong()));
					item.iInSleepDate = new SimpleDateFormat("yyyy-MM-dd").parse(s).getTime();

					item.iInSleepTime = new SimpleDateFormat("HH:mm").parse(new SimpleDateFormat("HH:mm").format(Long.parseLong(softData.getSleepTimeLong()))).getTime();
					String s1 = new SimpleDateFormat("yyyy-MM-dd").format(Long.parseLong(softData.getGetUpTimeLong()));

					item.iOutSleepDate = new SimpleDateFormat("yyyy-MM-dd")
							.parse(s1).getTime();
					item.iOutSleepTime = new SimpleDateFormat("HH:mm").parse(new SimpleDateFormat("HH:mm").format(Long.parseLong(softData.getGetUpTimeLong())))
							.getTime();
					tTimelist.add(item);
				}
			}
			GetTimeResult re = GetTimeParamItem.ProcTimeData(tTimelist);// 最早 最迟  返回数据
			List<String> listDayDates = CalenderUtil.getLastSevenDay(7);
			if(type==0){
				ArrayList<byte[]> listByte=(ArrayList<byte[]>) getIntent().getSerializableExtra("listByte");
				pillowWeekLength.setVisibility(View.VISIBLE);
				pillowWeekTime.setVisibility(View.GONE);
				findViewById(R.id.analyze_result).setVisibility(View.GONE);
				findViewById(R.id.ll_type1).setVisibility(View.VISIBLE);
				findViewById(R.id.ll_sleeplenght).setVisibility(View.VISIBLE);
				findViewById(R.id.sleepLenght_hint).setVisibility(View.VISIBLE);
				avgDeepSleeptime.setText(getIntent().getStringExtra("deepSleep"));
				avgShallowerSleeptime.setText(getIntent().getStringExtra("shallowSleep"));
				pillowWeekLength.setData(listByte,listDayDates);
				
				if(hasDataCount > 0 && result!=null){
					int avgInSleepTime = result.iAvgInSleepTime;
					int avgOutSleepTime = result.iAvgOutSleepTime;
					int avgTotalSleepTime = totalTime / hasDataCount;
					String avgInSleepTimeFormat = ((avgInSleepTime / 3600) < 10 ? "0"+ (avgInSleepTime / 3600) : (avgInSleepTime / 3600))+ ":"+ (((avgInSleepTime / 60) % 60) < 10 ? "0"
							+ ((avgInSleepTime / 60) % 60): ((avgInSleepTime / 60) % 60));
					String avgOutSleepTimeFormat = ((avgOutSleepTime / 3600) < 10 ? "0"+ (avgOutSleepTime / 3600) : (avgOutSleepTime / 3600))
					+ ":"+ (((avgOutSleepTime / 60) % 60) < 10 ? "0"+ ((avgOutSleepTime / 60) % 60): ((avgOutSleepTime / 60) % 60));
					String avgTotalTimeFormat = ((avgTotalSleepTime / 60) < 10 ? "0"+ (avgTotalSleepTime / 60) : (avgTotalSleepTime / 60))+ "小时"
							+ ((avgTotalSleepTime % 60) < 10 ? "0"+ (avgTotalSleepTime % 60): (avgTotalSleepTime % 60))+"分";
					tvAverage.setText("平均睡眠时长："+avgTotalTimeFormat);
				
					float setlength=PreManager.instance().getRecommendTarget(this);
					time1.setTextColor(0xff7F7D92);
					time3.setTextColor(0xff7F7D92);
					time5.setTextColor(0xff7F7D92);
					
					time2.setText((setlength - 1) + "h");
					time4.setText((setlength + 1) + "h");
					float avglength=((float)avgTotalSleepTime)/60;
					if (avglength < setlength - 1) {
						float temp = (setlength - 1) - avglength;
						int tempPro = (timeLength * 60)-((int) (temp * 60));
						my_LengthSeekbar.setProgress(tempPro < 25 ? 25 : tempPro);
						time1.setTextColor(0xff4471FF);
						my_LengthSeekbar.setThumb(getResources().getDrawable(R.drawable.index_icon1));
					} else if (avglength > setlength + 1) {
						float temp = avglength - (setlength + 1);
						int tempPro =(timeLength * 60 * 2) +((int) (temp * 60));
						my_LengthSeekbar.setProgress(tempPro  >695 ? 695: tempPro);
						my_LengthSeekbar.setThumb(getResources().getDrawable(R.drawable.index_icon1));
						time5.setTextColor(0xff4471FF);
					} else {
						float temp = avglength - (setlength - 1);
						int tempPro = (int) (temp * (timeLength / 2) * 60);
						my_LengthSeekbar.setProgress((timeLength * 60) + tempPro);
						my_LengthSeekbar.setThumb(getResources().getDrawable(R.drawable.index_icon));
						time3.setTextColor(0xff8745ff);
					}
					
					initSleepAnalyzeDocumen(avgInSleepTimeFormat,avgOutSleepTimeFormat,avglength,type);
				}else{
					my_LengthSeekbar.setVisibility(View.INVISIBLE);
					tvAverage.setText("平均睡眠时长：00小时00分");
					tvConclusion.setText(str[type]);
				}
			}else if (type==1){
				pillowWeekTime.setData(softList, alarmSleepTime, 0);
				progerssbar.setVisibility(View.VISIBLE);
				pillowWeekLength.setVisibility(View.GONE);
				pillowWeekTime.setVisibility(View.VISIBLE);
				findViewById(R.id.analyze_result).setVisibility(View.VISIBLE);
				findViewById(R.id.ll_type1).setVisibility(View.GONE);
				if(hasDataCount >0 && result!=null){
					int avgInSleepTime = result.iAvgInSleepTime;
					int avgOutSleepTime = result.iAvgOutSleepTime;
					int avgTotalSleepTime = totalTime / hasDataCount;
					String avgInSleepTimeFormat = ((avgInSleepTime / 3600) < 10 ? "0"+ (avgInSleepTime / 3600) : (avgInSleepTime / 3600))+ ":"+ (((avgInSleepTime / 60) % 60) < 10 ? "0"
							+ ((avgInSleepTime / 60) % 60): ((avgInSleepTime / 60) % 60));
					String avgOutSleepTimeFormat = ((avgOutSleepTime / 3600) < 10 ? "0"+ (avgOutSleepTime / 3600) : (avgOutSleepTime / 3600))
					+ ":"+ (((avgOutSleepTime / 60) % 60) < 10 ? "0"+ ((avgOutSleepTime / 60) % 60): ((avgOutSleepTime / 60) % 60));
					tvAvgText.setText("平均入睡时间点："+avgInSleepTimeFormat);
					float avglength=((float)avgTotalSleepTime)/60;
					int temps=(int)((((double)complateSleepCount)/ hasDataCount) * 100);
					progerssbar.setProgress(temps);
					tvPro.setText(temps+ "%");
					tvComplete.setText("最近7天目标完成"+ temps + "% ,请继续加油");
					tvGoalLater.setText("最迟睡觉时间点: "+ new SimpleDateFormat("HH:mm").format(re.iInSleepTime_last));
					tvGoalBefore.setText("最早睡觉时间点: "+ new SimpleDateFormat("HH:mm").format(re.iInSleepTime_early));
					tvGoalBetween.setText("有" + complateSleepCount + "天准时睡觉");
					initSleepAnalyzeDocumen(avgInSleepTimeFormat,avgOutSleepTimeFormat,avglength,type);
					
				}else{
					progerssbar.setProgress(0);
					tvComplete.setText("最近7天目标完成0% ,请继续加油");
					tvGoalLater.setText("最迟睡觉时间点:--:--");
					tvGoalBefore.setText("最早睡觉时间点: --:--");
					tvAvgText.setText("平均入睡时间点：--:--");
					tvGoalBetween.setText("有" + complateSleepCount + "天准时睡觉");
					tvPro.setText("0%");
					tvConclusion.setText(str[type]);
				}
			}else if(type==2){
				pillowWeekTime.setData(softList, alarmGetUpTime, 1);
				progerssbar.setVisibility(View.VISIBLE);
				pillowWeekLength.setVisibility(View.GONE);
				pillowWeekTime.setVisibility(View.VISIBLE);
				findViewById(R.id.analyze_result).setVisibility(View.VISIBLE);
				findViewById(R.id.ll_type1).setVisibility(View.GONE);
				if(hasDataCount >0  && result!=null){
					int avgInSleepTime = result.iAvgInSleepTime;
					int avgOutSleepTime = result.iAvgOutSleepTime;
					int avgTotalSleepTime = totalTime / hasDataCount;
					String avgInSleepTimeFormat = ((avgInSleepTime / 3600) < 10 ? "0"+ (avgInSleepTime / 3600) : (avgInSleepTime / 3600))+ ":"+ (((avgInSleepTime / 60) % 60) < 10 ? "0"
							+ ((avgInSleepTime / 60) % 60): ((avgInSleepTime / 60) % 60));
					String avgOutSleepTimeFormat = ((avgOutSleepTime / 3600) < 10 ? "0"+ (avgOutSleepTime / 3600) : (avgOutSleepTime / 3600))
					+ ":"+ (((avgOutSleepTime / 60) % 60) < 10 ? "0"+ ((avgOutSleepTime / 60) % 60): ((avgOutSleepTime / 60) % 60));
					tvAvgText.setText("平均起床时间点："+avgOutSleepTimeFormat);
					float avglength=((float)avgTotalSleepTime)/60;
					int temps=(int)((((double)complateGetUpCount)/ hasDataCount) * 100);
					progerssbar.setProgress(temps);
					tvPro.setText(temps+ "%");
					tvComplete.setText("最近7天目标完成"+ temps + "% ,请继续加油");
					tvGoalLater.setText("最迟醒来时间点: "+ new SimpleDateFormat("HH:mm").format(re.iOutSleepTime_last));
					tvGoalBefore.setText("最早醒来时间点: "+ new SimpleDateFormat("HH:mm").format(re.iOutSleepTime_early));
					tvGoalBetween.setText("有" + complateGetUpCount + "天准时醒来");
					initSleepAnalyzeDocumen(avgInSleepTimeFormat,avgOutSleepTimeFormat,avglength,type);
				}else{
					tvAvgText.setText("平均起床时间点：--:--");
					tvComplete.setText("最近7天目标完成0% ,请继续加油");
					tvGoalLater.setText("最迟醒来时间点:--:--");
					tvGoalBefore.setText("最早醒来时间点: --:--");
					tvGoalBetween.setText("有" + complateGetUpCount + "天准时醒来");
					tvPro.setText("0%");
					tvConclusion.setText(str[type]);
				}
			}

		} catch (Exception e) {
		}
	}

	/**
	 * 显示睡眠分析结果
	 */
	private void initSleepAnalyzeDocumen(String sleepTime,String getupTime,float aveSleepLength, int type) {
		try {
			// 睡眠分析结果数据，0 睡觉时间分析结果 ，1睡眠时长分析结果，2起床时间分析结果
			List<String> analyzeResultBySleepTime = new SleepDocumentUtils()
					.getAnalyzeResultBySleepTime(this, sleepTime, aveSleepLength,getupTime);
			if (analyzeResultBySleepTime != null && analyzeResultBySleepTime.size() == 3) {
				if(type==0){
					tvConclusion.setText(analyzeResultBySleepTime.get(1));
				}else if(type==1){
					tvConclusion.setText(analyzeResultBySleepTime.get(0));
				}else if(type==2){
					tvConclusion.setText(analyzeResultBySleepTime.get(2));
				}
			}
		} catch (Exception e) {
		}
	}


	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.btn_title_right:
			if(type==0)
				startActivity(new Intent(this,MonthDataAnalysisActivity.class).putExtra("type", "0").putExtra("dataType", "2"));
			else if(type==1)
				startActivity(new Intent(this,MonthDataAnalysisActivity.class).putExtra("type", "1").putExtra("dataType", "2"));
			else if(type==2)
				startActivity(new Intent(this,MonthDataAnalysisActivity.class).putExtra("type", "2").putExtra("dataType", "2"));
			break;
		default:
			break;
		}
	}
}
