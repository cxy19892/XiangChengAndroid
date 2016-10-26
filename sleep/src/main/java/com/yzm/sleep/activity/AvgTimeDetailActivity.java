package com.yzm.sleep.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.NewRoundProgressBar;
import com.yzm.sleep.R;
import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.render.GetSleepAvgTimeValueClass.AvgTimeResult;
import com.yzm.sleep.utils.GetTimeParamItem.GetTimeResult;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.SleepDocumentUtils;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.widget.SleepTimePointTable;

/**
 * 平均起床时间点 平均入睡时间点
 * @author tianxun
 */
public class AvgTimeDetailActivity extends BaseActivity {
	
	private SleepTimePointTable sleepTimePointTable;
	private TextView tvTitle,tvTime,tvComplete,tvGoalBefore,tvGoalLater,tvGoalBetween,tipsDesc,tvPro;
	private NewRoundProgressBar progerssbar;
	private String sleepDate/*入睡时间*/,getupDate/*起床时间*/;
	private boolean isSleep;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sleeporgetupavgtime_detail);
		isSleep = getIntent().getBooleanExtra("isSleep", false);
		findViewById(R.id.back).setOnClickListener(this);
		TextView title=(TextView) findViewById(R.id.title);
		title.setText(getIntent().getStringExtra("title"));
		Button right=(Button) findViewById(R.id.btn_title_right);
		right.setCompoundDrawables(null, null, null, null);
		right.setText("更多");
		right.setOnClickListener(this);
		right.setVisibility(View.VISIBLE);
		getupDate = PreManager.instance().getGetupTime_Setting(this);
		sleepDate = PreManager.instance().getSleepTime_Setting(this);
		initView();
		initData();
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		if (isSleep) {
			MobclickAgent.onPageStart("App_WeeklyReport_Avg_SleepTime"); 
		}else {
			MobclickAgent.onPageStart("App_WeeklyReport_Avg_WakeupTime"); 
		}
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (isSleep) {
			MobclickAgent.onPageEnd("App_WeeklyReport_Avg_SleepTime");
		}else{
			MobclickAgent.onPageEnd("App_WeeklyReport_Avg_WakeupTime");
		}
		MobclickAgent.onPause(this);
	}

	private void initView() {
		sleepTimePointTable=(SleepTimePointTable) findViewById(R.id.soft_week_data_view2);
		tvTitle=(TextView) findViewById(R.id.tv_title);
		tvTime=(TextView) findViewById(R.id.tv_time);
		progerssbar=(NewRoundProgressBar) findViewById(R.id.progerssbar);
		tvComplete=(TextView) findViewById(R.id.tv_complete);
		tvComplete.setText("0%");
		tvGoalBefore=(TextView) findViewById(R.id.tv_goal_before);
		tvGoalLater=(TextView) findViewById(R.id.tv_goal_later);
		tvGoalBetween=(TextView) findViewById(R.id.tv_goal_between);
		tipsDesc=(TextView) findViewById(R.id.tips_desc);
		tvPro=(TextView) findViewById(R.id.tv_pro);
	}

	@SuppressLint("SimpleDateFormat") 
	private void initData() {
		@SuppressWarnings("unchecked")
		ArrayList<SoftDayData> softList = (ArrayList<SoftDayData>)getIntent().getSerializableExtra("softList");
		AvgTimeResult avgResult = (AvgTimeResult) getIntent().getSerializableExtra("avgResult");
		GetTimeResult getTimeResult = (GetTimeResult) getIntent().getSerializableExtra("getTimeResult");
		
		int hasDataCount = getIntent().getIntExtra("hasDataCount", 0);
		int compalteSleepCount = 0, compalteGetupCount = 0;
		int size = softList== null ? 0 : softList.size();
		for (int i = 0; i < size; i++) {
			
			int inSleepTime = TimeFormatUtil.timeToMiss(softList.get(i).getSleepTime());
			int outSleepTime = TimeFormatUtil.timeToMiss(softList.get(i).getGetUpTime());
			
			//统计入睡达标的天数
			long targetS=TimeFormatUtil.timeToMiss(sleepDate);//目标入睡时间
			long temp=inSleepTime - targetS;
			if(temp >= -15*60 && temp <= 15*60)
				compalteSleepCount+=1;
			
			//统计起床达标的天数
			long targetu=TimeFormatUtil.timeToMiss(getupDate);
			long tempup=outSleepTime - targetu;
			if(tempup >= -15*60 && tempup <= 15*60)
				compalteGetupCount+=1;
		}
		
		tvTitle.setText((isSleep?"平均入睡时间点":"平均醒来时间点"));
		if(avgResult != null){
			int avgInSleepTime = avgResult.iAvgInSleepTime;
			int avgOutSleepTime = avgResult.iAvgOutSleepTime;
			String avgInSleepTimeFormat = ((avgInSleepTime / 3600) < 10 ? "0"+ (avgInSleepTime / 3600) : (avgInSleepTime / 3600))+ ":"+ (((avgInSleepTime / 60) % 60) < 10 ? "0"
					+ ((avgInSleepTime / 60) % 60): ((avgInSleepTime / 60) % 60));
			String avgOutSleepTimeFormat = ((avgOutSleepTime / 3600) < 10 ? "0"+ (avgOutSleepTime / 3600) : (avgOutSleepTime / 3600))
			+ ":"+ (((avgOutSleepTime / 60) % 60) < 10 ? "0"+ ((avgOutSleepTime / 60) % 60): ((avgOutSleepTime / 60) % 60));
			tvTime.setText(isSleep ? avgInSleepTimeFormat : avgOutSleepTimeFormat);
			
			initSleepAnalyzeDocumen(avgInSleepTimeFormat, avgOutSleepTimeFormat, 0, isSleep);
		}else{
			tvTime.setText("--:--");
		}
		
		if (isSleep) {
			sleepTimePointTable.setData(softList,sleepDate, 0);
		}else{
			sleepTimePointTable.setData(softList,getupDate, 1);
		}
		
		progerssbar.setCenterText("");
		
	    if(isSleep){
	    	if (hasDataCount > 0 && getTimeResult != null) {
				progerssbar.setProgress((int)((((float)compalteSleepCount)/hasDataCount)*100));
				tvPro.setText((int)((((float)compalteSleepCount)/hasDataCount)*100) + "%");
				tvComplete.setText("最近7天目标完成"+((int)((((float)compalteSleepCount)/hasDataCount)*100))+"% ,请继续加油");
				tvGoalBefore.setText("最早睡觉时间点: "+new SimpleDateFormat("HH:mm").format(getTimeResult.iInSleepTime_early));
				tvGoalLater.setText("最迟睡觉时间点: "+new SimpleDateFormat("HH:mm").format(getTimeResult.iInSleepTime_last));
				tvGoalBetween.setText("有"+compalteSleepCount+"天准时睡觉");
			}else{
				tipsDesc.setText("好的睡眠习惯，能够保证白天精力充沛、容颜娇好。");
	    		tvComplete.setText("最近7天目标完成0% ,请继续加油");
				tvPro.setText("0%");
				tvGoalBefore.setText("最早睡觉时间点: --:--");
				tvGoalLater.setText("最迟睡觉时间点: --:--");
			}
	    }else{
	    	if (hasDataCount >= 0 && getTimeResult != null) {
				progerssbar.setProgress((int)((((float)compalteGetupCount)/hasDataCount)*100));
				tvPro.setText((int)((((float)compalteGetupCount)/hasDataCount)*100) + "%");
				tvComplete.setText("最近7天目标完成"+((int)((((float)compalteGetupCount)/hasDataCount)*100))+"% ,请继续加油");
				tvGoalBefore.setText("最早醒来时间点: "+new SimpleDateFormat("HH:mm").format(getTimeResult.iOutSleepTime_early));
				tvGoalLater.setText("最迟醒来时间点: "+new SimpleDateFormat("HH:mm").format(getTimeResult.iOutSleepTime_last));
				tvGoalBetween.setText("有"+compalteGetupCount+"天准时起床");
			}else{
				tipsDesc.setText("好的睡眠习惯，能够保证白天精力充沛、容颜娇好。");
	    		tvComplete.setText("最近7天目标完成0% ,请继续加油");
				tvPro.setText("0%");
				tvGoalBefore.setText("最早醒来时间点: --:--");
				tvGoalLater.setText("最迟睡觉时间点: --:--");
			}
	    }
	}
	
	
	/**
	 * 显示睡眠分析结果
	 */
	private void initSleepAnalyzeDocumen(String sleepTime,String getupTime,float aveSleepLength,boolean isSleep) {
		try {
			//睡眠分析结果数据，0 睡觉时间分析结果 ，1睡眠时长分析结果，2起床时间分析结果
			List<String> analyzeResultBySleepTime = new SleepDocumentUtils().getAnalyzeResultBySleepTime(this,sleepTime,aveSleepLength,getupTime);
			if (analyzeResultBySleepTime != null && analyzeResultBySleepTime.size() == 3) {
				if(isSleep)
					tipsDesc.setText(analyzeResultBySleepTime.get(0));
				else
					tipsDesc.setText(analyzeResultBySleepTime.get(2));
			}
		} catch (Exception e) {
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.btn_title_right:
			if(isSleep)
				startActivity(new Intent(this,MonthDataAnalysisActivity.class).putExtra("type", "1"));
			else
				startActivity(new Intent(this,MonthDataAnalysisActivity.class).putExtra("type", "2"));
			break;
		default:
			break;
		}
	}
}
