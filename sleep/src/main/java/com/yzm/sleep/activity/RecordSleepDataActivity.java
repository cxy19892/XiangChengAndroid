package com.yzm.sleep.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.bean.FankuiDataBean;
import com.yzm.sleep.bean.RemindBean;
import com.yzm.sleep.bean.ReportDataBean;
import com.yzm.sleep.model.RollPickerDialog;
import com.yzm.sleep.model.RollPickerDialog.RollCallBack;
import com.yzm.sleep.model.SignInData;
import com.yzm.sleep.model.SpeRollPickerDialog;
import com.yzm.sleep.utils.DateOperaUtil;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceGetBuyUrlCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceAddRecordCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceGetRecordByDateCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSupplyRecordCallBack;
import com.yzm.sleep.utils.CalenderUtil;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.SleepTimeData;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.utils.XiangchengProcClass;

/**
 * 睡眠数据记录
 * 
 * @params date 日期 yyyy-MM--dd 如果不传 则取系统时间
 */
@SuppressLint("SimpleDateFormat")
public class RecordSleepDataActivity extends BaseActivity {
	private TextView title, gobedTime, tryTime, spendTime,
			wakeTimeLenght, getupTime, earlyWakLength, leaveBedTime;
	private TextView btnGotoBad, btnTryTime, btnSpendTime, btnWakeTime, btnGetupTime, btnEarlyWake, btnLeaveBed;
	private RollPickerDialog dialog;
	private SpeRollPickerDialog spedialog;
	private ArrayList<String> hour;
	private ArrayList<String> minute;
	private ArrayList<String> list3;
	private SimpleDateFormat sdf1, sdf, simp, sdf2;
	private String type = "1";
	private ReportDataBean reportData;
	private List<FankuiDataBean> fankuiDatas;
	private ProgressUtils pro;
	/**
	 * 显示进度
	 */
	private void showPro() {
		try{
			if(pro == null){
				pro = new ProgressUtils(this);
				pro.setCanceledOnTouchOutside(false);
			}
			
			if (pro != null)
				 pro.show();
		}catch(Exception e){}
	}

