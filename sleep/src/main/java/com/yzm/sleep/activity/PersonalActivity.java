package com.yzm.sleep.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.CircleImageView;
import com.yzm.sleep.Constant;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.doctor.MyReservationActivity;
import com.yzm.sleep.activity.pillow.BindingProcessActivity;
import com.yzm.sleep.activity.pillow.PillowDayDataActivity;
import com.yzm.sleep.bean.HomeDataBean;
import com.yzm.sleep.bean.UserMessageBean;
import com.yzm.sleep.im.ChatActivity;
import com.yzm.sleep.utils.BottomPopDialog;
import com.yzm.sleep.utils.CommunityProcClass;
import com.yzm.sleep.utils.CustomImageLoadingListener;
import com.yzm.sleep.utils.ImprSucDialog;
import com.yzm.sleep.utils.InterFaceUtilsClass.DynamicMsgNumParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDynamicMsgNumCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSafeappCallBack;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.URLUtil;
import com.yzm.sleep.utils.XiangchengMallProcClass;

/**
 * 我的
 */
public class PersonalActivity extends BaseActivity {
	
	private TextView tv_personal_nickname;
	private CircleImageView user_icon;
	private ImageView iv_update_message, ivNoReadMsg;
	private ImageView ivUserType;
	private ImageView ivPillowMessage;
	private boolean isGetSafInfo = false;
	private boolean isClickSafe = false;
	private int flag = 2;
	private String kefuid = null;
	private HomeDataBean mHomeDataBean = null;
	private BottomPopDialog popDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_personal);
		mHomeDataBean = (HomeDataBean)getIntent().getSerializableExtra("report");
		initView();
		registerReceiver();
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("UserCenter");
		// 显示设置旁边的小红点
		if (PreManager.instance().getIsUpdateVersion(this)) 
			iv_update_message.setVisibility(View.VISIBLE);
		else 
			iv_update_message.setVisibility(View.INVISIBLE);
		
		if (PreManager.instance().getIsFirstUseSmartPillow(this)) 
			ivPillowMessage.setVisibility(View.VISIBLE);
		else 
			ivPillowMessage.setVisibility(View.INVISIBLE);
		//获取未读消息数
		getUnReadMessage();
		getSafeInfo();
	}
	
	private void initView() {
		((TextView) findViewById(R.id.title)).setText("我的");
		findViewById(R.id.back).setOnClickListener(this);
		iv_update_message = (ImageView)findViewById(R.id.iv_update_message);
		ivNoReadMsg = (ImageView) findViewById(R.id.iv_message);
		user_icon = (CircleImageView)findViewById(R.id.user_icon);
		user_icon.setOnClickListener(this);
		tv_personal_nickname = (TextView) findViewById(R.id.tv_personal_nickname);
		tv_personal_nickname.setOnClickListener(this);
		
		// 睡眠评估
		findViewById(R.id.layout_sleep_pg).setOnClickListener(this);
		//我的数据
		findViewById(R.id.layout_mysleepdata).setOnClickListener(this);
		// 智能枕头
		findViewById(R.id.layout_smart_pillow).setOnClickListener(this);
		//我的消息
		findViewById(R.id.layout_mymsg).setOnClickListener(this);
		//我的订单
		findViewById(R.id.layout_my_shop_order).setOnClickListener(this);
		// 应用设置
		findViewById(R.id.layout_setting).setOnClickListener(this);
		//保险入口
		findViewById(R.id.layout_secure).setOnClickListener(this);
		//意见反馈
		findViewById(R.id.layout_feedback).setOnClickListener(this);
		
		ivUserType = (ImageView)findViewById(R.id.ivUserType);

		ivPillowMessage = (ImageView)findViewById(R.id.ivPillowMessage);

		setData();
	}

	/**
	 * 登录状态改变
	 */
	private void setData() {
		final String profileKey = PreManager.instance().getUserProfileKey(this);
		ImageLoader.getInstance().displayImage(
				PreManager.instance().getUserProfileUrl(this),
				profileKey,
				user_icon, MyApplication.headPicOptn, new CustomImageLoadingListener(this, user_icon, profileKey, true));
		tv_personal_nickname.setText(PreManager.instance().getUserNickname(this));
		
		if ("1".equals(PreManager.instance().getUserIsExpert(this))) 
			ivUserType.setVisibility(View.VISIBLE);
		else 
			ivUserType.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.user_icon:
		case R.id.tv_personal_nickname:
			// 跳转到个人主页界面
			intent = new Intent(this, PersonalInfoActivity.class).putExtra("user_id", PreManager.instance().getUserId(this));
			break;
			
		case R.id.layout_sleep_pg://睡眠评估
			if (PreManager.instance().getIsCompleteSleepPg(this))
				intent = new Intent(this, EstimateBaseActivity.class);
			else{
				intent = new Intent(this, EstimateWebActivity.class);
				intent.putExtra("type", "0");
			}
			break;

		case R.id.layout_feedback:
			if (PreManager.instance().getIsLogin(this))
				intent = new Intent(this, FeedBackActivity.class);
			else {
				intent = new Intent(this, LoginActivity.class);
			}
			break;
			
		case R.id.layout_mysleepdata:// 我的周报//数据
			//intent = new Intent(this, SleepTrendActivity.class);
			/*if(haveReport_before == 1){
				intent = new Intent(this, SleepDataReportActivity.class);
			}else{
				toastMsg("暂无周报");
			}*/
			if((mHomeDataBean.getData_flag() | mHomeDataBean.getFeel_flag() | mHomeDataBean.getEnv_flag() |
					mHomeDataBean.getSmoke_flag() | mHomeDataBean.getWin_flag() |
					mHomeDataBean.getCoffo_flag() | mHomeDataBean.getWeight_flag()) == 1){
				if(mHomeDataBean.getData_flag() == 1){
					intent = new Intent(this, RecordSleepDataActivity.class)
							.putExtra("data_flag", mHomeDataBean.getData_flag())
							.putExtra("feel_flag", mHomeDataBean.getFeel_flag())
							.putExtra("env_flag", mHomeDataBean.getEnv_flag())
							.putExtra("smoke_flag", mHomeDataBean.getSmoke_flag())
							.putExtra("win_flag", mHomeDataBean.getWin_flag())
							.putExtra("coffo_flag", mHomeDataBean.getCoffo_flag())
							.putExtra("weight_flag", mHomeDataBean.getWeight_flag())
							.putExtra("sport_flag", mHomeDataBean.getSport_flag());
				}else{
					intent = new Intent(this, RecordFeelDataActivity.class)
							.putExtra("feel_flag", mHomeDataBean.getFeel_flag())
							.putExtra("env_flag", mHomeDataBean.getEnv_flag())
							.putExtra("smoke_flag", mHomeDataBean.getSmoke_flag())
							.putExtra("win_flag", mHomeDataBean.getWin_flag())
							.putExtra("coffo_flag", mHomeDataBean.getCoffo_flag())
							.putExtra("weight_flag", mHomeDataBean.getWeight_flag())
							.putExtra("sport_flag", mHomeDataBean.getSport_flag());
				}
			}else{
				if(mHomeDataBean.getZongjie_flag() == 1 || mHomeDataBean.getReport_before() == 1){
					if(mHomeDataBean.getIndex_flag() == 1){
						showDialogForRecord();
					}else{
						intent = new Intent(this, SleepDataReportActivity.class);
					}
				}else{
					toastMsg("暂无周报");
				}

			}
			break;
			
		case R.id.layout_smart_pillow:// 智能枕头
			PreManager.instance().saveIsFirstUseSmartPillow(this, false);
			if (!TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(this))) {
//				intent = new Intent(this, BundPillowInfoActivity.class);
				intent = new Intent(this, PillowDayDataActivity.class);
			}else 
				intent = new Intent(this, BindingProcessActivity.class);
			break;
			
		case R.id.layout_mymsg:// 我的消息
			intent = new Intent(this, MessageListActivity.class);
			ivNoReadMsg.setVisibility(View.GONE);
			break;
			
		case R.id.layout_my_shop_order: //我的订单
			intent = new Intent(this, MyReservationActivity.class);
			break;
			
		case R.id.layout_setting:// 设置
			intent = new Intent(this, AppSettingActivity.class);
			break;
		case R.id.layout_secure://保险
			if (PreManager.instance().getIsLogin(this)) {
				if(isGetSafInfo){
					if(flag == 1 && kefuid !=null){
						UserMessageBean userBean = new UserMessageBean();
						userBean.setUid(kefuid);
						userBean.setNickname("客服");
						intent = new Intent(PersonalActivity.this,ChatActivity.class);
						intent.putExtra("userBean", userBean);
					}else{
						intent = new Intent(this, WebViewActivity.class);
						intent.putExtra("title", "失眠服务");
						intent.putExtra("type", 1);
						intent.putExtra("url", URLUtil.BASEURL +"/"+"safepro/index.php?uid="+PreManager.instance().getUserId(this));
					}
				}else{
					isClickSafe = true;
				}
			} else {
				intent = new Intent(this, LoginActivity.class);
			}
			
			break;
		
		default:
			break;
		}
		
		if (intent != null) 
			startActivity(intent);
	}

	private void showDialogForRecord(){
		if(popDialog == null){
			popDialog = new BottomPopDialog(this, new BottomPopDialog.PopDialogClickListener() {

				@Override
				public void PopDialogClick(int clickid) {
					if(1 == clickid){
						popDialog.cancel();
						Intent intent = new Intent(PersonalActivity.this, SleepDataReportActivity.class);
						startActivity(intent);
					}else{
						AppManager.getAppManager().finishActivity(PersonalActivity.class);
					}
				}
			});
		}
		popDialog.show();
		popDialog.setShowViewsAndBtn(1, "睡眠数据不齐全，是否回首页补充？", null, "补充", "直接出报告");
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("UserCenter");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	/**
	 * 获取未读消息
	 */
	private void getUnReadMessage() {
		if (!PreManager.instance().getIsLogin(this) || !EMChat.getInstance().isLoggedIn()) {
			return;
		}
		
		if(EMChatManager.getInstance().getUnreadMsgsCount() > 0)
			ivNoReadMsg.setVisibility(View.VISIBLE);
		
		//获取社区未读消息数
		DynamicMsgNumParamClass mParam = new DynamicMsgNumParamClass();
		mParam.my_int_id = PreManager.instance().getUserId(getApplicationContext());
		new CommunityProcClass(getApplicationContext()).getDynamicMsgNum(mParam, new InterfaceDynamicMsgNumCallBack() {
			@Override
			public void onError(int icode, String strErrMsg) {
				// 暂不处理
			}

			@Override
			public void onSuccess(int icode, String strSuccMsg, int num) {
				if (num > 0)
					ivNoReadMsg.setVisibility(View.VISIBLE);
			}
		});
	}
	
	private void getSafeInfo(){
		if (!PreManager.instance().getIsLogin(this)) {
			return;
		}
		new XiangchengMallProcClass(this).safeapp(PreManager.instance().getUserId(this), new InterfaceSafeappCallBack() {

			@Override
			public void onSuccess(String icode, int flag, String kefuid) {
				isGetSafInfo = true;
				PersonalActivity.this.flag = flag;
				PersonalActivity.this.kefuid = kefuid;
				if (isClickSafe && !PersonalActivity.this.isFinishing()) {
					Intent intent;
					if (flag == 1 && kefuid != null) {
						UserMessageBean userBean = new UserMessageBean();
						userBean.setUid(kefuid);
						userBean.setNickname("客服");
						intent = new Intent(PersonalActivity.this, ChatActivity.class);
						intent.putExtra("userBean", userBean);
					} else {
						intent = new Intent(PersonalActivity.this, WebViewActivity.class);
						intent.putExtra("title", "保险");
						intent.putExtra("type", 1);
						intent.putExtra("url", URLUtil.BASEURL + "/" + "safepro/index.php?uid=" + PreManager.instance().getUserId(PersonalActivity.this));
					}
					isClickSafe = false;
					startActivity(intent);
				}
			}

			@Override
			public void onError(String icode, String strErrMsg) {
				isGetSafInfo = true;
				toastMsg(strErrMsg);

			}
		});
	}


	/**
	 * 广播注册
	 */
	private void registerReceiver() {
		IntentFilter inFilter = new IntentFilter();
		inFilter.addAction(Constant.WEEK_FEED_BACK_SUC_INPERSONAL);
		registerReceiver(mReceiver, inFilter);
	}

	/**
	 * 接收广播后以及具体操作
	 */
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent data) {
			String action = data.getAction();
			if (Constant.WEEK_FEED_BACK_SUC_INPERSONAL.equals(action)) {
				mHomeDataBean = (HomeDataBean) data.getSerializableExtra("report_changge");
			}
		}
	};
}