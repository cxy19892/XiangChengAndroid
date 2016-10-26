package com.yzm.sleep.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.CircleImageView;
import com.yzm.sleep.Constant;
import com.yzm.sleep.MessageInterface;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.AppSettingActivity;
import com.yzm.sleep.activity.EstimateBaseActivity;
import com.yzm.sleep.activity.EstimateWebActivity;
import com.yzm.sleep.activity.LoginActivity;
import com.yzm.sleep.activity.OrdersActivity;
import com.yzm.sleep.activity.PersonalInfoActivity;
import com.yzm.sleep.activity.PlayMusicActivity;
import com.yzm.sleep.activity.SleepPlanActivity;
import com.yzm.sleep.activity.SleepReportActivity;
import com.yzm.sleep.activity.community.MyGroupListActivity;
import com.yzm.sleep.activity.doctor.MyReservationActivity;
import com.yzm.sleep.activity.pillow.BindingProcessActivity;
import com.yzm.sleep.activity.pillow.BundPillowInfoActivity;
import com.yzm.sleep.activity.shop.ShopOrderListActivity;
import com.yzm.sleep.model.FriendsNearbyInfo;
import com.yzm.sleep.model.UserMsg;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.QiNiuUploadTool;

public class FragmentPage4Profile extends Fragment implements OnClickListener {
	private final int MESSAGE_COUNT_CHANGE = 3;
	private final int REQUEST_CODE = 4;
	private final int REQUEST_FALSE = 5;
	public ArrayList<FriendsNearbyInfo> request_list = new ArrayList<FriendsNearbyInfo>();
	private Activity activity;
	private View fragment_personal;
	private TextView tv_personal_nickname;
	private CircleImageView user_icon;
	private ImageView iv_update_message;
	private int noreadCount = 0;
	private int operatorReceiverCount = 0;
	private boolean isNeedUpdata = false;
	private ImageView ivUserType;
	private MessageInterface messageInterface;
	private ImageView ivPillowMessage;
	private RelativeLayout layoutUserHead;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		fragment_personal = inflater.inflate(R.layout.fragment_personal, null);
		return fragment_personal;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView();
	}

	/**
	 * 绑定消息查看的监听
	 * 
	 * @param messageInterface
	 */
	public void setMessageListener(MessageInterface messageInterface) {
		this.messageInterface = messageInterface;
	}

	@Override
	public void onResume() {
		super.onResume();
		registerReceiver();
		MobclickAgent.onPageStart("UserCenter");
		// 显示设置旁边的小红点
		if (PreManager.instance().getIsUpdateVersion(activity)) {
			iv_update_message.setVisibility(View.VISIBLE);
			isNeedUpdata = true;
		} else {
			iv_update_message.setVisibility(View.INVISIBLE);
			isNeedUpdata = false;
		}
		if (PreManager.instance().getIsFirstUseSmartPillow(activity)) {
			ivPillowMessage.setVisibility(View.VISIBLE);
		} else {
			ivPillowMessage.setVisibility(View.INVISIBLE);
		}
		// 网络请求并 显示消息旁边的消息数量，好友请求数量和铃音数量
		checkMessageState();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REQUEST_CODE:
				UserMsg userMsg = (UserMsg) msg.obj;
				request_list = userMsg.request_list;
				if (request_list != null)
					((MyApplication) activity.getApplication()).g_FriendRequest = request_list
							.size();
				else
					((MyApplication) activity.getApplication()).g_FriendRequest = 0;
				// 保存好友请求数目到 Preference
				try {
					if (request_list != null && request_list.size() > 0) {
						PreManager.instance().saveMessageCount(
								activity.getApplicationContext(),
								request_list.size());
					} else {
						PreManager.instance().saveMessageCount(
								activity.getApplicationContext(), 0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;

			case REQUEST_FALSE:
				break;

			case MESSAGE_COUNT_CHANGE:
				ifShowingRedpointInHomeBottom();
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 登录状态改变
	 * 
	 * @param state
	 */
	private void loginStateChange(boolean login) {
		if (login) {
			layoutUserHead
					.setBackgroundResource(R.drawable.home_my_head_circle);
			final String profileKey = PreManager.instance().getUserProfileKey(activity);
			ImageLoader.getInstance().displayImage(
					PreManager.instance().getUserProfileUrl(activity),
					profileKey,
					user_icon, MyApplication.headPicOptn,new ImageLoadingListener() {
								
								@Override
								public void onLoadingStarted(String arg0, View arg1) {
									
								}
								
								@Override
								public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
									QiNiuUploadTool.getInstance().refreshDisCache(activity, profileKey, user_icon);
								}
								
								@Override
								public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
									
								}
								
								@Override
								public void onLoadingCancelled(String arg0, View arg1) {
									
								}
							});

			tv_personal_nickname.setText(PreManager.instance().getUserNickname(
					activity));
			if ("1".equals(PreManager.instance().getUserIsExpert(activity))) {
				ivUserType.setVisibility(View.VISIBLE);
			} else {
				ivUserType.setVisibility(View.INVISIBLE);
			}
		} else {
			layoutUserHead.setBackgroundResource(R.drawable.ic_my_tx);
			user_icon.setImageDrawable(null);
			tv_personal_nickname.setText("点击登录");
			ivUserType.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onDestroy() {
		if (receiver != null)
			activity.unregisterReceiver(receiver);
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
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
	public void onStart() {
		super.onStart();
	}

	private void initView() {
		((TextView) fragment_personal.findViewById(R.id.title)).setText("我的");
		fragment_personal.findViewById(R.id.back).setVisibility(View.INVISIBLE);
		layoutUserHead = (RelativeLayout) fragment_personal
				.findViewById(R.id.layout_user_icon);
		iv_update_message = (ImageView) fragment_personal
				.findViewById(R.id.iv_update_message);
		user_icon = (CircleImageView) fragment_personal
				.findViewById(R.id.user_icon);
		user_icon.setOnClickListener(this);
		tv_personal_nickname = (TextView) fragment_personal
				.findViewById(R.id.tv_personal_nickname);
		tv_personal_nickname.setOnClickListener(this);
		
		// 睡眠评估
		fragment_personal.findViewById(R.id.layout_sleep_pg)
		.setOnClickListener(this);
		// 7日计划
		fragment_personal.findViewById(R.id.layout_sleep_plan)
		.setOnClickListener(this);
		//睡眠报告
		fragment_personal.findViewById(R.id.layout_sleep_report).setOnClickListener(this);
		// 智能枕头
		fragment_personal.findViewById(R.id.layout_smart_pillow)
				.setOnClickListener(this);
		// 我的小组
//		fragment_personal.findViewById(R.id.layout_my_group)
//				.setOnClickListener(this);
		// 我的预约
//		fragment_personal.findViewById(R.id.layout_my_order)
//		.setOnClickListener(this);
		// 应用设置
		fragment_personal.findViewById(R.id.layout_setting).setOnClickListener(
				this);
		//我的订单
		fragment_personal.findViewById(R.id.layout_my_shop_order).setOnClickListener(
				this);
		
		ivUserType = (ImageView) fragment_personal
				.findViewById(R.id.ivUserType);

		ivPillowMessage = (ImageView) fragment_personal
				.findViewById(R.id.ivPillowMessage);

		// ScrollView scrollView = (ScrollView)
		// fragment_personal.findViewById(R.id.scrollView);
		// scrollView.setFillViewport(true);

		loginStateChange(PreManager.instance().getIsLogin(activity));
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.user_icon:
		case R.id.tv_personal_nickname:
			if (PreManager.instance().getIsLogin(activity)) {
				intent = new Intent(activity, PersonalInfoActivity.class);// 跳转到个人主页界面
				intent.putExtra("user_id",
						PreManager.instance().getUserId(activity));
			} else {
				intent = new Intent(activity, LoginActivity.class);
			}
			break;
		case R.id.layout_sleep_plan://7日计划
			MobclickAgent.onEvent(activity, "618");
//			if (PreManager.instance().getIsLogin(activity)) {
//				if ("1".equals(PreManager.instance().getUserIsAssess(activity)))
//					intent = new Intent(activity, SleepPlanActivity.class);
//				else
//					intent = new Intent(activity, AssessmentActivity.class);
//			} else
//				intent = new Intent(activity, LoginActivity.class);
			
			if(PreManager.instance().getIsLogin(activity)){
				if (PreManager.instance().getIsCompleteSleepPg(activity))
					intent = new Intent(activity, SleepPlanActivity.class);
				else
					intent = new Intent(activity, EstimateWebActivity.class).putExtra("type", "0");
			}else
				intent = new Intent(activity, LoginActivity.class);
			
			break;
		case R.id.layout_sleep_pg://睡眠评估
			if (PreManager.instance().getIsLogin(activity)) {
				if (PreManager.instance().getIsCompleteSleepPg(activity))
					intent = new Intent(activity, EstimateBaseActivity.class);
				else{
					intent = new Intent(activity, EstimateWebActivity.class);
					intent.putExtra("type", "0");
				}
				
			} else
				intent = new Intent(activity, LoginActivity.class);
			break;
		case R.id.layout_sleep_report://睡眠报告
			if(PreManager.instance().getIsLogin(activity)){
				intent = new Intent(activity, SleepReportActivity.class);
//				intent = new Intent(activity, EstimateWebActivity.class);
//				if (PreManager.instance().getIsCompleteSleepPg(activity))
//					intent.putExtra("type", "6");
//				else
//					intent.putExtra("type", "0");
			}else
				intent = new Intent(activity, LoginActivity.class);
			break;
		case R.id.layout_setting:// 设置
			intent = new Intent(activity, AppSettingActivity.class);
			break;
		case R.id.layout_smart_pillow:// 智能枕头
			MobclickAgent.onEvent(activity, "650");
			if (PreManager.instance().getIsLogin(activity)) {
				PreManager.instance()
						.saveIsFirstUseSmartPillow(activity, false);
				if (!TextUtils.isEmpty(PreManager.instance()
						.getBundbluetoothPillow(getActivity()))) {
					intent = new Intent(getActivity(),
							BundPillowInfoActivity.class);
				} else {
					intent = new Intent(getActivity(),
							BindingProcessActivity.class);
				}
			} else {
				intent = new Intent(activity, LoginActivity.class);
			}
			break;
//		case R.id.layout_my_group:
//			if (PreManager.instance().getIsLogin(activity)) {
//				intent = new Intent(activity, MyGroupListActivity.class);
//			} else {
//				intent = new Intent(activity, LoginActivity.class);
//			}
//			break;
//		case R.id.layout_my_order:
//			if (PreManager.instance().getIsLogin(activity)) {
//				intent = new Intent(activity, MyReservationActivity.class);
//			} else {
//				intent = new Intent(activity, LoginActivity.class);
//			}
//			break;
		case R.id.layout_my_shop_order:
			if (PreManager.instance().getIsLogin(activity)) {
//				intent = new Intent(activity, OrdersActivity.class);//ShopOrderListActivity
				intent = new Intent(activity, MyReservationActivity.class);
			} else {
				intent = new Intent(activity, LoginActivity.class);
			}
			break;
		
		default:
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
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

	private void registerReceiver() {
		// 注册好友请求的广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.action.msg.FRIEND_REQSEST_CHANGE");
		intentFilter.addAction("com.action.msg.OPERATE_AUDIO_CHANGE");
		intentFilter.addAction(Constant.RECEVER_USER_MESSAGE_UPDATE);
		intentFilter.addAction(Constant.RECEVER_LOGIN_ACTION);
		intentFilter.addAction(Constant.RECEVER_EXIT);
		intentFilter.addAction(EMChatManager.getInstance()
				.getNewMessageBroadcastAction());
		intentFilter.setPriority(900);
		activity.registerReceiver(receiver, intentFilter);
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			String action = intent.getAction();
			if (action.equals(EMChatManager.getInstance()
					.getNewMessageBroadcastAction())) {
				String msgid = intent.getStringExtra("msgid");
				// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
				EMMessage message = EMChatManager.getInstance().getMessage(
						msgid);
				if (message.getChatType() == ChatType.Chat) {
					checkMessageState();
				}
			} else if (action.equals("com.action.msg.FRIEND_REQSEST_CHANGE")) {
				// 收到广播的时候去请求一遍好友请求列表
				checkMessageState();
			} else if (action.equals("com.action.msg.OPERATE_AUDIO_CHANGE")) {
				checkMessageState();
			} else if (action.equals(Constant.RECEVER_EXIT)) {
				loginStateChange(false);
			} else if (action.equals(Constant.RECEVER_LOGIN_ACTION)) {
				loginStateChange(true);
			} else if (action.equals(Constant.RECEVER_USER_MESSAGE_UPDATE)) {
				if (PreManager.instance().getIsLogin(activity)) {
					final String profileKey = PreManager.instance()
							.getUserProfileKey(activity);
					ImageLoader.getInstance().displayImage(
							PreManager.instance().getUserProfileUrl(activity),
							profileKey, user_icon,
							MyApplication.headPicOptn);

					tv_personal_nickname.setText(PreManager.instance()
							.getUserNickname(activity));
				}
			}
		}
	};

	/**
	 * 获取保存于本地的好友请求数
	 * 
	 * @return
	 */
	public int getRequestCount() {
		int requestCount = 0;
		try {
			requestCount = PreManager.instance().getMessageCount(
					activity.getApplicationContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requestCount;
	}


	/**
	 * 获取未读消息数
	 */
	private void CheckInitPoint() {

		if (EMChat.getInstance().isLoggedIn()) {

			noreadCount = EMChatManager.getInstance().getUnreadMsgsCount();

		}

	}

	/**
	 * 消息数量+好友请求数量+专属铃音数量
	 */
	private void checkMessageState() {
		if (PreManager.instance().getIsLogin(
				getActivity().getApplicationContext())) {
			CheckInitPoint();
		}
	}

	/**
	 */
	private void ifShowingRedpointInHomeBottom() {
		if (!isNeedUpdata) {
			if (noreadCount
					+ operatorReceiverCount
					+ ((MyApplication) activity.getApplication()).g_FriendRequest == 0) {// 如果需要升级
				if (messageInterface != null)
					messageInterface.messageChange(false);
			} else {// 显示
				if (messageInterface != null)
					messageInterface.messageChange(true);
			}
			if (!PreManager.instance().getIsLogin(activity)) {
				if (messageInterface != null)
					messageInterface.messageChange(false);
			}
		} else {
			if (messageInterface != null)
				messageInterface.messageChange(true);
		}
	}

}
