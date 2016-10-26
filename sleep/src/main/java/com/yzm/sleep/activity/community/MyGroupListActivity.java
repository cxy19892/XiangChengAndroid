package com.yzm.sleep.activity.community;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.adapter.MyGroupListAdapter;
import com.yzm.sleep.bean.CommunityGrougsBean;
import com.yzm.sleep.bean.CommunityGroupBean;
import com.yzm.sleep.refresh.MaterialRefreshLayout;
import com.yzm.sleep.refresh.MaterialRefreshListener;
import com.yzm.sleep.refresh.OnClickRequestListener;
import com.yzm.sleep.utils.CommunityProcClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetCommunityGroupParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetCommunityGroupCallBack;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.Util;

/**
 * 我的小组
 * 
 * @author Administrator
 * 
 */
public class MyGroupListActivity extends BaseActivity {
	private MyGroupListAdapter mAdapter;
	private Button butbreak;
	private ListView mElistview;
	private MaterialRefreshLayout mRefresh;
	private int page = 0;
	private List<CommunityGrougsBean> group_List;
	private int total_page;
	private boolean isHttpCode = true;
	private int screenWidht = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grouplist);
		group_List = new ArrayList<CommunityGrougsBean>();
		initView();
	}

	private void initView() {
		screenWidht = getIntent().getIntExtra("sw", -1);
		((TextView) findViewById(R.id.title)).setText("我的小组");
		butbreak = (Button) findViewById(R.id.back);
		butbreak.setOnClickListener(this);
		TextView textView = new TextView(MyGroupListActivity.this);
		textView.setHeight(0);
		mRefresh = (MaterialRefreshLayout) findViewById(R.id.fragment_mian_refreshGroup);
		mElistview = (ListView) findViewById(R.id.my_expanlistGroup);
		mElistview.setBackgroundResource(R.color.bg_color);
		mElistview.setDivider(null);
		mElistview.setHeaderDividersEnabled(false);
		mElistview.setDividerHeight(0);
		mElistview.addHeaderView(textView);
		mElistview.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		mAdapter = new MyGroupListAdapter(MyGroupListActivity.this,
				screenWidht, 2);
		mElistview.setAdapter(mAdapter);
		mElistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (Util.checkNetWork(MyGroupListActivity.this)) {
					int index = position - mElistview.getHeaderViewsCount();
					Intent intent = new Intent(MyGroupListActivity.this,
							TeamDetailsActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("bean", getCommunitInfo(group_List.get(index)));
					intent.putExtras(bundle);
					startActivity(intent);
				} else {
					toastMsg("请检查您的网络");
				}
			}
		});

		/**
		 * 上啦加载更多
		 */
		mElistview.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScroll(AbsListView listView, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int lastItem = firstVisibleItem + visibleItemCount;
				if (lastItem == totalItemCount) {
					if (mElistview.getChildCount() > 0) {
						View lastItemView = (View) mElistview
								.getChildAt(mElistview.getChildCount() - 1);
						if ((mElistview.getBottom()) == lastItemView
								.getBottom()) {
							if (page < total_page && isHttpCode) {
								mRefresh.addListViewState(mElistview,
										Constant.LOADING);
								isHttpCode = false;
								HttpCreateData(true);
							}
						}
					}
				}

			}

			@Override
			public void onScrollStateChanged(AbsListView listview,
					int scrollState) {
			}
		});
		/**
		 * 下啦刷新
		 */
		mRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {

			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				HttpCreateData(false);

			}
		});
		mRefresh.setOnClickRequestListener(new OnClickRequestListener() {

			@Override
			public void setRequest() {
				mRefresh.autoRefresh();
			}
		});
		butbreak.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppManager.getAppManager().finishActivity();
			}
		});
		mRefresh.autoRefresh();
	}

	/**
	 * @param bean
	 * @return
	 */
	private CommunityGroupBean getCommunitInfo(CommunityGrougsBean bean) {
		CommunityGroupBean b = new CommunityGroupBean();
		if (bean != null) {
			b.setGid(bean.getGid());
			b.setG_name(bean.getG_name());
			b.setG_ico(bean.getG_ico());
			b.setG_ico_key(bean.getG_ico_key());
		} else {
			return null;
		}
		return b;
	}

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constant.POST_GROPUSATTENTION);// 关注
		intentFilter.addAction("com.action.change.NOINTEREST_GROUP");// 取消关注
		registerReceiver(receiver, intentFilter);
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	/**
	 * 广播接收器
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Constant.POST_GROPUSATTENTION)) {
				HttpCreateData(false);
			} else if (action.equals("com.action.change.NOINTEREST_GROUP")) {
				String id = intent.getStringExtra("gid");
				for (int i = 0; i < group_List.size(); i++) {
					if (id != null) {
						if (id.equals(group_List.get(i).getGid())) {
							group_List.remove(i);
						}
					}
				}
				if (group_List.size() > 0) {
					mAdapter.setData(group_List);
				} else {
					mRefresh.addListViewState(mElistview, Constant.NO_DATA);
				}
			}
		}
	};

	private void HttpCreateData(final boolean ischd) {
		if (ischd) {
			page++;
		} else {
			page = 1;
		}
		// 请求网络之前先判断网络是否可以用
		if (!Util.checkNetWork(MyGroupListActivity.this)) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					mRefresh.finishRefresh();
					mRefresh.addListViewState(mElistview, -1);
					Util.show(MyGroupListActivity.this, "请检查您的网络");
					if (group_List.size() == 0) {
						mRefresh.addListViewState(mElistview, Constant.NO_DATA);
					}
				}
			}, 800);
			return;
		}
		GetCommunityGroupParamClass class1 = new GetCommunityGroupParamClass();
		class1.my_int_id = PreManager.instance().getUserId(
				MyGroupListActivity.this);
		class1.page = page + "";
		class1.pagesize = "10";
		new CommunityProcClass(MyGroupListActivity.this).getMyGroup(class1,
				new InterfaceGetCommunityGroupCallBack() {

					@Override
					public void onSuccess(int icode,
							List<CommunityGrougsBean> grouupList, int totalpage) {
						mRefresh.addListViewState(mElistview, -1);
						total_page = totalpage;
						mRefresh.finishRefresh();
						if (ischd) {
							group_List.addAll(grouupList);
						} else {
							group_List.clear();
							group_List.addAll(grouupList);
						}
						saveGroupData();
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						mRefresh.addListViewState(mElistview, -1);
						mRefresh.finishRefresh();
						toastMsg(strErrMsg);
					}
				});

	}

	private void saveGroupData() {
		if (group_List != null && group_List.size() > 0) {
			mAdapter.setData(group_List);
		}
		if (page >= total_page) {
			isHttpCode = false;
			if (group_List.size() > 0) {
				mRefresh.addListViewState(mElistview, Constant.NO_MORE);
			} else {
				mRefresh.addListViewState(mElistview, Constant.NO_DATA);
			}
		} else {
			isHttpCode = true;
		}

	}
}
