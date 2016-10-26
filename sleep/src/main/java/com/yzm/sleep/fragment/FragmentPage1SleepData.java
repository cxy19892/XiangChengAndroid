package com.yzm.sleep.fragment;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.umeng.analytics.MobclickAgent;
import com.xpillowjni.XpillowInterface;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.activity.DataCountActivity;
import com.yzm.sleep.activity.EmotionManagerActivity;
import com.yzm.sleep.activity.EstimateWebActivity;
import com.yzm.sleep.activity.LoginActivity;
import com.yzm.sleep.activity.MessageListActivity;
import com.yzm.sleep.activity.RelaxActivity;
import com.yzm.sleep.activity.SetRemindActivity;
import com.yzm.sleep.activity.ShareActivity;
import com.yzm.sleep.activity.SignInActivity;
import com.yzm.sleep.activity.SleepStatusDetailActivity;
import com.yzm.sleep.activity.SleepTrendActivity;
import com.yzm.sleep.activity.community.CommunityTopiceDetailActivity;
import com.yzm.sleep.activity.community.ProgramDetailsActivity;
import com.yzm.sleep.activity.pillow.PillDataCountActivity;
import com.yzm.sleep.activity.pillow.PillowDataOpera;
import com.yzm.sleep.activity.pillow.PillowUpgradeActivity;
import com.yzm.sleep.adapter.DaysPlanAdapter;
import com.yzm.sleep.adapter.DaysPlanAdapter.DayPlanClickCallBack;
import com.yzm.sleep.background.DataUtils;
import com.yzm.sleep.background.JudgFuction;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.background.SignInDBOperation;
import com.yzm.sleep.background.SleepInfo;
import com.yzm.sleep.bluetoothBLE.CopyOfPillowHelper;
import com.yzm.sleep.bluetoothBLE.PillowCallback;
import com.yzm.sleep.model.MyDialog;
import com.yzm.sleep.model.PillowDataModel;
import com.yzm.sleep.model.SignInData;
import com.yzm.sleep.model.SignInResult;
import com.yzm.sleep.model.SignInResult.Plan;
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
import com.yzm.sleep.utils.InterfaceMallUtillClass.ComplatePlanCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.ComplatePlanParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.EvaluateSignInCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.EvaluateSignInParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetHomePageDataCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetHomePageDataParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSignInCallBack;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.PunchTipDialog;
import com.yzm.sleep.utils.SleepConstants;
import com.yzm.sleep.utils.SleepDataProClass;
import com.yzm.sleep.utils.SmartNotificationUtil;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.ToastManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.CustomDialog;
import com.yzm.sleep.widget.CustomListView;
import com.yzm.sleep.widget.KJScrollView;
import com.yzm.sleep.widget.OneKeyDialog;
import com.yzm.sleep.widget.SignInCircle;
import com.yzm.sleep.widget.SignInCircle.OnClickChangeTime;
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
public class FragmentPage1SleepData extends Fragment implements OnClickListener,
		OnClickChangeTime, MySyncOnClickListener, DayPlanClickCallBack {
//	private TextView tvSeepDetail;
	private SignInCircle dataCircle;
	private Activity activity;
	private SoftDayData dayData;
	// private String date;
	// addchen
	@SuppressWarnings("unused")
	private BluetoothAdapter mBluetoothAdapter;
	private CopyOfPillowHelper pillowserver = null;
	private boolean isgotdata = false;
	private boolean isneedUpLoad = false;
	private boolean isConnect = false;
	private boolean IS_NEED_TO_OPEN_BLUETOOTH = false;
	private CustomDialog syncFaileDialog = null;
	private ImageView hasNewMsg;
	private boolean isPunchShowing = false;
	private IWeiboShareAPI mWeiboShareAPI;
	/*4.2.0*/
	private Button btnLastDay, btnNextDay;
	private TextView tvDateToday;
	/*选择的时间与现在的时间差（天）*/
	private int selectDataindex = 1;
	private TextView yesterdayContent, welcomeTime;
	private TextView punchDaysTips, daysplanTips, pingguTips, planTips;
	private TextView sleepTimetips, relaxTvBtn, feelgoodTv, feelbadTv;
	private ImageView sleepFeelImg;
	private LinearLayout punchTimesLin, todaySleepTimeLin, daysPlanLin, eqMnagerLin, feelSelectLin, relaxLin, welconLin;
	private RelativeLayout afterPunchRela;
	private EditText edThink, edtFeel;
	private Button emotionCommitBtn;
	private CustomListView daysPlanListv;
	private SignInResult mSignInResult;
//	private SleepDataInfoBaen sleepInfo;
	private DaysPlanAdapter mPlanAdapter;
	private Animation animation, animation2, animation3;
	private ProgressUtils pro, pro2;
	private CustomDialog feedBackDialog = null;
	private KJScrollView mScrollView;
	private WaveView waveView;
	/**
	 * 睡眠详情跳转类型 0-首次使用无数据 1-没有睡眠数据 2-有数据
	 * 
	 * */
//	private int targetIntentType = -1;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// date = getArguments().getString("date");
		registerReceiver();
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(activity,
				SleepConstants.SINA_SLEEP_APP_KEY);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_sleep_data, container, false);
	}

	@SuppressLint("NewApi")
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView(view);
		selectDataindex = 1;
		
		dataCircle = (SignInCircle) view.findViewById(R.id.soft_day_data_view);
		dataCircle.setOnClickListener(this);
