package com.yzm.sleep.activity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.util.EasyUtils;
import com.easemob.util.NetUtils;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.MessageInterface;
import com.yzm.sleep.R;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.background.MytabOperate;
import com.yzm.sleep.background.SecretaryDBOperate;
import com.yzm.sleep.background.StartTimingService;
import com.yzm.sleep.bean.MsgListBean;
import com.yzm.sleep.fragment.FragmentPage1Record;
import com.yzm.sleep.fragment.FragmentPage2Doctor;
import com.yzm.sleep.fragment.FragmentPage3Community;
import com.yzm.sleep.fragment.FragmentPage3Tab1;
import com.yzm.sleep.fragment.FragmentPage4Profile;
import com.yzm.sleep.im.ChatActivity;
import com.yzm.sleep.model.MyDialog;
import com.yzm.sleep.utils.CommunityProcClass;
import com.yzm.sleep.utils.EaseUI;
import com.yzm.sleep.utils.InterFaceUtilsClass.DynamicMsgNumParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDynamicMsgNumCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetHardwareSensitivityCallBack;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.SleepConstants;

@SuppressLint({ "CutPasteId", "SimpleDateFormat" })
public class HomeActivity extends BaseActivity implements
		OnCheckedChangeListener, MessageInterface, EMEventListener,
		EMConnectionListener {

	private RadioGroup tabsGroup;
	private FragmentManager fm;
	private FragmentPage1Record recordFragment;
//	private FragmentPage3Community communityFragment;
	private FragmentPage3Tab1 communityFragment;
	private Fragment mContent;
	private List<Fragment> fragments;
//	private CircleImageView tipIcon;
	private static final int MESSAGE_COUNT_CHANGE = 0;
	private static final int UPLOAD_ERROR = 2;
	private static final int UPLOAD_FAULT = 3;
	private static final int UPLOAD_NO_LOGIN = 4;
	private int countUpdate = 0;
	private int countQuest = 0;
	private int countFriendMessage = 0;
	private View firstTip;
	public static Tencent mTencent;
	private MyDialog downloadDialog;
	private ImageView addRecordView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		registerReceiver();
		initQQShare();
		initView();
		
		EMChatManager.getInstance().addConnectionListener(this);
		try {
			new StartTimingService(this).start();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (savedInstanceState != null
				&& PreManager.instance().getIsLogin(HomeActivity.this)) {
			// 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
			// 三个fragment里加的判断同理
			// ///////环信没有登录重新登录
			if (!EMChat.getInstance().isLoggedIn()) {

				EMChatManager.getInstance().login(
						PreManager.instance().getUserId(HomeActivity.this),
						"123456", new EMCallBack() {

							@Override
							public void onSuccess() {
								EMChatManager.getInstance()
										.loadAllConversations();
							}

							@Override
							public void onProgress(int arg0, String arg1) {

							}

							@Override
							public void onError(int arg0, String arg1) {
							}
						});

			}
			return;
		}
		getLocation();

		if(getIntent().getBooleanExtra("jpushmsgtomsglist", false)){
			
			if(PreManager.instance().getIsLogin(HomeActivity.this))
				startActivity(new Intent(this, MessageListActivity.class));
			else
				startActivity(new Intent(this, LoginActivity.class));
		}
		
		if(getIntent().getBooleanExtra("chattoMsgList", false)){
			startActivity(new Intent(this, MessageListActivity.class));
		}
	}

	private void initQQShare() {
		if (mTencent == null) {
			mTencent = Tencent.createInstance(
					SleepConstants.TENCENT_SLEEP_APP_ID, HomeActivity.this);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Constant.ZEN_COMMENT_CODE
				|| resultCode == Constant.TOPICELIST_RESULTCODE) {
			if (communityFragment != null)
				communityFragment.onActivityResult(requestCode, resultCode,
						data);
		}
		mTencent.onActivityResult(requestCode, resultCode, data);
		recordFragment.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onNewIntent(Intent intent) {
		if(intent.getBooleanExtra("close_except_homeactivity", false)){
			AppManager.getAppManager().finishAllActivityExceptOne(HomeActivity.class);
			startActivity(new Intent(this, MessageListActivity.class));
		}
		if (intent.getBooleanExtra("isSetting", false)) {
			if (!PreManager.instance().getHasSetAlarm(this)) {
				if (firstTip != null) {
					clearSleepTable();
				}
			}
		}
		super.onNewIntent(intent);
	}

	/**
	 * 第一次使用前清空睡眠数据,重新加载
	 */
	private void clearSleepTable() {
		firstTip.setVisibility(View.GONE);
//		sleepFragment.refreshDownloadData();
		PreManager.instance().saveHasSetAlarm(HomeActivity.this, true);

	}

	@Override
	protected void onResume() {
		super.onResume();
		
		MobclickAgent.onResume(this); // 统计时长
		EMChatManager.getInstance().registerEventListener(
				this,
				new EMNotifierEvent.Event[] {
						EMNotifierEvent.Event.EventNewMessage,
						EMNotifierEvent.Event.EventOfflineMessage,
						EMNotifierEvent.Event.EventDeliveryAck,
						EMNotifierEvent.Event.EventReadAck });

		if (PreManager.instance().getIsLogin(this)) {
			showHasNewMsg();
		}

		checkMessageState();
		if (PreManager.instance().getIsFirstUseSmartPillow(HomeActivity.this)) {
			messageChange(true);
		}
	};

	/**
	 * 显示是否有新消息提示
	 */
	private void showHasNewMsg() {
		// 聊天未读消息
		final int chatMsgNum = EMChatManager.getInstance().getUnreadMsgsCount();
		// 获取小秘书 未读消息
		final MsgListBean bean = SecretaryDBOperate
				.queryLastMsg(getApplicationContext());
		DynamicMsgNumParamClass mParam = new DynamicMsgNumParamClass();
		mParam.my_int_id = PreManager.instance().getUserId(this);
		// 获取社区 未读消息
		new CommunityProcClass(getApplicationContext()).getDynamicMsgNum(
				mParam, new InterfaceDynamicMsgNumCallBack() {
					@Override
					public void onError(int icode, String strErrMsg) {
						int myTipNum = 0;
						if (bean != null) {
							myTipNum = bean.getMsgCount();
						}
						if ((chatMsgNum + myTipNum) > 0) {
							if (recordFragment != null)
								recordFragment.setHasNewMsg(View.VISIBLE);
						} else {
							if (recordFragment != null)
								recordFragment.setHasNewMsg(View.GONE);
						}
					}

					@Override
					public void onSuccess(int icode, String strSuccMsg, int num) {
						int myTipNum = 0;
						if (bean != null) {
							myTipNum = bean.getMsgCount();
						}
						if ((chatMsgNum + num + myTipNum) > 0) {
							if (recordFragment != null)
								recordFragment.setHasNewMsg(View.VISIBLE);
						} else {
							if (recordFragment != null)
								recordFragment.setHasNewMsg(View.GONE);
						}
					}
				});
	}

	@Override
	protected void onDestroy() {
		if (loginReceiver != null) {
			unregisterReceiver(loginReceiver);
		}
		EMChatManager.getInstance().unregisterEventListener(this);
		super.onDestroy();
	}

	private long currTime;

	@SuppressLint("ShowToast")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - currTime) > 2000) {
				Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
				currTime = System.currentTimeMillis();
				return true;
			} else {
				AppManager.getAppManager().finishAllActivity();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_COUNT_CHANGE:
				if (countUpdate + countQuest + countFriendMessage
						 > 0) {
					messageChange(true);
				} else {
					messageChange(false);
				}
				break;

			case UPLOAD_FAULT:
				Toast.makeText(HomeActivity.this, "上传数据失败", Toast.LENGTH_LONG)
						.show();
				break;
			case UPLOAD_ERROR:
				Toast.makeText(HomeActivity.this, "网络连接失败", Toast.LENGTH_LONG)
						.show();
				break;
			case UPLOAD_NO_LOGIN:
				Toast.makeText(HomeActivity.this, "请先登录", Toast.LENGTH_LONG)
						.show();
				break;
//			case DownloadUserData.DOWNLOAD_SLEEP_DATA_SUCCESS:
//				
//				toastMsg("同步成功");
//				if (downloadDialog != null && downloadDialog.isShowing()) {
//					downloadDialog.cancel();
//				}
//				sleepFragment.refreshDownloadData();
//				break;
//			case DownloadUserData.DOWNLOAD_SLEEP_DATA_ERROR:
//				toastMsg("同步失败");
//				if (downloadDialog != null && downloadDialog.isShowing()) {
//					downloadDialog.cancel();
//				}
//				break;
			default:
				break;
			}
		}
	};

	@SuppressLint("InlinedApi")
	private void initView() {
		tabsGroup = (RadioGroup) findViewById(R.id.home_tabs);
//		tipIcon = (CircleImageView) findViewById(R.id.home_tabs_tip);
		addRecordView = (ImageView) findViewById(R.id.img_add_record);
		addRecordView.setOnClickListener(this);
		tabsGroup.setOnCheckedChangeListener(this);
		fm = getSupportFragmentManager();
		fragments = new ArrayList<Fragment>();
		Bundle bundle = new Bundle();
		bundle.putInt("screenWidth", getScreenWidth());
		recordFragment = new FragmentPage1Record();
		mContent = recordFragment;
		
		FragmentPage2Doctor page2Doctor = new FragmentPage2Doctor();
		Bundle bundleDoctor = new Bundle(); 
		bundleDoctor.putInt("screenWidth", getScreenWidth());
		page2Doctor.setArguments(bundleDoctor);
		communityFragment = new FragmentPage3Tab1();
		communityFragment.setArguments(bundle);
		FragmentPage4Profile fragmentPage4Profile = new FragmentPage4Profile();
		fragmentPage4Profile.setMessageListener(this);
		
		fragments.add(page2Doctor);
		fragments.add(recordFragment);
		fragments.add(communityFragment);
//		fragments.add(fragmentPage4Profile);
		fm.beginTransaction().add(R.id.home_framelayout, recordFragment)
				.commit();
		// 睡醒提示框
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int arg1) {
		switch (arg1) {
		case R.id.home_tabs_btn1:
			switchContent(mContent, fragments.get(0));
			addRecordView.setVisibility(View.GONE);
			break;
		case R.id.home_tabs_btn2:
			switchContent(mContent, fragments.get(1));
			addRecordView.setVisibility(View.VISIBLE);
			break;
		case R.id.home_tabs_btn3:
			switchContent(mContent, fragments.get(2));
			addRecordView.setVisibility(View.GONE);
			break;
//		case R.id.home_tabs_btn4:
//			switchContent(mContent, fragments.get(3));
//			break;
			
		default:
			break;
		}
	}

	/**
	 * 切换主页的显示界面碎片
	 * 
	 * @param from
	 *            当前页面
	 * @param to
	 *            需要切换的页面
	 */
	public void switchContent(Fragment from, Fragment to) {
		if (mContent != to) {
			mContent = to;
			FragmentTransaction transaction = fm.beginTransaction();
			if (!to.isAdded()) {// 先判断是否被add过
				transaction.hide(from).add(R.id.home_framelayout, to)
						.commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
			} else {
				transaction.hide(from).show(to).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
			}
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		checkMessageState();
		if(v.getId() == R.id.img_add_record){
			if (recordFragment != null) 
				recordFragment.startRecord();
		}
	}

	private void checkMessageState() {
		checkUpdateVersion();
		if (PreManager.instance().getIsLogin(getApplicationContext())) {
			CheckInitPoint();
//			getReceiverOperatorNum(HomeActivity.this);
		}
	}

	/**
	 * 检查是否需要跟新
	 */
	private void checkUpdateVersion() {
		if (PreManager.instance().getIsUpdateVersion(HomeActivity.this)) {
			countUpdate = 1;
		} else {
			countUpdate = 0;
		}
		mHandler.sendEmptyMessage(MESSAGE_COUNT_CHANGE);
	}

	/**
	 * 获取未读消息数
	 */
	private void CheckInitPoint() {

		if (EMChat.getInstance().isLoggedIn()) {
			countFriendMessage = EMChatManager.getInstance()
					.getUnreadMsgsCount();
			PreManager.instance().saveNoReadMessageCount(HomeActivity.this,
					countFriendMessage);
			mHandler.sendEmptyMessage(MESSAGE_COUNT_CHANGE);
		}

	}

	/**
	 * 是否有未查看的消息
	 */
	@Override
	public void messageChange(boolean haveMsg) {
//		if (tipIcon == null)
//			return;
//		if (haveMsg)
//			tipIcon.setVisibility(View.VISIBLE);
//		else
//			tipIcon.setVisibility(View.GONE);
	}

	public class LoginReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Constant.RECEVER_LOGIN_ACTION)) {
				JPushInterface.setAlias(HomeActivity.this, PreManager.instance().getUserId(HomeActivity.this), new TagAliasCallback() {
					@Override
					public void gotResult(int arg0, String arg1, Set<String> arg2) {
					}
				});
//				showDownloadDataDialog();
//				DownloadUserData.getInstance(HomeActivity.this, mHandler).startUploadAndDownload();
				getDevSensitive();
			} else if (action.equals("com.action.msg.FRIEND_REQSEST_CHANGE")) {
				messageChange(true);
			} else if (action.equals("com.action.msg.OPERATE_AUDIO_CHANGE")) {
				messageChange(true);
			}
		}
	}

	/**
	 * 注册广播
	 * */
	LoginReceiver loginReceiver;

	private void registerReceiver() {
		// 初始化
		loginReceiver = new LoginReceiver();
		// 初始化IntentFilter对象
		IntentFilter filter = new IntentFilter();
		// 添加一系列动作
		filter.addAction(Constant.RECEVER_LOGIN_ACTION);
		filter.addAction("com.action.msg.FRIEND_REQSEST_CHANGE");
		filter.addAction("com.action.msg.OPERATE_AUDIO_CHANGE");
		// 注册
		registerReceiver(loginReceiver, filter);

	}

	// 账号在别处登录
	public boolean isConflict = false;
	// 账号被移除
	private boolean isCurrentAccountRemoved = false;

	/**
	 * 检查当前用户是否被删除
	 */
	public boolean getCurrentAccountRemoved() {
		return isCurrentAccountRemoved;
	}

	/**
	 * 注销登录，删除睡眠相关数据
	 */
	private void deleLocalSleepData() {
		MyDatabaseHelper helper = MyDatabaseHelper
				.getInstance(HomeActivity.this);

		// 删除日视图数据
		MytabOperate operateDayDataDelete = new MytabOperate(
				helper.getWritableDatabase(), MyTabList.SLEEPTIME);
		operateDayDataDelete.delete(null, null);
		operateDayDataDelete.close();

		// 删除硬件睡眠数据
		MytabOperate operateHardwareSleepDelete = new MytabOperate(
				helper.getWritableDatabase(), MyTabList.PILLOW_SLEEP_DATA);
		operateHardwareSleepDelete.delete(null, null);
		operateHardwareSleepDelete.close();

		// 删除硬件数据
		PreManager.instance().setBundbluetoothPillow(HomeActivity.this, "");
		PreManager.instance().setBluetoothPillowFirmwareVersion(
				HomeActivity.this, "");

	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	/**
	 * 显示下载历史数据的对话框
	 */
	private void showDownloadDataDialog() {
//		if (downloadDialog == null) {
//			downloadDialog = new MyDialog(this, 0, 0,
//					R.layout.dialog_synchro_data, R.style.bottom_animation,
//					Gravity.BOTTOM, 1.0f, 0.0f);
//		}
//		((TextView) downloadDialog.findViewById(R.id.tv)).setText("正在同步历史数据");
//		downloadDialog.setCanceledOnTouchOutside(false);
//		downloadDialog.show();
	}

	/**
	 * 获取登陆的账号设定的设备灵敏度！
	 */
	private void getDevSensitive() {
		if (checkNetWork(HomeActivity.this)) {
			new CommunityProcClass(HomeActivity.this).getHardwareSensitivity(
					PreManager.instance().getUserId(HomeActivity.this),
					new InterfaceGetHardwareSensitivityCallBack() {

						@Override
						public void onSuccess(int icode, String selectLMD,
								String allLMD) {
							if (!HomeActivity.this.isFinishing()) {
								if (!TextUtils.isEmpty(allLMD)) {
									PreManager.instance().setAllSensitives(
											HomeActivity.this, allLMD);
								}
								if (!TextUtils.isEmpty(selectLMD)) {
									if ("-1".equals(selectLMD)) {
										selectLMD = "0";
									}
									PreManager.instance()
											.setBluetoothDevSensitive(
													HomeActivity.this,
													selectLMD);
								} else {
									PreManager.instance()
											.setBluetoothDevSensitive(
													HomeActivity.this, "0");
								}
							}
						}

						@Override
						public void onError(int icode, String strErrMsg) {

						}
					});
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// super.onSaveInstanceState(outState);
		// 注释掉这个，如果进程被杀，就重新加载
	}

	@Override
	// 接收聊天消息监听
	public void onEvent(EMNotifierEvent event) {
		EMMessage message = (EMMessage) event.getData();
		switch (event.getEvent()) {
		case EventNewMessage:
			String className=AppManager.getAppManager().currentActivity().getClass().getSimpleName();
			String chatClassName=ChatActivity.class.getSimpleName();
			String msgListClassName=MessageListActivity.class.getSimpleName();
			if (!className.equals(chatClassName) && !className.equals(msgListClassName) && EasyUtils.isAppRunningForeground(getApplicationContext())) {
				EaseUI.getInstance().getNotifier().onNewMsg(message);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						if (recordFragment != null) {
							recordFragment.setHasNewMsg(View.VISIBLE);
						}
					}
				});
			}
			break;
		case EventOfflineMessage:
			int  count=EMChatManager.getInstance().getUnreadMsgsCount();
			if (count > 0) {
				if (recordFragment != null)
					recordFragment.setHasNewMsg(View.VISIBLE);
			} 
			break;
		default:
			break;
		}
	}

	@Override
	public void onConnected() {
	}

	@Override
	public void onDisconnected(final int error) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (error == EMError.USER_REMOVED && PreManager.instance().getIsLogin(HomeActivity.this)) { // 账号被移出
					isCurrentAccountRemoved = true;
					PreManager.instance().saveIsLogin(HomeActivity.this, false);
					EMChatManager.getInstance().logout();
					PreManager.instance().saveIsLogin(HomeActivity.this, false);
					PreManager.instance()
							.logoutClearLoginMsg(HomeActivity.this);
					AppManager.getAppManager().finishAllActivityExceptOne(
							HomeActivity.class);
				} else if (error == EMError.CONNECTION_CONFLICT && PreManager.instance().getIsLogin(HomeActivity.this)) {// 其他设备登陆
					EMChatManager.getInstance().logout();
					PreManager.instance().saveIsLogin(HomeActivity.this, false);
					PreManager.instance().logoutClearLoginMsg(HomeActivity.this);
					isConflict = true;
					toastMsg("账号在其它设备登录");
					sendBroadcast(new Intent("com.action.change.EXIT"));
					deleLocalSleepData();
					AppManager.getAppManager().finishAllActivityExceptOne(
							HomeActivity.class);
				} else {
					if (!NetUtils.hasNetwork(HomeActivity.this)) {
						toastMsg("网络不可用,请检查设置");
					}
				}
			}
		});
	}
}
