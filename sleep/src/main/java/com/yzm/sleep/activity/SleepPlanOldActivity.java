package com.yzm.sleep.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.background.DataUtils;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.background.MytabOperate;
//import com.yzm.sleep.background.PermanentService;
import com.yzm.sleep.background.SleepInfo;
import com.yzm.sleep.model.MyAlertDialog;
import com.yzm.sleep.model.MyAlertDialog.MyOnClickListener;
import com.yzm.sleep.model.RollPickerDialog;
import com.yzm.sleep.model.RollPickerDialog.RollCallBack;
import com.yzm.sleep.tools.DealSetTimeUtils;
import com.yzm.sleep.utils.PreManager;

/**
 * 睡眠计划
 * @author tianuxn
 */
@SuppressLint("SimpleDateFormat") 
public class SleepPlanOldActivity extends BaseActivity implements OnSeekBarChangeListener{

	private TextView bestSleepTime, sleepTime, time1, time2, time3, time4,time5, wakeTime;
	private SeekBar sbTimeValue;
	private Context context;
	private String sleepDate/*入睡时间*/,getupDate/*起床时间*/;
	private double indexTime=6;//起始时间
	private double recommendTimeGrow,/*推荐睡眠时长*/setTimeGrow;/*设置睡眠时长*/
	private boolean isFirst;//是否是第一次登陆
	private RollPickerDialog  dialog;
	private MyAlertDialog alDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sleep_plan_old);
		isFirst = getIntent().getBooleanExtra("isFirst", false);
		context=this;
		initView();
		initData();
	}

	private void initView() {
		findViewById(R.id.back).setOnClickListener(this);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("睡眠计划");
		bestSleepTime = (TextView) findViewById(R.id.tv_analyzedate_scope);
		sleepTime = (TextView) findViewById(R.id.sleep_tiem);
		sbTimeValue = (SeekBar) findViewById(R.id.sb_time_value);
		Button right=(Button) findViewById(R.id.btn_title_right);
		right.setText("保存");
		right.setCompoundDrawables(null, null, null, null);
		right.setOnClickListener(this);
		right.setVisibility(View.VISIBLE);
		
		sbTimeValue.setMax(240);
		sbTimeValue.setOnSeekBarChangeListener(this);
		time1 = (TextView) findViewById(R.id.time1);
		time2 = (TextView) findViewById(R.id.time2);
		time3 = (TextView) findViewById(R.id.time3);
		time4 = (TextView) findViewById(R.id.time4);
		time5 = (TextView) findViewById(R.id.time5);
		wakeTime=(TextView) findViewById(R.id.tv_wake_time);
		wakeTime.setOnClickListener(this);
	}
	
	private void initData() {
		getupDate=PreManager.instance().getGetupTime_Setting(context);
		sleepDate=PreManager.instance().getSleepTime_Setting(context);
		wakeTime.setText(getupDate);

		bestSleepTime.setTextSize(30);
		recommendTimeGrow=PreManager.instance().getRecommendTarget(context);
		bestSleepTime.setText(((recommendTimeGrow-1)+"-"+(recommendTimeGrow+1)+"h").replace(".0", ""));

		//设置刻度下面显示的时间
		indexTime=recommendTimeGrow-2;//重置 刻度起始时间
		time1.setText(indexTime+"h");
		time2.setText((indexTime+1)+"h");
		time3.setText((indexTime+2)+"h");
		time4.setText((indexTime+3)+"h");
		time5.setText((indexTime+4)+"h");
		
		//计算设置的睡眠时长(起床时间-入睡时间)
		setTimeGrow=countLengthByTime(sleepDate,getupDate);
		if(!isFirst){
			if(setTimeGrow< indexTime || setTimeGrow>(recommendTimeGrow+2)){
				sleepTime.setText("计划睡眠时长："+recommendTimeGrow+"小时");
				sbTimeValue.setProgress(120);
			}else{
				sleepTime.setText("计划睡眠时长："+setTimeGrow+"小时");
				int pro=(int) ((setTimeGrow-indexTime)*60);
				sbTimeValue.setProgress(pro);
			}
		}else{
			sleepTime.setText("计划睡眠时长："+recommendTimeGrow+"小时");
			sbTimeValue.setProgress(120);
		}
	}
	
	@SuppressLint("SimpleDateFormat") 
	private float countLengthByTime(String sleepTime, String getUpTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		try {
			long sleepTimeLong = sdf.parse(sleepTime).getTime();
			long getUpTimeLong = sdf.parse(getUpTime).getTime();
			long totalTime = getUpTimeLong - sleepTimeLong;
			if(totalTime < 0)
				totalTime += 24 * 60 * 60 * 1000;
			return totalTime/((float)60 * 60 * 1000);       
		} catch (Exception e) {
		}
		return 0;
	}

	private void complete(){
		PreManager.instance().saveHasSetAlarm(this, true);
		double time=indexTime+(sbTimeValue.getProgress()/30*0.5);
		try{
			String getupHore=getupDate.split(":")[0],getupMin=getupDate.split(":")[1];
			int getupH=Integer.parseInt(getupHore);
			int getupM=Integer.parseInt(getupMin);
			int sleepMinH=(int) ((time*60)/60);
			int sleepMinM=(int) ((time*60)%60);
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
			PreManager.instance().saveOldT1(context, oldt1);
			PreManager.instance().saveOldT2(context, oldt2);
			
			//处理新区间
			PreManager.instance().saveSleepTime_Setting(this,(tempH<10?("0"+tempH):tempH)+":"+(tempM<10?("0"+tempM):tempM));
			PreManager.instance().saveGetupTime_Setting(this,getupDate);
		
			//从上班模式中获取数据并保存数据
			final int startTime =tempH * 60 + tempM;
			final int endTime =Integer.parseInt(getupHore) * 60 + getupM;
			setTime(startTime,endTime);
			if(!isFirst) {
				try{
					toastMsg("正在重新设置时间，请稍等");
					String date = DataUtils.getRecordDate(new Date());
					long newt1 = DataUtils.getAllerateStartTime() / (60*1000);
					long newt2 = DataUtils.getAllerateStopTime() / (60*1000);
					DealSetTimeUtils.getInstance().setTime(context, date, System.currentTimeMillis(), oldt11, oldt21, newt1, newt2);
				}catch(Exception e) {
				}
			}

		}catch(Exception e){}
	}

	private void showDialog(){
		alDialog  = new MyAlertDialog(context,R.style.bottom_animation);
		
		alDialog.setCanceledOnTouchOutside(true);
		alDialog.show();
		alDialog.setTV1("是否保存修改的睡眠计划",View.VISIBLE);
		alDialog.setTV2("", null, View.GONE);
		alDialog.setTV3("", null, View.GONE);
		
		alDialog.setTV4("保存", new MyOnClickListener() {
			@Override
			public void Onclick(View v) {
				save();
				alDialog.dismiss();
				alDialog=null;
			}
		}, View.VISIBLE);
		
		alDialog.setTVbottom("放弃", new MyOnClickListener() {
			@Override
			public void Onclick(View v) {
				PreManager.instance().setIsUpdata(context, false);
				AppManager.getAppManager().finishActivity();
				alDialog.dismiss();
				alDialog = null;
			}
		}, View.VISIBLE);

	}
	
	private void save(){
		PreManager.instance().setIsUpdata(context, false);
		complete();
//		startPermanentService();
		if(isFirst){//如果是第一次，跳转到homeactivity
			Intent intentHome = new Intent(context, HomeActivity.class);
			intentHome.putExtra("isSetting", true);
			startActivity(intentHome);
			AppManager.getAppManager().finishActivity();
		}else{
			AppManager.getAppManager().finishActivity();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			MobclickAgent.onEvent(this, "622");
			if(PreManager.instance().getIsUpdata(context)){
				showDialog();
			}else
				AppManager.getAppManager().finishActivity();
		}
		return true;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			MobclickAgent.onEvent(this, "622");
			if(PreManager.instance().getIsUpdata(context)){
				showDialog();
			}else
				AppManager.getAppManager().finishActivity();
			break;
		case R.id.btn_title_right:
			save();
			break;
		case R.id.tv_wake_time:
			MobclickAgent.onEvent(this, "621");
			PreManager.instance().setIsUpdata(context, true);
			dialog=new RollPickerDialog(context, new RollCallBack() {
				
				@Override
				public void setResaultRoll(int select1, int select2, int select3) {
					getupDate=(select1 < 10 ? ("0"+select1) : select1+"")+ ":" +(select2 < 10 ? ("0"+select2) : select2 +"");
					wakeTime.setText(getupDate);
					dialog.dismiss();
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
		default:
			break;
		}
	}
	

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		sbTimeValue.setProgress(progress);		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		PreManager.instance().setIsUpdata(context, true);
		int progress = sbTimeValue.getProgress();
		if (progress <15) {
			sbTimeValue.setProgress(0);
			sleepTime.setText("计划睡眠时长："+indexTime+"小时");
			setTimeGrow=indexTime;
		} else if (progress >=15 && progress < 45) {
			sleepTime.setText("计划睡眠时长："+(indexTime+0.5)+"小时");
			setTimeGrow=indexTime+0.5;
			sbTimeValue.setProgress(30);
		} else if (progress >= 45 && progress <= 75) {
			sleepTime.setText("计划睡眠时长："+(indexTime+1)+"小时");
			setTimeGrow=indexTime+1;
			sbTimeValue.setProgress(60);
		}else if (progress >= 75 && progress <= 105) {
			sleepTime.setText("计划睡眠时长："+(indexTime+1.5)+"小时");
			setTimeGrow=indexTime+1.5;
			sbTimeValue.setProgress(90);
		 } else if (progress >= 105 && progress <= 135) {
			 sleepTime.setText("计划睡眠时长："+(indexTime+2)+"小时");
			 setTimeGrow=indexTime+2;
			 sbTimeValue.setProgress(120);
		} else if (progress >= 135 && progress <= 165) {
			sleepTime.setText("计划睡眠时长："+(indexTime+2.5)+"小时");
			setTimeGrow=indexTime+2.5;
			sbTimeValue.setProgress(150);
		} else if (progress >= 165 && progress <= 195) {
			sleepTime.setText("计划睡眠时长："+(indexTime+3)+"小时");
			setTimeGrow=indexTime+3;
			sbTimeValue.setProgress(180);
		}else if (progress >= 195 && progress <= 225) {
			sleepTime.setText("计划睡眠时长："+(indexTime+3.5)+"小时");
			setTimeGrow=indexTime+3.5;
			sbTimeValue.setProgress(210);
		}else if (progress>=225) {
			sleepTime.setText("计划睡眠时长："+(indexTime+4)+"小时");
			setTimeGrow=indexTime+4;
			sbTimeValue.setProgress(240);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SleepPlan"); 
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SleepPlan");
		MobclickAgent.onPause(this);
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
	
	/**
	 * 启动加速度进程
	 */
//	private void startPermanentService() {
//		Intent newIntent = new Intent(SleepPlanOldActivity.this,PermanentService.class);
//    	startService(newIntent);
//	}
}
