package com.yzm.sleep.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.NewRoundProgressBar;
import com.yzm.sleep.R;
import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.utils.CalenderUtil;
import com.yzm.sleep.utils.GetTimeParamItem;
import com.yzm.sleep.utils.GetTimeParamItem.GetTimeResult;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.SleepDocumentUtils;
import com.yzm.sleep.utils.SleepUtils;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.widget.SleepTimePointTable;

/**
 * 平均起床时间点 平均入睡时间点
 * @author tianxun
 *
 */
public class SleepOrGetUpAvgTimeDetail extends BaseActivity {
	
	private SleepTimePointTable sleepTimePointTable;
	private TextView tvTitle,tvTime,tvComplete,tvGoalBefore,tvGoalLater,tvGoalBetween,tipsDesc,tvPro;
	private NewRoundProgressBar progerssbar;
	private List<SoftDayData> weekDatas;
	private String alarmSleepTime, alarmGetUpTime;
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
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DATE, -1);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SoftDayData dayData = SleepUtils.getDaySleepData(this,sdf1.format(ca.getTime()), "1");
		alarmSleepTime = PreManager.instance().getSleepTime_Setting(this);
		alarmGetUpTime = PreManager.instance().getGetupTime_Setting(this);
		weekDatas = new ArrayList<SoftDayData>();
		List<String> listday= CalenderUtil.getLastSevenDay(7);
		for (String string : listday) {
			weekDatas.add(SleepUtils.getDaySleepData(this,string, "1"));
		}
		// 数据库有当天睡眠计划的取睡眠计划
		try {
			String startTime = dayData.getStartTime();
			String endTime = dayData.getEndTime();
			if (startTime != null)
				alarmSleepTime = TimeFormatUtil.minToTime(Integer.parseInt(startTime));
			if (endTime != null)
				alarmGetUpTime = TimeFormatUtil.minToTime(Integer.parseInt(endTime));
		} catch (Exception e) {}
		
		String[] sleepgetup = SleepUtils.getAvgSleepTimeAndGetupTime(weekDatas);
		tvTitle.setText((isSleep?"平均入睡时间点":"平均醒来时间点"));
		tvTime.setText(isSleep?sleepgetup[0]:sleepgetup[1]);
		
		if (isSleep) {
			sleepTimePointTable.setData(weekDatas,alarmSleepTime, 0);
		}else{
			sleepTimePointTable.setData(weekDatas,alarmGetUpTime, 1);
		}
		
		progerssbar.setCenterText("");
		
		int countDay1=0;
		double completSleep=0,completgetUp=0;
		ArrayList<GetTimeParamItem> list=new ArrayList<GetTimeParamItem>();
	    for (int i=0;i<(weekDatas==null? 0:weekDatas.size());i++) {
			if(weekDatas.get(i)!=null && !TextUtils.isEmpty(weekDatas.get(i).getSleepTime())&&!TextUtils.isEmpty(weekDatas.get(i).getGetUpTime())){
				countDay1++;
				String dateSleep=weekDatas.get(i).getSleepTime();
				String dateGetUp=weekDatas.get(i).getGetUpTime();
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
					//统计入睡达标的天数
					long targets=sdf.parse(alarmSleepTime).getTime();//目标入睡时间
					int targetsM=(int) (targets/(60 * 1000));
					long sleepTimes=sdf.parse(dateSleep).getTime();//实际入睡时间
					int sleepTimeM=(int) (sleepTimes/(60 * 1000));
					int temp=sleepTimeM-targetsM;
					if(temp>=-15 && temp<=15)
						completSleep+=1;
					
					//统计起床达标的天数
					long targetu=sdf.parse(alarmGetUpTime).getTime();
					int targetMu=(int) (targetu/(60 * 1000));
					long sleepTimeu=sdf.parse(dateGetUp).getTime();
					int upTimeM=(int) (sleepTimeu/(60 * 1000));
					int tempup=upTimeM-targetMu;
					if(tempup>=-15 && tempup<=15)
						completgetUp+=1;
					
					//提取数据 便于计算最早最迟
					GetTimeParamItem item=new GetTimeParamItem();
					item.iBelongDate= new SimpleDateFormat("yyyy-MM-dd").parse(weekDatas.get(i).getDate()).getTime();
					String sleepDate=new SimpleDateFormat("yyyy-MM-dd").format(Long.parseLong(weekDatas.get(i).getSleepTimeLong()));
					item.iInSleepDate=new SimpleDateFormat("yyyy-MM-dd").parse(sleepDate).getTime();
					String sleepTime=new SimpleDateFormat("HH:mm").format(Long.parseLong(weekDatas.get(i).getSleepTimeLong()));
					item.iInSleepTime= new SimpleDateFormat("HH:mm").parse(sleepTime).getTime();
					
					String getUpDate=new SimpleDateFormat("yyyy-MM-dd").format(Long.parseLong(weekDatas.get(i).getGetUpTimeLong()));
					item.iOutSleepDate=new SimpleDateFormat("yyyy-MM-dd").parse(getUpDate).getTime();
					String getupTime=new SimpleDateFormat("HH:mm").format(Long.parseLong(weekDatas.get(i).getGetUpTimeLong()));
					item.iOutSleepTime= new SimpleDateFormat("HH:mm").parse(getupTime).getTime();
					list.add(item);
					
				} catch (Exception e) {
				}
			}
		}
	    GetTimeResult re = GetTimeParamItem.ProcTimeData(list);//最早 最迟 返回数据
	    String aveLength=SleepUtils.getAverageSleepLengthString(this, weekDatas);
	    String[] times = aveLength.split(":");
	    int sleepM=Integer.parseInt(times[0])*60+Integer.parseInt(times[1]);
	    float avgLenght=((float)sleepM)/60;
	    if(isSleep){
	    	if (countDay1==0) {
	    		tipsDesc.setText("好的睡眠习惯，能够保证白天精力充沛、容颜娇好。");
	    		tvComplete.setText("最近7天目标完成0% ,请继续加油");
				tvPro.setText("0%");
				tvGoalBefore.setText("最早睡觉时间点: --:--");
				tvGoalLater.setText("最迟睡觉时间点: --:--");
			}else{
				initSleepAnalyzeDocumen(sleepgetup[0],sleepgetup[1],avgLenght,isSleep);
				progerssbar.setProgress((int)((completSleep/countDay1)*100));
				tvPro.setText(((int)((completSleep/countDay1)*100))+"%");
				tvComplete.setText("最近7天目标完成"+((int)((completSleep/countDay1)*100))+"% ,请继续加油");
				tvGoalBefore.setText("最早睡觉时间点: "+new SimpleDateFormat("HH:mm").format(re.iInSleepTime_early));
				tvGoalLater.setText("最迟睡觉时间点: "+new SimpleDateFormat("HH:mm").format(re.iInSleepTime_last));
				tvGoalBetween.setText(("有"+completSleep+"天准时睡觉").replace(".0", ""));
			}
	    }else{
	    	if (countDay1==0) {
	    		tipsDesc.setText("好的睡眠习惯，能够保证白天精力充沛、容颜娇好。");
	    		tvComplete.setText("最近7天目标完成0% ,请继续加油");
				tvPro.setText("0%");
				tvGoalBefore.setText("最早醒来时间点: --:--");
				tvGoalLater.setText("最迟睡觉时间点: --:--");
			}else{
				initSleepAnalyzeDocumen(sleepgetup[0],sleepgetup[1],avgLenght,isSleep);
				progerssbar.setProgress((int)((completgetUp/countDay1)*100));
				tvPro.setText(((int)((completgetUp/countDay1)*100))+"%");
				tvComplete.setText("最近7天目标完成"+((int)((completgetUp/countDay1)*100))+"% ,请继续加油");
				tvGoalBefore.setText("最早醒来时间点: "+new SimpleDateFormat("HH:mm").format(re.iOutSleepTime_early));
				tvGoalLater.setText("最迟醒来时间点: "+new SimpleDateFormat("HH:mm").format(re.iOutSleepTime_last));
				tvGoalBetween.setText(("有"+completgetUp+"天准时起床").replace(".0", ""));
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
				startActivity(new Intent(this,MonthDataAnalysisActivity.class).putExtra("type", "1").putExtra("dataType", "1"));
			else
				startActivity(new Intent(this,MonthDataAnalysisActivity.class).putExtra("type", "2").putExtra("dataType", "1"));
			
			
		break;
		default:
			break;
		}
	}
}
