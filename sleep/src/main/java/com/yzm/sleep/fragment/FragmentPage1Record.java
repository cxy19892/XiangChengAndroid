package com.yzm.sleep.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.CircleImageView;
import com.yzm.sleep.Constant;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.DataResultFeedbackActivity;
import com.yzm.sleep.activity.FeedBackActivity;
import com.yzm.sleep.activity.ImprSlpPlanActivity;
import com.yzm.sleep.activity.LoginActivity;
import com.yzm.sleep.activity.PerceiveResetActivity;
import com.yzm.sleep.activity.PersonalActivity;
import com.yzm.sleep.activity.PlayMusicActivity;
import com.yzm.sleep.activity.RecordFeelDataActivity;
import com.yzm.sleep.activity.RecordNoteActivity;
import com.yzm.sleep.activity.RecordSleepDataActivity;
import com.yzm.sleep.activity.SleepDataReportActivity;
import com.yzm.sleep.activity.SleepTrendActivity;
import com.yzm.sleep.activity.SlpPlanActivity;
import com.yzm.sleep.background.SignInDBOperation;
import com.yzm.sleep.background.SleepInfo;
import com.yzm.sleep.bean.HomeDataBean;
import com.yzm.sleep.bean.PlanListBean;
import com.yzm.sleep.bean.RemindBean;
import com.yzm.sleep.bean.ShopCommodityBean;
import com.yzm.sleep.bean.UserBean;
import com.yzm.sleep.model.SignInData;
import com.yzm.sleep.model.SmartRemindBean;
import com.yzm.sleep.utils.BottomPopDialog;
import com.yzm.sleep.utils.ImprSucDialog;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceHomeDataCallBack;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.QiNiuUploadTool;
import com.yzm.sleep.utils.SmartNotificationUtil;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.McolumnChart1;

public class FragmentPage1Record  extends Fragment implements OnClickListener{

