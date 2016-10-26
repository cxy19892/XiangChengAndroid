package com.yzm.sleep.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.GroupActivity;
import com.yzm.sleep.activity.LoginActivity;
import com.yzm.sleep.activity.community.TeamDetailsActivity;
import com.yzm.sleep.adapter.FragmentPage3GroupAdapter;
import com.yzm.sleep.adapter.GroupHeadViewAdapter;
import com.yzm.sleep.bean.ArticleBean;
import com.yzm.sleep.bean.CommunityGroupBean;
import com.yzm.sleep.refresh.MaterialRefreshLayout;
import com.yzm.sleep.refresh.MaterialRefreshListener;
import com.yzm.sleep.refresh.OnClickRequestListener;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.CommunityGroupListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceCommunityGroupListCallback;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengProcClass;
import com.yzm.sleep.widget.CustomListView;
import com.yzm.sleep.widget.LazyFragment;

/**
 * 小组
 * 
 * @author Administrator
 * 
 */
public class FragmentPage3Tab2Group extends LazyFragment {
	private Activity activity;
	private ListView mElistview;
	private List<CommunityGroupBean> team_List;// 香橙推荐集合
	private List<ArticleBean> hottList;
	private MaterialRefreshLayout mRefresh;
	private FragmentPage3GroupAdapter mAdapter;
	private GroupHeadViewAdapter headmAdapter;
	private CustomListView customListView;
	private int screenWidht;
	private int page = 0;
	private boolean isHttpCode = true;
	private int total_page;
	private View headerView;
//	private Button butHeadView;
	private Button but_zd;
	private RelativeLayout lin_teamName;