//		tvSeepDetail = (TextView) view.findViewById(R.id.tvSeepDetail);
		hasNewMsg = (ImageView) view.findViewById(R.id.has_new_msg);
		hasNewMsg.setOnClickListener(this);
		activity.registerReceiver(bluetoothState, new IntentFilter(
				BluetoothAdapter.ACTION_STATE_CHANGED));
		waveView = (WaveView) view.findViewById(R.id.wave);
		waveView.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
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
//		ImageButton ibShare = (ImageButton) view.findViewById(R.id.ib_left);
//		ibShare.setOnClickListener(this);

		ImageButton ibMessage = (ImageButton) view.findViewById(R.id.ib_right);
		ibMessage.setOnClickListener(this);
		btnLastDay = (Button) view.findViewById(R.id.btn_last_day);
		btnNextDay = (Button) view.findViewById(R.id.btn_next_day);
		tvDateToday= (TextView) view.findViewById(R.id.tv_date_today);
		yesterdayContent = (TextView) view.findViewById(R.id.tv_todays_sleep_tips);
		punchDaysTips = (TextView) view.findViewById(R.id.tv_punch_clock_times);
		sleepTimetips = (TextView) view.findViewById(R.id.tv_todays_sleep_times);
		sleepFeelImg  = (ImageView) view.findViewById(R.id.iv_feel_smail_face);
		punchTimesLin = (LinearLayout) view.findViewById(R.id.lin_clock_times);
		todaySleepTimeLin = (LinearLayout) view.findViewById(R.id.lin_todays_sleep_tips);
		daysPlanLin = (LinearLayout) view.findViewById(R.id.lin_days_plan);
		eqMnagerLin = (LinearLayout) view.findViewById(R.id.lin_eq_manager);
		feelSelectLin = (LinearLayout) view.findViewById(R.id.lin_feels_select);
		feelgoodTv = (TextView) view.findViewById(R.id.tv_feels_good);
		feelbadTv  = (TextView) view.findViewById(R.id.tv_feels_badly);
		afterPunchRela = (RelativeLayout) view.findViewById(R.id.rel_content_after_punch);
		edThink = (EditText) view.findViewById(R.id.edt_sleep_thinking);
		edtFeel = (EditText) view.findViewById(R.id.edt_sleep_feeling);
		emotionCommitBtn = (Button) view.findViewById(R.id.btn_commit_emotion);
		daysPlanListv = (CustomListView) view.findViewById(R.id.lv_days_plan);
		relaxTvBtn = (TextView) view.findViewById(R.id.tv_relax_self);
		relaxLin = (LinearLayout) view.findViewById(R.id.lin_relax_self);
		view.findViewById(R.id.lin_sleep_time_select).setOnClickListener(this);
