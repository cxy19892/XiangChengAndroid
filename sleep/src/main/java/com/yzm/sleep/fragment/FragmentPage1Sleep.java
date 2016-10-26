package com.yzm.sleep.fragment;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import android.graphics.drawable.Drawable;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.umeng.analytics.MobclickAgent;
import com.xpillowjni.XpillowInterface;
import com.yzm.sleep.Constant;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.activity.DataCountActivity;
import com.yzm.sleep.activity.EstimateWebActivity;
import com.yzm.sleep.activity.LoginActivity;
import com.yzm.sleep.activity.MessageListActivity;
import com.yzm.sleep.activity.ShareActivity;
import com.yzm.sleep.activity.YestereveDataActivity;
import com.yzm.sleep.activity.alar.AlarmActivity;
import com.yzm.sleep.activity.alar.OneShotReminsAlarmRecever;
import com.yzm.sleep.activity.alar.RemindAlarmEditActivity;
import com.yzm.sleep.activity.community.TeamDetailsActivity;
import com.yzm.sleep.activity.pillow.PillDataCountActivity;
import com.yzm.sleep.activity.pillow.PillowDataOpera;
import com.yzm.sleep.activity.pillow.PillowDayDataActivity;
import com.yzm.sleep.activity.pillow.PillowUpgradeActivity;
import com.yzm.sleep.adapter.HotGroupAdapter;
import com.yzm.sleep.background.AlarmDBOperate;
import com.yzm.sleep.background.AlarmService.ListAlarmTime;
import com.yzm.sleep.background.DataUtils;
import com.yzm.sleep.background.JudgFuction;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.background.SignInDBOperation;
import com.yzm.sleep.background.SleepInfo;
import com.yzm.sleep.bean.CommunityGroupBean;
import com.yzm.sleep.bean.FirstRecommentGroupBean;
import com.yzm.sleep.bluetoothBLE.CopyOfPillowHelper;
import com.yzm.sleep.bluetoothBLE.PillowCallback;
import com.yzm.sleep.model.DaySleepMsg;
import com.yzm.sleep.model.MyDialog;
import com.yzm.sleep.model.PillowDataModel;
import com.yzm.sleep.model.SignInData;
import com.yzm.sleep.model.SmartRemindBean;
import com.yzm.sleep.render.GetModelsValueClass.ModelsValues;
import com.yzm.sleep.render.GetSleepResultValueClass.SleepDataHead;
import com.yzm.sleep.utils.AlarmConstants;
import com.yzm.sleep.utils.AlarmUtils;
import com.yzm.sleep.utils.BluetoothDataFormatUtil;
import com.yzm.sleep.utils.BluetoothUtil;
import com.yzm.sleep.utils.BottomPopDialog;
import com.yzm.sleep.utils.CalenderUtil;
import com.yzm.sleep.utils.CommunityProcClass;
import com.yzm.sleep.utils.DateOperaUtil;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceFirstPageRecommentGroupCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadHardwareAllDayCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadHardwareDataCallBack1;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadHardwareAllDayParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadHardwareDataParamClass1;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.PunchDialog;
import com.yzm.sleep.utils.PunchTipDialog;
import com.yzm.sleep.utils.SleepConstants;
import com.yzm.sleep.utils.SleepDataProClass;
import com.yzm.sleep.utils.SleepUtils;
import com.yzm.sleep.utils.SmartNotificationUtil;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.ToastManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.widget.CustomDialog;
import com.yzm.sleep.widget.DayDataCircle;
import com.yzm.sleep.widget.DayDataCircle.OnClickChangeTime;
import com.yzm.sleep.widget.OneKeyDialog;
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
public class FragmentPage1Sleep extends Fragment implements OnClickListener,
		OnClickChangeTime, MySyncOnClickListener {
	private TextView tvSeepDetail;
	private DayDataCircle dataCircle;
	private Activity activity;
	private SoftDayData dayData;
	// private String date;
	// addchen
	@SuppressWarnings("unused")
	private BluetoothAdapter mBluetoothAdapter;
	private Handler mHandler = null;
	private CopyOfPillowHelper pillowserver = null;
	private boolean isgotdata = false;
	private boolean isneedUpLoad = false;
	private boolean isConnect = false;
	private boolean IS_NEED_TO_OPEN_BLUETOOTH = false;
	private CustomDialog syncFaileDialog = null;
	private LinearLayout llHotGroup;
	private ImageView hasNewMsg;
	private TextView tvWeekContent;
	private boolean isPunchShowing = false;
	private List<FirstRecommentGroupBean> groupHot;
	private ArrayList<ListAlarmTime> alarmData;
	private HotGroupAdapter groupAdapter;
	private GridView groupGrid;
	// private List<SoftDayData> list;
	private Button btnClockSleep, btnClockGetup, btnClockAdd;
	private WaveView waveView;
	private IWeiboShareAPI mWeiboShareAPI;
	/**纠正睡眠数据的按钮*/
	private Button debugBtn;
	private BottomPopDialog popDialog;

	/**
	 * 睡眠详情跳转类型 0-首次使用无数据 1-没有睡眠数据 2-有数据
	 * 
	 * */
	private int targetIntentType = -1;

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
		return inflater.inflate(R.layout.fragment_sleep, container, false);
	}

	@SuppressLint("NewApi")
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView(view);

		dataCircle = (DayDataCircle) view.findViewById(R.id.soft_day_data_view);
		dataCircle.setOnClickListener(this);
		tvSeepDetail = (TextView) view.findViewById(R.id.tvSeepDetail);
		hasNewMsg = (ImageView) view.findViewById(R.id.has_new_msg);
		hasNewMsg.setOnClickListener(this);
		waveView = (WaveView) view.findViewById(R.id.wave);
		waveView.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				waveView.updateProgress(0.8f);
			}
		}, 1000);
		activity.registerReceiver(bluetoothState, new IntentFilter(
				BluetoothAdapter.ACTION_STATE_CHANGED));
		initSleepData();
		initGroupData();
		initAlarmData();
	}

	public void setHasNewMsg(int visibility) {
		if (hasNewMsg != null)
			hasNewMsg.setVisibility(visibility);
	}

	private void initView(View view) {
		ImageButton ibShare = (ImageButton) view.findViewById(R.id.ib_left);
		ibShare.setOnClickListener(this);

		ImageButton ibMessage = (ImageButton) view.findViewById(R.id.ib_right);
		ibMessage.setOnClickListener(this);
		llHotGroup = (LinearLayout) view.findViewById(R.id.llHotGroup);
		view.findViewById(R.id.llWeekNews).setOnClickListener(this);
		view.findViewById(R.id.llAlarm).setOnClickListener(this);
		tvWeekContent = (TextView) view.findViewById(R.id.tvWeekContent);

		groupGrid = (GridView) view.findViewById(R.id.gridViewGroup);
		btnClockSleep = (Button) view.findViewById(R.id.btnClockSleep);
		btnClockGetup = (Button) view.findViewById(R.id.btnClockGetup);
		btnClockAdd = (Button) view.findViewById(R.id.btnClockAdd);
		
		debugBtn = (Button) view.findViewById(R.id.btn_4_debug_user);
		debugBtn.setOnClickListener(this);
		btnClockSleep.setOnClickListener(this);
		btnClockGetup.setOnClickListener(this);
		btnClockAdd.setOnClickListener(this);

		groupGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MobclickAgent.onEvent(activity, "643");
				if (PreManager.instance().getIsLogin(activity)) {
					Intent intent = new Intent(activity,
							TeamDetailsActivity.class);
					CommunityGroupBean bean = new CommunityGroupBean();
					bean.setGid(groupHot.get(position).getGid());
					bean.setG_name(groupHot.get(position).getG_name());
					bean.setG_ico(groupHot.get(position).getG_ico());
					bean.setG_ico_key(groupHot.get(position).getG_ico_key());
					intent.putExtra("bean", bean);
					startActivity(intent);
				} else {
					startActivity(new Intent(activity, LoginActivity.class));
				}
			}
		});
	}

	/**
	 * 刷新周报提示
	 */
	private void refreshSleepWeekDetail() {
		// 睡眠周报详情信息
		List<SoftDayData> weekData = null;
		String[] avgSleepTimeAndGetupTime = null;
		if (TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(
				activity))) {// 取软件睡眠数据
			weekData = SleepUtils.getWeekSoftSleepData(activity);
			avgSleepTimeAndGetupTime = SleepUtils
					.getAvgSleepTimeAndGetupTime(weekData);
		} else {// 取硬件睡眠数据
			weekData = SleepUtils.getWeekPillowSleepData(activity);
			String[] days;
			List<String> lastSevenDay = CalenderUtil.getLastSevenDay(7);
			days = new String[lastSevenDay.size()];
			int i = 0;
			for (String string : lastSevenDay) {
				days[i] = string;
				i++;
			}
			avgSleepTimeAndGetupTime = SleepUtils
					.getPillAvgSleepTimeAndGetupTime(activity, days);
		}
		if (weekData == null || avgSleepTimeAndGetupTime == null) {
			return;
		}
		String totalSleepLengthString = SleepUtils.getTotalSleepLengthString(
				activity, weekData);
		String averageSleepLengthString = SleepUtils
				.getAverageSleepLengthString(activity, weekData);
		if (TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(
				activity))) {
			tvWeekContent.setText("最近7天睡眠总时长"
					+ totalSleepLengthString.split(":")[0] + "小时"
					+ totalSleepLengthString.split(":")[1] + "分" + "，平均每天"
					+ averageSleepLengthString.split(":")[0] + "小时"
					+ averageSleepLengthString.split(":")[1] + "分"
					+ "的睡眠，平均入睡时间"
					+ avgSleepTimeAndGetupTime[0].replaceAll("-", "0")
					+ "，平均醒来时间"
					+ avgSleepTimeAndGetupTime[1].replaceAll("-", "0"));
		} else {
			ArrayList<String> byDateSleepDate = SleepUtils
					.getByDateSleepDate(activity);
			tvWeekContent.setText("最近7天睡眠总时长" + byDateSleepDate.get(3)
					+ "，平均每天" + byDateSleepDate.get(0) + "的睡眠，平均入睡时间"
					+ byDateSleepDate.get(1) + "，平均醒来时间"
					+ byDateSleepDate.get(2));
		}
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
			mWeiboShareAPI.registerApp();
			MobclickAgent.onEvent(activity, "640");
			if (PreManager.instance().getIsLogin(activity)) {
				if (dayData != null
						&& !TextUtils.isEmpty(dayData.getSleepTime())
						&& !TextUtils.isEmpty(dayData.getGetUpTime())) {
					intent = new Intent(activity, ShareActivity.class);
					intent.putExtra("sleep_data", dayData);
					intent.putExtra("from", Constant.SHARE_FROM_SLEEP_DATA);
				} else {
					Util.show(activity, "暂无数据分享");
				}
			} else {
				intent = new Intent(activity, LoginActivity.class);
			}

			break;
		case R.id.ib_right:// 消息
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
		case R.id.llAlarm:// 闹钟
		case R.id.btnClockSleep:
		case R.id.btnClockGetup:
			intent = new Intent(activity, AlarmActivity.class);
			break;
		case R.id.btnClockAdd:
			AlarmUtils.saveDefaultCurrentAudio(getActivity());
			Intent intentRemind = new Intent(getActivity(),
					RemindAlarmEditActivity.class);
			intentRemind.putExtra(AlarmConstants.IS_EDIT, false);
			intentRemind.putExtra("from", "alarm_list");
			PreManager.instance()
					.saveIsFirstChooseRingtone(getActivity(), true);
			MyApplication.instance().setAddStart(true);
			PreManager.instance().saveIsEditAlarm(getActivity(), false);
			startActivity(intentRemind);
			getActivity().overridePendingTransition(R.anim.dialog_bottom_in,
					R.anim.alpha_out);
			break;
		case R.id.btn_4_debug_user:
			if(PreManager.instance().getIsLogin(activity)){//未登陆的时候提醒登陆
				if(TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(//无硬件的时候提示无硬件
						activity))){
					Toast.makeText(activity, "请先绑定硬件", Toast.LENGTH_SHORT).show();
				}else{
					String syncHardwareSleepDate = PreManager.instance()
					.getSyncHardwareSleepDate(activity);
					if (!TextUtils.isEmpty(syncHardwareSleepDate)
							&& syncHardwareSleepDate.equals(PreManager
									.instance().getUserId(activity)
									+ new SimpleDateFormat("yyyy-MM-dd")
							.format(new Date()))){//无数据的时候提示无数据
//						showDebugPopdialog();
					}else{
						Toast.makeText(activity, "暂无今日数据", Toast.LENGTH_SHORT).show();
					}
				}
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

	@Override
	public void onClickPunch() {
		// TODO Auto-generated method stub
		
	
		
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
		
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			try {
				long t = sdf.parse(sdf.format(System.currentTimeMillis()))
						.getTime();
				long t1 = sdf.parse("04:00").getTime();
				long t2 = sdf.parse("11:00").getTime();
				if (t >= t1 && t <= t2) {
					if(!isPunchShowing){
						startActivityForResult(new Intent(activity, PunchDialog.class),
								100);
						isPunchShowing = true;
					}
				} else {
					new PunchTipDialog(activity).show();
				}
			} catch (Exception e) {
				new PunchTipDialog(activity).show();
			}
		}else{
			activity.startActivity(new Intent(activity, LoginActivity.class));
		}
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100 && resultCode == 101) { // 打卡成功之后返回刷新数据
			initSleepData();
		}
		if(requestCode == 100){
			isPunchShowing = false;
		}
	}

	@Override
	public void onClickShowDaySleep() {// ~~~
		MobclickAgent.onEvent(activity, "641");
		Intent intent = null;
		if (PreManager.instance().getIsLogin(activity)) {// 已登录
			if (TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(// 未绑定枕头
					activity))) {
				intent = new Intent(activity, YestereveDataActivity.class);
				intent.putExtra("dayData", dayData);
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
		dayData = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		String date = sdf.format(calendar.getTime());

		
		if(!PreManager.instance().getIsCompleteSleepPg(activity)){
			dataCircle.setEvaluatedSleep(true);
		}else{
			dataCircle.setEvaluatedSleep(false);
		}
		
		// 记录硬件昨晚是否有数据,用来判断是否显示同步按钮
		boolean haveData = false;
		// 根据是否绑定硬件设备，取不同的数据
		if (TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(
				activity))) {
			//debugBtn.setVisibility(View.GONE);//无硬件的时候不显示上报硬件数据的按钮
			try {
				dayData = SleepUtils.getDaySleepData(activity, date, "1");
			} catch (Exception e) {
			}
			
			if (dayData.getDate() == null) {
				calendar.setTime(new Date());
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				dayData.setDate(date);
			}
			dataCircle.setIsBindPillow(false);
			if (dayData.getSleepTime() == null
					&& dayData.getGetUpTime() == null) {
				dataCircle.setDataIsSync(false);
			} else {
				dataCircle.setDataIsSync(true);
			}
		} else {
			//debugBtn.setVisibility(View.VISIBLE);//有硬件的时候显示上报硬件数据的按钮
			dataCircle.setIsBindPillow(true);
			PillowDataModel pModel = PillowDataOpera.queryDataFromSQL(
					activity.getApplicationContext(), date);
			if (pModel != null && pModel.getBfr() != null
					&& pModel.getBfr().length > 0) {
				haveData = true;
				SleepDataHead datahead = BluetoothDataFormatUtil.format3(
						pModel.getBfr(), 10);
				pModel.getBfr();
				dayData = new SoftDayData();
				dayData.setDate(date);
				dayData.setGetUpTime(TimeFormatUtil.formatTime1(
						datahead.OutSleepTime, "HH:mm"));
				dayData.setSleepTime(TimeFormatUtil.formatTime1(
						datahead.InSleepTime, "HH:mm"));
				dayData.setTotalSleepTime(datahead.TotalSleepTime);
			} else {
				dayData = new SoftDayData();
			}
		}

		String alarmSleepTime = PreManager.instance().getSleepTime_Setting(
				activity);
		String alarmGetUpTime = PreManager.instance().getGetupTime_Setting(
				activity);

		// 数据库有当天睡眠计划的取睡眠计划
		try {
			String startTime = dayData.getStartTime();
			String endTime = dayData.getEndTime();
			if (startTime != null)
				alarmSleepTime = TimeFormatUtil.minToTime(Integer
						.parseInt(startTime));
			if (endTime != null)
				alarmGetUpTime = TimeFormatUtil.minToTime(Integer
						.parseInt(endTime));
		} catch (Exception e) {
		}

		checkIsShowBtn(haveData);
		dataCircle.setData(alarmSleepTime, alarmGetUpTime, dayData);
		dataCircle.setOnClickChangeTime(this);

		if (dayData == null || dayData.getSleepTime() == null
				|| dayData.getGetUpTime() == null
				|| TextUtils.isEmpty(dayData.getSleepTime())) {
			if (TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(
					activity))) {
				refreshSleepDetail(0);// 无数据
			} else {
				String syncHardwareSleepDate = PreManager.instance()
						.getSyncHardwareSleepDate(activity);
				if (!TextUtils.isEmpty(syncHardwareSleepDate)
						&& syncHardwareSleepDate.equals(PreManager.instance()
								.getUserId(activity)
								+ new SimpleDateFormat("yyyy-MM-dd")
										.format(new Date()))) {
					refreshSleepDetail(5);
					dataCircle.setDataIsSync(true);
				} else {
					refreshSleepDetail(4);
				}
			}
			targetIntentType = 1;
		} else {// 有数据
			dataCircle.setDataIsSync(true);
			targetIntentType = 2;
			
			/*计算昨晚睡眠时长*/
			float countLengthByTime = 0;
			try {
				if (TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(// 未绑定枕头
						activity))) {
					countLengthByTime = SleepUtils.countLengthByTime(
							dayData.getSleepTime(), dayData.getGetUpTime());
				} else {
					countLengthByTime = dayData.getTotalSleepTime() / 60f;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			int sleepLengthState = SleepUtils.getSleepLengthState(activity,
					countLengthByTime);
			switch (sleepLengthState) {
			case -1:
				refreshSleepDetail(1);// 偏短
				break;
			case 0:
				refreshSleepDetail(2);// 健康
				break;
			case 1:
				refreshSleepDetail(3);// 偏长
				break;

			default:
				break;
			}
		}
		refreshSleepWeekDetail();
	}


	/**
	 * 刷新睡眠详情提示文字
	 * 
	 * @param sleepType
	 *            0-欢迎使用香橙睡眠（无睡眠数据） 1-偏短 2-健康 3-偏长 4-未同步枕扣设备 5-枕扣设备中无昨晚数据
	 *            6-欢迎使用香橙睡眠 7-小米手机无数据，去设置自启动
	 */
	public void refreshSleepDetail(final int sleepType) {
		Drawable drawableYes = activity.getResources().getDrawable(
				R.drawable.home_sleep_data_yes);
		Drawable drawableNo = activity.getResources().getDrawable(
				R.drawable.home_sleep_data_no);
		drawableYes.setBounds(0, 0, drawableYes.getMinimumWidth(),
				drawableYes.getMinimumHeight());
		drawableNo.setBounds(0, 0, drawableNo.getMinimumWidth(),
				drawableNo.getMinimumHeight());

		String[] detailStringArray = { 
				"欢迎使用香橙睡眠！",
				"昨晚睡眠不足，请合理安排睡眠",
				"昨晚睡眠充足，请继续保持", 
				"昨晚睡眠过多，请合理安排睡眠", 
				"你还没有同步枕扣设备数据", 
				"昨晚枕扣未检测到数据", };
		tvSeepDetail.setText(detailStringArray[sleepType]);

		if (sleepType == 1 || sleepType == 3) {
			tvSeepDetail.setCompoundDrawables(drawableNo, null, null, null);
		} else {
			tvSeepDetail.setCompoundDrawables(drawableYes, null, null, null);
		}
	}

	private SyncAlertDialog syncDialog;

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

	@SuppressLint("NewApi")
	private void connectDevice(final String mac) {
		// 开始链接设备并同步数据 in_iSelect 1代表同步数据， 2代表绑定设备， 3代表空中升级
		// add chen
		final BluetoothManager bluetoothManager = (BluetoothManager) activity
				.getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		mHandler = new Handler();
		String sensitive = "6,2,2";
		sensitive = Util.getSensitive(
				PreManager.instance().getAllSensitives(activity), Integer
						.parseInt(PreManager.instance()
								.getBluetoothDevSensitive(activity)));

		pillowserver = CopyOfPillowHelper.Getinstance(activity, callback,
				sensitive);
		// pillowserver.setPillowCallback(callback);
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				pillowserver.startConnectDev(1, mac);
			}
		}, 100);

	}

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
		if (pillowserver != null)
			pillowserver.Stop_server(activity);

		super.onDestroy();
	}

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
		String datPath = PillowDataOpera.saveDataToSDcard(buffer,
				sdf.format(new Date()) + ".dat");
		handleData(false, Modellist, datPath);

	}

	/**
	 * 处理硬件数据
	 * 
	 * @param batIsUpload
	 * @param Modellist
	 * @param datPath
	 */
	private void handleData(boolean batIsUpload, List<ModelsValues> Modellist,
			String datPath) {
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
				model.setDatFileName(sqlDate + ".dat");
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
		}
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				initSleepData();
				getSleepDataAndSetSmartAlarm(1);
			}
		});
		uploadDatFile(datPath, list);
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
					// TODO Auto-generated method stub
					waveView.updateProgress(0.8f);
				}
			}, 1000);
		}

		if (dayData != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			String creenDate = sdf.format(calendar.getTime());
			if (!creenDate.equals(dayData.getDate())) {
				initSleepData();
			}

			boolean haveData = !TextUtils.isEmpty(dayData.getSleepTime());
			checkIsShowBtn(haveData);
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
	 * 注册接受绑定硬件和解绑硬件的广播
	 */
	private void registerReceiver() {
		IntentFilter inFilter = new IntentFilter();
		inFilter.addAction(Constant.BUND_BLUETOOTH_CHANGE);
		inFilter.addAction(Constant.RECEIVER_ALARM_SAVE_SUCCESS);
		inFilter.addAction(Constant.RECEIVER_ALARM_UPDATE_SUCCESS);
		inFilter.addAction(Constant.RECEVER_EXIT);
		inFilter.addAction(Constant.RECEVER_USER_BIRTHDAY_UPDATE);
		activity.registerReceiver(mReceiver, inFilter);
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent data) {
			String action = data.getAction();
			if (Constant.BUND_BLUETOOTH_CHANGE.equals(action)) {
				initSleepData();
			} else if (Constant.RECEIVER_ALARM_SAVE_SUCCESS.equals(action)
					|| Constant.RECEIVER_ALARM_UPDATE_SUCCESS.equals(action)) {
				initAlarmData();
			} else if (Constant.RECEVER_EXIT.equals(action)) {
				initSleepData();
			} else if (Constant.RECEVER_USER_BIRTHDAY_UPDATE.equals(action)) {
				initSleepData();
			}
		}
	};

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
				// startActivity(new
				// Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
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

	// 设置提醒闹钟
	private void setRemindAlarm(String SuggestRemindTime) {
		String[] times = SuggestRemindTime.split(":");
		Intent intent = new Intent(activity, OneShotReminsAlarmRecever.class);
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

	private void initGroupData() {
		groupHot = new ArrayList<FirstRecommentGroupBean>();
		groupAdapter = new HotGroupAdapter(activity);
		groupGrid.setAdapter(groupAdapter);
		getHotGroup();
	}

	private void getHotGroup() {
		new CommunityProcClass(activity).getFirstPageRecommentGroups(PreManager
				.instance().getUserId(activity),
				new InterfaceFirstPageRecommentGroupCallBack() {

					@Override
					public void onSuccess(int icode,
							List<FirstRecommentGroupBean> mList) {
						groupHot = mList;
						groupAdapter.setData(mList);
						if (groupHot.size() > 0) {
							llHotGroup.setVisibility(View.VISIBLE);
						} else {
							llHotGroup.setVisibility(View.GONE);
						}
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						llHotGroup.setVisibility(View.GONE);
					}
				});
	}

	private void initAlarmData() {
		if (PreManager.instance().getIsFirstUseDefaultAudio(activity)) {
			AlarmUtils.saveDefaultAudioAndAlarm(activity);// 保存默认闹钟
			PreManager.instance().saveIsFirstUseDefaultAudio(activity, false);
		}
		if (alarmData == null) {
			alarmData = new ArrayList<ListAlarmTime>();
		}else{
			alarmData.clear();
		}

		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(activity
				.getApplicationContext());
		SQLiteDatabase db = helper.getWritableDatabase();
		AlarmDBOperate alarmDBOperate = new AlarmDBOperate(db,
				MyTabList.ALARM_TIME);
		// alarmData
		ArrayList<ListAlarmTime> notTimeoutAlarms = (ArrayList<ListAlarmTime>) alarmDBOperate
				.GetALarmDataAftertimeASC(TimeFormatUtil.getNowTimeAfter1Min());
		for (int af = 0; af < notTimeoutAlarms.size() && af < 2; af++) {// ListAlarmTime
																		// mListAlarmTime
																		// :
																		// notTimeoutAlarms
			alarmData.add(notTimeoutAlarms.get(af));
		}
		if (alarmData.size() < 2) {
			ArrayList<ListAlarmTime> allAlarms = (ArrayList<ListAlarmTime>) alarmDBOperate
					.GetALarmDataASC();
			for (int al = 0; al < allAlarms.size() && al < 2; al++) {// ListAlarmTime
																		// mAlarm
																		// :
																		// allAlarms
				alarmData.add(allAlarms.get(al));
			}
		}
		if (alarmData != null && alarmData.size() >= 2) {
			btnClockSleep.setText(alarmData.get(0).AlarmTime);
			btnClockGetup.setText(alarmData.get(1).AlarmTime);
		}
	}

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
	
}