	private Button btnStartSetPlan, btnStartWeekReport;//, headRightBtn;
	private CircleImageView userIcon;
	private TextView userName, stateMsg, joinNum;//, stateName;
	private LinearLayout linUserIcon, linTimeState, RelSleepInfo;
	private RelativeLayout RelSleepPlan, RelSleepState, RelSleepWeek, RelHead, relTimebg;
	private Activity activity;
	private ImageView hasNewMsg;
	private HorizontalScrollView mScrollView;
	private HomeDataBean mHomeDataBean;
	private ImageButton recordButton;
	private FrameLayout mFrameLayout;
	private BottomPopDialog popDialog;
	private TextView signtipsTv;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_record, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		initview(view);
		initViewsmodle(false);
		registerReceiver();
		getRecordData();
	}
	
	

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loginStateChange(PreManager.instance().getIsLogin(activity));
	}
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		activity.unregisterReceiver(mReceiver);
	}
	
	
	public void setHasNewMsg(int visibility) {
		if (hasNewMsg != null)
			hasNewMsg.setVisibility(visibility);
	}
	
	
	private void initview(View view) {
		view.findViewById(R.id.btn1).setOnClickListener(this);
		view.findViewById(R.id.btn2).setOnClickListener(this);
		view.findViewById(R.id.btn4).setOnClickListener(this);
		
		mFrameLayout = (FrameLayout) view.findViewById(R.id.fram_bg);//margin relTimebg's hight +title's hight
		relTimebg = (RelativeLayout) view.findViewById(R.id.rel_data);//margin top title's hight;

		linTimeState = (LinearLayout) view.findViewById(R.id.rl_time);
		hasNewMsg = (ImageView) view.findViewById(R.id.has_new_msg);
		userIcon = (CircleImageView) view.findViewById(R.id.img_user_Icon);
		userName = (TextView) view.findViewById(R.id.tv_user_Name);
		stateMsg = (TextView) view.findViewById(R.id.tv_state_msg);
		btnStartSetPlan = (Button) view.findViewById(R.id.btn_set_plan);
		btnStartWeekReport = (Button) view.findViewById(R.id.btn_week_report);
		linUserIcon = (LinearLayout) view.findViewById(R.id.lin_user_icon);
		RelSleepInfo= (LinearLayout) view.findViewById(R.id.rel_sleep_info);
		RelSleepPlan= (RelativeLayout) view.findViewById(R.id.rel_sleep_set_plan);
		RelSleepState = (RelativeLayout) view.findViewById(R.id.lin_sleep_state);
		RelSleepWeek= (RelativeLayout)view.findViewById(R.id.lin_sleep_week);
		RelHead = (RelativeLayout) view.findViewById(R.id.rel_head);
		joinNum = (TextView) view.findViewById(R.id.tv_join_num);
//		headRightBtn = (Button) view.findViewById(R.id.head_right_btn);
//		stateName = (TextView) view.findViewById(R.id.tv_state_name);
		signtipsTv = (TextView) view.findViewById(R.id.tv_sign_tips);
		view.findViewById(R.id.tv_fankui).setOnClickListener(this);
		btnStartSetPlan.setOnClickListener(this);
		btnStartWeekReport.setOnClickListener(this);
		RelSleepState.setOnClickListener(this);
		RelHead.setOnClickListener(this);
		signtipsTv.setOnClickListener(this);
		RelSleepInfo.setVisibility(View.GONE);
		RelSleepPlan.setVisibility(View.VISIBLE);
		mScrollView = (HorizontalScrollView) view.findViewById(R.id.hsv_view);
		recordButton = (ImageButton) view.findViewById(R.id.head_record_btn);
		recordButton.setOnClickListener(this);
		
	}
	
	
	private void initViewsmodle(boolean isshow){
		
		RelativeLayout.LayoutParams relp4 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) (Constant.screenWidht * 2 /3));
		relp4.topMargin = Util.Dp2Px(activity, 58) + Constant.screenHeight/6 - 20;
		
		RelativeLayout.LayoutParams relp5 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) (Constant.screenWidht * 2 /3));
		relp5.topMargin = Util.Dp2Px(activity, 58);
		
		RelativeLayout.LayoutParams relp6 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		relp6.topMargin = Util.Dp2Px(activity, 58 )+ Constant.screenHeight/6 - 20 + (int) (Constant.screenWidht * 2 /3);
		
		RelativeLayout.LayoutParams relp7 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		relp7.topMargin = Util.Dp2Px(activity, 58 )+ Constant.screenHeight/2;
		
		LinearLayout.LayoutParams relp8 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		relp8.bottomMargin = Constant.screenWidht/12 + Util.Dp2Px(activity, 20);
		
		FrameLayout.LayoutParams relp9 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		relp9.bottomMargin = Constant.screenWidht/12 + Util.Dp2Px(activity, 20);

//		RelSleepWeek.setPadding(Constant.screenWidht/3, 0, 0, Constant.screenWidht/12 + Util.Dp2Px(activity, 20));
		RelSleepState.setPadding(Constant.screenWidht / 3, 0, 0, Constant.screenWidht / 12 + Util.Dp2Px(activity, 20));
//		RelSleepPlan.setPadding(0, 0, 0, Constant.screenWidht/12 + Util.Dp2Px(activity, 20));
		
		RelSleepWeek.setLayoutParams(relp8);
		RelSleepPlan.setLayoutParams(relp9);
		relTimebg.setLayoutParams(relp5);
		if(isshow){
			RelSleepPlan.setVisibility(View.GONE);
			//relTimebg.setLayoutParams(relp5);
			mFrameLayout.setLayoutParams(relp4);
		}else{
			RelSleepPlan.setVisibility(View.VISIBLE);
			mFrameLayout.setLayoutParams(relp4);
			mFrameLayout.setBackgroundResource(R.drawable.bg_home_impro);
		}
	}
	
	/**
	 * 登录状态改变
	 * 
	 * @param
	 */
	private void loginStateChange(boolean login) {
		if (login) {
//			recordButton.setVisibility(View.VISIBLE);
			final String profileKey = PreManager.instance().getUserProfileKey(activity);
			ImageLoader.getInstance().displayImage(
					PreManager.instance().getUserProfileUrl(activity),
					profileKey,
					userIcon, MyApplication.headPicOptn,new ImageLoadingListener() {
								
								@Override
								public void onLoadingStarted(String arg0, View arg1) {
									
								}
								
								@Override
								public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
									QiNiuUploadTool.getInstance().refreshDisCache(activity, profileKey, userIcon);
								}
								
								@Override
								public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
									
								}
								
								@Override
								public void onLoadingCancelled(String arg0, View arg1) {
									
								}
							});

			userName.setText(PreManager.instance().getUserNickname(
					activity));
		} else {
			setHasNewMsg(View.GONE);
//			recordButton.setVisibility(View.GONE);
//			headRightBtn.setVisibility(View.GONE);
			userIcon.setBackgroundResource(R.drawable.default_head_icon);
			userIcon.setImageDrawable(null);
			userName.setText("登录");
		}
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btn1:
			if(PreManager.instance().getIsLogin(activity)){//未登陆的时候提醒登陆
				intent = new Intent(activity, PlayMusicActivity.class);
			}else{
				activity.startActivity(new Intent(activity, LoginActivity.class));
			}
