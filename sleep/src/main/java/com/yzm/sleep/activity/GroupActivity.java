package com.yzm.sleep.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
import com.yzm.sleep.activity.community.TeamDetailsActivity;
import com.yzm.sleep.adapter.GroupHeadViewAdapter;
import com.yzm.sleep.bean.CommunityGroupBean;
import com.yzm.sleep.refresh.MaterialRefreshLayout;
import com.yzm.sleep.refresh.MaterialRefreshListener;
import com.yzm.sleep.refresh.OnClickRequestListener;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceRecommmendMoreGroupCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.RecommmendMoreGroupParamClass;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengProcClass;

/**
 * 香橙推荐
 * 
 * @author Administrator
 * 
 */
public class GroupActivity extends BaseActivity {
	private GroupHeadViewAdapter mAdapter;
	private Button butbreak;
	private ListView mElistview;
	private MaterialRefreshLayout mRefresh;
	private int page = 0;
	private List<CommunityGroupBean> pushList;
	private int total_page;
	private boolean isHttpCode = true;
	private int screenWidht = 0;
	private int type = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grouplist);
		initView();
	}

	private void initView() {
		screenWidht = getIntent().getIntExtra("sw", -1);
		pushList = new ArrayList<CommunityGroupBean>();
		((TextView) findViewById(R.id.title)).setText("香橙推荐");
		butbreak = (Button) findViewById(R.id.back);
		butbreak.setOnClickListener(this);
		TextView textView = new TextView(GroupActivity.this);
		textView.setHeight(0);
		mRefresh = (MaterialRefreshLayout) findViewById(R.id.fragment_mian_refreshGroup);
		mElistview = (ListView) findViewById(R.id.my_expanlistGroup);
		mElistview.setBackgroundResource(R.color.bg_color);
		mElistview.setDivider(null);
		mElistview.setHeaderDividersEnabled(false);
		mElistview.setDividerHeight(0);
		mElistview.addHeaderView(textView);
		mElistview.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		mAdapter = new GroupHeadViewAdapter(GroupActivity.this, screenWidht, 2);
		mElistview.setAdapter(mAdapter);
		mElistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = null;
				if (PreManager.instance().getIsLogin(GroupActivity.this)) {
					position--;
					intent = new Intent(GroupActivity.this,
							TeamDetailsActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("bean", pushList.get(position));
					intent.putExtras(bundle);
				} else {
					intent = new Intent(GroupActivity.this, LoginActivity.class);
				}
				startActivity(intent);
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
		/**
		 * 没网络点击刷新
		 */
		mRefresh.setOnClickRequestListener(new OnClickRequestListener() {

			@Override
			public void setRequest() {
				mRefresh.autoRefresh();
			}
		});
		mRefresh.autoRefresh();
	}

	private void HttpCreateData(final boolean ischd) {
		if (ischd) {
			page++;
		} else {
			page = 1;
		}
		// 请求网络之前先判断网络是否可以用
		if (!Util.checkNetWork(GroupActivity.this)) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					mRefresh.finishRefresh();
					Util.show(GroupActivity.this, "请检查您的网络");
					if (pushList.size() == 0 && type == 1) {
						mRefresh.addListViewState(mElistview, Constant.NO_NET);
						type = 2;
					}else{
						mAdapter.setData(pushList);
					}
				}
			}, 300);
			return;
		}
		type = 1;
		RecommmendMoreGroupParamClass class1 = new RecommmendMoreGroupParamClass();
		class1.my_int_id = PreManager.instance().getUserId(GroupActivity.this);
		class1.page = page + "";
		class1.pagesize = "10";
		new XiangchengProcClass(GroupActivity.this).recommmendMoreGroup(class1,
				new InterfaceRecommmendMoreGroupCallback() {

					@Override
					public void onSuccess(int icode,
							List<CommunityGroupBean> push_list, int totalpage) {
						mRefresh.addListViewState(mElistview, -1);
						total_page = totalpage;
						mRefresh.finishRefresh();
						if (ischd) {
							pushList.addAll(push_list);
						} else {
							pushList.clear();
							pushList.addAll(push_list);
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
		if (pushList != null && pushList.size() > 0) {
			mAdapter.setData(pushList);
		}
		if (page >= total_page) {
			isHttpCode = false;
			if (pushList.size() > 0) {
				mRefresh.addListViewState(mElistview, Constant.NO_MORE);
			}
		} else {
			isHttpCode = true;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;

		default:
			break;
		}
	}
}
