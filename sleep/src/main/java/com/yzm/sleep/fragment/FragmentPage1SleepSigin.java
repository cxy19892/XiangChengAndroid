package com.yzm.sleep.fragment;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.xpillowjni.XpillowInterface;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.activity.BedroomEnvironmentActivity;
import com.yzm.sleep.activity.DataCountActivity;
import com.yzm.sleep.activity.EstimateWebActivity;
import com.yzm.sleep.activity.ImprSleepPlanActivity;
import com.yzm.sleep.activity.LifeHabitActivity;
import com.yzm.sleep.activity.LoginActivity;
import com.yzm.sleep.activity.MessageListActivity;
import com.yzm.sleep.activity.RestToRegulActivity;
import com.yzm.sleep.activity.SetRemindActivity;
import com.yzm.sleep.activity.SignInActivity;
import com.yzm.sleep.activity.SleepCaseActivity;
import com.yzm.sleep.activity.SleepTrendActivity;
import com.yzm.sleep.activity.pillow.PillDataCountActivity;
import com.yzm.sleep.activity.pillow.PillowDataOpera;
import com.yzm.sleep.activity.pillow.PillowDayDataActivity;
import com.yzm.sleep.activity.pillow.PillowUpgradeActivity;
import com.yzm.sleep.background.DataUtils;
import com.yzm.sleep.background.JudgFuction;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.background.SignInDBOperation;
import com.yzm.sleep.background.SleepInfo;
import com.yzm.sleep.bean.SleepCaseBean;
import com.yzm.sleep.bluetoothBLE.CopyOfPillowHelper;
import com.yzm.sleep.bluetoothBLE.PillowCallback;
import com.yzm.sleep.model.ModifySignInResult;
import com.yzm.sleep.model.MyDialog;
import com.yzm.sleep.model.PillowDataModel;
import com.yzm.sleep.model.SignInData;
import com.yzm.sleep.model.SmartRemindBean;
import com.yzm.sleep.render.GetModelsValueClass.ModelsValues;
import com.yzm.sleep.render.GetSleepResultValueClass.SleepDataHead;
import com.yzm.sleep.utils.BluetoothDataFormatUtil;
import com.yzm.sleep.utils.BluetoothUtil;
import com.yzm.sleep.utils.CommunityProcClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadHardwareAllDayCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadHardwareDataCallBack1;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadHardwareAllDayParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadHardwareDataParamClass1;
import com.yzm.sleep.utils.InterfaceMallUtillClass.CancelFeedbackCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.CancelFeedbackParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.HomeDateParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceCaseCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceHomeDateCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSignInCallBack4_2_1;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.SleepDataProClass;
import com.yzm.sleep.utils.SmartNotificationUtil;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.ToastManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.CustomDialog;
import com.yzm.sleep.widget.OneKeyDialog;
import com.yzm.sleep.widget.SiginDataCircle;
import com.yzm.sleep.widget.SiginDataCircle.OnClickChangeTime;
import com.yzm.sleep.widget.SyncAlertDialog;
import com.yzm.sleep.widget.SyncAlertDialog.MySyncOnClickListener;
import com.yzm.sleep.widget.WaveView;

/**
 * 首页
 * 
 * @author hetonghua
 * 
 */