//		view.findViewById(R.id.tv_todays_setting_remind).setOnClickListener(this);
		welconLin = (LinearLayout) view.findViewById(R.id.lin_todays_welcome);
		mScrollView = (KJScrollView) view.findViewById(R.id.m_scrollview);
		daysplanTips = (TextView) view.findViewById(R.id.tv_days_plan);
		welcomeTime = (TextView) view.findViewById(R.id.tv_todays_welcome_time);
		pingguTips = (TextView) view.findViewById(R.id.tv_welcome_go_pinggu);
		planTips   = (TextView) view.findViewById(R.id.tv_welcome_go_plan);
		emotionCommitBtn.setOnClickListener(this);
		btnLastDay.setOnClickListener(this);
		btnNextDay.setOnClickListener(this);
		sleepFeelImg.setOnClickListener(this);
		feelgoodTv.setOnClickListener(this);
		feelbadTv.setOnClickListener(this);
		tvDateToday.setOnClickListener(this);//不做处理，只是为了防止点到背景view
		relaxTvBtn.setOnClickListener(this);
		todaySleepTimeLin.setOnClickListener(this);
		mPlanAdapter = new DaysPlanAdapter(activity, this);
		daysPlanListv.setAdapter(mPlanAdapter);
		
		animation = AnimationUtils.loadAnimation(activity, R.anim.alpha_out);
		animation2 = AnimationUtils.loadAnimation(activity, R.anim.accordion_right_in); 
		animation3 = AnimationUtils.loadAnimation(activity, R.anim.accordion_left_in);
//		animation4 = AnimationUtils.loadAnimation(activity, R.anim.dialog_bottom_out); 
		
		view.findViewById(R.id.ib_left).setOnClickListener(this);
	}
	
	/**
	 * 根据日期刷新数据
	 * @param 
	 */
	private void refreshDateLin(){
		punchTimesLin.setVisibility(View.VISIBLE);
		feelSelectLin.setVisibility(View.GONE);
		
		String getDayTimed = TimeFormatUtil.getOtherDay("yyyy-MM-dd", selectDataindex);
		String getDayTimes = TimeFormatUtil.getOtherDay("yyyyMMdd", selectDataindex);
		String daytimeStr = TimeFormatUtil.getOtherDay("yyyy年MM月dd日", selectDataindex);
//		String weektimeStr= TimeFormatUtil.getWeekDate(selectDataindex);
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
				btnNextDay.setClickable(true);
			}else{
				btnNextDay.setClickable(true);
			}
		}
		tvDateToday.setText(daytimeStr);
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
//			mWeiboShareAPI.registerApp();
//			MobclickAgent.onEvent(activity, "640");
//			if (PreManager.instance().getIsLogin(activity)) {
//				if (dayData != null
//						&& !TextUtils.isEmpty(dayData.getSleepTime())
//						&& !TextUtils.isEmpty(dayData.getGetUpTime())) {
//					intent = new Intent(activity, ShareActivity.class);
//					intent.putExtra("sleep_data", dayData);
//					intent.putExtra("from", Constant.SHARE_FROM_SLEEP_DATA);
//				} else {
//					Util.show(activity, "暂无数据分享");
//				}
//			} else {
//				intent = new Intent(activity, LoginActivity.class);
//			}

			intent = new Intent(activity, SleepTrendActivity.class);
			
			break;
		case R.id.ib_right:// 消息