	@Override
	public void onAttach(Activity activity) {
		this.activity = activity;
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		screenWidht = bundle.getInt("screenWidth", 0);
		hottList = new ArrayList<ArticleBean>();
		team_List = new ArrayList<CommunityGroupBean>();
		mAdapter = new FragmentPage3GroupAdapter(activity, screenWidht);
		headmAdapter = new GroupHeadViewAdapter(activity, screenWidht,1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 加载布局，或初始化布局对象
		headerView = inflater.inflate(
				R.layout.group_headerview, null);
		return inflater.inflate(R.layout.fragment_page3_tab1_my, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView(view);
		isPrepared = true;
		lazyLoad();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("Community_Group");
		MobclickAgent.onResume(activity); 
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("Community_Group"); 
		MobclickAgent.onPause(activity);
	}
	
	private void initView(View view) {
		lin_teamName = (RelativeLayout) headerView.findViewById(R.id.lin_teamName);
		lin_teamName.setVisibility(View.GONE);
//		butHeadView = (Button) headerView.findViewById(R.id.but_more);
		customListView = (CustomListView) headerView
				.findViewById(R.id.groupHeader);
		customListView.setAdapter(headmAdapter);
		TextView nullView = new TextView(activity);
		mRefresh = (MaterialRefreshLayout) view
				.findViewById(R.id.fragment_mian_refresh);
		mElistview = (ListView) view.findViewById(R.id.my_expanlist);
		mElistview.setBackgroundResource(R.color.bg_color);
		mElistview.setDivider(null); 
		mElistview.setHeaderDividersEnabled(false);
		mElistview.setDividerHeight(0);
		mElistview.addFooterView(nullView, null, false);
		mElistview.setTag(nullView);
		mElistview.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		but_zd = (Button) view.findViewById(R.id.but_zd);
		
		customListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent;
				if (PreManager.instance().getIsLogin(activity)) {
					intent = new Intent(activity, TeamDetailsActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("bean", team_List.get(position));
					intent.putExtras(bundle);
				} else {
					intent = new Intent(activity, LoginActivity.class);
				}
				activity.startActivity(intent);
			}
		});
		/**
		 * 上啦加载更多
		 */
		mElistview.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScroll(AbsListView listView, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				//b如果没有回到头部。那么继续滚动
//				if(firstVisibleItem>0 && !mBolena){
//					mElistview.smoothScrollToPosition(0);
//				}else{
//					mBolena = true;
//				}
				
				int lastItem = firstVisibleItem + visibleItemCount;
				if (lastItem == totalItemCount) {
					if (mElistview.getChildCount() > 0) {
						View lastItemView = (View) mElistview
								.getChildAt(mElistview.getChildCount() - 1);
						if ((mElistview.getBottom()) == lastItemView
								.getBottom()) {
							if (page < total_page && isHttpCode) {
								mRefresh.addListViewState(mElistview, Constant.LOADING);
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
				switch (scrollState) {
					// 当不滚动时
				case OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
					if (hottList.size() > 0) {
						if (mElistview.getFirstVisiblePosition() >= 1 && total_page>1) {
							but_zd.setVisibility(View.VISIBLE);
						}
						// 判断滚动到顶部
						if (mElistview.getFirstVisiblePosition() == 0) {
							but_zd.setVisibility(View.GONE);
						}
					} else {
						if (Util.checkNetWork(activity)) {
							// 判断滚动到底部
							if (mElistview.getFirstVisiblePosition() >= 1 && total_page>1) {
								but_zd.setVisibility(View.VISIBLE);
							}
							// 判断滚动到顶部
							if (mElistview.getFirstVisiblePosition() == 0) {
								but_zd.setVisibility(View.GONE);
							}
						} else {
							but_zd.setVisibility(View.GONE);
						}
					}
					
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
					break;
				case OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
					break;
				default:
					break;
				}
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
		 * 更多
		 */
//		butHeadView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(activity, GroupActivity.class);
//				intent.putExtra("sw", screenWidht);
//				startActivity(intent);
//			}
//		});
		but_zd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(android.os.Build.VERSION.SDK_INT>=8){
					mElistview.smoothScrollToPosition(0);
				}else{
					mElistview.setSelection(0);
				}
			}
		});
		mRefresh.setOnClickRequestListener(new OnClickRequestListener() {
			
			@Override
			public void setRequest() {
				mRefresh.autoRefresh();
			}
		});
	}

	private void HttpCreateData(final boolean ischd) {
		if (ischd) {
			page++;
		} else {
			page = 1;
		}
		// 请求网络之前先判断网络是否可以用
		if (!Util.checkNetWork(activity)) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					mRefresh.finishRefresh();
					mRefresh.addListViewState(mElistview, -1);
					Util.show(activity, "请检查您的网络");
					if(team_List.size()==0 && hottList.size()==0){
						mRefresh.addListViewState(mElistview, Constant.NO_NET);
					}
				}
			}, 300);
			return;
		}
		CommunityGroupListParamClass communityGroupListParamClass = new CommunityGroupListParamClass();
		communityGroupListParamClass.my_int_id = PreManager.instance()
				.getUserId(activity);
		communityGroupListParamClass.page = page + "";
		communityGroupListParamClass.pagesize = "10";
		new XiangchengProcClass(activity).communityGroupList(
				communityGroupListParamClass,
				new InterfaceCommunityGroupListCallback() {

					@Override
					public void onSuccess(int icode,
							List<CommunityGroupBean> push_list,
							List<ArticleBean> hot_thread, int totalpage) {
						mRefresh.addListViewState(mElistview, -1);
						total_page = totalpage;
						mRefresh.finishRefresh();;
						daCcData(push_list, hot_thread, ischd);
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						mRefresh.addListViewState(mElistview, -1);
						mRefresh.finishRefresh();
						if (!Util.checkNetWork(activity)) {
							mAdapter.setData(hottList);
							Util.show(activity, "请检查您的网络");
							mRefresh.addListViewState(mElistview, Constant.NO_NET);
						} else {
							Util.show(activity, strErrMsg);
						}
					}
				});

	}

	private void daCcData(List<CommunityGroupBean> push_list,
			List<ArticleBean> hot_thread,boolean ischd) {
		
		if (hot_thread != null && hot_thread.size() != 0) {
			if (ischd) {
				hottList.addAll(hot_thread);
				mAdapter.setData(hottList);
			} else {
				if (hottList.size() > 0) {
					if (!hottList.get(0).getTid()
							.equals(hot_thread.get(0).getTid())) {
						hottList.clear();
						hottList.addAll(hot_thread);
						mAdapter.setData(hottList);
					}
				} else {
					hottList.clear();
					hottList.addAll(hot_thread);
					mAdapter.setData(hottList);
				}

			}
		}
		if (push_list != null && push_list.size() > 0) {
			team_List.clear();
			team_List.addAll(push_list);
			lin_teamName.setVisibility(View.VISIBLE);
			headmAdapter.setData(team_List);
		}
		if (page >= total_page) {
			isHttpCode = false;
			if (hottList.size() > 0) {
				mRefresh.addListViewState(mElistview, Constant.NO_MORE);
			}
		} else {
			isHttpCode = true;
		}
	}

	// 标志位，标志已经初始化完成。
	private boolean isPrepared;
	private boolean isFirst = true;
	private boolean isRequest = false;

	@Override
	protected void lazyLoad() {
		if (!isPrepared || !isVisible) {
			return;
		}
		if (!isRequest) {
			isRequest = true;
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() { 
					if (isFirst) {
						mElistview.addHeaderView(headerView, null, false);
						mElistview.setAdapter(mAdapter);
						mElistview.removeFooterView((View) mElistview.getTag());
						isFirst = false;
					}
					if (Util.checkNetWork(activity)) {
						mRefresh.autoRefresh();
					} else {
						new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {
								mRefresh.finishRefresh();
								mRefresh.addListViewState(mElistview, Constant.NO_NET);
							}
						}, 300);
					}
				}
			}, 100);
		}

	}
}
