package com.yzm.sleep.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.doctor.ReservationDetailActivity;
import com.yzm.sleep.adapter.MyReservationAdapter;
import com.yzm.sleep.bean.ReservationBean;
import com.yzm.sleep.refresh.MaterialRefreshLayout;
import com.yzm.sleep.refresh.MaterialRefreshListener;
import com.yzm.sleep.refresh.OnClickRequestListener;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceReservationListCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.ReservationListParamClass;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengProcClass;

public class FragmentMyReservation extends Fragment {

	
	private List<ReservationBean> mReservations;
	private MyReservationAdapter mAdapter;
	private MaterialRefreshLayout refreshView;
	private ListView mListView;
	private View noDataView;
	private int dataPage = 1, totalPage = 0;
	private Activity activity;
	
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.activity_my_reservation, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		initView(view);
		mAdapter = new MyReservationAdapter(activity);
		mReservations = new ArrayList<ReservationBean>();
		mListView.setAdapter(mAdapter);
		initData();
	}


	private void initData() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				refreshView.autoRefresh();
			}
		}, 300);
	}

	private void initView(View view) {
		refreshView = (MaterialRefreshLayout) view.findViewById(R.id.pull_refreshview);

		mListView = (ListView) view.findViewById(R.id.listView);
		mListView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
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
						View lastItemView = (View) mListView
								.getChildAt(mListView.getChildCount() - 1);
						if ((mListView.getBottom()) == lastItemView.getBottom()) {
							if (dataPage < totalPage) {
								dataPage += 1;
								refreshView.addListViewState(mListView, Constant.LOADING);
								getReservationData(false);
							}
						}
					}
				}
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int iPosition = position - mListView.getHeaderViewsCount();
				Intent intent = new Intent(activity,
						ReservationDetailActivity.class);
				intent.putExtra("id", mReservations.get(iPosition).getId());
				startActivity(intent);
			}
		});

		noDataView = LayoutInflater.from(activity).inflate(
				R.layout.no_comment_data_layout, null);
		noDataView.findViewById(R.id.no_data_view).setVisibility(View.GONE);
		((TextView) noDataView.findViewById(R.id.no_data_txt)).setText("无预约记录");
		noDataView.setVisibility(View.GONE);
		mListView.addFooterView(noDataView, null, false);

		refreshView.setMaterialRefreshListener(new MaterialRefreshListener() {

			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				dataPage = 1;
				if (Util.checkNetWork(activity)) {
					getReservationData(true);
				} else {
					new Handler().postDelayed(new Runnable() {
						public void run() {
							Util.show(activity, "网络连接错误");
							removeFooterView();
							refreshView.finishRefresh();
							refreshView.addListViewState(mListView,
									Constant.NO_NET);
						}
					}, 500);
				}

			}
		});

		refreshView.setOnClickRequestListener(new OnClickRequestListener() {

			@Override
			public void setRequest() {
				refreshView.autoRefresh();
			}
		});
	}

	private void getReservationData(final boolean isRefresh) {
		ReservationListParamClass mParams = new ReservationListParamClass();
		mParams.uid = PreManager.instance().getUserId(activity);
		mParams.page = String.valueOf(dataPage);
		mParams.pagesize = "10";
		new XiangchengProcClass(activity).reservationList(
				mParams, new InterfaceReservationListCallback() {

					@Override
					public void onSuccess(int icode,
							List<ReservationBean> reserationList, int totalpage) {
						refreshView.addListViewState(mListView, -1);
						totalPage = totalpage;
						refreshView.finishRefresh();
						removeFooterView();
						if (reserationList != null && reserationList.size() > 0) {
							if (isRefresh) {
								mReservations = reserationList;
							} else {
								mReservations.addAll(reserationList);
							}
							mAdapter.setData(mReservations);
						}

						if (mReservations.size() > 0) {
							if (dataPage >= totalpage) {
								refreshView.addListViewState(mListView,
										Constant.NO_MORE);
							}
						} else {
							mListView.addFooterView(noDataView, null, false);
							noDataView.setVisibility(View.VISIBLE);
							// refreshView.addListViewState(mListView,
							// Constant.NO_DATA);
						}
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						refreshView.addListViewState(mListView, -1);
						removeFooterView();
						refreshView.finishRefresh();
						if (mReservations.size() == 0
								&& !Util.checkNetWork(activity)) {
							refreshView.addListViewState(mListView,
									Constant.NO_MORE);
						}
						Util.show(activity, strErrMsg);
					}
				});

	}

	private void removeFooterView() {
		mListView.removeFooterView(noDataView);
	}
}