@SuppressLint("SimpleDateFormat")
public class FragmentPage1SleepSigin extends Fragment implements OnClickListener,
		OnClickChangeTime, MySyncOnClickListener{
	private SiginDataCircle dataCircle;
	private Activity activity;
	@SuppressWarnings("unused")
	private BluetoothAdapter mBluetoothAdapter;
	private CopyOfPillowHelper pillowserver = null;
	private boolean isgotdata = false;
	private boolean isneedUpLoad = false;
	private boolean isConnect = false;
	private boolean IS_NEED_TO_OPEN_BLUETOOTH = false;
	private CustomDialog syncFaileDialog = null;
	private ImageView hasNewMsg;
	/*4.2.0*/
//	private TextView tvDateToday;
	/*选择的时间与现在的时间差（天）*/
	private int selectDataindex = 1;
	private LinearLayout notSetPlanLin, areadySetPlanLin;
	private RelativeLayout RelaxLin, BedroomEnvLin, LiveHabitLin;// areadyOpenRelaxLin, areadyOpenBedroomEnvLin, areadyOpenLiveHabitLin;
	private ModifySignInResult mSignInResult;
	private ProgressUtils pro, pro2;
	private CustomDialog feedBackDialog = null;
	private WaveView waveView;
	/*4.2.1*/
//	private TextView notSetPlanStart1;
	private ImageView RelaxStart, BedroomEnvStart, HabitStart, RelaxState, BedroomState, HabitState;
//	private TextView OpenRelaxStart, OpenBedroomEnvStart, OpenLiveHabitStart;
	private TextView planNowT, planNextT, planNowTip, planNextTip, planLastT, planLastTip;
	private LinearLayout planNowState, planNextState, planLastState;
	private OneKeyDialog mOneKeyDialog;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerReceiver();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_sleep_sigin, container, false);
	}

	@SuppressLint("NewApi")
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView(view);
		selectDataindex = 1;
		
		dataCircle = (SiginDataCircle) view.findViewById(R.id.soft_day_data_view);
		dataCircle.setOnClickListener(this);
		FrameLayout.LayoutParams lpf = new FrameLayout.LayoutParams(Constant.screenHeight/2-60, Constant.screenHeight/2-100);
		lpf.leftMargin = (Constant.screenWidht-Constant.screenHeight/2 + 60)/2;
		lpf.rightMargin= (Constant.screenWidht-Constant.screenHeight/2 + 60)/2;
		dataCircle.setLayoutParams(lpf);
		hasNewMsg = (ImageView) view.findViewById(R.id.has_new_msg);
		hasNewMsg.setOnClickListener(this);
		activity.registerReceiver(bluetoothState, new IntentFilter(
				BluetoothAdapter.ACTION_STATE_CHANGED));
		waveView = (WaveView) view.findViewById(R.id.wave);
		waveView.postDelayed(new Runnable() {

			@Override
			public void run() {
				waveView.updateProgress(0.8f);
			}
		}, 1000);
		try {
			refreshDateLin();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setHasNewMsg(int visibility) {
		if (hasNewMsg != null)
			hasNewMsg.setVisibility(visibility);
	}

	private void initView(View view) {
		ImageButton ibShare = (ImageButton) view.findViewById(R.id.ib_left);
		ibShare.setOnClickListener(this);
		mOneKeyDialog = new OneKeyDialog(activity);

		ImageButton ibMessage = (ImageButton) view.findViewById(R.id.ib_right);
		ibMessage.setOnClickListener(this);
//		tvDateToday   = (TextView) view.findViewById(R.id.tv_date_yestoday);
		notSetPlanLin = (LinearLayout) view.findViewById(R.id.lin_not_set_plan);
		areadySetPlanLin = (LinearLayout) view.findViewById(R.id.lin_already_set_plan);
		RelaxLin  = (RelativeLayout) view.findViewById(R.id.rel_relax_control);
		BedroomEnvLin = (RelativeLayout) view.findViewById(R.id.rel_bedroom_control);
		LiveHabitLin  = (RelativeLayout) view.findViewById(R.id.rel_habit_control);

		RelaxStart      = (ImageView) view.findViewById(R.id.img_setrelax_start);
		BedroomEnvStart = (ImageView) view.findViewById(R.id.img_bedroom_start);
		HabitStart  = (ImageView) view.findViewById(R.id.img_habit_start); 
		
		RelaxState  = (ImageView) view.findViewById(R.id.img_relax_state);
		BedroomState= (ImageView) view.findViewById(R.id.img_bedroom_state);
		HabitState  = (ImageView) view.findViewById(R.id.img_habit_state);
		
		planNowT = (TextView) view.findViewById(R.id.tv_plan_nowtime);
		planNextT= (TextView) view.findViewById(R.id.tv_plan_nexttime);
		planLastT= (TextView) view.findViewById(R.id.tv_plan_lasttime);
		planNowTip = (TextView) view.findViewById(R.id.tv_plan_nowstate);
		planNextTip= (TextView) view.findViewById(R.id.tv_plan_nextstate);
		planLastTip= (TextView) view.findViewById(R.id.tv_plan_laststate);
		planNowState = (LinearLayout) view.findViewById(R.id.lin_now_state);
		planNextState= (LinearLayout) view.findViewById(R.id.lin_next_state);
		planLastState= (LinearLayout) view.findViewById(R.id.lin_last_state);
		
		notSetPlanLin.setOnClickListener(this);
//		notOpenRelaxLin.setOnClickListener(this);
//		notOpenBedroomEnvLin .setOnClickListener(this);
//		notOpenLiveHabitLin  .setOnClickListener(this);
		RelaxLin.setOnClickListener(this);
		BedroomEnvLin.setOnClickListener(this);
		LiveHabitLin.setOnClickListener(this);
		planNowState.setOnClickListener(this);
		planNextState.setOnClickListener(this);
		planLastState.setOnClickListener(this);
		
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((Constant.screenWidht - 210)/3, (Constant.screenWidht - 210)/3);
		lp.setMargins(35, 35, 35, 35);
		RelaxStart.setLayoutParams(lp);
		BedroomEnvStart.setLayoutParams(lp);
		HabitStart.setLayoutParams(lp);
	}
	
	/**
	 * 根据日期刷新数据
	 * @param 
	 */
	private void refreshDateLin(){
		areadySetPlanLin.setVisibility(View.GONE);
		notSetPlanLin.setVisibility(View.VISIBLE);
//		notOpenRelaxLin.setVisibility(View.VISIBLE);
//		notOpenBedroomEnvLin.setVisibility(View.VISIBLE);
//		notOpenLiveHabitLin.setVisibility(View.VISIBLE);
//		areadyOpenRelaxLin.setVisibility(View.GONE);
//		areadyOpenBedroomEnvLin.setVisibility(View.GONE);
//		areadyOpenLiveHabitLin .setVisibility(View.GONE);
		
		
		String getDayTimed = TimeFormatUtil.getOtherDay("yyyy-MM-dd", selectDataindex);
		String getDayTimes = TimeFormatUtil.getOtherDay("yyyyMMdd", selectDataindex);
		String daytimeStr = TimeFormatUtil.getOtherDay("yyyy年MM月dd日", selectDataindex);
		String todayString= TimeFormatUtil.getOtherDay("yyyy年MM月dd日", 1);
		String thisyear = TimeFormatUtil.getTodayYearTime();
		
		
		if(!TextUtils.isEmpty(daytimeStr) && daytimeStr.length()>4){
			if(daytimeStr.equals(todayString)){
				String todayStr = TimeFormatUtil.isToday();
				
				if(todayStr.equals(daytimeStr)){
					daytimeStr = "昨晚";
				}else if(daytimeStr.contains(thisyear)){
					daytimeStr = todayStr.substring(5);
					selectDataindex += 1;
					refreshDateLin();
					return;
				}		
			}else if(daytimeStr.contains(thisyear)){
				daytimeStr = daytimeStr.substring(5);
			}else{
			}
		}
//		tvDateToday.setText("昨晚睡眠未达标");
		getSleepInfoFromSer(getDayTimes, getDayTimed, true);
	}
	

	
	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.ib_left:// 分享
			if(!Util.isFastClick()){
				if (PreManager.instance().getIsLogin(activity)) 
					intent = new Intent(activity, SleepTrendActivity.class);
				else 
					intent = new Intent(activity, LoginActivity.class);
			}
			break;
		case R.id.ib_right:// 消息
			if(!Util.isFastClick()){
				if (PreManager.instance().getIsLogin(activity)) {
					intent = new Intent(activity, MessageListActivity.class);
				} else {
					intent = new Intent(activity, LoginActivity.class);
				}
			}
			break;
		case R.id.llWeekNews:// 睡眠周报
			MobclickAgent.onEvent(activity, "642");
			if (PreManager.instance().getIsLogin(activity)) {// 已登录
				if (TextUtils.isEmpty(PreManager.instance()
						.getBundbluetoothPillow(activity))) {// 未绑定硬件
					intent = new Intent(activity, DataCountActivity.class);
				} else {
					intent = new Intent(activity, PillDataCountActivity.class);
				}
			} else {
				intent = new Intent(activity, LoginActivity.class);
			}
			break;
		
		case R.id.lin_todays_sleep_tips:
			if(PreManager.instance().getIsLogin(activity)){//未登陆的时候提醒登陆
				intent = new Intent(activity, SetRemindActivity.class);
			}else{
				activity.startActivity(new Intent(activity, LoginActivity.class));
			}
			break;
			
		case R.id.lin_not_set_plan:
			if(PreManager.instance().getIsLogin(activity)){//未登陆的时候提醒登陆
				Intent improveSleepIntent = new Intent(activity, ImprSleepPlanActivity.class);
				startActivity(improveSleepIntent);
			}else{
				activity.startActivity(new Intent(activity, LoginActivity.class));
			}
			break;
		/*case R.id.lin_not_set_relax://减压调节
			if(PreManager.instance().getIsLogin(activity)){//未登陆的时候提醒登陆
				dialogForPinggu();
			}else{
				activity.startActivity(new Intent(activity, LoginActivity.class));
			}
			break;
			
		case R.id.lin_bedroom_env_notopen://卧室环境
			if(PreManager.instance().getIsLogin(activity)){//未登陆的时候提醒登陆
				dialogForPinggu();
			}else{
				activity.startActivity(new Intent(activity, LoginActivity.class));
			}
			
			break;
			
		case R.id.lin_live_habit_not_open://生活习惯
			if(PreManager.instance().getIsLogin(activity)){//未登陆的时候提醒登陆
				dialogForPinggu();
			}else{
				activity.startActivity(new Intent(activity, LoginActivity.class));
			}
			
			break;*/
			
		case R.id.lin_now_state:
			if(PreManager.instance().getIsLogin(activity)){
				intent = new Intent(activity, SleepCaseActivity.class);//
				intent.putExtra("state", planNowTip.getText().toString());
			}else{
				activity.startActivity(new Intent(activity, LoginActivity.class));
			}
			break;
		case R.id.lin_next_state:
			if(PreManager.instance().getIsLogin(activity)){
				intent = new Intent(activity, SleepCaseActivity.class);//
				intent.putExtra("state", planNextTip.getText().toString());
			}else{
				activity.startActivity(new Intent(activity, LoginActivity.class));
			}
			break;
		case R.id.lin_last_state:
			if(PreManager.instance().getIsLogin(activity)){
				intent = new Intent(activity, SleepCaseActivity.class);//
				intent.putExtra("state", planLastTip.getText().toString());
			}else{
				activity.startActivity(new Intent(activity, LoginActivity.class));
			}
			break;
			
		case R.id.rel_relax_control:
			if(PreManager.instance().getIsLogin(activity)){//未登陆的时候提醒登陆
				if(mSignInResult.getPinggu() == 1){
					intent = new Intent(activity, RestToRegulActivity.class);
				}else{
					dialogForPinggu();
				}
			}else{
				activity.startActivity(new Intent(activity, LoginActivity.class));
			}
			break;
		case R.id.rel_bedroom_control:
			if(PreManager.instance().getIsLogin(activity)){//未登陆的时候提醒登陆
				if(mSignInResult.getPinggu() == 1){
				intent = new Intent(activity, BedroomEnvironmentActivity.class);
				}else{
					dialogForPinggu();
				}
			}else{
				activity.startActivity(new Intent(activity, LoginActivity.class));
			}
			break;
		case R.id.rel_habit_control:
			if(PreManager.instance().getIsLogin(activity)){//未登陆的时候提醒登陆
				if(mSignInResult.getPinggu() == 1){
					intent = new Intent(activity, LifeHabitActivity.class);
				}else{
					dialogForPinggu();
				}
			}else{
				activity.startActivity(new Intent(activity, LoginActivity.class));
			}
			break;
//		case R.id.tv_date_yestoday:
//			mOneKeyDialog.show();
//			mOneKeyDialog.setMessage("睡眠效率＝睡着时长/躺床时长，它反应了你快速入睡和保持睡眠的能力，效率低于85%即为低质睡眠。");
//
//			break;
			
		default:
			break;
		}
		if (intent != null) {
			activity.startActivity(intent);
		}
	}

	/**
	 * 响应签到按钮
	 */
	@Override
	public void onClickPunch() {
		if(!Util.isFastClick()){
			if(PreManager.instance().getIsLogin(activity)){

				try {
					String date = DataUtils.getRecordDate(new Date());

					MyDatabaseHelper helper = MyDatabaseHelper.getInstance(activity.getApplicationContext());
					SQLiteDatabase db = helper.getWritableDatabase();
					Cursor cursor = db.rawQuery("select * from " + MyTabList.SLEEPTIME
							+ " where date = ? and record_state = ?", new String[] { date, "4"});
					if(cursor.getCount() == 0){
						DataUtils.saveRecordState(activity, date, "3");
						new JudgFuction().judge(activity.getApplicationContext(), date);
					}
				} catch (Exception e) {
				}

				try {
					Intent punchIntent = new Intent(activity, SignInActivity.class);
					String getDayTime = TimeFormatUtil.getOtherDay("yyyy-MM-dd", selectDataindex);
					if(TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(activity))){
						punchIntent.putExtra("type", "1");
					}else{
						punchIntent.putExtra("type", "2");
					}
					punchIntent.putExtra("date", getDayTime);
					startActivityForResult(punchIntent ,
							100);
				} catch (Exception e) {
					//				new PunchTipDialog(activity).show();
				}
			}else{
				activity.startActivity(new Intent(activity, LoginActivity.class));
			}
		}		
	}
	

	
	/**
	 * 打卡的返回
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100 && resultCode == 101) { // 打卡成功之后返回刷新数据
			refreshDateLin();
			getSleepDataAndSetSmartAlarm(1);
		}
		if(requestCode == 100){
			
		}
	}

	/**
	 * 点击顶部的环形view进入睡眠详情界面
	 */
	@Override
	public void onClickShowDaySleep() {// ~~~
		if(!Util.isFastClick()){
			MobclickAgent.onEvent(activity, "641");
			Intent intent = null;
			if (PreManager.instance().getIsLogin(activity)) {// 已登录
				if (TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(// 未绑定枕头
						activity))) {
					Intent punchIntent = new Intent(activity, SignInActivity.class);
					String getDayTime = TimeFormatUtil.getOtherDay("yyyy-MM-dd", selectDataindex);
					if(TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(activity))){
						punchIntent.putExtra("type", "1");
					}else{
						punchIntent.putExtra("type", "2");
					}
					punchIntent.putExtra("date", getDayTime);
					startActivityForResult(punchIntent ,
							100);
				} else {
					intent = new Intent(activity, PillowDayDataActivity.class);
				}
			} else {
				intent = new Intent(activity, LoginActivity.class);
			}
			if (intent != null) {
				activity.startActivity(intent);
			}
		}
	}

	/**
	 * 下载完数据之后刷新
	 */
	public void refreshDownloadData() {
		try {
			initSleepData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取数据，重绘视图
	 */
	@SuppressLint("SimpleDateFormat")
	private void initSleepData() {
		if (activity == null)
			return;
		boolean haveData = false;
		if (TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(
				activity))) {
			dataCircle.setIsBindPillow(false);
			if(mSignInResult!= null && 1 == (mSignInResult.getQiandao())){//已签到
				haveData = true;
			}else{
				haveData = false;
			}
		} else {
			dataCircle.setIsBindPillow(true);
			
			if(mSignInResult!= null && 1 == (mSignInResult.getQiandao())){//已签到
				haveData = true;
			}else{
				String todayString= TimeFormatUtil.getOtherDay("yyyy年MM月dd日", selectDataindex);
				String todayStr = TimeFormatUtil.isToday();
				if(todayStr.equals(todayString)){
					haveData = false;
				}else{
					haveData = true;
				}
			}
		}
		checkIsShowBtn(haveData);
		if(mSignInResult != null){
			if(mSignInResult.getPinggu() == 1){
				RelaxState.setVisibility(View.GONE);
				BedroomState.setVisibility(View.GONE);
				HabitState.setVisibility(View.GONE);
				RelaxStart.setImageResource(R.drawable.ic_chuang);
				BedroomEnvStart.setImageResource(R.drawable.ic_woshi);
				HabitStart.setImageResource(R.drawable.ic_zicha);
			}else{
				RelaxState.setVisibility(View.VISIBLE);
				BedroomState.setVisibility(View.VISIBLE);
				HabitState.setVisibility(View.VISIBLE);
				RelaxStart.setImageResource(R.drawable.ic_chuang_1);
				BedroomEnvStart.setImageResource(R.drawable.ic_woshi_1);
				HabitStart.setImageResource(R.drawable.ic_zhicha_1);
			}

			if(mSignInResult.getChangesleep() == 1){
				notSetPlanLin.setVisibility(View.GONE);
				areadySetPlanLin.setVisibility(View.VISIBLE);
				setPlansView();
			}else{
				areadySetPlanLin.setVisibility(View.GONE);
				notSetPlanLin.setVisibility(View.VISIBLE);
			}
			
			if(TextUtils.isEmpty(mSignInResult.getSleep_time()) || TextUtils.isEmpty(mSignInResult.getWakeup_time())
					|| mSignInResult.getSleep_time().equals("0")||mSignInResult.getWakeup_time().equals("0")){
				mSignInResult.setSleep_time(PreManager.instance().getSleepTime_Setting(activity));
				mSignInResult.setWakeup_time(PreManager.instance().getGetupTime_Setting(activity));
			}
			
			float efficient = 0f;
			try {
				efficient = Float.parseFloat(mSignInResult.getReport_data().getXiaolv());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			if(efficient == 0){
//				if(mSignInResult.getQiandao() == 0){
//					if(TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(activity))){
//						tvDateToday.setText("签到后，能够了解自己的睡眠效率");
//					}else{
//						tvDateToday.setText("同步后，能够了解自己的睡眠效率");
//					}
//				}else{
//					tvDateToday.setText("签到更多数据后才能了解睡眠效率");
//				}
//			}else if(efficient >= 0.85){
//				tvDateToday.setText("昨晚睡眠效率达标");
//			}else{
//				tvDateToday.setText("昨晚睡眠效率未达标");
//			}
			
			dataCircle.setData(mSignInResult.getSleep_time(), mSignInResult.getWakeup_time(),mSignInResult.getReport_data());
		}else{
			notSetPlanLin.setVisibility(View.VISIBLE);
			areadySetPlanLin.setVisibility(View.GONE);
			
			RelaxState.setVisibility(View.VISIBLE);
			BedroomState.setVisibility(View.VISIBLE);
			HabitState.setVisibility(View.VISIBLE);
			RelaxStart.setImageResource(R.drawable.ic_chuang_1);
			BedroomEnvStart.setImageResource(R.drawable.ic_woshi_1);
			HabitStart.setImageResource(R.drawable.ic_zhicha_1);
			
			dataCircle.setData("", "" , null);
		}
		if(mSignInResult != null && PreManager.instance().getIsLogin(activity)){
			if(mSignInResult.getReport_data().getReport_ok() == 1){
				dialogForWeekData();
			}
		}
		dataCircle.setOnClickChangeTime(this);
	}

	private void setPlansView(){
		
		if(Util.checkNetWork(activity)){
			getSleepCase();
		}else{
			String plansStr = PreManager.instance().getJSON_SLEEP_plans(activity);
			try {
				Gson gson = new Gson();
				List<SleepCaseBean> datas = gson.fromJson(plansStr, new TypeToken<List<SleepCaseBean>>(){}.getType());
				setCurrentPlan(datas);
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private SyncAlertDialog syncDialog;

	/**
	 * 点击同步按钮
	 */
	@Override
	public void onClickSyncData() {
		if (!TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(
				activity))) {
			boolean isOpen = BluetoothUtil.bluetoothIsOn(activity);
			if (isOpen) {
				MobclickAgent.onEvent(activity, "646");
				syncDialog = new SyncAlertDialog(activity,
						R.style.bottom_animation, this);
				syncDialog.show();
				isgotdata = false;
				isneedUpLoad = false;
				isConnect = false;
				connectDevice(PreManager.instance().getBundbluetoothPillow(
						activity));
				syncDialog.setCanceledOnTouchOutside(false);
				syncDialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(
							android.content.DialogInterface dialog,
							int keyCode, android.view.KeyEvent event) {
						if (keyCode == android.view.KeyEvent.KEYCODE_BACK
								&& event.getRepeatCount() == 0) {
							MobclickAgent.onEvent(activity, "647");
							if (syncDialog != null)
								syncDialog.cancel();
							if (pillowserver != null)
								pillowserver.Stop_server(activity);
						}
						return false;
					}
				});
			} else {
				showOpenbluetoothDialog();
			}
		} else {
			Toast.makeText(activity, "绑定数据出错，请重新绑定", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 通过mac链接设备
	 * @param mac
	 */
	@SuppressLint("NewApi")
	private void connectDevice(final String mac) {
		// 开始链接设备并同步数据 in_iSelect 1代表同步数据， 2代表绑定设备， 3代表空中升级
		final BluetoothManager bluetoothManager = (BluetoothManager) activity
				.getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		String sensitive = "6,2,2";
		sensitive = Util.getSensitive(
				PreManager.instance().getAllSensitives(activity), Integer
						.parseInt(PreManager.instance()
								.getBluetoothDevSensitive(activity)));

		pillowserver = CopyOfPillowHelper.Getinstance(activity, callback,
				sensitive);
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				pillowserver.startConnectDev(1, mac);
			}
		}, 100);

	}

	/**
	 * 设置同步百分比的显示
	 * @param data
	 */
	private void setSyncData(String data) {
		if (syncDialog != null) {
			float percent = 0;
			try {
				percent = Float.parseFloat(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
			int per = (int) percent;
			syncDialog.updataSyncValue(per);
		}
	}

	@Override
	public void onDestroy() {
		PreManager.instance().saveIsFirstUse(activity, false);
		cancelPro();
		if (pillowserver != null)
			pillowserver.Stop_server(activity);

		super.onDestroy();
	}

	/**
	 * 硬件枕扣的回调信息
	 */
	private PillowCallback callback = new PillowCallback() {
		/**
		 * 8000:传入数据有误，提示需要先绑定在同步<br>
		 * 8001:没有找到设备<br>
		 * 8002:找到了多个设备<br>
		 * 8003:找到设备开始链接<br>
		 * 8004:未连接GATT服务端<br>
		 * 8005:未发现GATT服务<br>
		 * 8006:DEVICE_DOES_NOT_SUPPORT_UART<br>
		 * 9000:密码校验失败<br>
		 * 9001:密码校验成功,开始同步数据<br>
		 * 9002:更换绑定成功<br>
		 * 9003:更滑绑定失败<br>
		 * 9004:电池电量返回 msg.obj<br>
		 * 9005:固件版本返回 msg.obj<br>
		 * 9006:数据包个数 msg.obj<br>
		 * 9007:固件收到发送的升级命令返回<br>
		 * 9008:数据包接受中 百分比 <br>
		 * 9009:数据包接收结束<br>
		 * 9010:时间同步完成<br>
		 * 9011:恢复出厂设置完成<br>
		 * 9012:异常结束<br>
		 * 9020:询问是否继续绑定
		 */
		@SuppressLint("SimpleDateFormat")
		@Override
		public void getPillowcallback(Message msg) {
			// Log.i(TAG, "getPillowcallback " + msg.what);
			switch (msg.what) {
			case 8004:// 链接 断开
				if (isConnect) {

					if (isgotdata) {// 已经获得数据
						if (!isneedUpLoad) {
							ToastManager.getInstance(activity).show("同步完成");
							if(!activity.isFinishing()){
								syncDialog.cancel();
							}
						}
						PreManager.instance().saveSyncHardwareSleepDate(
								activity,
								PreManager.instance().getUserId(activity)
										+ new SimpleDateFormat("yyyy-MM-dd")
												.format(new Date()));
						if(!activity.isFinishing()){
							if (syncDialog.isShowing())
								syncDialog.cancel();
						}
					} else {// 获取数据失败，可能是校验错误
						if(!activity.isFinishing()){
							if (syncDialog.isShowing()) {
								syncDialog.updataViews(2);
							}
						}
					}
					if (pillowserver != null)
						pillowserver.Stop_server(activity);

				} else {
					if (pillowserver != null)
						pillowserver.Stop_server(activity);
					if (syncDialog != null)
						connectDevice(PreManager.instance()
								.getBundbluetoothPillow(activity));
				}
				break;
			case 8006:// 用于重复链接
				if (pillowserver != null)
					pillowserver.Stop_server(activity);
				if (syncDialog != null)
					connectDevice(PreManager.instance().getBundbluetoothPillow(
							activity));
				break;
			case 8007:
				isConnect = true;
				break;
			case 9001:// 密码校验成功,开始同步数据
				break;
			case 9009:// 数据包接收结束
				isgotdata = true;
				syncDialog.updataSyncValue(100);
				break;
			case 9008:// 同步数据的进度
				syncDialog.updataViews(1);// changeSyncLoading();
				setSyncData(msg.obj.toString());
				break;
			case 9016:// 有新的升级信息
				PreManager.instance().saveSyncHardwareSleepDate(
						activity,
						PreManager.instance().getUserId(activity)
								+ new SimpleDateFormat("yyyy-MM-dd")
										.format(new Date()));
				isneedUpLoad = true;
				syncDialog.updataViews(3);
				break;
			default:
				break;
			}
		}

		@Override
		public void getPillowError(int errorcode, String errormsg) {
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		public void pillowData(byte[] buffer) {
			PreManager.instance().saveSyncHardwareSleepDate(
					activity,
					PreManager.instance().getUserId(activity)
							+ new SimpleDateFormat("yyyy-MM-dd")
									.format(new Date()));
			saveDataToFile(buffer);
		}
	};

	/**
	 * 保存数据
	 * 
	 * @param buffer
	 */
	@SuppressLint("SimpleDateFormat")
	private void saveDataToFile(byte[] buffer) {
		if (buffer == null || buffer.length == 0)
			return;
		List<ModelsValues> Modellist = BluetoothDataFormatUtil.format1(buffer);
		// 先存储总的buffer为.dat
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String datName = sdf.format(new Date()) + ".dat";
		String datPath = PillowDataOpera.saveDataToSDcard(buffer,
				datName);
		handleData(false, Modellist, datPath, datName);

	}

	/**
	 * 处理硬件数据
	 * 
	 * @param batIsUpload
	 * @param Modellist
	 * @param datPath
	 */
	private void handleData(boolean batIsUpload, List<ModelsValues> Modellist,
			String datPath, String datName) {
		List<ModelsValues> pModellist = new ArrayList<ModelsValues>();
		if (Modellist == null || Modellist.size() == 0) {
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					initSleepData();
					mOneKeyDialog.show();
					mOneKeyDialog.setTitle("昨晚枕扣未检测到您的睡眠");
				}
			});
			uploadDatFile(datPath, new ArrayList<String>());
			return;
		}
		List<String> list = new ArrayList<String>();
		for (ModelsValues modelsValues : Modellist) {
			if (modelsValues == null || modelsValues.mBuf == null
					|| modelsValues.mBuf.length == 0)
				continue;
			try {
				long dateTime = modelsValues.mtime;
				String sqlDate = TimeFormatUtil.formatTime1(dateTime,
						"yyyy-MM-dd");
				list.add(sqlDate);
				boolean isExist = PillowDataOpera.queryDataExistFromSQL(
						activity.getApplicationContext(), sqlDate);
				if (isExist) {
					PillowDataModel oldData = PillowDataOpera.queryDataFromSQL(
							activity.getApplicationContext(), sqlDate);
					if (oldData != null && oldData.getBfr() != null
							&& oldData.getBfr().length > 0) {
						XpillowInterface inter = new XpillowInterface();

						int which = inter.OneDayJudge(modelsValues.mBuf,
								modelsValues.mBuf.length, oldData.getBfr(),
								oldData.getBfr().length);
						if (which == 2 || which == -1) {
							continue;
						}
					}
				}
				String modelPath = PillowDataOpera.saveDataToSDcard(
						modelsValues.mBuf, sqlDate + ".model");
				PillowDataModel model = new PillowDataModel();
				model.setDate(sqlDate);
				model.setBfr(modelsValues.mBuf);
				model.setFileName(sqlDate + ".model");
				model.setIsUpload("0");
				if (batIsUpload)
					model.setDatIsUpload("1");
				else
					model.setDatIsUpload("0");
				model.setDatFileName(datName);
				if (isExist)
					PillowDataOpera.updateDataToSQL(
							activity.getApplicationContext(), model);
				else
					PillowDataOpera.insertDataToSQL(
							activity.getApplicationContext(), model);
				if (!TextUtils.isEmpty(modelPath))// 跟服务器保持一致，上传日期时间传long型（按秒来计算的long型）
					uploadDataToService(modelsValues, String.valueOf(dateTime),
							modelPath, sqlDate);
			} catch (Exception e) {
			}
			pModellist .add(modelsValues);
		}
		
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				
//				initSleepData();
				getSleepDataAndSetSmartAlarm(1);
			}
		});
		uploadDatFile(datPath, list);
		punchForPillowData(pModellist);
	}

	/** 上传dat文件@param datPath */
	private void uploadDatFile(String datPath, final List<String> dates) {
		UploadHardwareAllDayParamClass mParam = new UploadHardwareAllDayParamClass();
		mParam.path = datPath;
		mParam.my_int_id = PreManager.instance().getUserId(activity);
		mParam.date_of_data = String.valueOf(TimeFormatUtil.formatTime(
				System.currentTimeMillis(), "yyyyMMddHHmmss"));
		new SleepDataProClass(activity).UploadHardwareAllDay(mParam,
				new InterfaceUploadHardwareAllDayCallBack() {
					@Override
					public void onSuccess(int iCode, String strSuccMsg) {
						updateSqlState(dates, "1");
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						updateSqlState(dates, "0");
					}
				});
	}

	/**
	 * 将获取的硬件文件信息存入数据库
	 * @param dates 时间
	 * @param state 是否上传成功的状态
	 */
	private void updateSqlState(List<String> dates, String state) {
		for (String date : dates) {
			PillowDataModel model = new PillowDataModel();
			model.setDate(date);
			model.setDatIsUpload(state);
			PillowDataOpera.updateDataToSQL(activity.getApplicationContext(),
					model);
		}
	}

	/**
	 * 上传数据到服务器备份
	 * 
	 * @param modelsValues
	 * @param date
	 */
	private void uploadDataToService(ModelsValues modelsValues, String date,
			String modelPath, final String sqlDate) {
		SleepDataHead datahead = BluetoothDataFormatUtil.format2(modelsValues,
				10);
		UploadHardwareDataParamClass1 param = new UploadHardwareDataParamClass1();
		param.awakecount = String.valueOf(datahead.AwakeCount);
		param.awakenogetupcount = String.valueOf(datahead.AwakeNoGetUpCount);
		param.awaketimesleep = String.valueOf(datahead.AwakeTime_Sleep);
		param.date = date;
		param.deepsleep = String.valueOf(datahead.Deep_Sleep);
		param.file = modelPath;
		param.getuptime = String.valueOf(datahead.GetUpTime);
		param.gotobedtime = String.valueOf(datahead.GoToBedTime);
		param.insleeptime = String.valueOf(datahead.InSleepTime);
		param.listlength = "0";
		param.my_int_id = PreManager.instance().getUserId(activity);
		param.onbed = String.valueOf(datahead.OnBed);
		param.outsleeptime = String.valueOf(datahead.OutSleepTime);
		param.shallowsleep = String.valueOf(datahead.Shallow_Sleep);
		param.sleepbak1 = String.valueOf(datahead.SleepBak1);
		param.Sleepbak2 = String.valueOf(datahead.SleepBak2);
		param.tosleep = String.valueOf(datahead.ToSleep);
		param.totalsleeptime = String.valueOf(datahead.TotalSleepTime);
		param.user_location_x = PreManager.instance().getLocation_x(activity);
		param.user_location_y = PreManager.instance().getLocation_y(activity);
		param.xstart = String.valueOf(datahead.XStart);
		param.xstop = String.valueOf(datahead.XStop);
		param.ymax = String.valueOf(datahead.YMax);
		new CommunityProcClass(activity).UploadHardwareSleepData(param,
				new InterfaceUploadHardwareDataCallBack1() {

					@Override
					public void onError(int icode, String strErrMsg) {
					}

					@Override
					public void onSuccess(int icode, String strSuccMsg) {
						PillowDataModel model = new PillowDataModel();
						model.setIsUpload("1");
						model.setDate(sqlDate);
						PillowDataOpera.updateDataToSQL(activity, model);
					}
				});
	}

	@Override
	public void onDestroyView() {
		try {
			activity.unregisterReceiver(bluetoothState);
			activity.unregisterReceiver(mReceiver);
		} catch (Exception e) {
		}
		super.onDestroyView();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (waveView != null) {
			waveView.postDelayed(new Runnable() {

				@Override
				public void run() {
					waveView.updateProgress(0.8f);
				}
			}, 1000);
		}
		if(!TextUtils.isEmpty(PreManager.instance().getUserId(activity))){
			mHandler.sendEmptyMessage(1);
		}	
	}

	/**
	 * 检查是否显示同步数据的btn
	 * 
	 * @param haveData
	 */
	@SuppressLint("SimpleDateFormat")
	private void checkIsShowBtn(boolean haveData) {
		if (!TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(
				activity))) {
			dataCircle.setIsBindPillow(true);
			if (haveData)
				dataCircle.setDataIsSync(true);
			else {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
					String crrentTime = sdf.format(new Date());
					long t1 = sdf.parse("04:00").getTime();
					long t2 = sdf.parse(crrentTime).getTime();
					if (t2 - t1 >= 0) {
						String syncHardwareSleepDate = PreManager.instance()
								.getSyncHardwareSleepDate(activity);
						if (!TextUtils.isEmpty(syncHardwareSleepDate)
								&& syncHardwareSleepDate.equals(PreManager
										.instance().getUserId(activity)
										+ new SimpleDateFormat("yyyy-MM-dd")
												.format(new Date())))
							dataCircle.setDataIsSync(true);
						else
							dataCircle.setDataIsSync(false);
					} else
						dataCircle.setDataIsSync(true);
				} catch (Exception e) {
				}
			}
		} else{
			dataCircle.setIsBindPillow(false);
			if (haveData)
				dataCircle.setDataIsSync(true);
			else {
				dataCircle.setDataIsSync(false);
			}
		}
	}

	/**
	 * 注册广播
	 */
	private void registerReceiver() {
		IntentFilter inFilter = new IntentFilter();
		inFilter.addAction(Constant.BUND_BLUETOOTH_CHANGE);
		inFilter.addAction(Constant.RECEVER_EXIT);
		inFilter.addAction(Constant.RECEVER_USER_BIRTHDAY_UPDATE);
		inFilter.addAction(Constant.MODIFY_PLAN);
		inFilter.addAction(Constant.WEEK_FEED_BACK_SUC);
		inFilter.addAction(Constant.RECEVER_LOGIN_ACTION);
		inFilter.addAction(Constant.PINGGU_DEAL_ACTION);
		inFilter.addAction(Constant.SLEEP_PLAN_IMPROVE_ACTION);
		activity.registerReceiver(mReceiver, inFilter);
	}

	/**
	 * 接收广播后以及具体操作
	 */
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent data) {
			String action = data.getAction();
			if (Constant.BUND_BLUETOOTH_CHANGE.equals(action)) {
				selectDataindex = 1;
				mSignInResult = null;
				refreshDateLin();
			} else if (Constant.RECEVER_EXIT.equals(action)) {
				selectDataindex = 1;
				mSignInResult = null;
				refreshDownloadData();
			} else if (Constant.RECEVER_USER_BIRTHDAY_UPDATE.equals(action)) {
				refreshDownloadData();
			}else if(Constant.MODIFY_PLAN.equals(action) || Constant.SLEEP_PLAN_IMPROVE_ACTION.equals(action)){
				refreshDateLin();//重新请求数据
			}else if(Constant.WEEK_FEED_BACK_SUC .equals(action)){
				refreshDateLin();//重新请求数据
			}else if(Constant.RECEVER_LOGIN_ACTION.equals(action)){
				selectDataindex = 1;
				refreshDateLin();
			}else if(Constant.PINGGU_DEAL_ACTION.equals(action) ){
				if(Util.checkNetWork(activity)){
					refreshDateLin();
				}
			}
		}
	};

	/**
	 * 显示提示打开蓝牙的dialog
	 */
	private void showOpenbluetoothDialog() {

		final MyDialog exitDialog = new MyDialog(activity, 0, 0,
				R.layout.dialog_open_blutooth, R.style.bottom_animation,
				Gravity.BOTTOM, 0.96f, 0.0f);
		exitDialog.setCanceledOnTouchOutside(false);

		// 得到view中的控件
		TextView dialog_title = (TextView) exitDialog
				.findViewById(R.id.dialog_title);

		dialog_title.setText("Orange枕扣需要使用蓝牙功能，是否允许？");

		Button btn_allow = (Button) exitDialog
				.findViewById(R.id.btn_allow_open_bluetooth);
		Button btn_refuse = (Button) exitDialog
				.findViewById(R.id.btn_refuse_open_bluetooth);

		btn_allow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				IS_NEED_TO_OPEN_BLUETOOTH = true;
				BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
						.getDefaultAdapter();
				mBluetoothAdapter.enable();
				exitDialog.cancel();
			}
		});

		btn_refuse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				exitDialog.cancel();
			}
		});

		// 显示
		exitDialog.show();
	}

	/**
	 * 开始同步数据
	 */
	private void startToConnectdEV() {
		if (!activity.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(activity, R.string.ble_not_supported,
					Toast.LENGTH_SHORT).show();
		} else {
			syncDialog = new SyncAlertDialog(activity,
					R.style.bottom_animation, this);
			syncDialog.show();
			isgotdata = false;
			connectDevice(PreManager.instance()
					.getBundbluetoothPillow(activity));
		}
	}

	/**
	 * 蓝牙具体状态改变时的具体操作
	 */
	BroadcastReceiver bluetoothState = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			if (!activity.isFinishing()) {
				String stateExtra = BluetoothAdapter.EXTRA_STATE;
				int state = intent.getIntExtra(stateExtra, -1);
				switch (state) {
				case BluetoothAdapter.STATE_TURNING_ON:
					break;
				case BluetoothAdapter.STATE_ON:
					if (IS_NEED_TO_OPEN_BLUETOOTH) {
						IS_NEED_TO_OPEN_BLUETOOTH = false;
						startToConnectdEV();
					}
					break;
				case BluetoothAdapter.STATE_TURNING_OFF:
					break;
				case BluetoothAdapter.STATE_OFF:
					if(!activity.isFinishing()){
						if (syncDialog != null
								&& syncDialog.isShowing()
								&& (syncFaileDialog == null || !syncFaileDialog
										.isShowing())) {
							syncDialog.updataViews(2);
						}
					}
					if (pillowserver != null)
						pillowserver.Stop_server(activity);
					break;
				}
			}
		}
	};
	
	/**
	 * 硬件签到
	 */
	private void punchForPillowData(List<ModelsValues> Modellist){
		//1获取硬件的信息，打卡的日期是硬件日期的后一天
		//2强硬件的打卡信息存入一个list
		//3遍历为每一天的硬件信息，为确切的时间打卡
		List<SignInData> list = new ArrayList<SignInData>();
			for (ModelsValues modelsValues : Modellist) {
				if (modelsValues == null || modelsValues.mBuf == null
						|| modelsValues.mBuf.length == 0)
					continue;
				
				SleepDataHead datahead = null;
				String sqlDate = null;
				try {
					datahead = BluetoothDataFormatUtil.format3(
							modelsValues.mBuf, 10);
					
					long dateTime = modelsValues.mtime;
					sqlDate = TimeFormatUtil.formatTime1(dateTime,
							"yyyy-MM-dd");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(datahead!=null && sqlDate !=null){
					SignInData mSignInData = new SignInData();
					mSignInData.setDate(sqlDate);
					mSignInData.setGoBedTime((datahead.GoToBedTime*1000)+"");
					mSignInData.setHowLongSleepTime(datahead.ToSleep);
					mSignInData.setHowLongWakeTime(datahead.AwakeTime_Sleep - (datahead.ToSleep + datahead.AwakeNoGetUpCount));
					mSignInData.setOutBedTime((datahead.GetUpTime*1000)+"");
					mSignInData.setTrySleepTime(((datahead.InSleepTime - datahead.ToSleep * 60)*1000) +"");
					mSignInData.setWakeCount(datahead.AwakeCount);
					mSignInData.setWakeEarlyTime(0);
					mSignInData.setWakeUpTime((datahead.OutSleepTime*1000)+"");
					mSignInData.setShallowsleep(datahead.Shallow_Sleep);
					mSignInData.setDeepsleep(datahead.Deep_Sleep);
					mSignInData.setSoftOrOrange("1");
					list.add(mSignInData);
				}
			}
		
		if(list.size()>0){
			showProNotCancle();
			if(Util.checkNetWork(activity)){
				for (SignInData signInData : list) {
					LogUtil.d("chen", "硬件数据"+signInData.getDate());
					if(signInData.getDate().equals(TimeFormatUtil.getYesterdayTime())){
						signInData.setUpload(1);
						signInPillowData(signInData);
					}else{
						signInData.setUpload(1);
					}
				}
				SignInDBOperation.initDB(activity).updateSignInDataList(list, "1");
				mHandler.sendEmptyMessage(2);
				cancelProNotCancle();
				getPillowSigInFaildData();
			}else{
				LogUtil.d("chen", "网络不可用");
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setUpload(1);
				}
				SignInDBOperation.initDB(activity).updateSignInDataList(list, "1");
				mHandler.sendEmptyMessage(2);
				cancelProNotCancle();
			}
		}else{
			mHandler.sendEmptyMessage(1);
		}
	}
	//============================================================================================
	
	/**
	 * 将硬件信息上传服务器进行签到
	 * @param list
	 * @param signIn
	 */
	private void signInPillowData(final SignInData signIn){
		new XiangchengMallProcClass(activity).signIn4_2_1(PreManager.instance().getUserId(activity), "2", signIn, new InterfaceSignInCallBack4_2_1() {
			
			@Override
			public void onSuccess(String icode, JSONObject response) {
				try {
//					JSONObject json = new JSONObject(response.getString("report_data"));
//					signIn.setSignInId(json.getString("qiandaoid"));
					signIn.setResult(response.toString());
					SignInDBOperation.initDB(activity).updateSignInData(signIn, "1");//0软件1硬件
				} catch (Exception e) {
				}
				mHandler.sendEmptyMessage(2);
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
//				list.remove(0);
				signIn.setUpload(1);
				SignInDBOperation.initDB(activity).updateSignInData(signIn, "1");
			}
		}); 
	}
	

	/**
	 * 智能提醒
	 * @param remind_style
	 * 0 午间提醒 1 睡前提醒
	 */
	@SuppressLint("SimpleDateFormat")
	private void getSleepDataAndSetSmartAlarm(int remind_style) {
		SmartRemindBean mSmartRemindBean = null;
		SharedPreferences sp = activity.getApplicationContext().getSharedPreferences(SleepInfo.SLEEP_SETTIME, activity.getApplicationContext().MODE_APPEND);
		String startTime = "";
		String endTime = "";
		String suggestSleepTime = PreManager.instance().getSleepTime_Setting(activity);
		String remind_time_style = sp.getString(SleepInfo.REMIND_BEFORE_SLEEP, "");
		try {
			if(remind_time_style.equals("")){
				SignInData mSignInData= SignInDBOperation.initDB(activity).querySignInData(TimeFormatUtil.getYesterdayTime(), 
						TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(activity))?"0":"1");
				if(!TextUtils.isEmpty(mSignInData.getTrySleepTime()) && !TextUtils.isEmpty(mSignInData.getWakeUpTime())){
					try {
						startTime = TimeFormatUtil.formatTime(Long.parseLong(mSignInData.getTrySleepTime()), "HH:mm");
						endTime = TimeFormatUtil.formatTime(Long.parseLong(mSignInData.getWakeUpTime()), "HH:mm");
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(startTime.equals("")||endTime.equals("")){
					startTime = sp.getString(SleepInfo.STARTTIME, "");
					endTime = sp.getString(SleepInfo.ENDTIME, "");

					if(startTime.equals("")||endTime.equals("")){
						mSmartRemindBean = SmartNotificationUtil.GetSmartNotifications(remind_style, PreManager.instance().getGetupTime_Setting(activity), PreManager.instance().getSleepTime_Setting(activity), suggestSleepTime);
					}else{
						int start_time = Integer.parseInt(startTime);
						int end_time = Integer.parseInt(endTime);
						startTime = start_time/60+":"+(start_time%60 == 0?"00":start_time%60);
						endTime = end_time/60+":"+(end_time%60== 0?"00":end_time%60);

						mSmartRemindBean = SmartNotificationUtil.GetSmartNotifications(remind_style, endTime, startTime, suggestSleepTime);
					}

				}else{
					mSmartRemindBean = SmartNotificationUtil.GetSmartNotifications(remind_style, endTime, startTime, suggestSleepTime);
				}
			}
			if (mSmartRemindBean != null
					&& mSmartRemindBean.SuggestRemindTime != null) {
				// 重新设置睡觉提醒时间
				LogUtil.d("chen", mSmartRemindBean.SuggestRemindTime + mSmartRemindBean.Remindmsgs);
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
				//			setRemindAlarm(mSmartRemindBean.SuggestRemindTime);
				String datee = TimeFormatUtil.getTodayTime() + " "	+ mSmartRemindBean.SuggestRemindTime;
				String datee1 = TimeFormatUtil.getTomaDay("yyyy-MM-dd") + " " + mSmartRemindBean.SuggestRemindTime;

				SimpleDateFormat Day = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm");
				Date TimeSet, TimeSet1;
				TimeSet = Day.parse(datee);
				TimeSet1 = Day.parse(datee1);

				long PlantTime = TimeSet.getTime();
				long PlantTime1 = TimeSet1.getTime();

				long currSysTime = System.currentTimeMillis();

				if (currSysTime - PlantTime > 0)// 表示当前系统时间已经超过预设时间
				{
					setRemindAlarm(PlantTime1);

				} else {
					setRemindAlarm(PlantTime);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 设置智能提醒闹钟
	private void setRemindAlarm(long SuggestRemindTime) {
//		String[] times = SuggestRemindTime.split(":");
		Intent intent = new Intent("com.yzm.sleep.alarm.MART_NOTIFY");
		PendingIntent sender = PendingIntent.getBroadcast(activity, 0, intent,
				0);

		// We want the alarm to go off 10 seconds from now.
		Calendar calendar = Calendar.getInstance();
//		calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
//		calendar.set(Calendar.MINUTE, Integer.parseInt(times[1]));
//		calendar.set(Calendar.SECOND, 0);
		calendar.setTimeInMillis(SuggestRemindTime);

		// Schedule the alarm!
		@SuppressWarnings("static-access")
		AlarmManager am = (AlarmManager) activity
				.getSystemService(activity.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
	}



	/**
	 * 硬件同步dialog的具体操作
	 */
	@Override
	public void OnSyncClick(int value) {
		switch (value) {
		case 0:
			MobclickAgent.onEvent(activity, "647");
			if(!activity.isFinishing()){
				if (syncDialog != null)
					syncDialog.cancel();
			}
			if (pillowserver != null)
				pillowserver.Stop_server(activity);
			break;
		case 1:
			MobclickAgent.onEvent(activity, "649");
			syncDialog.dismiss();
			break;
		case 2:
			MobclickAgent.onEvent(activity, "648");
			boolean isOpen = BluetoothUtil.bluetoothIsOn(activity);
			if (isOpen) {
				syncDialog.updataViews(0);
				isgotdata = false;
				isneedUpLoad = false;
				isConnect = false;
				connectDevice(PreManager.instance().getBundbluetoothPillow(
						activity));
			} else {
				showOpenbluetoothDialog();
			}
			break;
		case 3:
			MobclickAgent.onEvent(activity, "655");
			pillowserver.cancleDevUpload();
			if (syncDialog != null)
				syncDialog.cancel();
			break;
		case 4:
			pillowserver.ensureDevUpload();
			MobclickAgent.onEvent(activity, "654");
			startActivity(new Intent(activity,

			PillowUpgradeActivity.class));
			if (syncDialog != null)
				syncDialog.cancel();
			break;
		default:
			break;
		}

	}

	/**
	 * 睡眠评估按钮的响应执行
	 */
	@Override
	public void onEvaluatedSleep() {
		Intent intent = null;
		if (PreManager.instance().getIsLogin(activity)) {
			intent = new Intent(activity, EstimateWebActivity.class);
			intent.putExtra("type", "0");
		}else{
			intent = new Intent(activity, LoginActivity.class);
		}
		if(intent != null){
			startActivity(intent);
		}
	}
	
	
	/**
	 * 网络请求指定日期的数据
	 * @param data 日期时间 yyyyMMdd
	 * @param getFromDBifFail 是否在失败之后使用本地 
	 */
	private void getSleepInfoFromSer(final String serdata, final String dbdata, final boolean getFromDBifFail){
		if(Util.checkNetWork(activity)){
			showPro();
			HomeDateParams mParams = new HomeDateParams();
			mParams.date = serdata;
			mParams.my_int_id = PreManager.instance().getUserId(activity);
			mParams.type = TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(activity))?"1":"2";
			new XiangchengMallProcClass(activity).getHomePageData(mParams, new InterfaceHomeDateCallBack() {

				@Override
				public void onSuccess(String icode, JSONObject response) {
					cancelPro();
//					LogUtil.d("chen", response.toString());
					mSignInResult = Util.getModifySleepInfoFromJson(response.toString());
					
					if(!TextUtils.isEmpty(mSignInResult.getSleep_time()) && !TextUtils.isEmpty(mSignInResult.getWakeup_time())){
						try {
							int s_hour = 0, s_min = 0, e_hour = 0, e_min = 0;
							int startTime = 0, endTime = 0 ;
							try {
								PreManager.instance().saveGetupTime_Setting(activity, mSignInResult.getWakeup_time());
								PreManager.instance().saveSleepTime_Setting(activity, mSignInResult.getSleep_time());
								s_hour = Integer.parseInt(mSignInResult.getSleep_time().split(":")[0]);
								s_min  = Integer.parseInt(mSignInResult.getSleep_time().split(":")[1]);
								startTime = s_hour*60+s_min;
								e_hour = Integer.parseInt(mSignInResult.getWakeup_time().split(":")[0]);
								e_min  = Integer.parseInt(mSignInResult.getWakeup_time().split(":")[1]);
								endTime = e_hour*60+e_min;
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
							if(endTime < 0){
								endTime = 24*60+endTime;
							}
							if(startTime < 0){
								startTime = 24*60+startTime;
							}
							
							SharedPreferences sp = activity.getSharedPreferences(SleepInfo.SLEEP_SETTIME, activity.MODE_APPEND);
							Editor edit = sp.edit();
							edit.putString(SleepInfo.STARTTIME, startTime+"");
							edit.putString(SleepInfo.ENDTIME, endTime +"");
							edit.commit();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					//存入数据库
					String StrType = "0";
					if(TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(
							activity))){//软件
						StrType = "0";
					}else{//硬件
						StrType = "1";
					}
					if(mSignInResult != null && 1 == mSignInResult.getQiandao()){
						SignInData signdata = new SignInData();
						SignInData getsignData = SignInDBOperation.initDB(activity).querySignInData(dbdata, StrType);
						if(TextUtils.isEmpty(getsignData.getTrySleepTime())){
							signdata.setDate(dbdata);
							try {
								signdata.setTrySleepTime((Long.parseLong(mSignInResult.getReport_data().getSleep())*1000)+"");
								signdata.setWakeUpTime((Long.parseLong(mSignInResult.getReport_data().getWakeup())*1000)+"");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else{
							signdata = getsignData;
						}
						signdata.setSoftOrOrange(StrType);
						signdata.setResult(response.toString());
						SignInDBOperation.initDB(activity).updateSignInData(signdata, StrType);
					}
					mHandler.sendEmptyMessage(1);
				}

				@Override
				public void onError(String icode, String strErrMsg) {
					cancelPro();
					cancelPro();
					if(icode.equals("4911")){//日期错误  yyyyMMdd
						//根据获取的日期在去请求数据。
						String serDataStr = Util.getTimeFromString(strErrMsg);
						serDataStr = TimeFormatUtil.getYesterDay(serDataStr, "yyyy-MM-dd");
						if(!TextUtils.isEmpty(serDataStr)){
							getSleepInfoFromSer(serDataStr.replace("-", ""), serDataStr, getFromDBifFail);
						}
					}else{
						if(getFromDBifFail  && PreManager.instance().getIsLogin(activity)){//如果方案为请求失败则使用本地
							mSignInResult = getSleepDataInfoFromDB(dbdata);
						}else{//如果方案为请求失败不适用本地
							mSignInResult = null;
						}
						mHandler.sendEmptyMessage(1);
					}
				}
			});
		}else{
			//读取本地数据库
			if(getFromDBifFail){//如果方案为请求失败则使用本地
				mSignInResult = getSleepDataInfoFromDB(dbdata);
			}else{//如果方案为请求失败不适用本地
				mSignInResult = null;
			}
			mHandler.sendEmptyMessage(1);
			ToastManager.getInstance(activity).show("网络连接失败");
		}
	}
	
	/**
	 * 取消周反馈
	 * @param dataType
	 */
	private void cancleFeedBack(String dataType){
		CancelFeedbackParams mParams = new CancelFeedbackParams();
		mParams.date = TimeFormatUtil.getOtherDay("yyyyMMdd", selectDataindex);
		mParams.my_int_id = PreManager.instance().getUserId(activity);
		mParams.type = dataType;
		new XiangchengMallProcClass(activity).complatePlan(mParams, new CancelFeedbackCallBack() {
			
			@Override
			public void onSuccess(int icode, String strSuccMsg) {
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
			}
		});
	}
	
	
	/**
	 * 从数据库获取指定时间的数据
	 * @param date yyyyMMdd
	 */
	private ModifySignInResult getSleepDataInfoFromDB(String date){
		ModifySignInResult mtempInfo = null;
		SignInData signData = SignInDBOperation.initDB(activity).querySignInData(date, TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(
				activity))?"0":"1");
		if(signData!=null && !TextUtils.isEmpty(signData.getResult())){
			mtempInfo = Util.getModifySleepInfoFromJson(signData.getResult());
		}else{
			//没有数据
			mtempInfo = null;
		}
		return mtempInfo;
	}
	
	
	@SuppressLint("HandlerLeak") 
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				refreshDownloadData();
				break;
			case 2:
				refreshDateLin();
				break;
			default:
				super.handleMessage(msg);
				break;
			}
		}
		
	};

	
	/**
	 * 显示进度
	 */
	public void showPro() {
		if (pro == null) {
			pro = new ProgressUtils(activity);
		}
		pro.setCanceledOnTouchOutside(false);
		pro.show();
	}

	/**
	 * 取消进度
	 */
	public void cancelPro() {
		if(!activity.isFinishing()){
			if (pro != null) {
				pro.cancel();
				pro = null;
			}
		}
	}
	
	/**
	 * 显示进度
	 */
	public void showProNotCancle() {
		if (pro2 == null) {
			pro2 = new ProgressUtils(activity);
		}
		pro2.setCancelable(false);
		pro2.setCanceledOnTouchOutside(false);
		pro2.show();
	}

	/**
	 * 取消进度
	 */
	public void cancelProNotCancle() {
		if(!activity.isFinishing()){
			if (pro2 != null) {
				pro2.cancel();
				pro2 = null;
			}
		}
	}
	
	
	private void getSleepCase(){
		new XiangchengMallProcClass(activity).getSleepCaseData(PreManager.instance().getUserId(activity), new InterfaceCaseCallBack() {
			
			@Override
			public void onSuccess(String icode, List<SleepCaseBean> datas) {
				try {
					JSONArray jsonAry = new JSONArray();
					if(datas != null){
						for (SleepCaseBean mSleepCaseBean : datas) {
							JSONObject jsonObjItem = new JSONObject();
							if(mSleepCaseBean != null){
								jsonObjItem.put("start_time", mSleepCaseBean.getStart_time());
								jsonObjItem.put("end_time", mSleepCaseBean.getEnd_time());
								jsonObjItem.put("title", mSleepCaseBean.getTitle());
//								jsonObjItem.put("suggest", "[建议1,建议1建议1]");
							}
							jsonAry.put(jsonObjItem);
						}
					}
					PreManager.instance().saveJSON_SLEEP_plans(activity, jsonAry.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				setCurrentPlan(datas);
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
			}
		});
	}
	
	
	private void setCurrentPlan(List<SleepCaseBean> datas){
		for (SleepCaseBean sleepCaseBean : datas) {
			if(TimeFormatUtil.isBetweenTime(sleepCaseBean.getStart_time(), sleepCaseBean.getEnd_time())){
				planNowT.setText("现在");
				planNowTip.setText(sleepCaseBean.getTitle());
				for (SleepCaseBean tempCaseBean : datas) {
					if(tempCaseBean.getStart_time().equals(sleepCaseBean.getEnd_time())){
						planNextT.setText(tempCaseBean.getStart_time());
						planNextTip.setText(tempCaseBean.getTitle());
					}
					if(tempCaseBean.getEnd_time().equals(sleepCaseBean.getStart_time())){
						planLastT.setText(tempCaseBean.getEnd_time());
						planLastTip.setText(tempCaseBean.getTitle());
					}
				}
				break;
			}
		}
	}
	
	
	/**
	 * 周报弹窗
	 */
	private void dialogForWeekData() {
		if(feedBackDialog == null || !feedBackDialog.isShowing()){
			feedBackDialog = new CustomDialog(activity);
			feedBackDialog.show();
			feedBackDialog.setTitle("一周反馈");
			feedBackDialog.setSub("香橙顾问非常高兴为您服务，在过去一周，您的睡眠情况如何呢？");
			feedBackDialog.setOnLeftClickListener("取消", new
					com.yzm.sleep.widget.CustomDialog.MyOnClickListener() {

				@Override
				public void Onclick(View v) {
					feedBackDialog.dismiss();
					SignInData signData = new SignInData();
					SignInData getsignData = SignInDBOperation.initDB(activity).querySignInData(TimeFormatUtil.getOtherDay("yyyy-MM-dd", selectDataindex), TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(
							activity))?"0":"1");
					mSignInResult.getReport_data().setReport_ok(0);;
					signData = getsignData;
					signData.setResult(mSignInResult.toString());
					SignInDBOperation.initDB(activity).updateSignInData(signData, TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(
							activity))?"0":"1");
					
					cancleFeedBack(TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(
							activity))?"1":"2");
					mHandler.sendEmptyMessage(1);
				}
			});
			feedBackDialog.setOnRightClickListener("反馈", new

					com.yzm.sleep.widget.CustomDialog.MyOnClickListener() {

				@Override
				public void Onclick(View v) {
					feedBackDialog.dismiss();
					SignInData signData = new SignInData();
					SignInData getsignData = SignInDBOperation.initDB(activity).querySignInData(TimeFormatUtil.getOtherDay("yyyy-MM-dd", selectDataindex), TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(
							activity))?"0":"1");
					mSignInResult.getReport_data().setReport_ok(0);;
					signData = getsignData;
					signData.setResult(mSignInResult.toString());
					SignInDBOperation.initDB(activity).updateSignInData(signData, TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(
							activity))?"0":"1");
					
					Intent intent = new Intent(activity, EstimateWebActivity.class);
					intent.putExtra("type", "5");
					intent.putExtra("date", TimeFormatUtil.getOtherDay("yyyyMMdd", selectDataindex));
					if(TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(
							activity))){
						intent.putExtra("datatype", "1");
					}else{
						intent.putExtra("datatype", "2");
					}
					startActivity(intent);
					
				}
			});
		}
	}
	
	private void dialogForPinggu() {
			final CustomDialog dialog = new CustomDialog(
					activity);
			dialog.show();
			dialog.setTitle("使用该功能前，需要进行全面睡眠评估，是否评估？");
			dialog.setSubGone();
			dialog.setOnLeftClickListener("取消", new

			com.yzm.sleep.widget.CustomDialog.MyOnClickListener() {
				@Override
				public void Onclick(View v) {
					dialog.dismiss();
				}
			});
			dialog.setOnRightClickListener("评估", new

			com.yzm.sleep.widget.CustomDialog.MyOnClickListener() {
				@Override
				public void Onclick(View v) {
					Intent intent = new Intent(activity, EstimateWebActivity.class);
					intent.putExtra("type", "0");
					startActivity(intent);
					dialog.dismiss();
				}
			});
	}
	
	private void getPillowSigInFaildData(){
		List<SignInData> list = SignInDBOperation.initDB(activity).getUnUploadData();
		if(list.size()>0){
			for(SignInData mSignInData : list){
				signInPillowOherData(mSignInData);
			}
		}
	}
	
	private void signInPillowOherData(final SignInData signIn){
		if(signIn != null){
			
			new XiangchengMallProcClass(activity).signIn4_2_1(PreManager.instance().getUserId(activity), "2", signIn, new InterfaceSignInCallBack4_2_1() {
				
				@Override
				public void onSuccess(String icode, JSONObject response) {
					try {
						LogUtil.d("chen", signIn.getDate()+"签到成功");
						JSONObject json = new JSONObject(response.getString("report_data"));
						signIn.setSignInId(json.getString("qiandaoid"));
						signIn.setResult(response.toString());
						signIn.setUpload(0);
						SignInDBOperation.initDB(activity).updateSignInData(signIn, "1");//0软件1硬件
					} catch (Exception e) {
					}
				}
				
				@Override
				public void onError(String icode, String strErrMsg) {
				}
			});
		}
	}

	@Override
	public void onClickDialog() {
		if(PreManager.instance().getIsLogin(activity)){
			mOneKeyDialog.show();
			mOneKeyDialog.setMessage("睡眠效率＝睡着时长/躺床时长，它反应了你快速入睡和保持睡眠的能力，效率低于85%即为低质睡眠。");
		}else{
			activity.startActivity(new Intent(activity, LoginActivity.class));
		}
	}
	
}