//			intent = new Intent(activity, RecordFeelDataActivity.class)
//			.putExtra("feel_flag",1)
//			.putExtra("env_flag", 1)
//			.putExtra("smoke_flag", 1)
//			.putExtra("win_flag", 1)
//			.putExtra("coffo_flag",1)
//			.putExtra("sport_flag",1)
//			.putExtra("weight_flag", 1);
			break;
		case R.id.btn2:
			if(PreManager.instance().getIsLogin(activity))
				intent = new Intent(activity, RecordNoteActivity.class);	
			else
				intent = new Intent(activity, LoginActivity.class);
			break;
		case R.id.btn4:
			if(PreManager.instance().getIsLogin(activity)){
					intent = new Intent(activity, PerceiveResetActivity.class);
			}else
				intent = new Intent(activity, LoginActivity.class);
			break;
		case R.id.rel_head:
			if (PreManager.instance().getIsLogin(activity)) {
				intent = new Intent(activity, PersonalActivity.class);
				if(mHomeDataBean != null){
					intent.putExtra("report", mHomeDataBean);
				}
			} else {
				intent = new Intent(activity, LoginActivity.class);
			}
			break;
			
		case R.id.btn_set_plan:
			if (PreManager.instance().getIsLogin(activity)) {
				intent = new Intent(activity, ImprSlpPlanActivity.class);
			} else {
				intent = new Intent(activity, LoginActivity.class);
			}
			break;
		case R.id.lin_sleep_state:
			if (PreManager.instance().getIsLogin(activity)) {
				intent = new Intent(activity, SlpPlanActivity.class);
				if(mHomeDataBean != null && mHomeDataBean.getPlan() != null && !TextUtils.isEmpty(mHomeDataBean.getPlan().getTitle())){
					intent.putExtra("plan", mHomeDataBean.getPlan());
				}
			} else {
				intent = new Intent(activity, LoginActivity.class);
			}
			break;
		case R.id.btn_week_report:
