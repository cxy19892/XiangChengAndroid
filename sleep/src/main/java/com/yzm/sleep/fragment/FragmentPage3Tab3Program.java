package com.yzm.sleep.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.LoginActivity;
import com.yzm.sleep.activity.community.CommunityTopiceDetailActivity;
import com.yzm.sleep.activity.community.ProgramDetailsActivity;
import com.yzm.sleep.adapter.FragmentPage3ProgramAdapter;
import com.yzm.sleep.bean.CommunityEventBean;
import com.yzm.sleep.refresh.MaterialRefreshLayout;
import com.yzm.sleep.refresh.MaterialRefreshListener;
import com.yzm.sleep.refresh.OnClickRequestListener;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.EventListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceEventListCallback;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ToastManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengProcClass;
import com.yzm.sleep.widget.LazyFragment;

/**
 * 活动
 * 
 * @author Administrator
 * 
 */
public class FragmentPage3Tab3Program extends LazyFragment {
	private Activity activity;
	private MaterialRefreshLayout mRefresh;
	private ListView mElistview;
	private int screenWidht;
	private FragmentPage3ProgramAdapter mAdapter;// 适配器
	private List<CommunityEventBean> progList;
	private int page = 0;
	private int total_page;
	private boolean isHttpCode = true;
	private View view_Nodata;

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
		progList = new ArrayList<CommunityEventBean>();
		mAdapter = new FragmentPage3ProgramAdapter(activity, screenWidht);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view_Nodata = inflater.inflate(R.layout.layout_found_notdata, null);
		return inflater.inflate(R.layout.fragment_page3_tab1_my, null);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("Community_Activity");
		MobclickAgent.onResume(activity); 
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("Community_Activity"); 
		MobclickAgent.onPause(activity);
	}	

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mRefresh = (MaterialRefreshLayout) view
				.findViewById(R.id.fragment_mian_refresh);
		TextView textView = new TextView(activity);
		textView.setHeight(0);
		mElistview = (ListView) view.findViewById(R.id.my_expanlist);
		mElistview.setDivider(null);
		mElistview.setHeaderDividersEnabled(false);
		mElistview.setFooterDividersEnabled(false);
		mElistview.setSelector(R.color.white);
		mElistview.setVerticalScrollBarEnabled(true);
		mElistview.setDividerHeight(0);
		mElistview.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		mElistview.addFooterView(textView);
		mElistview.setAdapter(mAdapter);
		mElistview.removeFooterView(textView);
		mElistview.setSelector(getResources().getDrawable(
				R.drawable.common_tab_bg));
		mElistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (Util.checkNetWork(activity)) {
					if (progList != null && progList.size() > 0) {
						if(progList.get(position).getType().equals("2")){
							if(PreManager.instance().getIsLogin(activity)){

								Intent intent2Topic = new Intent(getActivity(), CommunityTopiceDetailActivity.class);
								intent2Topic.putExtra("topices_id", progList.get(position).getUrlid());
								intent2Topic.putExtra("topices_title", "");
								if(progList.get(position).getUrltype().equals("1")){//1表示跳转到文章详情;
									intent2Topic.putExtra("is_choiceness", true);
								}else{//2表示跳转到话题详情;
									intent2Topic.putExtra("is_choiceness", false);
								}
								getActivity().startActivity(intent2Topic);
							}else
								startActivity(new Intent(activity, LoginActivity.class));
							
						}else{
							Intent intent = new Intent(activity,
									ProgramDetailsActivity.class);
							intent.putExtra("id", progList.get(position).getId());
							intent.putExtra("title", progList.get(position).getTitle());
							intent.putExtra("sw", screenWidht);
							activity.startActivity(intent);
						}
					}
				} else {
					ToastManager.getInstance(activity).show("请检查您的网络");
				}
			}
		});
		/**
		 * 下啦刷新
		 */
		mRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
			
			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				HttpProgramData(false);				
			}
		});
		
//		mRefresh.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
//
//			@Override
//			public void onRefresh() {
//				mRefresh.setRefreshing(true);
//			}
//		});
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
								HttpProgramData(true);
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
		mRefresh.setOnClickRequestListener(new OnClickRequestListener() {
			
			@Override
			public void setRequest() {
				mRefresh.autoRefresh();
//				HttpProgramData(false);
			}
		});
		isPrepared = true;
		lazyLoad();
	}

	private void HttpProgramData(final boolean ischd) {
		mElistview.removeFooterView(view_Nodata);
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
					mRefresh.addListViewState(mElistview, -1);
					mRefresh.finishRefresh();
					Util.show(activity, "请检查您的网络");
					if (progList.size() == 0) {
						mRefresh.addListViewState(mElistview, Constant.NO_NET);
					}
				}
			}, 300);
			return;
		}
		EventListParamClass handPickArticleListParamClass = new EventListParamClass();
		handPickArticleListParamClass.my_int_id = PreManager.instance()
				.getUserId(activity);
		handPickArticleListParamClass.page = page + "";
		handPickArticleListParamClass.pagesize = "10";
		new XiangchengProcClass(activity).eventList(
				handPickArticleListParamClass,
				new InterfaceEventListCallback() {

					@Override
					public void onSuccess(int icode,
							List<CommunityEventBean> list, int totalpage) {
						mRefresh.addListViewState(mElistview, -1);
						mRefresh.finishRefresh();
						if (ischd) {
							progList.addAll(list);
							listViewType(totalpage, true);
						} else {
							if (progList.size() > 0 && list != null
									&& list.size() > 0) {
									progList.clear();
									progList.addAll(list);
									listViewType(totalpage, true);
							} else {
								progList.clear();
								progList.addAll(list);
								listViewType(totalpage, true);
							}
						}
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						mRefresh.addListViewState(mElistview, -1);
						mRefresh.finishRefresh();
						mRefresh.addListViewState(mElistview, -1);
						if (!Util.checkNetWork(activity)) {
							ToastManager.getInstance(activity).show("请检查你的网络");
							mRefresh.addListViewState(mElistview,
									Constant.NO_NET);
						} else {
							if (progList != null && progList.size() > 0) {
								mElistview.addFooterView(view_Nodata);
							}
						}
					}
				});
	}

	private void listViewType(int totalpage, boolean isdeCode) {
		total_page = totalpage;

		if (progList != null && progList.size() > 0) {
			if (page >= total_page) {
				if (progList.size() > 0) {
					mRefresh.addListViewState(mElistview, Constant.NO_MORE);
				}
				isHttpCode = false;
			} else {
				isHttpCode = true;
			}
			if (isdeCode) {
				mAdapter.setData(progList);
			}
		} else {
			mRefresh.addListViewState(mElistview, -1);
			mRefresh.addListViewState(mElistview, Constant.NO_DATA);
		}

	}

	// 标志位，标志已经初始化完成。
	private boolean isPrepared;
	private boolean isFirst = true;
	private boolean isRequest = false;

	/**
	 * 每次滑动到当前页面。就会调用的方法
	 */
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
						isFirst = false;
					}
					if (Util.checkNetWork(activity)) {
						mRefresh.autoRefresh();
					} else {
						new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {
								mRefresh.addListViewState(mElistview,
										Constant.NO_NET);
								mRefresh.finishRefresh();
							}
						}, 300);
					}
				}
			}, 100);
		}

	}

}
