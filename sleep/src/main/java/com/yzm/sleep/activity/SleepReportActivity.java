package com.yzm.sleep.activity;

import java.math.BigDecimal;
import java.text.ParseException;
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
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.activity.pillow.PillowDataOpera;
import com.yzm.sleep.background.DataUtils;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.background.MytabOperate;
import com.yzm.sleep.background.SignInDBOperation;
import com.yzm.sleep.background.SleepInfo;
import com.yzm.sleep.bean.SleepCaseBean;
import com.yzm.sleep.bean.SleepSignDataBean;
import com.yzm.sleep.bean.SleepStatusBean;
import com.yzm.sleep.model.DaySleepMsg;
import com.yzm.sleep.model.PillowDataModel;
import com.yzm.sleep.model.SignInData;
import com.yzm.sleep.model.SmartRemindBean;
import com.yzm.sleep.render.GetSleepAvgTimeValueClass;
import com.yzm.sleep.render.GetSleepAvgTimeValueClass.AvgTimeResult;
import com.yzm.sleep.render.GetSleepAvgTimeValueClass.GetAvgTimeParamItem;
import com.yzm.sleep.render.GetSleepResultValueClass.SleepDataHead;
import com.yzm.sleep.tools.DealSetTimeUtils;
import com.yzm.sleep.utils.BluetoothDataFormatUtil;
import com.yzm.sleep.utils.InterfaceMallUtillClass.CancelFeedbackCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.CancelFeedbackParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSetSleepTimeCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSleepFeedbackCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.SetSleepTimeParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.SleepFeedbackParams;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.SmartNotificationUtil;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.CustomDialog;
import com.yzm.sleep.widget.CustomDialog.MyOnClickListener;
import com.yzm.sleep.widget.ReportSleepLengthTable;
import com.yzm.sleep.widget.SleepTimePointTable;

/**
 * 睡眠报告
 * 
 * @author
 */
@SuppressLint("SimpleDateFormat") 
public class SleepReportActivity extends BaseActivity {
	