//		case R.id.head_right_btn:
			if((mHomeDataBean.getData_flag() | mHomeDataBean.getFeel_flag() | mHomeDataBean.getEnv_flag() |
					mHomeDataBean.getSmoke_flag() | mHomeDataBean.getWin_flag() |
					mHomeDataBean.getCoffo_flag() | mHomeDataBean.getWeight_flag()) == 1){
				if(mHomeDataBean.getData_flag() == 1){
					intent = new Intent(activity, RecordSleepDataActivity.class)
					.putExtra("data_flag", mHomeDataBean.getData_flag())
					.putExtra("feel_flag", mHomeDataBean.getFeel_flag())
					.putExtra("env_flag", mHomeDataBean.getEnv_flag())
					.putExtra("smoke_flag", mHomeDataBean.getSmoke_flag())
					.putExtra("win_flag", mHomeDataBean.getWin_flag())
					.putExtra("coffo_flag", mHomeDataBean.getCoffo_flag())
					.putExtra("weight_flag", mHomeDataBean.getWeight_flag())
					.putExtra("sport_flag", mHomeDataBean.getSport_flag());
				}else{
					intent = new Intent(activity, RecordFeelDataActivity.class)
					.putExtra("feel_flag", mHomeDataBean.getFeel_flag())
					.putExtra("env_flag", mHomeDataBean.getEnv_flag())
					.putExtra("smoke_flag", mHomeDataBean.getSmoke_flag())
					.putExtra("win_flag", mHomeDataBean.getWin_flag())
					.putExtra("coffo_flag", mHomeDataBean.getCoffo_flag())
					.putExtra("weight_flag", mHomeDataBean.getWeight_flag())
					.putExtra("sport_flag", mHomeDataBean.getSport_flag());
				}
			}else{
				if(mHomeDataBean.getIndex_flag() == 1){
					showDialogForRecord();
				}else{
					intent = new Intent(activity, SleepDataReportActivity.class);
				}
			}
			
			break;
		case R.id.head_record_btn:
			if (PreManager.instance().getIsLogin(activity)) {
				//intent = new Intent(activity, SleepDataReportActivity.class);
				intent = new Intent(activity, SleepTrendActivity.class);
			} else {
				intent = new Intent(activity, LoginActivity.class);
			}
			break;
		case R.id.tv_sign_tips:
			if(signtipsTv.getTag() != null){
				intent = new Intent(activity, RecordSleepDataActivity.class);
				intent.putExtra("date", (String)signtipsTv.getTag());
			}
			break;
		case R.id.tv_fankui:
			if (PreManager.instance().getIsLogin(activity)) {
				intent = new Intent(activity, FeedBackActivity.class);
			} else {
				intent = new Intent(activity, LoginActivity.class);
			}
			break;

		default:
			break;
		}
		if(intent != null){
			startActivity(intent);
		}
	}
	
	
	private void showDialogForRecord(){
		if(popDialog == null){
			popDialog = new BottomPopDialog(activity, new BottomPopDialog.PopDialogClickListener() {
				
				@Override
				public void PopDialogClick(int clickid) {
					if(1 == clickid){
						popDialog.cancel();
						Intent intent = new Intent(activity, SleepDataReportActivity.class);
						startActivity(intent);
					}
				}
			});
		}
		popDialog.show();
		popDialog.setShowViewsAndBtn(1, "睡眠数据不齐全，是否补充？", null, "补充", "直接出报告");
	}

	/**
	 * 广播注册
	 */
	private void registerReceiver() {
		IntentFilter inFilter = new IntentFilter();
		inFilter.addAction(Constant.BUND_BLUETOOTH_CHANGE);
		inFilter.addAction(Constant.RECEVER_EXIT);
		inFilter.addAction(Constant.RECEVER_USER_BIRTHDAY_UPDATE);
		inFilter.addAction(Constant.MODIFY_PLAN);
		inFilter.addAction(Constant.WEEK_FEED_BACK_SUC);
		inFilter.addAction(Constant.RECEVER_LOGIN_ACTION);
		inFilter.addAction(Constant.SLEEP_PLAN_IMPROVE_ACTION);
		inFilter.addAction(Constant.MODEFY_SLEEP_FEEL_ACTION);
		inFilter.addAction(Constant.IMPROVE_SLEEP_PLAN);
		inFilter.addAction("com.action.msg.FRIEND_REQSEST_CHANGE");
		inFilter.addAction("com.action.msg.OPERATE_AUDIO_CHANGE");
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
				
			} else if (Constant.RECEVER_EXIT.equals(action)) {
				RelSleepPlan.setVisibility(View.VISIBLE);
				RelSleepInfo.setVisibility(View.GONE);
				getRecordData();
				loginStateChange(false);
			} else if (Constant.RECEVER_USER_BIRTHDAY_UPDATE.equals(action)) {
				
			}else if(Constant.MODIFY_PLAN.equals(action) || Constant.SLEEP_PLAN_IMPROVE_ACTION.equals(action)){
				getRecordData();

			}else if(Constant.WEEK_FEED_BACK_SUC .equals(action) || Constant.MODEFY_SLEEP_FEEL_ACTION .equals(action)){
				getRecordData();
			}else if(Constant.RECEVER_LOGIN_ACTION.equals(action)){
				getRecordData();
				loginStateChange(true);
			}else if (action.equals("com.action.msg.FRIEND_REQSEST_CHANGE")) {
				setHasNewMsg(View.VISIBLE);
			} else if (action.equals("com.action.msg.OPERATE_AUDIO_CHANGE")) {
				setHasNewMsg(View.VISIBLE);
			}else if (Constant.IMPROVE_SLEEP_PLAN.equals(action)) {
				getRecordData();
				if(data != null){
					ImprSucDialog mImprSucDialog = new ImprSucDialog(activity);

					mImprSucDialog.setData(data.getStringExtra("sleep"), data.getStringExtra("getup"));
					mImprSucDialog.show();
				}
			}
		}
	};


	/**
	 * 获取首页数据
	 */
	private void getRecordData(){
		if(Util.checkNetWork(activity)){
			String typeString = "1";
			new XiangchengMallProcClass(activity).getHomeData(PreManager.instance().getUserId(activity), typeString, new InterfaceHomeDataCallBack() {
				
				@Override
				public void onSuccess(String icode, String response, HomeDataBean dataBena) {
					mHomeDataBean = dataBena;
					setHomeDate(dataBena);
					PreManager.instance().saveRecordHomeData(activity, response);
					Intent intent = new Intent();
					intent.setAction(Constant.WEEK_FEED_BACK_SUC_INPERSONAL);
					intent.putExtra("report_changge", mHomeDataBean);
					activity.sendBroadcast(intent);
				}
				
				@Override
				public void onError(String icode, String strErrMsg) {
					LogUtil.d("chen", strErrMsg);
				}
			});
		}else{
			String response = PreManager.instance().getRecordHomeData(activity);
			if(!TextUtils.isEmpty(response)){
				mHomeDataBean = Util.getHomeDataBeanFromJson(response);
				setHomeDate(mHomeDataBean);
			}
		}
		
	}
	/**
	 * 修改显示状态
	 * @param dataBena
	 */
	private void setHomeDate(HomeDataBean dataBena){

		if(!TextUtils.isEmpty(dataBena.getSleep())  &&  !TextUtils.isEmpty(dataBena.getWakeup())){
			PreManager.instance().saveSleepTime_Setting(activity, dataBena.getSleep());
			PreManager.instance().saveGetupTime_Setting(activity, dataBena.getWakeup());
		}

		recordButton.setVisibility(View.VISIBLE);
		relTimebg.setVisibility(View.VISIBLE);
		if(dataBena.getPlan_flag() == 1){//已设置改善睡眠计划
			//relTimebg.setVisibility(View.VISIBLE);
			RelSleepInfo.setVisibility(View.VISIBLE);
			RelSleepPlan.setVisibility(View.GONE);
			addLabel(dataBena.getPlan_list());
			if(dataBena.getPlan() != null && dataBena.getPlan().getSuggest() != null)
				stateMsg.setText(dataBena.getPlan().getSuggest());
			if(dataBena.getZongjie_flag() == 1){//显示总结
				RelSleepState.setVisibility(View.GONE);
				RelSleepWeek.setVisibility(View.VISIBLE);
//				headRightBtn.setVisibility(View.VISIBLE);
//				headRightBtn.setOnClickListener(this);
				//recordButton.setVisibility(View.GONE);
				mFrameLayout.setBackgroundResource(R.drawable.bg_home_report);
			}else{//显示正常的计划信息
				RelSleepWeek.setVisibility(View.GONE);
				RelSleepState.setVisibility(View.VISIBLE);
				/*if(dataBena.getReport_before() == 1){
					recordButton.setVisibility(View.VISIBLE);
					recordButton.setOnClickListener(this);
				}*/
				if(dataBena != null && dataBena.getPlan() != null &&dataBena.getPlan().getTitle()!=null){
					if (dataBena.getPlan().getTitle().equals("可睡时间")) {
						mFrameLayout.setBackgroundResource(R.drawable.bg_home);
					} else if (dataBena.getPlan().getTitle().equals("停止忙碌")) {
						mFrameLayout.setBackgroundResource(R.drawable.bg_home_1);
					} else if (dataBena.getPlan().getTitle().equals("活动时间")) {
						mFrameLayout.setBackgroundResource(R.drawable.bg_home_2);
					} else if (dataBena.getPlan().getTitle().equals("安静休息")) {
						mFrameLayout.setBackgroundResource(R.drawable.bg_home_3);
					} else {
						mFrameLayout.setBackgroundResource(R.drawable.bg_home_4);
					}
				}
			}
			
			initViewsmodle(true);
		}else{//未设置改善睡眠计划
			addLabel(setPlanList());
//			relTimebg.setVisibility(View.GONE);
			RelSleepPlan.setVisibility(View.VISIBLE);
			RelSleepInfo.setVisibility(View.GONE);
			initViewsmodle(false);
		}
		

		addUser(dataBena.getUser_list());
		joinNum.setText(dataBena.getNums() + "橙友正在参与");
		getSleepDataAndSetSmartAlarm(1);
	}

	/**
	 * 在没有睡眠数据的时候设置假数据
	 * @return
	 */
	private List<PlanListBean> setPlanList(){
		List<PlanListBean> plan_list = new ArrayList<PlanListBean>();
		for(int k = 0 ; k < 7 ; k++){
			PlanListBean mPLbean = new PlanListBean();
			mPLbean.setDateline(TimeFormatUtil.getOtherDay("yyyyMMdd", -k));
			mPLbean.setFlag(0);
			mPLbean.setXiaolv(0);
			plan_list.add(mPLbean);
		}
		return plan_list;
	}
	/**
	 * 初始化每日签到数据
	 * @param plan_list
	 */
	private void addLabel(List<PlanListBean> plan_list){
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Constant.screenWidht/6, Constant.screenHeight/6);
		lp.setMargins(0, 0, 0, 0);
		int offset = -1;
		linTimeState.removeAllViews();
		if(plan_list == null || plan_list.size() == 0){
			linTimeState.setVisibility(View.GONE);
		}else{
			linTimeState.setVisibility(View.VISIBLE);
			for (int i = 0; i < plan_list.size(); i++) {
				if(plan_list.get(i).getDateline().equals(TimeFormatUtil.getTodayTimeyyyyMMdd())){
					offset = i;
					McolumnChart1 column= new McolumnChart1(activity);
					column.setValue(plan_list.get(i), PreManager.instance().getIsLogin(activity));
					column.setLayoutParams(lp);
					if(!PreManager.instance().getIsLogin(activity)){
						column.setOnClickListener(new LabelOnClickToLoginListener());
					}
//					column.setOnClickListener(new LabelOnClickListener(plan_list.get(i)));
					column.setPadding(10, 10, 10, 10);
					LinearLayout linearLayout = new LinearLayout(activity);
					linearLayout.setOrientation(LinearLayout.HORIZONTAL);
					linearLayout.setGravity(Gravity.CENTER_VERTICAL);
					linearLayout.addView(column);
					linTimeState.addView(linearLayout);
				
				}else{
					McolumnChart1 column= new McolumnChart1(activity);
					if(TimeFormatUtil.isDateBiger(TimeFormatUtil.getTodayTimeyyyyMMdd(), plan_list.get(i).getDateline(), "yyyyMMdd")){
						column.setOnClickListener(new LabelOnClickListener(plan_list.get(i)));
						column.setValue(plan_list.get(i), PreManager.instance().getIsLogin(activity));
					}else{
						column.setValue(plan_list.get(i),  PreManager.instance().getIsLogin(activity));
						if(!PreManager.instance().getIsLogin(activity)){
							column.setOnClickListener(new LabelOnClickToLoginListener());
						}
					}
					column.setLayoutParams(lp);
					column.setPadding(10, 10, 10, 10);
					LinearLayout linearLayout = new LinearLayout(activity);
					linearLayout.setOrientation(LinearLayout.HORIZONTAL);
					linearLayout.setGravity(Gravity.CENTER_VERTICAL);
					linearLayout.addView(column);
					linTimeState.addView(linearLayout);

				}
			}	
		}
		if(offset > 4){
			mScrollView.post(new Runnable() {
				
				@Override
				public void run() {
					mScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
				}
			});
		}

		if(plan_list != null && plan_list.size() != 0){
			signtipsTv.setTag(null);
			linTimeState.setVisibility(View.VISIBLE);
			signtipsTv.setVisibility(View.GONE);
			for (int i = 0; i < plan_list.size(); i++) {
				if(TimeFormatUtil.isDateBiger(TimeFormatUtil.getTodayTimeyyyyMMdd(), plan_list.get(i).getDateline(), "yyyyMMdd")){
					if(plan_list.get(i).getFlag() == 0){
						signtipsTv.setVisibility(View.VISIBLE);
						if(plan_list.get(i).getDateline().equals(TimeFormatUtil.getYesterDay("yyyyMMdd"))){
							signtipsTv.setText("昨晚的睡眠数据还未记录，立即添加 >");
							signtipsTv.setTag(TimeFormatUtil.ExchangeTimeformat(plan_list.get(i).getDateline(), "yyyyMMdd", "yyyy-MM-dd"));
							return;
						}else{
							signtipsTv.setText(TimeFormatUtil.ExchangeTimeformat(plan_list.get(i).getDateline(), "yyyyMMdd", "MM月dd日") + "的睡眠数据还未记录，立即添加 >");
							signtipsTv.setTag(TimeFormatUtil.ExchangeTimeformat(plan_list.get(i).getDateline(), "yyyyMMdd", "yyyy-MM-dd"));
						}
					}
				}
			}
		}
	}
	/**
	 * 每日数据的点击监听
	 * @author Administrator
	 *
	 */
	class LabelOnClickListener implements OnClickListener {
		PlanListBean Bean;
		public LabelOnClickListener(PlanListBean bean) {
			Bean = bean;
		}

		@Override
		public void onClick(View view) {
			Intent intent = null;
			if(Bean.getFlag() == 0){
				intent = new Intent(activity, RecordSleepDataActivity.class);
				intent.putExtra("date", TimeFormatUtil.ExchangeTimeformat(Bean.getDateline(),"yyyyMMdd", "yyyy-MM-dd"));
			}else{
				intent = new Intent(activity, DataResultFeedbackActivity.class);
				intent.putExtra("date", TimeFormatUtil.ExchangeTimeformat(Bean.getDateline(),"yyyyMMdd", "yyyy-MM-dd"));
				intent.putExtra("type", "1");
			}
			if(intent != null)
			startActivity(intent);
		}
	}


	class LabelOnClickToLoginListener implements OnClickListener {


		@Override
		public void onClick(View view) {
			startActivity(new Intent(activity, LoginActivity.class));
		}
	}
	
	public void startRecord(){
		Intent intent = null;
		if (PreManager.instance().getIsLogin(activity)) {
			if(mHomeDataBean == null){
				return;
			}else{
				if(mHomeDataBean.getPlan_flag() != 1){//没有设置计划的设置计划
					intent = new Intent(activity, ImprSlpPlanActivity.class);
				}else{//设置了计划
					if(isSingin(TimeFormatUtil.getYesterDay("yyyyMMdd"))){
						intent = new Intent(activity, DataResultFeedbackActivity.class);
						intent.putExtra("date", TimeFormatUtil.getYesterDay("yyyy-MM-dd"));
						intent.putExtra("type", "1");
					}else{
						intent = new Intent(activity, RecordSleepDataActivity.class);
						intent.putExtra("date", TimeFormatUtil.getYesterDay("yyyy-MM-dd"));
					}
				}
			}
		} else {
			intent = new Intent(activity, LoginActivity.class);
		}
		if(intent != null)
			startActivity(intent);
	}
	
	
	private boolean isSingin(String daytime){
		
		if(mHomeDataBean.getPlan_list() != null){
			for ( PlanListBean mPlanListBean : mHomeDataBean.getPlan_list()) {
				if(mPlanListBean.getDateline().equals(daytime)){
					if(mPlanListBean.getFlag() == 1){
						return true;
					}else{
						return false;
					}
				}
			}
		}
		return false;
	}
	/**
	 * 显示用户头像
	 * @param userlist
	 */
	private void addUser(List<UserBean> userlist){
			@SuppressWarnings("static-access")
			LayoutInflater inflater2 = activity.getLayoutInflater().from(activity);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Constant.screenWidht/12,LayoutParams.WRAP_CONTENT);
			LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(Constant.screenWidht/12,Constant.screenWidht/12);
			lp.leftMargin = Util.Dp2Px(activity, 5);
			lp.rightMargin = Util.Dp2Px(activity, 5);
			if(linUserIcon.getChildCount()!=0){
				linUserIcon.removeAllViews();
			}
			CircleImageView molderImg;
			for (int i = 0; i < ((userlist.size() >3 ? 3 : userlist.size())); i++) {
				UserBean mItemBean = userlist.get(i);
				View view  = inflater2.inflate(R.layout.item_pic_and_text, null);
				molderImg = (CircleImageView) view.findViewById(R.id.item_pic_img);
				view.findViewById(R.id.pic_title).setVisibility(View.GONE);
				molderImg.setLayoutParams(lp2);
				view.setLayoutParams(lp);
				ImageLoader.getInstance().displayImage(mItemBean.getPic(), mItemBean.getPic_key(), molderImg, MyApplication.headPicOptn);
				linUserIcon.addView(view);
			}
	}


	private void getSleepDataAndSetSmartAlarm(int remind_style){
		//判断是否开启智能闹钟
		//如果开启则则去取得提醒然后设置提醒时间
		SmartRemindBean mSmartRemindBean = null;
		SharedPreferences sp = activity.getApplicationContext().getSharedPreferences(SleepInfo.SLEEP_SETTIME, activity.getApplicationContext().MODE_APPEND);
		String startTime = "";
		String endTime = "";
		String suggestSleepTime = PreManager.instance().getSleepTime_Setting(activity);
		/*String remind_time_style = sp.getString(SleepInfo.REMIND_BEFORE_SLEEP, "");
		if(remind_time_style.equals("")){
			String remindStr = PreManager.instance().getSmartRemindData(activity);
			if(TextUtils.isEmpty(remindStr)){
				return;
			}

			RemindBean mRemindBean = new Gson().fromJson(remindStr, new TypeToken<RemindBean>() {
			}.getType());
			if(!mRemindBean.getDate().equals(TimeFormatUtil.getYesterDay("yyyy-MM-dd"))){
				return;
			}
			try {
				startTime = TimeFormatUtil.formatTime(Long.parseLong(mRemindBean.getSleepTime()), "HH:mm");//TimeFormatUtil.ExchangeTimeformat(mRemindBean.getSleepTime(), "yyyyMMdd HH:mm", "HH:mm");
				endTime = TimeFormatUtil.formatTime(Long.parseLong(mRemindBean.getGetupTime()), "HH:mm");
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(startTime.equals("")||endTime.equals("")){
				return;
			}
			mSmartRemindBean = SmartNotificationUtil.GetSmartNotifications(remind_style, endTime, startTime, suggestSleepTime);
		}*/

		mSmartRemindBean = new SmartRemindBean();
		mSmartRemindBean.Remindmsgs = "现在开始，在您感觉很困的时候才上床。";
		mSmartRemindBean.SuggestRemindTime = suggestSleepTime;

		if(mSmartRemindBean != null && mSmartRemindBean.SuggestRemindTime != null){
			LogUtil.d("chen", suggestSleepTime + mSmartRemindBean.Remindmsgs);
			//重新设置睡觉提醒时间
			SharedPreferences.Editor edt = sp.edit();
			edt.putString(SleepInfo.REMIND_MSG, mSmartRemindBean.Remindmsgs);
			if(remind_style == 0){
				edt.putString(SleepInfo.REMIND_TIME_DAY, mSmartRemindBean.SuggestRemindTime);
			}else{
				edt.putString(SleepInfo.REMIND_TIME_NIGHT, mSmartRemindBean.SuggestRemindTime);
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
		}
	}

	//设置提醒闹钟
	private void openRemindAlarm(long remindtime){

		Intent intent = new Intent("com.yzm.sleep.alarm.MART_NOTIFY");

		PendingIntent sender = PendingIntent.getBroadcast(
				activity, 0, intent, 0);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(remindtime);
		AlarmManager am = (AlarmManager) activity.getSystemService(activity.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
	}
}
