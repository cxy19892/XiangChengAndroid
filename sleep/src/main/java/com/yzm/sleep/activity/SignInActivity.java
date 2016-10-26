package com.yzm.sleep.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.background.SignInDBOperation;
import com.yzm.sleep.model.CalenderSelectDialog;
import com.yzm.sleep.model.CalenderSelectDialog.SelectDayDateListener;
import com.yzm.sleep.model.RollPickerDialog;
import com.yzm.sleep.model.RollPickerDialog.RollCallBack;
import com.yzm.sleep.model.SignInData;
import com.yzm.sleep.model.SpeRollPickerDialog;
import com.yzm.sleep.utils.CalenderUtil;
import com.yzm.sleep.utils.DateOperaUtil;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceGetBuyUrlCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetSignInfoCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetSignInfoParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSignInCallBack4_2_1;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.SleepTimeData;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.utils.XiangchengProcClass;

/**
 * 签到
 * @author tianxun
 * @params date 格式 yyyy-MM-dd
 */
@SuppressLint("SimpleDateFormat") 
public class SignInActivity extends BaseActivity {
	
	private TextView title, gobedTime, tryTime, spendTime, wakeCount, wakeTimeLenght, getupTime, earlyWakLength, leaveBedTime, showMore;
	private RollPickerDialog dialog;
	private SpeRollPickerDialog spedialog;
	private CalenderSelectDialog caDialog;
	private ArrayList<String> hour;
	private ArrayList<String> minute;
	private ArrayList<String> list3;
	private ArrayList<String> list4;
	private SimpleDateFormat sdf1, sdf, simp, sdf2; 
	private Button complate;
	private String type;
	private LinearLayout rlQuestionOne, rlQuestionFore, rlQuestionFive, rlQuestionSeven, rlQuestionEight;
	private boolean isRelate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signin);
		sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
		sdf = new SimpleDateFormat("HH:mm");
		simp = new SimpleDateFormat("yyyy-MM-dd");
		sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if(TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(this)))
			type = "1";
		else
			type = "2";
		
		initData();
		initView();
	}
	
	/**
	 * 初始化选择的数据
	 */
	private void initData(){
		
		hour = new ArrayList<String>();
		for (int i = 0; i < 24; i++) {
			hour.add(i < 10 ? ("0"+i) : String.valueOf(i));
		}
		minute = new ArrayList<String>();
		for (int i = 0; i < 60; i++) {
			minute.add(i < 10 ? ("0"+i) : String.valueOf(i));
		}
		list3 = new ArrayList<String>();
		list3.add("0分钟");
		list3.add("5分钟");
		list3.add("10分钟");
		list3.add("15分钟");
		list3.add("30分钟");
		list3.add("45分钟");
		list3.add("1小时");
		list3.add("1小时30分");
		list3.add("2小时");
		list3.add("2小时30分");
		list3.add("3小时");
		list3.add("3小时30分");
		list3.add("4小时");
		list3.add("4小时30分");
		list3.add("5小时");
		list3.add("5小时30分");
		list3.add("6小时");
		list3.add("6小时30分");
		list3.add("7小时");
		list3.add("7小时30分");
		list3.add("8小时");
		
		list4 = new ArrayList<String>();
		for (int i = 0; i < 9; i++) {
			list4.add(i+"次");
		}
		list4.add("8次以上");
	}

	private void initView(){
		findViewById(R.id.back).setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		rlQuestionOne = (LinearLayout) findViewById(R.id.rl_question_one);
		rlQuestionFore = (LinearLayout) findViewById(R.id.rl_question_four);
		rlQuestionFive = (LinearLayout) findViewById(R.id.rl_question_five);
		rlQuestionSeven = (LinearLayout) findViewById(R.id.rl_question_seven);
		rlQuestionEight = (LinearLayout) findViewById(R.id.rl_question_eight);
		
		gobedTime = (TextView) findViewById(R.id.gobed_time);
		gobedTime.setOnClickListener(this);
		tryTime = (TextView) findViewById(R.id.try_time);
		tryTime.setOnClickListener(this);
		spendTime = (TextView) findViewById(R.id.spend_sleep_time);
		spendTime.setOnClickListener(this);
		wakeCount = (TextView) findViewById(R.id.wake_count);
		wakeCount.setOnClickListener(this);
		wakeTimeLenght = (TextView) findViewById(R.id.wake_time_lenght);
		wakeTimeLenght.setOnClickListener(this);
		getupTime = (TextView) findViewById(R.id.getup_time);
		getupTime.setOnClickListener(this);
		earlyWakLength = (TextView) findViewById(R.id.early_wake_length);
		earlyWakLength.setOnClickListener(this);
		leaveBedTime = (TextView) findViewById(R.id.leave_bed_time);
		leaveBedTime.setOnClickListener(this);
		showMore = (TextView) findViewById(R.id.show_more);
		showMore.setOnClickListener(this);
		
		complate = (Button) findViewById(R.id.btn_complate);
		complate.setOnClickListener(this);
		findViewById(R.id.btn_buying).setOnClickListener(this);
		
		Button right = (Button) findViewById(R.id.btn_title_right);
		right.setCompoundDrawables(null, null, null, null);
		right.setText("日历");
		right.setOnClickListener(this);
		right.setVisibility(View.VISIBLE);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		title.setTag(simp.format(calendar.getTime()));
		title.setText(CalenderUtil.getStrByDate(simp.format(calendar.getTime())));
		getServerSignData(title.getTag().toString());
		
		tryTime.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if(isRelate && TextUtils.isEmpty(gobedTime.getText().toString())){
					gobedTime.setText(tryTime.getText().toString());
				}
			}
		});
		
		getupTime.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if(isRelate && TextUtils.isEmpty(leaveBedTime.getText().toString())){
					leaveBedTime.setText(getupTime.getText().toString());
				}
			}
		});
	}
	
	private void showMoreView(){
		isRelate = true;
		rlQuestionOne.setVisibility(View.VISIBLE);
		rlQuestionFore.setVisibility(View.VISIBLE);
		rlQuestionSeven.setVisibility(View.VISIBLE);
		rlQuestionEight.setVisibility(View.VISIBLE);
		showMore.setVisibility(View.GONE);
		try {
			if(Integer.parseInt(wakeCount.getTag().toString()) >0)
				rlQuestionFive.setVisibility(View.VISIBLE);
		} catch (Exception e) {
		}
		
		String tt = tryTime.getText().toString();
		String gbt = gobedTime.getText().toString();
		String gut = getupTime.getText().toString();
		String lbt = leaveBedTime.getText().toString();
		if(!TextUtils.isEmpty(tt) && TextUtils.isEmpty(gbt)){
			gobedTime.setText(tt);
		}
		if(!TextUtils.isEmpty(gut) && TextUtils.isEmpty(lbt)){
			leaveBedTime.setText(gut);
		}
	}
	
	private void hindMoreView(){
		rlQuestionOne.setVisibility(View.GONE);
		rlQuestionFore.setVisibility(View.GONE);
		rlQuestionFive.setVisibility(View.GONE);
		rlQuestionSeven.setVisibility(View.GONE);
		rlQuestionEight.setVisibility(View.GONE);
		showMore.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 显示日历选择控件
	 */
	private void showCalendar(){
		caDialog = new CalenderSelectDialog(this,getScreenWidth(),title.getTag().toString(), new SelectDayDateListener() {
			@Override
			public void selectday(String dayDate) {
				try {
					long day = simp.parse(dayDate).getTime();
					Calendar calendar=Calendar.getInstance();
					calendar.add(Calendar.DAY_OF_MONTH, -1);
					
					if(day <= simp.parse(simp.format(calendar.getTime())).getTime()){
						title.setTag(dayDate);
						title.setText(CalenderUtil.getStrByDate(dayDate));
						hindMoreView();
						getServerSignData(dayDate);
					}else
						Util.show(SignInActivity.this, "不能玩穿越");
					
				} catch (Exception e) {
				}
			}
		});
		caDialog.show();
	}
	
	private ProgressUtils pro;
	/**
	 * 显示进度
	 */
	private void showPro() {
		if (pro == null) {
			pro = new ProgressUtils(this);
		}
		pro.show();
	}

	/**
	 * 取消进度
	 */
	private void cancelPro() {
		if (pro != null) {
			pro.dismiss();
			pro = null;
		}
	}

	
	/**
	 * 获取服务器签到数据
	 * @param date
	 */
	private void getServerSignData(final String date){
		GetSignInfoParams mParams = new GetSignInfoParams();
		mParams.my_int_id = PreManager.instance().getUserId(this);
		mParams.date = date.replace("-", "");
		mParams.type = type;
		showPro();
		new XiangchengMallProcClass(this).getSignInfo(mParams, new GetSignInfoCallBack() {
			
			@Override
			public void onSuccess(int icode, String response) {
				cancelPro();
				try {
					JSONObject json = new JSONObject(response);
					SignInData signIn = new SignInData();
					signIn.setDate(date);
					signIn.setSignInId(json.getString("qiandaoid"));
					signIn.setGoBedTime(String.valueOf(Long.parseLong(json.getString("ti_1"))*1000));
					signIn.setTrySleepTime(String.valueOf(Long.parseLong(json.getString("ti_2"))*1000));
					signIn.setHowLongSleepTime(Integer.parseInt(json.getString("ti_3")));
					signIn.setWakeCount(Integer.parseInt(json.getString("ti_4")));
					signIn.setHowLongWakeTime(Integer.parseInt(json.getString("ti_5")));
					signIn.setWakeUpTime(String.valueOf(Long.parseLong(json.getString("ti_6"))*1000));
					signIn.setWakeEarlyTime(Integer.parseInt(json.getString("ti_7")));
					signIn.setOutBedTime(String.valueOf(Long.parseLong(json.getString("ti_8"))*1000));
					doSignInData(signIn);
				} catch (Exception e) {
					clearData();
				}
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				cancelPro();
				clearData();
			}
		});
	}
	
	private void clearData(){
		complate.setTag("");
		gobedTime.setText("");
		tryTime.setText("");
		spendTime.setText("");
		wakeCount.setText("");
		wakeCount.setTag(0);
		wakeTimeLenght.setText("");
		rlQuestionFive.setVisibility(View.GONE);
		getupTime.setText("");
		earlyWakLength.setText("");
		leaveBedTime.setText("");
	}
	
	/**
	 * 设置数据
	 */
	private void doSignInData(SignInData data){
		try {
			clearData();
			
			if(!"0".equals(data.getGoBedTime())){
				gobedTime.setText(sdf.format(Long.parseLong(data.getGoBedTime())));
				gobedTime.setTag(sdf1.format(Long.parseLong(data.getGoBedTime())));
			}
			
			if(!"0".equals(data.getTrySleepTime())){
				tryTime.setText(sdf.format(Long.parseLong(data.getTrySleepTime())));
				tryTime.setTag(sdf1.format(Long.parseLong(data.getTrySleepTime())));
			}
			
			int hour = data.getHowLongSleepTime() / 60;
			int minute = data.getHowLongSleepTime() % 60;
			spendTime.setText((hour > 0) ? hour + "小时" + minute + "分钟": minute + "分钟");
			wakeCount.setText(data.getWakeCount() > 8 ? "8次以上" : data.getWakeCount() +"次");
			wakeCount.setTag(data.getWakeCount());
			
			if(data.getWakeCount() > 0){
				if(rlQuestionFore.getVisibility() == View.VISIBLE)
					rlQuestionFive.setVisibility(View.VISIBLE);
				int hour1 = data.getHowLongWakeTime() / 60;
				int minute1 = data.getHowLongWakeTime() % 60;
				wakeTimeLenght.setText((hour1 > 0) ? hour1 + "小时" + minute1 + "分钟": minute1 + "分钟");
			}else{
				rlQuestionFive.setVisibility(View.GONE);
				wakeTimeLenght.setText("");
			}
			
			if(!"0".equals(data.getWakeUpTime())){
				getupTime.setText(sdf.format(Long.parseLong(data.getWakeUpTime())));
				getupTime.setTag(sdf1.format(Long.parseLong(data.getWakeUpTime())));
			}
			
			int hour2 = data.getWakeEarlyTime() / 60;
			int minute2 = data.getWakeEarlyTime() % 60;
			earlyWakLength.setText((hour2 > 0) ? hour2 + "小时" + minute2 + "分钟": minute2 + "分钟");
			
			if(!"0".equals(data.getOutBedTime())){
				leaveBedTime.setText(sdf.format(Long.parseLong(data.getOutBedTime())));
				leaveBedTime.setTag(sdf1.format(Long.parseLong(data.getOutBedTime())));
			}
			if(!TextUtils.isEmpty(gobedTime.getText().toString()) || !TextUtils.isEmpty(wakeCount.getText().toString())
					|| !TextUtils.isEmpty(wakeTimeLenght.getText().toString()) || !TextUtils.isEmpty(earlyWakLength.getText().toString())
					|| !TextUtils.isEmpty(leaveBedTime.getText().toString())){
				showMoreView();
			}
			complate.setTag(data.getSignInId());
		} catch (Exception e) {
			clearData();
		}
	}
	
	/**
	 * 提交签到数据
	 */
 	private void complate(){
		final SignInData signIn = new SignInData();
		signIn.setDate(title.getTag().toString());
		//上床时间点 t1
		String gobed = gobedTime.getText().toString(); 
		//尝试入睡时间点 t2
		String trytime = tryTime.getText().toString(); 
		if(TextUtils.isEmpty(trytime)){
			Util.show(this, "请选择尝试睡觉时间");
			return;
		}
		//花费多久时间入睡 t3
		String spendtime = spendTime.getText().toString(); 
		if(TextUtils.isEmpty(spendtime)){
			Util.show(this, "请选择花费多长时间入睡");
			return;
		}
		signIn.setHowLongSleepTime(Util.getMinutes(spendtime));
		//中途醒来次数 t4
		int count = wakeCount.getTag() == null ? 0 :((Integer)wakeCount.getTag());
		signIn.setWakeCount(count);
		//中途醒来时长 t5
		if(count > 0 ){
			String wakelenght = wakeTimeLenght.getText().toString();
			signIn.setHowLongWakeTime(Util.getMinutes(wakelenght));
		}else
			signIn.setHowLongWakeTime(0);
		//醒来时间点 t6
		String getuptime = getupTime.getText().toString();
		if(TextUtils.isEmpty(getuptime)){
			Util.show(this, "请选择几点醒来");
			return;
		}
		//早起时长 t7
		String earlylength = earlyWakLength.getText().toString();
		signIn.setWakeEarlyTime(Util.getMinutes(earlylength));
		//离床时间段 t8
		String leavebed = leaveBedTime.getText().toString();
		
		if(!TextUtils.isEmpty(gobed) && !TextUtils.isEmpty(leavebed)){
			SleepTimeData date = new SleepTimeData();
			date.setDate(title.getTag().toString());
			date.setGbStr(gobed);
			date.setTsStr(trytime);
			date.setWuStr(getuptime);
			date.setObStr(leavebed);
			date = DateOperaUtil.comperaTime(date);
			if(!date.isState()){
				Util.show(this, date.getErrMsg());
				return;
			}
			
			signIn.setGoBedTime(String.valueOf(date.getGoBedTime()));
			signIn.setTrySleepTime(String.valueOf(date.getTrySleepTime()));
			signIn.setWakeUpTime(String.valueOf(date.getWakeUpTime()));
			signIn.setOutBedTime(String.valueOf(date.getOutBedTime()));
			signIn.setDeepsleep(0);
			signIn.setShallowsleep(0);
			
		}else if(!TextUtils.isEmpty(gobed) && TextUtils.isEmpty(leavebed)){
			SoftDayData dayData = new SoftDayData();
			long gobedLong = 0;
			try {
				dayData.setDate(title.getTag().toString());
				dayData.setSleepTime(trytime);
				dayData.setSleepTimeLong(String.valueOf(sdf2.parse(dayData.getDate()+" "+dayData.getSleepTime()).getTime()));
				dayData.setGetUpTime(getuptime);
				dayData.setGetUpTimeLong(String.valueOf(sdf2.parse(dayData.getDate()+" "+dayData.getGetUpTime()).getTime()));
				gobedLong = sdf2.parse(dayData.getDate() +" "+ gobed).getTime();
			} catch (Exception e) {
			}
			dayData = DateOperaUtil.comperaDate(dayData);
			if(gobedLong > Long.parseLong(dayData.getSleepTimeLong())){
				gobedLong -= 24 * 60 * 60 * 1000;
			}
			signIn.setGoBedTime(String.valueOf(gobedLong));
			signIn.setTrySleepTime(dayData.getSleepTimeLong());
			signIn.setWakeUpTime(dayData.getGetUpTimeLong());
			signIn.setOutBedTime("0");
			signIn.setDeepsleep(0);
			signIn.setShallowsleep(0);
			
		}else if(TextUtils.isEmpty(gobed) && !TextUtils.isEmpty(leavebed)){
			SoftDayData dayData = new SoftDayData();
			long leavebedLong = 0;
			try {
				dayData.setDate(title.getTag().toString());
				dayData.setSleepTime(trytime);
				dayData.setSleepTimeLong(String.valueOf(sdf2.parse(dayData.getDate()+" "+dayData.getSleepTime()).getTime()));
				dayData.setGetUpTime(getuptime);
				dayData.setGetUpTimeLong(String.valueOf(sdf2.parse(dayData.getDate()+" "+dayData.getGetUpTime()).getTime()));
				leavebedLong = sdf2.parse(dayData.getDate() +" "+ leavebed).getTime();
			} catch (Exception e) {
			}
			
			dayData = DateOperaUtil.comperaDate(dayData);
			
			if(leavebedLong < Long.parseLong(dayData.getGetUpTimeLong())){
				leavebedLong += 24 * 60 * 60 * 1000;
			}
			signIn.setGoBedTime("0");
			signIn.setTrySleepTime(dayData.getSleepTimeLong());
			signIn.setWakeUpTime(dayData.getGetUpTimeLong());
			signIn.setOutBedTime(String.valueOf(leavebedLong));
			signIn.setDeepsleep(0);
			signIn.setShallowsleep(0); 
			
		}else{
			SoftDayData dayData = new SoftDayData();
			try {
				dayData.setDate(title.getTag().toString());
				dayData.setSleepTime(trytime);
				dayData.setSleepTimeLong(String.valueOf(sdf2.parse(dayData.getDate()+" "+dayData.getSleepTime()).getTime()));
				dayData.setGetUpTime(getuptime);
				dayData.setGetUpTimeLong(String.valueOf(sdf2.parse(dayData.getDate()+" "+dayData.getGetUpTime()).getTime()));
			} catch (Exception e) {
			}
			
			dayData = DateOperaUtil.comperaDate(dayData);
			signIn.setGoBedTime("0");
			signIn.setTrySleepTime(dayData.getSleepTimeLong());
			signIn.setWakeUpTime(dayData.getGetUpTimeLong());
			signIn.setOutBedTime("0");
			signIn.setDeepsleep(0);
			signIn.setShallowsleep(0);
		}

		showPro(); 
		new XiangchengMallProcClass(this).signIn4_2_1(PreManager.instance().getUserId(this), type, signIn, new InterfaceSignInCallBack4_2_1() {
			
			@Override
			public void onSuccess(String icode, JSONObject response) {
				cancelPro();
				signIn.setResult(response.toString());
				SignInDBOperation.initDB(SignInActivity.this).updateSignInData(signIn, "0");
				setResult(101);
				AppManager.getAppManager().finishActivity();
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
				cancelPro();
				Util.show(SignInActivity.this, strErrMsg);
			}
		});
	}
	
 	private int getIndex(String text, List<String> list){
 		int size = list.size();
 		for (int i = 0; i < size; i++) {
			if(text.equals(list.get(i))){
				return i;
			}
		}
 		return 0;
 	}
 	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.btn_title_right:
			showCalendar();
			break;
		case R.id.show_more:
			showMoreView();
			break;
		case R.id.btn_buying:
			if(Util.checkNetWork(this)){
				new XiangchengProcClass(this).getBuyUrlData(new InterfaceGetBuyUrlCallback() {
					
					@Override
					public void onSuccess(int icode, String strSucMsg, String url) {
						Uri uri = Uri.parse(url);
						Intent it = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(it);
					}
					
					@Override
					public void onError(int icode, String strErrMsg) {
						Util.show(SignInActivity.this, strErrMsg);
					}
				});
			}else
				Util.show(SignInActivity.this, "请检查网络设置");
			break;
		case R.id.gobed_time:
			String gobadH = "00", gobad = "00";
			try {
				String goBad = gobedTime.getText().toString();
				gobadH = goBad.split(":")[0];
				gobad = goBad.split(":")[1];
			} catch (Exception e) {
			}
			spedialog = new SpeRollPickerDialog(this, new com.yzm.sleep.model.SpeRollPickerDialog.RollCallBack() {
				
				@Override
				public void setResaultRollString(String select1, String select2,String select3) {
					gobedTime.setText(new StringBuffer().append(select1).append(":").append(select2).toString()); 
				}
				
				@Override
				public void setResaultRoll(int select1, int select2, int select3) {
				}
			});
			
			spedialog.SetStrDate(2, "晚上几点上床", hour, getIndex(gobadH, hour), minute, getIndex(gobad, minute), null, 0, true);
			spedialog.show();
			break;
		case R.id.try_time:
			String tryTimedH = "00", tryTimeM = "00";
			try {
				String goBad = tryTime.getText().toString();
				tryTimedH = goBad.split(":")[0];
				tryTimeM = goBad.split(":")[1];
			} catch (Exception e) {
			}
			spedialog = new SpeRollPickerDialog(this, new com.yzm.sleep.model.SpeRollPickerDialog.RollCallBack() {
				
				@Override
				public void setResaultRollString(String select1, String select2,String select3) {
					tryTime.setText(new StringBuffer().append(select1).append(":").append(select2).toString());
				}
				
				@Override
				public void setResaultRoll(int select1, int select2, int select3) {
				}
			});
			spedialog.SetStrDate(2, "晚上几点开尝试睡觉", hour, getIndex(tryTimedH, hour), minute, getIndex(tryTimeM, minute), null, 0,true);
			spedialog.show();
			break;
		case R.id.spend_sleep_time:
			dialog = new RollPickerDialog(this, new RollCallBack() {
				
				@Override
				public void setResaultRollString(String select1, String select2,String select3) {
					spendTime.setText(select1);
				}
				
				@Override
				public void setResaultRoll(int select1, int select2, int select3) {
					
				}
			});
			dialog.SetStrDate(2, "花了多长时间入睡", list3 , getIndex(spendTime.getText()==null ? "" : spendTime.getText().toString(), list3), null, 0, null, 0, true);
			dialog.show();
			break;
		case R.id.wake_count:
			dialog = new RollPickerDialog(this, new RollCallBack() {
				
				@Override
				public void setResaultRollString(String select1, String select2,String select3) {
					int  count = 0;
					try {
						if(select1.length() > 3)
							count = 9;
						else
							count = Integer.parseInt(select1.split("次")[0]);
					} catch (Exception e) {
					}
					if(count > 0)
						rlQuestionFive .setVisibility(View.VISIBLE);
					else{
						rlQuestionFive .setVisibility(View.GONE);
						wakeTimeLenght.setText("");
					}
					wakeCount.setText(select1);
					wakeCount.setTag(count);
				}
				
				@Override
				public void setResaultRoll(int select1, int select2, int select3) {
					
				}
			});
			dialog.SetStrDate(2, "中途醒来几次", list4 , getIndex(wakeCount.getText()==null ? "" : wakeCount.getText().toString(), list4), null, 0, null, 0, true);
			dialog.show();
			break;
		case R.id.wake_time_lenght:
			dialog = new RollPickerDialog(this, new RollCallBack() {
				
				@Override
				public void setResaultRollString(String select1, String select2,String select3) {
					wakeTimeLenght.setText(select1);
				}
				
				@Override
				public void setResaultRoll(int select1, int select2, int select3) {
				}
			});
			dialog.SetStrDate(2, "中途醒来总时长", list3 , getIndex(wakeTimeLenght.getText()==null ? "" : wakeTimeLenght.getText().toString(), list3), null, 0, null, 0, true);
			dialog.show();
			break;
		case R.id.getup_time:
			String getUpH = "00", getUpM = "00";
			try {
				String goBad = getupTime.getText().toString();
				getUpH = goBad.split(":")[0];
				getUpM = goBad.split(":")[1];
			} catch (Exception e) {
			}
			spedialog = new SpeRollPickerDialog(this, new com.yzm.sleep.model.SpeRollPickerDialog.RollCallBack() {
				
				@Override
				public void setResaultRollString(String select1, String select2,String select3) {
					getupTime.setText(new StringBuffer().append(select1).append(":").append(select2).toString());
				}
				
				@Override
				public void setResaultRoll(int select1, int select2, int select3) {
				}
			});
			spedialog.SetStrDate(2, "早晨几点醒来", hour, getIndex(getUpH, hour), minute, getIndex(getUpM, minute), null, 0, true);
			spedialog.show();
			break;
		case R.id.early_wake_length:
			dialog = new RollPickerDialog(this, new RollCallBack() {
				
				@Override
				public void setResaultRollString(String select1, String select2,String select3) {
					earlyWakLength.setText(select1);
				}
				
				@Override
				public void setResaultRoll(int select1, int select2, int select3) {
				}
			});
			dialog.SetStrDate(2, "比计划早醒多久", list3 , getIndex(earlyWakLength.getText()==null ? "" : earlyWakLength.getText().toString(), list3), null, 0, null, 0, true);
			dialog.show();
			break;
		case R.id.leave_bed_time:
			String outBadH = "00", outBadM = "00";
			try {
				String goBad = leaveBedTime.getText().toString();
				outBadH = goBad.split(":")[0];
				outBadM = goBad.split(":")[1];
			} catch (Exception e) {
			}
			spedialog = new SpeRollPickerDialog(this, new com.yzm.sleep.model.SpeRollPickerDialog.RollCallBack() {
				
				@Override
				public void setResaultRollString(String select1, String select2,String select3) {
					leaveBedTime.setText(new StringBuffer().append(select1).append(":").append(select2).toString());
				}
				
				@Override
				public void setResaultRoll(int select1, int select2, int select3) {
				}
			});
			spedialog.SetStrDate(2, "几点离床", hour, getIndex(outBadH, hour), minute, getIndex(outBadM, minute), null, 0, true);
			spedialog.show();
			break;
		case R.id.btn_complate:
			complate();
			break;
		default:
			break;
		}
		
	}
}