	private Context mContext;
	private View scView, noData;
	private TextView tvExplain, tvLengthExplain, tvSleepExplain, tvGetUpExplain;
	private SleepTimePointTable sleepTab, getupTab;
	private ReportSleepLengthTable reportTab;
	private SimpleDateFormat simp; 
	private ProgressUtils pro;
	private String dataType;
	private ArrayList<SoftDayData> softList;
	private List<SleepStatusBean> mList;
	private String sleepDate/*入睡时间*/,getupDate/*起床时间*/;
	private int hasDataCount = 0, totalTime = 0, lenghtWDb = 0, xlDab = 0, totalXl= 0, compalteGetupCount = 0, compalteSleepCount = 0;
	private int temp2;
	private CustomDialog dialog;
	private String sleepLength;
	
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
		setContentView(R.layout.activity_sleep_report);
		this.mContext = this;
		simp = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		softList = new ArrayList<SoftDayData>();
		mList = new ArrayList<SleepStatusBean>();
		getupDate = PreManager.instance().getGetupTime_Setting(this);
		sleepDate = PreManager.instance().getSleepTime_Setting(this);
		if(TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(this)))
			dataType = "1";
		else
			dataType = "2";
		initView();
		getReportData(simp.format(calendar.getTime()));
	}
	
	private void initView(){
		findViewById(R.id.back).setOnClickListener(this);
		((TextView)findViewById(R.id.title)).setText("睡眠报告");
		scView = findViewById(R.id.sc_view);
		tvExplain = (TextView) findViewById(R.id.tv_explain);
		tvLengthExplain = (TextView) findViewById(R.id.tv_length_explain);
		tvSleepExplain = (TextView) findViewById(R.id.tv_sleep_explain);
		tvGetUpExplain = (TextView) findViewById(R.id.tv_getup_explain);
		noData  =findViewById(R.id.nodata);
		sleepTab = (SleepTimePointTable) findViewById(R.id.soft_tab1);
		getupTab = (SleepTimePointTable) findViewById(R.id.soft_tab2);
		reportTab = (ReportSleepLengthTable) findViewById(R.id.report_tab);
	}
	
	private void showFeedBackDialog(){
		dialog = new CustomDialog(this);
		dialog.show();
		dialog.setTitle("一周反馈");
		dialog.setSub("香橙顾问非常高兴为您服务，在过去一周，您的睡眠情况如何呢？");
		dialog.setOnLeftClickListener("取消", new MyOnClickListener() {
			
			@Override
			public void Onclick(View v) {
				cancleFeedBack();
				dialog.dismiss();
				AppManager.getAppManager().finishActivity();
			}
		});
		dialog.setOnRightClickListener("反馈", new MyOnClickListener() {
			
			@Override
			public void Onclick(View v) {
				//进入反馈 
				startActivity(new Intent(SleepReportActivity.this, EstimateWebActivity.class).putExtra("type", "5")
						.putExtra("datatype", dataType));
				AppManager.getAppManager().finishActivity(SleepReportActivity.class);
			}
		});
	}
	
	/**
	 * 取消周反馈
	 * @param dataType
	 */
	private void cancleFeedBack(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		
		CancelFeedbackParams mParams = new CancelFeedbackParams();
		mParams.date = simp.format(calendar.getTime()).replace("-", "");
		mParams.my_int_id = PreManager.instance().getUserId(this);
		mParams.type = dataType;
		new XiangchengMallProcClass(this).complatePlan(mParams, new CancelFeedbackCallBack() {
			
			@Override
			public void onSuccess(int icode, String strSuccMsg) {
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
			}
		});
	}
	
	/**
	 * 获取报告数据
	 */
	private void getReportData(final String date){
		
		SleepFeedbackParams params = new SleepFeedbackParams();
		params.my_int_id = PreManager.instance().getUserId(mContext);
		params.date = date.replace("-", "");
		params.type = dataType;
		showPro();
		new XiangchengMallProcClass(mContext).sleepFeedback(params, new InterfaceSleepFeedbackCallBack() {
			
			@Override
			public void onSuccess(String icode, String report_ok, String text, List<SleepSignDataBean> datas) {
				cancelPro();
				noData.setVisibility(View.GONE);
				doCallBackData(date, report_ok, text, datas);
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
				cancelPro();
				noData.setVisibility(View.VISIBLE);
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if(arg0 == 2016 && arg1 == RESULT_OK){
			if (scView.getVisibility() != View.VISIBLE)
				scView.setVisibility(View.VISIBLE);
			if(dialog != null)
				dialog.dismiss();
		}
	}

	private void doCallBackData(String date, String report_ok, String text, List<SleepSignDataBean> datas){
		
		if("1".equals(report_ok)){
			showFeedBackDialog();
			scView.setVisibility(View.GONE);
		}else{	
			if (scView.getVisibility() != View.VISIBLE)
				scView.setVisibility(View.VISIBLE);
		}

		ArrayList<GetAvgTimeParamItem> list1 = new ArrayList<GetAvgTimeParamItem>();
		tvExplain.setText(text);
		for(int i = 0; i < datas.size(); i++){
			SleepSignDataBean data = datas.get(i);
			SoftDayData softData = new SoftDayData();
			SleepStatusBean bean= new SleepStatusBean();
			bean.setDatestring(data.getDate());
			try {
				softData.setDate(simp.format(new SimpleDateFormat("yyyyMMdd").parse(data.getDate())));
				int sleepLenght = 0;
				try {
					sleepLenght = Integer.parseInt(data.getSleeplong());
				} catch (Exception e) {
				}
				if(!TextUtils.isEmpty(data.getSleep()) && !TextUtils.isEmpty(data.getWakeup())&& sleepLenght > 0){
					bean.setXiaolv(data.getXiaolv());
					int xl = (int) (new BigDecimal(Double.parseDouble(data.getXiaolv())).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() * 100);
					bean.setSleeplong(data.getSleeplong());
					int total = Integer.parseInt(data.getSleeplong());
					double xl1 = Double.parseDouble(data.getXiaolv());
					bean.setDeepsleep(String.valueOf((int)(xl1 * total)));
					//效率未达标
					if(xl >= 85)
						xlDab += 1;
					//睡眠时长达标天数
					if(total < 6.5 * 60 || total > 8.5 *60)
						lenghtWDb += 1;	
						
					if(xl < 80)
						temp2 +=1;
					
					//总效率
					totalXl += xl;
					//总睡眠时间长 分钟
					totalTime += total; 
					
					long sleepLong = Long.parseLong(data.getSleep()) * 1000;
					long getupLong = Long.parseLong(data.getWakeup()) * 1000;
					softData.setSleepTime(new SimpleDateFormat("HH:mm").format(sleepLong));
					softData.setGetUpTime(new SimpleDateFormat("HH:mm").format(getupLong));
					softData.setSleepTimeLong(String.valueOf(sleepLong));
					softData.setGetUpTimeLong(String.valueOf(getupLong));
					//平均时间点
					GetAvgTimeParamItem item = new GetAvgTimeParamItem();
					int inSleepTime = TimeFormatUtil.timeToMiss(softData.getSleepTime());
					int outSleepTime = TimeFormatUtil.timeToMiss(softData.getGetUpTime());
					item.iInSleepTime = inSleepTime;
					item.iOutSleepTime = outSleepTime;
					list1.add(item);
					//醒来时间达标天数
					long targetu=TimeFormatUtil.timeToMiss(getupDate);
					long tempup=outSleepTime - targetu;
					if(tempup >= -15*60 && tempup <= 15*60)
						compalteGetupCount+=1;
					//入睡时间达标天数
					long targetS=TimeFormatUtil.timeToMiss(sleepDate);//目标入睡时间
					long temp=inSleepTime - targetS;
					if(temp >= 0 && temp <=30*60)
						compalteSleepCount+=1;
					hasDataCount += 1;
				}
			} catch (Exception e) {
			}
			
			mList.add(bean);
			softList.add(softData);
		}
		
		AvgTimeResult avgResult = new GetSleepAvgTimeValueClass().getAVG(list1);
		if(avgResult != null && hasDataCount > 0){
			totalTime = totalTime/hasDataCount;
			String avgLenght = ((totalTime/60) < 10 ? ("0" + (totalTime/60)) : (totalTime/60))+ "小时" + ((totalTime%60) < 0 ? ("0" + (totalTime%60)) : (totalTime%60))+"分";
			tvLengthExplain.setText(new StringBuffer().append("平均睡眠时长：").append(avgLenght).append("，").append(lenghtWDb).append("天处于亚健康，")
					.append("平均睡眠效率：").append((totalXl/hasDataCount)).append("%，").append(xlDab).append("天达标。").toString());
			
			int avgInSleepTime = avgResult.iAvgInSleepTime;
			int avgOutSleepTime = avgResult.iAvgOutSleepTime;
			String avgInSleepTimeFormat = ((avgInSleepTime / 3600) < 10 ? "0"+ (avgInSleepTime / 3600) : (avgInSleepTime / 3600))+ ":"+ (((avgInSleepTime / 60) % 60) < 10 ? "0"
					+ ((avgInSleepTime / 60) % 60): ((avgInSleepTime / 60) % 60));
			String avgOutSleepTimeFormat = ((avgOutSleepTime / 3600) < 10 ? "0"+ (avgOutSleepTime / 3600) : (avgOutSleepTime / 3600))
			+ ":"+ (((avgOutSleepTime / 60) % 60) < 10 ? "0"+ ((avgOutSleepTime / 60) % 60): ((avgOutSleepTime / 60) % 60));
			tvSleepExplain.setText(new StringBuffer().append("平均入睡时间：").append(avgInSleepTimeFormat).append("，").append(compalteSleepCount).append("天达标。").toString());
			tvGetUpExplain.setText(new StringBuffer().append("平均醒来时间：").append(avgOutSleepTimeFormat).append("，").append(compalteGetupCount).append("天达标。").toString());
		}else{
			tvSleepExplain.setText("平均入睡时间：_ _：_ _，0天未达标。");
			tvGetUpExplain.setText("平均醒来时间：_ _:_ _，0天未达标。");
			tvLengthExplain.setText("平均睡眠时长：_ _:_ _，0天处于亚健康，平均睡眠效率：_%，0天未达标。");
		}
		reportTab.setData(mList);
		sleepTab.setData(softList, sleepDate, 0);
		getupTab.setData(softList, getupDate, 1);
	}
	
	private void editSleepLegth(){
		showPro();
		SetSleepTimeParams mParams = new SetSleepTimeParams();
		mParams.my_int_id = PreManager.instance().getUserId(this);
		mParams.wakeup = getupDate;
		mParams.sleeplong = String.valueOf(totalTime);
		new XiangchengMallProcClass(this).setSleepTime(mParams, new InterfaceSetSleepTimeCallBack() {
			@Override
			public void onSuccess(String icode, List<SleepCaseBean> datas) {
				cancelPro();
				complete();
				AppManager.getAppManager().finishActivity();
			}
			@Override
			public void onError(String icode, String strErrMsg) {
				cancelPro();
				Util.show(SleepReportActivity.this, strErrMsg);
				AppManager.getAppManager().finishActivity();
			}
		});
		
	}
	
	private void judgeShowDialog(){
//		sendFeedBackBroadcast();
		int lenght = countLengthByTime(sleepDate, getupDate);
		if(temp2 > 0 && lenght > totalTime){
			sleepLength = (totalTime/60 < 10 ? ("0" + totalTime/60) : totalTime/60) + "小时" + (totalTime%60 < 10 ? ("0" + totalTime%60) : totalTime%60)+"分";
			dialog = new CustomDialog(this);
			dialog.show();
			dialog.setTitle("提示");
			dialog.setSub("根据你上周数据分析，建议你每天睡眠时长为" + sleepLength);
			dialog.setOnLeftClickListener("取消", new MyOnClickListener() {
				
				@Override
				public void Onclick(View v) {
					dialog.dismiss();
					AppManager.getAppManager().finishActivity();
				}
			});
			dialog.setOnRightClickListener("确定", new MyOnClickListener() {
				
				@Override
				public void Onclick(View v) {
					editSleepLegth();
					dialog.dismiss();
				}
			});
		}else{
			AppManager.getAppManager().finishActivity();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.back:
				judgeShowDialog();
				break;
			default:
				break;
		}
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			judgeShowDialog();
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}

	private static int countLengthByTime(String sleepTime, String getUpTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		try {
			int sleepTimeLong = (int) sdf.parse(sleepTime).getTime();
			int getUpTimeLong = (int) sdf.parse(getUpTime).getTime();
			int totalTime = getUpTimeLong - sleepTimeLong;
			if(totalTime < 0)
				totalTime += 24 * 60 * 60 * 1000;
			return totalTime/(60 * 1000);       
		} catch (Exception e) {
		}
		return 0;
	}
	
	private void complete(){
		try{
			String getupHore=getupDate.split(":")[0],getupMin=getupDate.split(":")[1];
			String sleepLengths = new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH小时mm分").parse(sleepLength).getTime());
			int getupH=Integer.parseInt(getupHore);
			int getupM=Integer.parseInt(getupMin);
			int sleepMinH=Integer.parseInt(sleepLengths.split(":")[0]);
			int sleepMinM=Integer.parseInt(sleepLengths.split(":")[1]);
		
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
			PreManager.instance().saveOldT1(this, oldt1);
			PreManager.instance().saveOldT2(this, oldt2);
			
			//处理新区间
			PreManager.instance().saveSleepTime_Setting(this,(tempH<10?("0"+tempH):tempH)+":"+(tempM<10?("0"+tempM):tempM));
			PreManager.instance().saveGetupTime_Setting(this,getupDate);
		
			//从上班模式中获取数据并保存数据
			final int startTime =tempH * 60 + tempM;
			final int endTime =Integer.parseInt(getupHore) * 60 + getupM;
			setTime(startTime,endTime);
			try{
				Util.show(this, "正在重新设置时间，请稍等");
				String date = DataUtils.getRecordDate(new Date());
				long newt1 = DataUtils.getAllerateStartTime() / (60*1000);
				long newt2 = DataUtils.getAllerateStopTime() / (60*1000);
				DealSetTimeUtils.getInstance().setTime(this, date, System.currentTimeMillis(), oldt11, oldt21, newt1, newt2);
			}catch(Exception e) {
			}
		}catch(Exception e){}
	}

	/**
	 * 设置时间
	 * @param startTime
	 * @param endTime
	 */
	private void setTime(int startTime,int endTime) {
		getSleepDataAndSetSmartAlarm(1);
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
	 * @param remind_style   0 午间提醒 1 睡前提醒
	 */
	@SuppressLint("SimpleDateFormat")
	private void getSleepDataAndSetSmartAlarm(int remind_style) {
		getDbData();
		// 判断是否开启智能闹钟
		// 如果开启则则去取得提醒然后设置提醒时间
		SmartRemindBean mSmartRemindBean = null;
		@SuppressWarnings("static-access")
		SharedPreferences sp = this.getApplicationContext()
				.getSharedPreferences(SleepInfo.SLEEP_SETTIME,
						getApplicationContext().MODE_APPEND);

		String startTime = "";
		String endTime = "";
		String suggestSleepTime = PreManager.instance().getSleepTime_Setting(
				this);
		String remind_time_style = sp.getString(SleepInfo.REMIND_BEFORE_SLEEP,
				"");
		if (remind_time_style.equals("")) {
			// 如果绑定了硬件 就使用硬件的数据
			if (!TextUtils.isEmpty(PreManager.instance()
					.getBundbluetoothPillow(this))) {
				PillowDataModel pModel = PillowDataOpera.queryDataFromSQL(this
						.getApplicationContext(), TimeFormatUtil.getYesterDay(
						new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
						"yyyy-MM-dd"));
				if (pModel != null) {
					if (pModel.getBfr() != null) {
						SleepDataHead datahead = BluetoothDataFormatUtil
								.format3(pModel.getBfr(), 10);
						startTime = TimeFormatUtil.formatTime1(
								datahead.InSleepTime, "HH:mm");
						endTime = TimeFormatUtil.formatTime1(
								datahead.OutSleepTime, "HH:mm");
					}
				}
				if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
					mSmartRemindBean = SmartNotificationUtil
							.GetSmartNotifications(
									remind_style,
									PreManager.instance().getGetupTime_Setting(
											this),
									PreManager.instance().getSleepTime_Setting(
											this), suggestSleepTime);
				} else {
					// suggestSleepTime = startTime;
					mSmartRemindBean = SmartNotificationUtil
							.GetSmartNotifications(remind_style, endTime,
									startTime, suggestSleepTime);
				}
			} else {// 如果没有硬件的时候使用软件的数据
				SignInData mDaySleepMsg = SignInDBOperation.initDB(this).querySignInData(getYesterdayTime(), ("2".equals(getIntent().getStringExtra("dataType"))) ? "1":"0");
				if (null != mDaySleepMsg) {
					try {
						startTime =new SimpleDateFormat("HH:mm").format(Long.parseLong(mDaySleepMsg.getGoBedTime()));
						endTime = new SimpleDateFormat("HH:mm").format(Long.parseLong(mDaySleepMsg.getOutBedTime()));
					} catch (Exception e) {
					}
					if (null == startTime) {
						startTime = "";
					}
					if (null == endTime) {
						endTime = "";
					}
				}

				if (startTime.equals("") || endTime.equals("")) {
					startTime = sp.getString(SleepInfo.STARTTIME, "");
					endTime = sp.getString(SleepInfo.ENDTIME, "");

					if (startTime.equals("") || endTime.equals("")) {
						mSmartRemindBean = SmartNotificationUtil
								.GetSmartNotifications(
										remind_style,
										PreManager
												.instance()
												.getGetupTime_Setting(
														this),
										PreManager
												.instance()
												.getSleepTime_Setting(
														this),
										suggestSleepTime);
					} else {
						int start_time = Integer.parseInt(startTime);
						int end_time = Integer.parseInt(endTime);
						startTime = start_time
								/ 60
								+ ":"
								+ (start_time % 60 == 0 ? "00"
										: start_time % 60);
						endTime = end_time / 60 + ":"
								+ (end_time % 60 == 0 ? "00" : end_time % 60);
						mSmartRemindBean = SmartNotificationUtil
								.GetSmartNotifications(remind_style, endTime,
										startTime, suggestSleepTime);
					}

				} else {
					mSmartRemindBean = SmartNotificationUtil
							.GetSmartNotifications(remind_style, endTime,
									startTime, suggestSleepTime);
				}
			}
		}
		if (mSmartRemindBean != null
				&& mSmartRemindBean.SuggestRemindTime != null) {
			// 重新设置睡觉提醒时间
			LogUtil.d("chen", "睡前提醒 SuggestRemindTime = "+mSmartRemindBean.SuggestRemindTime +"\n" +mSmartRemindBean.Remindmsgs);
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
	
	public ArrayList<DaySleepMsg> list = new ArrayList<DaySleepMsg>();
	private void getDbData() {
		if (!this.isFinishing()) {
			MyDatabaseHelper helper = MyDatabaseHelper
					.getInstance(getApplicationContext());
			MytabOperate operate = new MytabOperate(
					helper.getWritableDatabase(), MyTabList.SLEEPTIME);
			try {
				list.clear();
				ArrayList<DaySleepMsg> tmplist = operate.queryDisplayDateList(
						DataUtils.getRecordDate(new

						Date()), getApplicationContext());
				if (tmplist != null) {
					list.addAll(tmplist);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			} finally {
				operate.close();
			}
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
//		calendar.set(Calendar.SECOND, 10);
		calendar.setTimeInMillis(remindtime);

		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
	}
	
	// 获取昨天的日期
	@SuppressLint("SimpleDateFormat")
	private String getYesterdayTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(new Date());
	}
//	private void sendFeedBackBroadcast(){
//		Intent intent = new Intent(Constant.WEEK_FEED_BACK_SUC);
//		this.sendBroadcast(intent);
//	}
}
