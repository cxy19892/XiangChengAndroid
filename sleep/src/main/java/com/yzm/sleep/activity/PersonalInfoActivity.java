package com.yzm.sleep.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.CircleImageView;
import com.yzm.sleep.Constant;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.activity.community.CommunityTopiceDetailActivity;
import com.yzm.sleep.adapter.FragmentPage3FoundAdapter;
import com.yzm.sleep.bean.ArticleBean;
import com.yzm.sleep.bean.UserMessageBean;
import com.yzm.sleep.bean.UserSleepDataBean;
import com.yzm.sleep.im.ChatActivity;
import com.yzm.sleep.utils.InterFaceUtilsClass.UserBasicInfoClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceUserMessageCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.UserMessageParamClass;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.SleepUtils;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengProcClass;

/**
 * 个人资料
 * 
 * 传入参数 user_id String
 * 
 * @author
 */
public class PersonalInfoActivity extends BaseActivity {
	private FragmentPage3FoundAdapter mAdapter;
	private List<ArticleBean> topicesData;
	private boolean hasAddHead = false;
	private ListView mListView;
	private String user_id, user_login_id;
	private int dataPage = 1, mTotalPage = 0;
	private View headViewComm, noDataView, loadingView, noNetView,noMoreView;
	private ImageView ivOp;
	private CircleImageView userHeadIcon;
	private TextView tvNickname, tvZjInfo, tvSleepDays, tvZjIntroduce,
			tvSleepIntroTitle, userType, tvSleepLength,
			tvGetUp, tvSleepTime, tvChat;
	private LinearLayout layoutSleepData, lyoutBasic, llChat;
	private UserMessageBean userBean;
	private boolean isFinish = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onEvent(this, "636");
		setContentView(R.layout.activity_personal_info);
		user_id = getIntent().getStringExtra("user_id");
		try {
			isFinish = getIntent().getBooleanExtra("isfinish", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		user_login_id = PreManager.instance().getUserId(this);
		initTitleView();
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("UserHomepage");
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("UserHomepage");
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void initTitleView() {
		findViewById(R.id.back).setOnClickListener(this);
		if (findViewById(R.id.title_bottom) != null)
			findViewById(R.id.title_bottom).setVisibility(View.GONE);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(user_login_id.equals(user_id) ? "我的主页" : "他的主页");
	}

	private void initView() {
		// 基本信息
		lyoutBasic = (LinearLayout) findViewById(R.id.lyoutBasic);
		userHeadIcon = (CircleImageView) findViewById(R.id.userHeadIcon);
		userType = (TextView) findViewById(R.id.userType);
		tvNickname = (TextView) findViewById(R.id.tvNickname);
		tvZjInfo = (TextView) findViewById(R.id.tvZjInfo);
		tvSleepDays = (TextView) findViewById(R.id.tvSleepDays);
		ivOp = (ImageView) findViewById(R.id.ivEnterMyDetail);
		llChat = (LinearLayout) findViewById(R.id.llChat);
		llChat.setOnClickListener(this);
		tvChat = (TextView) findViewById(R.id.tvChat);

		// listView headView
		headViewComm = getLayoutInflater().inflate(
				R.layout.personal_page_head_comm, null);
		tvZjIntroduce = (TextView) headViewComm
				.findViewById(R.id.tvZjIntroduce);
		layoutSleepData = (LinearLayout) headViewComm
				.findViewById(R.id.layoutSleepData);
		tvSleepIntroTitle = (TextView) headViewComm
				.findViewById(R.id.tvSleepIntroTitle);
		tvSleepLength = (TextView) headViewComm
				.findViewById(R.id.tvSleepLength);
		tvGetUp = (TextView) headViewComm.findViewById(R.id.tvGetUp);
		tvSleepTime = (TextView) headViewComm.findViewById(R.id.tvSleepTime);
		noDataView = getLayoutInflater().inflate(
				R.layout.no_comment_data_layout, null);
		
		((ImageView) noDataView.findViewById(R.id.no_data_view)).setVisibility(View.GONE);
		TextView tvHint = (TextView) noDataView.findViewById(R.id.no_data_txt);
		if (user_id.equals(user_login_id)) {
			tvHint.setText("您还没有发布过话题");
		}else{
			tvHint.setText("该用户未发布过话题");
		}
				

		loadingView = getLayoutInflater().inflate(R.layout.listview_loading,
				null);
		noNetView = getLayoutInflater().inflate(R.layout.layout_no_net, null);

		findViewById(R.id.rl1).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (user_login_id.equals(user_id)) {
					startActivityForResult(new Intent(
							PersonalInfoActivity.this,
							MyDetailInfoActivity.class),
							Constant.MYDETAILINFO_REQUSTCODE);
				}
			}
		});
		noMoreView = getLayoutInflater().inflate(
				R.layout.listview_footer_nomore, null);
		mListView = (ListView) findViewById(R.id.lv_topices);
		mListView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		mListView.setDividerHeight(0);
		mListView.addHeaderView(loadingView, null, false);
		mListView.addHeaderView(headViewComm, null, false);
		mListView.addFooterView(noNetView, null, false);
		mListView.addFooterView(noDataView, null, false);
		mListView.addFooterView(noMoreView, null, false);
		mListView.setHeaderDividersEnabled(false);
		mListView.setDivider(null);
		mListView.setDividerHeight(0);
		mListView.setSelector(getResources().getDrawable(
				R.drawable.comm_bg_listview_item));
		topicesData = new ArrayList<ArticleBean>();
		mAdapter = new FragmentPage3FoundAdapter(PersonalInfoActivity.this,
				getScreenWidth(),user_id.equals(user_login_id) ? -1 :-1);
		mListView.setAdapter(mAdapter);
		headViewComm.setVisibility(View.GONE);
		removeFooterView();

		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int lastItem = firstVisibleItem + visibleItemCount;
				if (lastItem == totalItemCount) {
					if (mListView.getChildCount() > 0) {
						
						if (dataPage < mTotalPage) {
							dataPage += 1;
							setListState(Constant.LOADING);
							getUserMessage();
						}
					}
				}
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int realPosition = position - mListView.getHeaderViewsCount();
				if (realPosition >= 0) {
					Intent intent = new Intent(PersonalInfoActivity.this,
							CommunityTopiceDetailActivity.class);
					intent.putExtra("topices_id", topicesData.get(realPosition)
							.getTid());
					intent.putExtra("author_id", topicesData.get(realPosition)
							.getUid());
					startActivityForResult(intent, Constant.REQUSTCODE);
				}
			}
		});
		
		noNetView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				removeFooterView();
				initView();
			}
		});
		getUserMessage();
	}

	/**
	 * 获取用户信息
	 */
	private void getUserMessage() {
		UserMessageParamClass mParams = new UserMessageParamClass();
		mParams.uid = user_id;
		mParams.page = String.valueOf(dataPage);
		mParams.pagesize = "10";
		new XiangchengProcClass(PersonalInfoActivity.this).userMessage(mParams,
				new InterfaceUserMessageCallback() {

					public void onSuccess(int icode, UserMessageBean user_info,
							List<UserSleepDataBean> sleeplist,
							List<UserSleepDataBean> sleeplist2,
							List<ArticleBean> thread_list, int totalpage) {
						mTotalPage = totalpage;
						mListView.removeHeaderView(loadingView);
						if (!hasAddHead) {
							headViewComm.setVisibility(View.VISIBLE);
							hasAddHead = true;
							userBean = user_info != null ? user_info : new UserMessageBean();
							// 处理用户基本信息
							doAnalysisCallBackBasicData(user_info);
							// 处理用户睡眠信息
//							if (TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(PersonalInfoActivity.this))) {
								setSleepData(sleeplist);
//							}else {
//								setSleepData(sleeplist2);
//							}//只使用软件数据
						}

						/*if (thread_list != null && thread_list.size() > 0) {
							topicesData.addAll(thread_list);
							mAdapter.setData(topicesData);
						}
						if (topicesData.size() > 0) {
							removeFooterView();
							if (dataPage >= totalpage){
								setListState(Constant.NO_MORE);
							}
						} else {
							setListState(Constant.NO_DATA);
						}*/
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						Util.show(PersonalInfoActivity.this, strErrMsg);
						mListView.removeHeaderView(loadingView);
						if (-1 == PreManager
								.getNetType(PersonalInfoActivity.this)) {
							mListView.removeHeaderView(headViewComm);
							setListState(Constant.NO_NET);
						} else {
							/*if (topicesData.size() == 0 && hasAddHead) {
								setListState(Constant.NO_DATA);
							}*/
						}
					}
				});
	}

	private void removeFooterView() {
		mListView.removeFooterView(noDataView);
		mListView.removeFooterView(noNetView);
		mListView.removeFooterView(noMoreView);
		mListView.removeFooterView(loadingView);
	}
	
	private void setListState(int type){
		removeFooterView();
		switch (type) {
		case Constant.NO_NET:
			mListView.addFooterView(noNetView, null, false);
			break;
		case Constant.NO_DATA:
			mListView.addFooterView(noDataView, null, false);
			break;
		case Constant.NO_MORE:
			mListView.addFooterView(noMoreView, null, false);
			break;
		case Constant.LOADING:
			mListView.addFooterView(loadingView, null, false);
			break;
		default:
			break;
		}
	}

	@SuppressLint("SimpleDateFormat")
	private void doAnalysisCallBackBasicData(UserMessageBean bean) {
		lyoutBasic.setVisibility(View.VISIBLE);
		// 设置不同用户类型 展示不同信息
		if ("1".equals(bean.getIs_zj())) {
			userType.setVisibility(View.VISIBLE);
		} else {
			userType.setVisibility(View.INVISIBLE);
		}

		if (user_login_id.equals(user_id)) {
			if (!"1".equals(bean.getIs_zj())) {
				setUserTypeView(0);
			} else {
				setUserTypeView(1);
			}
		} else {
			if (!"1".equals(bean.getIs_zj())) {
				setUserTypeView(2);
			} else {
				setUserTypeView(3);
			}
		}

		ImageLoader.getInstance().displayImage(bean.getProfile(),
				bean.getProfile_key(), userHeadIcon,
				MyApplication.headPicOptn);
		tvNickname.setText(bean.getNickname().replace("ོ", ""));
		
		String sleepday = "0";
		if (bean.getSleepdays() != null) {
			if (!bean.getSleepdays().equals("null")) {
				sleepday = bean.getSleepdays();
			}
		}
		tvSleepDays.setText("香橙睡眠" + sleepday + "天");
		StringBuffer b = new StringBuffer();
		if (bean.getZhicheng() != null) {
			b.append(bean.getZhicheng());
		}
		
		if (bean.getDanwei() != null) {
			b.append(" | ");
			b.append(bean.getDanwei());
		}
		tvZjInfo.setText(b.toString());

		Drawable drawableGender = null;
		if ("01".equals(bean.getGender())) {
			drawableGender = getResources().getDrawable(R.drawable.ic_man);
			drawableGender.setBounds(0, 0, drawableGender.getMinimumWidth(),
					drawableGender.getMinimumHeight());
		} else if ("02".equals(bean.getGender())) {
			drawableGender = getResources().getDrawable(R.drawable.ic_woman);
			drawableGender.setBounds(0, 0, drawableGender.getMinimumWidth(),
					drawableGender.getMinimumHeight());
		}
		
		if (bean.getIs_zj() != null && "1".equals(bean.getIs_zj())) {
			if (!"".equals(bean.getZjintro())) {
				tvZjIntroduce.setText(bean.getZjintro());
			} else {
			}
		}else {
			tvNickname.setCompoundDrawables(null, null, drawableGender, null);
		}
	}

	/**
	 * 设置用户睡眠数据
	 * 
	 * @param sleeplist
	 */
	@SuppressLint("SimpleDateFormat")
	private void setSleepData(List<UserSleepDataBean> sleeplist) {
		if (sleeplist == null) {
			return;
		}
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmm");
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
		List<SoftDayData> sleepInfos = new ArrayList<SoftDayData>();
		int totalSleepLenght = 0;
		for (int i = 0; i < sleeplist.size(); i++) {
			UserSleepDataBean info = sleeplist.get(i);
			try {
				if(Double.parseDouble(info.getBueatysleep_duration1()) * 10 > 0){
					totalSleepLenght += Double.parseDouble(info.getBueatysleep_duration1());
					SoftDayData sleepInfo = new SoftDayData();
					sleepInfo.setDate(info.getDate());
					sleepInfo.setSleepTime(sdf2.format(sdf1.parse(info.getSleep_point())));
					sleepInfo.setGetUpTime(sdf2.format(sdf1.parse(info.getWakeup_point())));
					sleepInfos.add(sleepInfo);
				}
			} catch (Exception e) {
			}
		}

		if(sleepInfos.size() > 0){
			totalSleepLenght = totalSleepLenght/sleepInfos.size();
			String[] times = SleepUtils.getAvgSleepTimeAndGetupTime(sleepInfos);
			tvSleepTime.setText("--:--".equals(times[0]) ? "无数据" : times[0]);
			tvGetUp.setText("--:--".equals(times[1]) ? "无数据" : times[1]);
			tvSleepLength.setText(totalSleepLenght/60 + "小时"+ totalSleepLenght%60+"分");
		}else{
			tvSleepLength.setText("无数据");
			tvSleepTime.setText( "无数据" );
			tvGetUp.setText( "无数据");
		}
	}

	/**
	 * 设置信息展示类型
	 * 
	 * @param type
	 *            0-本人（普通用户） 1-本人（专家用户） 2-非本人（普通用户） 3-非本人（专家用户）
	 */
	private void setUserTypeView(int type) {
		switch (type) {
		case 0:
			ivOp.setVisibility(View.VISIBLE);
			tvSleepDays.setVisibility(View.INVISIBLE);
			llChat.setVisibility(View.GONE);

			tvZjInfo.setVisibility(View.GONE);
			tvSleepIntroTitle.setText("睡眠数据");
			tvZjIntroduce.setVisibility(View.GONE);
			layoutSleepData.setVisibility(View.VISIBLE);
			break;
		case 1:
			ivOp.setVisibility(View.VISIBLE);
			tvSleepDays.setVisibility(View.INVISIBLE);
			llChat.setVisibility(View.GONE);

			tvZjInfo.setVisibility(View.GONE);
			tvSleepIntroTitle.setText("睡眠数据");
			tvZjIntroduce.setVisibility(View.GONE);
			layoutSleepData.setVisibility(View.VISIBLE);
			break;
		case 2:
			ivOp.setVisibility(View.GONE);
			tvSleepDays.setVisibility(View.VISIBLE);
			llChat.setVisibility(View.VISIBLE);
			tvChat.setText("私聊");

			tvZjInfo.setVisibility(View.GONE);
			tvSleepIntroTitle.setText("睡眠数据");
			tvZjIntroduce.setVisibility(View.GONE);
			layoutSleepData.setVisibility(View.VISIBLE);
			break;
		case 3:
			ivOp.setVisibility(View.GONE);
			tvSleepDays.setVisibility(View.INVISIBLE);
			llChat.setVisibility(View.VISIBLE);
			tvChat.setText("咨询");

			tvZjInfo.setVisibility(View.VISIBLE);
			tvSleepIntroTitle.setText("专家介绍");
			tvZjIntroduce.setVisibility(View.VISIBLE);
			layoutSleepData.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}

	/**
	 * 发消息
	 */
	private void sendMessage() {
		Intent intent = new Intent(PersonalInfoActivity.this,ChatActivity.class);
		intent.putExtra("userBean", userBean);
		startActivity(intent);
		if (isFinish) {
			AppManager.getAppManager().finishActivity(PersonalInfoActivity.class);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 我的资料修改返回
		if (requestCode == Constant.MYDETAILINFO_REQUSTCODE
				&& resultCode == Constant.MYDETAILINFO_RESULTCODE) {
			UserBasicInfoClass bean = (UserBasicInfoClass) data
					.getSerializableExtra("user_bean");
			if (bean != null && userBean != null) {
				userBean.setProfile(bean.profile);
				userBean.setProfile_key(bean.profile_key);
				userBean.setNickname(bean.nickname);
				userBean.setGender(bean.gender);
				doAnalysisCallBackBasicData(userBean);
			}
		}
		
		if (resultCode == Constant.RESULTCODE) {
			int position = data.getIntExtra("position", 0);
			topicesData.remove(position);
			mAdapter.notifyDataSetChanged();
			if (topicesData.size() == 0) {
				mListView.addFooterView(noDataView);
				noDataView.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.llChat:// 私聊
			sendMessage();
			break;
		// case R.id.ib_more:
		// showPopupWindow();
		// break;
		default:
			break;
		}
	}

}