	/**
	 * 取消进度
	 */
	private void cancelPro() {
		try{
			if (pro != null) {
				pro.dismiss();
			}
		}catch(Exception e){}
	}

	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 0)
				cancelPro();
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_sleepdata);
		findViewById(R.id.rl_title).setBackgroundColor(getResources().getColor(R.color.white));
		sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
		sdf = new SimpleDateFormat("HH:mm");
		simp = new SimpleDateFormat("yyyy-MM-dd");
		sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		initData();
		initView();
	}

	/**
	 * 初始化选择的数据
	 */
	private void initData() {
		hour = new ArrayList<String>();
		for (int i = 0; i < 24; i++) {
			hour.add(i < 10 ? ("0" + i) : String.valueOf(i));
		}
		minute = new ArrayList<String>();
		for (int i = 0; i < 60; i++) {
			minute.add(i < 10 ? ("0" + i) : String.valueOf(i));
		}
		list3 = new ArrayList<String>();
		list3.add("0分钟");
		list3.add("5分钟");
		list3.add("10分钟");
		list3.add("15分钟");
		list3.add("30分钟");
		list3.add("45分钟");
		list3.add("1小时");
		list3.add("1小时30分钟");
		list3.add("2小时");
		list3.add("2小时30分钟");
		list3.add("3小时");
		list3.add("3小时30分钟");
		list3.add("4小时");
		list3.add("4小时30分钟");
		list3.add("5小时");
		list3.add("5小时30分钟");
		list3.add("6小时");
		list3.add("6小时30分钟");
		list3.add("7小时");
		list3.add("7小时30分钟");
		list3.add("8小时");
	}

	private void initView() {
		findViewById(R.id.back).setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		gobedTime = (TextView) findViewById(R.id.gobed_time);
		btnGotoBad = (TextView) findViewById(R.id.btn_gobed_time);
		btnGotoBad.setOnClickListener(this);

		tryTime = (TextView) findViewById(R.id.try_time);
		btnTryTime = (TextView) findViewById(R.id.btn_try_time);
		btnTryTime.setOnClickListener(this);

		spendTime = (TextView) findViewById(R.id.spend_sleep_time);
		btnSpendTime = (TextView) findViewById(R.id.btn_spend_sleep_time);
		btnSpendTime.setOnClickListener(this);

		wakeTimeLenght = (TextView) findViewById(R.id.wake_time_lenght);
		btnWakeTime = (TextView) findViewById(R.id.btn_wake_time_lenght);
		btnWakeTime.setOnClickListener(this);

		getupTime = (TextView) findViewById(R.id.getup_time);
		btnGetupTime = (TextView) findViewById(R.id.btn_getup_time);
		btnGetupTime.setOnClickListener(this);

		earlyWakLength = (TextView) findViewById(R.id.early_wake_length);
		btnEarlyWake = (TextView) findViewById(R.id.btn_early_wake_length);
		btnEarlyWake.setOnClickListener(this);

		leaveBedTime = (TextView) findViewById(R.id.leave_bed_time);
		btnLeaveBed = (TextView) findViewById(R.id.btn_leave_bed_time);
		btnLeaveBed.setOnClickListener(this);

		Button right = (Button) findViewById(R.id.btn_title_right);
		Drawable drawable = getResources().getDrawable(R.drawable.ic_next_step_b);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		right.setCompoundDrawables(drawable, null, null, null);
		right.setOnClickListener(this);
		right.setVisibility(View.VISIBLE);
		
		if (!TextUtils.isEmpty(getIntent().getStringExtra("date"))) {
			LogUtil.i("chen","传入的时间"+getIntent().getStringExtra("date"));
			try {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(simp.parse(getIntent().getStringExtra("date")));
				title.setTag(simp.format(calendar.getTime()));
				if(getIntent().getIntExtra("data_flag", 0) ==1){
					title.setText("数据补充");
				}else
					title.setText(CalenderUtil.getStrByDate(getIntent().getStringExtra("date"), "yyyy-MM-dd"));
			} catch (Exception e) {
				Calendar calendar = Calendar.getInstance();
				title.setTag(simp.format(calendar.getTime()));
				if(getIntent().getIntExtra("data_flag", 0) ==1){
					title.setText("数据补充");
				}else
					title.setText(new SimpleDateFormat("MM月dd日").format(calendar.getTime()));
			}
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1);
			title.setTag(simp.format(calendar.getTime()));
			if(getIntent().getIntExtra("data_flag", 0) ==1){
				title.setText("数据补充");
			}else
				title.setText(new SimpleDateFormat("MM月dd日").format(calendar.getTime()));
		}

		ReportDataBean data = (ReportDataBean) getIntent()
				.getSerializableExtra("data");
		if (data != null)
			doSignInData(data);
		else
			getServerSignData(title.getTag().toString());

		gobedTime.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(tryTime.getText().toString())) {
					tryTime.setText(gobedTime.getText().toString());
					btnTryTime.setText("修改");
				}
			}
		});

		getupTime.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(leaveBedTime.getText().toString())) {
					leaveBedTime.setText(getupTime.getText().toString());
					btnLeaveBed.setText("修改");
				}
			}
		});
	}

	/**
	 * 获取服务器签到数据
	 * 
	 * @param date
	 */
	private void getServerSignData(final String date) {
		showPro();
		new XiangchengMallProcClass(this).getRecordByDate(PreManager.instance()
						.getUserId(this), date.replace("-", ""), type,
				new InterfaceGetRecordByDateCallBack() {

					@Override
					public void onSuccess(String icode,
										  ReportDataBean reportData,
										  List<FankuiDataBean> datas) {
						Message msg = new Message();
						msg.what = 0;
						handler.sendMessage(msg);
						fankuiDatas = datas;
						doSignInData(reportData);
					}

					@Override
					public void onError(String icode, String strErrMsg) {
						Message msg = new Message();
						msg.what = 0;
						handler.sendMessage(msg);

					}
				});
	}

	private void clearData() {
		gobedTime.setText("");
		btnGotoBad.setText("选择");
		tryTime.setText("");
		btnTryTime.setText("选择");
		spendTime.setText("");
		btnSpendTime.setText("选择");
		wakeTimeLenght.setText("");
		btnWakeTime.setText("选择");
		getupTime.setText("");
		btnGetupTime.setText("选择");
		earlyWakLength.setText("");
		btnEarlyWake.setText("选择");
		leaveBedTime.setText("");
		btnLeaveBed.setText("选择");
	}

	/**
	 * 设置数据
	 */
	private void doSignInData(ReportDataBean data) {
		this.reportData = data;
		try {
			clearData();
			if (!"0".equals(data.getTi_1())) {
				gobedTime.setText(sdf.format(1000 * Long.parseLong(data.getTi_1())));
				gobedTime.setTag(sdf1.format(1000 * Long.parseLong(data.getTi_1())));
				btnGotoBad.setText("修改");
			}

			if (!"0".equals(data.getTi_2())) {
				tryTime.setText(sdf.format(1000 * Long.parseLong(data.getTi_2())));
				tryTime.setTag(sdf1.format(1000 * Long.parseLong(data.getTi_2())));
				btnTryTime.setText("修改");
			}

			if(!TextUtils.isEmpty(data.getTi_3())) {
				int hour = Integer.parseInt(data.getTi_3()) / 60;
				int minute = Integer.parseInt(data.getTi_3()) % 60;
				spendTime.setText(((hour > 0) ? (hour + "小时") : "") + (minute + "分钟"));
				btnSpendTime.setText("修改");
			}

			if (!TextUtils.isEmpty(data.getTi_5())) {
				int hour1 = Integer.parseInt(data.getTi_5()) / 60;
				int minute1 = Integer.parseInt(data.getTi_5()) % 60;
				wakeTimeLenght.setText(((hour1 > 0) ? (hour1 + "小时") : "") + (minute1 + "分钟"));
				btnWakeTime.setText("修改");
			}

			if (!"0".equals(data.getTi_6())) {
				getupTime.setText(sdf.format(1000 * Long.parseLong(data .getTi_6())));
				getupTime.setTag(sdf1.format(1000 * Long.parseLong(data.getTi_6())));
				btnGetupTime.setText("修改");
			}

			if (!TextUtils.isEmpty(data.getTi_7())) {
				int hour2 = Integer.parseInt(data.getTi_7()) / 60;
				int minute2 = Integer.parseInt(data.getTi_7()) % 60;
				earlyWakLength.setText((hour2 > 0) ? hour2 + "小时" + minute2 + "分钟" : minute2 + "分钟");
				btnEarlyWake.setText("修改");
			}

			if (!"0".equals(data.getTi_8())) {
				leaveBedTime.setText(sdf.format(1000 * Long.parseLong(data .getTi_8())));
				leaveBedTime.setTag(sdf1.format(1000 * Long.parseLong(data .getTi_8())));
				btnLeaveBed.setText("修改");
			}

		} catch (Exception e) {
			clearData();
		}
		
	}

	/**
	 * 提交签到数据
	 */
	private void complate() {

		final SignInData signIn = new SignInData();
		signIn.setDate(title.getTag().toString());
		// 上床时间点 t1
		String gobed = gobedTime.getText().toString();
		// 尝试入睡时间点 t2
		String trytime = tryTime.getText().toString();
		if (TextUtils.isEmpty(trytime)) {
			Util.show(this, "请选择尝试睡觉时间");
			return;
		}
		// 花费多久时间入睡 t3
		String spendtime = spendTime.getText().toString();
		if (TextUtils.isEmpty(spendtime)) {
			Util.show(this, "请选择花费多长时间入睡");
			return;
		}
		signIn.setHowLongSleepTime(Util.getMinutes(spendtime));
		signIn.setWakeCount(0);
		
		// 中途醒来时长 t5
		String wakelenght = wakeTimeLenght.getText().toString();
		if (TextUtils.isEmpty(wakelenght)) {
			Util.show(this, "请选择中途醒来总时长");
			return;
		}

		signIn.setHowLongWakeTime(Util.getMinutes(wakelenght));
		
		// 醒来时间点 t6
		String getuptime = getupTime.getText().toString();
		if (TextUtils.isEmpty(getuptime)) {
			Util.show(this, "请选择几点醒来");
			return;
		}
		// 早起时长 t7
		String earlylength = earlyWakLength.getText().toString();
		signIn.setWakeEarlyTime(Util.getMinutes(earlylength));
		// 离床时间段 t8
		String leavebed = leaveBedTime.getText().toString();

		if (!TextUtils.isEmpty(gobed) && !TextUtils.isEmpty(leavebed)) {
			SleepTimeData date = new SleepTimeData();
			date.setDate(title.getTag().toString());
			date.setGbStr(gobed);
			date.setTsStr(trytime);
			date.setWuStr(getuptime);
			date.setObStr(leavebed);
			date = DateOperaUtil.comperaTime(date);
			if (!date.isState()) {
				Util.show(this, date.getErrMsg());
				return;
			}

			signIn.setGoBedTime(String.valueOf(date.getGoBedTime()));
			signIn.setTrySleepTime(String.valueOf(date.getTrySleepTime()));
			signIn.setWakeUpTime(String.valueOf(date.getWakeUpTime()));
			signIn.setOutBedTime(String.valueOf(date.getOutBedTime()));
			signIn.setDeepsleep(0);
			signIn.setShallowsleep(0);

		} else if (!TextUtils.isEmpty(gobed) && TextUtils.isEmpty(leavebed)) {
			SoftDayData dayData = new SoftDayData();
			long gobedLong = 0;
			try {
				dayData.setDate(title.getTag().toString());
				dayData.setSleepTime(trytime);
				dayData.setSleepTimeLong(String.valueOf(sdf2.parse(
						dayData.getDate() + " " + dayData.getSleepTime())
						.getTime()));
				dayData.setGetUpTime(getuptime);
				dayData.setGetUpTimeLong(String.valueOf(sdf2.parse(
						dayData.getDate() + " " + dayData.getGetUpTime())
						.getTime()));
				gobedLong = sdf2.parse(dayData.getDate() + " " + gobed)
						.getTime();
			} catch (Exception e) {
			}
			dayData = DateOperaUtil.comperaDate(dayData);
			if (gobedLong > Long.parseLong(dayData.getSleepTimeLong())) {
				gobedLong -= 24 * 60 * 60 * 1000;
			}
			signIn.setGoBedTime(String.valueOf(gobedLong));
			signIn.setTrySleepTime(dayData.getSleepTimeLong());
			signIn.setWakeUpTime(dayData.getGetUpTimeLong());
			signIn.setOutBedTime("0");
			signIn.setDeepsleep(0);
			signIn.setShallowsleep(0);

		} else if (TextUtils.isEmpty(gobed) && !TextUtils.isEmpty(leavebed)) {
			SoftDayData dayData = new SoftDayData();
			long leavebedLong = 0;
			try {
				dayData.setDate(title.getTag().toString());
				dayData.setSleepTime(trytime);
				dayData.setSleepTimeLong(String.valueOf(sdf2.parse(
						dayData.getDate() + " " + dayData.getSleepTime())
						.getTime()));
				dayData.setGetUpTime(getuptime);
				dayData.setGetUpTimeLong(String.valueOf(sdf2.parse(
						dayData.getDate() + " " + dayData.getGetUpTime())
						.getTime()));
				leavebedLong = sdf2.parse(dayData.getDate() + " " + leavebed)
						.getTime();
			} catch (Exception e) {
			}

			dayData = DateOperaUtil.comperaDate(dayData);

			if (leavebedLong < Long.parseLong(dayData.getGetUpTimeLong())) {
				leavebedLong += 24 * 60 * 60 * 1000;
			}
			signIn.setGoBedTime("0");
			signIn.setTrySleepTime(dayData.getSleepTimeLong());
			signIn.setWakeUpTime(dayData.getGetUpTimeLong());
			signIn.setOutBedTime(String.valueOf(leavebedLong));
			signIn.setDeepsleep(0);
			signIn.setShallowsleep(0);

		} else {
			SoftDayData dayData = new SoftDayData();
			try {
				dayData.setDate(title.getTag().toString());
				dayData.setSleepTime(trytime);
				dayData.setSleepTimeLong(String.valueOf(sdf2.parse(
						dayData.getDate() + " " + dayData.getSleepTime())
						.getTime()));
				dayData.setGetUpTime(getuptime);
				dayData.setGetUpTimeLong(String.valueOf(sdf2.parse(
						dayData.getDate() + " " + dayData.getGetUpTime())
						.getTime()));
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
		if(getIntent().getIntExtra("data_flag", 0) == 1){//补签
			new XiangchengMallProcClass(this).supplySleepDataRecord(PreManager.instance().getUserId(this), signIn, type, new InterfaceSupplyRecordCallBack() {
				
				@Override
				public void onSuccess(String icode, String strMsg) {
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);
					int feel_flag = getIntent().getIntExtra("feel_flag", 0);
					int env_flag = getIntent().getIntExtra("env_flag", 0);
					int smoke_flag = getIntent().getIntExtra("smoke_flag", 0);
					int win_flag = getIntent().getIntExtra("win_flag", 0);
					int coffo_flag = getIntent().getIntExtra("coffo_flag", 0);
					int weight_flag = getIntent().getIntExtra("weight_flag", 0);
					int sport_flag = getIntent().getIntExtra("sport_flag", 0);
					

					Intent intent = new Intent();
					intent.setAction(Constant.WEEK_FEED_BACK_SUC);
					sendBroadcast(intent);
					
					
					if((feel_flag | env_flag | smoke_flag | win_flag | coffo_flag | weight_flag | sport_flag) == 1){
						startActivity(new Intent(RecordSleepDataActivity.this, RecordFeelDataActivity.class)
						.putExtra("feel_flag", feel_flag)
						.putExtra("env_flag", env_flag)
						.putExtra("smoke_flag", smoke_flag)
						.putExtra("win_flag", win_flag)
						.putExtra("coffo_flag",coffo_flag)
						.putExtra("sport_flag",sport_flag)
						.putExtra("weight_flag", weight_flag));
					}else{
						startActivity(new Intent(RecordSleepDataActivity.this, SleepDataReportActivity.class));
					}
					
					AppManager.getAppManager().finishActivity();
				}
				
				@Override
				public void onError(String icode, String strErrMsg) {
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);
					Util.show(RecordSleepDataActivity.this, strErrMsg);
				}
			});
		}else{
			saveRemindData(signIn);
			new XiangchengMallProcClass(this).addRecordData(PreManager.instance()
					.getUserId(this), type, signIn,
					new InterfaceAddRecordCallBack() {
	
						@Override
						public void onSuccess(String icode, ReportDataBean datas,
								List<FankuiDataBean> fankuiData) {
							Message msg = new Message();
							msg.what = 0;
							handler.sendMessage(msg);
							AppManager.getAppManager().finishActivity(DataResultFeedbackActivity.class);
							reportData = datas;
							fankuiDatas = fankuiData;
							startActivity(new Intent(RecordSleepDataActivity.this,
									DataResultFeedbackActivity.class)
									.putExtra("reportData", reportData)
									.putExtra("fankuiData", new Gson().toJson(fankuiDatas))
									.putExtra("date", title.getTag().toString()));
							
							Intent intent = new Intent();
							intent.setAction(Constant.WEEK_FEED_BACK_SUC);
							sendBroadcast(intent);
							
							AppManager.getAppManager().finishActivity();
							
						}
	
						@Override
						public void onError(String icode, String strErrMsg) {
							Message msg = new Message();
							msg.what = 0;
							handler.sendMessage(msg);
							Util.show(RecordSleepDataActivity.this, strErrMsg);
						}
					});
		}
	}

	private int getIndex(String text, List<String> list) {
		int size = list.size();
		for (int i = 0; i < size; i++) {
			if (text.equals(list.get(i))) {
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
			complate();
			break;

		case R.id.btn_gobed_time:
			String gobadH = "00",
			gobad = "00";
			try {
				String goBad = gobedTime.getText().toString();
				gobadH = goBad.split(":")[0];
				gobad = goBad.split(":")[1];
			} catch (Exception e) {
			}
			spedialog = new SpeRollPickerDialog(this,
					new com.yzm.sleep.model.SpeRollPickerDialog.RollCallBack() {

						@Override
						public void setResaultRollString(String select1,String select2, String select3) {
							gobedTime.setText(new StringBuffer() .append(select1).append(":").append(select2).toString());
							btnGotoBad.setText("修改");
							btnTryTime.setText("修改");
						}

						@Override
						public void setResaultRoll(int select1, int select2,
								int select3) {
						}
					});

			spedialog.SetStrDate(2, "晚上几点上床", hour, getIndex(gobadH, hour),
					minute, getIndex(gobad, minute), null, 0, true);
			spedialog.show();
			break;
		case R.id.btn_try_time:
			String tryTimedH = "00",
			tryTimeM = "00";
			try {
				String goBad = tryTime.getText().toString();
				tryTimedH = goBad.split(":")[0];
				tryTimeM = goBad.split(":")[1];
			} catch (Exception e) {
			}
			spedialog = new SpeRollPickerDialog(this,
					new com.yzm.sleep.model.SpeRollPickerDialog.RollCallBack() {

						@Override
						public void setResaultRollString(String select1,
								String select2, String select3) {
							tryTime.setText(new StringBuffer().append(select1)
									.append(":").append(select2).toString());
							btnTryTime.setText("修改");
						}

						@Override
						public void setResaultRoll(int select1, int select2,
								int select3) {
						}
					});
			spedialog.SetStrDate(2, "晚上几点开尝试睡觉", hour,
					getIndex(tryTimedH, hour), minute,
					getIndex(tryTimeM, minute), null, 0, true);
			spedialog.show();
			break;
		case R.id.btn_spend_sleep_time:
			dialog = new RollPickerDialog(this, new RollCallBack() {

				@Override
				public void setResaultRollString(String select1,
						String select2, String select3) {
					spendTime.setText(select1);
					btnSpendTime.setText("修改");
				}

				@Override
				public void setResaultRoll(int select1, int select2, int select3) {

				}
			});
			dialog.SetStrDate(
					2,
					"花了多长时间入睡",
					list3,
					getIndex(spendTime.getText() == null ? "" : spendTime
							.getText().toString(), list3), null, 0, null, 0,
					true);
			dialog.show();
			break;
		case R.id.btn_wake_time_lenght:
			dialog = new RollPickerDialog(this, new RollCallBack() {

				@Override
				public void setResaultRollString(String select1,
						String select2, String select3) {
					wakeTimeLenght.setText(select1);
					btnWakeTime.setText("修改");
				}

				@Override
				public void setResaultRoll(int select1, int select2, int select3) {
				}
			});
			dialog.SetStrDate(2,
					"中途醒来总时长",
					list3,
					getIndex(wakeTimeLenght.getText() == null ? ""
							: wakeTimeLenght.getText().toString(), list3),
					null, 0, null, 0, true);
			dialog.show();
			break;
		case R.id.btn_getup_time:
			String getUpH = "00",
			getUpM = "00";
			try {
				String goBad = getupTime.getText().toString();
				getUpH = goBad.split(":")[0];
				getUpM = goBad.split(":")[1];
			} catch (Exception e) {
			}
			spedialog = new SpeRollPickerDialog(this,
					new com.yzm.sleep.model.SpeRollPickerDialog.RollCallBack() {

						@Override
						public void setResaultRollString(String select1,
								String select2, String select3) {
							getupTime.setText(new StringBuffer()
									.append(select1).append(":")
									.append(select2).toString());
							btnGetupTime.setText("修改");
							btnLeaveBed.setText("修改");
						}

						@Override
						public void setResaultRoll(int select1, int select2,
								int select3) {
						}
					});
			spedialog.SetStrDate(2, "早晨几点醒来", hour, getIndex(getUpH, hour),
					minute, getIndex(getUpM, minute), null, 0, true);
			spedialog.show();
			break;
		case R.id.btn_early_wake_length:
			dialog = new RollPickerDialog(this, new RollCallBack() {

				@Override
				public void setResaultRollString(String select1,
						String select2, String select3) {
					earlyWakLength.setText(select1);
					btnEarlyWake.setText("修改");
				}

				@Override
				public void setResaultRoll(int select1, int select2, int select3) {
				}
			});
			dialog.SetStrDate(
					2,
					"比计划早醒多久",
					list3,
					getIndex(earlyWakLength.getText() == null ? ""
							: earlyWakLength.getText().toString(), list3),
					null, 0, null, 0, true);
			dialog.show();
			break;
		case R.id.btn_leave_bed_time:
			String outBadH = "00",
			outBadM = "00";
			try {
				String goBad = leaveBedTime.getText().toString();
				outBadH = goBad.split(":")[0];
				outBadM = goBad.split(":")[1];
			} catch (Exception e) {
			}
			spedialog = new SpeRollPickerDialog(this,
					new com.yzm.sleep.model.SpeRollPickerDialog.RollCallBack() {

						@Override
						public void setResaultRollString(String select1,
								String select2, String select3) {
							leaveBedTime.setText(new StringBuffer()
									.append(select1).append(":")
									.append(select2).toString());
							btnLeaveBed.setText("修改");
						}

						@Override
						public void setResaultRoll(int select1, int select2,
								int select3) {
						}
					});
			spedialog.SetStrDate(2, "几点离床", hour, getIndex(outBadH, hour),
					minute, getIndex(outBadM, minute), null, 0, true);
			spedialog.show();
			break;
		default:
			break;
		}

	}

	private void saveRemindData(SignInData mSignInData){
		if(mSignInData.getDate().equals(TimeFormatUtil.getYesterDay("yyyy-MM-dd"))){
			RemindBean mRemindBean = new RemindBean();
			mRemindBean.setDate(mSignInData.getDate());
			mRemindBean.setGetupTime(mSignInData.getWakeUpTime());
			mRemindBean.setSleepTime(mSignInData.getTrySleepTime());
			PreManager.instance().saveSmartRemindData(this, mRemindBean.getString());
		}

	}
}