//			getmodle();
			if (PreManager.instance().getIsLogin(activity)) {
				intent = new Intent(activity, MessageListActivity.class);
			} else {
				intent = new Intent(activity, LoginActivity.class);
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
		case R.id.iv_feel_smail_face:
			punchTimesLin.startAnimation(animation);
			feelSelectLin.startAnimation(animation2);
			punchTimesLin.setVisibility(View.GONE);
			feelSelectLin.setVisibility(View.VISIBLE);
			break;
		case R.id.tv_feels_good:
//			feelSelectLin.startAnimation(animation);
			punchTimesLin.startAnimation(animation3);
			punchTimesLin.setVisibility(View.VISIBLE);
			feelSelectLin.setVisibility(View.GONE);
			sleepFeelImg.setImageResource(R.drawable.ic_smile);
			if(mSignInResult!= null&& mSignInResult.getReport_data()!=null){
				setFeelingsChange("1", mSignInResult.getReport_data().getQiandaoid());
			}
			break;
		case R.id.tv_feels_badly:
//			feelSelectLin.startAnimation(animation);
			punchTimesLin.startAnimation(animation3);
			punchTimesLin.setVisibility(View.VISIBLE);
			feelSelectLin.setVisibility(View.GONE);
			sleepFeelImg.setImageResource(R.drawable.ic_cry);
			if(mSignInResult!= null&& mSignInResult.getReport_data()!=null){
				setFeelingsChange("-1", mSignInResult.getReport_data().getQiandaoid());
			}
			break;
		case R.id.btn_last_day:
			if(PreManager.instance().getIsLogin(activity)){//未登陆的时候提醒登陆
			if(mSignInResult!= null)
				mSignInResult.setReport_data(null);
			selectDataindex +=1;
			refreshDateLin();
			}else{
				activity.startActivity(new Intent(activity, LoginActivity.class));
			}
			break;
		case R.id.btn_next_day:
			if(PreManager.instance().getIsLogin(activity)){//未登陆的时候提醒登陆
				if(selectDataindex>1){
					if(mSignInResult!= null)
						mSignInResult.setReport_data(null);
					selectDataindex-=1;
					refreshDateLin();
				}else{
					Toast.makeText(activity, "不能玩穿越", Toast.LENGTH_SHORT).show();
				}
			}else{
				activity.startActivity(new Intent(activity, LoginActivity.class));
			}
			break;
		case R.id.btn_commit_emotion:
			if(PreManager.instance().getIsLogin(activity)){//未登陆的时候提醒登陆
				intent = new Intent(activity, EmotionManagerActivity.class);
				intent.putExtra("thinking", edThink.getText().toString());
				intent.putExtra("feeling", edtFeel.getText().toString());
				edtFeel.setText("");
				edThink.setText("");
			}else{
				activity.startActivity(new Intent(activity, LoginActivity.class));
			}
			break;
		case R.id.tv_relax_self:
			if(PreManager.instance().getIsLogin(activity)){//未登陆的时候提醒登陆
				intent = new Intent(activity, RelaxActivity.class);
			}else{
				activity.startActivity(new Intent(activity, LoginActivity.class));
			}
			break;
		case R.id.lin_todays_sleep_tips:
			if(PreManager.instance().getIsLogin(activity)){//未登陆的时候提醒登陆
				intent = new Intent(activity, SetRemindActivity.class);
			}else{
				activity.startActivity(new Intent(activity, LoginActivity.class));
			}
			break;
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
					if(!isPunchShowing){
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
						isPunchShowing = true;
					}
			} catch (Exception e) {
				new PunchTipDialog(activity).show();
			}
		}else{
			activity.startActivity(new Intent(activity, LoginActivity.class));
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
//			initSleepData();
			refreshDateLin();
			getSleepDataAndSetSmartAlarm(1);
		}
		if(requestCode == 100){
			isPunchShowing = false;
		}
	}

	/**
	 * 点击顶部的环形view进入睡眠详情界面
	 */
	@Override
	public void onClickShowDaySleep() {// ~~~
		MobclickAgent.onEvent(activity, "641");
		Intent intent = null;
		if (PreManager.instance().getIsLogin(activity)) {// 已登录
			intent = new Intent(activity, SleepStatusDetailActivity.class);
			if (TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(activity))) 
				intent.putExtra("type", "1");
			else 
				intent.putExtra("type", "2");
			
			intent.putExtra("date", TimeFormatUtil.getOtherDay("yyyy-MM-dd", selectDataindex));
			
		} else {
			intent = new Intent(activity, LoginActivity.class);
			
		}
		if (intent != null) {
			activity.startActivity(intent);
		}
	}

	/**
	 * 下载完数据之后刷新
	 */
	public void refreshDownloadData() {
		try {
			refreshSleepDayDetailtips();
			initSleepData();
			refreshDaysPlan();
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
		if(!PreManager.instance().getIsCompleteSleepPg(activity)){
			dataCircle.setEvaluatedSleep(true);
		}else{
			dataCircle.setEvaluatedSleep(false);
		}
		// 记录硬件昨晚是否有数据,用来判断是否显示同步按钮
		boolean haveData = false;
		long sleepTime = 0l;
		long wakeTime = 0l;
		float per = 0f;
		// 根据是否绑定硬件设备，取不同的数据
		if (TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(
				activity))) {
			dataCircle.setIsBindPillow(false);
			if(mSignInResult!= null && 1 == (mSignInResult.getQiandao())){//已签到
				if(mSignInResult.getReport_data()!= null){
					try {
						sleepTime = Long.parseLong(mSignInResult.getReport_data().getSleep())*1000;
						wakeTime = Long.parseLong(mSignInResult.getReport_data().getWakeup())*1000;
						per = Float.parseFloat(mSignInResult.getReport_data().getXiaolv());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		} else {
			dataCircle.setIsBindPillow(true);
			/*String getDayTime = TimeFormatUtil.getOtherDay("yyyy-MM-dd", selectDataindex);
			PillowDataModel pModel = PillowDataOpera.queryDataFromSQL(
					activity.getApplicationContext(), getDayTime);
			if (pModel != null && pModel.getBfr() != null
					&& pModel.getBfr().length > 0) {
				haveData = true;
				SleepDataHead datahead = BluetoothDataFormatUtil.format3(
						pModel.getBfr(), 10);
				pModel.getBfr();
				
				sleepTime = datahead.InSleepTime;
				wakeTime = datahead.OutSleepTime;
				dataCircle.setDataIsSync(true);
			}*/
			if(mSignInResult!= null && 1 == (mSignInResult.getQiandao())){//已签到
				if(mSignInResult.getReport_data()!= null){
					try {
						sleepTime = Long.parseLong(mSignInResult.getReport_data().getSleep())*1000;
						wakeTime = Long.parseLong(mSignInResult.getReport_data().getWakeup())*1000;
						per = Float.parseFloat(mSignInResult.getReport_data().getXiaolv());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
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
		
		dataCircle.setData(sleepTime, wakeTime, "", per);
		dataCircle.setOnClickChangeTime(this);
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
			final OneKeyDialog mOneKeyDialog = new OneKeyDialog(activity);
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					initSleepData();
					mOneKeyDialog.show();
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
		} else
			dataCircle.setIsBindPillow(false);
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
			}else if(Constant.MODIFY_PLAN.equals(action)){
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
				signInPillowData(list, list.get(0));
			}else{
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
	private void signInPillowData(final List<SignInData> list, final SignInData signIn){
		new XiangchengMallProcClass(activity).signIn(PreManager.instance().getUserId(activity), "2", signIn, new InterfaceSignInCallBack() {

			@Override
			public void onSuccess(int icode, JSONObject response) {
				try {
					JSONObject json = new JSONObject(response.getString("report_data"));
					signIn.setSignInId(json.getString("qiandaoid"));
					signIn.setResult(response.toString());
					SignInDBOperation.initDB(activity).updateSignInData(signIn, "1");//0软件1硬件
				} catch (Exception e) {
				}finally{
					list.remove(0);
					if(list.size()>0){
						signInPillowData(list, list.get(0));
					}else{
						mHandler.sendEmptyMessage(2);
						cancelProNotCancle();
					}
				}
			}

			@Override
			public void onError(int icode, String strErrMsg) {
				list.remove(0);
				signIn.setUpload(1);
				SignInDBOperation.initDB(activity).updateSignInData(signIn, "1");

				if(list.size()>0){
					signInPillowData(list, list.get(0));
				}else{
					mHandler.sendEmptyMessage(2);
					cancelProNotCancle();
				}
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
		String startTime = "";
		String endTime = "";
		String suggestSleepTime = PreManager.instance().getSleepTime_Setting(
				activity);
		@SuppressWarnings("static-access")
		SharedPreferences sp = activity.getSharedPreferences(
				SleepInfo.SLEEP_SETTIME, activity.MODE_APPEND);
		SmartRemindBean mSmartRemindBean = null;
		String remind_time_style = sp.getString(SleepInfo.REMIND_BEFORE_SLEEP,
				"");
		if (remind_time_style.equals("")) {
			SignInData mSignInData = null;
			if(TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(
					activity))){//软件
				mSignInData= SignInDBOperation.initDB(activity).querySignInData(TimeFormatUtil.getYesterdayTime(), "0");
			}else{//硬件
				mSignInData= SignInDBOperation.initDB(activity).querySignInData(TimeFormatUtil.getYesterdayTime(), "1");
			}
			
			if(mSignInData != null){
				if(!TextUtils.isEmpty(mSignInData.getTrySleepTime()) && !TextUtils.isEmpty(mSignInData.getWakeUpTime())){
					try {
						startTime = TimeFormatUtil.formatTime(Long.parseLong(mSignInData.getTrySleepTime()), "HH:mm");
						endTime = TimeFormatUtil.formatTime(Long.parseLong(mSignInData.getWakeUpTime()), "HH:mm");
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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

			String datee = TimeFormatUtil.getTodayTime() + " "
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
					setRemindAlarm(mSmartRemindBean.SuggestRemindTime);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

	}

	// 设置智能提醒闹钟
	private void setRemindAlarm(String SuggestRemindTime) {
		String[] times = SuggestRemindTime.split(":");
		Intent intent = new Intent("com.yzm.sleep.alarm.MART_NOTIFY");
		PendingIntent sender = PendingIntent.getBroadcast(activity, 0, intent,
				0);

		// We want the alarm to go off 10 seconds from now.
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
		calendar.set(Calendar.MINUTE, Integer.parseInt(times[1]));
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

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
	 * 完成每日计划
	 * @param planid
	 */
	private void finishPlanWorks(final String planid){
		ComplatePlanParams mParams = new ComplatePlanParams();
		mParams.isfinish = "1";
		mParams.my_int_id= PreManager.instance().getUserId(activity);
		mParams.planid   = planid;
		new XiangchengMallProcClass(activity).complatePlan(mParams, new ComplatePlanCallBack() {
			
			@Override
			public void onSuccess(int icode, String content) {
				List<Plan> list = mPlanAdapter.getdata();
				for(int l = 0 ; l < list.size() ; l++){
					if(list.get(l).getPlanid().equals(planid)){
						list.get(l).setIsfinish("1");;
						//修改本地数据库中的睡眠感觉
						SignInData signData = new SignInData();
						String StrType = "0";
						if(TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(
								activity))){//软件
							StrType = "0";
						}else{//硬件
							StrType = "1";
						}
						
						SignInData getsignData = SignInDBOperation.initDB(activity).querySignInData(TimeFormatUtil.getOtherDay("yyyy-MM-dd", selectDataindex), StrType);
						for(int ms = 0 ; ms < mSignInResult.getPlan_list().size() ; ms++){
							if(mSignInResult.getPlan_list().get(ms).getPlanid().equals(planid)){
								mSignInResult.getPlan_list().get(ms).setIsfinish("1");
								break;
							}
						}
						signData = getsignData;
						signData.setResult(mSignInResult.toString());
						SignInDBOperation.initDB(activity).updateSignInData(signData, StrType);
						break;
					}
				}
				mPlanAdapter.setData(list);
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				ToastManager.getInstance(activity).show(strErrMsg);
				
			}
		});
	}
	
	/**
	 * 网络请求指定日期的数据
	 * @param data 日期时间 yyyyMMdd
	 * @param getFromDBifFail 是否在失败之后使用本地 
	 */
	private void getSleepInfoFromSer(final String serdata, final String dbdata, final boolean getFromDBifFail){
		if(Util.checkNetWork(activity)){
			showPro();
			GetHomePageDataParams mParams = new GetHomePageDataParams();
			mParams.date = serdata;
			mParams.my_int_id = PreManager.instance().getUserId(activity);
			mParams.type = TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(activity))?"1":"2";
			new XiangchengMallProcClass(activity).getHomePage(mParams, new GetHomePageDataCallBack() {

				@Override
				public void onSuccess(int icode, String pinggu, String sginIn,
						JSONObject response) {
					cancelPro();
					LogUtil.d("chen2", response.toString());
					mSignInResult = Util.getSleepInfoFromJson(response.toString());
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
							signdata.setSignInId(mSignInResult.getReport_data().getQiandaoid());
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
				public void onError(int icode, String strErrMsg) {
					cancelPro();
					if(icode == 4864){//日期错误  yyyyMMdd
						//根据获取的日期在去请求数据。
						String serDataStr = Util.getTimeFromString(strErrMsg);
						serDataStr = TimeFormatUtil.getYesterDay(serDataStr, "yyyy-MM-dd");
						if(!TextUtils.isEmpty(serDataStr)){
							getSleepInfoFromSer(serDataStr.replace("-", ""), serDataStr, getFromDBifFail);
						}
					}else{
						if(getFromDBifFail){//如果方案为请求失败则使用本地
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
	 * 设置当前的对睡眠的感觉
	 */
	private void setFeelingsChange(String feeling, String qiandaoid){
		EvaluateSignInParams mParams = new EvaluateSignInParams();
		mParams.feeling = feeling;
		mParams.qiandaoid = qiandaoid;
		
		new XiangchengMallProcClass(activity).evaluateSignIn(mParams, new EvaluateSignInCallBack() {
			
			@Override
			public void onSuccess(int icode, String strSuccMsg) {
				LogUtil.d("chen", strSuccMsg);
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				LogUtil.d("chen", strErrMsg);
			}
		});
		String StrType = "0";
		if(TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(
				activity))){//软件
			StrType = "0";
		}else{//硬件
			StrType = "1";
		}
		//修改本地数据库中的睡眠感觉
		SignInData signData = new SignInData();
		SignInData getsignData = SignInDBOperation.initDB(activity).querySignInData(TimeFormatUtil.getOtherDay("yyyy-MM-dd", selectDataindex), StrType);
		mSignInResult.getReport_data().setFeeling(feeling);
		signData = getsignData;
		signData.setResult(mSignInResult.toString());
		SignInDBOperation.initDB(activity).updateSignInData(signData, StrType);
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
	private SignInResult getSleepDataInfoFromDB(String date){
		SignInResult mtempInfo = null;
		SignInData signData = SignInDBOperation.initDB(activity).querySignInData(date, TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(
				activity))?"0":"1");
		if(signData!=null && !TextUtils.isEmpty(signData.getResult())){
			mtempInfo = Util.getSleepInfoFromJson(signData.getResult());
		}else{
			//没有数据
			mtempInfo = null;
		}
		return mtempInfo;
	}
	
	
	/**
	 * 刷新昨日睡眠信息
	 */
	private void refreshSleepDayDetailtips(){
			if(mSignInResult != null && 1 == mSignInResult.getPinggu() && 1==mSignInResult.getQiandao() && PreManager.instance().getIsLogin(activity)){
				if(mSignInResult.getReport_data() != null){
					todaySleepTimeLin.setVisibility(View.VISIBLE);
					afterPunchRela.setVisibility(View.VISIBLE);
					long sleeptime = 0l;
					try {
						sleeptime = Long.parseLong(mSignInResult.getReport_data().getSleeplong());
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
					
					if("-1".equals(mSignInResult.getReport_data().getFeeling())){
						sleepFeelImg.setImageResource(R.drawable.ic_cry);
					}else if("1".equals(mSignInResult.getReport_data().getFeeling())){
						sleepFeelImg.setImageResource(R.drawable.ic_smile);
					}else{
						sleepFeelImg.setImageResource(R.drawable.ic_smile_normal);
					}
					if(mSignInResult.getReport_data().getDays() > 0 && mSignInResult.getReport_data().getDays() < 8){
						punchDaysTips.setVisibility(View.VISIBLE);
						punchDaysTips.setText(mSignInResult.getReport_data().getDays()+"天后出周报");
					}else{
						punchDaysTips.setVisibility(View.GONE);
					}
					sleepTimetips.setText("昨晚睡眠时长："+sleeptime/60+"小时"+sleeptime%60+"分钟");
					yesterdayContent.setText(mSignInResult.getReport_data().getReport());
				}
			}else{
				todaySleepTimeLin.setVisibility(View.GONE);
				afterPunchRela.setVisibility(View.GONE);
			}
			if(mSignInResult != null && 1 == mSignInResult.getPinggu() && PreManager.instance().getIsLogin(activity)){
				if(mSignInResult.getReport_data().getReport_ok() == 1){
					dialogForWeekData();
				}
			}
	}
	/**
	 * 刷新每日计划
	 */
	private void refreshDaysPlan(){
		welconLin.setVisibility(View.GONE);
		
		if(mSignInResult != null){
			List<Plan> planlistreal = mSignInResult.getPlan_list();
			mPlanAdapter.setData(planlistreal);
			//设置每日计划是否显示
			if(planlistreal != null && planlistreal.size()!=0){
				daysplanTips.setVisibility(View.VISIBLE);
				daysPlanLin.setVisibility(View.VISIBLE);
				planTips.setVisibility(View.GONE);
			}else{
				daysplanTips.setVisibility(View.GONE);
				planTips.setVisibility(View.VISIBLE);
			}

			daysPlanListv.post(new Runnable() {

				@Override
				public void run() {
					mScrollView.scrollTo(10, 10);  
				}
			});
			
			if("1".equals(mSignInResult.getEmotion())){
				eqMnagerLin.setVisibility(View.VISIBLE);
			}else{
				eqMnagerLin.setVisibility(View.GONE);
			}
			if("1".equals(mSignInResult.getXinqing())){
				relaxLin.setVisibility(View.VISIBLE);
			}else{
				relaxLin.setVisibility(View.GONE);
			}
			
			if(1 ==mSignInResult.getPinggu()){
				pingguTips.setVisibility(View.GONE);
			}else{
				pingguTips.setVisibility(View.GONE);
				planTips.setVisibility(View.GONE);
				eqMnagerLin.setVisibility(View.GONE);
				relaxLin.setVisibility(View.GONE);
				welconLin.setVisibility(View.VISIBLE);
			}
			
		}else{
			daysPlanLin.setVisibility(View.GONE);
		}
		
		if (!PreManager.instance().getIsLogin(activity)) {
			eqMnagerLin.setVisibility(View.GONE);
			relaxLin.setVisibility(View.GONE);
			daysPlanLin.setVisibility(View.GONE);
			welconLin.setVisibility(View.VISIBLE);
			pingguTips.setVisibility(View.GONE);
			planTips.setVisibility(View.GONE);
			welcomeTime.setText(TimeFormatUtil.getOtherDay("MM月dd日", 0));
		}
		
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
	 * 每日计划点击1.点击计划本身
	 */
	@Override
	public void DayPlanClickDetail(Plan plan) {
		//点击计划本身 跳转
		switch (plan.getUrltype()) {
		case 1://1活动详情 ;   
			Intent pIntent = new Intent(activity, ProgramDetailsActivity.class);
			pIntent.putExtra("id", plan.getUrlid());
			startActivity(pIntent);
			break;
		case 2://2话题详情  ;
			Intent intentToTopic = new Intent(activity, CommunityTopiceDetailActivity.class);
			 intentToTopic.putExtra("topices_id", plan.getUrlid());
			 intentToTopic.putExtra("is_choiceness", false);
			 startActivity(intentToTopic);
			break;
		case 3://3文章详情
			Intent cintent = new Intent(activity, CommunityTopiceDetailActivity.class);
			cintent.putExtra("topices_id", plan.getUrlid());
			cintent.putExtra("is_choiceness", true);
			 startActivity(cintent);
			break;
		
		default:
			break;
		}
	}

	/**
	 * 每日计划点击1.点击完成
	 */
	@Override
	public void DayPlanClickFinish(Plan plan) {
		finishPlanWorks(plan.getPlanid());
	}
	
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
					feedBackDialog.dismiss();
				}
			});
		}
	}
	
	
}
